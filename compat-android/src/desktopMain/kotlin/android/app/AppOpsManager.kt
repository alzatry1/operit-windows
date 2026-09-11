package android.app

/**
 * android.app.AppOpsManager 编译级 stub（P3-B4）。
 * 桌面无 ops 服务；检查一律返回 MODE_ALLOWED（最宽松策略，与
 * "永远最新且无权限限制的设备" 原则一致）。
 */
open class AppOpsManager {

    open fun checkOpNoThrow(op: String, uid: Int, packageName: String): Int = MODE_ALLOWED

    open fun unsafeCheckOpNoThrow(op: String, uid: Int, packageName: String): Int = MODE_ALLOWED

    open fun checkOp(op: String, uid: Int, packageName: String): Int = MODE_ALLOWED

    open fun noteOp(op: String, uid: Int, packageName: String): Int = MODE_ALLOWED

    open fun noteProxyOp(op: String, proxiedPackageName: String): Int = MODE_ALLOWED

    open fun startWatchingMode(op: String?, packageName: String?, callback: Any?) {}

    open fun stopWatchingMode(callback: Any?) {}

    companion object {
        const val MODE_ALLOWED = 0
        const val MODE_IGNORED = 1
        const val MODE_ERRORED = 2
        const val MODE_DEFAULT = 3
        const val MODE_FOREGROUND = 4

        const val OPSTR_GET_USAGE_STATS = "android:get_usage_stats"
        const val OPSTR_SYSTEM_ALERT_WINDOW = "android:system_alert_window"
        const val OPSTR_WRITE_SETTINGS = "android:write_settings"
        const val OPSTR_PICTURE_IN_PICTURE = "android:picture_in_picture"
        const val OPSTR_MANAGE_EXTERNAL_STORAGE = "android:manage_external_storage"
    }
}
