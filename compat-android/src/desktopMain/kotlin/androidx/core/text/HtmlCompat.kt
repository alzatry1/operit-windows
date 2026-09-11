package androidx.core.text

import android.text.Spanned
import android.text.SpannedString

/**
 * androidx.core.text.HtmlCompat 桌面版。
 * fromHtml 做轻量标签剥离后包装成 Spanned（桌面无 TextView 渲染管线，
 * 调用方大多只需要纯文本）；toHtml 直接返回 Spanned 的文本内容。
 */
object HtmlCompat {

    const val FROM_HTML_MODE_LEGACY = 0
    const val FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1
    const val FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 2
    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4
    const val FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8
    const val FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 16
    const val FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32
    const val FROM_HTML_MODE_COMPACT = 63
    const val FROM_HTML_OPTION_USE_CSS_COLORS = 256

    const val TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0
    const val TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1

    /** 剥离 HTML 标签：<br>→换行、</p>→双换行，其余标签删除，常见实体反转义。 */
    private fun stripHtml(source: String): String {
        var s = source
        s = s.replace(Regex("(?i)<br\\s*/?>"), "\n")
        s = s.replace(Regex("(?i)</p\\s*>"), "\n\n")
        s = s.replace(Regex("(?i)<li[^>]*>"), "\n• ")
        s = s.replace(Regex("<[^>]*>"), "")
        s = s.replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&#039;", "'")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
        return s
    }

    @JvmStatic
    fun fromHtml(source: String, flags: Int): Spanned = SpannedString(stripHtml(source))

    @JvmStatic
    fun toHtml(spanned: Spanned, flags: Int): String = spanned.toString()
}
