package android.service.voice

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * android.service.voice 语音交互体系垫片（桌面无系统语音助手框架）。——Nova 注
 */

open class VoiceInteractionSessionService : android.app.Service() {
    override fun onBind(intent: android.content.Intent?): android.os.IBinder? = null
    open fun onNewSession(args: Bundle?): VoiceInteractionSession = VoiceInteractionSession(com.ai.assistance.operit.compat.AppGlobals.applicationContext)
}

open class VoiceInteractionSession(protected val context: Context) {
    open fun show(args: Bundle?, flags: Int) {}
    /** VoiceInteractionSession.onShow(args, showFlags)：会话显示回调。——Nova 注 */
    open fun onShow(args: Bundle?, showFlags: Int) {}
    open fun onHide() {}
    open fun hide() {}
    open fun setContentView(view: View?) {}
    open fun onCreate() {}
    open fun onDestroy() {}
    open fun finish() {}
}
