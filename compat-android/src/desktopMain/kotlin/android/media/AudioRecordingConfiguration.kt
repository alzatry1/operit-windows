package android.media

/**
 * android.media.AudioRecordingConfiguration 垫片（P3-B3）。
 * 桌面无系统录音配置体系；方法与真实 API 同形，返回空值。
 * app 侧通过反射调用 getClientUid/getClientPackageName，提供真实方法名以保证反射命中。
 */
open class AudioRecordingConfiguration {
    open fun getClientUid(): Int = -1
    open fun getClientPackageName(): String = ""
    open fun getAudioSource(): Int = 0
    open fun getClientAudioSessionId(): Int = 0
    open fun getAudioFormat(): Any? = null
    open fun isClientSilenced(): Boolean = false
}
