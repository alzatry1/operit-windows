package android.text

/**
 * android.text 核心接口与 Spannable 实现。
 */

/** android.text.GetChars。 */
interface GetChars : CharSequence {
    fun getChars(start: Int, end: Int, dest: CharArray, destoff: Int)
}

/** android.text.NoCopySpan：标记接口。 */
interface NoCopySpan

/** android.text.SpanWatcher / SpanSet 轻量支持不需要，直接给 Spanned。 */
interface Spanned : CharSequence {
    fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>): Array<T>
    fun getSpanStart(tag: Any?): Int
    fun getSpanEnd(tag: Any?): Int
    fun getSpanFlags(tag: Any?): Int
    fun nextSpanTransition(start: Int, limit: Int, kind: Class<*>?): Int

    companion object {
        const val SPAN_MARK_MARK = 17
        const val SPAN_MARK_POINT = 18
        const val SPAN_POINT_MARK = 33
        const val SPAN_POINT_POINT = 34
        const val SPAN_PARAGRAPH = 51
        const val SPAN_INCLUSIVE_EXCLUSIVE = SPAN_MARK_MARK
        const val SPAN_INCLUSIVE_INCLUSIVE = SPAN_POINT_MARK
        const val SPAN_EXCLUSIVE_EXCLUSIVE = SPAN_POINT_POINT
        const val SPAN_EXCLUSIVE_INCLUSIVE = SPAN_MARK_POINT
        const val SPAN_COMPOSING = 256
        const val SPAN_INTERMEDIATE = 512
        const val SPAN_PRIORITY_SHIFT = 16
        const val SPAN_PRIORITY = 16711680
        const val SPAN_USER_SHIFT = 24
        const val SPAN_USER = -16777216
    }
}

/** android.text.Spannable。 */
interface Spannable : Spanned {
    fun setSpan(what: Any?, start: Int, end: Int, flags: Int)
    fun removeSpan(what: Any?)
}

/** android.text.Editable。 */
interface Editable : GetChars, Spannable, Appendable {
    override fun append(text: CharSequence): Editable
    override fun append(text: CharSequence, start: Int, end: Int): Editable
    override fun append(c: Char): Editable
    fun replace(st: Int, en: Int, source: CharSequence, start: Int, end: Int): Editable
    fun replace(st: Int, en: Int, text: CharSequence): Editable
    fun delete(st: Int, en: Int): Editable
    fun insert(where: Int, text: CharSequence, start: Int, end: Int): Editable
    fun insert(where: Int, text: CharSequence): Editable
    fun clear()
    fun clearSpans()
    fun setFilters(filters: Array<InputFilter>)
    fun getFilters(): Array<InputFilter>

    class Factory {
        open fun newEditable(source: CharSequence): Editable = SpannableStringBuilder(source)

        companion object {
            @JvmStatic fun getInstance(): Factory = Factory()
        }
    }
}

/** android.text.InputFilter。 */
interface InputFilter {
    fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence?

    class AllCaps : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? =
            source.subSequence(start, end).toString().uppercase()
    }

    class LengthFilter(private val max: Int) : InputFilter {
        constructor(max: Int, unused: Any?) : this(max)

        fun getMax(): Int = max

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            val keep = max - (dest.length - (dend - dstart))
            if (keep <= 0) return ""
            if (keep >= end - start) return null
            return source.subSequence(start, start + keep)
        }
    }
}

/** span 记录。 */
internal data class SpanRec(var what: Any?, var start: Int, var end: Int, var flags: Int)

/** Spanned 共享实现基座（字符串 + span 列表）。 */
abstract class SpannedBase : Spanned {
    internal val spans = ArrayList<SpanRec>()

    protected abstract val text: CharSequence

    override val length: Int get() = text.length
    override fun get(index: Int): Char = text[index]
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = text.subSequence(startIndex, endIndex)
    override fun toString(): String = text.toString()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>): Array<T> {
        val list = spans.filter { rec ->
            type.isInstance(rec.what) && rec.start < end && rec.end > start
        }.map { it.what as T }
        @Suppress("UNCHECKED_CAST")
        val arr = java.lang.reflect.Array.newInstance(type, list.size) as Array<T>
        list.forEachIndexed { i, v -> arr[i] = v }
        return arr
    }

    override fun getSpanStart(tag: Any?): Int = spans.firstOrNull { it.what === tag }?.start ?: -1
    override fun getSpanEnd(tag: Any?): Int = spans.firstOrNull { it.what === tag }?.end ?: -1
    override fun getSpanFlags(tag: Any?): Int = spans.firstOrNull { it.what === tag }?.flags ?: 0

    override fun nextSpanTransition(start: Int, limit: Int, kind: Class<*>?): Int {
        var next = limit
        for (rec in spans) {
            if (kind == null || kind.isInstance(rec.what)) {
                if (rec.start in (start + 1)..<next) next = rec.start
                if (rec.end in (start + 1)..<next) next = rec.end
            }
        }
        return next
    }
}

/** android.text.SpannableString：内容不可变，span 可变。 */
open class SpannableString(private val content: CharSequence) : SpannedBase(), Spannable, GetChars {

    override val text: CharSequence get() = content

    init {
        // 拷贝已有 span
        if (content is Spanned) {
            val src = content.getSpans(0, content.length, Any::class.java)
            for (span in src) {
                val st = content.getSpanStart(span)
                val en = content.getSpanEnd(span)
                val fl = content.getSpanFlags(span)
                if (st >= 0 && en >= st) spans.add(SpanRec(span, st, en, fl))
            }
        }
    }

    override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
        removeSpan(what)
        spans.add(SpanRec(what, start, end, flags))
    }

    override fun removeSpan(what: Any?) {
        spans.removeAll { it.what === what }
    }

    override fun getChars(start: Int, end: Int, dest: CharArray, destoff: Int) {
        val s = text.toString()
        for (i in start until end) dest[destoff + i - start] = s[i]
    }

    companion object {
        @JvmStatic
        fun valueOf(source: CharSequence?): SpannableString =
            if (source is SpannableString) source else SpannableString(source ?: "")
    }
}

/** android.text.SpannableStringBuilder：可变内容 + span 跟随偏移。 */
open class SpannableStringBuilder : SpannedBase, Editable, GetChars {

    private val sb = StringBuilder()
    override val text: CharSequence get() = sb.toString()

    constructor()
    constructor(text: CharSequence) {
        sb.append(text)
        if (text is Spanned) {
            for (span in text.getSpans(0, text.length, Any::class.java)) {
                val st = text.getSpanStart(span)
                val en = text.getSpanEnd(span)
                if (st >= 0 && en >= st) spans.add(SpanRec(span, st, en, text.getSpanFlags(span)))
            }
        }
    }
    constructor(text: CharSequence, start: Int, end: Int) {
        sb.append(text.subSequence(start, end))
    }

    private var filters: Array<InputFilter> = emptyArray()

    override fun setFilters(filters: Array<InputFilter>) { this.filters = filters }
    override fun getFilters(): Array<InputFilter> = filters

    override val length: Int get() = sb.length
    override fun get(index: Int): Char = sb[index]
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = sb.subSequence(startIndex, endIndex)
    fun substring(start: Int): String = sb.substring(start)
    fun substring(start: Int, end: Int): String = sb.substring(start, end)
    override fun toString(): String = sb.toString()

    /** 插入/删除后调整 span 边界。 */
    private fun adjustSpans(offset: Int, delta: Int) {
        for (rec in spans) {
            if (rec.start >= offset) rec.start = (rec.start + delta).coerceAtLeast(0)
            if (rec.end >= offset) rec.end = (rec.end + delta).coerceAtLeast(0)
        }
        spans.removeAll { it.start > it.end }
    }

    override fun append(text: CharSequence): Editable = insert(sb.length, text)
    override fun append(text: CharSequence, start: Int, end: Int): Editable = insert(sb.length, text, start, end)
    override fun append(c: Char): Editable {
        sb.append(c)
        adjustSpans(sb.length - 1, 1)
        return this
    }

    fun append(text: CharSequence, what: Any, flags: Int): SpannableStringBuilder {
        val start = sb.length
        append(text)
        setSpan(what, start, sb.length, flags)
        return this
    }

    override fun insert(where: Int, text: CharSequence): Editable = insert(where, text, 0, text.length)

    override fun insert(where: Int, text: CharSequence, start: Int, end: Int): Editable {
        val len = end - start
        sb.insert(where.coerceIn(0, sb.length), text.subSequence(start, end))
        adjustSpans(where, len)
        return this
    }

    override fun delete(st: Int, en: Int): Editable = replace(st, en, "", 0, 0)

    override fun replace(st: Int, en: Int, text: CharSequence): Editable = replace(st, en, text, 0, text.length)

    override fun replace(st: Int, en: Int, source: CharSequence, start: Int, end: Int): Editable {
        val s = st.coerceIn(0, sb.length); val e = en.coerceIn(s, sb.length)
        sb.replace(s, e, source.subSequence(start, end).toString())
        adjustSpans(e, (end - start) - (e - s))
        return this
    }

    override fun clear() {
        sb.setLength(0)
        spans.clear()
    }

    override fun clearSpans() {
        spans.clear()
    }

    override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
        removeSpan(what)
        spans.add(SpanRec(what, start, end, flags))
    }

    override fun removeSpan(what: Any?) {
        spans.removeAll { it.what === what }
    }

    fun removeSpan(what: Any?, flags: Int) = removeSpan(what)

    override fun getChars(start: Int, end: Int, dest: CharArray, destoff: Int) {
        for (i in start until end) dest[destoff + i - start] = sb[i]
    }

    companion object {
        @JvmStatic
        fun valueOf(source: CharSequence?): SpannableStringBuilder = SpannableStringBuilder(source ?: "")
    }
}

/** android.text.SpannedString：完全不可变。 */
class SpannedString(content: CharSequence) : SpannedBase(), GetChars {
    private val content: CharSequence = SpannableString.valueOf(content)
    override val text: CharSequence get() = content

    override fun getChars(start: Int, end: Int, dest: CharArray, destoff: Int) {
        (content as? GetChars)?.getChars(start, end, dest, destoff)
            ?: run { val s = content.toString(); for (i in start until end) dest[destoff + i - start] = s[i] }
    }

    companion object {
        @JvmStatic
        fun valueOf(source: CharSequence?): SpannedString =
            if (source is SpannedString) source else SpannedString(source ?: "")
    }
}

/** android.text.Selection：基于 marker span 的光标管理。 */
object Selection {
    private object START : NoCopySpan
    private object END : NoCopySpan

    @JvmField val SELECTION_START: Any = START
    @JvmField val SELECTION_END: Any = END

    @JvmStatic
    fun getSelectionStart(text: CharSequence?): Int {
        if (text !is Spannable) return -1
        val start = text.getSpanStart(SELECTION_START)
        return start
    }

    @JvmStatic
    fun getSelectionEnd(text: CharSequence?): Int {
        if (text !is Spannable) return -1
        return text.getSpanStart(SELECTION_END)
    }

    @JvmStatic
    fun setSelection(text: Spannable, start: Int, stop: Int) {
        text.setSpan(SELECTION_START, start, start, Spanned.SPAN_POINT_POINT)
        text.setSpan(SELECTION_END, stop, stop, Spanned.SPAN_POINT_POINT)
    }

    @JvmStatic
    fun setSelection(text: Spannable, index: Int) = setSelection(text, index, index)

    @JvmStatic
    fun selectAll(text: Spannable) = setSelection(text, 0, text.length)

    @JvmStatic
    fun removeSelection(text: Spannable) {
        text.removeSpan(SELECTION_START)
        text.removeSpan(SELECTION_END)
    }

    @JvmStatic
    fun extendSelection(text: Spannable, index: Int) {
        text.setSpan(SELECTION_END, index, index, Spanned.SPAN_POINT_POINT)
    }

    @JvmStatic fun moveUp(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun moveDown(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun moveLeft(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun moveRight(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun extendUp(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun extendDown(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun extendLeft(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun extendRight(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun moveToStart(text: Spannable, layout: Any?): Boolean = false
    @JvmStatic fun moveToEnd(text: Spannable, layout: Any?): Boolean = false
}
