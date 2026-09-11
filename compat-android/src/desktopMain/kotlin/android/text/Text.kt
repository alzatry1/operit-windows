package android.text

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import java.util.Locale

/** android.text.TextPaint：Paint + 文本装饰字段。 */
open class TextPaint : Paint {
    constructor() : super()
    constructor(flags: Int) : super(flags)
    constructor(p: Paint) : super(p)

    var linkColor: Int = Color.BLUE
    var underlineColor: Int = 0
    var underlineThickness: Float = 0f
    var bgColor: Int = Color.TRANSPARENT
    var baselineShift: Int = 0
    var drawableState: IntArray = IntArray(0)
    var textLocale: Locale = Locale.getDefault()

    open fun set(tp: TextPaint) {
        super.set(tp)
        linkColor = tp.linkColor
        underlineColor = tp.underlineColor
        underlineThickness = tp.underlineThickness
        bgColor = tp.bgColor
        baselineShift = tp.baselineShift
        drawableState = tp.drawableState
        textLocale = tp.textLocale
    }
}

/** android.text.TextUtils。 */
object TextUtils {

    enum class TruncateAt { START, MIDDLE, END, MARQUEE, END_SMALL }

    @JvmStatic
    fun isEmpty(str: CharSequence?): Boolean = str == null || str.isEmpty()

    @JvmStatic
    fun isDigitsOnly(str: CharSequence?): Boolean =
        !str.isNullOrEmpty() && str.all { it.isDigit() }

    @JvmStatic
    fun equals(a: CharSequence?, b: CharSequence?): Boolean =
        if (a === b) true else if (a == null || b == null || a.length != b.length) false else a.toString() == b.toString()

    @JvmStatic
    fun getTrimmedLength(s: CharSequence?): Int {
        if (s == null) return 0
        var start = 0; var end = s.length
        while (start < end && s[start] <= ' ') start++
        while (end > start && s[end - 1] <= ' ') end--
        return end - start
    }

    @JvmStatic
    fun join(delimiter: CharSequence, tokens: Array<out Any?>?): String {
        if (tokens == null) return ""
        val sb = StringBuilder()
        tokens.forEachIndexed { i, token ->
            if (i > 0) sb.append(delimiter)
            sb.append(token?.toString() ?: "")
        }
        return sb.toString()
    }

    @JvmStatic
    fun join(delimiter: CharSequence, tokens: Iterable<*>?): String {
        if (tokens == null) return ""
        val sb = StringBuilder()
        var first = true
        for (token in tokens) {
            if (!first) sb.append(delimiter)
            first = false
            sb.append(token?.toString() ?: "")
        }
        return sb.toString()
    }

    @JvmStatic
    fun split(text: String, expression: java.util.regex.Pattern): Array<String> =
        expression.split(text)

    @JvmStatic
    fun split(text: String, delimiter: String): Array<String> =
        text.split(java.util.regex.Pattern.quote(delimiter)).toTypedArray()

    @JvmStatic
    fun htmlEncode(s: String?): String {
        if (s == null) return ""
        val sb = StringBuilder()
        for (c in s) {
            when (c) {
                '<' -> sb.append("&lt;")
                '>' -> sb.append("&gt;")
                '&' -> sb.append("&amp;")
                '\'' -> sb.append("&#39;")
                '"' -> sb.append("&quot;")
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }

    @JvmStatic
    fun indexOf(s: CharSequence, needle: Char): Int = indexOf(s, needle, 0)

    @JvmStatic
    fun indexOf(s: CharSequence, needle: Char, fromIndex: Int): Int = s.indexOf(needle, fromIndex)

    @JvmStatic
    fun indexOf(s: CharSequence, needle: Char, fromIndex: Int, toIndex: Int): Int {
        val idx = s.indexOf(needle, fromIndex)
        return if (idx in fromIndex until toIndex) idx else -1
    }

    @JvmStatic
    fun lastIndexOf(s: CharSequence, needle: Char): Int = lastIndexOf(s, needle, s.length - 1)

    @JvmStatic
    fun lastIndexOf(s: CharSequence, needle: Char, fromIndex: Int): Int = s.lastIndexOf(needle, fromIndex)

    @JvmStatic
    fun lastIndexOf(s: CharSequence, needle: Char, fromIndex: Int, toIndex: Int): Int {
        val idx = s.lastIndexOf(needle, fromIndex)
        return if (idx in toIndex..fromIndex) idx else -1
    }

    @JvmStatic
    fun regionMatches(one: CharSequence, toffset: Int, two: CharSequence, ooffset: Int, len: Int): Boolean {
        if (toffset < 0 || ooffset < 0 || toffset + len > one.length || ooffset + len > two.length) return false
        for (i in 0 until len) if (one[toffset + i] != two[ooffset + i]) return false
        return true
    }

    @JvmStatic
    fun substring(text: CharSequence, start: Int, end: Int): String = text.subSequence(start, end).toString()

    @JvmStatic
    fun writeToParcel(cs: CharSequence?, p: android.os.Parcel, parcelableFlags: Int) {
        p.writeString(cs?.toString())
    }

    @JvmStatic
    fun replace(template: CharSequence, sources: Array<String>, destinations: Array<CharSequence>): CharSequence {
        var result = template.toString()
        for (i in sources.indices) {
            result = result.replace(sources[i], destinations[i].toString())
        }
        return result
    }

    @JvmStatic
    fun expandTemplate(template: CharSequence, vararg values: CharSequence): CharSequence {
        var result = template.toString()
        values.forEachIndexed { i, v ->
            result = result.replace("^${i + 1}".toRegex(RegexOption.LITERAL).toString(), v.toString())
            result = result.replace("^${i + 1}", v.toString())
        }
        return result
    }

    @JvmStatic
    fun concat(vararg text: CharSequence): CharSequence = text.joinToString("")

    @JvmStatic
    fun nullIfEmpty(str: String?): String? = if (str.isNullOrEmpty()) null else str

    @JvmStatic
    fun stringOrSpannedString(source: CharSequence?): CharSequence? = source

    @JvmStatic
    fun getChars(s: CharSequence, start: Int, end: Int, dest: CharArray, destoff: Int) {
        for (i in start until end) dest[destoff + i - start] = s[i]
    }

    @JvmStatic
    fun copySpansFrom(source: Spanned, start: Int, end: Int, kind: Class<*>, dest: Spannable, destoff: Int) {
        val spans = source.getSpans(start, end, kind)
        for (span in spans) {
            val st = maxOf(source.getSpanStart(span), start)
            val en = minOf(source.getSpanEnd(span), end)
            val flags = source.getSpanFlags(span)
            dest.setSpan(span, st - start + destoff, en - start + destoff, flags)
        }
    }

    @JvmStatic
    fun ellipsize(text: CharSequence, paint: android.graphics.Paint, avail: Float, where: TruncateAt): CharSequence =
        ellipsize(text, paint, avail, where, false, null)

    @JvmStatic
    fun ellipsize(
        text: CharSequence, paint: android.graphics.Paint, avail: Float,
        where: TruncateAt, preserveLength: Boolean, callback: EllipsizeCallback?,
    ): CharSequence {
        val full = paint.measureText(text.toString())
        if (full <= avail) return text
        val ellipsis = "\u2026"
        val ellipsisWidth = paint.measureText(ellipsis)
        if (ellipsisWidth > avail) {
            callback?.ellipsized(0, 0)
            return if (preserveLength) text else ""
        }
        val target = avail - ellipsisWidth
        val s = text.toString()
        return when (where) {
            TruncateAt.START -> {
                var lo = 0; var hi = s.length
                while (lo < hi) {
                    val mid = (lo + hi) / 2
                    if (paint.measureText(s.substring(mid)) <= target) hi = mid else lo = mid + 1
                }
                callback?.ellipsized(0, lo)
                (if (preserveLength) s.substring(0, lo) + ellipsis + s.substring(lo).substring(1) else ellipsis + s.substring(lo))
            }
            TruncateAt.MIDDLE -> {
                val half = target / 2
                var left = 0
                while (left < s.length && paint.measureText(s.substring(0, left + 1)) <= half) left++
                var right = s.length
                while (right > left && paint.measureText(s.substring(right - 1)) <= half) right--
                callback?.ellipsized(left, right)
                (s.substring(0, left) + ellipsis + s.substring(right))
            }
            else -> {
                var lo = 0; var hi = s.length
                while (lo < hi) {
                    val mid = (lo + hi + 1) / 2
                    if (paint.measureText(s.substring(0, mid)) <= target) lo = mid else hi = mid - 1
                }
                callback?.ellipsized(lo, s.length)
                s.substring(0, lo) + ellipsis
            }
        }
    }

    @JvmStatic
    fun commaEllipsize(text: CharSequence, paint: android.graphics.Paint, avail: Float, oneMore: String, more: String): CharSequence =
        ellipsize(text, paint, avail, TruncateAt.END)

    @JvmStatic
    fun delimitedStringContains(delimitedString: String, delimiter: Char, item: String): Boolean {
        if (delimitedString.isEmpty() || item.isEmpty()) return false
        return delimitedString.split(delimiter).contains(item)
    }

    @JvmStatic
    fun getCapsMode(cs: CharSequence?, off: Int, reqModes: Int): Int = 0

    fun interface EllipsizeCallback {
        fun ellipsized(start: Int, end: Int)
    }
}

/** android.text.Html 轻实现：去标签 + 实体解码。 */
object Html {
    const val FROM_HTML_MODE_LEGACY = 0
    const val FROM_HTML_MODE_COMPACT = 63
    const val FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1
    const val FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2
    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4
    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8
    const val FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16
    const val FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32
    const val FROM_HTML_SEPARATOR_LINE_BREAK_LI = 64
    const val FROM_HTML_OPTION_USE_CSS_COLORS = 256
    const val TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0
    const val TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1

    fun interface ImageGetter {
        fun getDrawable(source: String): android.graphics.drawable.Drawable
    }

    interface TagHandler {
        fun handleTag(opening: Boolean, tag: String, output: Editable, attributes: Any?)
    }

    @JvmStatic
    fun fromHtml(source: String): Spanned = fromHtml(source, FROM_HTML_MODE_LEGACY, null, null)

    @JvmStatic
    fun fromHtml(source: String, flags: Int): Spanned = fromHtml(source, flags, null, null)

    @JvmStatic
    fun fromHtml(source: String, imageGetter: ImageGetter?, tagHandler: TagHandler?): Spanned =
        fromHtml(source, FROM_HTML_MODE_LEGACY, imageGetter, tagHandler)

    @JvmStatic
    fun fromHtml(source: String, flags: Int, imageGetter: ImageGetter?, tagHandler: TagHandler?): Spanned {
        // 轻实现：去标签 + 常见实体解码（不生成 span，渲染由 B4 批次用 jsoup 实装）
        var text = source
            .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</p\\s*>", RegexOption.IGNORE_CASE), "\n\n")
            .replace(Regex("</div\\s*>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<li\\s*>", RegexOption.IGNORE_CASE), "• ")
            .replace(Regex("</li\\s*>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<[^>]+>"), "")
        text = decodeEntities(text)
        return SpannableString.valueOf(text)
    }

    private fun decodeEntities(s: String): String = s
        .replace("&lt;", "<").replace("&gt;", ">")
        .replace("&quot;", "\"").replace("&#39;", "'")
        .replace("&apos;", "'").replace("&nbsp;", " ")
        .replace("&amp;", "&")

    @JvmStatic
    fun toHtml(text: Spanned): String = toHtml(text, TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)

    @JvmStatic
    fun toHtml(text: Spanned, option: Int): String = TextUtils.htmlEncode(text.toString())
}

/** android.text.format.DateFormat。 */
object DateFormatCompatMarker // 防包为空

/** android.text.Annotation 轻 stub。 */
open class Annotation : android.os.Parcelable {
    var key: String? = null
    var value: String? = null

    constructor()
    constructor(key: String, value: String) {
        this.key = key; this.value = value
    }

    constructor(src: android.os.Parcel)

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {
        dest.writeString(key); dest.writeString(value)
    }
}

/** android.text.SpannableString 伴生已在 Core.kt。此处保留 SpannableStringInternal 占位。 */
internal object SpannableStringInternalMarker
