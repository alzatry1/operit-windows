package android.view

import android.graphics.Bitmap
import android.os.Handler

/**
 * android.view.PixelCopy 的桌面垫片：从 Surface/Window 拷贝像素到 Bitmap。
 * 桌面合成器下由真实 Surface 渲染层接管，这里提供编译形状 + 立即回 SUCCESS。——Nova 注
 */
object PixelCopy {
    const val SUCCESS = 0
    const val ERROR_UNKNOWN = 1
    const val ERROR_TIMEOUT = 2
    const val ERROR_SOURCE_NO_DATA = 3
    const val ERROR_SOURCE_INVALID = 4
    const val ERROR_DESTINATION_INVALID = 5

    fun interface OnPixelCopyFinishedListener {
        fun onPixelCopyFinished(copyResult: Int)
    }

    @JvmStatic
    fun request(surface: Surface, dest: Bitmap, listener: OnPixelCopyFinishedListener, listenerThread: Handler) {
        listener.onPixelCopyFinished(SUCCESS)
    }

    @JvmStatic
    fun request(surface: Surface, srcRect: android.graphics.Rect?, dest: Bitmap, listener: OnPixelCopyFinishedListener, listenerThread: Handler) {
        listener.onPixelCopyFinished(SUCCESS)
    }

    @JvmStatic
    fun request(window: android.view.Window, dest: Bitmap, listener: OnPixelCopyFinishedListener, listenerThread: Handler) {
        listener.onPixelCopyFinished(SUCCESS)
    }

    @JvmStatic
    fun request(window: android.view.Window, srcRect: android.graphics.Rect?, dest: Bitmap, listener: OnPixelCopyFinishedListener, listenerThread: Handler) {
        listener.onPixelCopyFinished(SUCCESS)
    }
}
