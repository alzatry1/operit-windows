package android.view

import android.content.Context
import android.util.AttributeSet

/**
 * android.view 容器体系（P3-B2 新增）。
 * ViewGroup 内存维护子 View 列表；FrameLayout/LinearLayout/ScrollView 为编译级 stub。
 */

/** android.view.ViewParent。 */
interface ViewParent {
    fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean)
    fun requestLayout() {}
    fun isLayoutRequested(): Boolean = false
    fun requestChildFocus(child: View?, focused: View?) {}
    fun clearChildFocus(child: View?) {}
    fun focusableViewAvailable(v: View?) {}
    fun bringChildToFront(child: View?) {}
    fun requestSendAccessibilityEvent(child: View?, event: android.view.accessibility.AccessibilityEvent?): Boolean = false
}

/** android.view.ViewGroup。 */
open class ViewGroup : View, ViewParent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val children = mutableListOf<View>()

    /** ViewGroup.requestSendAccessibilityEvent：桌面无无障碍分发，返回 false。 */
    override fun requestSendAccessibilityEvent(child: View?, event: android.view.accessibility.AccessibilityEvent?): Boolean = false
    open fun onRequestSendAccessibilityEvent(child: View?, event: android.view.accessibility.AccessibilityEvent?): Boolean = false

    open fun addView(child: View?) {
        if (child != null) children.add(child)
    }

    open fun addView(child: View?, index: Int) {
        if (child != null) children.add(index.coerceIn(0, children.size), child)
    }

    open fun addView(child: View?, params: LayoutParams?) {
        child?.layoutParams = params
        addView(child)
    }

    open fun addView(child: View?, width: Int, height: Int) {
        child?.layoutParams = LayoutParams(width, height)
        addView(child)
    }

    open fun addView(child: View?, index: Int, params: LayoutParams?) {
        child?.layoutParams = params
        addView(child, index)
    }

    open fun removeView(view: View?) {
        children.remove(view)
    }

    open fun removeViewAt(index: Int) {
        if (index in children.indices) children.removeAt(index)
    }

    open fun removeAllViews() {
        children.clear()
    }

    open fun getChildAt(index: Int): View? = children.getOrNull(index)

    open val childCount: Int get() = children.size

    open fun indexOfChild(child: View?): Int = children.indexOf(child)

    open fun childCountFromField(): Int = children.size

    open fun onInterceptTouchEvent(event: MotionEvent): Boolean = false

    open fun childDrawableStateChanged(child: View?) {}

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun requestLayout() {}

    /** android.view.ViewGroup.LayoutParams。 */
    open class LayoutParams {
        var width: Int
        var height: Int

        constructor(width: Int, height: Int) {
            this.width = width
            this.height = height
        }

        constructor(context: Context?, attrs: AttributeSet?) {
            this.width = WRAP_CONTENT
            this.height = WRAP_CONTENT
        }

        constructor(source: LayoutParams?) {
            this.width = source?.width ?: WRAP_CONTENT
            this.height = source?.height ?: WRAP_CONTENT
        }

        companion object {
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2

            @Deprecated("deprecated")
            const val FILL_PARENT = -1
        }
    }

    /** android.view.ViewGroup.MarginLayoutParams。 */
    open class MarginLayoutParams : LayoutParams {
        var leftMargin: Int = 0
        var topMargin: Int = 0
        var rightMargin: Int = 0
        var bottomMargin: Int = 0
        var marginStart: Int = 0
        var marginEnd: Int = 0

        constructor(width: Int, height: Int) : super(width, height)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(source: LayoutParams?) : super(source)
        constructor(source: MarginLayoutParams?) : super(source) {
            if (source != null) {
                leftMargin = source.leftMargin
                topMargin = source.topMargin
                rightMargin = source.rightMargin
                bottomMargin = source.bottomMargin
                marginStart = source.marginStart
                marginEnd = source.marginEnd
            }
        }

        open fun setMargins(left: Int, top: Int, right: Int, bottom: Int) {
            leftMargin = left
            topMargin = top
            rightMargin = right
            bottomMargin = bottom
        }

        open fun resolveLayoutDirection(layoutDirection: Int) {}
    }
}

/** android.widget.FrameLayout 的基类放在 widget 包；这里仅 ViewGroup 层级。 */
