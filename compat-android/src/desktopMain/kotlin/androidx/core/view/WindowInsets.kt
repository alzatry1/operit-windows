package androidx.core.view

import androidx.core.graphics.Insets

/**
 * androidx.core.view.WindowInsetsCompat 桌面版。
 * 桌面无系统栏；所有 insets 返回全 0。
 */
class WindowInsetsCompat private constructor(
    private val insetsMap: Map<Int, Insets>,
) {

    /** Type 常量（与 Android 位掩码一致）。 */
    object Type {
        const val STATUS_BARS = 1
        const val NAVIGATION_BARS = 2
        const val CAPTION_BAR = 4
        const val IME = 8
        const val SYSTEM_GESTURES = 16
        const val MANDATORY_SYSTEM_GESTURES = 32
        const val TAPPABLE_ELEMENT = 64
        const val DISPLAY_CUTOUT = 128

        @JvmStatic fun statusBars(): Int = STATUS_BARS
        @JvmStatic fun navigationBars(): Int = NAVIGATION_BARS
        @JvmStatic fun captionBar(): Int = CAPTION_BAR
        @JvmStatic fun ime(): Int = IME
        @JvmStatic fun systemGestures(): Int = SYSTEM_GESTURES
        @JvmStatic fun mandatorySystemGestures(): Int = MANDATORY_SYSTEM_GESTURES
        @JvmStatic fun tappableElement(): Int = TAPPABLE_ELEMENT
        @JvmStatic fun displayCutout(): Int = DISPLAY_CUTOUT
        @JvmStatic fun systemBars(): Int = STATUS_BARS or NAVIGATION_BARS or CAPTION_BAR
        @JvmStatic fun all(): Int = 0x1FF
    }

    fun getInsets(type: Int): Insets = insetsMap[type] ?: Insets.NONE
    fun getInsetsIgnoringVisibility(type: Int): Insets = insetsMap[type] ?: Insets.NONE
    fun isVisible(type: Int): Boolean = false
    fun hasSystemWindowInsets(): Boolean = insetsMap.values.any { it != Insets.NONE }

    class Builder() {
        private val map = HashMap<Int, Insets>()

        constructor(insets: WindowInsetsCompat) : this() {
            map.putAll(insets.insetsMap)
        }

        fun setInsets(type: Int, insets: Insets): Builder = apply { map[type] = insets }
        fun setVisible(type: Int, visible: Boolean): Builder = this
        fun setSystemWindowInsets(insets: Insets): Builder = apply { map[Type.STATUS_BARS] = insets }
        fun setSystemGestureInsets(insets: Insets): Builder = this
        fun setStableInsets(insets: Insets): Builder = this
        fun setDisplayCutout(cutout: Any?): Builder = this
        fun setTappableElementInsets(insets: Insets): Builder = this
        fun setMandatorySystemGestureInsets(insets: Insets): Builder = this
        fun build(): WindowInsetsCompat = WindowInsetsCompat(map)
    }

    companion object {
        @JvmField val CONSUMED = WindowInsetsCompat(emptyMap())

        @JvmStatic
        fun toWindowInsetsCompat(insets: Any?): WindowInsetsCompat = CONSUMED
    }
}

/**
 * androidx.core.view.WindowInsetsControllerCompat 桌面版。
 * 桌面无系统栏；show/hide 记日志。
 */
class WindowInsetsControllerCompat(
    private val window: android.view.Window?,
    private val view: android.view.View?,
) {

    var systemBarsBehavior: Int = BEHAVIOR_SHOW_BARS_BY_TOUCH

    fun show(types: Int) {
        android.util.Log.d("WindowInsetsController", "show($types) no-op")
    }

    fun hide(types: Int) {
        android.util.Log.d("WindowInsetsController", "hide($types) no-op")
    }

    fun isVisible(types: Int): Boolean = true

    fun controlWindowInsetsAnimation(types: Int, durationMillis: Long, interpolator: Any?, cancellationSignal: Any?, listener: Any?) {}

    fun addOnControllableInsetsChangedListener(listener: OnControllableInsetsChangedListener) {}
    fun removeOnControllableInsetsChangedListener(listener: OnControllableInsetsChangedListener) {}

    fun interface OnControllableInsetsChangedListener {
        fun onControllableInsetsChanged(controller: WindowInsetsControllerCompat, typeMask: Int)
    }

    companion object {
        const val BEHAVIOR_SHOW_BARS_BY_TOUCH = 0
        const val BEHAVIOR_SHOW_BARS_BY_SWIPE = 1
        const val BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE = 2
    }
}
