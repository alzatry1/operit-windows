package android.media

import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * android.media 轻 stub（B1a 只需要 AudioAttributes/AudioManager 支撑 NotificationChannel 与服务表）。
 * MediaPlayer/MediaRecorder/AudioRecord 等实装见后续批次。
 */

/** android.media.AudioAttributes。 */
class AudioAttributes private constructor(
    val usage: Int,
    val contentType: Int,
    val flags: Int,
) {

    class Builder {
        private var usage = USAGE_UNKNOWN
        private var contentType = CONTENT_TYPE_UNKNOWN
        private var flags = 0

        constructor()
        constructor(aa: AudioAttributes) {
            usage = aa.usage; contentType = aa.contentType; flags = aa.flags
        }

        fun setUsage(usage: Int): Builder = apply { this.usage = usage }
        fun setContentType(contentType: Int): Builder = apply { this.contentType = contentType }
        fun setFlags(flags: Int): Builder = apply { this.flags = flags }
        fun setLegacyStreamType(streamType: Int): Builder = this
        fun build(): AudioAttributes = AudioAttributes(usage, contentType, flags)
    }

    companion object {
        const val USAGE_UNKNOWN = 0
        const val USAGE_MEDIA = 1
        const val USAGE_VOICE_COMMUNICATION = 2
        const val USAGE_VOICE_COMMUNICATION_SIGNALLING = 3
        const val USAGE_ALARM = 4
        const val USAGE_NOTIFICATION = 5
        const val USAGE_NOTIFICATION_RINGTONE = 6
        const val USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7
        const val USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8
        const val USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9
        const val USAGE_NOTIFICATION_EVENT = 10
        const val USAGE_ASSISTANCE_ACCESSIBILITY = 11
        const val USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12
        const val USAGE_ASSISTANCE_SONIFICATION = 13
        const val USAGE_GAME = 14
        const val USAGE_VIRTUAL_SOURCE = 15
        const val USAGE_ASSISTANT = 16

        const val CONTENT_TYPE_UNKNOWN = 0
        const val CONTENT_TYPE_SPEECH = 1
        const val CONTENT_TYPE_MUSIC = 2
        const val CONTENT_TYPE_MOVIE = 3
        const val CONTENT_TYPE_SONIFICATION = 4

        const val FLAG_AUDIBILITY_ENFORCED = 1
        const val FLAG_HW_AV_SYNC = 16
        const val FLAG_LOW_LATENCY = 256
    }
}

/** android.media.AudioManager 轻 stub。 */
open class AudioManager {
    open fun getMode(): Int = MODE_NORMAL
    open fun setMode(mode: Int) {}
    open fun isSpeakerphoneOn(): Boolean = true
    open fun setSpeakerphoneOn(on: Boolean) {}
    open fun isBluetoothScoOn(): Boolean = false
    open fun setBluetoothScoOn(on: Boolean) {}
    open fun isMicrophoneMute(): Boolean = false
    open fun setMicrophoneMute(on: Boolean) {}
    open fun isMusicActive(): Boolean = false
    open fun isWiredHeadsetOn(): Boolean = false
    open fun isBluetoothA2dpOn(): Boolean = false
    open fun getStreamVolume(streamType: Int): Int = 8
    open fun setStreamVolume(streamType: Int, index: Int, flags: Int) {}
    open fun getStreamMaxVolume(streamType: Int): Int = 15
    open fun getStreamMinVolume(streamType: Int): Int = 0
    open fun adjustStreamVolume(streamType: Int, direction: Int, flags: Int) {}
    open fun adjustVolume(direction: Int, flags: Int) {}
    open fun setStreamMute(streamType: Int, state: Boolean) {}
    open fun isStreamMute(streamType: Int): Boolean = false
    open fun getRingerMode(): Int = RINGER_MODE_NORMAL
    open fun setRingerMode(ringerMode: Int) {}
    open fun abandonAudioFocus(l: OnAudioFocusChangeListener?): Int = AUDIOFOCUS_REQUEST_GRANTED
    open fun requestAudioFocus(l: OnAudioFocusChangeListener?, streamType: Int, durationHint: Int): Int = AUDIOFOCUS_REQUEST_GRANTED
    open fun requestAudioFocus(request: AudioFocusRequest): Int = AUDIOFOCUS_REQUEST_GRANTED
    open fun abandonAudioFocusRequest(request: AudioFocusRequest): Int = AUDIOFOCUS_REQUEST_GRANTED
    open fun setParameters(keyValuePairs: String?) {}
    open fun getParameters(keys: String?): String = ""
    open fun playSoundEffect(effectType: Int) {}
    open fun playSoundEffect(effectType: Int, volume: Float) {}
    open fun registerAudioDeviceCallback(callback: Any?, handler: Handler?) {}
    open fun unregisterAudioDeviceCallback(callback: Any?) {}
    open fun registerAudioRecordingCallback(callback: AudioRecordingCallback, handler: Handler?) {}
    open fun unregisterAudioRecordingCallback(callback: AudioRecordingCallback) {}
    open val activeRecordingConfigurations: List<AudioRecordingConfiguration>
        get() = emptyList()

    /** android.media.AudioManager.AudioRecordingCallback。 */
    open class AudioRecordingCallback {
        open fun onRecordingConfigChanged(configs: List<AudioRecordingConfiguration>) {}
    }
    open fun getDevices(flags: Int): Array<Any> = emptyArray()
    open fun setStreamSolo(streamType: Int, state: Boolean) {}
    open fun shouldVibrate(vibrateType: Int): Boolean = false
    open fun getVibrateSetting(vibrateType: Int): Int = VIBRATE_SETTING_OFF
    open fun setVibrateSetting(vibrateType: Int, vibrateSetting: Int) {}

    fun interface OnAudioFocusChangeListener {
        fun onAudioFocusChange(focusChange: Int)
    }

    class AudioFocusRequest private constructor() {
        class Builder {
            constructor()
            constructor(focusGain: Int)
            fun build(): AudioFocusRequest = AudioFocusRequest()
            fun setFocusGain(focusGain: Int): Builder = this
            fun setOnAudioFocusChangeListener(l: OnAudioFocusChangeListener): Builder = this
            fun setAudioAttributes(aa: AudioAttributes): Builder = this
            fun setAcceptsDelayedFocusGain(accepts: Boolean): Builder = this
            fun setWillPauseWhenDucked(willPause: Boolean): Builder = this
        }
    }

    companion object {
        const val STREAM_VOICE_CALL = 0
        const val STREAM_SYSTEM = 1
        const val STREAM_RING = 2
        const val STREAM_MUSIC = 3
        const val STREAM_ALARM = 4
        const val STREAM_NOTIFICATION = 5
        const val STREAM_DTMF = 8
        const val STREAM_ACCESSIBILITY = 10

        const val MODE_NORMAL = 0
        const val MODE_RINGTONE = 1
        const val MODE_IN_CALL = 2
        const val MODE_IN_COMMUNICATION = 3
        const val MODE_CALL_SCREENING = 4

        const val RINGER_MODE_SILENT = 0
        const val RINGER_MODE_VIBRATE = 1
        const val RINGER_MODE_NORMAL = 2

        const val ADJUST_RAISE = 1
        const val ADJUST_LOWER = -1
        const val ADJUST_SAME = 0
        const val ADJUST_MUTE = -100
        const val ADJUST_UNMUTE = 100
        const val ADJUST_TOGGLE_MUTE = 101

        const val FLAG_SHOW_UI = 1
        const val FLAG_PLAY_SOUND = 4
        const val FLAG_VIBRATE = 16

        const val AUDIOFOCUS_REQUEST_GRANTED = 1
        const val AUDIOFOCUS_REQUEST_FAILED = 0
        const val AUDIOFOCUS_REQUEST_DELAYED = 2

        const val AUDIOFOCUS_GAIN = 1
        const val AUDIOFOCUS_GAIN_TRANSIENT = 2
        const val AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3
        const val AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4
        const val AUDIOFOCUS_LOSS = -1
        const val AUDIOFOCUS_LOSS_TRANSIENT = -2
        const val AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3
        const val AUDIOFOCUS_GAIN_TRANSIENT_NO_DUCK = 6

        const val VIBRATE_TYPE_RINGER = 0
        const val VIBRATE_TYPE_NOTIFICATION = 1
        const val VIBRATE_SETTING_ON = 1
        const val VIBRATE_SETTING_OFF = 0
        const val VIBRATE_SETTING_ONLY_SILENT = 2

        const val ACTION_AUDIO_BECOMING_NOISY = "android.media.AUDIO_BECOMING_NOISY"
        const val ACTION_SCO_AUDIO_STATE_UPDATED = "android.media.SCO_AUDIO_STATE_UPDATED"
        const val ACTION_VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION"
    }
}

/** android.media.MediaPlayer 轻 stub（后续批次用 VLCJ/FFmpeg 实装）。 */
open class MediaPlayer {
    open fun setDataSource(path: String?) {}
    open fun setDataSource(context: android.content.Context?, uri: android.net.Uri?) {}
    open fun prepare() {}
    open fun prepareAsync() {}
    open fun start() {}
    open fun stop() {}
    open fun pause() {}
    open fun release() {}
    open fun reset() {}
    open fun seekTo(msec: Int) {}
    open fun isPlaying(): Boolean = false
    open fun getCurrentPosition(): Int = 0
    open fun getDuration(): Int = 0
    open fun getAudioSessionId(): Int = 0
    open fun setAudioSessionId(sessionId: Int) {}
    open fun setVolume(leftVolume: Float, rightVolume: Float) {}
    open fun setAudioAttributes(attributes: AudioAttributes?) {}
    open fun setLooping(looping: Boolean) {}
    open fun isLooping(): Boolean = false
    open fun setOnCompletionListener(l: OnCompletionListener?) {}
    open fun setOnPreparedListener(l: OnPreparedListener?) {}
    open fun setOnErrorListener(l: OnErrorListener?) {}
    open fun setOnBufferingUpdateListener(l: OnBufferingUpdateListener?) {}

    fun interface OnCompletionListener { fun onCompletion(mp: MediaPlayer) }
    fun interface OnPreparedListener { fun onPrepared(mp: MediaPlayer) }
    fun interface OnErrorListener { fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean }
    fun interface OnBufferingUpdateListener { fun onBufferingUpdate(mp: MediaPlayer, percent: Int) }

    companion object {
        const val MEDIA_ERROR_UNKNOWN = 1
        const val MEDIA_ERROR_SERVER_DIED = 100
        const val MEDIA_ERROR_IO = -1004
        const val MEDIA_ERROR_MALFORMED = -1007
        const val MEDIA_ERROR_UNSUPPORTED = -1010
        const val MEDIA_ERROR_TIMED_OUT = -110
    }
}

/** android.media.MediaRecorder 轻 stub。 */
open class MediaRecorder {
    open fun setAudioSource(source: Int) {}
    open fun setVideoSource(source: Int) {}
    open fun setOutputFormat(format: Int) {}
    open fun setAudioEncoder(encoder: Int) {}
    open fun setVideoEncoder(encoder: Int) {}
    open fun setOutputFile(path: String?) {}
    open fun setOutputFile(fd: java.io.FileDescriptor?) {}
    open fun setOutputFile(file: java.io.File?) {}
    open fun setAudioEncodingBitRate(bitRate: Int) {}
    open fun setAudioSamplingRate(samplingRate: Int) {}
    open fun setAudioChannels(numChannels: Int) {}
    open fun setVideoEncodingBitRate(bitRate: Int) {}
    open fun setVideoSize(width: Int, height: Int) {}
    open fun setVideoFrameRate(rate: Int) {}
    open fun setMaxDuration(maxDurationMs: Int) {}
    open fun setMaxFileSize(maxFileSizeBytes: Long) {}
    open fun prepare() {}
    open fun start() {}
    open fun stop() {}
    open fun pause() {}
    open fun resume() {}
    open fun reset() {}
    open fun release() {}
    open fun setOnInfoListener(l: OnInfoListener?) {}
    open fun setOnErrorListener(l: OnErrorListener?) {}
    open fun getSurface(): Any? = null

    fun interface OnInfoListener { fun onInfo(mr: MediaRecorder, what: Int, extra: Int) }
    fun interface OnErrorListener { fun onError(mr: MediaRecorder, what: Int, extra: Int) }

    object AudioSource {
        const val DEFAULT = 0
        const val MIC = 1
        const val VOICE_UPLINK = 2
        const val VOICE_DOWNLINK = 3
        const val VOICE_CALL = 4
        const val CAMCORDER = 5
        const val VOICE_RECOGNITION = 6
        const val VOICE_COMMUNICATION = 7
        const val REMOTE_SUBMIX = 8
        const val UNPROCESSED = 9
    }

    object VideoSource {
        const val DEFAULT = 0
        const val CAMERA = 1
        const val SURFACE = 2
    }

    object OutputFormat {
        const val DEFAULT = 0
        const val THREE_GPP = 1
        const val MPEG_4 = 2
        const val AMR_NB = 3
        const val AMR_WB = 4
        const val AAC_ADTS = 6
        const val MPEG_2_TS = 8
        const val WEBM = 9
        const val OGG = 11
    }

    object AudioEncoder {
        const val DEFAULT = 0
        const val AMR_NB = 1
        const val AMR_WB = 2
        const val AAC = 3
        const val HE_AAC = 4
        const val AAC_ELD = 5
        const val VORBIS = 6
        const val OPUS = 7
    }

    object VideoEncoder {
        const val DEFAULT = 0
        const val H263 = 1
        const val H264 = 2
        const val MPEG_4_SP = 3
        const val VP8 = 4
        const val HEVC = 5
    }
}

/** android.media.AudioRecord 轻 stub。 */
open class AudioRecord(
    private val audioSource: Int = 1,
    private val sampleRateInHz: Int = 44100,
    private val channelConfig: Int = 16,
    private val audioFormat: Int = 2,
    private val bufferSizeInBytes: Int = 0,
) {
    open fun startRecording() {}
    open fun stop() {}
    open fun release() {}
    open fun read(audioData: ByteArray, offsetInBytes: Int, sizeInBytes: Int): Int = 0
    open fun read(audioData: ShortArray, offsetInShorts: Int, sizeInShorts: Int): Int = 0
    open fun getRecordingState(): Int = RECORDSTATE_STOPPED
    open fun getState(): Int = STATE_INITIALIZED
    open fun getSampleRate(): Int = sampleRateInHz
    open fun getChannelCount(): Int = 1
    open fun getAudioFormat(): Int = audioFormat
    open fun getBufferSizeInFrames(): Int = bufferSizeInBytes / 2

    companion object {
        const val STATE_UNINITIALIZED = 0
        const val STATE_INITIALIZED = 1
        const val RECORDSTATE_STOPPED = 1
        const val RECORDSTATE_RECORDING = 3
        const val SUCCESS = 0
        const val ERROR = -1
        const val ERROR_BAD_VALUE = -2
        const val ERROR_INVALID_OPERATION = -3
        const val ERROR_DEAD_OBJECT = -6

        @JvmStatic
        fun getMinBufferSize(sampleRateInHz: Int, channelConfig: Int, audioFormat: Int): Int = 4096
    }
}

/** android.media.AudioTrack 轻 stub。 */
open class AudioTrack(
    private val streamType: Int = 3,
    private val sampleRateInHz: Int = 44100,
    private val channelConfig: Int = 12,
    private val audioFormat: Int = 2,
    private val bufferSizeInBytes: Int = 0,
    private val mode: Int = 1,
) {
    /** android.media.AudioTrack.Builder（桌面编译形状）。——Nova 注 */
    class Builder {
        private var audioAttributes: AudioAttributes? = null
        private var audioFormat: AudioFormat? = null
        private var bufferSizeInBytes: Int = 0
        private var transferMode: Int = MODE_STREAM
        private var sessionId: Int = 0
        fun setAudioAttributes(attributes: AudioAttributes?): Builder = apply { audioAttributes = attributes }
        fun setAudioFormat(format: AudioFormat?): Builder = apply { audioFormat = format }
        fun setBufferSizeInBytes(size: Int): Builder = apply { bufferSizeInBytes = size }
        fun setTransferMode(mode: Int): Builder = apply { transferMode = mode }
        fun setSessionId(id: Int): Builder = apply { sessionId = id }
        fun build(): AudioTrack = AudioTrack(
            sampleRateInHz = audioFormat?.sampleRate ?: 44100,
            channelConfig = audioFormat?.channelMask ?: 12,
            audioFormat = audioFormat?.encoding ?: 2,
            bufferSizeInBytes = bufferSizeInBytes,
            mode = transferMode,
        )
    }

    open fun play() {}
    open fun setAudioAttributes(attributes: AudioAttributes?) {}
    open fun stop() {}
    open fun pause() {}
    open fun flush() {}
    open fun release() {}
    open fun write(audioData: ByteArray, offsetInBytes: Int, sizeInBytes: Int): Int = sizeInBytes
    open fun write(audioData: ShortArray, offsetInShorts: Int, sizeInShorts: Int): Int = sizeInShorts
    open fun getPlayState(): Int = PLAYSTATE_STOPPED
    open fun getState(): Int = STATE_INITIALIZED
    open fun getSampleRate(): Int = sampleRateInHz
    open fun setStereoVolume(leftGain: Float, rightGain: Float): Int = SUCCESS

    companion object {
        const val STATE_UNINITIALIZED = 0
        const val STATE_INITIALIZED = 1
        const val STATE_NO_STATIC_BUFFER = 2
        const val PLAYSTATE_STOPPED = 1
        const val PLAYSTATE_PAUSED = 2
        const val PLAYSTATE_PLAYING = 3
        const val MODE_STATIC = 0
        const val MODE_STREAM = 1
        const val SUCCESS = 0
        const val ERROR = -1
        const val ERROR_BAD_VALUE = -2
        const val ERROR_INVALID_OPERATION = -3
        const val ERROR_DEAD_OBJECT = -6

        @JvmStatic
        fun getMinBufferSize(sampleRateInHz: Int, channelConfig: Int, audioFormat: Int): Int = 4096
    }
}

/** android.media.AudioFormat 常量。 */
class AudioFormat(
    val encoding: Int = ENCODING_DEFAULT,
    val sampleRate: Int = 44100,
    val channelMask: Int = CHANNEL_OUT_STEREO,
) {
    /** android.media.AudioFormat.Builder。 */
    class Builder {
        private var encoding: Int = ENCODING_PCM_16BIT
        private var sampleRate: Int = 44100
        private var channelMask: Int = CHANNEL_OUT_STEREO
        fun setEncoding(encoding: Int): Builder = apply { this.encoding = encoding }
        fun setSampleRate(sampleRate: Int): Builder = apply { this.sampleRate = sampleRate }
        fun setChannelMask(channelMask: Int): Builder = apply { this.channelMask = channelMask }
        fun setChannelIndexMask(channelIndexMask: Int): Builder = apply { this.channelMask = channelIndexMask }
        fun build(): AudioFormat = AudioFormat(encoding, sampleRate, channelMask)
    }

    companion object {

    const val ENCODING_INVALID = 0
    const val ENCODING_DEFAULT = 1
    const val ENCODING_PCM_16BIT = 2
    const val ENCODING_PCM_8BIT = 3
    const val ENCODING_PCM_FLOAT = 4
    const val ENCODING_AC3 = 5
    const val ENCODING_E_AC3 = 6
    const val ENCODING_DTS = 7
    const val ENCODING_DTS_HD = 8
    const val ENCODING_MP3 = 9
    const val ENCODING_AAC_LC = 10
    const val ENCODING_AAC_HE_V1 = 11
    const val ENCODING_AAC_HE_V2 = 12
    const val ENCODING_IEC61937 = 13
    const val ENCODING_DOLBY_TRUEHD = 14
    const val ENCODING_AAC_ELD = 15
    const val ENCODING_AAC_XHE = 16
    const val ENCODING_AC4 = 17
    const val ENCODING_E_AC3_JOC = 18
    const val ENCODING_DOLBY_MAT = 19
    const val ENCODING_OPUS = 20
    const val ENCODING_PCM_24BIT_PACKED = 21
    const val ENCODING_PCM_32BIT = 22

    const val CHANNEL_CONFIGURATION_DEFAULT = 1
    const val CHANNEL_CONFIGURATION_MONO = 2
    const val CHANNEL_CONFIGURATION_STEREO = 3
    const val CHANNEL_OUT_FRONT_LEFT = 1
    const val CHANNEL_OUT_FRONT_RIGHT = 2
    const val CHANNEL_OUT_FRONT_CENTER = 4
    const val CHANNEL_OUT_LOW_FREQUENCY = 8
    const val CHANNEL_OUT_BACK_LEFT = 16
    const val CHANNEL_OUT_BACK_RIGHT = 32
    const val CHANNEL_OUT_STEREO = 12
    const val CHANNEL_OUT_MONO = 4
    const val CHANNEL_OUT_QUAD = 51
    const val CHANNEL_OUT_SURROUND = 51
    const val CHANNEL_OUT_5POINT1 = 63
    const val CHANNEL_OUT_7POINT1_SURROUND = 639
    const val CHANNEL_IN_LEFT = 4
    const val CHANNEL_IN_RIGHT = 8
    const val CHANNEL_IN_FRONT = 16
    const val CHANNEL_IN_BACK = 32
    const val CHANNEL_IN_MONO = 16
    const val CHANNEL_IN_STEREO = 12

}
}

/** android.media.MediaMetadataRetriever 轻 stub。 */
open class MediaMetadataRetriever {
    open fun setDataSource(path: String?) {}
    open fun setDataSource(context: android.content.Context?, uri: android.net.Uri?) {}
    open fun setDataSource(fd: java.io.FileDescriptor?) {}
    open fun setDataSource(fd: java.io.FileDescriptor?, offset: Long, length: Long) {}
    open fun extractMetadata(keyCode: Int): String? = null
    open fun getFrameAtTime(timeUs: Long, option: Int): android.graphics.Bitmap? = null
    open fun getFrameAtTime(timeUs: Long): android.graphics.Bitmap? = null
    open fun getFrameAtTime(): android.graphics.Bitmap? = null
    open fun getEmbeddedPicture(): ByteArray? = null
    open fun release() {}

    companion object {
        const val METADATA_KEY_DURATION = 9
        const val METADATA_KEY_TITLE = 2
        const val METADATA_KEY_ARTIST = 13
        const val METADATA_KEY_ALBUM = 1
        const val METADATA_KEY_BITRATE = 20
        const val METADATA_KEY_VIDEO_WIDTH = 19
        const val METADATA_KEY_VIDEO_HEIGHT = 18
        const val METADATA_KEY_MIMETYPE = 12
        const val METADATA_KEY_DATE = 5
        const val METADATA_KEY_HAS_VIDEO = 16
        const val METADATA_KEY_HAS_AUDIO = 15
        const val METADATA_KEY_NUM_TRACKS = 10
        const val OPTION_CLOSEST_SYNC = 2
        const val OPTION_CLOSEST = 3
        const val OPTION_NEXT_SYNC = 1
        const val OPTION_PREVIOUS_SYNC = 0
    }
}

/** android.media.MediaScannerConnection 轻 stub。 */
open class MediaScannerConnection(
    context: android.content.Context?,
    callback: MediaScannerConnectionClient?,
) {
    interface MediaScannerConnectionClient : OnScanCompletedListener {
        fun onMediaScannerConnected()
    }

    fun interface OnScanCompletedListener {
        fun onScanCompleted(path: String?, uri: android.net.Uri?)
    }

    open fun connect() {}
    open fun disconnect() {}
    open fun isConnected(): Boolean = false
    open fun scanFile(path: String?, mimeType: String?) {}
    open fun scanFile(path: String?, mimeType: String?, callback: OnScanCompletedListener?) {}

    companion object {
        @JvmStatic
        fun scanFile(context: android.content.Context?, paths: Array<String>, mimeTypes: Array<String>?, callback: OnScanCompletedListener?) {
            paths.forEach { path -> callback?.onScanCompleted(path, null) }
        }
    }
}

/** android.media.ExifInterface 轻 stub。 */
open class ExifInterface {
    private val attributes = HashMap<String, String>()

    constructor(path: String)
    constructor(file: java.io.File)
    constructor(fd: java.io.FileDescriptor)
    constructor(stream: java.io.InputStream)

    open fun getAttribute(tag: String): String? = attributes[tag]
    open fun setAttribute(tag: String, value: String?) { if (value == null) attributes.remove(tag) else attributes[tag] = value }
    open fun saveAttributes() {}
    open fun getLatLong(): DoubleArray? = null
    open fun getAltitude(defaultValue: Double): Double = defaultValue
    open fun getAttributeInt(tag: String, defaultValue: Int): Int = attributes[tag]?.toIntOrNull() ?: defaultValue
    open fun getAttributeLong(tag: String, defaultValue: Long): Long = attributes[tag]?.toLongOrNull() ?: defaultValue
    open fun getAttributeDouble(tag: String, defaultValue: Double): Double = attributes[tag]?.toDoubleOrNull() ?: defaultValue
    open fun hasAttribute(tag: String): Boolean = attributes.containsKey(tag)
    open fun getThumbnail(): ByteArray? = null
    open fun getThumbnailBitmap(): android.graphics.Bitmap? = null
    open fun isThumbnailCompressed(): Boolean = false
    open fun getRotation(): Int = 0

    companion object {
        const val TAG_ORIENTATION = "Orientation"
        const val TAG_DATETIME = "DateTime"
        const val TAG_DATETIME_ORIGINAL = "DateTimeOriginal"
        const val TAG_MAKE = "Make"
        const val TAG_MODEL = "Model"
        const val TAG_FLASH = "Flash"
        const val TAG_FOCAL_LENGTH = "FocalLength"
        const val TAG_GPS_LATITUDE = "GPSLatitude"
        const val TAG_GPS_LONGITUDE = "GPSLongitude"
        const val TAG_GPS_LATITUDE_REF = "GPSLatitudeRef"
        const val TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef"
        const val TAG_GPS_ALTITUDE = "GPSAltitude"
        const val TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef"
        const val TAG_IMAGE_WIDTH = "ImageWidth"
        const val TAG_IMAGE_LENGTH = "ImageLength"
        const val TAG_EXPOSURE_TIME = "ExposureTime"
        const val TAG_APERTURE_VALUE = "ApertureValue"
        const val TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings"
        const val TAG_WHITE_BALANCE = "WhiteBalance"
        const val ORIENTATION_UNDEFINED = 0
        const val ORIENTATION_NORMAL = 1
        const val ORIENTATION_FLIP_HORIZONTAL = 2
        const val ORIENTATION_ROTATE_180 = 3
        const val ORIENTATION_FLIP_VERTICAL = 4
        const val ORIENTATION_TRANSPOSE = 5
        const val ORIENTATION_ROTATE_90 = 6
        const val ORIENTATION_TRANSVERSE = 7
        const val ORIENTATION_ROTATE_270 = 8
    }
}
