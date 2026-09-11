package android.content

import android.app.NotificationManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.UserHandle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import com.ai.assistance.operit.compat.AppGlobals
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executor

/**
 * android.content.Context 桌面版。
 * open class + delegate 模式：ContextWrapper 只需覆写 delegate 即可获得全部委托行为。
 * 所有无参 getter 以 Kotlin 属性声明（迁移代码以属性语法调用为主）。
 */
open class Context {

    /** 委托目标；ContextWrapper 覆写。 */
    protected open val delegate: Context? get() = null

    // ---- 应用/包 ----
    open val applicationContext: Context
        get() = delegate?.applicationContext ?: AppGlobals.applicationContext

    open val packageName: String
        get() = delegate?.packageName ?: AppGlobals.PACKAGE_NAME

    open val packageCodePath: String
        get() = delegate?.packageCodePath ?: AppGlobals.appDir.absolutePath

    open val packageResourcePath: String
        get() = delegate?.packageResourcePath ?: AppGlobals.appDir.absolutePath

    open val applicationInfo: ApplicationInfo
        get() = delegate?.applicationInfo ?: ApplicationInfo().apply {
            packageName = AppGlobals.PACKAGE_NAME
            dataDir = AppGlobals.appDir.absolutePath
            targetSdkVersion = 35
        }

    open val classLoader: ClassLoader
        get() = delegate?.classLoader ?: javaClass.classLoader

    open val opPackageName: String
        get() = packageName

    open val attributionTag: String?
        get() = null

    open val processName: String
        get() = packageName

    open val isDeviceProtectedStorage: Boolean
        get() = false

    // ---- 目录 ----
    open val filesDir: File
        get() = delegate?.filesDir ?: AppGlobals.filesDir

    open val cacheDir: File
        get() = delegate?.cacheDir ?: AppGlobals.cacheDir

    open val dataDir: File
        get() = delegate?.dataDir ?: AppGlobals.appDir

    open val noBackupFilesDir: File
        get() = delegate?.noBackupFilesDir ?: AppGlobals.noBackupDir

    open val obbDir: File
        get() = delegate?.obbDir ?: AppGlobals.obbDir

    open val codeCacheDir: File
        get() = delegate?.codeCacheDir ?: AppGlobals.codeCacheDir

    open val externalCacheDir: File?
        get() = delegate?.externalCacheDir ?: File(AppGlobals.externalDir, "cache").apply { mkdirs() }

    open val fileStreamPath: File
        get() = filesDir

    open fun getExternalFilesDir(type: String?): File? =
        delegate?.getExternalFilesDir(type) ?: File(AppGlobals.externalDir, type ?: "").apply { mkdirs() }

    open fun getExternalFilesDirs(type: String?): Array<File?> =
        delegate?.getExternalFilesDirs(type) ?: arrayOf(getExternalFilesDir(type))

    open fun getExternalCacheDirs(): Array<File?> =
        delegate?.getExternalCacheDirs() ?: arrayOf(externalCacheDir)

    open fun getExternalMediaDirs(): Array<File> =
        delegate?.getExternalMediaDirs() ?: android.os.Environment.getExternalMediaDirs()

    open fun getObbDirs(): Array<File> = delegate?.getObbDirs() ?: arrayOf(obbDir)

    open fun getDir(name: String, mode: Int): File =
        delegate?.getDir(name, mode) ?: File(AppGlobals.appDir, "app_$name").apply { mkdirs() }

    open fun getDatabasePath(name: String): File =
        delegate?.getDatabasePath(name) ?: File(AppGlobals.databasesDir, name).also { it.parentFile?.mkdirs() }

    open fun databaseList(): Array<String> =
        delegate?.databaseList() ?: (AppGlobals.databasesDir.list() ?: emptyArray())

    open fun deleteDatabase(name: String): Boolean =
        delegate?.deleteDatabase(name) ?: getDatabasePath(name).delete()

    open fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase =
        delegate?.openOrCreateDatabase(name, mode, factory)
            ?: SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)

    // ---- 文件 ----
    open fun openFileInput(name: String): FileInputStream =
        delegate?.openFileInput(name) ?: FileInputStream(File(filesDir, name))

    open fun openFileOutput(name: String, mode: Int): FileOutputStream {
        delegate?.let { return it.openFileOutput(name, mode) }
        val f = File(filesDir, name)
        f.parentFile?.mkdirs()
        return FileOutputStream(f, mode == MODE_APPEND)
    }

    open fun deleteFile(name: String): Boolean =
        delegate?.deleteFile(name) ?: File(filesDir, name).delete()

    open fun fileList(): Array<String> =
        delegate?.fileList() ?: (filesDir.list() ?: emptyArray())

    // ---- 框架服务 ----
    open val packageManager: PackageManager
        get() = delegate?.packageManager ?: AppGlobals.packageManager

    open val assets: AssetManager
        get() = delegate?.assets ?: AppGlobals.assets

    open val resources: Resources
        get() = delegate?.resources ?: AppGlobals.resources

    open val contentResolver: ContentResolver
        get() = delegate?.contentResolver ?: AppGlobals.contentResolver

    open val display: Display?
        get() = delegate?.display ?: AppGlobals.display

    open val displayAdjustments: Any
        get() = Any()

    open val mainLooper: Looper
        get() = delegate?.mainLooper ?: Looper.getMainLooper()

    open val mainExecutor: Executor
        get() = delegate?.mainExecutor ?: Executor { r -> r.run() }

    open val theme: Resources.Theme
        get() = delegate?.theme ?: resources.newTheme()

    open fun setTheme(themeResId: Int) { delegate?.setTheme(themeResId) }

    open val wallpaper: Drawable?
        get() = null

    open fun getSystemService(name: String): Any? =
        delegate?.getSystemService(name) ?: AppGlobals.systemService(name)

    @Suppress("UNCHECKED_CAST")
    open fun <T : Any> getSystemService(serviceClass: Class<T>): T? =
        delegate?.getSystemService(serviceClass) ?: AppGlobals.systemService(serviceClass)

    open fun getSystemServiceName(serviceClass: Class<*>): String? =
        delegate?.getSystemServiceName(serviceClass) ?: when (serviceClass) {
            ClipboardManager::class.java -> CLIPBOARD_SERVICE
            NotificationManager::class.java -> NOTIFICATION_SERVICE
            else -> null
        }

    // ---- 资源便捷 ----
    open fun getString(resId: Int): String =
        delegate?.getString(resId) ?: resources.getString(resId)

    open fun getString(resId: Int, vararg formatArgs: Any?): String =
        delegate?.getString(resId, *formatArgs) ?: resources.getString(resId, *formatArgs)

    open fun getText(resId: Int): CharSequence =
        delegate?.getText(resId) ?: resources.getText(resId)

    open fun getColor(id: Int): Int =
        delegate?.getColor(id) ?: resources.getColor(id, null)

    open fun getColorStateList(id: Int): ColorStateList =
        delegate?.getColorStateList(id) ?: resources.getColorStateList(id, null)

    open fun getDrawable(id: Int): Drawable? =
        delegate?.getDrawable(id) ?: resources.getDrawable(id, null)

    open fun obtainStyledAttributes(set: IntArray): TypedArray = TypedArray.EMPTY

    open fun obtainStyledAttributes(attrs: AttributeSet?, set: IntArray): TypedArray = TypedArray.EMPTY

    open fun obtainStyledAttributes(set: IntArray, defStyleAttr: Int, defStyleRes: Int): TypedArray = TypedArray.EMPTY

    open fun obtainStyledAttributes(
        attrs: AttributeSet?, set: IntArray, defStyleAttr: Int, defStyleRes: Int,
    ): TypedArray = TypedArray.EMPTY

    // ---- 偏好 ----
    open fun getSharedPreferences(name: String, mode: Int): SharedPreferences =
        delegate?.getSharedPreferences(name, mode) ?: SharedPreferencesImpl.getInstance(name)

    open fun getDefaultSharedPreferences(): SharedPreferences =
        getSharedPreferences(packageName + "_preferences", MODE_PRIVATE)

    open fun moveSharedPreferencesFrom(sourceContext: Context, name: String): Boolean = false

    open fun deleteSharedPreferences(name: String): Boolean {
        delegate?.let { return it.deleteSharedPreferences(name) }
        return File(AppGlobals.prefsDir, "$name.properties").delete()
    }

    // ---- 组件 ----
    open fun startActivity(intent: Intent) {
        delegate?.let { it.startActivity(intent); return }
        launchIntent(intent)
    }

    open fun startActivity(intent: Intent, options: Bundle?) = startActivity(intent)

    open fun startActivities(intents: Array<out Intent>) {
        intents.forEach { startActivity(it) }
    }

    private fun launchIntent(intent: Intent) {
        val uri = intent.data
        try {
            when {
                intent.action == Intent.ACTION_VIEW && uri != null &&
                    (uri.scheme == "http" || uri.scheme == "https") -> {
                    java.awt.Desktop.getDesktop().browse(java.net.URI(uri.toString()))
                }
                intent.action == Intent.ACTION_SENDTO && uri != null -> {
                    java.awt.Desktop.getDesktop().mail(java.net.URI(uri.toString()))
                }
                else -> Log.w("Context", "startActivity 无法处理: action=${intent.action} data=$uri")
            }
        } catch (t: Throwable) {
            Log.w("Context", "startActivity 失败: ${t.message}")
        }
    }

    open fun startService(service: Intent): ComponentName? {
        Log.d("Context", "startService(${service.component}) no-op")
        return service.component
    }

    open fun startForegroundService(service: Intent): ComponentName? = startService(service)

    open fun stopService(service: Intent): Boolean = true

    open fun bindService(service: Intent, conn: ServiceConnection, flags: Int): Boolean {
        Log.d("Context", "bindService(${service.component}) no-op")
        return false
    }

    open fun bindService(service: Intent, flags: Int, executor: Executor, conn: ServiceConnection): Boolean =
        bindService(service, conn, flags)

    open fun unbindService(conn: ServiceConnection) {}

    open fun sendBroadcast(intent: Intent) {
        Log.d("Context", "sendBroadcast(${intent.action}) no-op")
    }

    open fun sendBroadcast(intent: Intent, receiverPermission: String?) = sendBroadcast(intent)
    open fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?) = sendBroadcast(intent)
    open fun sendBroadcastAsUser(intent: Intent, user: UserHandle?) = sendBroadcast(intent)

    open fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter): Intent? {
        Log.d("Context", "registerReceiver(${filter.actionsSummary()}) no-op")
        return null
    }

    open fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter, flags: Int): Intent? =
        registerReceiver(receiver, filter)

    open fun registerReceiver(
        receiver: BroadcastReceiver?, filter: IntentFilter,
        broadcastPermission: String?, scheduler: Handler?,
    ): Intent? = registerReceiver(receiver, filter)

    open fun registerReceiver(
        receiver: BroadcastReceiver?, filter: IntentFilter,
        broadcastPermission: String?, scheduler: Handler?, flags: Int,
    ): Intent? = registerReceiver(receiver, filter)

    open fun unregisterReceiver(receiver: BroadcastReceiver) {}

    // ---- 权限 ----
    open fun checkSelfPermission(permission: String): Int = PackageManager.PERMISSION_GRANTED
    open fun checkPermission(permission: String, pid: Int, uid: Int): Int = PackageManager.PERMISSION_GRANTED
    open fun checkCallingOrSelfPermission(permission: String): Int = PackageManager.PERMISSION_GRANTED
    open fun checkCallingPermission(permission: String): Int = PackageManager.PERMISSION_GRANTED
    open fun checkUriPermission(uri: Uri?, pid: Int, uid: Int, modeFlags: Int): Int = PackageManager.PERMISSION_GRANTED
    open fun checkUriPermissions(
        uris: List<Uri>, pid: Int, uid: Int, modeFlags: Int,
    ): Int = PackageManager.PERMISSION_GRANTED

    open fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?) {}
    open fun enforceCallingOrSelfPermission(permission: String, message: String?) {}
    open fun enforceSelfPermission(permission: String, message: String?) {}
    open fun enforceCallingPermission(permission: String, message: String?) {}

    open fun grantUriPermission(toPackage: String?, uri: Uri, modeFlags: Int) {}
    open fun revokeUriPermission(uri: Uri, modeFlags: Int) {}
    open fun revokeUriPermission(toPackage: String?, uri: Uri, modeFlags: Int) {}

    // ---- 上下文派生 ----
    open fun createDeviceProtectedStorageContext(): Context = this
    open fun createConfigurationContext(overrideConfiguration: Configuration): Context = this
    open fun createDisplayContext(display: Display): Context = this
    open fun createPackageContext(packageName: String?, flags: Int): Context = this
    open fun createContextForSplit(splitName: String?): Context = this
    open fun createAttributionContext(attributionTag: String?): Context = this
    open fun createWindowContext(type: Int, options: Bundle?): Context = this

    open fun getInstrumentationPackageName(): String = packageName

    companion object {
        const val MODE_PRIVATE = 0
        const val MODE_APPEND = 32768
        const val MODE_WORLD_READABLE = 1
        const val MODE_WORLD_WRITEABLE = 2
        const val MODE_MULTI_PROCESS = 4
        const val MODE_ENABLE_WRITE_AHEAD_LOGGING = 8
        const val MODE_NO_LOCALIZED_COLLATORS = 16

        const val BIND_AUTO_CREATE = 1
        const val BIND_DEBUG_UNBIND = 2
        const val BIND_NOT_FOREGROUND = 4
        const val BIND_ABOVE_CLIENT = 8
        const val BIND_ALLOW_OOM_MANAGEMENT = 16
        const val BIND_WAIVE_PRIORITY = 32
        const val BIND_IMPORTANT = 64
        const val BIND_ADJUST_WITH_ACTIVITY = 128
        const val BIND_EXTERNAL_SERVICE = Int.MIN_VALUE or 0x0
        const val BIND_ALLOW_ACTIVITY_STARTS = 0x00000200

        const val RECEIVER_EXPORTED = 2
        const val RECEIVER_NOT_EXPORTED = 4
        const val RECEIVER_VISIBLE_TO_INSTANT_APPS = 1

        const val CONTEXT_INCLUDE_CODE = 1
        const val CONTEXT_IGNORE_SECURITY = 2
        const val CONTEXT_RESTRICTED = 4

        const val POWER_SERVICE = "power"
        const val WINDOW_SERVICE = "window"
        const val LAYOUT_INFLATER_SERVICE = "layout_inflater"
        const val ACCOUNT_SERVICE = "account"
        const val ACTIVITY_SERVICE = "activity"
        const val ALARM_SERVICE = "alarm"
        const val NOTIFICATION_SERVICE = "notification"
        const val ACCESSIBILITY_SERVICE = "accessibility"
        const val CAPTIONING_SERVICE = "captioning"
        const val KEYGUARD_SERVICE = "keyguard"
        const val LOCATION_SERVICE = "location"
        const val SEARCH_SERVICE = "search"
        const val SENSOR_SERVICE = "sensor"
        const val STORAGE_SERVICE = "storage"
        const val STORAGE_STATS_SERVICE = "storagestats"
        const val WALLPAPER_SERVICE = "wallpaper"
        const val VIBRATOR_SERVICE = "vibrator"
        const val VIBRATOR_MANAGER_SERVICE = "vibrator_manager"
        const val CONNECTIVITY_SERVICE = "connectivity"
        const val WIFI_SERVICE = "wifi"
        const val WIFI_AWARE_SERVICE = "wifiaware"
        const val WIFI_P2P_SERVICE = "wifip2p"
        const val NSD_SERVICE = "servicediscovery"
        const val AUDIO_SERVICE = "audio"
        const val FINGERPRINT_SERVICE = "fingerprint"
        const val BIOMETRIC_SERVICE = "biometric"
        const val MEDIA_PROJECTION_SERVICE = "media_projection"
        const val MEDIA_SESSION_SERVICE = "media_session"
        const val MEDIA_ROUTER_SERVICE = "media_router"
        const val TELEPHONY_SERVICE = "phone"
        const val TELEPHONY_SUBSCRIPTION_SERVICE = "telephony_subscription_service"
        const val CARRIER_CONFIG_SERVICE = "carrier_config"
        const val CLIPBOARD_SERVICE = "clipboard"
        const val INPUT_METHOD_SERVICE = "input_method"
        const val TEXT_SERVICES_MANAGER_SERVICE = "textservices"
        const val APP_OPS_SERVICE = "appops"
        const val JOB_SCHEDULER_SERVICE = "jobscheduler"
        const val DEVICE_POLICY_SERVICE = "device_policy"
        const val UI_MODE_SERVICE = "uimode"
        const val DOWNLOAD_SERVICE = "download"
        const val BATTERY_SERVICE = "batterymanager"
        const val NFC_SERVICE = "nfc"
        const val BLUETOOTH_SERVICE = "bluetooth"
        const val USB_SERVICE = "usb"
        const val HARDWARE_PROPERTIES_SERVICE = "hardware_properties"
        const val PRINT_SERVICE = "print"
        const val COMPANION_DEVICE_SERVICE = "companiondevice"
        const val INPUT_SERVICE = "input"
        const val DISPLAY_SERVICE = "display"
        const val USER_SERVICE = "user"
        const val LAUNCHER_APPS_SERVICE = "launcherapps"
        const val RESTRICTIONS_SERVICE = "restrictions"
        const val APP_SEARCH_SERVICE = "app_search"
        const val USAGE_STATS_SERVICE = "usagestats"
        const val CAMERA_SERVICE = "camera"
        const val MEDIA_METRICS_SERVICE = "media_metrics"
        const val DROPBOX_SERVICE = "dropbox"
        const val SHORTCUT_SERVICE = "shortcut"
        const val SYSTEM_HEALTH_SERVICE = "systemhealth"
        const val MIDI_SERVICE = "midi"
        const val ROLE_SERVICE = "role"
        const val NOTIFICATION_BUBBLE_SERVICE = "bubble"
        const val TV_INPUT_SERVICE = "tv_input"
        const val WINDOW_MANAGER_SERVICE = "window"
    }
}

/** android.content.ContextWrapper。 */
open class ContextWrapper(base: Context?) : Context() {
    private var base: Context? = base

    /** 真实 Android 中 getBaseContext() 返回平台类型 Context!；此处属性为非空类型，未 attach 时回落全局上下文。 */
    open var baseContext: Context
        get() = base ?: com.ai.assistance.operit.compat.AppGlobals.applicationContext
        set(value) { base = value }

    fun attachBaseContext(base: Context?) {
        this.base = base
    }

    override val delegate: Context?
        get() = base ?: com.ai.assistance.operit.compat.AppGlobals.applicationContext
}

/** android.content.ContextThemeWrapper。 */
open class ContextThemeWrapper : ContextWrapper {
    private var themeResId: Int = 0
    private var themeInstance: Resources.Theme? = null

    constructor() : super(null)
    constructor(base: Context?) : super(base)
    constructor(base: Context?, themeResId: Int) : super(base) {
        this.themeResId = themeResId
    }

    override fun setTheme(themeResId: Int) {
        this.themeResId = themeResId
        themeInstance = null
    }

    override val theme: Resources.Theme
        get() {
            if (themeInstance == null) themeInstance = resources.newTheme()
            return themeInstance!!
        }

    fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {}

    fun getThemeResId(): Int = themeResId
}
