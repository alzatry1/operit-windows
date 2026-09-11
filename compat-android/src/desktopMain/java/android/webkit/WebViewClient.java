package android.webkit;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;

/**
 * android.webkit.WebViewClient 的 Java 版（平台类型）。
 * 关键原因：Kotlin override 要求参数可空性完全一致，而 app 代码里
 * 这些回调的可空/非空覆写混用；只有 Java 平台类型能让两者都成立。——Nova 注
 */
public class WebViewClient {

    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { return false; }

    @Deprecated
    public boolean shouldOverrideUrlLoading(WebView view, String url) { return false; }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {}

    public void onPageFinished(WebView view, String url) {}

    public void onLoadResource(WebView view, String url) {}

    public void onPageCommitVisible(WebView view, String url) {}

    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) { return null; }

    @Deprecated
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) { return null; }

    @Deprecated
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {}

    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {}

    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {}

    public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {}

    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {}

    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {}

    public void onFormResubmission(WebView view, Message dontResend, Message resend) {}

    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {}

    public void onScaleChanged(WebView view, float oldScale, float newScale) {}

    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) { return false; }

    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {}

    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) { return false; }

    public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {}

    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {}

    public static final int SAFE_BROWSING_THREAT_UNKNOWN = 0;
    public static final int SAFE_BROWSING_THREAT_MALWARE = 1;
    public static final int SAFE_BROWSING_THREAT_PHISHING = 2;
    public static final int SAFE_BROWSING_THREAT_UNWANTED_SOFTWARE = 3;
    public static final int SAFE_BROWSING_THREAT_BILLING = 4;
}
