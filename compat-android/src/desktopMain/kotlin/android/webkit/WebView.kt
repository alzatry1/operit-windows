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
    open var supportZoom: Boolean = true
    open var autoZoomEnabled: Boolean = false
    open var allowUniversalAccessFromFileURLs: Boolean = false
    open var allowFileAccessFromFileURLs: Boolean = false
    open var safeBrowsingEnabled: Boolean = false

    private var geolocationEnabled = true

    open fun setGeolocationEnabled(flag: Boolean) { geolocationEnabled = flag }
    open fun getGeolocationEnabled(): Boolean = geolocationEnabled

    // ---- 链式 set 方法（app 用 Java 式 setXxx() 调用；返回值改宿主类规避与属性 setter 的 JVM 冲突）——Nova 注 ----
    fun setJavaScriptEnabled(v: Boolean): WebSettings = apply { javaScriptEnabled = v }
    fun setDomStorageEnabled(v: Boolean): WebSettings = apply { domStorageEnabled = v }
    fun setAllowFileAccess(v: Boolean): WebSettings = apply { allowFileAccess = v }
    fun setAllowContentAccess(v: Boolean): WebSettings = apply { allowContentAccess = v }
    fun setSupportZoom(v: Boolean): WebSettings = apply { supportZoom = v }
    fun setSupportMultipleWindows(v: Boolean): WebSettings = apply { supportMultipleWindows = v }
    fun setUserAgentString(v: String?): WebSettings = apply { userAgentString = v }
    fun setCacheMode(v: Int): WebSettings = apply { cacheMode = v }
    fun setTextZoom(v: Int): WebSettings = apply { textZoom = v }
    fun setMediaPlaybackRequiresUserGesture(v: Boolean): WebSettings = apply { mediaPlaybackRequiresUserGesture = v }
    fun setBuiltInZoomControls(v: Boolean): WebSettings = apply { builtInZoomControls = v }
    fun setDisplayZoomControls(v: Boolean): WebSettings = apply { displayZoomControls = v }
    fun setUseWideViewPort(v: Boolean): WebSettings = apply { useWideViewPort = v }
    fun setLoadWithOverviewMode(v: Boolean): WebSettings = apply { loadWithOverviewMode = v }
    fun setDatabaseEnabled(v: Boolean): WebSettings = apply { databaseEnabled = v }
    fun setJavaScriptCanOpenWindowsAutomatically(v: Boolean): WebSettings = apply { javaScriptCanOpenWindowsAutomatically = v }
    fun setLoadsImagesAutomatically(v: Boolean): WebSettings = apply { loadsImagesAutomatically = v }
    fun setBlockNetworkImage(v: Boolean): WebSettings = apply { blockNetworkImage = v }
    fun setBlockNetworkLoads(v: Boolean): WebSettings = apply { blockNetworkLoads = v }
    fun setDefaultTextEncodingName(v: String?): WebSettings = apply { defaultTextEncodingName = v }
    fun setMixedContentMode(v: Int): WebSettings = apply { mixedContentMode = v }
    fun setDefaultFontSize(v: Int): WebSettings = apply { defaultFontSize = v }
    fun setMinimumFontSize(v: Int): WebSettings = apply { minimumFontSize = v }
    fun setSafeBrowsingEnabled(v: Boolean): WebSettings = apply { safeBrowsingEnabled = v }
    fun setAutoZoomEnabled(v: Boolean): WebSettings = apply { autoZoomEnabled = v }
    fun setAllowUniversalAccessFromFileURLs(v: Boolean): WebSettings = apply { allowUniversalAccessFromFileURLs = v }
    fun setAllowFileAccessFromFileURLs(v: Boolean): WebSettings = apply { allowFileAccessFromFileURLs = v }

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

/** android.webkit.WebView：桌面无 WebView 渲染，仅骨架。继承 FrameLayout 以支持 addView/removeAllViews 等容器操作。 */
open class WebView : android.widget.FrameLayout {
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
    /** WebView.loadUrl(url, headers) 重载。——Nova 注 */
    open fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {}
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
    /** WebView.copyBackForwardList：浏览历史栈。桌面 webview 为占位实现。——Nova 注 */
    open fun copyBackForwardList(): WebBackForwardList = WebBackForwardList()
    open fun canGoForward(): Boolean = false
    open fun clearHistory() {}
    open fun clearCache(includeDiskFiles: Boolean) {}
    open fun clearFormData() {}
    open fun clearSslPreferences() {}
    open val url: String? get() = null
    open val originalUrl: String? get() = null
    open val title: String? get() = null
    open val scale: Float get() = 1f
    open fun onResume() {}
    open fun onPause() {}
    open fun pauseTimers() {}
    open fun resumeTimers() {}
    open fun destroy() {}
}
