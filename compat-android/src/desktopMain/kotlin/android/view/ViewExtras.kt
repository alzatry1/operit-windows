package android.view

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * android.view 杂项垫片（P3-B2 新增）。
 * Choreographer 真实回调（走 main Handler）；LayoutInflater 返回空 View；
 * GestureDetector/ScaleGestureDetector 编译级 stub。
 */

/** android.view.SoundEffectConstants。 */
object SoundEffectConstants {
    const val CLICK = 0
    const val NAVIGATION_LEFT = 1
    const val NAVIGATION_UP = 2
    const val NAVIGATION_RIGHT = 3
    const val NAVIGATION_DOWN = 4
    const val NAVIGATION_REPEATED_LEFT = 5
    const val NAVIGATION_REPEATED_UP = 6
    const val NAVIGATION_REPEATED_RIGHT = 7
    const val NAVIGATION_REPEATED_DOWN = 8
    const val TEXT_SELECTION_MOVE = 9
    const val CONTIGUOUS_NAVIGATION_REPEAT = 12

    @JvmStatic
    fun getContantForFocusDirection(direction: Int): Int = CLICK
}

/** android.view.Choreographer：帧回调真实投递到 main Handler。 */
open class Choreographer {

    fun interface FrameCallback {
        fun doFrame(frameTimeNanos: Long)
    }

    open fun postFrameCallback(callback: FrameCallback?) {
        if (callback != null) {
            Handler(Looper.getMainLooper()).post { callback.doFrame(System.nanoTime()) }
        }
    }

    open fun postFrameCallbackDelayed(callback: FrameCallback?, delayMillis: Long) {
        if (callback != null) {
            Handler(Looper.getMainLooper()).postDelayed({ callback.doFrame(System.nanoTime()) }, delayMillis)
        }
    }

    open fun removeFrameCallback(callback: FrameCallback?) {}

    open fun postCallback(callbackType: Int, action: Runnable?, token: Any?) {
        if (action != null) Handler(Looper.getMainLooper()).post(action)
    }

    open fun removeCallbacks(callbackType: Int, action: Runnable?, token: Any?) {}

    companion object {
        const val CALLBACK_INPUT = 0
        const val CALLBACK_ANIMATION = 1
        const val CALLBACK_INSETS_ANIMATION = 2
        const val CALLBACK_TRAVERSAL = 3
        const val CALLBACK_COMMIT = 4

        private val sharedInstance by lazy { Choreographer() }

        @JvmStatic
        fun getInstance(): Choreographer = sharedInstance

        @JvmStatic
        fun getFrameTime(): Long = System.nanoTime() / 1000000

        @JvmStatic
        fun getFrameTimeNanos(): Long = System.nanoTime()

        @JvmStatic
        fun subtractFrameDelay(delayMillis: Long): Long = delayMillis

        @JvmStatic
        fun getRefreshRate(): Float = 60.0f
    }
}

/** android.view.LayoutInflater：桌面无 XML 布局，inflate 返回空 View。 */
open class LayoutInflater private constructor(private val context: Context) {

    interface Factory {
        fun onCreateView(name: String, context: Context, attrs: android.util.AttributeSet?): View?
    }

    interface Factory2 : Factory {
        fun onCreateView(parent: View?, name: String, context: Context, attrs: android.util.AttributeSet?): View? = null
    }

    interface Filter {
        fun onLoadClass(clazz: Class<*>): Boolean
    }

    open fun inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean): View = View(context)

    open fun inflate(resource: Int, root: ViewGroup?): View = inflate(resource, root, root != null)

    open fun inflate(parser: Any?, root: ViewGroup?, attachToRoot: Boolean): View = View(context)

    open fun cloneInContext(newContext: Context): LayoutInflater = this

    open fun setFactory(factory: Factory?) {}
    open fun setFactory2(factory: Factory2?) {}
    open fun setFilter(filter: Filter?) {}

    companion object {
        @JvmStatic
        fun from(context: Context): LayoutInflater = LayoutInflater(context)
    }
}

/** android.view.GestureDetector。 */
open class GestureDetector {

    interface OnGestureListener {
        fun onDown(e: MotionEvent): Boolean
        fun onShowPress(e: MotionEvent) {}
        fun onSingleTapUp(e: MotionEvent): Boolean
        fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean
        fun onLongPress(e: MotionEvent) {}
        fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean
    }

    interface OnDoubleTapListener {
        fun onSingleTapConfirmed(e: MotionEvent): Boolean
        fun onDoubleTap(e: MotionEvent): Boolean
        fun onDoubleTapEvent(e: MotionEvent): Boolean
    }

    interface OnContextClickListener {
        fun onContextClick(e: MotionEvent): Boolean
    }

    open class SimpleOnGestureListener : OnGestureListener, OnDoubleTapListener, OnContextClickListener {
        override fun onDown(e: MotionEvent): Boolean = false
        override fun onSingleTapUp(e: MotionEvent): Boolean = false
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean = false
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean = false
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean = false
        override fun onDoubleTap(e: MotionEvent): Boolean = false
        override fun onDoubleTapEvent(e: MotionEvent): Boolean = false
        override fun onContextClick(e: MotionEvent): Boolean = false
    }

    private val listener: OnGestureListener?

    constructor(context: Context?, listener: OnGestureListener?) {
        this.listener = listener
    }

    constructor(context: Context?, listener: OnGestureListener?, handler: Handler?) {
        this.listener = listener
    }

    constructor(context: Context?, listener: OnGestureListener?, handler: Handler?, unused: Boolean) {
        this.listener = listener
    }

    open fun onTouchEvent(ev: MotionEvent?): Boolean = false
    open fun onGenericMotionEvent(ev: MotionEvent?): Boolean = false
    open fun isLongpressEnabled(): Boolean = true
    open fun setIsLongpressEnabled(enabled: Boolean) {}
    open fun setOnDoubleTapListener(listener: OnDoubleTapListener?) {}
    open fun setContextClickListener(listener: OnContextClickListener?) {}
}

/** android.view.ScaleGestureDetector。 */
open class ScaleGestureDetector {

    interface OnScaleGestureListener {
        fun onScale(detector: ScaleGestureDetector): Boolean
        fun onScaleBegin(detector: ScaleGestureDetector): Boolean
        fun onScaleEnd(detector: ScaleGestureDetector) {}
    }

    open class SimpleOnScaleGestureListener : OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean = false
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true
    }

    constructor(context: Context?, listener: OnScaleGestureListener?)
    constructor(context: Context?, listener: OnScaleGestureListener?, handler: Handler?)

    open fun onTouchEvent(event: MotionEvent?): Boolean = false
    open val isInProgress: Boolean = false
    open val scaleFactor: Float = 1.0f
    open val focusX: Float = 0f
    open val focusY: Float = 0f
    open val currentSpan: Float = 0f
    open val previousSpan: Float = 0f
    open val timeDelta: Long = 0L
    open val eventTime: Long = 0L
}

/** android.view.Menu。 */
interface Menu {
    fun size(): Int = 0
    fun getItem(index: Int): MenuItem? = null
    fun add(title: CharSequence?): MenuItem? = null
    fun add(groupId: Int, itemId: Int, order: Int, title: CharSequence?): MenuItem? = null
    fun add(groupId: Int, itemId: Int, order: Int, titleRes: Int): MenuItem? = null
    fun removeItem(id: Int) {}
    fun clear() {}
    fun setQwertyMode(isQwerty: Boolean) {}
    fun findItem(id: Int): MenuItem? = null
}

/** android.view.MenuItem。 */
interface MenuItem {
    val itemId: Int get() = 0
    val groupId: Int get() = 0
    val order: Int get() = 0
    var title: CharSequence?
        get() = null
        set(value) {}
    var isVisible: Boolean
        get() = true
        set(value) {}
    var isEnabled: Boolean
        get() = true
        set(value) {}
    var isChecked: Boolean
        get() = false
        set(value) {}
    fun setIcon(iconRes: Int): MenuItem = this
    fun setShowAsAction(actionEnum: Int) {}
    fun expandActionView(): Boolean = false
    fun collapseActionView(): Boolean = false

    companion object {
        const val SHOW_AS_ACTION_NEVER = 0
        const val SHOW_AS_ACTION_IF_ROOM = 1
        const val SHOW_AS_ACTION_ALWAYS = 2
        const val SHOW_AS_ACTION_WITH_TEXT = 4
        const val SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8
    }
}

/** android.view.ActionMode。 */
interface ActionMode {
    fun finish() {}
    fun invalidate() {}
    fun setTitle(title: CharSequence?) {}
    fun setSubtitle(subtitle: CharSequence?) {}
    fun getMenu(): Menu? = null

    interface Callback {
        fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean
        fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean
        fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean
        fun onDestroyActionMode(mode: ActionMode)
    }

    /** ActionMode.Callback2：带内容矩形的回调（浮动选择菜单）。 */
    abstract class Callback2 : Callback {
        open fun onGetContentRect(mode: ActionMode, view: android.view.View, outRect: android.graphics.Rect) {}
    }

    companion object {
        const val TYPE_PRIMARY = 0
        const val TYPE_FLOATING = 1
    }
}
