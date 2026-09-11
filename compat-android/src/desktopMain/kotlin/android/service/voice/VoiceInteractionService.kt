package android.service.voice

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * android.service.voice.VoiceInteractionService 编译级 stub。
 * 桌面无系统语音助手框架；生命周期方法为安全 no-op。
 */
open class VoiceInteractionService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    /** 服务准备就绪时回调。 */
    open fun onReady() {}

    /** 返回本服务支持的语音动作集合。 */
    open fun onGetSupportedVoiceActions(voiceActions: MutableSet<String>): MutableSet<String> =
        mutableSetOf()

    /** 系统关闭语音交互时回调。 */
    open fun onShutdown() {}
}
