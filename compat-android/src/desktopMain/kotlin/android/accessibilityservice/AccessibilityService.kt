package android.accessibilityservice

import android.content.Intent
import android.os.IBinder
import android.view.accessibility.AccessibilityEvent

/**
 * android.accessibilityservice.AccessibilityService（P3-B2 新增，编译级 stub）。
 * 桌面无无障碍框架；常量齐全（GLOBAL_ACTION_*），手势/事件 API 为空实现。
 */
open class AccessibilityService : android.app.Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    open fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    open fun onInterrupt() {}
    open fun onServiceConnected() {}

    open fun performGlobalAction(action: Int): Boolean = false

    open fun getRootInActiveWindow(): android.view.accessibility.AccessibilityNodeInfo? = null
    open fun getWindows(): List<android.view.accessibility.AccessibilityWindowInfo> = emptyList()

    open fun dispatchGesture(
        gesture: GestureDescription?,
        callback: GestureResultCallback?,
        handler: android.os.Handler?,
    ): Boolean = false

    open fun takeScreenshot(displayId: Int, executor: java.util.concurrent.Executor?, callback: Any?) {}

    /** android.accessibilityservice.AccessibilityService.GestureDescription。 */
    class GestureDescription private constructor() {
        class Builder {
            fun addStroke(stroke: StrokeDescription): Builder = this
            fun build(): GestureDescription = GestureDescription()
        }

        class StrokeDescription(
            val path: android.graphics.Path,
            val startTime: Long,
            val duration: Long,
        ) {
            fun continueStroke(path: android.graphics.Path, startTime: Long, duration: Long, willContinue: Boolean): StrokeDescription =
                StrokeDescription(path, startTime, duration)
        }
    }

    /** android.accessibilityservice.AccessibilityService.GestureResultCallback。 */
    abstract class GestureResultCallback {
        open fun onCompleted(gestureDescription: GestureDescription?) {}
        open fun onCancelled(gestureDescription: GestureDescription?) {}
    }

    /** android.accessibilityservice.AccessibilityService.ScreenshotResult。 */
    open class ScreenshotResult {
        val format: Int = 0
        val width: Int = 0
        val height: Int = 0
        val hardwareBuffer: Any? = null
        val colorSpace: Any? = null
        val timestamp: Long = 0
    }

    /** android.accessibilityservice.AccessibilityService.SoftKeyboardController。 */
    open class SoftKeyboardController {
        fun getShowMode(): Int = 0
        fun setShowMode(showMode: Int) {}
    }

    companion object {
        const val GLOBAL_ACTION_BACK = 1
        const val GLOBAL_ACTION_HOME = 2
        const val GLOBAL_ACTION_RECENTS = 3
        const val GLOBAL_ACTION_NOTIFICATIONS = 4
        const val GLOBAL_ACTION_QUICK_SETTINGS = 5
        const val GLOBAL_ACTION_POWER_DIALOG = 6
        const val GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 12
        const val GLOBAL_ACTION_LOCK_SCREEN = 8
        const val GLOBAL_ACTION_TAKE_SCREENSHOT = 9
        const val GLOBAL_ACTION_ACCESSIBILITY_BUTTON = 11
        const val GLOBAL_ACTION_ACCESSIBILITY_BUTTON_CHOOSER = 13
        const val GLOBAL_ACTION_ACCESSIBILITY_ALL_APPS = 14
        const val GLOBAL_ACTION_DISMISS_NOTIFICATION_SHADE = 15
        const val GLOBAL_ACTION_KEYCODE_HEADSETHOOK = 16

        const val SERVICE_INTERFACE = "android.accessibilityservice.AccessibilityService"
        const val SERVICE_META_DATA = "android.accessibilityservice"

        const val GESTURE_2_FINGER_TAP = 1
        const val GESTURE_2_FINGER_DOUBLE_TAP = 2
        const val GESTURE_SWIPE_UP = 100

        const val FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 1
        const val FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4
        const val FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8
        const val FLAG_REQUEST_FILTER_KEY_EVENTS = 32
        const val FLAG_REQUEST_ACCESSIBILITY_BUTTON = 128

        const val ERROR_GESTURE_SERVICE_PROBLEM = 1
    }
}
