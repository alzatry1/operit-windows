package android.net

import android.os.Parcel
import android.os.Parcelable
import java.io.File

/**
 * android.net.Uri 桌面实现：不可变、宽松解析（scheme://authority/path?query#fragment）。
 * 兼容 http(s)://、file://、content:// 及不透明 URI（mailto: 等）。
 * 全部无参访问器以 Kotlin 属性声明（与迁移代码的 uri.scheme/uri.path 属性语法一致）。
 */
class Uri private constructor(
    val scheme: String?,
    val encodedOpaquePart: String?,   // 不透明部分（无 // 时）
    val authority: String?,
    val path: String?,
    val encodedQuery: String?,
    val encodedFragment: String?,
) : Parcelable, Comparable<Uri>, java.io.Serializable {

    private val stringForm: String by lazy { buildString() }

    private fun buildString(): String {
        val sb = StringBuilder()
        if (scheme != null) sb.append(scheme).append(':')
        if (authority != null || path != null) {
            if (authority != null) sb.append("//").append(authority)
            if (path != null) sb.append(path)
        } else if (encodedOpaquePart != null) {
            sb.append(encodedOpaquePart)
        }
        if (encodedQuery != null) sb.append('?').append(encodedQuery)
        if (encodedFragment != null) sb.append('#').append(encodedFragment)
        return sb.toString()
    }

    // ---- 派生属性 ----
    val userInfo: String?
        get() = authority?.takeIf { it.contains('@') }?.substringBefore('@')

    val encodedUserInfo: String?
        get() = userInfo

    val host: String?
        get() {
            val auth = authority ?: return null
            val afterAt = auth.substringAfterLast('@')
            return afterAt.substringBefore(':')
        }

    val port: Int
        get() {
            val auth = authority ?: return -1
            val afterAt = auth.substringAfterLast('@')
            val colon = afterAt.indexOf(':')
            if (colon < 0) return -1
            return afterAt.substring(colon + 1).toIntOrNull() ?: -1
        }

    val query: String?
        get() = encodedQuery?.let { decode(it) }

    val fragment: String?
        get() = encodedFragment?.let { decode(it) }

    val schemeSpecificPart: String
        get() = encodedOpaquePart ?: run {
            val sb = StringBuilder()
            if (authority != null) sb.append("//").append(authority)
            path?.let(sb::append)
            sb.toString()
        }

    val encodedSchemeSpecificPart: String
        get() = schemeSpecificPart

    val isOpaque: Boolean
        get() = scheme != null && authority == null && encodedOpaquePart != null

    val isAbsolute: Boolean
        get() = scheme != null

    val isRelative: Boolean
        get() = scheme == null

    val isHierarchical: Boolean
        get() = !isOpaque

    val pathSegments: List<String>
        get() {
            val p = path ?: return emptyList()
            return p.split('/').filter { it.isNotEmpty() }
        }

    val encodedPath: String?
        get() = path

    val encodedAuthority: String?
        get() = authority

    val encodedPathSegments: List<String>
        get() = pathSegments

    val lastPathSegment: String?
        get() = pathSegments.lastOrNull()

    // ---- 带参方法 ----
    fun getQueryParameter(key: String): String? {
        val q = encodedQuery ?: return null
        val encodedKey = encode(key, null)
        var start = 0
        while (start <= q.length) {
            val end = q.indexOf('&', start).let { if (it < 0) q.length else it }
            val pair = q.substring(start, end)
            val eq = pair.indexOf('=')
            val k = if (eq < 0) pair else pair.substring(0, eq)
            if (k == key || k == encodedKey) {
                return if (eq < 0) "" else decode(pair.substring(eq + 1))
            }
            if (end == q.length) break
            start = end + 1
        }
        return null
    }

    fun getQueryParameters(key: String): List<String> {
        val q = encodedQuery ?: return emptyList()
        val result = ArrayList<String>()
        val encodedKey = encode(key, null)
        var start = 0
        while (start <= q.length) {
            val end = q.indexOf('&', start).let { if (it < 0) q.length else it }
            val pair = q.substring(start, end)
            val eq = pair.indexOf('=')
            val k = if (eq < 0) pair else pair.substring(0, eq)
            if (k == key || k == encodedKey) result += if (eq < 0) "" else decode(pair.substring(eq + 1))
            if (end == q.length) break
            start = end + 1
        }
        return result
    }

    val queryParameterNames: Set<String>
        get() {
            val q = encodedQuery ?: return emptySet()
            val result = LinkedHashSet<String>()
            var start = 0
            while (start <= q.length) {
                val end = q.indexOf('&', start).let { if (it < 0) q.length else it }
                val pair = q.substring(start, end)
                val eq = pair.indexOf('=')
                result += decode(if (eq < 0) pair else pair.substring(0, eq))
                if (end == q.length) break
                start = end + 1
            }
            return result
        }

    fun getBooleanQueryParameter(key: String, defaultValue: Boolean): Boolean {
        val v = getQueryParameter(key) ?: return defaultValue
        return v.equals("true", true) || v == "1"
    }

    fun buildUpon(): Builder = Builder()
        .scheme(scheme)
        .also { b ->
            if (encodedOpaquePart != null && authority == null) b.encodedOpaquePart(encodedOpaquePart)
            else b.encodedAuthority(authority).encodedPath(path)
        }
        .encodedQuery(encodedQuery)
        .encodedFragment(encodedFragment)

    fun normalizeScheme(): Uri {
        val s = scheme?.lowercase() ?: return this
        if (s == scheme) return this
        return parse(s + stringForm.substring(scheme!!.length))
    }

    override fun toString(): String = stringForm
    fun toSafeString(): String = stringForm
    override fun equals(other: Any?): Boolean = other is Uri && other.stringForm == stringForm
    override fun hashCode(): Int = stringForm.hashCode()
    override fun compareTo(other: Uri): Int = stringForm.compareTo(other.stringForm)

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeString(stringForm) }

    /** android.net.Uri.Builder */
    class Builder {
        private var scheme: String? = null
        private var ssp: String? = null
        private var authority: String? = null
        private var path: String? = null
        private var query: String? = null
        private var fragment: String? = null

        fun scheme(scheme: String?): Builder = apply { this.scheme = scheme }
        fun opaquePart(ssp: String?): Builder = apply { this.ssp = ssp }
        fun encodedOpaquePart(ssp: String?): Builder = apply { this.ssp = ssp }
        fun authority(authority: String?): Builder = apply { this.authority = authority?.let { encode(it, "/:@") } }
        fun encodedAuthority(authority: String?): Builder = apply { this.authority = authority }
        fun path(path: String?): Builder = apply { this.path = path?.let { encode(it, "/") } }
        fun encodedPath(path: String?): Builder = apply { this.path = path }
        fun appendPath(newSegment: String?): Builder = apply {
            if (newSegment == null) return@apply
            val seg = encode(newSegment, null)
            path = if (path.isNullOrEmpty()) "/$seg" else path!!.trimEnd('/') + "/" + seg
        }
        fun appendEncodedPath(newSegment: String?): Builder = apply {
            if (newSegment == null) return@apply
            path = if (path.isNullOrEmpty()) newSegment else path!!.trimEnd('/') + "/" + newSegment.trimStart('/')
        }
        fun query(query: String?): Builder = apply { this.query = query?.let { encode(it, null) } }
        fun encodedQuery(query: String?): Builder = apply { this.query = query }
        fun fragment(fragment: String?): Builder = apply { this.fragment = fragment?.let { encode(it, null) } }
        fun encodedFragment(fragment: String?): Builder = apply { this.fragment = fragment }
        fun appendQueryParameter(key: String, value: String): Builder = apply {
            val entry = encode(key, null) + "=" + encode(value, null)
            query = if (query.isNullOrEmpty()) entry else "$query&$entry"
        }
        fun clearQuery(): Builder = apply { query = null }
        fun build(): Uri = Uri(scheme, ssp, authority, path, query, fragment)
        override fun toString(): String = build().toString()
    }

    companion object {
        @JvmField val EMPTY: Uri = Uri(null, "", null, null, null, null)

        @JvmField val CREATOR: Parcelable.Creator<Uri> = object : Parcelable.Creator<Uri> {
            override fun createFromParcel(source: Parcel): Uri = parse(source.readString() ?: "")
            override fun newArray(size: Int): Array<Uri?> = arrayOfNulls(size)
        }

        /** 宽松解析：尽量拆出 scheme/authority/path/query/fragment，不抛异常。 */
        @JvmStatic
        fun parse(uriString: String?): Uri {
            var s = uriString ?: ""
            var fragment: String? = null
            var query: String? = null
            var scheme: String? = null
            var authority: String? = null
            var path: String? = null
            var ssp: String? = null

            val hashIdx = s.indexOf('#')
            if (hashIdx >= 0) { fragment = s.substring(hashIdx + 1); s = s.substring(0, hashIdx) }
            val qIdx = s.indexOf('?')
            if (qIdx >= 0) { query = s.substring(qIdx + 1); s = s.substring(0, qIdx) }

            val colonIdx = s.indexOf(':')
            if (colonIdx > 0 && s.substring(0, colonIdx).all { it.isLetterOrDigit() || it in "+-." } &&
                s.substring(0, colonIdx).first().isLetter()
            ) {
                scheme = s.substring(0, colonIdx)
                s = s.substring(colonIdx + 1)
            }

            if (s.startsWith("//")) {
                val rest = s.substring(2)
                val slashIdx = rest.indexOf('/')
                if (slashIdx < 0) {
                    authority = rest; path = ""
                } else {
                    authority = rest.substring(0, slashIdx); path = rest.substring(slashIdx)
                }
            } else if (scheme != null) {
                ssp = s // 不透明 URI（mailto:, tel: 等）
            } else {
                path = s
            }
            return Uri(scheme, ssp, authority, path, query, fragment)
        }

        @JvmStatic
        fun fromParts(scheme: String?, ssp: String?, fragment: String?): Uri =
            Uri(scheme, ssp, null, null, null, fragment)

        @JvmStatic
        fun fromFile(file: File): Uri {
            val p = file.absolutePath.replace('\\', '/')
            return Uri("file", null, "", if (p.startsWith("/")) p else "/$p", null, null)
        }

        private const val HEX_DIGITS = "0123456789ABCDEF"
        private const val DEFAULT_ALLOWED =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~"

        @JvmStatic
        fun encode(s: String?): String = encode(s, null)

        @JvmStatic
        fun encode(s: String?, allow: String?): String {
            if (s == null) return ""
            val allowed = DEFAULT_ALLOWED + (allow ?: "")
            val sb = StringBuilder()
            val bytes = s.toByteArray(Charsets.UTF_8)
            for (b in bytes) {
                val c = b.toInt().toChar()
                if (b.toInt() in 32..126 && c in allowed) sb.append(c)
                else sb.append('%').append(HEX_DIGITS[(b.toInt() shr 4) and 0xF]).append(HEX_DIGITS[b.toInt() and 0xF])
            }
            return sb.toString()
        }

        @JvmStatic
        fun decode(s: String?): String {
            if (s == null) return ""
            val bytes = java.io.ByteArrayOutputStream()
            var i = 0
            while (i < s.length) {
                val c = s[i]
                if (c == '%' && i + 2 < s.length) {
                    try {
                        bytes.write(s.substring(i + 1, i + 3).toInt(16))
                        i += 3
                        continue
                    } catch (e: NumberFormatException) { /* 原样输出 */ }
                }
                if (c == '+') bytes.write(' '.code) else bytes.write(c.toString().toByteArray(Charsets.UTF_8))
                i++
            }
            return bytes.toString(Charsets.UTF_8.name())
        }

        @JvmStatic
        fun withAppendedPath(baseUri: Uri?, pathSegment: String?): Uri? =
            baseUri?.buildUpon()?.appendEncodedPath(pathSegment)?.build()

        @JvmStatic
        fun isPathPrefixMatch(prefix: Uri, seg: Uri): Boolean {
            val p = prefix.pathSegments; val s = seg.pathSegments
            if (p.size > s.size) return false
            for (i in p.indices) if (p[i] != s[i]) return false
            return true
        }

        @JvmStatic
        fun writeToParcel(out: Parcel?, uri: Uri?) {
            out?.writeString(uri?.toString())
        }

        @JvmStatic
        fun readFromParcel(parcel: Parcel): Uri? = parcel.readString()?.let { parse(it) }
    }
}
