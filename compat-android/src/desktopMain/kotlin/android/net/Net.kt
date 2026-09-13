package android.net

import android.os.Parcel
import android.os.Parcelable

/** android.net.UriMatcher：支持 #（数字段）与 *（通配）。 */
class UriMatcher(private val code: Int = NO_MATCH) {
    private val children = LinkedHashMap<String, UriMatcher>()
    private var matchCode = NO_MATCH

    fun addURI(authority: String?, path: String?, code: Int) {
        var node = rootFor(authority)
        if (path != null) {
            val segments = path.trim('/').split('/').filter { it.isNotEmpty() }
            for (seg in segments) {
                node = node.children.getOrPut(seg) { UriMatcher() }
            }
        }
        node.matchCode = code
    }

    private val roots = LinkedHashMap<String, UriMatcher>()

    private fun rootFor(authority: String?): UriMatcher =
        roots.getOrPut(authority ?: "") { UriMatcher() }

    fun match(uri: Uri?): Int {
        if (uri == null) return NO_MATCH
        var node = roots[uri.authority ?: ""] ?: return NO_MATCH
        val segments = uri.pathSegments
        if (segments.isEmpty()) return if (node.matchCode != NO_MATCH) node.matchCode else NO_MATCH
        for (seg in segments) {
            val next = node.children[seg]
                ?: node.children.entries.firstOrNull { it.key == "#" && seg.toLongOrNull() != null }?.value
                ?: node.children["*"]
                ?: return NO_MATCH
            node = next
        }
        return node.matchCode
    }

    companion object {
        const val NO_MATCH = -1
    }
}

/** android.net.Network 轻 stub。 */
class Network(val netId: Int = 0) : Parcelable {
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeInt(netId) }
    override fun equals(other: Any?): Boolean = other is Network && other.netId == netId
    override fun hashCode(): Int = netId
    override fun toString(): String = "Network($netId)"
}

/** android.net.NetworkInfo：桌面恒连网。 */
class NetworkInfo(
    private val type: Int = ConnectivityManager.TYPE_WIFI,
    private val subtype: Int = 0,
    private val typeName: String = "WIFI",
    private val subtypeName: String = "",
) : Parcelable {
    enum class State { CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN }
    enum class DetailedState {
        IDLE, SCANNING, CONNECTING, AUTHENTICATING, OBTAINING_IPADDR,
        CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, FAILED, BLOCKED, VERIFYING_POOR_LINK, CAPTIVE_PORTAL_CHECK
    }

    fun getType(): Int = type
    fun getSubtype(): Int = subtype
    fun getTypeName(): String = typeName
    fun getSubtypeName(): String = subtypeName
    fun isConnected(): Boolean = true
    fun isConnectedOrConnecting(): Boolean = true
    fun isAvailable(): Boolean = true
    fun getState(): State = State.CONNECTED
    fun getDetailedState(): DetailedState = DetailedState.CONNECTED
    fun isFailover(): Boolean = false
    fun isRoaming(): Boolean = false
    fun getReason(): String? = null
    fun getExtraInfo(): String? = null
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeInt(type) }
    override fun toString(): String = "NetworkInfo: type=$typeName, state=CONNECTED"
}

/** android.net.NetworkCapabilities 轻 stub。 */
class NetworkCapabilities : Parcelable {
    fun hasTransport(transportType: Int): Boolean = true
    fun hasCapability(capability: Int): Boolean = true
    fun getLinkDownstreamBandwidthKbps(): Int = 100_000
    fun getLinkUpstreamBandwidthKbps(): Int = 100_000
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val TRANSPORT_CELLULAR = 0
        const val TRANSPORT_WIFI = 1
        const val TRANSPORT_BLUETOOTH = 2
        const val TRANSPORT_ETHERNET = 3
        const val TRANSPORT_VPN = 4
        const val TRANSPORT_WIFI_AWARE = 5
        const val TRANSPORT_LOWPAN = 6
        const val TRANSPORT_USB = 7
        const val NET_CAPABILITY_INTERNET = 12
        const val NET_CAPABILITY_NOT_METERED = 11
        const val NET_CAPABILITY_NOT_VPN = 15
        const val NET_CAPABILITY_VALIDATED = 16
        const val NET_CAPABILITY_CAPTIVE_PORTAL = 17
        const val NET_CAPABILITY_FOREGROUND = 19
    }
}

/** android.net.ConnectivityManager：桌面恒有网。 */
open class ConnectivityManager {
    open fun getActiveNetworkInfo(): NetworkInfo? = defaultNetworkInfo
    /** activeNetwork 属性（app 用 connectivityManager.activeNetwork）——Nova 注 */
    open val activeNetwork: Network? get() = Network(1)
    open fun getNetworkInfo(networkType: Int): NetworkInfo? = defaultNetworkInfo
    open fun getAllNetworkInfo(): Array<NetworkInfo> = arrayOf(defaultNetworkInfo)
    open fun getNetworkInfo(network: Network): NetworkInfo = defaultNetworkInfo
    open fun isActiveNetworkMetered(): Boolean = false
    /** getNetworkCapabilities：真实 Android activeNetwork 是 @Nullable Network，app 直接传。——Nova 注 */
    open fun getNetworkCapabilities(network: Network?): NetworkCapabilities = NetworkCapabilities()
    open fun getLinkProperties(network: Network?): Any? = null
    open fun getRestrictBackgroundStatus(): Int = RESTRICT_BACKGROUND_STATUS_DISABLED
    open fun registerNetworkCallback(request: Any?, callback: NetworkCallback) {}
    open fun unregisterNetworkCallback(callback: NetworkCallback) {}
    open fun registerDefaultNetworkCallback(callback: NetworkCallback) {}
    open fun unregisterNetworkCallback(callback: Any?) {}
    open fun requestNetwork(request: Any?, callback: NetworkCallback) {}
    open fun getMultipathPreference(network: Network?): Int = 0
    open fun reportBadNetwork(network: Network?) {}

    open class NetworkCallback {
        open fun onAvailable(network: Network) {}
        open fun onLosing(network: Network, maxMsToLive: Int) {}
        open fun onLost(network: Network) {}
        open fun onUnavailable() {}
        open fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {}
        open fun onLinkPropertiesChanged(network: Network, lp: Any?) {}
        open fun onBlockedStatusChanged(network: Network, blocked: Boolean) {}
    }

    companion object {
        const val TYPE_MOBILE = 0
        const val TYPE_WIFI = 1
        const val TYPE_MOBILE_MMS = 2
        const val TYPE_MOBILE_SUPL = 3
        const val TYPE_MOBILE_DUN = 4
        const val TYPE_MOBILE_HIPRI = 5
        const val TYPE_WIMAX = 6
        const val TYPE_BLUETOOTH = 7
        const val TYPE_DUMMY = 8
        const val TYPE_ETHERNET = 9
        const val TYPE_VPN = 17
        const val TYPE_NONE = -1

        const val RESTRICT_BACKGROUND_STATUS_DISABLED = 1
        const val RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2
        const val RESTRICT_BACKGROUND_STATUS_ENABLED = 3

        const val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
        const val EXTRA_NETWORK_INFO = "networkInfo"
        const val EXTRA_IS_FAILOVER = "isFailover"
        const val EXTRA_NO_CONNECTIVITY = "noConnectivity"
        const val EXTRA_REASON = "reason"

        const val DEFAULT_NETWORK_PREFERENCE = TYPE_WIFI

        val defaultNetworkInfo = NetworkInfo()

        @JvmStatic
        fun isNetworkTypeValid(networkType: Int): Boolean = networkType in 0..17

        @JvmStatic
        fun getNetworkTypeName(type: Int): String = when (type) {
            TYPE_MOBILE -> "MOBILE"
            TYPE_WIFI -> "WIFI"
            TYPE_ETHERNET -> "ETHERNET"
            TYPE_BLUETOOTH -> "BLUETOOTH"
            TYPE_VPN -> "VPN"
            else -> "UNKNOWN"
        }
    }
}

/** android.net.TrafficStats：stub（census 未出现，防御性补齐）。 */
object TrafficStats {
    const val UNSUPPORTED = -1L
    const val UID_UNKNOWN = -1
    @JvmStatic fun getTotalRxBytes(): Long = 0
    @JvmStatic fun getTotalTxBytes(): Long = 0
    @JvmStatic fun getMobileRxBytes(): Long = 0
    @JvmStatic fun getMobileTxBytes(): Long = 0
    @JvmStatic fun getUidRxBytes(uid: Int): Long = 0
    @JvmStatic fun getUidTxBytes(uid: Int): Long = 0
    @JvmStatic fun setThreadStatsTag(tag: Int) {}
    @JvmStatic fun clearThreadStatsTag() {}
    @JvmStatic fun getThreadStatsTag(): Int = -1
    @JvmStatic fun tagSocket(socket: java.net.Socket?) {}
    @JvmStatic fun untagSocket(socket: java.net.Socket?) {}
}
