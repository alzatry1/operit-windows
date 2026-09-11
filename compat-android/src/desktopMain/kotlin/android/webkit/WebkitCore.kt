package android.webkit

import android.net.Uri
import java.util.concurrent.ConcurrentHashMap

/**
 * android.webkit 核心垫片（P3-B2 新增）。
 * 桌面功能等价原则：MimeTypeMap/URLUtil 为真实实现；CookieManager 为内存表实现；
 * 其余为编译级骨架（桌面无 WebView 渲染，运行期语义由 B4 渲染宿主接管）。
 */

/** android.webkit.JavascriptInterface 注解。 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class JavascriptInterface

/** android.webkit.MimeTypeMap：真实 MIME 类型表实现。 */
class MimeTypeMap private constructor() {

    fun getMimeTypeFromExtension(extension: String?): String? {
        if (extension.isNullOrEmpty()) return null
        return MIME_TYPES[extension.lowercase().trimStart('.')]
    }

    fun hasMimeType(mimeType: String?): Boolean =
        mimeType != null && MIME_TYPES.values.contains(mimeType.lowercase())

    fun getExtensionFromMimeType(mimeType: String?): String? {
        if (mimeType.isNullOrEmpty()) return null
        val lower = mimeType.lowercase().substringBefore(';').trim()
        return MIME_TYPES.entries.firstOrNull { it.value == lower }?.key
    }

    fun hasExtension(extension: String?): Boolean =
        extension != null && MIME_TYPES.containsKey(extension.lowercase().trimStart('.'))

    companion object {
        private val singletonInstance by lazy { MimeTypeMap() }

        @JvmStatic
        fun getSingleton(): MimeTypeMap = singletonInstance

        @JvmStatic
        fun getFileExtensionFromUrl(url: String?): String {
            if (url.isNullOrEmpty()) return ""
            // 去掉 query / fragment
            var path = url
            val queryPos = path.indexOf('?')
            if (queryPos >= 0) path = path.substring(0, queryPos)
            val fragPos = path.indexOf('#')
            if (fragPos >= 0) path = path.substring(0, fragPos)
            // 取最后一段路径
            val lastSlash = path.lastIndexOf('/')
            val filename = if (lastSlash >= 0) path.substring(lastSlash + 1) else path
            val dotPos = filename.lastIndexOf('.')
            if (dotPos <= 0 || dotPos == filename.length - 1) return ""
            val ext = filename.substring(dotPos + 1).lowercase()
            // 扩展名只允许字母数字
            return if (ext.all { it.isLetterOrDigit() }) ext else ""
        }

        @JvmStatic
        fun fileExtensionFromUrl(url: String?): String = getFileExtensionFromUrl(url)

        private val MIME_TYPES: Map<String, String> = mapOf(
            // 文本
            "html" to "text/html", "htm" to "text/html", "shtml" to "text/html",
            "css" to "text/css", "js" to "text/javascript", "mjs" to "text/javascript",
            "txt" to "text/plain", "text" to "text/plain", "log" to "text/plain",
            "xml" to "text/xml", "csv" to "text/csv", "md" to "text/markdown",
            "json" to "application/json", "map" to "application/json",
            // 图片
            "png" to "image/png", "jpg" to "image/jpeg", "jpeg" to "image/jpeg",
            "gif" to "image/gif", "bmp" to "image/bmp", "webp" to "image/webp",
            "svg" to "image/svg+xml", "ico" to "image/x-icon", "tif" to "image/tiff",
            "tiff" to "image/tiff", "heic" to "image/heic", "heif" to "image/heif",
            "avif" to "image/avif",
            // 音视频
            "mp3" to "audio/mpeg", "wav" to "audio/wav", "ogg" to "audio/ogg",
            "m4a" to "audio/mp4", "aac" to "audio/aac", "flac" to "audio/flac",
            "opus" to "audio/opus", "mid" to "audio/midi",
            "mp4" to "video/mp4", "m4v" to "video/mp4", "webm" to "video/webm",
            "mkv" to "video/x-matroska", "avi" to "video/x-msvideo",
            "mov" to "video/quicktime", "flv" to "video/x-flv", "3gp" to "video/3gpp",
            "ts" to "video/mp2t",
            // 文档
            "pdf" to "application/pdf", "doc" to "application/msword",
            "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "xls" to "application/vnd.ms-excel",
            "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "ppt" to "application/vnd.ms-powerpoint",
            "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "rtf" to "application/rtf", "epub" to "application/epub+zip",
            // 压缩包
            "zip" to "application/zip", "rar" to "application/x-rar-compressed",
            "7z" to "application/x-7z-compressed", "tar" to "application/x-tar",
            "gz" to "application/gzip", "bz2" to "application/x-bzip2",
            // Android / 可执行
            "apk" to "application/vnd.android.package-archive",
            "aab" to "application/vnd.android.app-bundle",
            "exe" to "application/octet-stream", "msi" to "application/x-msdownload",
            "dmg" to "application/x-apple-diskimage", "deb" to "application/vnd.debian.binary-package",
            // 代码
            "java" to "text/x-java-source", "kt" to "text/x-kotlin", "kts" to "text/x-kotlin",
            "py" to "text/x-python", "c" to "text/x-c", "h" to "text/x-c",
            "cpp" to "text/x-c++src", "hpp" to "text/x-c++hdr",
            "sh" to "application/x-sh", "bat" to "application/x-bat",
            "ps1" to "text/x-powershell", "yaml" to "application/yaml", "yml" to "application/yaml",
            "properties" to "text/x-properties", "gradle" to "text/x-groovy",
            "lua" to "text/x-lua", "go" to "text/x-go", "rs" to "text/x-rust",
            "swift" to "text/x-swift", "rb" to "text/x-ruby", "php" to "application/x-php",
            "sql" to "application/x-sql", "db" to "application/x-sqlite3", "sqlite" to "application/x-sqlite3",
            "ttf" to "font/ttf", "otf" to "font/otf", "woff" to "font/woff", "woff2" to "font/woff2",
            "wasm" to "application/wasm", "jar" to "application/java-archive",
            "so" to "application/octet-stream", "dll" to "application/octet-stream",
            "bin" to "application/octet-stream", "dat" to "application/octet-stream",
        )
    }
}

/** android.webkit.CookieManager：内存 cookie 表（按 host 存储原始 cookie 串）。 */
class CookieManager private constructor() {

    private val store = ConcurrentHashMap<String, MutableList<String>>()
    @Volatile private var acceptCookie = true
    private val acceptThirdParty = java.util.Collections.synchronizedMap(java.util.WeakHashMap<WebView, Boolean>())

    fun setAcceptCookie(accept: Boolean) {
        acceptCookie = accept
    }

    fun acceptCookie(): Boolean = acceptCookie

    fun setAcceptThirdPartyCookies(webview: WebView, accept: Boolean) {
        acceptThirdParty[webview] = accept
    }

    fun acceptThirdPartyCookies(webview: WebView): Boolean = acceptThirdParty[webview] ?: false

    fun setCookie(url: String?, value: String?) {
        if (url == null || value == null || !acceptCookie) return
        val host = hostOf(url) ?: return
        val list = store.getOrPut(host) { java.util.Collections.synchronizedList(mutableListOf()) }
        synchronized(list) {
            val name = value.substringBefore('=')
            list.removeAll { it.substringBefore('=').trim() == name.trim() }
            list.add(value.substringBefore(';'))
        }
    }

    fun setCookie(url: String?, value: String?, callback: ValueCallback<Boolean>?) {
        setCookie(url, value)
        callback?.onReceiveValue(true)
    }

    fun getCookie(url: String?): String? {
        if (url == null) return null
        val host = hostOf(url) ?: return null
        val list = store[host] ?: return null
        return synchronized(list) {
            if (list.isEmpty()) null else list.joinToString("; ")
        }
    }

    fun getCookie(url: String?, privateBrowsing: Boolean): String? = getCookie(url)

    fun hasCookies(): Boolean = store.values.any { it.isNotEmpty() }

    fun hasCookies(privateBrowsing: Boolean): Boolean = hasCookies()

    fun removeAllCookies(callback: ValueCallback<Boolean>?) {
        store.clear()
        callback?.onReceiveValue(true)
    }

    fun removeSessionCookies(callback: ValueCallback<Boolean>?) {
        callback?.onReceiveValue(true)
    }

    fun removeExpiredCookies() {}

    fun removeAllCookie() {
        store.clear()
    }

    fun flush() {}

    private fun hostOf(url: String): String? = try {
        java.net.URI(url).host ?: url.substringAfter("://").substringBefore('/').substringBefore(':').ifEmpty { null }
    } catch (e: Exception) {
        url.substringAfter("://").substringBefore('/').substringBefore(':').ifEmpty { null }
    }

    companion object {
        private val singletonInstance by lazy { CookieManager() }

        @JvmStatic
        fun getInstance(): CookieManager = singletonInstance

        @JvmStatic
        fun allowFileSchemeCookies(): Boolean = true

        @JvmStatic
        fun setAcceptFileSchemeCookies(accept: Boolean) {}
    }
}

/** android.webkit.URLUtil：真实 URL 判定实现。 */
object URLUtil {

    @JvmStatic
    fun isHttpUrl(url: String?): Boolean = url != null && url.startsWith("http:", ignoreCase = true)

    @JvmStatic
    fun isHttpsUrl(url: String?): Boolean = url != null && url.startsWith("https:", ignoreCase = true)

    @JvmStatic
    fun isNetworkUrl(url: String?): Boolean = isHttpUrl(url) || isHttpsUrl(url)

    @JvmStatic
    fun isFileUrl(url: String?): Boolean = url != null && url.startsWith("file:", ignoreCase = true)

    @JvmStatic
    fun isAboutUrl(url: String?): Boolean = url != null && url.startsWith("about:", ignoreCase = true)

    @JvmStatic
    fun isDataUrl(url: String?): Boolean = url != null && url.startsWith("data:", ignoreCase = true)

    @JvmStatic
    fun isJavaScriptUrl(url: String?): Boolean = url != null && url.startsWith("javascript:", ignoreCase = true)

    @JvmStatic
    fun isAssetUrl(url: String?): Boolean = url != null && url.startsWith("file:///android_asset/", ignoreCase = true)

    @JvmStatic
    fun isContentUrl(url: String?): Boolean = url != null && url.startsWith("content:", ignoreCase = true)

    @JvmStatic
    fun isCookielessProxyUrl(url: String?): Boolean = false

    @JvmStatic
    fun isValidUrl(url: String?): Boolean =
        url != null && (isNetworkUrl(url) || isFileUrl(url) || isAboutUrl(url) || isDataUrl(url) ||
            isJavaScriptUrl(url) || isContentUrl(url))

    @JvmStatic
    fun guessUrl(inUrl: String?): String {
        if (inUrl.isNullOrEmpty()) return ""
        var url = inUrl.trim()
        if (isNetworkUrl(url) || isFileUrl(url) || isAboutUrl(url) || isDataUrl(url)) return url
        if (!url.contains(".")) url = "www.$url.com"
        return "http://$url"
    }

    @JvmStatic
    fun composeSearchUrl(inQuery: String?, template: String?, queryPlaceHolder: String?): String {
        if (inQuery == null || template == null || queryPlaceHolder == null) return ""
        val idx = template.indexOf(queryPlaceHolder)
        if (idx < 0) return ""
        val encoded = java.net.URLEncoder.encode(inQuery, "UTF-8")
        return template.substring(0, idx) + encoded + template.substring(idx + queryPlaceHolder.length)
    }

    @JvmStatic
    fun decode(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val sb = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b = bytes[i].toInt()
            if (b == '%'.code && i + 2 < bytes.size) {
                try {
                    sb.append(((hexVal(bytes[i + 1]) shl 4) or hexVal(bytes[i + 2])).toChar())
                    i += 3
                    continue
                } catch (_: Exception) {}
            }
            sb.append(b.toChar())
            i++
        }
        return sb.toString()
    }

    private fun hexVal(b: Byte): Int {
        val c = b.toInt().toChar()
        return when (c) {
            in '0'..'9' -> c - '0'
            in 'a'..'f' -> c - 'a' + 10
            in 'A'..'F' -> c - 'A' + 10
            else -> throw IllegalArgumentException("bad hex")
        }
    }

    @JvmStatic
    fun guessFileName(url: String?, contentDisposition: String?, mimeType: String?): String {
        // 1. content-disposition filename
        if (contentDisposition != null) {
            Regex("""filename\*?=(?:UTF-8'')?"?([^";]+)"?""", RegexOption.IGNORE_CASE)
                .find(contentDisposition)?.groupValues?.get(1)?.trim()?.let { name ->
                    if (name.isNotEmpty()) return java.net.URLDecoder.decode(name, "UTF-8")
                }
        }
        // 2. URL 最后一段
        var filename = ""
        if (url != null) {
            var path = url.substringBefore('?').substringBefore('#')
            path = path.substringAfterLast('/')
            if (path.isNotEmpty()) filename = java.net.URLDecoder.decode(path, "UTF-8")
        }
        if (filename.isEmpty()) filename = "downloadfile"
        // 3. 无扩展名时按 mimeType 补
        if ('.' !in filename.substringAfterLast('/', "") || filename.endsWith('.')) {
            val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            if (ext != null) {
                if (filename.endsWith('.')) filename += ext else filename = "$filename.$ext"
            } else if (filename.endsWith('.')) {
                filename += "html"
            }
        }
        return filename
    }

    @JvmStatic
    fun fileUrlExists(url: String?): Boolean = false
}

/** android.webkit.WebStorage：桌面无 web 存储，骨架实现。 */
class WebStorage private constructor() {

    class Origin(val origin: String?, val quota: Long = 0, val usage: Long = 0)

    fun getOrigins(callback: ValueCallback<Map<String, Origin>>?) {
        callback?.onReceiveValue(emptyMap())
    }

    fun getUsageForOrigin(origin: String?): Long = 0
    fun getQuotaForOrigin(origin: String?): Long = 0
    fun setQuotaForOrigin(origin: String?, quota: Long) {}

    @Deprecated("deprecated")
    fun setAppCacheMaximumSize(size: Long) {}

    fun deleteOrigin(origin: String?) {}
    fun deleteAllData() {}

    companion object {
        private val singletonInstance by lazy { WebStorage() }

        @JvmStatic
        fun getInstance(): WebStorage = singletonInstance
    }
}

/** android.webkit.ConsoleMessage。 */
open class ConsoleMessage(
    private val msg: String? = null,
    private val src: String? = null,
    private val line: Int = 0,
    private val level: MessageLevel = MessageLevel.LOG,
) {
    enum class MessageLevel { DEBUG, ERROR, LOG, TIP, WARNING, UNKNOWN }

    fun message(): String? = msg
    fun sourceId(): String? = src
    fun lineNumber(): Int = line
    fun messageLevel(): MessageLevel = level
}

/** android.webkit.GeolocationPermissions。 */
class GeolocationPermissions private constructor() {

    fun interface Callback {
        fun invoke(origin: String?, allow: Boolean, retain: Boolean)
    }

    fun getOrigins(callback: ValueCallback<Set<String>>?) {
        callback?.onReceiveValue(emptySet())
    }

    fun getAllowed(origin: String?, callback: ValueCallback<Boolean>?) {
        callback?.onReceiveValue(false)
    }

    fun allow(origin: String?) {}
    fun clear(origin: String?) {}
    fun clearAll() {}

    companion object {
        private val singletonInstance by lazy { GeolocationPermissions() }

        @JvmStatic
        fun getInstance(): GeolocationPermissions = singletonInstance
    }
}

/** android.webkit.DownloadListener。 */
fun interface DownloadListener {
    fun onDownloadStart(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimetype: String?,
        contentLength: Long,
    )
}

/** android.webkit.JsResult。 */
open class JsResult {
    open fun confirm() {}
    open fun cancel() {}
}

/** android.webkit.JsPromptResult。 */
open class JsPromptResult : JsResult() {
    open fun confirm(result: String?) {}
}

/** android.webkit.SslErrorHandler。 */
open class SslErrorHandler {
    open fun proceed() {}
    open fun cancel() {}
}

/** android.webkit.HttpAuthHandler。 */
open class HttpAuthHandler {
    open fun proceed(username: String?, password: String?) {}
    open fun cancel() {}
    open fun useHttpAuthUsernamePassword(): Boolean = false
}

/** android.webkit.ClientCertRequest。 */
open class ClientCertRequest {
    open val host: String? = null
    open val port: Int = -1
    open val keyTypes: Array<String>? = null
    open val principals: Array<java.security.Principal>? = null

    open fun proceed(privateKey: java.security.PrivateKey?, chain: Array<java.security.cert.X509Certificate>?) {}
    open fun cancel() {}
    open fun ignore() {}
}

/** android.webkit.RenderProcessGoneDetail。 */
open class RenderProcessGoneDetail {
    open fun didCrash(): Boolean = false
    open fun rendererPriorityAtExit(): Int = 0
}

/** android.webkit.SafeBrowsingResponse。 */
open class SafeBrowsingResponse {
    open fun showInterstitial(allowReporting: Boolean) {}
    open fun proceed(report: Boolean) {}
    open fun backToSafety(report: Boolean) {}
}

/** android.webkit.PermissionRequest。 */
open class PermissionRequest {
    open val origin: Uri? = null
    open val resources: Array<String> = emptyArray()

    open fun grant(resources: Array<String>?) {}
    open fun deny() {}

    companion object {
        const val RESOURCE_AUDIO_CAPTURE = "android.webkit.resource.AUDIO_CAPTURE"
        const val RESOURCE_VIDEO_CAPTURE = "android.webkit.resource.VIDEO_CAPTURE"
        const val RESOURCE_PROTECTED_MEDIA_ID = "android.webkit.resource.PROTECTED_MEDIA_ID"
        const val RESOURCE_MIDI_SYSEX = "android.webkit.resource.MIDI_SYSEX"
    }
}
