package android.graphics

import android.content.res.AssetManager

/** android.graphics.PorterDuff：混合模式枚举。 */
object PorterDuff {
    enum class Mode {
        CLEAR, SRC, DST, SRC_OVER, DST_OVER, SRC_IN, DST_IN, SRC_OUT, DST_OUT,
        SRC_ATOP, DST_ATOP, XOR, DARKEN, LIGHTEN, MULTIPLY, SCREEN, ADD, OVERLAY;

        internal fun toSkia(): org.jetbrains.skia.BlendMode = when (this) {
            CLEAR -> org.jetbrains.skia.BlendMode.CLEAR
            SRC -> org.jetbrains.skia.BlendMode.SRC
            DST -> org.jetbrains.skia.BlendMode.DST
            SRC_OVER -> org.jetbrains.skia.BlendMode.SRC_OVER
            DST_OVER -> org.jetbrains.skia.BlendMode.DST_OVER
            SRC_IN -> org.jetbrains.skia.BlendMode.SRC_IN
            DST_IN -> org.jetbrains.skia.BlendMode.DST_IN
            SRC_OUT -> org.jetbrains.skia.BlendMode.SRC_OUT
            DST_OUT -> org.jetbrains.skia.BlendMode.DST_OUT
            SRC_ATOP -> org.jetbrains.skia.BlendMode.SRC_ATOP
            DST_ATOP -> org.jetbrains.skia.BlendMode.DST_ATOP
            XOR -> org.jetbrains.skia.BlendMode.XOR
            DARKEN -> org.jetbrains.skia.BlendMode.DARKEN
            LIGHTEN -> org.jetbrains.skia.BlendMode.LIGHTEN
            MULTIPLY -> org.jetbrains.skia.BlendMode.MULTIPLY
            SCREEN -> org.jetbrains.skia.BlendMode.SCREEN
            ADD -> org.jetbrains.skia.BlendMode.PLUS
            OVERLAY -> org.jetbrains.skia.BlendMode.OVERLAY
        }
    }
}

/** android.graphics.BlendMode → skia BlendMode（Primitives.kt 中定义枚举）。 */
internal fun BlendMode.toSkia(): org.jetbrains.skia.BlendMode = when (this) {
    BlendMode.CLEAR -> org.jetbrains.skia.BlendMode.CLEAR
    BlendMode.SRC -> org.jetbrains.skia.BlendMode.SRC
    BlendMode.DST -> org.jetbrains.skia.BlendMode.DST
    BlendMode.SRC_OVER -> org.jetbrains.skia.BlendMode.SRC_OVER
    BlendMode.DST_OVER -> org.jetbrains.skia.BlendMode.DST_OVER
    BlendMode.SRC_IN -> org.jetbrains.skia.BlendMode.SRC_IN
    BlendMode.DST_IN -> org.jetbrains.skia.BlendMode.DST_IN
    BlendMode.SRC_OUT -> org.jetbrains.skia.BlendMode.SRC_OUT
    BlendMode.DST_OUT -> org.jetbrains.skia.BlendMode.DST_OUT
    BlendMode.SRC_ATOP -> org.jetbrains.skia.BlendMode.SRC_ATOP
    BlendMode.DST_ATOP -> org.jetbrains.skia.BlendMode.DST_ATOP
    BlendMode.XOR -> org.jetbrains.skia.BlendMode.XOR
    BlendMode.PLUS -> org.jetbrains.skia.BlendMode.PLUS
    BlendMode.MODULATE -> org.jetbrains.skia.BlendMode.MODULATE
    BlendMode.SCREEN -> org.jetbrains.skia.BlendMode.SCREEN
    BlendMode.OVERLAY -> org.jetbrains.skia.BlendMode.OVERLAY
    BlendMode.DARKEN -> org.jetbrains.skia.BlendMode.DARKEN
    BlendMode.LIGHTEN -> org.jetbrains.skia.BlendMode.LIGHTEN
    BlendMode.COLOR_DODGE -> org.jetbrains.skia.BlendMode.COLOR_DODGE
    BlendMode.COLOR_BURN -> org.jetbrains.skia.BlendMode.COLOR_BURN
    BlendMode.HARD_LIGHT -> org.jetbrains.skia.BlendMode.HARD_LIGHT
    BlendMode.SOFT_LIGHT -> org.jetbrains.skia.BlendMode.SOFT_LIGHT
    BlendMode.DIFFERENCE -> org.jetbrains.skia.BlendMode.DIFFERENCE
    BlendMode.EXCLUSION -> org.jetbrains.skia.BlendMode.EXCLUSION
    BlendMode.MULTIPLY -> org.jetbrains.skia.BlendMode.MULTIPLY
    BlendMode.HUE -> org.jetbrains.skia.BlendMode.HUE
    BlendMode.SATURATION -> org.jetbrains.skia.BlendMode.SATURATION
    BlendMode.COLOR -> org.jetbrains.skia.BlendMode.COLOR
    BlendMode.LUMINOSITY -> org.jetbrains.skia.BlendMode.LUMINOSITY
}

/** android.graphics.Xfermode。 */
open class Xfermode

/** android.graphics.PorterDuffXfermode。 */
class PorterDuffXfermode(val mode: PorterDuff.Mode) : Xfermode() {
    internal fun toSkia(): org.jetbrains.skia.BlendMode = mode.toSkia()
}

/** android.graphics.ColorFilter。 */
open class ColorFilter {
    internal open fun toSkia(): org.jetbrains.skia.ColorFilter? = null
}

/** android.graphics.PorterDuffColorFilter。 */
class PorterDuffColorFilter(val color: Int, val mode: PorterDuff.Mode) : ColorFilter() {
    override fun toSkia(): org.jetbrains.skia.ColorFilter =
        org.jetbrains.skia.ColorFilter.makeBlend(color, mode.toSkia())
}

/** android.graphics.LightingColorFilter。 */
class LightingColorFilter(val colorMultiply: Int, val colorAdd: Int) : ColorFilter() {
    override fun toSkia(): org.jetbrains.skia.ColorFilter =
        org.jetbrains.skia.ColorFilter.makeLighting(colorMultiply, colorAdd)
}

/** android.graphics.PathEffect。 */
open class PathEffect {
    internal open fun toSkia(): org.jetbrains.skia.PathEffect? = null
}

/** android.graphics.DashPathEffect。 */
class DashPathEffect(val intervals: FloatArray, val phase: Float) : PathEffect() {
    override fun toSkia(): org.jetbrains.skia.PathEffect? =
        org.jetbrains.skia.PathEffect.makeDash(intervals, phase)
}

/** android.graphics.CornerPathEffect。 */
class CornerPathEffect(val radius: Float) : PathEffect() {
    override fun toSkia(): org.jetbrains.skia.PathEffect? =
        org.jetbrains.skia.PathEffect.makeCorner(radius)
}

/** android.graphics.ComposePathEffect。 */
class ComposePathEffect(val outerpe: PathEffect, val innerpe: PathEffect) : PathEffect() {
    override fun toSkia(): org.jetbrains.skia.PathEffect? {
        val inner = innerpe.toSkia() ?: return outerpe.toSkia()
        return outerpe.toSkia()?.makeCompose(inner)
    }
}

/** android.graphics.MaskFilter。 */
open class MaskFilter {
    internal open fun toSkia(): org.jetbrains.skia.MaskFilter? = null
}

/** android.graphics.BlurMaskFilter。 */
class BlurMaskFilter(val radius: Float, val style: Blur) : MaskFilter() {
    enum class Blur {
        NORMAL, SOLID, OUTER, INNER;

        internal fun toSkia(): org.jetbrains.skia.FilterBlurMode = when (this) {
            NORMAL -> org.jetbrains.skia.FilterBlurMode.NORMAL
            SOLID -> org.jetbrains.skia.FilterBlurMode.SOLID
            OUTER -> org.jetbrains.skia.FilterBlurMode.OUTER
            INNER -> org.jetbrains.skia.FilterBlurMode.INNER
        }
    }

    override fun toSkia(): org.jetbrains.skia.MaskFilter {
        // Skia sigma = radius * 0.57735 + 0.5
        val sigma = radius * 0.57735f + 0.5f
        return org.jetbrains.skia.MaskFilter.makeBlur(style.toSkia(), sigma, true)
    }
}

/** android.graphics.Shader。 */
open class Shader {
    internal open fun toSkia(): org.jetbrains.skia.Shader? = null
    private var localMatrix: Matrix? = null

    enum class TileMode {
        CLAMP, REPEAT, MIRROR;

        internal fun toSkia(): org.jetbrains.skia.FilterTileMode = when (this) {
            CLAMP -> org.jetbrains.skia.FilterTileMode.CLAMP
            REPEAT -> org.jetbrains.skia.FilterTileMode.REPEAT
            MIRROR -> org.jetbrains.skia.FilterTileMode.MIRROR
        }
    }

    open fun setLocalMatrix(localM: Matrix) { localMatrix = localM }
    open fun getLocalMatrix(localM: Matrix): Boolean {
        val m = localMatrix ?: return false
        localM.set(m)
        return true
    }
}

/** android.graphics.BitmapShader。 */
class BitmapShader(
    val bitmap: Bitmap,
    val tileX: TileMode,
    val tileY: TileMode,
) : Shader() {
    constructor(bitmap: Bitmap, tileX: TileMode, tileY: TileMode, filter: Boolean) : this(bitmap, tileX, tileY)

    override fun toSkia(): org.jetbrains.skia.Shader? {
        // skiko 0.9.x Image 无公开 makeShader；BitmapShader 暂不支持（渲染时退化为纯色）
        android.util.Log.w("BitmapShader", "toSkia: BitmapShader 暂不支持，使用 bitmap 平均色退化")
        return null
    }
}

/** android.graphics.LinearGradient。 */
class LinearGradient : Shader {
    private val x0: Float; private val y0: Float; private val x1: Float; private val y1: Float
    private val colors: IntArray; private val positions: FloatArray?; private val tile: TileMode

    constructor(x0: Float, y0: Float, x1: Float, y1: Float, colors: IntArray, positions: FloatArray?, tile: TileMode) {
        this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1
        this.colors = colors; this.positions = positions; this.tile = tile
    }

    constructor(x0: Float, y0: Float, x1: Float, y1: Float, color0: Int, color1: Int, tile: TileMode) :
        this(x0, y0, x1, y1, intArrayOf(color0, color1), null, tile)

    override fun toSkia(): org.jetbrains.skia.Shader =
        org.jetbrains.skia.Shader.makeLinearGradient(
            org.jetbrains.skia.Point(x0, y0), org.jetbrains.skia.Point(x1, y1),
            colors, positions, org.jetbrains.skia.GradientStyle(tile.toSkia(), false, org.jetbrains.skia.Matrix33.IDENTITY),
        )
}

/** android.graphics.RadialGradient。 */
class RadialGradient : Shader {
    private val cx: Float; private val cy: Float; private val radius: Float
    private val colors: IntArray; private val stops: FloatArray?; private val tile: TileMode

    constructor(centerX: Float, centerY: Float, radius: Float, colors: IntArray, stops: FloatArray?, tileMode: TileMode) {
        this.cx = centerX; this.cy = centerY; this.radius = radius
        this.colors = colors; this.stops = stops; this.tile = tileMode
    }

    constructor(centerX: Float, centerY: Float, radius: Float, color0: Int, color1: Int, tileMode: TileMode) :
        this(centerX, centerY, radius, intArrayOf(color0, color1), null, tileMode)

    override fun toSkia(): org.jetbrains.skia.Shader =
        org.jetbrains.skia.Shader.makeRadialGradient(
            org.jetbrains.skia.Point(cx, cy), radius, colors, stops,
            org.jetbrains.skia.GradientStyle(tile.toSkia(), false, org.jetbrains.skia.Matrix33.IDENTITY),
        )
}

/** android.graphics.SweepGradient。 */
class SweepGradient : Shader {
    private val cx: Float; private val cy: Float
    private val colors: IntArray; private val positions: FloatArray?

    constructor(cx: Float, cy: Float, colors: IntArray, positions: FloatArray?) {
        this.cx = cx; this.cy = cy; this.colors = colors; this.positions = positions
    }

    constructor(cx: Float, cy: Float, color0: Int, color1: Int) : this(cx, cy, intArrayOf(color0, color1), null)

    override fun toSkia(): org.jetbrains.skia.Shader =
        org.jetbrains.skia.Shader.makeSweepGradient(
            cx, cy, colors, positions, org.jetbrains.skia.GradientStyle(org.jetbrains.skia.FilterTileMode.CLAMP, false, org.jetbrains.skia.Matrix33.IDENTITY),
        )
}

/** android.graphics.Typeface：包壳 skia Typeface。 */
open class Typeface internal constructor(
    internal val skiaTypeface: org.jetbrains.skia.Typeface?,
    val style: Int,
) {
    val isBold: Boolean get() = style and BOLD != 0
    val isItalic: Boolean get() = style and ITALIC != 0

    override fun equals(other: Any?): Boolean = other is Typeface && other.skiaTypeface == skiaTypeface && other.style == style
    override fun hashCode(): Int = (skiaTypeface?.uniqueId ?: 0) * 31 + style

    companion object {
        const val NORMAL = 0
        const val BOLD = 1
        const val ITALIC = 2
        const val BOLD_ITALIC = 3

        private val fontMgr get() = org.jetbrains.skia.FontMgr.default

        private fun skiaStyle(style: Int): org.jetbrains.skia.FontStyle {
            val weight = if (style and BOLD != 0) 700 else 400
            val slant = if (style and ITALIC != 0) org.jetbrains.skia.FontSlant.ITALIC else org.jetbrains.skia.FontSlant.UPRIGHT
            return org.jetbrains.skia.FontStyle(weight, 5, slant)
        }

        private fun resolve(familyName: String?, style: Int): org.jetbrains.skia.Typeface? {
            val fs = skiaStyle(style)
            for (name in listOfNotNull(familyName, "sans-serif", "Default", null)) {
                try {
                    val tf = if (name == null) fontMgr.matchFamilyStyle("", fs)
                    else fontMgr.matchFamilyStyle(name, fs)
                    if (tf != null) return tf
                } catch (t: Throwable) { /* try next */ }
            }
            return null
        }

        @JvmField val DEFAULT: Typeface = create("sans-serif", NORMAL)
        @JvmField val DEFAULT_BOLD: Typeface = create("sans-serif", BOLD)
        @JvmField val SANS_SERIF: Typeface = create("sans-serif", NORMAL)
        @JvmField val SERIF: Typeface = create("serif", NORMAL)
        @JvmField val MONOSPACE: Typeface = create("monospace", NORMAL)

        @JvmStatic
        fun create(familyName: String?, style: Int): Typeface = Typeface(resolve(familyName, style), style)

        @JvmStatic
        fun create(family: Typeface?, style: Int): Typeface {
            if (family != null && style == family.style) return family
            return Typeface(
                if (family?.skiaTypeface != null && style == family.style) family.skiaTypeface else resolve(null, style),
                style,
            )
        }

        @JvmStatic
        fun create(family: Typeface?, weight: Int, italic: Boolean): Typeface {
            val style = (if (weight >= 600) BOLD else 0) or (if (italic) ITALIC else 0)
            return create(family, style)
        }

        @JvmStatic
        fun createFromAsset(mgr: AssetManager, path: String): Typeface {
            return try {
                mgr.open(path).use { stream ->
                    val bytes = stream.readBytes()
                    val data = org.jetbrains.skia.Data.makeFromBytes(bytes)
                    Typeface(fontMgr.makeFromData(data, 0), NORMAL)
                }
            } catch (e: Exception) {
                android.util.Log.w("Typeface", "createFromAsset 失败: $path", e)
                DEFAULT
            }
        }

        @JvmStatic
        fun createFromFile(path: String): Typeface {
            return try {
                Typeface(fontMgr.makeFromFile(path, 0), NORMAL)
            } catch (e: Exception) {
                android.util.Log.w("Typeface", "createFromFile 失败: $path", e)
                DEFAULT
            }
        }

        @JvmStatic
        fun createFromFile(file: java.io.File): Typeface = createFromFile(file.absolutePath)

        @JvmStatic
        fun defaultFromStyle(style: Int): Typeface = when (style) {
            BOLD -> DEFAULT_BOLD
            else -> create("sans-serif", style)
        }

        @JvmStatic
        fun createFromTypefaceWithVariation(family: Typeface?, axes: List<Any>?): Typeface = family ?: DEFAULT
    }
}

/** android.graphics.Paint：记录状态 + skia Font 用于测量。 */
open class Paint {
    constructor()
    constructor(flags: Int) { this.flags = flags }
    constructor(paint: Paint) { set(paint) }

    enum class Style { FILL, STROKE, FILL_AND_STROKE }
    enum class Cap { BUTT, ROUND, SQUARE }
    enum class Join { MITER, ROUND, BEVEL }
    enum class Align { LEFT, CENTER, RIGHT }

    /** 字体度量（Float）。 */
    open class FontMetrics {
        var top: Float = 0f
        var ascent: Float = 0f
        var descent: Float = 0f
        var bottom: Float = 0f
        var leading: Float = 0f
    }

    /** 字体度量（Int）。 */
    open class FontMetricsInt {
        var top: Int = 0
        var ascent: Int = 0
        var descent: Int = 0
        var bottom: Int = 0
        var leading: Int = 0
    }

    var color: Int = Color.BLACK
    var alpha: Int = 255
    var strokeWidth: Float = 0f
    var style: Style = Style.FILL
    var textSize: Float = 12f
    /** typeface 改非空（真实 Android Paint.getTypeface() 非空，默认 Typeface.DEFAULT；app 非空用）。——Nova 注 */
    var typeface: Typeface = Typeface.DEFAULT
    var isAntiAlias: Boolean = false
    var isDither: Boolean = false
    var isFilterBitmap: Boolean = false
    var isFakeBoldText: Boolean = false
    var isUnderlineText: Boolean = false
    var isStrikeThruText: Boolean = false
    var isSubpixelText: Boolean = false
    var isLinearText: Boolean = false
    var isElegantTextHeight: Boolean = false
    var textAlign: Align = Align.LEFT
    var letterSpacing: Float = 0f
    var textScaleX: Float = 1f
    var textSkewX: Float = 0f
    var strokeMiter: Float = 4f
    var strokeCap: Cap = Cap.BUTT
    var strokeJoin: Join = Join.MITER
    var shader: Shader? = null
    var colorFilter: ColorFilter? = null
    var xfermode: Xfermode? = null
    var pathEffect: PathEffect? = null
    var maskFilter: MaskFilter? = null
    var flags: Int = 0
    var hinting: Int = HINTING_ON
    var density: Float = 1.0f

    fun set(paint: Paint) {
        color = paint.color; alpha = paint.alpha; strokeWidth = paint.strokeWidth
        style = paint.style; textSize = paint.textSize; typeface = paint.typeface
        isAntiAlias = paint.isAntiAlias; isDither = paint.isDither; isFilterBitmap = paint.isFilterBitmap
        isFakeBoldText = paint.isFakeBoldText; isUnderlineText = paint.isUnderlineText
        isStrikeThruText = paint.isStrikeThruText; textAlign = paint.textAlign
        letterSpacing = paint.letterSpacing; textScaleX = paint.textScaleX; textSkewX = paint.textSkewX
        strokeMiter = paint.strokeMiter; strokeCap = paint.strokeCap; strokeJoin = paint.strokeJoin
        shader = paint.shader; colorFilter = paint.colorFilter; xfermode = paint.xfermode
        pathEffect = paint.pathEffect; maskFilter = paint.maskFilter; flags = paint.flags
        hinting = paint.hinting; density = paint.density
    }

    fun reset() { set(Paint()) }

    // ---- Java 风格 setter（少量方法调用点） ----
    fun setARGB(a: Int, r: Int, g: Int, b: Int) { color = Color.argb(a, r, g, b) }
    // 注意：typeface/shader/colorFilter/xfermode/pathEffect/maskFilter 均为公开属性，直接赋值
    fun setShadowLayer(radius: Float, dx: Float, dy: Float, shadowColor: Int) {}
    fun clearShadowLayer() {}
    // textLocale 在 TextPaint 中以属性声明，此处不再提供方法避免 JVM 签名冲突
    fun setBlendMode(blendmode: BlendMode?) {}
    fun getBlendMode(): BlendMode? = null

    /** 转 skia Paint（内部用）。 */
    internal fun toSkia(): org.jetbrains.skia.Paint {
        val p = org.jetbrains.skia.Paint()
        // 折叠 alpha 到 color
        val baseAlpha = Color.alpha(color)
        val finalAlpha = baseAlpha * alpha / 255
        p.color = (color and 0x00FFFFFF) or (finalAlpha shl 24)
        p.mode = when (style) {
            Style.FILL -> org.jetbrains.skia.PaintMode.FILL
            Style.STROKE -> org.jetbrains.skia.PaintMode.STROKE
            Style.FILL_AND_STROKE -> org.jetbrains.skia.PaintMode.STROKE_AND_FILL
        }
        p.strokeWidth = strokeWidth
        p.strokeMiter = strokeMiter
        p.strokeCap = org.jetbrains.skia.PaintStrokeCap.valueOf(strokeCap.name)
        p.strokeJoin = org.jetbrains.skia.PaintStrokeJoin.valueOf(strokeJoin.name)
        p.isAntiAlias = isAntiAlias
        p.isDither = isDither
        shader?.toSkia()?.let { p.shader = it }
        colorFilter?.toSkia()?.let { p.colorFilter = it }
        (xfermode as? PorterDuffXfermode)?.let { p.blendMode = it.toSkia() }
        pathEffect?.toSkia()?.let { p.pathEffect = it }
        maskFilter?.toSkia()?.let { p.maskFilter = it }
        return p
    }

    /** 转 skia Font（内部用）。 */
    internal fun toSkiaFont(): org.jetbrains.skia.Font {
        val tf = typeface?.skiaTypeface ?: Typeface.DEFAULT.skiaTypeface
        val f = org.jetbrains.skia.Font(tf, textSize, textScaleX, textSkewX)
        f.isSubpixel = isSubpixelText
        f.isLinearMetrics = isLinearText
        return f
    }

    // ---- 文本测量 ----
    fun measureText(text: String): Float {
        val f = toSkiaFont()
        var w = f.measureTextWidth(text, toSkia())
        if (letterSpacing != 0f) w += letterSpacing * textSize * text.length
        return w
    }

    fun measureText(text: String, start: Int, end: Int): Float = measureText(text.substring(start, end))
    fun measureText(text: CharSequence, start: Int, end: Int): Float = measureText(text.substring(start, end))
    fun measureText(text: CharArray, index: Int, count: Int): Float = measureText(String(text, index, count))

    fun getTextBounds(text: String, start: Int, end: Int, bounds: Rect) {
        val r = toSkiaFont().measureText(text.substring(start, end), toSkia())
        bounds.set(r.left.toInt(), r.top.toInt(), r.right.toInt(), r.bottom.toInt())
    }

    fun getTextBounds(text: CharArray, index: Int, count: Int, bounds: Rect) =
        getTextBounds(String(text, index, count), 0, count, bounds)

    fun descent(): Float = toSkiaFont().metrics.descent
    fun ascent(): Float = toSkiaFont().metrics.ascent

    fun getFontMetrics(metrics: FontMetrics?): Float {
        val m = toSkiaFont().metrics
        metrics?.apply {
            top = m.top; ascent = m.ascent; descent = m.descent; bottom = m.bottom; leading = m.leading
        }
        return m.bottom - m.top
    }

    fun getFontMetricsInt(fmi: FontMetricsInt?): Int {
        val m = toSkiaFont().metrics
        fmi?.apply {
            top = m.top.toInt(); ascent = m.ascent.toInt()
            descent = m.descent.toInt(); bottom = m.bottom.toInt(); leading = m.leading.toInt()
        }
        return (m.bottom - m.top).toInt()
    }

    val fontMetrics: FontMetrics
        get() = FontMetrics().also { getFontMetrics(it) }

    val fontMetricsInt: FontMetricsInt
        get() = FontMetricsInt().also { getFontMetricsInt(it) }

    fun getFontSpacing(): Float {
        val m = toSkiaFont().metrics
        return m.bottom - m.top + m.leading
    }

    fun breakText(text: String, measureForwards: Boolean, maxWidth: Float, measuredWidth: FloatArray?): Int =
        breakText(text, 0, text.length, measureForwards, maxWidth, measuredWidth)

    fun breakText(text: CharSequence, measureForwards: Boolean, maxWidth: Float, measuredWidth: FloatArray?): Int =
        breakText(text.toString(), 0, text.length, measureForwards, maxWidth, measuredWidth)

    fun breakText(
        text: String, start: Int, end: Int, measureForwards: Boolean, maxWidth: Float, measuredWidth: FloatArray?,
    ): Int {
        if (start >= end) return 0
        val sub = text.substring(start, end)
        val full = measureText(sub)
        if (full <= maxWidth) {
            measuredWidth?.let { it[0] = full }
            return sub.length
        }
        // 二分查找最大可放下的前缀
        var lo = 0; var hi = sub.length
        while (lo < hi) {
            val mid = (lo + hi + 1) / 2
            if (measureText(sub.substring(0, mid)) <= maxWidth) lo = mid else hi = mid - 1
        }
        measuredWidth?.let { it[0] = measureText(sub.substring(0, lo)) }
        return lo
    }

    fun getTextWidths(text: String, widths: FloatArray): Int = getTextWidths(text, 0, text.length, widths)
    fun getTextWidths(text: String, start: Int, end: Int, widths: FloatArray): Int {
        var count = 0
        for (i in start until end) {
            if (count >= widths.size) break
            widths[count++] = measureText(text[i].toString())
        }
        return count
    }

    fun hasGlyph(string: String): Boolean = try {
        (typeface?.skiaTypeface ?: Typeface.DEFAULT.skiaTypeface)?.getStringGlyphs(string)?.isNotEmpty() ?: true
    } catch (e: Throwable) { true }

    fun getRunPathAdvance(chars: CharArray, index: Int, count: Int, contextIndex: Int, contextCount: Int, isRtl: Boolean, runInfo: Int): Float = 0f

    // underlineThickness/underlinePosition 在 TextPaint 以属性提供，Paint 不再提供同名方法

    companion object {
        const val ANTI_ALIAS_FLAG = 1
        const val FILTER_BITMAP_FLAG = 2
        const val DITHER_FLAG = 4
        const val UNDERLINE_TEXT_FLAG = 8
        const val STRIKE_THRU_TEXT_FLAG = 16
        const val FAKE_BOLD_TEXT_FLAG = 32
        const val LINEAR_TEXT_FLAG = 64
        const val SUBPIXEL_TEXT_FLAG = 128
        const val EMBEDDED_BITMAP_TEXT_FLAG = 1024
        const val AUTO_HINTED_TEXT_FLAG = 2048
        const val VERTICAL_TEXT_FLAG = 4096
        const val TEXT_RUN_FLAG_LEFT_EDGE = 8192
        const val TEXT_RUN_FLAG_RIGHT_EDGE = 16384
        const val TEXT_RUN_FLAG_RTL = 65536

        const val HINTING_OFF = 0
        const val HINTING_ON = 1

        const val CURSOR_BEFORE = 0
        const val CURSOR_AFTER = 1
        const val CURSOR_AT = 2
        const val CURSOR_AT_OR_BEFORE = 3
        const val CURSOR_AT_OR_AFTER = 4
    }
}
