package android.webkit

import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.KeyEvent
import android.view.View
import java.io.InputStream

/**
 * android.webkit 客户端回调垫片（P3-B2 新增）。
 * WebViewClient / WebChromeClient 全 open 钩子，桌面无渲染宿主，默认返回值与 Android 一致。
 * 注意：钩子参数可空性严格对齐 app 覆写点（Kotlin 覆写要求参数类型完全一致）。
 */

/** android.webkit.WebResourceRequest。 */
interface WebResourceRequest {
    val url: Uri
    val isForMainFrame: Boolean
    val isRedirect: Boolean
    val hasGesture: Boolean
    val method: String
    val requestHeaders: Map<String, String>?
}

/** android.webkit.WebResourceError。 */
interface WebResourceError {
    val errorCode: Int
    val description: CharSequence?
    val failingUrl: String?
}

/** android.webkit.WebResourceResponse。 */
open class WebResourceResponse {
    var mimeType: String?
    var encoding: String?
    var data: InputStream?
    var statusCode: Int = 200
    var reasonPhrase: String? = "OK"
    var responseHeaders: Map<String, String>? = null

    constructor(mimeType: String?, encoding: String?) {
        this.mimeType = mimeType
        this.encoding = encoding
        this.data = null
    }

    constructor(mimeType: String?, encoding: String?, data: InputStream?) {
        this.mimeType = mimeType
        this.encoding = encoding
        this.data = data
    }

    constructor(
        mimeType: String?, encoding: String?, statusCode: Int,
        reasonPhrase: String?, responseHeaders: Map<String, String>?, data: InputStream?,
    ) {
        this.mimeType = mimeType
        this.encoding = encoding
        this.statusCode = statusCode
        this.reasonPhrase = reasonPhrase
        this.responseHeaders = responseHeaders
        this.data = data
    }
}

/** android.webkit.WebViewClient 已迁移至 Java 版（java/android/webkit/WebViewClient.java），平台类型支持可空/非空 override。——Nova 注 */


/** android.webkit.WebChromeClient：全 open 钩子。 */
open class WebChromeClient {

    interface CustomViewCallback {
        fun onCustomViewHidden()
    }

    /** android.webkit.WebChromeClient.FileChooserParams。 */
    open class FileChooserParams {
        open val mode: Int = MODE_OPEN
        open val acceptTypes: Array<String> = emptyArray()
        open val isCaptureEnabled: Boolean = false
        open val acceptMimeTypes: Array<String> get() = acceptTypes
        open val filenameHint: String? = null
        open val title: CharSequence? = null

        open fun createIntent(): android.content.Intent = android.content.Intent()

        companion object {
            const val MODE_OPEN = 0
            const val MODE_OPEN_MULTIPLE = 1
            const val MODE_SAVE = 2

            @JvmStatic
            fun parseResult(resultCode: Int, data: android.content.Intent?): Array<Uri>? = null
        }
    }

    open fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean = false

    open fun onCloseWindow(window: WebView?) {}

    open fun onProgressChanged(view: WebView?, newProgress: Int) {}

    open fun onReceivedTitle(view: WebView?, title: String?) {}

    open fun onReceivedIcon(view: WebView?, icon: Bitmap?) {}

    open fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {}

    open fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean = false

    @Deprecated("deprecated")
    open fun onConsoleMessage(message: String?, lineNumber: Int, sourceID: String?) {}

    open fun onShowCustomView(view: View, callback: CustomViewCallback) {}

    open fun onHideCustomView() {}

    open fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {}

    open fun onGeolocationPermissionsHidePrompt() {}

    open fun onPermissionRequest(request: PermissionRequest?) {}

    open fun onPermissionRequestCanceled(request: PermissionRequest?) {}

    open fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean = false

    open fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean = false

    open fun onJsPrompt(
        view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?,
    ): Boolean = false

    open fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean = false

    open fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?,
    ): Boolean = false

    open fun getDefaultVideoPoster(): Bitmap? = null

    open fun getVideoLoadingProgressView(): View? = null

    open fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {}
}
