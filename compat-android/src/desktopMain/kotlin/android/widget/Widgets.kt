package android.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

/**
 * android.widget 基础控件（P3-B2 新增，编译级 stub）。
 * FrameLayout/LinearLayout/ScrollView 为 ViewGroup 子类；TextView/ImageView 为 View 子类；
 * OverScroller 为简化真实物理（时间驱动减速曲线）。
 */

/** android.widget.FrameLayout。 */
open class FrameLayout : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open var measureAllChildren: Boolean = false

    open fun setForegroundGravity(foregroundGravity: Int) {}

    /** android.widget.FrameLayout.LayoutParams。 */
    open class LayoutParams : MarginLayoutParams {
        var gravity: Int = -1

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, gravity: Int) : super(width, height) {
            this.gravity = gravity
        }
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
        constructor(source: MarginLayoutParams?) : super(source)

        companion object {
            // 同 LinearLayout.LayoutParams：对齐 Java 静态继承语义
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2
            @Deprecated("deprecated")
            const val FILL_PARENT = -1
        }
    }
}

/** android.widget.LinearLayout。 */
open class LinearLayout : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open var orientation: Int = HORIZONTAL
    open var gravity: Int = Gravity.START or Gravity.TOP
    open var weightSum: Float = -1.0f
    open var baselineAligned: Boolean = true
    open var measureWithLargestChild: Boolean = false
    open var showDividers: Int = 0

    open fun setDividerDrawable(divider: Drawable?) {}
    open fun setDividerPadding(padding: Int) {}

    /** android.widget.LinearLayout.LayoutParams。 */
    open class LayoutParams : MarginLayoutParams {
        var weight: Float = 0f
        var gravity: Int = -1

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, weight: Float) : super(width, height) {
            this.weight = weight
        }
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(source: ViewGroup.LayoutParams?) : super(source)

        companion object {
            // Kotlin 伴生常量不会随继承被子类名访问，这里重复声明以对齐 Java 静态继承语义
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2
            @Deprecated("deprecated")
            const val FILL_PARENT = -1
        }
    }

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val SHOW_DIVIDER_NONE = 0
        const val SHOW_DIVIDER_BEGINNING = 1
        const val SHOW_DIVIDER_MIDDLE = 2
        const val SHOW_DIVIDER_END = 4
    }
}

/** android.widget.ScrollView。 */
open class ScrollView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open var isFillViewport: Boolean = false
    open var isSmoothScrollingEnabled: Boolean = true
    open var maxScrollAmount: Int = 0

    open fun scrollTo(x: Int, y: Int) {}
    open fun scrollBy(x: Int, y: Int) {}
    open fun smoothScrollTo(x: Int, y: Int) {}
    open fun smoothScrollBy(dx: Int, dy: Int) {}
    open fun fullScroll(direction: Int): Boolean = false
    open fun pageScroll(direction: Int): Boolean = false
    open fun arrowScroll(direction: Int): Boolean = false
    open fun fling(velocityY: Int) {}
    open fun canScrollVertically(direction: Int): Boolean = false
}

/** android.widget.ImageView。 */
open class ImageView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    enum class ScaleType {
        MATRIX, FIT_XY, FIT_START, FIT_CENTER, FIT_END,
        CENTER, CENTER_CROP, CENTER_INSIDE,
    }

    open var scaleType: ScaleType = ScaleType.CENTER
    open var adjustViewBounds: Boolean = false
    open var cropToPadding: Boolean = false
    open var baseline: Int = -1
    open var baselineAlignBottom: Boolean = false

    private var drawable: Drawable? = null
    private var bitmap: Bitmap? = null
    private var imageResource: Int = 0
    private var imageUri: Uri? = null

    open fun setImageBitmap(bm: Bitmap?) { bitmap = bm }
    open fun setImageResource(resId: Int) { imageResource = resId }
    open fun setImageDrawable(drawable: Drawable?) { this.drawable = drawable }
    open fun setImageURI(uri: Uri?) { imageUri = uri }
    open fun setImageIcon(icon: Any?) {}
    open fun setImageLevel(level: Int) {}
    open fun setColorFilter(color: Int) {}
    open fun setColorFilter(color: Int, mode: android.graphics.PorterDuff.Mode?) {}
    open fun clearColorFilter() {}
    open fun getDrawable(): Drawable? = drawable
}

/** android.widget.TextView。 */
open class TextView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    enum class BufferType { NORMAL, SPANNABLE, EDITABLE }

    open var text: CharSequence? = null
    open var textSize: Float = 14f
    open var hint: CharSequence? = null
    open var currentTextColor: Int = 0xFF000000.toInt()
    open var currentHintTextColor: Int = 0xFF808080.toInt()
    open var lineHeight: Int = 0
    open var gravity: Int = Gravity.START or Gravity.TOP
    open var maxLines: Int = Int.MAX_VALUE
    open var minLines: Int = 1
    open var typeface: Typeface? = null
    open var ellipsize: android.text.TextUtils.TruncateAt? = null
    open var isSingleLine: Boolean = false
    open var autoLinkMask: Int = 0
    open var linksClickable: Boolean = true

    open val paint: TextPaint get() = TextPaint()

    open fun setText(text: CharSequence?, type: BufferType) { this.text = text }
    open fun setText(resId: Int) {}
    open fun setTextSize(unit: Int, size: Float) { textSize = size }
    open fun setTextColor(color: Int) { currentTextColor = color }
    open fun setHintTextColor(color: Int) { currentHintTextColor = color }
    open fun setLineSpacing(add: Float, mult: Float) {}
    open fun setMaxLines(maxLines: Int, @Suppress("UNUSED_PARAMETER") ignored: Unit = Unit) { this.maxLines = maxLines }
    open fun setMinLines(minLines: Int, @Suppress("UNUSED_PARAMETER") ignored: Unit = Unit) { this.minLines = minLines }
    open fun setGravity(gravity: Int, @Suppress("UNUSED_PARAMETER") ignored: Unit = Unit) { this.gravity = gravity }
    open fun setTypeface(tf: Typeface?, style: Int) { typeface = tf }
    open fun setSingleLine(singleLine: Boolean, @Suppress("UNUSED_PARAMETER") ignored: Unit = Unit) { isSingleLine = singleLine }
    open fun setLetterSpacing(letterSpacing: Float) {}
    open fun setEms(ems: Int) {}
    open fun setShadowLayer(radius: Float, dx: Float, dy: Float, color: Int) {}
    open fun length(): Int = text?.length ?: 0
    open fun getEditableText(): android.text.Editable? = null
}

/** android.widget.OverScroller：简化真实物理（时间驱动减速）。 */
open class OverScroller {
    constructor(context: Context?)
    constructor(context: Context?, interpolator: android.view.animation.Interpolator?)

    private var finished = true
    private var startX = 0
    private var startY = 0
    private var endX = 0
    private var endY = 0
    private var startTime = 0L
    private var duration = 0
    private var currentX = 0
    private var currentY = 0
    private var velocity = 0f

    open val isFinished: Boolean get() = finished
    open val currX: Int get() = currentX
    open val currY: Int get() = currentY
    open val finalX: Int get() = endX
    open val finalY: Int get() = endY
    open val currVelocity: Float get() = velocity

    open fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) {
        this.startX = startX
        this.startY = startY
        this.currentX = startX
        this.currentY = startY
        this.duration = (Math.hypot(velocityX.toDouble(), velocityY.toDouble()) / 2.0).toInt().coerceIn(100, 2500)
        this.endX = (startX + velocityX * duration / 2000).coerceIn(minX, maxX)
        this.endY = (startY + velocityY * duration / 2000).coerceIn(minY, maxY)
        this.velocity = Math.hypot(velocityX.toDouble(), velocityY.toDouble()).toFloat()
        this.startTime = SystemClock.uptimeMillis()
        this.finished = false
    }

    open fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        startScroll(startX, startY, dx, dy, 250)
    }

    open fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        this.startX = startX
        this.startY = startY
        this.currentX = startX
        this.currentY = startY
        this.endX = startX + dx
        this.endY = startY + dy
        this.duration = duration
        this.startTime = SystemClock.uptimeMillis()
        this.finished = false
    }

    open fun computeScrollOffset(): Boolean {
        if (finished) return false
        val elapsed = (SystemClock.uptimeMillis() - startTime).coerceAtLeast(0)
        if (elapsed >= duration) {
            currentX = endX
            currentY = endY
            finished = true
            return false
        }
        // easeOutCubic
        val t = 1.0 - (elapsed.toDouble() / duration).coerceIn(0.0, 1.0)
        val ease = 1.0 - t * t * t
        currentX = (startX + (endX - startX) * ease).toInt()
        currentY = (startY + (endY - startY) * ease).toInt()
        return true
    }

    open fun abortAnimation() {
        finished = true
    }

    open fun forceFinished(finished: Boolean) {
        this.finished = finished
    }

    open fun springBack(startX: Int, startY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean = false

    open fun notifyVerticalEdgeReached(startY: Int, finalY: Int, overY: Int) {}
    open fun notifyHorizontalEdgeReached(startX: Int, finalX: Int, overX: Int) {}
    open fun timePassed(): Int = (SystemClock.uptimeMillis() - startTime).toInt()
}

/** android.widget.ProgressBar。 */
open class ProgressBar : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open var progress: Int = 0
    open var secondaryProgress: Int = 0
    open var max: Int = 100
    open var isIndeterminate: Boolean = false

    open fun incrementProgressBy(diff: Int) { progress += diff }
    open fun incrementSecondaryProgressBy(diff: Int) { secondaryProgress += diff }
}

/** android.widget.EditText。 */
open class EditText : TextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open var selectionStart: Int = 0
    open var selectionEnd: Int = 0

    open fun setSelection(index: Int) {}
    open fun setSelection(start: Int, stop: Int) {}
    open fun selectAll() {}
    open fun extendSelection(index: Int) {}
    open fun setInputType(type: Int) {}
}
