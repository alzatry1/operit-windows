package android.webkit

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * android.webkit 最小垫片（B1b-2 新建）。
 * 存在原因：androidx.webkit 垫片（WebViewCompat/WebSettingsCompat）的方法签名
 * 依赖 android.webkit.WebView / WebSettings / ValueCallback 类型。
 * WebView 完整体系（WebViewClient/CookieManager/渲染宿主）属后续批次，这里仅提供
 * 编译所需骨架，方法全部 no-op。
 */

/** android.webkit.ValueCallback。 */
interface ValueCallback<T> {
    fun onReceiveValue(value: T?)
}

/** android.webkit.WebSettings：纯字段容器。 */
open class WebSettings {
    open var userAgentString: String? = null
    open var javaScriptEnabled: Boolean = false
    open var useWideViewPort: Boolean = false
    open var loadWithOverviewMode: Boolean = false
    open var domStorageEnabled: Boolean = false
    open var allowFileAccess: Boolean = true
    open var allowContentAccess: Boolean = true
    open var mediaPlaybackRequiresUserGesture: Boolean = true
    open var builtInZoomControls: Boolean = false
    open var displayZoomControls: Boolean = false
    open var cacheMode: Int = LOAD_DEFAULT
    open var textZoom: Int = 100
    open var databaseEnabled: Boolean = false
    open var javaScriptCanOpenWindowsAutomatically: Boolean = false
    open var loadsImagesAutomatically: Boolean = true
    open var blockNetworkImage: Boolean = false
    open var blockNetworkLoads: Boolean = false
    open var defaultTextEncodingName: String? = "UTF-8"
    open var mixedContentMode: Int = MIXED_CONTENT_NEVER_ALLOW
    open var defaultFontSize: Int = 16
    open var minimumFontSize: Int = 8
    open var minimumLogicalFontSize: Int = 8
    open var supportMultipleWindows: Boolean = false
    open var setSupportZoom: Boolean = true

    private var geolocationEnabled = true

    open fun setGeolocationEnabled(flag: Boolean) { geolocationEnabled = flag }
    open fun getGeolocationEnabled(): Boolean = geolocationEnabled

    companion object {
        const val LOAD_DEFAULT = -1
        const val LOAD_NORMAL = 0
        const val LOAD_CACHE_ELSE_NETWORK = 1
        const val LOAD_NO_CACHE = 2
        const val LOAD_CACHE_ONLY = 3

        const val MIXED_CONTENT_NEVER_ALLOW = 1
        const val MIXED_CONTENT_ALWAYS_ALLOW = 0
        const val MIXED_CONTENT_COMPATIBILITY_MODE = 2
    }
}

/** android.webkit.WebView：桌面无 WebView 渲染，仅骨架。 */
open class WebView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open val settings: WebSettings = WebSettings()

    /** 当前装载的客户端。JVM 签名注意：属性 setter 与 set* fun 同名冲突，
     *  故 set* fun 额外带一个默认参数（Unit）以区分签名。 */
    open var webViewClient: WebViewClient? = null
    open var webChromeClient: WebChromeClient? = null

    @Suppress("UNUSED_PARAMETER")
    open fun setWebViewClient(client: WebViewClient?, ignored: Unit = Unit) { webViewClient = client }

    @Suppress("UNUSED_PARAMETER")
    open fun setWebChromeClient(client: WebChromeClient?, ignored: Unit = Unit) { webChromeClient = client }

    private var downloadListener: DownloadListener? = null
    open fun setDownloadListener(listener: DownloadListener?) { downloadListener = listener }

    /** android.webkit.WebView.WebViewTransport。 */
    class WebViewTransport {
        var webView: WebView? = null
    }

    open fun loadUrl(url: String) {}
    open fun loadData(data: String, mimeType: String?, encoding: String?) {}
    open fun loadDataWithBaseURL(baseUrl: String?, data: String, mimeType: String?, encoding: String?, historyUrl: String?) {}
    open fun evaluateJavascript(script: String, resultCallback: ValueCallback<String>?) {}
    open fun addJavascriptInterface(`object`: Any, name: String) {}
    open fun removeJavascriptInterface(name: String) {}
    open fun reload() {}
    open fun stopLoading() {}
    open fun goBack() {}
    open fun goForward() {}
    open fun canGoBack(): Boolean = false
    open fun canGoForward(): Boolean = false
    open fun clearHistory() {}
    open fun clearCache(includeDiskFiles: Boolean) {}
    open fun clearFormData() {}
    open fun clearSslPreferences() {}
    open fun getUrl(): String? = null
    open fun getOriginalUrl(): String? = null
    open fun getTitle(): String? = null
    open fun onResume() {}
    open fun onPause() {}
    open fun pauseTimers() {}
    open fun resumeTimers() {}
    open fun destroy() {}
}
