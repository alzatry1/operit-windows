package androidx.core.view

import android.view.View
import java.util.concurrent.atomic.AtomicInteger

/**
 * androidx.core.view.ViewCompat 桌面版。
 * View 是轻 stub，多数操作退化为属性赋值或 no-op。
 */
object ViewCompat {

    private val nextGeneratedId = AtomicInteger(1)

    fun interface OnApplyWindowInsetsListener {
        fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat
    }

    @JvmStatic
    fun postOnAnimation(view: View, runnable: Runnable) {
        view.post(runnable)
    }

    @JvmStatic
    fun postOnAnimationDelayed(view: View, runnable: Runnable, delayMillis: Long) {
        view.postDelayed(runnable, delayMillis)
    }

    @JvmStatic
    fun setOnApplyWindowInsetsListener(view: View, listener: OnApplyWindowInsetsListener?) {
        view.setTag(TAG_ON_APPLY_WINDOW_INSETS_LISTENER, listener)
    }

    @JvmStatic
    fun getOnApplyWindowInsetsListener(view: View): OnApplyWindowInsetsListener? =
        view.getTag(TAG_ON_APPLY_WINDOW_INSETS_LISTENER) as? OnApplyWindowInsetsListener

    @JvmStatic
    fun isAttachedToWindow(view: View): Boolean = true

    @JvmStatic
    fun canScrollVertically(view: View, direction: Int): Boolean = false

    @JvmStatic
    fun canScrollHorizontally(view: View, direction: Int): Boolean = false

    @JvmStatic
    fun setElevation(view: View, elevation: Float) {
        view.elevation = elevation
    }

    @JvmStatic
    fun getElevation(view: View): Float = view.elevation

    @JvmStatic
    fun setTranslationZ(view: View, translationZ: Float) {
        view.setTag(TAG_TRANSLATION_Z, translationZ)
    }

    @JvmStatic
    fun getTranslationZ(view: View): Float = (view.getTag(TAG_TRANSLATION_Z) as? Float) ?: 0f

    @JvmStatic
    fun generateViewId(): Int = nextGeneratedId.incrementAndGet()

    @JvmStatic
    fun getMinimumWidth(view: View): Int = view.minimumWidth

    @JvmStatic
    fun getMinimumHeight(view: View): Int = view.minimumHeight

    @JvmStatic
    fun requestApplyInsets(view: View) {}

    @JvmStatic
    fun setImportantForAccessibility(view: View, mode: Int) {
        view.importantForAccessibility = mode
    }

    @JvmStatic
    fun getImportantForAccessibility(view: View): Int = view.importantForAccessibility

    @JvmStatic
    fun setBackground(view: View, background: android.graphics.drawable.Drawable?) {}

    @JvmStatic
    fun getRootWindowInsets(view: View): WindowInsetsCompat? = null

    @JvmStatic
    fun computeSystemWindowInsets(view: View, insets: WindowInsetsCompat, outLocalInsets: android.graphics.Rect): WindowInsetsCompat = insets

    @JvmStatic
    fun setAlpha(view: View, alpha: Float) {
        view.alpha = alpha
    }

    @JvmStatic
    fun getAlpha(view: View): Float = view.alpha

    @JvmStatic
    fun setVisibility(view: View, visibility: Int) {
        view.visibility = visibility
    }

    @JvmStatic
    fun getVisibility(view: View): Int = view.visibility

    private const val TAG_ON_APPLY_WINDOW_INSETS_LISTENER = 0x7f0f0001
    private const val TAG_TRANSLATION_Z = 0x7f0f0002
}
