package androidx.appcompat.app

import androidx.core.os.LocaleListCompat

/**
 * androidx.appcompat.app.AppCompatDelegate 桌面 stub。
 * 只做状态记录与常量提供。
 */
open class AppCompatDelegate {

    companion object {
        const val MODE_NIGHT_FOLLOW_SYSTEM = -1
        const val MODE_NIGHT_AUTO = 0
        const val MODE_NIGHT_NO = 1
        const val MODE_NIGHT_YES = 2
        const val MODE_NIGHT_AUTO_BATTERY = 3
        const val MODE_NIGHT_AUTO_TIME = 0
        const val MODE_NIGHT_UNSPECIFIED = -100

        const val FEATURE_SUPPORT_ACTION_BAR = 108
        const val FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109
        const val FEATURE_ACTION_MODE_OVERLAY = 10

        @Volatile
        private var defaultNightMode: Int = MODE_NIGHT_FOLLOW_SYSTEM

        @Volatile
        private var applicationLocales: LocaleListCompat = LocaleListCompat.EMPTY

        @JvmStatic
        fun setDefaultNightMode(mode: Int) {
            defaultNightMode = mode
        }

        @JvmStatic
        fun getDefaultNightMode(): Int = defaultNightMode

        @JvmStatic
        fun setApplicationLocales(locales: LocaleListCompat) {
            applicationLocales = locales
            val first = if (!locales.isEmpty()) locales.get(0) else null
            if (first != null) java.util.Locale.setDefault(first)
        }

        @JvmStatic
        fun getApplicationLocales(): LocaleListCompat = applicationLocales

        @JvmStatic
        fun isCompatVectorFromResourcesEnabled(): Boolean = true

        @JvmStatic
        fun setCompatVectorFromResourcesEnabled(enabled: Boolean) {}
    }
}
