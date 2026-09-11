package androidx.core.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

/**
 * androidx.core.app.ActivityCompat 桌面版。
 * 权限请求立即授予；桌面无系统权限模型。
 */
object ActivityCompat {

    @JvmStatic
    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        activity.requestPermissions(permissions, requestCode)
    }

    @JvmStatic
    fun checkSelfPermission(context: Context, permission: String): Int =
        PackageManager.PERMISSION_GRANTED

    @JvmStatic
    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean = false

    @JvmStatic
    fun finishAffinity(activity: Activity) {
        activity.finishAffinity()
    }

    @JvmStatic
    fun finishAfterTransition(activity: Activity) {
        activity.finishAfterTransition()
    }

    @JvmStatic
    fun startActivityForResult(activity: Activity, intent: Intent, requestCode: Int, options: Bundle?) {
        activity.startActivityForResult(intent, requestCode, options)
    }

    @JvmStatic
    fun startIntentSenderForResult(
        activity: Activity, intentSender: android.content.IntentSender?, requestCode: Int,
        fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?,
    ) {
        activity.startIntentSenderForResult(
            intentSender, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options,
        )
    }

    @JvmStatic
    fun setPermissionDelegate(delegate: Any?) {}

    @JvmStatic
    fun recreate(activity: Activity) {
        activity.recreate()
    }

    @JvmStatic
    fun invalidateOptionsMenu(activity: Activity) {
        activity.invalidateOptionsMenu()
    }

    /** OnRequestPermissionsResultCallback。 */
    fun interface OnRequestPermissionsResultCallback {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    }

    /** PermissionCompatDelegate 标记接口。 */
    interface PermissionCompatDelegate {
        fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean
        fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Boolean
    }
}
