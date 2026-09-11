package android.webkit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;

/**
 * android.webkit.WebChromeClient 的 Java 版（平台类型）。
 * 同 WebViewClient：让 app 的可空/非空 override 都成立。——Nova 注
 */
public class WebChromeClient {

    public interface CustomViewCallback {
        void onCustomViewHidden();
    }

    /** android.webkit.WebChromeClient.FileChooserParams。 */
    public static class FileChooserParams {
        public int getMode() { return MODE_OPEN; }
        public String[] getAcceptTypes() { return new String[0]; }
        public boolean isCaptureEnabled() { return false; }
        public String[] getAcceptMimeTypes() { return getAcceptTypes(); }
        public String getFilenameHint() { return null; }
        public CharSequence getTitle() { return null; }

        public android.content.Intent createIntent() { return new android.content.Intent(); }

        public static final int MODE_OPEN = 0;
        public static final int MODE_OPEN_MULTIPLE = 1;
        public static final int MODE_SAVE = 2;

        public static Uri[] parseResult(int resultCode, android.content.Intent data) { return null; }
    }

    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) { return false; }

    public void onCloseWindow(WebView window) {}

    public void onProgressChanged(WebView view, int newProgress) {}

    public void onReceivedTitle(WebView view, String title) {}

    public void onReceivedIcon(WebView view, Bitmap icon) {}

    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {}

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) { return false; }

    @Deprecated
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {}

    public void onShowCustomView(View view, CustomViewCallback callback) {}

    public void onHideCustomView() {}

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {}

    public void onGeolocationPermissionsHidePrompt() {}

    public void onPermissionRequest(PermissionRequest request) {}

    public void onPermissionRequestCanceled(PermissionRequest request) {}

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) { return false; }

    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) { return false; }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) { return false; }

    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) { return false; }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) { return false; }

    public Bitmap getDefaultVideoPoster() { return null; }

    public View getVideoLoadingProgressView() { return null; }

    public void getVisitedHistory(ValueCallback<String[]> callback) {}
}
