package android.view

/**
 * android.view 输入事件（P3-B2 新增）。
 * MotionEvent 字段齐全 + obtain 工厂；KeyEvent 已在 ViewStubs.kt（B1a）实现。
 */

/** android.view.InputEvent。 */
open class InputEvent {
    open val deviceId: Int = 0
    open val eventTime: Long = android.os.SystemClock.uptimeMillis()
    open val source: Int = 0
    open val displayId: Int = 0
    open fun isCanceled(): Boolean = false
}

/** android.view.MotionEvent。 */
open class MotionEvent(
    private val actionField: Int = ACTION_DOWN,
    private val xField: Float = 0f,
    private val yField: Float = 0f,
) : InputEvent() {

    open val action: Int get() = actionField
    open val actionMasked: Int get() = actionField and ACTION_MASK
    open val actionIndex: Int get() = (actionField and ACTION_POINTER_INDEX_MASK) shr ACTION_POINTER_INDEX_SHIFT

    open val x: Float get() = xField
    open val y: Float get() = yField
    open val rawX: Float get() = xField
    open val rawY: Float get() = yField

    open val pointerCount: Int = 1
    open val downTime: Long = android.os.SystemClock.uptimeMillis()
    override val eventTime: Long = android.os.SystemClock.uptimeMillis()
    override val deviceId: Int = 0
    override val source: Int = 0
    open val historySize: Int = 0
    open val pressure: Float = 1.0f
    open val size: Float = 0.0f
    open val toolType: Int = TOOL_TYPE_FINGER
    open val metaState: Int = 0
    open val buttonState: Int = 0
    open val xPrecision: Float = 1.0f
    open val yPrecision: Float = 1.0f
    open val edgeFlags: Int = 0
    open val actionButton: Int = 0

    open fun getX(pointerIndex: Int): Float = xField
    open fun getHistoricalX(pointerIndex: Int, pos: Int): Float = xField
    open fun getHistoricalY(pointerIndex: Int, pos: Int): Float = yField
    open fun getHistoricalPressure(pointerIndex: Int, pos: Int): Float = pressure
    open fun getHistoricalSize(pointerIndex: Int, pos: Int): Float = size
    open fun getHistoricalEventTime(pointerIndex: Int, pos: Int): Long = eventTime
    open fun getHistoricalEventTime(pos: Int): Long = eventTime
    open fun getHistoricalX(pos: Int): Float = xField
    open fun getHistoricalY(pos: Int): Float = yField
    open fun getY(pointerIndex: Int): Float = yField
    open fun getPointerId(pointerIndex: Int): Int = 0
    open fun getPressure(pointerIndex: Int): Float = pressure
    open fun getSize(pointerIndex: Int): Float = size
    open fun getToolType(pointerIndex: Int): Int = toolType
    open fun getAxisValue(axis: Int): Float = 0f
    open fun getAxisValue(axis: Int, pointerIndex: Int): Float = 0f
    open fun findPointerIndex(pointerId: Int): Int = if (pointerId == 0) 0 else -1
    open fun offsetLocation(deltaX: Float, deltaY: Float) {}
    open fun setLocation(px: Float, py: Float) {}
    open fun setAction(action: Int) {}
    open fun recycle() {}
    open fun isTouchEvent(): Boolean = true
    open fun isFromSource(source: Int): Boolean = false
    open fun isShiftPressed(): Boolean = metaState and KeyEvent.META_SHIFT_ON != 0
    open fun isCtrlPressed(): Boolean = metaState and KeyEvent.META_CTRL_ON != 0
    open fun isAltPressed(): Boolean = metaState and KeyEvent.META_ALT_ON != 0

    companion object {
        const val ACTION_DOWN = 0
        const val ACTION_UP = 1
        const val ACTION_MOVE = 2
        const val ACTION_CANCEL = 3
        const val ACTION_OUTSIDE = 4
        const val ACTION_POINTER_DOWN = 5
        const val ACTION_POINTER_UP = 6
        const val ACTION_HOVER_MOVE = 7
        const val ACTION_SCROLL = 8
        const val ACTION_HOVER_ENTER = 9
        const val ACTION_HOVER_EXIT = 10
        const val ACTION_BUTTON_PRESS = 11
        const val ACTION_BUTTON_RELEASE = 12

        const val ACTION_MASK = 0xff
        const val ACTION_POINTER_INDEX_MASK = 0xff00
        const val ACTION_POINTER_INDEX_SHIFT = 8

        const val TOOL_TYPE_UNKNOWN = 0
        const val TOOL_TYPE_FINGER = 1
        const val TOOL_TYPE_STYLUS = 2
        const val TOOL_TYPE_MOUSE = 3
        const val TOOL_TYPE_ERASER = 4

        const val AXIS_X = 0
        const val AXIS_Y = 1
        const val AXIS_PRESSURE = 2
        const val AXIS_SIZE = 3
        const val AXIS_TOUCH_MAJOR = 4
        const val AXIS_TOUCH_MINOR = 5
        const val AXIS_TOOL_MAJOR = 6
        const val AXIS_TOOL_MINOR = 7
        const val AXIS_ORIENTATION = 8
        const val AXIS_VSCROLL = 9
        const val AXIS_HSCROLL = 10
        const val AXIS_Z = 11
        const val AXIS_RX = 12
        const val AXIS_RY = 13
        const val AXIS_RZ = 14
        const val AXIS_HAT_X = 15
        const val AXIS_HAT_Y = 16

        const val BUTTON_PRIMARY = 1
        const val BUTTON_SECONDARY = 2
        const val BUTTON_TERTIARY = 4

        @JvmStatic
        fun obtain(): MotionEvent = MotionEvent()

        @JvmStatic
        fun obtain(downTime: Long, eventTime: Long, action: Int, x: Float, y: Float, metaState: Int): MotionEvent =
            MotionEvent(action, x, y)

        @JvmStatic
        fun obtain(downTime: Long, eventTime: Long, action: Int, pointerCount: Int, x: FloatArray?, y: FloatArray?, metaState: Int): MotionEvent =
            MotionEvent(action, x?.firstOrNull() ?: 0f, y?.firstOrNull() ?: 0f)

        @JvmStatic
        fun actionToString(action: Int): String = when (action) {
            ACTION_DOWN -> "ACTION_DOWN"
            ACTION_UP -> "ACTION_UP"
            ACTION_MOVE -> "ACTION_MOVE"
            ACTION_CANCEL -> "ACTION_CANCEL"
            ACTION_OUTSIDE -> "ACTION_OUTSIDE"
            ACTION_POINTER_DOWN -> "ACTION_POINTER_DOWN"
            ACTION_POINTER_UP -> "ACTION_POINTER_UP"
            ACTION_HOVER_MOVE -> "ACTION_HOVER_MOVE"
            ACTION_SCROLL -> "ACTION_SCROLL"
            else -> action.toString()
        }
    }
}
