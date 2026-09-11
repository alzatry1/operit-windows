package android.media.projection

import android.content.Intent
import android.os.Handler

/**
 * android.media.projection 编译级 stub（P3-B4）。
 * 桌面端录屏改走 JVM 机器人/原生截屏方案（后续阶段实装），
 * 此处仅保证调用链可编译，方法为空操作。
 */
open class MediaProjection {

    /** MediaProjection.Callback。 */
    abstract class Callback {
        abstract fun onStop()
    }

    open fun registerCallback(callback: Callback, handler: Handler?) {}

    open fun unregisterCallback(callback: Callback) {}

    open fun createVirtualDisplay(
        name: String?,
        width: Int,
        height: Int,
        dpi: Int,
        flags: Int,
        surface: android.view.Surface?,
        callbacks: android.hardware.display.VirtualDisplay.Callback?,
        handler: Handler?
    ): android.hardware.display.VirtualDisplay? = null

    open fun stop() {}
}

/** MediaProjectionManager。 */
open class MediaProjectionManager {
    open fun createScreenCaptureIntent(): Intent = Intent()

    open fun getMediaProjection(resultCode: Int, data: Intent?): MediaProjection? = null
}
