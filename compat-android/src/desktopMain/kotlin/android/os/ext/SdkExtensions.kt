package android.os.ext

/** android.os.ext.SdkExtensions：桌面恒为"最新扩展版本"。 */
object SdkExtensions {
    const val R = 30
    const val S = 31
    const val S_V2 = 32
    const val TIRAMISU = 33
    const val UPSIDE_DOWN_CAKE = 34
    const val VANILLA_ICE_CREAM = 35
    const val BAKLAVA = 36
    const val AD_SERVICES = Int.MAX_VALUE

    @JvmStatic fun getExtensionVersion(extension: Int): Int = 35

    @JvmStatic
    fun getAllExtensionVersions(): Map<Int, Int> =
        mapOf(R to 35, S to 35, TIRAMISU to 35, UPSIDE_DOWN_CAKE to 35, VANILLA_ICE_CREAM to 35)
}
