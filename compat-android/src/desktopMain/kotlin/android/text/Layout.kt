package android.text

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

/**
 * android.text.Layout：抽象排版基类；StaticLayout：贪心换行的真实排版实现。
 */
abstract class Layout protected constructor(
    val text: CharSequence,
    val paint: TextPaint,
    val width: Int,
    val alignment: Alignment,
    val spacingMult: Float,
    val spacingAdd: Float,
) {

    enum class Alignment { ALIGN_NORMAL, ALIGN_OPPOSITE, ALIGN_CENTER }

    // ---- 抽象行信息 ----
    abstract fun getLineCount(): Int
    abstract fun getLineTop(line: Int): Int
    abstract fun getLineStart(line: Int): Int
    abstract fun getParagraphDirection(line: Int): Int
    abstract fun getLineContainsTab(line: Int): Boolean
    abstract fun getTopPadding(): Int
    abstract fun getBottomPadding(): Int

    // ---- 派生几何 ----
    open val height: Int get() = getLineTop(getLineCount())
    open val widthCompat: Int get() = width

    open fun getLineDescent(line: Int): Int {
        val fm = paint.fontMetricsInt
        return fm.descent
    }

    open fun getLineBottom(line: Int): Int = getLineTop(line + 1)

    open fun getLineBaseline(line: Int): Int {
        val fm = paint.fontMetricsInt
        return getLineTop(line + 1) - fm.descent
    }

    open fun getLineEnd(line: Int): Int =
        if (line + 1 < getLineCount()) getLineStart(line + 1) else text.length

    open fun getLineVisibleEnd(line: Int): Int = getLineEnd(line)

    open fun getLineForVertical(vertical: Int): Int {
        var line = 0
        val count = getLineCount()
        while (line < count) {
            if (getLineTop(line + 1) > vertical) break
            line++
        }
        return (line).coerceIn(0, (count - 1).coerceAtLeast(0))
    }

    open fun getLineForOffset(offset: Int): Int {
        var line = 0
        val count = getLineCount()
        while (line < count - 1 && getLineStart(line + 1) <= offset) line++
        return line
    }

    /** offset 处字符的 x 坐标（按对齐方式修正）。 */
    open fun getPrimaryHorizontal(offset: Int): Float {
        val clamped = offset.coerceIn(0, text.length)
        val line = getLineForOffset(clamped)
        val start = getLineStart(line)
        val end = getLineEnd(line)
        val textEnd = clamped.coerceAtMost(end)
        val measured = paint.measureText(text, start, textEnd)
        val lineWidth = paint.measureText(text, start, end)
        return when (alignment) {
            Alignment.ALIGN_NORMAL -> measured
            Alignment.ALIGN_OPPOSITE -> width - lineWidth + measured
            Alignment.ALIGN_CENTER -> (width - lineWidth) / 2f + measured
        }
    }

    open fun getSecondaryHorizontal(offset: Int): Float = getPrimaryHorizontal(offset)

    /** x 坐标 → offset。 */
    open fun getOffsetForHorizontal(line: Int, horiz: Float): Int {
        val start = getLineStart(line)
        val end = getLineEnd(line)
        val x = when (alignment) {
            Alignment.ALIGN_NORMAL -> horiz
            Alignment.ALIGN_OPPOSITE -> horiz - (width - paint.measureText(text, start, end))
            Alignment.ALIGN_CENTER -> horiz - (width - paint.measureText(text, start, end)) / 2f
        }
        // 逐字符逼近
        var acc = 0f
        for (i in start until end) {
            val w = paint.measureText(text, i, i + 1)
            if (acc + w / 2 > x) return i
            acc += w
        }
        return end
    }

    open fun getLineWidth(line: Int): Float {
        val start = getLineStart(line)
        val end = getLineEnd(line)
        return paint.measureText(text, start, end)
    }

    open fun getLineMax(line: Int): Float = getLineWidth(line)

    open fun getEllipsizedWidth(): Int = width

    open fun getOffsetToLeftOf(offset: Int): Int = (offset - 1).coerceAtLeast(0)
    open fun getOffsetToRightOf(offset: Int): Int = (offset + 1).coerceAtMost(text.length)

    open fun getParagraphLeft(line: Int): Int = 0
    open fun getParagraphRight(line: Int): Int = width

    open fun getSelectionPath(start: Int, end: Int, dest: android.graphics.Path) {}

    // ---- 绘制 ----
    open fun draw(c: Canvas?) {
        if (c == null) return
        val count = getLineCount()
        for (line in 0 until count) {
            val start = getLineStart(line)
            val end = getLineEnd(line)
            if (end <= start) continue
            val baseline = getLineBaseline(line).toFloat()
            val x = when (alignment) {
                Alignment.ALIGN_NORMAL -> 0f
                Alignment.ALIGN_OPPOSITE -> width - paint.measureText(text, start, end)
                Alignment.ALIGN_CENTER -> (width - paint.measureText(text, start, end)) / 2f
            }
            c.drawText(text, start, end, x, baseline, paint)
        }
    }

    open fun draw(c: Canvas, highlight: android.graphics.Path?, highlightPaint: Paint?, cursorOffsetVertical: Int) =
        draw(c)

    /** skia.Canvas 重载：app 直接传 canvas.nativeCanvas（Compose Desktop 底层）。包装成 android.graphics.Canvas 委托。——Nova 注 */
    open fun draw(c: org.jetbrains.skia.Canvas?) {
        if (c == null) return
        draw(android.graphics.Canvas(c))
    }

    companion object {
        const val DIR_LEFT_TO_RIGHT = 1
        const val DIR_RIGHT_TO_LEFT = -1
        const val DIR_REQUEST_LTR = 1
        const val DIR_REQUEST_RTL = -1
        const val DIR_REQUEST_DEFAULT_LTR = 2
        const val DIR_REQUEST_DEFAULT_RTL = -2

        const val BREAK_STRATEGY_SIMPLE = 0
        const val BREAK_STRATEGY_HIGH_QUALITY = 1
        const val BREAK_STRATEGY_BALANCED = 2
        const val HYPHENATION_FREQUENCY_NONE = 0
        const val HYPHENATION_FREQUENCY_NORMAL = 1
        const val HYPHENATION_FREQUENCY_FULL = 2
        const val JUSTIFICATION_MODE_NONE = 0
        const val JUSTIFICATION_MODE_INTER_WORD = 1
        const val DIRECTION_ALL_LTR = 0
        const val DIRECTION_ALL_RTL = 1
        const val DEFAULT_LINESPACING_MULTIPLIER = 1.0f
        const val DEFAULT_LINESPACING_EXTRA = 0f

        @JvmStatic
        fun getDesiredWidth(source: CharSequence, paint: TextPaint): Float {
            var max = 0f
            var start = 0
            val s = source.toString()
            for (i in s.indices) {
                if (s[i] == '\n') {
                    max = maxOf(max, paint.measureText(source, start, i))
                    start = i + 1
                }
            }
            max = maxOf(max, paint.measureText(source, start, s.length))
            return max
        }
    }
}

/**
 * android.text.StaticLayout：贪心按词换行（CJK 退化为按字）。
 */
open class StaticLayout : Layout {

    private val lineStarts: IntArray
    private val mLineCount: Int
    private val lineHeightPx: Int
    private val topPad: Int
    private val bottomPad: Int
    private val includePad: Boolean

    private constructor(
        source: CharSequence, paint: TextPaint, width: Int, align: Alignment,
        spacingmult: Float, spacingadd: Float, includepad: Boolean,
        ellipsize: TextUtils.TruncateAt?, ellipsizedWidth: Int, maxLines: Int,
    ) : super(source, paint, width, align, spacingmult, spacingadd) {
        this.includePad = includepad
        val fm = paint.fontMetricsInt
        val lineHeight = (fm.bottom - fm.top)
        this.lineHeightPx = (lineHeight * spacingmult + spacingadd).toInt().coerceAtLeast(1)
        this.topPad = if (includepad) fm.top else 0
        this.bottomPad = if (includepad) fm.bottom else 0

        // 贪心换行
        val starts = ArrayList<Int>()
        starts.add(0)
        val s = source
        var lineStart = 0
        var i = 0
        while (i < s.length) {
            if (s[i] == '\n') {
                starts.add(i + 1)
                lineStart = i + 1
                i++
                continue
            }
            val used = paint.measureText(s, lineStart, i + 1)
            if (used > width && i > lineStart) {
                // 回退到最近的可断点（空格/连字符），否则 CJK 按字断
                var breakAt = i
                for (j in i downTo lineStart + 1) {
                    val c = s[j - 1]
                    if (c == ' ' || c == '\t' || c == '-') { breakAt = j; break }
                }
                starts.add(breakAt)
                lineStart = breakAt
                i = breakAt
                continue
            }
            i++
        }
        // 限高截断（maxLines）
        val finalStarts = if (maxLines in 1 until starts.size) starts.subList(0, maxLines).toList() else starts
        this.lineStarts = finalStarts.toIntArray()
        this.mLineCount = lineStarts.size
    }

    constructor(
        source: CharSequence, paint: TextPaint, width: Int, align: Alignment,
        spacingmult: Float, spacingadd: Float, includepad: Boolean,
    ) : this(source, paint, width, align, spacingmult, spacingadd, includepad, null, 0, Int.MAX_VALUE)

    constructor(
        source: CharSequence, paint: TextPaint, width: Int, align: Alignment,
        spacingmult: Float, spacingadd: Float, includepad: Boolean,
        ellipsize: TextUtils.TruncateAt?, ellipsizedWidth: Int,
    ) : this(source, paint, width, align, spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Int.MAX_VALUE)

    override fun getLineCount(): Int = mLineCount
    override fun getLineTop(line: Int): Int = topPad + line * lineHeightPx
    override fun getLineStart(line: Int): Int = if (line in lineStarts.indices) lineStarts[line] else text.length
    override fun getParagraphDirection(line: Int): Int = DIR_LEFT_TO_RIGHT
    override fun getLineContainsTab(line: Int): Boolean = false
    override fun getTopPadding(): Int = topPad
    override fun getBottomPadding(): Int = bottomPad
    fun getEllipsisStart(line: Int): Int = 0
    fun getEllipsisCount(line: Int): Int = 0
    fun getEllipsis(line: Int): Int = 0

    /** android.text.StaticLayout.Builder。 */
    class Builder private constructor(
        private val source: CharSequence,
        private val start: Int,
        private val end: Int,
        private val paint: TextPaint,
        private val width: Int,
    ) {
        private var alignment: Alignment = Alignment.ALIGN_NORMAL
        private var spacingMult = DEFAULT_LINESPACING_MULTIPLIER
        private var spacingAdd = DEFAULT_LINESPACING_EXTRA
        private var includePad = true
        private var ellipsize: TextUtils.TruncateAt? = null
        private var ellipsizedWidth = width
        private var maxLines = Int.MAX_VALUE

        fun setAlignment(alignment: Alignment): Builder = apply { this.alignment = alignment }
        fun setLineSpacing(spacingAdd: Float, spacingMult: Float): Builder = apply {
            this.spacingAdd = spacingAdd; this.spacingMult = spacingMult
        }
        fun setIncludePad(includePad: Boolean): Builder = apply { this.includePad = includePad }
        fun setEllipsize(ellipsize: TextUtils.TruncateAt?): Builder = apply { this.ellipsize = ellipsize }
        fun setEllipsizedWidth(ellipsizedWidth: Int): Builder = apply { this.ellipsizedWidth = ellipsizedWidth }
        fun setMaxLines(maxLines: Int): Builder = apply { this.maxLines = maxLines }
        fun setBreakStrategy(breakStrategy: Int): Builder = this
        fun setHyphenationFrequency(hyphenationFrequency: Int): Builder = this
        fun setJustificationMode(justificationMode: Int): Builder = this
        fun setUseLineSpacingFromFallbacks(useLineSpacingFromFallbacks: Boolean): Builder = this
        fun setShiftDrawingOffsetForStartOverhang(shift: Boolean): Builder = this
        fun setText(text: CharSequence): Builder = this

        fun build(): StaticLayout = StaticLayout(
            source.subSequence(start, end), paint, width, alignment,
            spacingMult, spacingAdd, includePad, ellipsize, ellipsizedWidth, maxLines,
        )

        companion object {
            private const val DEFAULT_LINESPACING_MULTIPLIER = 1.0f
            private const val DEFAULT_LINESPACING_EXTRA = 0f

            @JvmStatic
            fun obtain(source: CharSequence, start: Int, end: Int, paint: TextPaint, width: Int): Builder =
                Builder(source, start, end, paint, width)
        }
    }

    companion object {
        private const val DEFAULT_LINESPACING_MULTIPLIER = 1.0f
        private const val DEFAULT_LINESPACING_EXTRA = 0f
    }
}
