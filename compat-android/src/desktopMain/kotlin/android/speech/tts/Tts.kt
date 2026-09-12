package android.speech.tts

import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.io.File
import java.util.Locale

/**
 * android.speech.tts（P3-B2 新增）。
 * 桌面无 TTS 引擎：构造后立即回调 onInit(SUCCESS)，speak() 记日志并直接触发
 * UtteranceProgressListener 的 onStart/onDone（保持 app 的 utterance 队列流转）。
 */
open class TextToSpeech {

    fun interface OnInitListener {
        fun onInit(status: Int)
    }

    private var shutdown = false
    private var listener: UtteranceProgressListener? = null
    private var speechRate = 1.0f
    private var pitch = 1.0f
    private var language: Locale? = null
    private var audioAttributes: AudioAttributes? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    constructor(context: Context?) 

    constructor(context: Context?, listener: OnInitListener?) {
        notifyInit(listener)
    }

    constructor(context: Context?, listener: OnInitListener?, engine: String?) {
        notifyInit(listener)
    }

    constructor(context: Context?, listener: OnInitListener?, engine: String?, packageName: String?) {
        notifyInit(listener)
    }

    private fun notifyInit(listener: OnInitListener?) {
        if (listener != null) {
            mainHandler.post { listener.onInit(SUCCESS) }
        }
    }

    open fun speak(text: CharSequence?, queueMode: Int, params: Bundle?): Int =
        speak(text, queueMode, params, null)

    /** 旧式 speak（app 传 HashMap）。——Nova 注 */
    open fun speak(text: CharSequence?, queueMode: Int, params: HashMap<String, String>?): Int =
        speak(text, queueMode, null as Bundle?, null)

    open fun speak(text: CharSequence?, queueMode: Int, params: Bundle?, utteranceId: String?): Int {
        if (shutdown) return ERROR
        android.util.Log.d("TextToSpeech", "speak(queueMode=$queueMode, id=$utteranceId): ${text?.take(40)}")
        val l = listener
        if (l != null) {
            mainHandler.post {
                l.onStart(utteranceId)
                l.onDone(utteranceId)
            }
        }
        return SUCCESS
    }

    open fun stop(): Int = SUCCESS
    open fun shutdown() { shutdown = true }
    open val isSpeaking: Boolean = false
    /** TextToSpeech.availableLanguages（可用语言集，桌面恒空集）。——Nova 注 */
    open val availableLanguages: Set<Locale> get() = emptySet()

    open fun setLanguage(loc: Locale?): Int {
        language = loc
        return LANG_AVAILABLE
    }

    open fun getLanguage(): Locale? = language
    open fun getDefaultEngine(): String? = null

    open fun setSpeechRate(speechRate: Float): Int {
        this.speechRate = speechRate
        return SUCCESS
    }

    open fun getSpeechRate(): Float = speechRate

    open fun setPitch(pitch: Float): Int {
        this.pitch = pitch
        return SUCCESS
    }

    open fun getPitch(): Float = pitch

    open fun setOnUtteranceProgressListener(listener: UtteranceProgressListener): Int {
        this.listener = listener
        return SUCCESS
    }

    open fun setAudioAttributes(audioAttributes: AudioAttributes?): Int {
        this.audioAttributes = audioAttributes
        return SUCCESS
    }

    open fun synthesizeToFile(text: CharSequence?, params: Bundle?, file: File?, utteranceId: String?): Int = ERROR
    open fun synthesizeToFile(text: CharSequence?, params: Bundle?, filename: String?): Int = ERROR
    open fun playEarcon(earcon: String?, queueMode: Int, params: Bundle?, utteranceId: String?): Int = SUCCESS
    open fun playSilence(durationInMs: Long, queueMode: Int, params: Bundle?): Int = SUCCESS

    /** voices 属性（app 用 tts.voices）——Nova 注 */
    open val voices: Set<Voice> get() = emptySet()
    open fun setVoice(voice: Voice?): Int = SUCCESS
    open fun getFeatures(locale: Locale?): Set<String> = emptySet()
    open fun isLanguageAvailable(loc: Locale?): Int = LANG_AVAILABLE

    /** android.speech.tts.TextToSpeech.Engine。 */
    class Engine {
        companion object {
            const val KEY_PARAM_UTTERANCE_ID = "utteranceId"
            const val KEY_PARAM_STREAM = "streamType"
            const val KEY_PARAM_VOLUME = "volume"
            const val KEY_PARAM_PAN = "pan"
            const val KEY_FEATURE_NETWORK_SYNTHESIS = "networkTts"
            const val KEY_FEATURE_EMBEDDED_SYNTHESIS = "embeddedTts"
            const val DEFAULT_STREAM = 3
            const val ACTION_TTS_QUEUE_PROCESSING_COMPLETED = "android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED"
            const val ACTION_CHECK_TTS_DATA = "android.speech.tts.engine.CHECK_TTS_DATA"
            const val ACTION_INSTALL_TTS_DATA = "android.speech.tts.engine.INSTALL_TTS_DATA"
        }
    }

    companion object {
        const val SUCCESS = 0
        const val ERROR = -1
        const val STOPPED = -2

        const val QUEUE_FLUSH = 0
        const val QUEUE_ADD = 1
        const val QUEUE_DESTROY = 2

        const val LANG_AVAILABLE = 0
        const val LANG_COUNTRY_AVAILABLE = 1
        const val LANG_COUNTRY_VAR_AVAILABLE = 2
        const val LANG_MISSING_DATA = -1
        const val LANG_NOT_SUPPORTED = -2

        const val ERROR_SYNTHESIS = -3
        const val ERROR_SERVICE = -4
        const val ERROR_OUTPUT = -5
        const val ERROR_NETWORK = -6
        const val ERROR_NETWORK_TIMEOUT = -7
        const val ERROR_INVALID_REQUEST = -8
        const val ERROR_NOT_INSTALLED_YET = -9
    }
}

/** android.speech.tts.Voice 垫片。——Nova 注 */
open class Voice(
    open val name: String,
    open val locale: java.util.Locale,
    open val quality: Int = QUALITY_NORMAL,
    open val latency: Int = LATENCY_NORMAL,
    open val isNetworkConnectionRequired: Boolean = false,
    open val features: Set<String> = emptySet(),
) {
    companion object {
        const val QUALITY_VERY_HIGH = 400
        const val QUALITY_HIGH = 300
        const val QUALITY_NORMAL = 200
        const val QUALITY_LOW = 100
        const val QUALITY_VERY_LOW = 0
        const val LATENCY_VERY_LOW = 200
        const val LATENCY_LOW = 300
        const val LATENCY_NORMAL = 400
        const val LATENCY_HIGH = 500
        const val LATENCY_VERY_HIGH = 600
    }
}
