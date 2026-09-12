package android.telephony

/**
 * android.telephony.TelephonyManager 桌面 stub（B9n）。
 * 桌面无蜂窝基带，国家码等返回 null；app 侧已用 try/catch 兜底。——Nova 注
 */
open class TelephonyManager {
    /** 网络所属国家码（桌面无基带，null）。 */
    open val networkCountryIso: String? = null
    /** SIM 卡所属国家码（桌面无 SIM，null）。 */
    open val simCountryIso: String? = null
    /** 设备电话类型。 */
    open val phoneType: Int = PHONE_TYPE_NONE
    /** SIM 状态。 */
    open val simState: Int = SIM_STATE_ABSENT

    companion object {
        const val PHONE_TYPE_NONE = 0
        const val PHONE_TYPE_GSM = 1
        const val PHONE_TYPE_CDMA = 2
        const val SIM_STATE_ABSENT = 1
        const val SIM_STATE_READY = 5
        const val SIM_STATE_UNKNOWN = 0
    }
}
