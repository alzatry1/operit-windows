package android.os

/**
 * android.os.Build：桌面版恒为"最新设备"（SDK 35），消除所有版本分支死代码。
 */
class Build {
    companion object {
        const val UNKNOWN = "unknown"
        const val ID = "11"
        const val DISPLAY = "Windows 11"
        const val PRODUCT = "windows_x86_64"
        const val DEVICE = "windows"
        const val BOARD = "windows"
        const val MANUFACTURER = "Microsoft"
        const val BRAND = "Microsoft"
        const val MODEL = "Windows"
        const val BOOTLOADER = "unknown"
        const val RADIO = "unknown"
        const val HARDWARE = "x86_64"
        const val SERIAL = "unknown"
        const val CPU_ABI = "x86_64"
        const val CPU_ABI2 = ""
        const val TYPE = "user"
        const val TAGS = "release-keys"
        const val FINGERPRINT = "Microsoft/Windows/11:35/windows/11:user/release-keys"
        const val TIME = 0L
        const val USER = "windows"
        const val HOST = "windows"
        const val IS_DEBUGGABLE = false
        const val IS_ENG = false
        const val IS_USERDEBUG = false
        const val IS_USER = true

        @JvmField val SUPPORTED_ABIS: Array<String> = arrayOf("x86_64")
        @JvmField val SUPPORTED_32_BIT_ABIS: Array<String> = emptyArray()
        @JvmField val SUPPORTED_64_BIT_ABIS: Array<String> = arrayOf("x86_64")

        @JvmStatic fun getSerial(): String = SERIAL
        @JvmStatic fun getRadioVersion(): String? = null
        @JvmStatic fun getFingerprintedPartitions(): List<Any> = emptyList()
    }

    object VERSION {
        const val INCREMENTAL = "1"
        const val RELEASE = "11"
        const val RELEASE_OR_CODENAME = "11"
        const val RELEASE_OR_PREVIEW_DISPLAY = "11"
        const val BASE_OS = ""
        const val SECURITY_PATCH = "2026-09-01"
        const val MEDIA_PERFORMANCE_CLASS = 0
        const val SDK = "35"
        const val SDK_INT = 35
        const val CODENAME = "REL"
        const val PREVIEW_SDK_INT = 0
        const val DEVICE_INITIAL_SDK_INT = 35
    }

    object VERSION_CODES {
        const val BASE = 1
        const val BASE_1_1 = 2
        const val CUPCAKE = 3
        const val DONUT = 4
        const val ECLAIR = 5
        const val ECLAIR_0_1 = 6
        const val ECLAIR_MR1 = 7
        const val FROYO = 8
        const val GINGERBREAD = 9
        const val GINGERBREAD_MR1 = 10
        const val HONEYCOMB = 11
        const val HONEYCOMB_MR1 = 12
        const val HONEYCOMB_MR2 = 13
        const val ICE_CREAM_SANDWICH = 14
        const val ICE_CREAM_SANDWICH_MR1 = 15
        const val JELLY_BEAN = 16
        const val JELLY_BEAN_MR1 = 17
        const val JELLY_BEAN_MR2 = 18
        const val KITKAT = 19
        const val KITKAT_WATCH = 20
        const val LOLLIPOP = 21
        const val LOLLIPOP_MR1 = 22
        const val M = 23
        const val N = 24
        const val N_MR1 = 25
        const val O = 26
        const val O_MR1 = 27
        const val P = 28
        const val Q = 29
        const val R = 30
        const val S = 31
        const val S_V2 = 32
        const val TIRAMISU = 33
        const val UPSIDE_DOWN_CAKE = 34
        const val VANILLA_ICE_CREAM = 35
        const val BAKLAVA = 36
        const val CUR_DEVELOPMENT = 10000
    }

    class VERSION_CODES_LEGACY {
        companion object { /* 占位，AOSP 无此类 */ }
    }

    object PARTITION {
        const val PARTITION_NAME_SYSTEM = "system"
    }
}
