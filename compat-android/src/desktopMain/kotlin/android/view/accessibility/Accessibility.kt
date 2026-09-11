package android.view.accessibility

import android.graphics.Rect
import android.os.Bundle

/**
 * android.view.accessibility 垫片（P3-B2 新增）。
 * 桌面无无障碍服务；AccessibilityNodeInfo 为全 API 形状 stub（自动化功能编译核心）。
 * Kotlin 属性风格：getX() 无参方法以属性声明（app 按合成属性访问）。
 */

/** android.view.accessibility.AccessibilityEvent。 */
open class AccessibilityEvent {
    var eventType: Int = TYPE_INVALID
    var action: Int = 0
    var packageName: CharSequence? = null
    var className: CharSequence? = null
    var contentDescription: CharSequence? = null
    var isEnabled: Boolean = false
    var isPassword: Boolean = false
    var isChecked: Boolean = false
    var isFullScreen: Boolean = false
    var isScrollable: Boolean = false
    var isImportantForAccessibility: Boolean = true
    var itemCount: Int = -1
    var currentItemIndex: Int = -1
    var fromIndex: Int = -1
    var toIndex: Int = -1
    var scrollX: Int = 0
    var scrollY: Int = 0
    var maxScrollX: Int = 0
    var maxScrollY: Int = 0
    var addedCount: Int = 0
    var removedCount: Int = 0
    var movementGranularity: Int = 0
    var displayId: Int = 0
    var windowChanges: Int = 0
    var eventTime: Long = 0
    var windowId: Int = -1
    var beforeText: CharSequence? = null
    var textChangeSource: CharSequence? = null

    val text: MutableList<CharSequence> = mutableListOf()
    val recordCount: Int get() = 0

    open fun setText(newText: CharSequence?) {
        text.clear()
        if (newText != null) text.add(newText)
    }

    open fun getRecord(index: Int): Any? = null

    open fun setSource(source: android.view.View?) {}
    open fun setSource(source: android.view.View?, virtualDescendantId: Int) {}

    companion object {
        const val TYPE_INVALID = -1
        const val TYPE_VIEW_CLICKED = 0x1
        const val TYPE_VIEW_LONG_CLICKED = 0x2
        const val TYPE_VIEW_SELECTED = 0x4
        const val TYPE_VIEW_FOCUSED = 0x8
        const val TYPE_VIEW_TEXT_CHANGED = 0x10
        const val TYPE_WINDOW_STATE_CHANGED = 0x20
        const val TYPE_NOTIFICATION_STATE_CHANGED = 0x40
        const val TYPE_VIEW_HOVER_ENTER = 0x80
        const val TYPE_VIEW_HOVER_EXIT = 0x100
        const val TYPE_TOUCH_EXPLORATION_GESTURE_START = 0x200
        const val TYPE_TOUCH_EXPLORATION_GESTURE_END = 0x400
        const val TYPE_WINDOW_CONTENT_CHANGED = 0x800
        const val TYPE_VIEW_SCROLLED = 0x1000
        const val TYPE_VIEW_TEXT_SELECTION_CHANGED = 0x2000
        const val TYPE_ANNOUNCEMENT = 0x4000
        const val TYPE_VIEW_ACCESSIBILITY_FOCUSED = 0x8000
        const val TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 0x10000
        const val TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY_BOUNDARY = 0x20000
        const val TYPE_GESTURE_DETECTION_START = 0x40000
        const val TYPE_GESTURE_DETECTION_END = 0x80000
        const val TYPE_TOUCH_INTERACTION_START = 0x100000
        const val TYPE_TOUCH_INTERACTION_END = 0x200000
        const val TYPE_WINDOWS_CHANGED = 0x400000
        const val TYPE_VIEW_CONTEXT_CLICKED = 0x800000

        const val CONTENT_CHANGE_TYPE_UNDEFINED = 0
        const val CONTENT_CHANGE_TYPE_SUBTREE = 1
        const val CONTENT_CHANGE_TYPE_TEXT = 2
        const val CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4
        const val CONTENT_CHANGE_TYPE_STATE_DESCRIPTION = 64

        const val WINDOWS_CHANGE_ADDED = 1
        const val WINDOWS_CHANGE_REMOVED = 2
        const val WINDOWS_CHANGE_TITLE = 4
        const val WINDOWS_CHANGE_ACTIVE = 32

        @JvmStatic
        fun obtain(): AccessibilityEvent = AccessibilityEvent()

        @JvmStatic
        fun obtain(eventType: Int): AccessibilityEvent = AccessibilityEvent().apply { this.eventType = eventType }

        @JvmStatic
        fun obtain(event: AccessibilityEvent?): AccessibilityEvent = AccessibilityEvent()
    }
}

/** android.view.accessibility.AccessibilityNodeInfo：全 API 形状 stub。 */
open class AccessibilityNodeInfo {

    var text: CharSequence? = null
    var className: CharSequence? = null
    var packageName: CharSequence? = null
    var contentDescription: CharSequence? = null
    var stateDescription: CharSequence? = null
    var viewIdResourceName: String? = null
    var uniqueId: String? = null
    var error: CharSequence? = null
    var hintText: CharSequence? = null
    var tooltipText: CharSequence? = null
    var paneTitle: CharSequence? = null
    var maxTextLength: Int = -1
    var textSelectionStart: Int = -1
    var textSelectionEnd: Int = -1
    var inputType: Int = 0
    var liveRegion: Int = 0
    var drawingOrder: Int = 0
    var movementGranularities: Int = 0

    var isClickable: Boolean = false
    var isLongClickable: Boolean = false
    var isEnabled: Boolean = true
    var isFocusable: Boolean = false
    var isFocused: Boolean = false
    var isScrollable: Boolean = false
    var isCheckable: Boolean = false
    var isChecked: Boolean = false
    var isSelected: Boolean = false
    var isVisibleToUser: Boolean = true
    var isAccessibilityFocused: Boolean = false
    var isPassword: Boolean = false
    var isHeading: Boolean = false
    var isImportantForAccessibility: Boolean = true
    var isEditable: Boolean = false
    var isDismissable: Boolean = false
    var isContextClickable: Boolean = false
    var isContentInvalid: Boolean = false
    var isShowingHintText: Boolean = false
    var isTextEntryKey: Boolean = false
    var isMultiLine: Boolean = false
    var isGranularScrollingSupported: Boolean = false

    val childCount: Int = 0
    val actions: Int = 0
    val windowId: Int = -1
    val actionList: List<AccessibilityAction> = emptyList()
    val extras: Bundle = Bundle()
    val collectionInfo: CollectionInfo? = null
    val collectionItemInfo: CollectionItemInfo? = null
    val rangeInfo: RangeInfo? = null
    val window: AccessibilityWindowInfo? = null
    val parent: AccessibilityNodeInfo? = null
    val boundsInParent: Rect = Rect()
    val availableExtraData: List<String> = emptyList()

    open fun getBoundsInScreen(outBounds: Rect) {}
    open fun setBoundsInScreen(bounds: Rect?) {}
    open fun getBoundsInParent(outBounds: Rect) {}
    open fun setBoundsInParent(bounds: Rect?) {}
    open fun getBoundsInWindow(outBounds: Rect) {}

    open fun getChild(index: Int): AccessibilityNodeInfo? = null
    open fun addChild(child: android.view.View?) {}
    open fun addChild(child: android.view.View?, virtualDescendantId: Int) {}
    open fun removeChild(child: android.view.View?): Boolean = false

    open fun performAction(action: Int): Boolean = false
    open fun performAction(action: Int, arguments: Bundle?): Boolean = false
    open fun addAction(action: Int) {}
    open fun addAction(action: AccessibilityAction?) {}
    open fun removeAction(action: AccessibilityAction?): Boolean = false
    open fun removeAction(action: Int): Boolean = false

    open fun findAccessibilityNodeInfosByText(text: String?): List<AccessibilityNodeInfo> = emptyList()
    open fun findAccessibilityNodeInfosByViewId(viewId: String?): List<AccessibilityNodeInfo> = emptyList()
    open fun findFocus(focus: Int): AccessibilityNodeInfo? = null
    open fun focusSearch(direction: Int): AccessibilityNodeInfo? = null
    open fun findAccessibilityNodeInfosByRenderingProcess(processName: String?): List<AccessibilityNodeInfo> = emptyList()

    open fun refresh(): Boolean = false
    open fun recycle() {}
    open fun setSource(source: android.view.View?) {}
    open fun setSource(source: android.view.View?, virtualDescendantId: Int) {}
    open fun setTraversalBefore(view: android.view.View?) {}
    open fun setTraversalAfter(view: android.view.View?) {}
    open fun setQueryFromAppProcessEnabled(view: android.view.View?, enabled: Boolean) {}
    open fun setAvailableExtraData(availableExtraData: List<String>?) {}

    /** android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction。 */
    class AccessibilityAction(val id: Int, val label: CharSequence?) {
        companion object {
            @JvmField val ACTION_CLICK = AccessibilityAction(16, null)
            @JvmField val ACTION_LONG_CLICK = AccessibilityAction(32, null)
            @JvmField val ACTION_FOCUS = AccessibilityAction(1, null)
            @JvmField val ACTION_CLEAR_FOCUS = AccessibilityAction(2, null)
            @JvmField val ACTION_SELECT = AccessibilityAction(4, null)
            @JvmField val ACTION_CLEAR_SELECTION = AccessibilityAction(8, null)
            @JvmField val ACTION_ACCESSIBILITY_FOCUS = AccessibilityAction(64, null)
            @JvmField val ACTION_CLEAR_ACCESSIBILITY_FOCUS = AccessibilityAction(128, null)
            @JvmField val ACTION_SCROLL_FORWARD = AccessibilityAction(4096, null)
            @JvmField val ACTION_SCROLL_BACKWARD = AccessibilityAction(8192, null)
            @JvmField val ACTION_SCROLL_UP = AccessibilityAction(0x00010000, null)
            @JvmField val ACTION_SCROLL_DOWN = AccessibilityAction(0x00020000, null)
            @JvmField val ACTION_SCROLL_LEFT = AccessibilityAction(0x00040000, null)
            @JvmField val ACTION_SCROLL_RIGHT = AccessibilityAction(0x00080000, null)
            @JvmField val ACTION_COPY = AccessibilityAction(0x00004000, null)
            @JvmField val ACTION_PASTE = AccessibilityAction(0x00008000, null)
            @JvmField val ACTION_CUT = AccessibilityAction(0x00010000, null)
            @JvmField val ACTION_SET_SELECTION = AccessibilityAction(0x00002000, null)
            @JvmField val ACTION_EXPAND = AccessibilityAction(0x00080000, null)
            @JvmField val ACTION_COLLAPSE = AccessibilityAction(0x00100000, null)
            @JvmField val ACTION_DISMISS = AccessibilityAction(0x00200000, null)
            @JvmField val ACTION_SET_TEXT = AccessibilityAction(0x00200000, null)
            @JvmField val ACTION_SHOW_ON_SCREEN = AccessibilityAction(0x01000000, null)
            @JvmField val ACTION_SCROLL_TO_POSITION = AccessibilityAction(0x02000000, null)
            @JvmField val ACTION_SET_PROGRESS = AccessibilityAction(0x04000000, null)
        }
    }

    /** android.view.accessibility.AccessibilityNodeInfo.CollectionInfo。 */
    open class CollectionInfo(
        val rowCount: Int = 0,
        val columnCount: Int = 0,
        val isHierarchical: Boolean = false,
    ) {
        companion object {
            @JvmStatic
            fun obtain(rowCount: Int, columnCount: Int, hierarchical: Boolean): CollectionInfo =
                CollectionInfo(rowCount, columnCount, hierarchical)
        }
    }

    /** android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo。 */
    open class CollectionItemInfo(
        val rowIndex: Int = 0,
        val rowSpan: Int = 0,
        val columnIndex: Int = 0,
        val columnSpan: Int = 0,
        val isHeading: Boolean = false,
    ) {
        companion object {
            @JvmStatic
            fun obtain(rowIndex: Int, rowSpan: Int, columnIndex: Int, columnSpan: Int, heading: Boolean): CollectionItemInfo =
                CollectionItemInfo(rowIndex, rowSpan, columnIndex, columnSpan, heading)
        }
    }

    /** android.view.accessibility.AccessibilityNodeInfo.RangeInfo。 */
    open class RangeInfo(
        val type: Int = 0,
        val min: Float = 0f,
        val max: Float = 0f,
        val current: Float = 0f,
    ) {
        companion object {
            const val RANGE_TYPE_INT = 0
            const val RANGE_TYPE_FLOAT = 1
            const val RANGE_TYPE_PERCENT = 2

            @JvmStatic
            fun obtain(type: Int, min: Float, max: Float, current: Float): RangeInfo = RangeInfo(type, min, max, current)
        }
    }

    companion object {
        const val ACTION_CLICK = 0x00000010
        const val ACTION_LONG_CLICK = 0x00000020
        const val ACTION_FOCUS = 0x00000001
        const val ACTION_CLEAR_FOCUS = 0x00000002
        const val ACTION_SELECT = 0x00000004
        const val ACTION_CLEAR_SELECTION = 0x00000008
        const val ACTION_ACCESSIBILITY_FOCUS = 0x00000040
        const val ACTION_CLEAR_ACCESSIBILITY_FOCUS = 0x00000080
        const val ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 0x00000100
        const val ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 0x00000200
        const val ACTION_NEXT_HTML_ELEMENT = 0x00000400
        const val ACTION_PREVIOUS_HTML_ELEMENT = 0x00000800
        const val ACTION_SCROLL_FORWARD = 0x00001000
        const val ACTION_SCROLL_BACKWARD = 0x00002000
        const val ACTION_COPY = 0x00004000
        const val ACTION_PASTE = 0x00008000
        const val ACTION_CUT = 0x00010000
        const val ACTION_SET_SELECTION = 0x00020000
        const val ACTION_EXPAND = 0x00040000
        const val ACTION_COLLAPSE = 0x00080000
        const val ACTION_DISMISS = 0x00100000
        const val ACTION_SET_TEXT = 0x00200000
        const val ACTION_SHOW_ON_SCREEN = 0x01000000
        const val ACTION_SCROLL_TO_POSITION = 0x02000000
        const val ACTION_SET_PROGRESS = 0x04000000

        const val FOCUS_INPUT = 1
        const val FOCUS_ACCESSIBILITY = 2

        const val FLAG_PREFETCH = 1

        @JvmStatic
        fun obtain(): AccessibilityNodeInfo = AccessibilityNodeInfo()

        @JvmStatic
        fun obtain(source: android.view.View?): AccessibilityNodeInfo = AccessibilityNodeInfo()

        @JvmStatic
        fun obtain(source: android.view.View?, virtualDescendantId: Int): AccessibilityNodeInfo =
            AccessibilityNodeInfo()

        @JvmStatic
        fun obtain(root: AccessibilityNodeInfo?): AccessibilityNodeInfo = AccessibilityNodeInfo()
    }
}

/** android.view.accessibility.AccessibilityNodeProvider。 */
open class AccessibilityNodeProvider {
    open fun createAccessibilityNodeInfo(virtualViewId: Int): AccessibilityNodeInfo? = null
    open fun performAction(virtualViewId: Int, action: Int, arguments: Bundle?): Boolean = false
    open fun findFocus(focus: Int): AccessibilityNodeInfo? = null
    open fun findAccessibilityNodeInfosByText(
        text: String?,
        virtualViewId: Int
    ): List<AccessibilityNodeInfo>? = null

    companion object {
        const val HOST_VIEW_ID = -1
    }
}

/** android.view.accessibility.AccessibilityManager。桌面无系统无障碍服务，全部安全默认值。 */
open class AccessibilityManager {
    open val isEnabled: Boolean get() = false
    open val isTouchExplorationEnabled: Boolean get() = false
    open val isHighTextContrastEnabled: Boolean get() = false
    open fun interrupt() {}
    open fun sendAccessibilityEvent(event: AccessibilityEvent) {}
    open fun addAccessibilityStateChangeListener(listener: Any?): Boolean = false
    open fun removeAccessibilityStateChangeListener(listener: Any?): Boolean = false
}

/** android.view.accessibility.AccessibilityWindowInfo。 */
open class AccessibilityWindowInfo {
    open val id: Int = -1
    open val type: Int = 0
    open val layer: Int = 0
    open val isActive: Boolean = false
    open val isFocused: Boolean = false
    open val isAccessibilityFocused: Boolean = false
    open val title: CharSequence? = null
    open val root: AccessibilityNodeInfo? = null
    open val parent: AccessibilityWindowInfo? = null
    open val childCount: Int = 0

    open fun getBoundsInScreen(outBounds: Rect) {}
    open fun getChild(index: Int): AccessibilityWindowInfo? = null
    open fun recycle() {}

    companion object {
        const val TYPE_APPLICATION = 1
        const val TYPE_INPUT_METHOD = 2
        const val TYPE_SPLIT_SCREEN_DIVIDER = 3
        const val TYPE_SYSTEM = 4
        const val TYPE_ACCESSIBILITY_OVERLAY = 5

        @JvmStatic
        fun obtain(): AccessibilityWindowInfo = AccessibilityWindowInfo()
    }
}
