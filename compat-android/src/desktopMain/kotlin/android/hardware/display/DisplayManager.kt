package android.hardware.display

import android.content.Context
import android.view.Surface

/**
 * android.hardware.display.DisplayManager 垫片。桌面无虚拟显示，常量+空实现。——Nova 注
 */
open class DisplayManager {
    fun getDisplays(): Array<android.view.Display> = emptyArray()
    fun getDisplay(id: Int): android.view.Display? = null
    fun registerDisplayListener(listener: DisplayListener?, handler: android.os.Handler?) {}
    fun unregisterDisplayListener(listener: DisplayListener) {}
    fun createVirtualDisplay(
        name: String, width: Int, height: Int, densityDpi: Int,
        surface: Surface?, flags: Int,
    ): android.hardware.display.VirtualDisplay? = null

    interface DisplayListener {
        fun onDisplayAdded(displayId: Int) {}
        fun onDisplayRemoved(displayId: Int) {}
        fun onDisplayChanged(displayId: Int) {}
    }

    companion object {
        const val VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR = 1 shl 0
        const val VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY = 1 shl 3
        const val VIRTUAL_DISPLAY_FLAG_PRESENTATION = 1 shl 1
        const val VIRTUAL_DISPLAY_FLAG_PUBLIC = 1 shl 2
        const val DEFAULT_DISPLAY = 0
    }
}
