package com.google.android.filament.android

/** filament android.UiHelper：SurfaceView 附着与透明度控制 stub。 */
open class UiHelper {
    open fun setOpaque(opaque: Boolean) {}
    open fun setTransparent(transparent: Boolean) {}
    open fun attachTo(view: Any?) {}
    open fun detach() {}
    open fun isOpaque(): Boolean = true
}
