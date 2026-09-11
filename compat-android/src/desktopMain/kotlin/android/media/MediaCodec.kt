package android.media

import java.nio.ByteBuffer

/**
 * android.media.MediaCodec 垫片。桌面版媒体编解码走 P8 的 VLCJ/ffmpeg，此处仅编译形状。——Nova 注
 */
open class MediaCodec {

    open class BufferInfo {
        var flags: Int = 0
        var offset: Int = 0
        var presentationTimeUs: Long = 0
        var size: Int = 0
    }

    companion object {
        const val BUFFER_FLAG_END_OF_STREAM = 4
        const val BUFFER_FLAG_KEY_FRAME = 1
        const val INFO_OUTPUT_FORMAT_CHANGED = -2
        const val INFO_TRY_AGAIN_LATER = -1

        @JvmStatic
        fun createDecoderByType(type: String): MediaCodec = MediaCodec()
        @JvmStatic
        fun createEncoderByType(type: String): MediaCodec = MediaCodec()
    }

    open fun configure(format: MediaFormat?, surface: android.view.Surface?, crypto: Any?, flags: Int) {}
    open fun start() {}
    open fun stop() {}
    open fun release() {}
    open fun dequeueInputBuffer(timeoutUs: Long): Int = -1
    open fun dequeueOutputBuffer(info: BufferInfo, timeoutUs: Long): Int = -1
    open fun getInputBuffer(index: Int): ByteBuffer? = null
    open fun getOutputBuffer(index: Int): ByteBuffer? = null
    open fun queueInputBuffer(index: Int, offset: Int, size: Int, presentationTimeUs: Long, flags: Int) {}
    open fun releaseOutputBuffer(index: Int, render: Boolean) {}
    open fun flush() {}
}
