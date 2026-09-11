package android.graphics.drawable

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.Shader
import android.util.Log

/** android.graphics.drawable.Drawable 基类。 */
abstract class Drawable {
    val bounds: Rect = Rect()
    open var callback: Callback? = null
    open var alpha: Int = 255
    open var colorFilter: ColorFilter? = null
    var isVisible: Boolean = true
        private set
    var isStateful: Boolean = false
    open var state: IntArray = IntArray(0)
        set(value) {
            if (!field.contentEquals(value)) {
                field = value
                onStateChange(value)
            }
        }
    open var level: Int = 0
        set(value) {
            if (field != value) {
                field = value
                onLevelChange(value)
            }
        }
    var changingConfigurations: Int = 0
    var layoutDirection: Int = 0
    open var isAutoMirrored: Boolean = false
    open val opacity: Int get() = PixelFormat.UNKNOWN
    open val current: Drawable get() = this
    open val constantState: ConstantState? get() = null
    open val intrinsicWidth: Int get() = -1
    open val intrinsicHeight: Int get() = -1
    open val minimumWidth: Int get() = 0
    open val minimumHeight: Int get() = 0
    open val transparentRegion: Region? get() = null

    abstract fun draw(canvas: Canvas)

    // ---- 边界 ----
    open fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        bounds.set(left, top, right, bottom)
        onBoundsChange(bounds)
    }

    open fun setBounds(bounds: Rect) = setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
    open fun copyBounds(): Rect = Rect(bounds)
    open fun copyBounds(outBounds: Rect) { outBounds.set(bounds) }
    protected open fun onBoundsChange(bounds: Rect) {}

    // ---- 状态（setter 内嵌回调；AOSP setState/setLevel 的 Boolean 返回值在属性模式下丢弃） ----
    open fun onStateChange(stateSet: IntArray): Boolean = false
    open fun onLevelChange(level: Int): Boolean = false
    open fun jumpToCurrentState() {}

    // ---- 外观 ----
    open fun setColorFilter(color: Int, mode: PorterDuff.Mode) {
        colorFilter = PorterDuffColorFilter(color, mode)
    }
    open fun clearColorFilter() { colorFilter = null }
    open fun setTint(tintColor: Int) {}
    open fun setTintList(tint: ColorStateList?) {}
    open fun setTintMode(tintMode: PorterDuff.Mode?) {}
    open fun setTintBlendMode(blendMode: android.graphics.BlendMode?) {}
    open fun setDither(dither: Boolean) {}
    open fun setFilterBitmap(filter: Boolean) {}
    open fun isFilterBitmap(): Boolean = false

    open fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        val changed = isVisible != visible
        isVisible = visible
        if (changed) invalidateSelf()
        return changed
    }

    open fun setLayoutDirection(layoutDirection: Int): Boolean {
        this.layoutDirection = layoutDirection
        return false
    }

    // ---- 回调 ----
    interface Callback {
        fun invalidateDrawable(who: Drawable)
        fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long)
        fun unscheduleDrawable(who: Drawable, what: Runnable)
    }

    open fun invalidateSelf() { callback?.invalidateDrawable(this) }
    open fun scheduleSelf(what: Runnable, `when`: Long) { callback?.scheduleDrawable(this, what, `when`) }
    open fun unscheduleSelf(what: Runnable) { callback?.unscheduleDrawable(this, what) }

    open fun mutate(): Drawable = this
    open fun clearMutated() {}
    open fun inflate(r: Resources, parser: Any?, attrs: android.util.AttributeSet?, theme: Resources.Theme?) {}

    open fun getPadding(padding: Rect): Boolean {
        padding.setEmpty()
        return false
    }

    open fun getOutline(outline: Any?) {}
    open fun getOpticalInsets(): Any? = null
    open fun canApplyTheme(): Boolean = false
    open fun applyTheme(t: Resources.Theme) {}
    open fun setHotspot(x: Float, y: Float) {}
    open fun setHotspotBounds(left: Int, top: Int, right: Int, bottom: Int) {}
    open fun getHotspotBounds(outRect: Rect) { outRect.set(bounds) }
    open fun getDirtyBounds(): Rect = Rect(bounds)
    open fun setProjected(projected: Boolean) {}
    open fun isProjected(): Boolean = false

    abstract class ConstantState {
        abstract fun newDrawable(): Drawable
        open fun newDrawable(res: Resources?): Drawable = newDrawable()
        open fun newDrawable(res: Resources?, theme: Resources.Theme?): Drawable = newDrawable(res)
        open fun getChangingConfigurations(): Int = 0
        open fun canApplyTheme(): Boolean = false
    }

    companion object {
        @JvmStatic
        fun createFromStream(inputStream: java.io.InputStream?, srcName: String?): Drawable? {
            val bmp = android.graphics.BitmapFactory.decodeStream(inputStream) ?: return null
            return BitmapDrawable(bmp)
        }

        @JvmStatic
        fun createFromPath(pathName: String?): Drawable? {
            val bmp = pathName?.let { android.graphics.BitmapFactory.decodeFile(it) } ?: return null
            return BitmapDrawable(bmp)
        }

        @JvmStatic
        fun resolveOpacity(d1: Int, d2: Int): Int = when {
            d1 == d2 -> d1
            d1 == PixelFormat.UNKNOWN || d2 == PixelFormat.UNKNOWN -> PixelFormat.UNKNOWN
            d1 == PixelFormat.TRANSLUCENT || d2 == PixelFormat.TRANSLUCENT -> PixelFormat.TRANSLUCENT
            d1 == PixelFormat.TRANSPARENT || d2 == PixelFormat.TRANSPARENT -> PixelFormat.TRANSPARENT
            else -> PixelFormat.OPAQUE
        }

        @JvmStatic fun getOpacityForColor(color: Int): Int = PixelFormat.UNKNOWN
    }
}

/** android.graphics.drawable.ColorDrawable。 */
open class ColorDrawable() : Drawable() {
    var color: Int = 0

    constructor(color: Int) : this() { this.color = color }

    override fun draw(canvas: Canvas) {
        if (Color.alpha(color) == 0) return
        val c = if (alpha == 255) color
        else (color and 0x00FFFFFF) or ((Color.alpha(color) * alpha / 255) shl 24)
        canvas.drawColor(c)
    }

    override val opacity: Int get() = when (Color.alpha(color)) {
        255 -> PixelFormat.OPAQUE
        0 -> PixelFormat.TRANSPARENT
        else -> PixelFormat.TRANSLUCENT
    }
}

/** android.graphics.drawable.GradientDrawable：记录属性并尽量真实绘制。 */
open class GradientDrawable : Drawable() {

    enum class Orientation {
        TOP_BOTTOM, TR_BL, RIGHT_LEFT, BR_TL, BOTTOM_TOP, BL_TR, LEFT_RIGHT, TL_BR,
    }

    private var colors: IntArray = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT)
    private var strokeColor: Int = Color.TRANSPARENT
    private var strokeWidth: Float = 0f
    private var strokeDashWidth: Float = 0f
    private var strokeDashGap: Float = 0f
    private var cornerRadius: Float = 0f
    private var cornerRadii: FloatArray? = null
    private var shape = RECTANGLE
    private var gradientType = LINEAR_GRADIENT
    private var orientation = Orientation.TOP_BOTTOM
    private var gradientCenterX = 0.5f
    private var gradientCenterY = 0.5f
    private var gradientRadius = 0f
    private var useLevel = false
    private var innerRadius = 0f
    private var thickness = 0
    private var width = -1
    private var height = -1

    fun setColor(argb: Int) { colors = intArrayOf(argb, argb) }
    fun setColor(colorStateList: ColorStateList?) { colorStateList?.let { setColor(it.getDefaultColor()) } }
    fun setColors(colors: IntArray?) { if (colors != null) this.colors = colors }
    fun setStroke(width: Int, color: Int) = setStroke(width, color, 0f, 0f)
    fun setStroke(width: Int, color: Int, dashWidth: Float, dashGap: Float) {
        this.strokeWidth = width.toFloat(); this.strokeColor = color
        this.strokeDashWidth = dashWidth; this.strokeDashGap = dashGap
    }
    fun setCornerRadius(radius: Float) { cornerRadius = radius }
    fun setCornerRadii(radii: FloatArray?) { cornerRadii = radii }
    fun setShape(shape: Int) { this.shape = shape }
    fun getShape(): Int = shape
    fun setGradientType(gradient: Int) { gradientType = gradient }
    fun setOrientation(orientation: Orientation) { this.orientation = orientation }
    fun setGradientCenter(x: Float, y: Float) { gradientCenterX = x; gradientCenterY = y }
    fun setGradientRadius(radius: Float) { gradientRadius = radius }
    fun setUseLevel(useLevel: Boolean) { this.useLevel = useLevel }
    fun setSize(width: Int, height: Int) { this.width = width; this.height = height }
    fun setInnerRadius(radius: Int) { innerRadius = radius.toFloat() }
    fun setThickness(thickness: Int) { this.thickness = thickness }
    fun getColor(): ColorStateList? = ColorStateList.valueOf(colors.firstOrNull() ?: Color.TRANSPARENT)
    fun getColors(): IntArray? = colors
    fun getCornerRadius(): Float = cornerRadius
    fun getStrokeColor(): ColorStateList? = ColorStateList.valueOf(strokeColor)
    fun getStrokeWidth(): Int = strokeWidth.toInt()
    fun getGradientRadius(): Float = gradientRadius
    fun getOrientation(): Orientation = orientation
    fun getGradientType(): Int = gradientType

    override fun draw(canvas: Canvas) {
        val b = RectF(bounds)
        if (b.isEmpty()) return

        val fillPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = colors.firstOrNull() ?: Color.TRANSPARENT
            alpha = this@GradientDrawable.alpha
            if (colors.size >= 2) {
                shader = when (gradientType) {
                    RADIAL_GRADIENT -> android.graphics.RadialGradient(
                        b.centerX(), b.centerY(),
                        if (gradientRadius > 0) gradientRadius else b.width() / 2,
                        colors, null, Shader.TileMode.CLAMP,
                    )
                    SWEEP_GRADIENT -> android.graphics.SweepGradient(b.centerX(), b.centerY(), colors, null)
                    else -> {
                        val (x0, y0, x1, y1) = when (orientation) {
                            Orientation.LEFT_RIGHT -> listOf(b.left, b.top, b.right, b.top)
                            Orientation.RIGHT_LEFT -> listOf(b.right, b.top, b.left, b.top)
                            Orientation.BOTTOM_TOP -> listOf(b.left, b.bottom, b.left, b.top)
                            Orientation.BL_TR -> listOf(b.left, b.bottom, b.right, b.top)
                            Orientation.BR_TL -> listOf(b.right, b.bottom, b.left, b.top)
                            Orientation.TR_BL -> listOf(b.right, b.top, b.left, b.bottom)
                            Orientation.TL_BR -> listOf(b.left, b.top, b.right, b.bottom)
                            else -> listOf(b.left, b.top, b.left, b.bottom)
                        }
                        LinearGradient(x0, y0, x1, y1, colors, null, Shader.TileMode.CLAMP)
                    }
                }
            }
        }

        when (shape) {
            OVAL -> canvas.drawOval(b, fillPaint)
            else -> if (cornerRadius > 0) canvas.drawRoundRect(b, cornerRadius, cornerRadius, fillPaint)
            else canvas.drawRect(b, fillPaint)
        }

        if (strokeWidth > 0 && Color.alpha(strokeColor) != 0) {
            val strokePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                this.strokeWidth = this@GradientDrawable.strokeWidth
                color = strokeColor
                alpha = this@GradientDrawable.alpha
                if (strokeDashWidth > 0) {
                    pathEffect = android.graphics.DashPathEffect(floatArrayOf(strokeDashWidth, strokeDashGap), 0f)
                }
            }
            when (shape) {
                OVAL -> canvas.drawOval(b, strokePaint)
                else -> if (cornerRadius > 0) canvas.drawRoundRect(b, cornerRadius, cornerRadius, strokePaint)
                else canvas.drawRect(b, strokePaint)
            }
        }
    }

    override val intrinsicWidth: Int get() = width
    override val intrinsicHeight: Int get() = height

    override val opacity: Int get() = PixelFormat.TRANSLUCENT

    companion object {
        const val RECTANGLE = 0
        const val OVAL = 1
        const val LINE = 2
        const val RING = 3
        const val LINEAR_GRADIENT = 0
        const val RADIAL_GRADIENT = 1
        const val SWEEP_GRADIENT = 2
        const val RADIUS_GRADIENT = 3
    }
}

/** android.graphics.drawable.BitmapDrawable。 */
open class BitmapDrawable() : Drawable() {
    var bitmap: Bitmap? = null
    var gravity: Int = android.view.Gravity.FILL
    val paint: Paint = Paint().apply { isFilterBitmap = true }
    var tileModeX: Shader.TileMode? = null
    var tileModeY: Shader.TileMode? = null
    var targetDensity: Int = android.util.DisplayMetrics.DENSITY_DEFAULT

    constructor(res: Resources?) : this()
    constructor(bitmap: Bitmap?) : this() { this.bitmap = bitmap }
    constructor(res: Resources?, bitmap: Bitmap?) : this() { this.bitmap = bitmap }
    constructor(filepath: String?) : this() { bitmap = filepath?.let { android.graphics.BitmapFactory.decodeFile(it) } }



    fun setTileModeXY(tileMode: Shader.TileMode?) { tileModeX = tileMode; tileModeY = tileMode }

    fun setAntiAlias(aa: Boolean) { paint.isAntiAlias = aa }

    override fun draw(canvas: Canvas) {
        val b = bitmap ?: return
        canvas.drawBitmap(b, null, bounds, paint)
    }

    override val intrinsicWidth: Int get() = bitmap?.width ?: -1
    override val intrinsicHeight: Int get() = bitmap?.height ?: -1
    override val minimumWidth: Int get() = bitmap?.width ?: 0
    override val minimumHeight: Int get() = bitmap?.height ?: 0
    override val opacity: Int get() = PixelFormat.TRANSLUCENT
}

/** android.graphics.drawable.DrawableContainer 轻 stub。 */
open class DrawableContainer : Drawable() {
    private val drawables = ArrayList<Drawable>()
    private var currentIndex = -1

    open fun addChild(drawable: Drawable): Int {
        drawables.add(drawable)
        if (currentIndex < 0) currentIndex = 0
        return drawables.size - 1
    }

    open fun getDrawable(index: Int): Drawable? = drawables.getOrNull(index)
    open fun getNumberOfChildren(): Int = drawables.size
    open fun setCurrent(index: Int) { currentIndex = index }

    override fun draw(canvas: Canvas) {
        drawables.getOrNull(currentIndex)?.draw(canvas)
    }

    override val current: Drawable get() = drawables.getOrNull(currentIndex) ?: this
}

/** android.graphics.drawable.StateListDrawable 轻 stub。 */
open class StateListDrawable : DrawableContainer() {
    open fun addState(stateSet: IntArray, drawable: Drawable) {
        addChild(drawable)
    }
}

/** android.graphics.drawable.LayerDrawable 轻 stub。 */
open class LayerDrawable(private val layers: Array<Drawable>) : DrawableContainer() {
    init { layers.forEach { addChild(it) } }

    open fun setLayerGravity(index: Int, gravity: Int) {}
    open fun setLayerInset(index: Int, l: Int, t: Int, r: Int, b: Int) {}
    open fun setLayerInsetStart(index: Int, s: Int) {}
    open fun setLayerInsetEnd(index: Int, e: Int) {}
    open fun setLayerSize(index: Int, w: Int, h: Int) {}

    override fun draw(canvas: Canvas) {
        for (d in 0 until getNumberOfChildren()) getDrawable(d)?.draw(canvas)
    }
}

/** android.graphics.drawable.Animatable。 */
interface Animatable {
    fun start()
    fun stop()
    fun isRunning(): Boolean
}

/** android.graphics.drawable.Animatable2。 */
interface Animatable2 : Animatable {
    fun registerAnimationCallback(callback: AnimationCallback)
    fun unregisterAnimationCallback(callback: AnimationCallback): Boolean
    fun clearAnimationCallbacks()

    abstract class AnimationCallback {
        open fun onAnimationStart(drawable: Drawable?) {}
        open fun onAnimationEnd(drawable: Drawable?) {}
    }
}

/** android.graphics.drawable.AnimatedImageDrawable stub。 */
open class AnimatedImageDrawable : Drawable(), Animatable2 {
    private val callbacks = ArrayList<Animatable2.AnimationCallback>()
    private var running = false

    override fun draw(canvas: Canvas) {}
    override fun start() { running = false }
    override fun stop() { running = false }
    override fun isRunning(): Boolean = running
    override fun registerAnimationCallback(callback: Animatable2.AnimationCallback) {
        if (!callbacks.contains(callback)) callbacks.add(callback)
    }
    override fun unregisterAnimationCallback(callback: Animatable2.AnimationCallback): Boolean = callbacks.remove(callback)
    override fun clearAnimationCallbacks() { callbacks.clear() }

    open fun getRepeatCount(): Int = 0
    open fun setRepeatCount(repeatCount: Int) {}

    companion object {
        const val REPEAT_INFINITE = -1
        const val REPEAT_UNDEFINED = 0
    }
}

/** android.graphics.drawable.Icon：stub。 */
class Icon private constructor(
    private val type: Int,
    private val bitmap: Bitmap? = null,
    private val string: String? = null,
    private val bytes: ByteArray? = null,
    private val resId: Int = 0,
) {
    fun getType(): Int = type
    fun getBitmap(): Bitmap? = bitmap
    fun getResId(): Int = resId
    fun getResPackage(): String = "com.ai.assistance.operit"
    fun getUri(): android.net.Uri? = string?.let { android.net.Uri.parse(it) }
    fun getDataBytes(): ByteArray? = bytes
    fun getDataOffset(): Int = 0
    fun getDataLength(): Int = bytes?.size ?: 0
    fun hasOnClickListeners(): Boolean = false

    fun setTint(tintColor: Int): Icon = this
    fun setTintList(tintList: ColorStateList?): Icon = this
    fun setTintMode(mode: PorterDuff.Mode): Icon = this
    fun setTintBlendMode(blendMode: android.graphics.BlendMode): Icon = this

    fun loadDrawable(context: android.content.Context): Drawable? = bitmap?.let { BitmapDrawable(it) }
    fun loadDrawableAsync(context: android.content.Context, listener: OnDrawableLoadedListener) {
        loadDrawable(context)?.let { listener.onDrawableLoaded(it) }
    }
    fun loadDrawableAsync(context: android.content.Context, listener: OnDrawableLoadedListener, handler: android.os.Handler) =
        loadDrawableAsync(context, listener)

    fun convertToIcon(): Any = this

    fun interface OnDrawableLoadedListener {
        fun onDrawableLoaded(d: Drawable)
    }

    override fun toString(): String = "Icon(type=$type)"

    companion object {
        const val TYPE_BITMAP = 1
        const val TYPE_DATA = 2
        const val TYPE_RESOURCE = 3
        const val TYPE_URI = 4
        const val TYPE_ADAPTIVE_BITMAP = 5
        const val TYPE_URI_ADAPTIVE_BITMAP = 6

        @JvmStatic fun createWithBitmap(bitmap: Bitmap): Icon = Icon(TYPE_BITMAP, bitmap = bitmap)
        @JvmStatic fun createWithAdaptiveBitmap(bitmap: Bitmap): Icon = Icon(TYPE_ADAPTIVE_BITMAP, bitmap = bitmap)
        @JvmStatic fun createWithData(data: ByteArray, offset: Int, length: Int): Icon =
            Icon(TYPE_DATA, bytes = data.copyOfRange(offset, offset + length))
        @JvmStatic fun createWithResource(context: android.content.Context, resId: Int): Icon = Icon(TYPE_RESOURCE, resId = resId)
        @JvmStatic fun createWithResource(pkg: String, resId: Int): Icon = Icon(TYPE_RESOURCE, resId = resId)
        @JvmStatic fun createWithFilePath(path: String): Icon = Icon(TYPE_URI, string = "file://$path")
        @JvmStatic fun createWithContentUri(uri: String): Icon = Icon(TYPE_URI, string = uri)
        @JvmStatic fun createWithContentUri(uri: android.net.Uri): Icon = Icon(TYPE_URI, string = uri.toString())
        @JvmStatic fun createWithAdaptiveBitmapContentUri(uri: String): Icon = Icon(TYPE_URI_ADAPTIVE_BITMAP, string = uri)
        @JvmStatic fun createFromParcel(inParcel: android.os.Parcel): Icon = Icon(TYPE_BITMAP)
    }
}
