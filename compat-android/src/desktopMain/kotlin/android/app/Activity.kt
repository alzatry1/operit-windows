package android.app

import android.content.ComponentCallbacks2
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentSender
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.ai.assistance.operit.compat.AppGlobals

/**
 * android.app.Activity 桌面版（B1b-1 新建）。
 * 单窗口桌面进程模型：生命周期方法为 open 钩子，由 androidx.activity.ComponentActivity
 * 与桌面入口驱动调用；finish() 仅记录状态，不真正退出进程。
 */
open class Activity : ContextWrapper(null), ComponentCallbacks2 {

    @Volatile private var finished = false
    @Volatile private var destroyed = false
    private var mResultCode = RESULT_CANCELED
    private var mResultData: Intent? = null
    private var titleText: CharSequence? = null

    protected val mainHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /** 对应 getIntent()/setIntent()。 */
    open var intent: Intent = Intent()

    /** 对应 get/setRequestedOrientation()。 */
    open var requestedOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    open val isFinishing: Boolean get() = finished
    open val isDestroyed: Boolean get() = destroyed

    open val application: Application
        get() = applicationContext as? Application ?: AppGlobals.applicationContext as Application

    open val window: Window by lazy { Window(this) }

    open val windowManager: WindowManager get() = window.windowManager

    open val localClassName: String get() = javaClass.simpleName

    open val componentName: ComponentName get() = ComponentName(packageName, javaClass.name)

    open val callingActivity: ComponentName? get() = null

    open val callingPackage: String? get() = null

    open val taskId: Int get() = 1

    open val isTaskRoot: Boolean get() = true

    open val title: CharSequence? get() = titleText

    open val referrer: android.net.Uri? get() = null

    // ---- 生命周期钩子（protected open，与 Android 一致） ----
    protected open fun onCreate(savedInstanceState: Bundle?) {}

    protected open fun onStart() {}

    protected open fun onResume() {}

    protected open fun onPause() {}

    protected open fun onStop() {}

    protected open fun onDestroy() {}

    protected open fun onRestart() {}

    protected open fun onPostCreate(savedInstanceState: Bundle?) {}

    protected open fun onPostResume() {}

    protected open fun onSaveInstanceState(outState: Bundle) {}

    protected open fun onRestoreInstanceState(savedInstanceState: Bundle) {}

    protected open fun onNewIntent(intent: Intent) {}

    protected open fun onUserLeaveHint() {}

    /** 供桌面入口/子类驱动生命周期的公开门面。 */
    open fun performCreate(savedInstanceState: Bundle?) {
        onCreate(savedInstanceState)
        onPostCreate(savedInstanceState)
    }

    open fun performStart() {
        onStart()
    }

    open fun performResume() {
        onResume()
        onPostResume()
    }

    open fun performPause() {
        onPause()
    }

    open fun performStop() {
        onStop()
    }

    open fun performDestroy() {
        destroyed = true
        onDestroy()
    }

    open fun dispatchConfigurationChanged(newConfig: Configuration) {
        onConfigurationChanged(newConfig)
    }

    // ---- ComponentCallbacks2 ----
    override fun onConfigurationChanged(newConfig: Configuration) {}

    override fun onLowMemory() {}

    override fun onTrimMemory(level: Int) {}

    // ---- 结果回传 ----
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

    @Deprecated("Deprecated in Java")
    open fun onBackPressed() {
        finish()
    }

    open fun onActivityReenter(resultCode: Int, data: Intent?) {}

    // ---- 启动/结果 ----
    open fun startActivityForResult(intent: Intent, requestCode: Int) {
        Log.d("Activity", "startActivityForResult($requestCode, $intent) 桌面无系统界面，不回调")
    }

    open fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) =
        startActivityForResult(intent, requestCode)

    open fun startIntentSenderForResult(
        intentSender: IntentSender?, requestCode: Int, fillInIntent: Intent?,
        flagsMask: Int, flagsValues: Int, extraFlags: Int,
    ) {
        Log.d("Activity", "startIntentSenderForResult($requestCode) no-op")
    }

    open fun startIntentSenderForResult(
        intentSender: IntentSender?, requestCode: Int, fillInIntent: Intent?,
        flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?,
    ) = startIntentSenderForResult(intentSender, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags)

    open fun startActivityIfNeeded(intent: Intent, requestCode: Int): Boolean = false
    open fun startActivityIfNeeded(intent: Intent, requestCode: Int, options: Bundle?): Boolean = false
    open fun startNextMatchingActivity(intent: Intent): Boolean = false
    open fun startNextMatchingActivity(intent: Intent, options: Bundle?): Boolean = false
    open fun startActivityFromChild(child: Activity, intent: Intent, requestCode: Int) =
        startActivityForResult(intent, requestCode)
    open fun startActivityFromChild(child: Activity, intent: Intent, requestCode: Int, options: Bundle?) =
        startActivityForResult(intent, requestCode, options)

    /** 立即授予全部权限并异步回调（桌面无运行时权限模型）。 */
    open fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        val grants = IntArray(permissions.size) { PackageManager.PERMISSION_GRANTED }
        mainHandler.post {
            try {
                onRequestPermissionsResult(requestCode, permissions, grants)
            } catch (t: Throwable) {
                Log.e("Activity", "onRequestPermissionsResult 回调异常", t)
            }
        }
    }

    open fun shouldShowRequestPermissionRationale(permission: String): Boolean = false

    // ---- 结束 ----
    open fun finish() {
        finished = true
        Log.d("Activity", "finish()（桌面仅标记状态）")
    }

    open fun finishAffinity() {
        finish()
    }

    open fun finishAfterTransition() = finish()

    open fun finishAndRemoveTask() = finish()

    open fun finishFromChild(child: Activity?) = finish()

    open fun setResult(resultCode: Int) {
        this.mResultCode = resultCode
    }

    open fun setResult(resultCode: Int, data: Intent?) {
        this.mResultCode = resultCode
        this.mResultData = data
    }

    fun getResultCode(): Int = mResultCode
    fun getResultData(): Intent? = mResultData

    // ---- UI 线程 ----
    open fun runOnUiThread(action: Runnable) {
        mainHandler.post(action)
    }

    // ---- View 宿主（Compose 桌面版不走 View 层级，全部 no-op） ----
    open fun setContentView(layoutResID: Int) {}

    open fun setContentView(view: View?) {}

    @Suppress("UNCHECKED_CAST")
    open fun <T : View?> findViewById(id: Int): T? = null

    open fun takeKeyEvents(get: Boolean) {}

    open fun setTitle(title: CharSequence?) {
        titleText = title
    }

    open fun setTitle(titleId: Int) {
        titleText = if (titleId != 0) getString(titleId) else null
    }

    open fun setTitleColor(textColor: Int) {}

    open fun setTaskDescription(taskDescription: ActivityManager.RecentTaskInfo?) {}

    open fun hasWindowFocus(): Boolean = true

    open fun onWindowFocusChanged(hasFocus: Boolean) {}

    open fun isChangingConfigurations(): Boolean = false

    open fun isInMultiWindowMode(): Boolean = false

    open fun isInPictureInPictureMode(): Boolean = false

    open fun isLocalVoiceInteractionSupported(): Boolean = false

    open fun isVoiceInteraction(): Boolean = false

    open fun getLastNonConfigurationInstance(): Any? = null

    open fun onRetainNonConfigurationInstance(): Any? = null

    open fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {}

    open fun overrideActivityTransition(overrideType: Int, enterAnim: Int, exitAnim: Int) {}

    open fun moveTaskToBack(nonRoot: Boolean): Boolean = false

    open fun recreate() {
        Log.d("Activity", "recreate() no-op")
    }

    open fun reportFullyDrawn() {}

    open fun setVisible(visible: Boolean) {}

    open fun setShowWhenLocked(showWhenLocked: Boolean) {}

    open fun setTurnScreenOn(turnScreenOn: Boolean) {}

    open fun setInheritShowWhenLocked(inheritShowWhenLocked: Boolean) {}

    open fun setVrModeEnabled(enabled: Boolean, requestedComponent: ComponentName) {}

    open fun openOptionsMenu() {}

    open fun closeOptionsMenu() {}

    open fun registerForContextMenu(view: View?) {}

    open fun unregisterForContextMenu(view: View?) {}

    open fun openContextMenu(view: View?) {}

    open fun closeContextMenu() {}

    open fun invalidateOptionsMenu() {}

    open fun onMenuOpened(featureId: Int, menu: Any?): Boolean = true

    open fun setProgressBarVisibility(visible: Boolean) {}

    open fun setProgressBarIndeterminateVisibility(visible: Boolean) {}

    open fun setProgressBarIndeterminate(indeterminate: Boolean) {}

    open fun setProgress(progress: Int) {}

    open fun setSecondaryProgress(secondaryProgress: Int) {}

    open fun setVolumeControlStream(streamType: Int) {}

    open fun getVolumeControlStream(): Int = android.media.AudioManager.STREAM_MUSIC

    open fun onWindowAttributesChanged(params: WindowManager.LayoutParams) {}

    open fun onWindowStartingActionMode(callback: Any?): Any? = null

    open fun onProvideKeyboardShortcuts(data: List<Any?>?, menu: Any?, deviceId: Int) {}

    companion object {
        const val RESULT_CANCELED = 0
        const val RESULT_OK = -1
        const val RESULT_FIRST_USER = 1

        const val DEFAULT_KEYS_DISABLE = 0
        const val DEFAULT_KEYS_DIALER = 1
        const val DEFAULT_KEYS_SHORTCUT = 2
        const val DEFAULT_KEYS_SEARCH_LOCAL = 3
        const val DEFAULT_KEYS_SEARCH_GLOBAL = 4

        const val OVERRIDE_TRANSITION_OPEN = 0
        const val OVERRIDE_TRANSITION_CLOSE = 1

        @JvmField val FOCUSED_STATE_SET = intArrayOf(0x0101009c)
    }
}
