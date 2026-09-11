package android.widget

import android.content.Context
import android.util.Log

/**
 * android.widget.Toast：桌面版记录日志（后续可接系统通知）。
 */
open class Toast(context: Context?) {
    private var duration = LENGTH_SHORT
    private var text: CharSequence? = null
    private var gravity = 0
    private var xOffset = 0
    private var yOffset = 0
    private var horizontalMargin = 0f
    private var verticalMargin = 0f
    private var view: android.view.View? = null

    open fun show() {
        Log.i("Toast", "show: $text")
    }

    open fun cancel() {}

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        this.gravity = gravity; this.xOffset = xOffset; this.yOffset = yOffset
    }

    fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        this.horizontalMargin = horizontalMargin; this.verticalMargin = verticalMargin
    }

    fun getXOffset(): Int = xOffset
    fun getYOffset(): Int = yOffset
    fun getHorizontalMargin(): Float = horizontalMargin
    fun getVerticalMargin(): Float = verticalMargin

    fun setDuration(duration: Int) { this.duration = duration }
    fun getDuration(): Int = duration

    fun setText(resId: Int) { text = com.ai.assistance.operit.res.Strings.get(resId) }
    fun setText(s: CharSequence) { text = s }

    @Deprecated("deprecated")
    fun setView(view: android.view.View?) { this.view = view }

    @Deprecated("deprecated")
    fun getView(): android.view.View? = view

    companion object {
        const val LENGTH_SHORT = 0
        const val LENGTH_LONG = 1

        @JvmStatic
        fun makeText(context: Context?, text: CharSequence, duration: Int): Toast =
            Toast(context).apply { this.text = text; this.duration = duration }

        @JvmStatic
        fun makeText(context: Context?, resId: Int, duration: Int): Toast =
            Toast(context).apply { this.text = com.ai.assistance.operit.res.Strings.get(resId); this.duration = duration }
    }
}
