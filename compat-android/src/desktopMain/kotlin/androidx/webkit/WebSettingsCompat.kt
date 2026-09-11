package androidx.webkit

import android.util.Log
import android.webkit.WebSettings

/**
 * androidx.webkit.WebSettingsCompat 桌面 stub。
 */
object WebSettingsCompat {

    @JvmStatic
    fun setUserAgentMetadata(settings: WebSettings, metadata: UserAgentMetadata) {
        Log.w("WebSettingsCompat", "setUserAgentMetadata() 桌面无 WebView，UA 元数据不生效")
    }

    @JvmStatic
    fun getUserAgentMetadata(settings: WebSettings): UserAgentMetadata? = null
}
