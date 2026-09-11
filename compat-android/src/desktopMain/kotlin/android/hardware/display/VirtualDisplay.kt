package android.hardware.display

/**
 * android.hardware.display.VirtualDisplay 编译级 stub（P3-B4）。
 * 桌面无虚拟显示；与 MediaProjection stub 配套。
 */
open class VirtualDisplay {

    abstract class Callback {
        abstract fun onPaused()
        abstract fun onResumed()
        abstract fun onStopped()
    }

    val display: android.view.Display? get() = null
    val surface: android.view.Surface? get() = null

    open fun resize(width: Int, height: Int, densityDpi: Int) {}
    open fun setSurface(surface: android.view.Surface?) {}
    open fun release() {}
}
