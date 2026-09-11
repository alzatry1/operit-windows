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


/** android.webkit.WebChromeClient 已迁移至 Java 版（java/android/webkit/WebChromeClient.java）。——Nova 注 */
