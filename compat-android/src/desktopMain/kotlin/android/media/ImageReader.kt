package android.media

import java.nio.ByteBuffer

/**
 * android.media.ImageReader / Image 编译级 stub。
 * 桌面无 VirtualDisplay 图像回读；acquireLatestImage 返回一块空白 RGBA 缓冲，
 * 使截图调用链可运行（内容为 0 填充）。
 */
open class ImageReader private constructor(
    val width: Int,
    val height: Int,
    val imageFormat: Int,
    val maxImages: Int
) {

    fun interface OnImageAvailableListener {
        fun onImageAvailable(reader: ImageReader)
    }

    open fun acquireLatestImage(): Image? {
        val bytes = ByteBuffer.allocateDirect(width * height * 4)
        return Image(width, height, imageFormat, bytes)
    }

    open fun acquireNextImage(): Image? = acquireLatestImage()

    open fun setOnImageAvailableListener(listener: OnImageAvailableListener?, handler: android.os.Handler?) {}

    open fun close() {}

    /** android.media.Image。 */
    open class Image(
        val width: Int,
        val height: Int,
        val format: Int,
        buffer: ByteBuffer
    ) : AutoCloseable {
        /** android.media.Image.Plane。 */
        open class Plane(
            val buffer: ByteBuffer,
            val pixelStride: Int,
            val rowStride: Int
        )

        private val planeArray: Array<Plane> =
            arrayOf(Plane(buffer, 4, width * 4))

        open val planes: Array<Plane>
            get() = planeArray

        open val timestamp: Long get() = 0L

        override fun close() {}
    }

    companion object {
        @JvmStatic
        fun newInstance(width: Int, height: Int, format: Int, maxImages: Int): ImageReader =
            ImageReader(width, height, format, maxImages)
    }
}
