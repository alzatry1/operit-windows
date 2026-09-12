package android.animation

/** android.animation.Animator 基类 stub。 */
open class Animator

/**
 * android.animation.ValueAnimator 编译级 stub（P3-B4）。
 * 桌面悬浮窗动画改由 Compose 承担；此类仅保证调用链可编译，
 * start() 立即置终值并触发监听器一次（语义等价于零时长动画）。
 */
open class ValueAnimator : Animator {

    constructor()

    fun interface AnimatorUpdateListener {
        fun onAnimationUpdate(animation: ValueAnimator)
    }

    interface AnimatorListener {
        fun onAnimationStart(animation: Animator) {}
        fun onAnimationEnd(animation: Animator) {}
        fun onAnimationCancel(animation: Animator) {}
        fun onAnimationRepeat(animation: Animator) {}
    }

    var duration: Long = 300
    var repeatCount: Int = 0
    var interpolator: Any? = null
    var animatedValue: Any? = null
        private set
    /** ValueAnimator.animatedFraction（动画进度 0-1；桌面占位）。——Nova 注 */
    open var animatedFraction: Float = 0f

    private val updateListeners = mutableListOf<AnimatorUpdateListener>()
    private val listeners = mutableListOf<AnimatorListener>()
    private var values: FloatArray = floatArrayOf()

    open fun setFloatValues(vararg values: Float) {
        this.values = values
    }

    open fun setIntValues(vararg values: Int) {
        this.values = values.map { it.toFloat() }.toFloatArray()
    }

    open fun addUpdateListener(listener: AnimatorUpdateListener?) {
        if (listener != null) updateListeners.add(listener)
    }

    open fun removeUpdateListener(listener: AnimatorUpdateListener?) {
        updateListeners.remove(listener)
    }

    open fun removeAllUpdateListeners() = updateListeners.clear()

    open fun addListener(listener: AnimatorListener?) {
        if (listener != null) listeners.add(listener)
    }

    open fun removeListener(listener: AnimatorListener?) {
        listeners.remove(listener)
    }

    open fun removeAllListeners() = listeners.clear()

    open fun start() {
        animatedValue = values.lastOrNull()
        updateListeners.toList().forEach { it.onAnimationUpdate(this) }
        listeners.toList().forEach { it.onAnimationStart(this); it.onAnimationEnd(this) }
    }

    open fun cancel() {
        listeners.toList().forEach { it.onAnimationCancel(this) }
    }

    open fun setDuration(duration: Long): ValueAnimator {
        this.duration = duration
        return this
    }

    open fun isRunning(): Boolean = false

    companion object {
        @JvmStatic
        fun ofFloat(vararg values: Float): ValueAnimator = ValueAnimator().apply { setFloatValues(*values) }

        @JvmStatic
        fun ofInt(vararg values: Int): ValueAnimator = ValueAnimator().apply { setIntValues(*values) }

        @JvmStatic
        fun ofArgb(vararg values: Int): ValueAnimator = ValueAnimator().apply { setIntValues(*values) }
    }
}

/** android.animation.AnimatorListenerAdapter。 */
abstract class AnimatorListenerAdapter : ValueAnimator.AnimatorListener

/** android.animation.ObjectAnimator 轻 stub。 */
open class ObjectAnimator private constructor() : ValueAnimator() {
    companion object {
        @JvmStatic
        fun ofFloat(target: Any?, propertyName: String?, vararg values: Float): ObjectAnimator =
            ObjectAnimator()

        @JvmStatic
        fun ofInt(target: Any?, propertyName: String?, vararg values: Int): ObjectAnimator =
            ObjectAnimator()
    }
}
