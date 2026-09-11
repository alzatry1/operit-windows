package android.view.animation

/**
 * android.view.animation 插值器（P3-B2 新增）。
 * DecelerateInterpolator 为真实减速曲线（factor 可配）。
 */

/** android.view.animation.Interpolator。 */
interface Interpolator {
    fun getInterpolation(input: Float): Float
}

/** android.view.animation.DecelerateInterpolator。 */
open class DecelerateInterpolator(private val factor: Float = 1.0f) : Interpolator {
    constructor() : this(1.0f)

    override fun getInterpolation(input: Float): Float {
        return if (factor == 1.0f) {
            1.0f - (1.0f - input) * (1.0f - input)
        } else {
            1.0f - Math.pow((1.0f - input).toDouble(), (2.0 * factor)).toFloat()
        }
    }
}

/** android.view.animation.AccelerateInterpolator。 */
open class AccelerateInterpolator(private val factor: Float = 1.0f) : Interpolator {
    constructor() : this(1.0f)

    override fun getInterpolation(input: Float): Float =
        if (factor == 1.0f) input * input
        else Math.pow(input.toDouble(), (2.0 * factor)).toFloat()
}

/** android.view.animation.LinearInterpolator。 */
open class LinearInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float = input
}

/** android.view.animation.AccelerateDecelerateInterpolator。 */
open class AccelerateDecelerateInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float =
        (Math.cos(((input + 1) * Math.PI)) / 2.0).toFloat() + 0.5f
}

/** android.view.animation.OvershootInterpolator。 */
open class OvershootInterpolator(private val tension: Float = 2.0f) : Interpolator {
    constructor() : this(2.0f)

    override fun getInterpolation(input: Float): Float {
        val t = input - 1.0f
        return t * t * ((tension + 1) * t + tension) + 1.0f
    }
}
