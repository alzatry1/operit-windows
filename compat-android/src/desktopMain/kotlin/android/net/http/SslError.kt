package android.net.http

/** android.net.http.SslError 轻 stub（webview 相关，B4 批次实装）。 */
open class SslError(
    private val error: Int = SSL_INVALID,
    private val certificate: Any? = null,
    val url: String = "",
) {
    val primaryError: Int get() = error
    fun getCertificate(): Any? = certificate
    fun addError(err: Int): Boolean = false
    fun hasError(err: Int): Boolean = err == error
    override fun toString(): String = "SslError{primary=$error, url=$url}"

    companion object {
        const val SSL_NOTYETVALID = 0
        const val SSL_EXPIRED = 1
        const val SSL_IDMISMATCH = 2
        const val SSL_UNTRUSTED = 3
        const val SSL_DATE_INVALID = 4
        const val SSL_INVALID = 5
        const val SSL_MAX_ERROR = 6
    }
}
