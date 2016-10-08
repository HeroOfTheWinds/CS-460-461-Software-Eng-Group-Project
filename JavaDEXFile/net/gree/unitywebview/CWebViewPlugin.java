package net.gree.unitywebview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;

public class CWebViewPlugin {
    private static FrameLayout layout;
    private boolean canGoBack;
    private boolean canGoForward;
    private WebView mWebView;
    private CWebViewPluginInterface mWebViewPlugin;

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.1 */
    class C11571 implements Runnable {
        final /* synthetic */ Activity val$a;
        final /* synthetic */ String val$gameObject;
        final /* synthetic */ CWebViewPlugin val$self;
        final /* synthetic */ boolean val$transparent;

        /* renamed from: net.gree.unitywebview.CWebViewPlugin.1.1 */
        class C11561 extends WebViewClient {
            final /* synthetic */ WebView val$webView;

            C11561(WebView webView) {
                this.val$webView = webView;
            }

            public void onPageFinished(WebView webView, String str) {
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
                CWebViewPlugin.this.mWebViewPlugin.call("CallOnLoaded", str);
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                this.val$webView.loadUrl("about:blank");
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
                CWebViewPlugin.this.mWebViewPlugin.call("CallOnError", i + "\t" + str + "\t" + str2);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str.startsWith("http://") || str.startsWith("https://") || str.startsWith("file://") || str.startsWith("javascript:")) {
                    return false;
                }
                if (str.startsWith("unity:")) {
                    CWebViewPlugin.this.mWebViewPlugin.call("CallFromJS", str.substring(6));
                    return true;
                }
                webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }
        }

        C11571(Activity activity, CWebViewPlugin cWebViewPlugin, String str, boolean z) {
            this.val$a = activity;
            this.val$self = cWebViewPlugin;
            this.val$gameObject = str;
            this.val$transparent = z;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView == null) {
                View webView = new WebView(this.val$a);
                webView.setVisibility(8);
                webView.setFocusable(true);
                webView.setFocusableInTouchMode(true);
                webView.setWebChromeClient(new WebChromeClient());
                CWebViewPlugin.this.mWebViewPlugin = new CWebViewPluginInterface(this.val$self, this.val$gameObject);
                webView.setWebViewClient(new C11561(webView));
                webView.addJavascriptInterface(CWebViewPlugin.this.mWebViewPlugin, "Unity");
                WebSettings settings = webView.getSettings();
                settings.setSupportZoom(false);
                settings.setJavaScriptEnabled(true);
                if (VERSION.SDK_INT >= 16) {
                    settings.setAllowUniversalAccessFromFileURLs(true);
                }
                settings.setDatabaseEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setDatabasePath(webView.getContext().getDir("databases", 0).getPath());
                settings.setUseWideViewPort(true);
                if (this.val$transparent) {
                    webView.setBackgroundColor(0);
                }
                if (CWebViewPlugin.layout == null) {
                    CWebViewPlugin.layout = new FrameLayout(this.val$a);
                    this.val$a.addContentView(CWebViewPlugin.layout, new LayoutParams(-1, -1));
                    CWebViewPlugin.layout.setFocusable(true);
                    CWebViewPlugin.layout.setFocusableInTouchMode(true);
                }
                CWebViewPlugin.layout.addView(webView, new FrameLayout.LayoutParams(-1, -1, 0));
                CWebViewPlugin.this.mWebView = webView;
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.2 */
    class C11582 implements OnGlobalLayoutListener {
        final /* synthetic */ Activity val$a;
        final /* synthetic */ View val$activityRootView;
        final /* synthetic */ String val$gameObject;

        C11582(View view, Activity activity, String str) {
            this.val$activityRootView = view;
            this.val$a = activity;
            this.val$gameObject = str;
        }

        public void onGlobalLayout() {
            Rect rect = new Rect();
            this.val$activityRootView.getWindowVisibleDisplayFrame(rect);
            Display defaultDisplay = this.val$a.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            if (this.val$activityRootView.getRootView().getHeight() - (rect.bottom - rect.top) > point.y / 3) {
                UnityPlayer.UnitySendMessage(this.val$gameObject, "SetKeyboardVisible", "true");
            } else {
                UnityPlayer.UnitySendMessage(this.val$gameObject, "SetKeyboardVisible", "false");
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.3 */
    class C11593 implements Runnable {
        C11593() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.layout.removeView(CWebViewPlugin.this.mWebView);
                CWebViewPlugin.this.mWebView = null;
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.4 */
    class C11604 implements Runnable {
        final /* synthetic */ String val$url;

        C11604(String str) {
            this.val$url = str;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.loadUrl(this.val$url);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.5 */
    class C11615 implements Runnable {
        final /* synthetic */ String val$js;

        C11615(String str) {
            this.val$js = str;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.loadUrl("javascript:" + this.val$js);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.6 */
    class C11626 implements Runnable {
        C11626() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.goBack();
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.7 */
    class C11637 implements Runnable {
        C11637() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.goForward();
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.8 */
    class C11648 implements Runnable {
        final /* synthetic */ FrameLayout.LayoutParams val$params;

        C11648(FrameLayout.LayoutParams layoutParams) {
            this.val$params = layoutParams;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.setLayoutParams(this.val$params);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.9 */
    class C11659 implements Runnable {
        final /* synthetic */ boolean val$visibility;

        C11659(boolean z) {
            this.val$visibility = z;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                if (this.val$visibility) {
                    CWebViewPlugin.this.mWebView.setVisibility(0);
                    CWebViewPlugin.layout.requestFocus();
                    CWebViewPlugin.this.mWebView.requestFocus();
                    return;
                }
                CWebViewPlugin.this.mWebView.setVisibility(8);
            }
        }
    }

    static {
        layout = null;
    }

    public void Destroy() {
        UnityPlayer.currentActivity.runOnUiThread(new C11593());
    }

    public void EvaluateJS(String str) {
        UnityPlayer.currentActivity.runOnUiThread(new C11615(str));
    }

    public void GoBack() {
        UnityPlayer.currentActivity.runOnUiThread(new C11626());
    }

    public void GoForward() {
        UnityPlayer.currentActivity.runOnUiThread(new C11637());
    }

    public void Init(String str, boolean z) {
        Activity activity = UnityPlayer.currentActivity;
        activity.runOnUiThread(new C11571(activity, this, str, z));
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new C11582(rootView, activity, str));
    }

    public boolean IsInitialized() {
        return this.mWebView != null;
    }

    public void LoadURL(String str) {
        UnityPlayer.currentActivity.runOnUiThread(new C11604(str));
    }

    public void SetMargins(int i, int i2, int i3, int i4) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1, 0);
        layoutParams.setMargins(i, i2, i3, i4);
        UnityPlayer.currentActivity.runOnUiThread(new C11648(layoutParams));
    }

    public void SetVisibility(boolean z) {
        UnityPlayer.currentActivity.runOnUiThread(new C11659(z));
    }
}
