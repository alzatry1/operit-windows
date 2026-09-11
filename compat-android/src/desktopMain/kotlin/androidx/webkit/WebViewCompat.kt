package androidx.webkit

import android.net.Uri
import android.util.Log
import android.webkit.WebView

/**
 * androidx.webkit 核心垫片桌面 stub（B1b-2）。
 * 桌面无 WebView：所有方法记日志并返回无害默认值。
 */

/** androidx.webkit.ScriptHandler（fun interface 便于 lambda 构造；与真实接口源码兼容）。 */
fun interface ScriptHandler {
    fun remove()
}

/** androidx.webkit.JavaScriptReplyProxy。 */
interface JavaScriptReplyProxy {
    fun postMessage(response: String)
}

/** androidx.webkit.WebMessageCompat：包装字符串消息。 */
class WebMessageCompat(val data: String?, val type: Int = TYPE_STRING) {
    constructor(data: String) : this(data, TYPE_STRING)

    companion object {
        const val TYPE_STRING = 0
        const val TYPE_ARRAY_BUFFER = 1
    }
}

/** androidx.webkit.WebViewCompat。 */
object WebViewCompat {

    /** androidx.webkit.WebViewCompat.WebMessageListener（嵌套接口，与真实 API 一致）。 */
    interface WebMessageListener {
        fun onPostMessage(
            view: WebView,
            message: WebMessageCompat,
            sourceOrigin: Uri,
            isMainFrame: Boolean,
            replyProxy: JavaScriptReplyProxy,
        )
    }

    @JvmStatic
    fun addDocumentStartJavaScript(
        webView: WebView,
        script: String,
        allowedOriginRules: Set<String>,
    ): ScriptHandler {
        Log.w("WebViewCompat", "addDocumentStartJavaScript(${script.length} chars) 桌面无 WebView，脚本不注入")
        return ScriptHandler {
            Log.d("WebViewCompat", "ScriptHandler.remove() no-op")
        }
    }

    @JvmStatic
    fun addWebMessageListener(
        webView: WebView,
        jsObjectName: String,
        allowedOriginRules: Set<String>,
        listener: WebMessageListener,
    ) {
        Log.w("WebViewCompat", "addWebMessageListener($jsObjectName) 桌面无 WebView，监听不生效")
    }

    @JvmStatic
    fun removeWebMessageListener(webView: WebView, jsObjectName: String) {
        Log.d("WebViewCompat", "removeWebMessageListener($jsObjectName) no-op")
    }

    @JvmStatic
    fun isAudioMuted(webView: WebView): Boolean = false

    @JvmStatic
    fun setAudioMuted(webView: WebView, mute: Boolean) {
        Log.d("WebViewCompat", "setAudioMuted($mute) no-op")
    }
}
