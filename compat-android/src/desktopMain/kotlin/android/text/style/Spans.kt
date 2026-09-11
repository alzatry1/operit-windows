package android.text.style

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.TextPaint
import android.view.View

/** android.text.style.ParagraphStyle：标记接口。 */
interface ParagraphStyle

/** android.text.style.ParagraphStyle 换行变体。 */
interface WrapTogetherSpan : ParagraphStyle

/** android.text.style.LeadingMarginSpan。 */
interface LeadingMarginSpan : ParagraphStyle {
    fun getLeadingMargin(first: Boolean): Int
    fun drawLeadingMargin(
        c: android.graphics.Canvas?, p: Paint?, x: Int, dir: Int,
        top: Int, baseline: Int, bottom: Int,
        text: CharSequence?, start: Int, end: Int, first: Boolean, layout: Layout?,
    ) {
    }

    class Standard : LeadingMarginSpan {
        private val first: Int
        private val rest: Int

        constructor(first: Int, rest: Int) { this.first = first; this.rest = rest }
        constructor(every: Int) : this(every, every)
        constructor(src: android.os.Parcel) : this(src.readInt(), src.readInt())

        override fun getLeadingMargin(first: Boolean): Int = if (first) this.first else this.rest
    }
}

/** android.text.style.LineBackgroundSpan。 */
interface LineBackgroundSpan : ParagraphStyle {
    fun drawBackground(
        c: android.graphics.Canvas, p: Paint, left: Int, right: Int,
        top: Int, baseline: Int, bottom: Int, text: CharSequence,
        start: Int, end: Int, lineNumber: Int,
    )
}

/** android.text.style.LineHeightSpan。 */
interface LineHeightSpan : ParagraphStyle {
    fun chooseHeight(
        text: CharSequence, start: Int, end: Int,
        spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt,
    )
}

/** android.text.style.TabStopSpan。 */
interface TabStopSpan : ParagraphStyle {
    fun getTabStop(): Int

    class Standard(private val tab: Int) : TabStopSpan {
        override fun getTabStop(): Int = tab
    }
}

/** android.text.style.AlignmentSpan。 */
interface AlignmentSpan : ParagraphStyle {
    fun getAlignment(): Layout.Alignment

    class Standard : AlignmentSpan {
        private val alignment: Layout.Alignment

        constructor(align: Layout.Alignment) { alignment = align }
        constructor(src: android.os.Parcel) : this(Layout.Alignment.ALIGN_NORMAL)

        override fun getAlignment(): Layout.Alignment = alignment
    }
}

/** android.text.style.CharacterStyle：updateDrawState 基类。 */
abstract class CharacterStyle {
    abstract fun updateDrawState(tp: TextPaint?)

    open fun updateMeasureState(tp: TextPaint) {}

    companion object {
        @JvmStatic
        fun wrap(cs: CharacterStyle?): CharacterStyle = cs ?: object : CharacterStyle() {
            override fun updateDrawState(tp: TextPaint?) {}
        }
    }
}

/** android.text.style.MetricAffectingSpan。 */
abstract class MetricAffectingSpan : CharacterStyle() {
    abstract override fun updateMeasureState(tp: TextPaint)

    override fun updateDrawState(tp: TextPaint?) {
        if (tp != null) updateMeasureState(tp)
    }
}

/** android.text.style.ReplacementSpan。 */
abstract class ReplacementSpan : MetricAffectingSpan() {
    abstract fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int
    abstract fun draw(
        canvas: android.graphics.Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint,
    )

    override fun updateMeasureState(tp: TextPaint) {}
    override fun updateDrawState(tp: TextPaint?) {}
}

/** android.text.style.ClickableSpan。 */
abstract class ClickableSpan : CharacterStyle() {
    abstract fun onClick(widget: View)

    override fun updateDrawState(ds: TextPaint?) {
        ds?.let {
            it.color = it.linkColor
            it.isUnderlineText = true
        }
    }
}

/** android.text.style.ForegroundColorSpan。 */
open class ForegroundColorSpan(private val color: Int) : CharacterStyle(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this(src.readInt())

    fun getForegroundColor(): Int = color
    override fun updateDrawState(tp: TextPaint?) { tp?.color = color }
    override fun getSpanTypeId(): Int = 2
}

/** android.text.style.BackgroundColorSpan。 */
open class BackgroundColorSpan(private val color: Int) : CharacterStyle(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this(src.readInt())

    fun getBackgroundColor(): Int = color
    override fun updateDrawState(tp: TextPaint?) { tp?.bgColor = color }
    override fun getSpanTypeId(): Int = 3
}

/** android.text.style.StyleSpan。 */
open class StyleSpan(private val style: Int) : MetricAffectingSpan(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this(src.readInt())

    fun getStyle(): Int = style

    override fun updateDrawState(tp: TextPaint?) { tp?.let { applyStyle(it) } }
    override fun updateMeasureState(tp: TextPaint) { applyStyle(tp) }

    private fun applyStyle(paint: TextPaint) {
        if (style and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
            val tf = paint.typeface
            paint.typeface = Typeface.create(tf, Typeface.BOLD)
        }
        if (style and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
    }

    override fun getSpanTypeId(): Int = 1
}

/** android.text.style.TypefaceSpan。 */
open class TypefaceSpan : MetricAffectingSpan, ParcelableSpan {
    private val family: String?
    private val typeface: Typeface?

    constructor(family: String?) { this.family = family; typeface = null }
    constructor(typeface: Typeface?) { this.family = null; this.typeface = typeface }
    constructor(src: android.os.Parcel) : this(src.readString())

    fun getFamily(): String? = family
    fun getTypeface(): Typeface? = typeface

    override fun updateDrawState(tp: TextPaint?) { tp?.let { apply(it) } }
    override fun updateMeasureState(tp: TextPaint) { apply(tp) }

    private fun apply(paint: TextPaint) {
        val tf = typeface
        if (tf != null) paint.typeface = tf
        else family?.let { paint.typeface = Typeface.create(it, Typeface.NORMAL) }
    }

    override fun getSpanTypeId(): Int = 13
}

/** android.text.style.RelativeSizeSpan。 */
open class RelativeSizeSpan(proportion: Float) : MetricAffectingSpan(), ParcelableSpan {
    private val proportion: Float = proportion

    constructor(src: android.os.Parcel) : this(src.readFloat())

    fun getSizeChange(): Float = proportion

    override fun updateDrawState(tp: TextPaint?) { tp?.let { it.textSize = it.textSize * proportion } }
    override fun updateMeasureState(tp: TextPaint) { tp.textSize = tp.textSize * proportion }
    override fun getSpanTypeId(): Int = 4
}

/** android.text.style.AbsoluteSizeSpan。 */
open class AbsoluteSizeSpan : MetricAffectingSpan, ParcelableSpan {
    private val size: Int
    private val dip: Boolean

    constructor(size: Int) { this.size = size; this.dip = false }
    constructor(size: Int, dip: Boolean) { this.size = size; this.dip = dip }
    constructor(src: android.os.Parcel) : this(src.readInt(), src.readInt() != 0)

    fun getSize(): Int = size
    fun isDip(): Boolean = dip

    private fun scaledSize(): Float = if (dip) size * 1.25f else size.toFloat()

    override fun updateDrawState(tp: TextPaint?) { tp?.let { it.textSize = scaledSize() } }
    override fun updateMeasureState(tp: TextPaint) { tp.textSize = scaledSize() }
    override fun getSpanTypeId(): Int = 16
}

/** android.text.style.UnderlineSpan。 */
open class UnderlineSpan() : CharacterStyle(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this()

    override fun updateDrawState(tp: TextPaint?) { tp?.isUnderlineText = true }
    override fun getSpanTypeId(): Int = 6
}

/** android.text.style.StrikethroughSpan。 */
open class StrikethroughSpan() : CharacterStyle(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this()

    override fun updateDrawState(tp: TextPaint?) { tp?.isStrikeThruText = true }
    override fun getSpanTypeId(): Int = 5
}

/** android.text.style.ScaleXSpan。 */
open class ScaleXSpan(proportion: Float) : MetricAffectingSpan(), ParcelableSpan {
    private val proportion: Float = proportion

    constructor(src: android.os.Parcel) : this(src.readFloat())

    fun getScaleX(): Float = proportion

    override fun updateDrawState(tp: TextPaint?) { tp?.let { it.textScaleX = it.textScaleX * proportion } }
    override fun updateMeasureState(tp: TextPaint) { tp.textScaleX = tp.textScaleX * proportion }
    override fun getSpanTypeId(): Int = 15
}

/** android.text.style.MaskFilterSpan。 */
open class MaskFilterSpan(private val filter: android.graphics.MaskFilter) : CharacterStyle(), ParcelableSpan {
    fun getMaskFilter(): android.graphics.MaskFilter = filter
    override fun updateDrawState(tp: TextPaint?) { tp?.maskFilter = filter }
    override fun getSpanTypeId(): Int = 10
}

/** android.text.style.SuperscriptSpan。 */
open class SuperscriptSpan() : MetricAffectingSpan(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this()

    override fun updateDrawState(tp: TextPaint?) { tp?.let { it.baselineShift += (it.textSize / 2).toInt() } }
    override fun updateMeasureState(tp: TextPaint) { tp.baselineShift += (tp.textSize / 2).toInt() }
    override fun getSpanTypeId(): Int = 14
}

/** android.text.style.SubscriptSpan。 */
open class SubscriptSpan() : MetricAffectingSpan(), ParcelableSpan {
    constructor(src: android.os.Parcel) : this()

    override fun updateDrawState(tp: TextPaint?) { tp?.let { it.baselineShift -= (it.textSize / 4).toInt() } }
    override fun updateMeasureState(tp: TextPaint) { tp.baselineShift -= (tp.textSize / 4).toInt() }
    override fun getSpanTypeId(): Int = 14
}

/** android.text.style.URLSpan：点击尝试打开浏览器。 */
open class URLSpan() : ClickableSpan(), ParcelableSpan {
    private var url: String? = null

    constructor(url: String?) : this() { this.url = url }
    constructor(src: android.os.Parcel) : this() { url = src.readString() }

    fun getURL(): String? = url

    override fun onClick(widget: View) {
        val u = url ?: return
        try {
            val uri = android.net.Uri.parse(u)
            if (uri.scheme == "http" || uri.scheme == "https") {
                java.awt.Desktop.getDesktop().browse(java.net.URI(u))
            }
        } catch (t: Throwable) {
            android.util.Log.w("URLSpan", "onClick 打开链接失败: $u: ${t.message}")
        }
    }

    override fun getSpanTypeId(): Int = 11
}

/** android.text.style.TextAppearanceSpan 轻实现。 */
open class TextAppearanceSpan(
    private val family: String?,
    private val style: Int,
    private val size: Int,
    private val color: Int?,
) : MetricAffectingSpan(), ParcelableSpan {
    constructor(context: android.content.Context, appearance: Int) : this(null, 0, -1, null)
    constructor(context: android.content.Context, appearance: Int, colorList: Int) : this(null, 0, -1, null)

    override fun updateDrawState(tp: TextPaint?) { tp?.let { apply(it) } }
    override fun updateMeasureState(tp: TextPaint) { apply(tp) }

    private fun apply(paint: TextPaint) {
        family?.let { paint.typeface = Typeface.create(it, style) }
        if (size > 0) paint.textSize = size.toFloat()
        color?.let { paint.color = it }
    }

    override fun getSpanTypeId(): Int = 17
}

/** android.text.style.BulletSpan。 */
open class BulletSpan : LeadingMarginSpan, ParcelableSpan {
    private val gapWidth: Int
    private val color: Int
    private val bulletRadius: Float

    constructor() : this(STANDARD_GAP_WIDTH, 0, STANDARD_BULLET_RADIUS)
    constructor(gapWidth: Int) : this(gapWidth, 0, STANDARD_BULLET_RADIUS)
    constructor(gapWidth: Int, color: Int) : this(gapWidth, color, STANDARD_BULLET_RADIUS)
    constructor(gapWidth: Int, color: Int, bulletRadius: Float) {
        this.gapWidth = gapWidth; this.color = color; this.bulletRadius = bulletRadius
    }
    constructor(src: android.os.Parcel) : this(src.readInt(), src.readInt(), src.readFloat())

    fun getGapWidth(): Int = gapWidth
    fun getBulletRadius(): Float = bulletRadius

    override fun getLeadingMargin(first: Boolean): Int = gapWidth

    override fun getSpanTypeId(): Int = 8

    companion object {
        const val STANDARD_GAP_WIDTH = 2
        const val STANDARD_BULLET_RADIUS = 1.6f
    }
}

/** android.text.style.QuoteSpan。 */
open class QuoteSpan : LeadingMarginSpan, ParagraphStyle, ParcelableSpan {
    private val color: Int

    constructor() : this(0xff0000ff.toInt())
    constructor(color: Int) { this.color = color }
    constructor(src: android.os.Parcel) : this(src.readInt())

    fun getColor(): Int = color

    override fun getLeadingMargin(first: Boolean): Int = 2 + 2
    override fun getSpanTypeId(): Int = 9
}

/** android.text.style.ImageSpan。 */
open class ImageSpan : DynamicDrawableSpan {
    private val drawable: android.graphics.drawable.Drawable?
    private val source: String?
    private val context: android.content.Context?

    constructor(d: android.graphics.drawable.Drawable) : this(d, null)
    constructor(d: android.graphics.drawable.Drawable, source: String?) : super() {
        this.drawable = d; this.source = source; this.context = null
    }
    constructor(d: android.graphics.drawable.Drawable, source: String?, verticalAlignment: Int) : super(verticalAlignment) {
        this.drawable = d; this.source = source; this.context = null
    }
    constructor(d: android.graphics.drawable.Drawable, verticalAlignment: Int) : this(d, null, verticalAlignment)
    constructor(context: android.content.Context, bitmap: android.graphics.Bitmap) : super() {
        this.drawable = android.graphics.drawable.BitmapDrawable(bitmap); this.source = null; this.context = context
    }
    constructor(context: android.content.Context, bitmap: android.graphics.Bitmap, verticalAlignment: Int) : super(verticalAlignment) {
        this.drawable = android.graphics.drawable.BitmapDrawable(bitmap); this.source = null; this.context = context
    }
    constructor(context: android.content.Context, uri: android.net.Uri) : super() {
        this.drawable = null; this.source = uri.toString(); this.context = context
    }
    constructor(context: android.content.Context, resourceId: Int) : super() {
        this.drawable = null; this.source = "res:$resourceId"; this.context = context
    }

    override fun getDrawable(): android.graphics.drawable.Drawable? = drawable
    fun getSource(): String? = source
}

/** android.text.style.DynamicDrawableSpan。 */
abstract class DynamicDrawableSpan : ReplacementSpan {
    protected val verticalAlignment: Int

    constructor() { verticalAlignment = ALIGN_BOTTOM }
    constructor(verticalAlignment: Int) { this.verticalAlignment = verticalAlignment }

    abstract fun getDrawable(): android.graphics.drawable.Drawable?

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val d = getDrawable() ?: return 0
        val rect = d.bounds
        fm?.let {
            it.ascent = -rect.height(); it.descent = 0
            it.top = it.ascent; it.bottom = 0
        }
        return rect.width()
    }

    override fun draw(
        canvas: android.graphics.Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint,
    ) {
        val d = getDrawable() ?: return
        canvas.save()
        val transY = bottom - d.bounds.height().toFloat() - paint.fontMetricsInt.descent
        canvas.translate(x, transY)
        d.draw(canvas)
        canvas.restore()
    }

    companion object {
        const val ALIGN_BOTTOM = 0
        const val ALIGN_BASELINE = 1
        const val ALIGN_CENTER = 2
    }
}

/** android.text.style.LocaleSpan 轻 stub。 */
class LocaleSpan(private val locales: android.os.LocaleList?) : MetricAffectingSpan() {
    constructor(locale: java.util.Locale?) : this(locale?.let { android.os.LocaleList.of(it) })
    fun getLocales(): android.os.LocaleList? = locales
    override fun updateDrawState(tp: TextPaint?) {}
    override fun updateMeasureState(tp: TextPaint) {}
}

/** android.text.style.SuggestionSpan / EasyEditSpan / TtsSpan 占位。 */
class SuggestionSpan : CharacterStyle() {
    override fun updateDrawState(tp: TextPaint?) {}
}
