package android.app.admin

import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * android.app.admin（P3-B2 新增，轻 stub）。
 * 桌面无设备管理器：isAdminActive=false，其余 no-op。
 */

/** android.app.admin.DevicePolicyManager。 */
class DevicePolicyManager private constructor() {

    fun isAdminActive(admin: ComponentName?): Boolean = false
    fun isDeviceOwnerApp(packageName: String?): Boolean = false
    fun isProfileOwnerApp(packageName: String?): Boolean = false
    fun lockNow() {}
    fun wipeData(flags: Int) {}
    fun removeActiveAdmin(admin: ComponentName?) {}
    fun resetPassword(password: String?, flags: Int): Boolean = false
    fun getActiveAdmins(): List<ComponentName>? = null
    fun isApplicationHidden(admin: ComponentName?, packageName: String?): Boolean = false
    fun setApplicationHidden(admin: ComponentName?, packageName: String?, hidden: Boolean): Boolean = false

    companion object {
        const val ACTION_ADD_DEVICE_ADMIN = "android.app.action.ADD_DEVICE_ADMIN"
        const val ACTION_PROVISION_MANAGED_PROFILE = "android.app.action.PROVISION_MANAGED_PROFILE"
        const val ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD"
        const val ACTION_DEVICE_ADMIN_DISABLED = "android.app.action.DEVICE_ADMIN_DISABLED"

        const val EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN"
        const val EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION"

        const val WIPE_EXTERNAL_STORAGE = 1
        const val WIPE_RESET_PROTECTION_DATA = 2

        @JvmStatic
        fun get(context: Context?): DevicePolicyManager = DevicePolicyManager()
    }
}

/** android.app.admin.DeviceAdminReceiver。 */
open class DeviceAdminReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {}

    open fun onEnabled(context: Context, intent: Intent) {}
    open fun onDisabled(context: Context, intent: Intent) {}
    open fun onPasswordChanged(context: Context, intent: Intent) {}
    open fun onPasswordFailed(context: Context, intent: Intent) {}
    open fun onPasswordSucceeded(context: Context, intent: Intent) {}

    companion object {
        const val ACTION_DEVICE_ADMIN_ENABLED = "android.app.action.DEVICE_ADMIN_ENABLED"
        const val ACTION_DEVICE_ADMIN_DISABLED = "android.app.action.DEVICE_ADMIN_DISABLED"
        const val EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN"
    }
}
