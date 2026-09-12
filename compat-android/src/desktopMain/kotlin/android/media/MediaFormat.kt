package android.media

/**
 * android.media.MediaFormat 垫片（键值容器）。——Nova 注
 */
open class MediaFormat {
    private val map = HashMap<String, Any?>()

    open fun setInteger(name: String, value: Int) { map[name] = value }
    open fun setLong(name: String, value: Long) { map[name] = value }
    open fun setFloat(name: String, value: Float) { map[name] = value }
    open fun setString(name: String, value: String?) { map[name] = value }
    open fun setByteBuffer(name: String, value: java.nio.ByteBuffer?) { map[name] = value }

    open fun getInteger(name: String): Int = map[name] as? Int ?: 0
    open fun getInteger(name: String, defaultValue: Int): Int = map[name] as? Int ?: defaultValue
    open fun getLong(name: String): Long = map[name] as? Long ?: 0L
    open fun getFloat(name: String): Float = map[name] as? Float ?: 0f
    open fun getFloat(name: String, defaultValue: Float): Float = map[name] as? Float ?: defaultValue
    open fun getString(name: String): String? = map[name] as? String
    open fun getByteBuffer(name: String): java.nio.ByteBuffer? = map[name] as? java.nio.ByteBuffer
    open fun containsKey(name: String): Boolean = map.containsKey(name)

    companion object {
        /** MediaFormat.createVideoFormat(mime, width, height)：构造视频格式。——Nova 注 */
        @JvmStatic
        fun createVideoFormat(mime: String?, width: Int, height: Int): MediaFormat = MediaFormat().apply {
            setString(KEY_MIME, mime)
            setInteger(KEY_WIDTH, width)
            setInteger(KEY_HEIGHT, height)
        }
        const val KEY_MIME = "mime"
        const val KEY_SAMPLE_RATE = "sample-rate"
        const val KEY_CHANNEL_COUNT = "channel-count"
        const val KEY_WIDTH = "width"
        const val KEY_HEIGHT = "height"
        const val KEY_BIT_RATE = "bitrate"
        const val KEY_FRAME_RATE = "frame-rate"
        const val KEY_I_FRAME_INTERVAL = "i-frame-interval"
        const val KEY_AAC_PROFILE = "aac-profile"
        const val MIMETYPE_AUDIO_AAC = "audio/mp4a-latm"
        const val MIMETYPE_VIDEO_AVC = "video/avc"
        const val MIMETYPE_VIDEO_HEVC = "video/hevc"
    }
}
