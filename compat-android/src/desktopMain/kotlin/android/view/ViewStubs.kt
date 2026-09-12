package android.view

import android.content.Context
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.AttributeSet
import android.util.DisplayMetrics

/**
 * android.view 最小占位（B1a 阶段）。
 * View 仅提供编译所需的骨架（无参 getter 以 Kotlin 属性声明，与迁移代码的属性调用风格一致），
 * 完整 View 体系在 B1b 实装。
 */
open class View(open val context: Context) {
    constructor(context: Context, attrs: AttributeSet?) : this(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : this(context)

    fun interface OnClickListener { fun onClick(v: View) }
    fun interface OnLongClickListener { fun onLongClick(v: View): Boolean }
    fun interface OnTouchListener { fun onTouch(v: View, event: MotionEvent): Boolean }
    interface OnAttachStateChangeListener {
        fun onViewAttachedToWindow(v: View)
        fun onViewDetachedFromWindow(v: View)
    }
    interface OnLayoutChangeListener {
        fun onLayoutChange(
            v: View, left: Int, top: Int, right: Int, bottom: Int,
            oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int,
        )
    }

    open var visibility: Int = VISIBLE
    open var alpha: Float = 1f
    open var isEnabled: Boolean = true
    open var isClickable: Boolean = false
    open var isLongClickable: Boolean = false
    open var isFocusable: Boolean = false
    open var isFocusableInTouchMode: Boolean = false
    open var isScreenReaderFocusable: Boolean = false
    open var isHapticFeedbackEnabled: Boolean = true
    open var accessibilityDelegate: AccessibilityDelegate? = null
    open var isSelected: Boolean = false
    open var isPressed: Boolean = false
    open var isActivated: Boolean = false
    open var contentDescription: CharSequence? = null
    open var tag: Any? = null
    open var translationX: Float = 0f
    open var translationY: Float = 0f
    open var scaleX: Float = 1f
    open var scaleY: Float = 1f
    open var rotation: Float = 0f
    open var pivotX: Float = 0f
    open var pivotY: Float = 0f
    open var elevation: Float = 0f
    open var x: Float = 0f
    open var y: Float = 0f
    open var id: Int = NO_ID

    open val width: Int get() = 0
    open val height: Int get() = 0
    open val measuredWidth: Int get() = 0
    open val measuredHeight: Int get() = 0
    open val minimumWidth: Int get() = 0
    open val minimumHeight: Int get() = 0
    open val left: Int get() = 0
    open val top: Int get() = 0
    open val right: Int get() = 0
    open val bottom: Int get() = 0
    open val paddingLeft: Int get() = 0
    open val paddingTop: Int get() = 0
    open val paddingRight: Int get() = 0
    open val paddingBottom: Int get() = 0
    open val scrollX: Int get() = 0
    open val scrollY: Int get() = 0

    open val resources: android.content.res.Resources get() = context.resources
    private val viewHandler: Handler get() = Handler(Looper.getMainLooper())
    open val rootView: View get() = this
    open val display: Display? get() = context.display
    open val windowToken: IBinder? get() = null
    open val applicationWindowToken: IBinder? get() = null
    open val isShown: Boolean get() = visibility == VISIBLE
    open val isAttachedToWindow: Boolean get() = true
    open val isInTouchMode: Boolean get() = false
    open val isHardwareAccelerated: Boolean get() = false
    open val hasWindowFocus: Boolean get() = true
    open val drawableState: IntArray get() = IntArray(0)
    open val viewTreeObserver: ViewTreeObserver get() = ViewTreeObserver()

    /** 父容器（桌面无 View 树，默认 null）。 */
    open val parent: ViewParent? get() = null

    /** 布局参数（ViewGroup.LayoutParams 在 ViewGroups.kt）。 */
    open var layoutParams: ViewGroup.LayoutParams? = null
    open var importantForAccessibility: Int = IMPORTANT_FOR_ACCESSIBILITY_AUTO
    open val systemUiVisibility: Int get() = 0
    open val layoutDirection: Int get() = LAYOUT_DIRECTION_LTR
    open val textDirection: Int get() = TEXT_DIRECTION_LTR

    private var clickListener: OnClickListener? = null
    private var longClickListener: OnLongClickListener? = null
    private val tags = HashMap<Int, Any?>()

    open fun postOnAnimation(action: Runnable) { post(action) }
    open fun postOnAnimationDelayed(action: Runnable, delayMillis: Long) { postDelayed(action, delayMillis) }
    open fun sendAccessibilityEventUnchecked(event: android.view.accessibility.AccessibilityEvent) {}

    /** android.view.View.AccessibilityDelegate。 */
    open class AccessibilityDelegate {
        open fun sendAccessibilityEvent(host: View, eventType: Int) {}
        open fun sendAccessibilityEventUnchecked(host: View, event: android.view.accessibility.AccessibilityEvent) {}
        open fun onPopulateAccessibilityEvent(host: View, event: android.view.accessibility.AccessibilityEvent) {}
        open fun onInitializeAccessibilityEvent(host: View, event: android.view.accessibility.AccessibilityEvent) {}
        open fun onInitializeAccessibilityNodeInfo(host: View, info: android.view.accessibility.AccessibilityNodeInfo) {}
        open fun onRequestSendAccessibilityEvent(host: android.view.ViewGroup?, child: View, event: android.view.accessibility.AccessibilityEvent): Boolean = false
        open fun performAccessibilityAction(host: View, action: Int, args: android.os.Bundle?): Boolean = false
        open fun getAccessibilityNodeProvider(host: View): android.view.accessibility.AccessibilityNodeProvider? = null
        open fun dispatchPopulateAccessibilityEvent(host: View, event: android.view.accessibility.AccessibilityEvent): Boolean = false
    }

    open fun hasFocus(): Boolean = false
    open fun requestFocus(): Boolean = false
    open fun clearFocus() {}

    open fun invalidate() {}
    open fun invalidate(dirty: android.graphics.Rect) {}
    open fun invalidate(l: Int, t: Int, r: Int, b: Int) {}
    open fun postInvalidate() {}
    open fun postInvalidateDelayed(delayMilliseconds: Long) {}
    open fun requestLayout() {}
    open fun forceLayout() {}
    open fun measure(widthMeasureSpec: Int, heightMeasureSpec: Int) {}
    open fun layout(l: Int, t: Int, r: Int, b: Int) {}

    open fun post(action: Runnable): Boolean = viewHandler.post(action)
    open fun postDelayed(action: Runnable, delayMillis: Long): Boolean = viewHandler.postDelayed(action, delayMillis)
    open fun removeCallbacks(action: Runnable): Boolean {
        viewHandler.removeCallbacks(action)
        return true
    }

    open fun setOnClickListener(l: OnClickListener?) { clickListener = l }
    open fun setOnLongClickListener(l: OnLongClickListener?) { longClickListener = l }
    open fun setOnTouchListener(l: OnTouchListener?) {}
    open fun performClick(): Boolean {
        clickListener?.onClick(this)
        return clickListener != null
    }
    open fun callOnClick(): Boolean = performClick()
    open fun performLongClick(): Boolean {
        longClickListener?.onLongClick(this)
        return longClickListener != null
    }

    open fun addOnAttachStateChangeListener(listener: OnAttachStateChangeListener) {}
    open fun removeOnAttachStateChangeListener(listener: OnAttachStateChangeListener) {}
    open fun addOnLayoutChangeListener(listener: OnLayoutChangeListener) {}
    open fun removeOnLayoutChangeListener(listener: OnLayoutChangeListener) {}

    open fun setTag(key: Int, tag: Any?) { tags[key] = tag }
    open fun getTag(key: Int): Any? = tags[key]
    @Suppress("UNCHECKED_CAST")
    open fun <T : View?> findViewById(id: Int): T? = null
    open fun setMinimumWidth(minWidth: Int) {}
    /** View.setMinWidth/setMinHeight（TextView 风格的便捷写法）。——Nova 注 */
    open fun setMinWidth(minWidth: Int) {}
    open fun setMinHeight(minHeight: Int) {}
    open fun setMinimumHeight(minHeight: Int) {}
    open fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {}
    open fun announceForAccessibility(text: CharSequence?) {}
    open fun bringToFront() {}
    open fun startAnimation(animation: Any?) {}
    open fun clearAnimation() {}
    open fun animate(): ViewPropertyAnimator = ViewPropertyAnimator()
    open fun setKeepScreenOn(keepScreenOn: Boolean) {}
    open fun sendAccessibilityEvent(eventType: Int) {}
    open fun onCreateDrawableState(extraSpace: Int): IntArray = IntArray(extraSpace)

    open fun onInitializeAccessibilityNodeInfo(info: android.view.accessibility.AccessibilityNodeInfo) {}
    open fun onInitializeAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent) {}

    /** View.getAccessibilityNodeProvider / setAccessibilityNodeProvider。 */
    open var accessibilityNodeProvider: android.view.accessibility.AccessibilityNodeProvider? = null
    open fun dispatchHoverEvent(event: MotionEvent): Boolean = false
    open fun dispatchGenericMotionEvent(event: MotionEvent): Boolean = false
    open fun startActionMode(callback: ActionMode.Callback): ActionMode? = null
    open fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode? = null

    // ---- P3-B2 增补 ----
    open fun onTouchEvent(event: MotionEvent): Boolean = false
    /** View.dispatchTouchEvent：分发触摸事件，委托 onTouchEvent。——Nova 注 */
    open fun dispatchTouchEvent(event: MotionEvent): Boolean = onTouchEvent(event)
    open fun onDraw(canvas: android.graphics.Canvas?) {}
    open fun draw(canvas: android.graphics.Canvas?) {}
    open fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {}
    open fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {}
    open fun onAttachedToWindow() {}
    // ---- overrides-nothing 簇补齐（app 覆写这些 View 方法）——Nova 注 ----
    open fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {}
    open fun computeScroll() {}
    open fun postInvalidateOnAnimation() {}
    open fun onCheckIsTextEditor(): Boolean = false
    open fun onCreateInputConnection(outAttrs: android.view.inputmethod.EditorInfo?): android.view.inputmethod.InputConnection? = null
    open fun findFocus(): View? = null
    open fun getGlobalVisibleRect(r: android.graphics.Rect?): Boolean = false
    open fun getLocalVisibleRect(r: android.graphics.Rect?): Boolean = false
    open fun getHitRect(outRect: android.graphics.Rect?) {}
    /** 在 onMeasure 里调用设置测量尺寸。——Nova 注 */
    open fun setMeasuredDimension(measuredWidth: Int, measuredHeight: Int) {}
    open fun scrollTo(x: Int, y: Int) {}
    open fun scrollBy(x: Int, y: Int) {}
    open fun onDetachedFromWindow() {}
    open fun getLocationOnScreen(outLocation: IntArray) {
        if (outLocation.size >= 2) { outLocation[0] = 0; outLocation[1] = 0 }
    }
    open fun getLocationInWindow(outLocation: IntArray) {
        if (outLocation.size >= 2) { outLocation[0] = 0; outLocation[1] = 0 }
    }
    open fun performHapticFeedback(hint: Int): Boolean = false
    open fun performHapticFeedback(hint: Int, flags: Int): Boolean = false
    open fun setBackgroundColor(color: Int) {}
    open fun setBackground(background: android.graphics.drawable.Drawable?) {}
    @Deprecated("deprecated")
    open fun setBackgroundDrawable(background: android.graphics.drawable.Drawable?) {}
    open fun setBackgroundResource(resid: Int) {}
    @Deprecated("deprecated")
    open fun setSystemUiVisibility(visibility: Int) {}
    open fun getWindowVisibility(): Int = VISIBLE
    open fun setWillNotDraw(willNotDraw: Boolean) {}
    open fun isInEditMode(): Boolean = false
    open fun setLayoutParams(params: ViewGroup.LayoutParams?, @Suppress("UNUSED_PARAMETER") ignored: Unit = Unit) {
        layoutParams = params
    }

    companion object {
        const val NO_ID = -1
        const val VISIBLE = 0
        const val INVISIBLE = 4
        const val GONE = 8
        const val OVER_SCROLL_ALWAYS = 0
        const val OVER_SCROLL_IF_CONTENT_SCROLLS = 1
        const val OVER_SCROLL_NEVER = 2
        const val LAYOUT_DIRECTION_LTR = 0
        const val LAYOUT_DIRECTION_RTL = 1
        const val LAYOUT_DIRECTION_INHERIT = 2
        const val LAYOUT_DIRECTION_LOCALE = 3
        const val TEXT_DIRECTION_LTR = 0
        const val TEXT_DIRECTION_RTL = 1
        const val TEXT_DIRECTION_INHERIT = 2
        const val TEXT_DIRECTION_FIRST_STRONG = 3
        const val IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0
        const val IMPORTANT_FOR_ACCESSIBILITY_YES = 1
        const val IMPORTANT_FOR_ACCESSIBILITY_NO = 2
        const val IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4
        const val MEASURED_SIZE_MASK = 0x00ffffff
        const val MEASURED_STATE_MASK = 0xff000000.toInt()
        const val SCROLLBAR_POSITION_DEFAULT = 0
        const val FOCUS_UP = 33
        const val FOCUS_DOWN = 130
        const val FOCUS_LEFT = 17
        const val FOCUS_RIGHT = 66
        const val FOCUS_FORWARD = 6
        const val FOCUS_BACKWARD = 1
        const val FOCUSABLES_ALL = 0
        const val FOCUSABLES_TOUCH_MODE = 1

        @JvmStatic fun generateViewId(): Int = java.util.concurrent.atomic.AtomicInteger(1).incrementAndGet()
        @JvmStatic fun combineMeasuredStates(curState: Int, newState: Int): Int = curState or newState
        @JvmStatic fun resolveSize(size: Int, measureSpec: Int): Int = size
        @JvmStatic fun resolveSizeAndState(size: Int, measureSpec: Int, childMeasuredState: Int): Int = size
        @JvmStatic fun getDefaultSize(size: Int, measureSpec: Int): Int = size
    }

    /** android.view.View.MeasureSpec：真实位运算。 */
    object MeasureSpec {
        private const val MODE_SHIFT = 30
        private const val MODE_MASK = 0x3 shl MODE_SHIFT

        const val UNSPECIFIED = 0 shl MODE_SHIFT
        const val EXACTLY = 1 shl MODE_SHIFT
        const val AT_MOST = 2 shl MODE_SHIFT

        @JvmStatic
        fun makeMeasureSpec(size: Int, mode: Int): Int =
            (size and MODE_MASK.inv()) or (mode and MODE_MASK)

        @JvmStatic
        fun getMode(measureSpec: Int): Int = measureSpec and MODE_MASK

        @JvmStatic
        fun getSize(measureSpec: Int): Int = measureSpec and MODE_MASK.inv()

        override fun toString(): String = "MeasureSpec"
    }
}

/** android.view.ViewPropertyAnimator 轻 stub。 */
open class ViewPropertyAnimator {
    open fun alpha(value: Float): ViewPropertyAnimator = this
    open fun translationX(value: Float): ViewPropertyAnimator = this
    open fun translationY(value: Float): ViewPropertyAnimator = this
    open fun scaleX(value: Float): ViewPropertyAnimator = this
    open fun scaleY(value: Float): ViewPropertyAnimator = this
    open fun rotation(value: Float): ViewPropertyAnimator = this
    open fun x(value: Float): ViewPropertyAnimator = this
    open fun y(value: Float): ViewPropertyAnimator = this
    open fun setDuration(duration: Long): ViewPropertyAnimator = this
    open fun setStartDelay(startDelay: Long): ViewPropertyAnimator = this
    open fun start() {}
    open fun cancel() {}
    open fun withEndAction(r: Runnable?): ViewPropertyAnimator = this
    open fun withStartAction(r: Runnable?): ViewPropertyAnimator = this
    open fun withLayer(): ViewPropertyAnimator = this
}

/** android.view.ViewTreeObserver 轻 stub。 */
open class ViewTreeObserver {
    fun interface OnGlobalLayoutListener { fun onGlobalLayout() }
    fun interface OnPreDrawListener { fun onPreDraw(): Boolean }
    fun interface OnDrawListener { fun onDraw() }
    fun interface OnScrollChangedListener { fun onScrollChanged() }

    open fun addOnGlobalLayoutListener(listener: OnGlobalLayoutListener) {}
    open fun removeOnGlobalLayoutListener(listener: OnGlobalLayoutListener) {}
    open fun addOnPreDrawListener(listener: OnPreDrawListener) {}
    open fun removeOnPreDrawListener(listener: OnPreDrawListener) {}
    open fun addOnDrawListener(listener: OnDrawListener) {}
    open fun removeOnDrawListener(listener: OnDrawListener) {}
    open fun addOnScrollChangedListener(listener: OnScrollChangedListener) {}
    open fun removeOnScrollChangedListener(listener: OnScrollChangedListener) {}
    open fun isAlive(): Boolean = true
    open fun dispatchOnGlobalLayout() {}
    open fun dispatchOnPreDraw(): Boolean = true
}

/** android.view.Gravity 常量。 */
object Gravity {
    const val NO_GRAVITY = 0x0000
    const val AXIS_SPECIFIED = 0x0001
    const val AXIS_PULL_BEFORE = 0x0002
    const val AXIS_PULL_AFTER = 0x0004
    const val AXIS_CLIP = 0x0008
    const val AXIS_X_SHIFT = 0
    const val AXIS_Y_SHIFT = 4
    const val TOP = AXIS_PULL_BEFORE shl AXIS_Y_SHIFT
    const val BOTTOM = AXIS_PULL_AFTER shl AXIS_Y_SHIFT
    const val LEFT = AXIS_PULL_BEFORE shl AXIS_X_SHIFT
    const val RIGHT = AXIS_PULL_AFTER shl AXIS_X_SHIFT
    const val CENTER_VERTICAL = AXIS_SPECIFIED shl AXIS_Y_SHIFT
    const val CENTER_HORIZONTAL = AXIS_SPECIFIED
    const val CENTER = CENTER_VERTICAL or CENTER_HORIZONTAL
    const val FILL_VERTICAL = TOP or BOTTOM
    const val FILL_HORIZONTAL = LEFT or RIGHT
    const val FILL = FILL_VERTICAL or FILL_HORIZONTAL
    const val CLIP_VERTICAL = 0x80
    const val CLIP_HORIZONTAL = 0x08
    const val RELATIVE_LAYOUT_DIRECTION = 0x00800000
    const val START = RELATIVE_LAYOUT_DIRECTION or LEFT
    const val END = RELATIVE_LAYOUT_DIRECTION or RIGHT
    const val HORIZONTAL_GRAVITY_MASK = AXIS_SPECIFIED or AXIS_PULL_BEFORE or AXIS_PULL_AFTER
    const val VERTICAL_GRAVITY_MASK = (AXIS_SPECIFIED or AXIS_PULL_BEFORE or AXIS_PULL_AFTER) shl AXIS_Y_SHIFT
    const val DISPLAY_CLIP_VERTICAL = 0x10000000
    const val DISPLAY_CLIP_HORIZONTAL = 0x01000000

    @JvmStatic
    fun getAbsoluteGravity(gravity: Int, layoutDirection: Int): Int {
        var result = gravity
        if (result and RELATIVE_LAYOUT_DIRECTION != 0) {
            result = result and RELATIVE_LAYOUT_DIRECTION.inv()
        }
        return result
    }

    @JvmStatic
    fun isHorizontal(gravity: Int): Boolean = gravity and HORIZONTAL_GRAVITY_MASK != 0

    @JvmStatic
    fun isVertical(gravity: Int): Boolean = gravity and VERTICAL_GRAVITY_MASK != 0

    @JvmStatic
    fun apply(gravity: Int, w: Int, h: Int, container: android.graphics.Rect, outRect: android.graphics.Rect) {
        apply(gravity, w, h, container, 0, 0, outRect)
    }

    @JvmStatic
    fun apply(
        gravity: Int, w: Int, h: Int, container: android.graphics.Rect,
        xAdj: Int, yAdj: Int, outRect: android.graphics.Rect,
    ) {
        var left = container.left + xAdj
        var top = container.top + yAdj
        when (gravity and HORIZONTAL_GRAVITY_MASK) {
            CENTER_HORIZONTAL -> left = container.left + (container.width() - w) / 2 + xAdj
            RIGHT -> left = container.right - w + xAdj
        }
        when (gravity and VERTICAL_GRAVITY_MASK) {
            CENTER_VERTICAL -> top = container.top + (container.height() - h) / 2 + yAdj
            BOTTOM -> top = container.bottom - h + yAdj
        }
        outRect.set(left, top, left + w, top + h)
    }

    @JvmStatic
    fun applyDisplay(gravity: Int, display: android.graphics.Rect, inoutObj: android.graphics.Rect) {}
}

/** android.view.KeyEvent 轻 stub。 */
open class KeyEvent(
    val action: Int = ACTION_DOWN,
    val keyCode: Int = 0,
) {
    // 属性式公开（app 用 event.keyCode/action/actionMasked/isShiftPressed 等）——Nova 注
    val actionMasked: Int get() = action
    val unicodeChar: Int get() = 0
    val displayLabel: Char get() = ' '
    val number: Char get() = ' '
    val repeatCount: Int get() = 0
    val modifiers: Int get() = 0
    val metaState: Int get() = 0
    val isShiftPressed: Boolean get() = false
    val isCtrlPressed: Boolean get() = false
    val isAltPressed: Boolean get() = false
    val isMetaPressed: Boolean get() = false
    val isCapsLockOn: Boolean get() = false
    val isNumLockOn: Boolean get() = false
    val isPrintingKey: Boolean get() = false
    val isLongPress: Boolean get() = false
    val eventTime: Long get() = android.os.SystemClock.uptimeMillis()
    val downTime: Long get() = android.os.SystemClock.uptimeMillis()
    val deviceId: Int get() = 0
    val source: Int get() = 0
    val scanCode: Int get() = 0
    val isCanceled: Boolean get() = false

    companion object {
        const val ACTION_DOWN = 0
        const val ACTION_UP = 1
        const val ACTION_MULTIPLE = 2

        const val KEYCODE_UNKNOWN = 0
        const val KEYCODE_HOME = 3
        const val KEYCODE_BACK = 4
        const val KEYCODE_CALL = 5
        const val KEYCODE_ENDCALL = 6
        const val KEYCODE_0 = 7
        const val KEYCODE_9 = 16
        const val KEYCODE_STAR = 17
        const val KEYCODE_POUND = 18
        const val KEYCODE_DPAD_UP = 19
        const val KEYCODE_DPAD_DOWN = 20
        const val KEYCODE_DPAD_LEFT = 21
        const val KEYCODE_DPAD_RIGHT = 22
        const val KEYCODE_DPAD_CENTER = 23
        const val KEYCODE_VOLUME_UP = 24
        const val KEYCODE_VOLUME_DOWN = 25
        const val KEYCODE_POWER = 26
        const val KEYCODE_CAMERA = 27
        const val KEYCODE_CLEAR = 28
        const val KEYCODE_A = 29
        const val KEYCODE_Z = 54
        const val KEYCODE_COMMA = 55
        const val KEYCODE_PERIOD = 56
        const val KEYCODE_ALT_LEFT = 57
        const val KEYCODE_ALT_RIGHT = 58
        const val KEYCODE_SHIFT_LEFT = 59
        const val KEYCODE_SHIFT_RIGHT = 60
        const val KEYCODE_TAB = 61
        const val KEYCODE_SPACE = 62
        const val KEYCODE_ENTER = 66
        const val KEYCODE_DEL = 67
        const val KEYCODE_GRAVE = 68
        const val KEYCODE_MINUS = 69
        const val KEYCODE_EQUALS = 70
        const val KEYCODE_LEFT_BRACKET = 71
        const val KEYCODE_RIGHT_BRACKET = 72
        const val KEYCODE_BACKSLASH = 73
        const val KEYCODE_SEMICOLON = 74
        const val KEYCODE_APOSTROPHE = 75
        const val KEYCODE_SLASH = 76
        const val KEYCODE_AT = 77
        const val KEYCODE_PLUS = 81
        const val KEYCODE_MENU = 82
        const val KEYCODE_SEARCH = 84
        const val KEYCODE_MEDIA_PLAY_PAUSE = 85
        const val KEYCODE_MEDIA_STOP = 86
        const val KEYCODE_MEDIA_NEXT = 87
        const val KEYCODE_MEDIA_PREVIOUS = 88
        const val KEYCODE_MEDIA_REWIND = 89
        const val KEYCODE_MEDIA_FAST_FORWARD = 90
        const val KEYCODE_MUTE = 91
        const val KEYCODE_PAGE_UP = 92
        const val KEYCODE_PAGE_DOWN = 93
        const val KEYCODE_ESCAPE = 111
        const val KEYCODE_FORWARD_DEL = 112
        const val KEYCODE_CTRL_LEFT = 113
        const val KEYCODE_CTRL_RIGHT = 114
        const val KEYCODE_CAPS_LOCK = 115
        const val KEYCODE_SCROLL_LOCK = 116
        const val KEYCODE_META_LEFT = 117
        const val KEYCODE_META_RIGHT = 118
        const val KEYCODE_INSERT = 124
        const val KEYCODE_F1 = 131
        const val KEYCODE_F12 = 142
        const val KEYCODE_NUM_LOCK = 143
        const val KEYCODE_MOVE_HOME = 122
        const val KEYCODE_MOVE_END = 123

        const val META_SHIFT_ON = 1
        const val META_ALT_ON = 2
        const val META_CTRL_ON = 4096
        const val META_META_ON = 65536

        const val FLAG_CANCELED = 32
        const val FLAG_LONG_PRESS = 128
        const val FLAG_TRACKING = 512

        @JvmStatic fun isModifierKey(keyCode: Int): Boolean =
            keyCode in KEYCODE_SHIFT_LEFT..KEYCODE_SHIFT_RIGHT || keyCode in KEYCODE_ALT_LEFT..KEYCODE_ALT_RIGHT ||
                keyCode in KEYCODE_CTRL_LEFT..KEYCODE_CTRL_RIGHT || keyCode in KEYCODE_META_LEFT..KEYCODE_META_RIGHT
    }
}

/** android.view.Display：包装桌面显示器信息。 */
open class Display {
    private val metrics = DisplayMetrics()

    open fun getMetrics(outMetrics: DisplayMetrics) { outMetrics.setTo(metrics) }
    open fun getRealMetrics(outMetrics: DisplayMetrics) { outMetrics.setTo(metrics) }
    open fun getSize(outPoint: android.graphics.Point) { outPoint.set(metrics.widthPixels, metrics.heightPixels) }
    open fun getRealSize(outPoint: android.graphics.Point) { getSize(outPoint) }

    @Deprecated("deprecated") open fun getWidth(): Int = metrics.widthPixels
    @Deprecated("deprecated") open fun getHeight(): Int = metrics.heightPixels

    open fun getRotation(): Int = 0
    open fun getDisplayId(): Int = 0
    open fun getName(): String = "Built-in Display"
    open fun getRefreshRate(): Float = 60.0f
    open fun getState(): Int = STATE_ON
    open fun isValid(): Boolean = true
    open fun getMode(): Mode = Mode(0, metrics.widthPixels, metrics.heightPixels, 60.0f)
    /** supportedModes 属性（app 用 display.supportedModes）——Nova 注 */
    open val supportedModes: Array<Mode> get() = arrayOf(getMode())
    open fun getPixelFormat(): Int = android.graphics.PixelFormat.RGBA_8888
    open fun isHdr(): Boolean = false
    open fun isWideColorGamut(): Boolean = false

    class Mode(
        private val modeId: Int,
        private val width: Int,
        private val height: Int,
        private val refreshRate: Float,
    ) {
        fun getModeId(): Int = modeId
        fun getPhysicalWidth(): Int = width
        fun getPhysicalHeight(): Int = height
        fun getRefreshRate(): Float = this.refreshRate
    }

    companion object {
        const val DEFAULT_DISPLAY = 0
        const val STATE_UNKNOWN = 0
        const val STATE_OFF = 1
        const val STATE_ON = 2
        const val STATE_DOZE = 3
        const val STATE_DOZE_SUSPEND = 4
        const val STATE_ON_SUSPEND = 5
        const val STATE_VR = 6

        const val ROTATION_0 = 0
        const val ROTATION_90 = 1
        const val ROTATION_180 = 2
        const val ROTATION_270 = 3

        const val TYPE_UNKNOWN = 0
        const val TYPE_BUILT_IN = 1
        const val TYPE_HDMI = 2
        const val TYPE_WIFI = 3
        const val TYPE_OVERLAY = 4
        const val TYPE_VIRTUAL = 5

        const val FLAG_PRESENTATION = 2
        const val FLAG_PRIVATE = 4
        const val FLAG_ROUND = 8
        const val FLAG_SUPPORTS_PROTECTED_BUFFERS = 1
        const val FLAG_SECURE = 32
    }
}

/** android.view.HapticFeedbackConstants 常量。 */
object HapticFeedbackConstants {
    const val LONG_PRESS = 0
    const val VIRTUAL_KEY = 1
    const val KEYBOARD_TAP = 3
    const val CLOCK_TICK = 4
    const val CALENDAR_DATE = 5
    const val CONTEXT_CLICK = 6
    const val KEYBOARD_PRESS = 3
    const val KEYBOARD_RELEASE = 7
    const val TEXT_HANDLE_MOVE = 9
    const val ENTRY_BUMP = 10
    const val DRAG_CROSSING = 11
    const val GESTURE_START = 12
    const val GESTURE_END = 13
    const val EDGE_SQUEEZE = 14
    const val EDGE_RELEASE = 15
    const val CONFIRM = 16
    const val REJECT = 17
    const val TOGGLE_ON = 21
    const val TOGGLE_OFF = 22
    const val SEGMENT_TICK = 26
    const val SEGMENT_FREQUENT_TICK = 27
    const val FLAG_IGNORE_VIEW_SETTING = 1
    const val FLAG_IGNORE_GLOBAL_SETTING = 2
}

/** android.view.ViewConfiguration：桌面合理默认值。 */
open class ViewConfiguration {
    companion object {
        @JvmStatic fun get(context: Context): ViewConfiguration = ViewConfiguration()
        @JvmStatic fun getScrollBarSize(): Int = 12
        @JvmStatic fun getFadingEdgeLength(): Int = 12
        @JvmStatic fun getPressedStateDuration(): Int = 64
        @JvmStatic fun getLongPressTimeout(): Int = 500
        @JvmStatic fun getTapTimeout(): Int = 100
        @JvmStatic fun getJumpTapTimeout(): Int = 500
        @JvmStatic fun getDoubleTapTimeout(): Int = 300
        @JvmStatic fun getDoubleTapMinTime(): Int = 40
        @JvmStatic fun getHoverTapTimeout(): Int = 150
        @JvmStatic fun getHoverTapSlop(): Int = 20
        @JvmStatic fun getEdgeSlop(): Int = 12
        @JvmStatic fun getTouchSlop(): Int = 8
        @JvmStatic fun getScrollDefaultDelay(): Int = 300
        @JvmStatic fun getScrollFriction(): Float = 0.015f
        @JvmStatic fun getMinimumFlingVelocity(): Int = 50
        @JvmStatic fun getMaximumFlingVelocity(): Int = 8000
        @JvmStatic fun getKeyRepeatTimeout(): Int = 500
        @JvmStatic fun getKeyRepeatDelay(): Int = 50
        @JvmStatic fun getGlobalActionKeyTimeout(): Int = 500
        @JvmStatic fun getZoomControlsTimeout(): Long = 3000
        @JvmStatic fun getDoubleTapSlop(): Int = 100
        @JvmStatic fun getScaledMaximumDrawingCacheSize(): Int = 0
    }

    val scaledTouchSlop: Int get() = 8
    val scaledScrollBarSize: Int get() = 12
    val scaledMinimumFlingVelocity: Int get() = 50
    val scaledMaximumFlingVelocity: Int get() = 8000
    val scaledPagingTouchSlop: Int get() = 16
    val scaledDoubleTapSlop: Int get() = 100
    val scaledEdgeSlop: Int get() = 12
    val scaledHoverTapSlop: Int get() = 20
    fun hasPermanentMenuKey(): Boolean = true
}

/** android.view.VelocityTracker。桌面无触摸速度追踪，全部返回 0。 */
class VelocityTracker private constructor() {
    val xVelocity: Float get() = 0f
    val yVelocity: Float get() = 0f
    fun addMovement(event: MotionEvent) {}
    fun computeCurrentVelocity(units: Int) {}
    fun computeCurrentVelocity(units: Int, maxVelocity: Float) {}
    fun getXVelocity(pointerId: Int): Float = 0f
    fun getYVelocity(pointerId: Int): Float = 0f
    fun clear() {}
    fun recycle() {}
    companion object {
        @JvmStatic fun obtain(): VelocityTracker = VelocityTracker()
    }
}
