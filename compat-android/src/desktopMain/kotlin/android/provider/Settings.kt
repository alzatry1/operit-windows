package android.provider

import android.content.ContentResolver
import android.net.Uri
import java.util.concurrent.ConcurrentHashMap

/** android.provider.Settings：常量齐全；System/Global/Secure 走内存 Map。 */
class Settings private constructor() {

    object System {
        @JvmField val CONTENT_URI: Uri = Uri.parse("content://settings/system")
        @JvmField val DEFAULT_NOTIFICATION_URI: Uri = Uri.parse("content://settings/system/notification_sound")
        @JvmField val DEFAULT_RINGTONE_URI: Uri = Uri.parse("content://settings/system/ringtone")
        @JvmField val DEFAULT_ALARM_ALERT_URI: Uri = Uri.parse("content://settings/system/alarm_alert")

        private val table = ConcurrentHashMap<String, String>()

        @JvmStatic fun getString(resolver: ContentResolver?, name: String): String? = table[name]
        @JvmStatic fun putString(resolver: ContentResolver?, name: String, value: String?): Boolean {
            if (value == null) table.remove(name) else table[name] = value
            return true
        }

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String): Int =
            table[name]?.toIntOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String, def: Int): Int =
            table[name]?.toIntOrNull() ?: def

        @JvmStatic fun putInt(resolver: ContentResolver?, name: String, value: Int): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String): Long =
            table[name]?.toLongOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String, def: Long): Long =
            table[name]?.toLongOrNull() ?: def

        @JvmStatic fun putLong(resolver: ContentResolver?, name: String, value: Long): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String): Float =
            table[name]?.toFloatOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String, def: Float): Float =
            table[name]?.toFloatOrNull() ?: def

        @JvmStatic fun putFloat(resolver: ContentResolver?, name: String, value: Float): Boolean =
            putString(resolver, name, value.toString())

        const val SCREEN_BRIGHTNESS = "screen_brightness"
        const val SCREEN_OFF_TIMEOUT = "screen_off_timeout"
        const val TIME_12_24 = "time_12_24"
        const val DATE_FORMAT = "date_format"
        const val ACCELEROMETER_ROTATION = "accelerometer_rotation"
        const val FONT_SCALE = "font_scale"
        const val ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION = "android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"
        const val NEXT_ALARM_FORMATTED = "next_alarm_formatted"
        const val ALARM_ALERT = "alarm_alert"
        const val RINGTONE = "ringtone"
        const val NOTIFICATION_SOUND = "notification_sound"
    }

    object Global {
        @JvmField val CONTENT_URI: Uri = Uri.parse("content://settings/global")

        private val table = ConcurrentHashMap<String, String>()

        @JvmStatic fun getString(resolver: ContentResolver?, name: String): String? = table[name]
        @JvmStatic fun putString(resolver: ContentResolver?, name: String, value: String?): Boolean {
            if (value == null) table.remove(name) else table[name] = value
            return true
        }

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String): Int =
            table[name]?.toIntOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String, def: Int): Int =
            table[name]?.toIntOrNull() ?: def

        @JvmStatic fun putInt(resolver: ContentResolver?, name: String, value: Int): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String): Long =
            table[name]?.toLongOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String, def: Long): Long =
            table[name]?.toLongOrNull() ?: def

        @JvmStatic fun putLong(resolver: ContentResolver?, name: String, value: Long): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String): Float =
            table[name]?.toFloatOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String, def: Float): Float =
            table[name]?.toFloatOrNull() ?: def

        @JvmStatic fun putFloat(resolver: ContentResolver?, name: String, value: Float): Boolean =
            putString(resolver, name, value.toString())

        const val DEVICE_NAME = "device_name"
        const val AIRPLANE_MODE_ON = "airplane_mode_on"
        const val ADB_ENABLED = "adb_enabled"
        const val STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in"
    }

    object Secure {
        @JvmField val CONTENT_URI: Uri = Uri.parse("content://settings/secure")

        private val table = ConcurrentHashMap<String, String>()

        @JvmStatic fun getString(resolver: ContentResolver?, name: String): String? = table[name]
        @JvmStatic fun putString(resolver: ContentResolver?, name: String, value: String?): Boolean {
            if (value == null) table.remove(name) else table[name] = value
            return true
        }

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String): Int =
            table[name]?.toIntOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getInt(resolver: ContentResolver?, name: String, def: Int): Int =
            table[name]?.toIntOrNull() ?: def

        @JvmStatic fun putInt(resolver: ContentResolver?, name: String, value: Int): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String): Long =
            table[name]?.toLongOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getLong(resolver: ContentResolver?, name: String, def: Long): Long =
            table[name]?.toLongOrNull() ?: def

        @JvmStatic fun putLong(resolver: ContentResolver?, name: String, value: Long): Boolean =
            putString(resolver, name, value.toString())

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String): Float =
            table[name]?.toFloatOrNull() ?: throw SettingNotFoundException(name)

        @JvmStatic
        fun getFloat(resolver: ContentResolver?, name: String, def: Float): Float =
            table[name]?.toFloatOrNull() ?: def

        @JvmStatic fun putFloat(resolver: ContentResolver?, name: String, value: Float): Boolean =
            putString(resolver, name, value.toString())

        const val ANDROID_ID = "android_id"
        const val ENABLED_ACCESSIBILITY_SERVICES = "enabled_accessibility_services"
        const val ACCESSIBILITY_ENABLED = "accessibility_enabled"
        const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        const val DEFAULT_INPUT_METHOD = "default_input_method"
        const val ENABLED_INPUT_METHODS = "enabled_input_methods"
        const val LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed"

        @JvmStatic fun getAndroidId(context: android.content.Context): String = "operit-desktop"
    }

    class SettingNotFoundException(msg: String?) : android.util.AndroidException(msg)

    companion object {
        const val ACTION_SETTINGS = "android.settings.SETTINGS"
        const val ACTION_DISPLAY_SETTINGS = "android.settings.DISPLAY_SETTINGS"
        const val ACTION_SOUND_SETTINGS = "android.settings.SOUND_SETTINGS"
        const val ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS"
        const val ACTION_WIRELESS_SETTINGS = "android.settings.WIRELESS_SETTINGS"
        const val ACTION_BLUETOOTH_SETTINGS = "android.settings.BLUETOOTH_SETTINGS"
        const val ACTION_SECURITY_SETTINGS = "android.settings.SECURITY_SETTINGS"
        const val ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS"
        const val ACTION_INTERNAL_STORAGE_SETTINGS = "android.settings.INTERNAL_STORAGE_SETTINGS"
        const val ACTION_MEMORY_CARD_SETTINGS = "android.settings.MEMORY_CARD_SETTINGS"
        const val ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS"
        const val ACTION_APPLICATION_SETTINGS = "android.settings.APPLICATION_SETTINGS"
        const val ACTION_MANAGE_APPLICATIONS_SETTINGS = "android.settings.MANAGE_APPLICATIONS_SETTINGS"
        const val ACTION_ACCESSIBILITY_SETTINGS = "android.settings.ACCESSIBILITY_SETTINGS"
        const val ACTION_INPUT_METHOD_SETTINGS = "android.settings.INPUT_METHOD_SETTINGS"
        const val ACTION_DATE_SETTINGS = "android.settings.DATE_SETTINGS"
        const val ACTION_PRIVACY_SETTINGS = "android.settings.PRIVACY_SETTINGS"
        const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        const val ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS = "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"
        const val ACTION_USAGE_ACCESS_SETTINGS = "android.settings.USAGE_ACCESS_SETTINGS"
        const val ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION"
        const val ACTION_MANAGE_WRITE_SETTINGS = "android.settings.action.MANAGE_WRITE_SETTINGS"
        const val ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"
        const val ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS"
        const val ACTION_ADD_ACCOUNT = "android.settings.ADD_ACCOUNT"
        const val ACTION_USER_SETTINGS = "android.settings.USER_SETTINGS"
        const val ACTION_DEVICE_INFO_SETTINGS = "android.settings.DEVICE_INFO_SETTINGS"
        const val ACTION_NFC_SETTINGS = "android.settings.NFC_SETTINGS"
        const val ACTION_HOME_SETTINGS = "android.settings.HOME_SETTINGS"
        const val ACTION_APPLICATION_DEVELOPMENT_SETTINGS = "android.settings.APPLICATION_DEVELOPMENT_SETTINGS"
        const val ACTION_DEVICE_ADMIN_SETTINGS = "android.settings.DEVICE_ADMIN_SETTINGS"
        const val ACTION_VPN_SETTINGS = "android.settings.VPN_SETTINGS"
        const val ACTION_PRINT_SETTINGS = "android.settings.ACTION_PRINT_SETTINGS"
        const val ACTION_QUICK_LAUNCH_SETTINGS = "android.settings.QUICK_LAUNCH_SETTINGS"
        const val ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS"
        const val ACTION_SYNC_SETTINGS = "android.settings.SYNC_SETTINGS"
        const val ACTION_NETWORK_OPERATOR_SETTINGS = "android.settings.NETWORK_OPERATOR_SETTINGS"
        const val ACTION_DATA_USAGE_SETTINGS = "android.settings.DATA_USAGE_SETTINGS"
        const val ACTION_MANAGE_DEFAULT_APPS_SETTINGS = "android.settings.MANAGE_DEFAULT_APPS_SETTINGS"
        const val ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS"

        const val EXTRA_APP_PACKAGE = "android.provider.extra.APP_PACKAGE"
        const val EXTRA_CHANNEL_ID = "android.provider.extra.CHANNEL_ID"
        const val EXTRA_CHANNEL_FILTER_LIST = "android.provider.extra.CHANNEL_FILTER_LIST"
        const val EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME = "android.provider.extra.NOTIFICATION_LISTENER_COMPONENT_NAME"

        const val NAME_VALUE_TABLE = "name_value_table"

        /** 桌面版：始终允许悬浮窗。 */
        @JvmStatic
        fun canDrawOverlays(context: android.content.Context): Boolean = true

        @JvmStatic
        fun isSystemPackage(context: android.content.Context): Boolean = false
    }
}

/** android.provider.OpenableColumns。 */
interface OpenableColumns {
    companion object {
        const val DISPLAY_NAME = "display_name"
        const val SIZE = "size"
    }
}

/** android.provider.Browser 轻 stub。 */
object Browser {
    const val BOOKMARKS_URI = "content://browser/bookmarks"
    const val INITIAL_ZOOM_LEVEL = "browser.initialZoomLevel"

    @JvmStatic
    fun sendString(context: android.content.Context, string: String) {}
}
