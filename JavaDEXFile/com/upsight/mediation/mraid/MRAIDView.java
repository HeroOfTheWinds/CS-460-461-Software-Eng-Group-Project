package com.upsight.mediation.mraid;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.location.places.Place;
import com.squareup.otto.Bus;
import com.upsight.mediation.mraid.internal.MRAIDHtmlProcessor;
import com.upsight.mediation.mraid.internal.MRAIDLog;
import com.upsight.mediation.mraid.internal.MRAIDNativeFeatureManager;
import com.upsight.mediation.mraid.internal.MRAIDParser;
import com.upsight.mediation.mraid.properties.MRAIDOrientationProperties;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Billing.Validation;
import com.voxelbusters.nativeplugins.defines.Keys.Mime;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import spacemadness.com.lunarconsole.BuildConfig;

@SuppressLint({"ViewConstructor"})
public class MRAIDView extends RelativeLayout {
    private static final int CLOSE_REGION_SIZE = 50;
    private static final int STATE_DEFAULT = 1;
    private static final int STATE_EXPANDED = 2;
    private static final int STATE_HIDDEN = 4;
    private static final int STATE_LOADING = 0;
    private static final int STATE_RESIZED = 3;
    private static final String TAG = "MRAIDView";
    public static final String VERSION = "1.0";
    private int backgroundColor;
    private String baseUrl;
    private ImageButton closeRegion;
    private int contentViewTop;
    private Context context;
    private Rect currentPosition;
    private WebView currentWebView;
    private Rect defaultPosition;
    private DisplayMetrics displayMetrics;
    private RelativeLayout expandedView;
    private GestureDetector gestureDetector;
    private Handler handler;
    private String htmlData;
    private boolean isActionBarShowing;
    private boolean isClosing;
    private boolean isExpandingFromDefault;
    private boolean isExpandingPart2;
    private boolean isForcingFullScreen;
    private final boolean isInterstitial;
    private boolean isLaidOut;
    private boolean isPageFinished;
    private boolean isViewable;
    private MRAIDViewListener listener;
    private Size maxSize;
    private String mraidJs;
    private MRAIDWebChromeClient mraidWebChromeClient;
    private MRAIDWebViewClient mraidWebViewClient;
    private MRAIDNativeFeatureListener nativeFeatureListener;
    private MRAIDNativeFeatureManager nativeFeatureManager;
    private MRAIDOrientationProperties orientationProperties;
    private int origTitleBarVisibility;
    private MRAIDResizeProperties resizeProperties;
    private RelativeLayout resizedView;
    private int rotateMode;
    private Size screenSize;
    private int state;
    private View titleBar;
    private boolean useCustomClose;
    private WebView webView;
    private WebView webViewPart2;

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.1 */
    class C10071 extends SimpleOnGestureListener {
        C10071() {
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return true;
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.2 */
    class C10082 extends WebView {
        private static final String TAG = "MRAIDView-WebView";

        C10082(Context context) {
            super(context);
        }

        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            MRAIDLog.m249v(TAG, "onConfigurationChanged " + (configuration.orientation == MRAIDView.STATE_DEFAULT ? "portrait" : "landscape"));
            if (MRAIDView.this.isInterstitial) {
                ((Activity) MRAIDView.this.context).getWindowManager().getDefaultDisplay().getMetrics(MRAIDView.this.displayMetrics);
            }
        }

        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            MRAIDView.this.onLayoutWebView(this, z, i, i2, i3, i4);
        }

        protected void onVisibilityChanged(View view, int i) {
            super.onVisibilityChanged(view, i);
            MRAIDLog.m249v(TAG, "onVisibilityChanged " + MRAIDView.getVisibilityString(i));
            if (MRAIDView.this.isInterstitial) {
                MRAIDView.this.setViewable(i);
            }
        }

        protected void onWindowVisibilityChanged(int i) {
            super.onWindowVisibilityChanged(i);
            int visibility = getVisibility();
            MRAIDLog.m249v(TAG, "onWindowVisibilityChanged " + MRAIDView.getVisibilityString(i) + " (actual " + MRAIDView.getVisibilityString(visibility) + ")");
            if (MRAIDView.this.isInterstitial) {
                MRAIDView.this.setViewable(visibility);
            }
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.3 */
    class C10093 implements OnTouchListener {
        C10093() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MRAIDView.STATE_LOADING /*0*/:
                case MRAIDView.STATE_DEFAULT /*1*/:
                    if (!view.hasFocus()) {
                        view.requestFocus();
                        break;
                    }
                    break;
            }
            return false;
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.4 */
    class C10104 implements Runnable {
        final /* synthetic */ boolean val$sendCallback;

        C10104(boolean z) {
            this.val$sendCallback = z;
        }

        public void run() {
            if (MRAIDView.this.state == 0) {
                return;
            }
            if ((MRAIDView.this.state == MRAIDView.STATE_DEFAULT && !MRAIDView.this.isInterstitial) || MRAIDView.this.state == MRAIDView.STATE_HIDDEN) {
                return;
            }
            if (MRAIDView.this.state == MRAIDView.STATE_DEFAULT || MRAIDView.this.state == MRAIDView.STATE_EXPANDED) {
                MRAIDView.this.closeFromExpanded(this.val$sendCallback);
            } else if (MRAIDView.this.state == MRAIDView.STATE_RESIZED) {
                MRAIDView.this.closeFromResized(this.val$sendCallback);
            }
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.5 */
    class C10115 implements Runnable {
        C10115() {
        }

        public void run() {
            if (MRAIDView.this.nativeFeatureListener != null) {
                MRAIDView.this.nativeFeatureListener.mraidRewardComplete();
            }
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.6 */
    class C10136 implements Runnable {
        final /* synthetic */ String val$finalUrl;

        /* renamed from: com.upsight.mediation.mraid.MRAIDView.6.1 */
        class C10121 implements Runnable {
            final /* synthetic */ String val$content;

            C10121(String str) {
                this.val$content = str;
            }

            public void run() {
                if (MRAIDView.this.state == MRAIDView.STATE_RESIZED) {
                    MRAIDView.this.removeResizeView();
                    MRAIDView.this.addView(MRAIDView.this.webView);
                }
                MRAIDView.this.webView.setWebChromeClient(null);
                MRAIDView.this.webView.setWebViewClient(null);
                MRAIDView.this.webViewPart2 = MRAIDView.this.createWebView();
                MRAIDView.this.injectMraidJs(MRAIDView.this.webViewPart2);
                MRAIDView.this.webViewPart2.loadDataWithBaseURL(MRAIDView.this.baseUrl, this.val$content, Mime.HTML_TEXT, "UTF-8", null);
                MRAIDView.this.currentWebView = MRAIDView.this.webViewPart2;
                MRAIDView.this.isExpandingPart2 = true;
                MRAIDView.this.expandHelper();
            }
        }

        C10136(String str) {
            this.val$finalUrl = str;
        }

        public void run() {
            Object access$1300 = MRAIDView.this.getStringFromUrl(this.val$finalUrl);
            if (TextUtils.isEmpty(access$1300)) {
                MRAIDLog.m246i("Could not load part 2 expanded content for URL: " + this.val$finalUrl);
            } else {
                ((Activity) MRAIDView.this.context).runOnUiThread(new C10121(access$1300));
            }
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.7 */
    class C10147 implements Runnable {
        C10147() {
        }

        public void run() {
            MRAIDView.this.fireStateChangeEvent();
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.8 */
    class C10158 implements Runnable {
        final /* synthetic */ boolean val$sendCallback;

        C10158(boolean z) {
            this.val$sendCallback = z;
        }

        public void run() {
            MRAIDView.this.fireStateChangeEvent();
            if (MRAIDView.this.listener != null && this.val$sendCallback) {
                MRAIDView.this.listener.mraidViewClose(MRAIDView.this);
            }
        }
    }

    /* renamed from: com.upsight.mediation.mraid.MRAIDView.9 */
    class C10169 implements Runnable {
        final /* synthetic */ boolean val$sendCallback;

        C10169(boolean z) {
            this.val$sendCallback = z;
        }

        public void run() {
            MRAIDView.this.fireStateChangeEvent();
            if (MRAIDView.this.listener != null && this.val$sendCallback) {
                MRAIDView.this.listener.mraidViewClose(MRAIDView.this);
            }
        }
    }

    private class MRAIDWebChromeClient extends WebChromeClient {
        private MRAIDWebChromeClient() {
        }

        private boolean handlePopups(JsResult jsResult) {
            jsResult.cancel();
            return true;
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (!consoleMessage.message().contains("Uncaught ReferenceError")) {
                MRAIDLog.m247i("JS console", consoleMessage.message() + (consoleMessage.sourceId() == null ? BuildConfig.FLAVOR : " at " + consoleMessage.sourceId()) + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + consoleMessage.lineNumber());
            }
            return true;
        }

        public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            MRAIDLog.m249v("JS alert", str2);
            return handlePopups(jsResult);
        }

        public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
            MRAIDLog.m249v("JS confirm", str2);
            return handlePopups(jsResult);
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            MRAIDLog.m249v("JS prompt", str2);
            return handlePopups(jsPromptResult);
        }
    }

    private class MRAIDWebViewClient extends WebViewClient {

        /* renamed from: com.upsight.mediation.mraid.MRAIDView.MRAIDWebViewClient.1 */
        class C10171 implements Runnable {
            C10171() {
            }

            public void run() {
                MRAIDView.this.injectJavaScript("mraid.setPlacementType('" + (MRAIDView.this.isInterstitial ? "interstitial" : "inline") + "');");
                MRAIDView.this.setSupportedServices();
                MRAIDView.this.setScreenSize();
                MRAIDView.this.setDefaultPosition();
                MRAIDLog.m249v(MRAIDView.TAG, "calling fireStateChangeEvent 2");
                MRAIDView.this.fireStateChangeEvent();
                MRAIDView.this.fireReadyEvent();
                if (MRAIDView.this.isViewable) {
                    MRAIDView.this.fireViewableChangeEvent();
                }
            }
        }

        private MRAIDWebViewClient() {
        }

        public void onPageFinished(WebView webView, String str) {
            MRAIDLog.m249v(MRAIDView.TAG, "onPageFinished: " + str);
            super.onPageFinished(webView, str);
            if (MRAIDView.this.state == 0) {
                MRAIDView.this.isPageFinished = true;
                MRAIDView.this.injectJavaScript("mraid.setPlacementType('" + (MRAIDView.this.isInterstitial ? "interstitial" : "inline") + "');");
                MRAIDView.this.setSupportedServices();
                if (MRAIDView.this.isLaidOut) {
                    MRAIDView.this.setScreenSize();
                    MRAIDView.this.setMaxSize();
                    MRAIDView.this.setCurrentPosition();
                    MRAIDView.this.setDefaultPosition();
                    MRAIDView.this.state = MRAIDView.STATE_DEFAULT;
                    MRAIDView.this.fireStateChangeEvent();
                    MRAIDView.this.fireReadyEvent();
                    if (MRAIDView.this.isViewable) {
                        MRAIDView.this.fireViewableChangeEvent();
                    }
                }
                if (MRAIDView.this.listener != null) {
                    MRAIDView.this.listener.mraidViewLoaded(MRAIDView.this);
                }
            }
            if (MRAIDView.this.isExpandingPart2) {
                MRAIDView.this.isExpandingPart2 = false;
                MRAIDView.this.handler.post(new C10171());
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            MRAIDLog.m249v(MRAIDView.TAG, "onReceivedError: " + str);
            super.onReceivedError(webView, i, str, str2);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            MRAIDLog.m249v(MRAIDView.TAG, "shouldOverrideUrlLoading: " + str);
            if (str.startsWith("mraid://")) {
                MRAIDView.this.parseCommandUrl(str);
                return true;
            } else if (str.startsWith("sms:")) {
                if (MRAIDView.this.nativeFeatureListener == null || !MRAIDView.this.nativeFeatureManager.isSmsSupported()) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureSendSms(str);
                return true;
            } else if (str.startsWith("tel:")) {
                if (MRAIDView.this.nativeFeatureListener == null || !MRAIDView.this.nativeFeatureManager.isTelSupported()) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureCallTel(str);
                return true;
            } else if (!str.startsWith("market:")) {
                return super.shouldOverrideUrlLoading(webView, str);
            } else {
                if (MRAIDView.this.nativeFeatureListener == null) {
                    return true;
                }
                MRAIDView.this.nativeFeatureListener.mraidNativeFeatureOpenMarket(str);
                return true;
            }
        }
    }

    private final class Size {
        public int height;
        public int width;

        private Size() {
        }
    }

    protected MRAIDView(Context context, String str, String str2, int i, String[] strArr, MRAIDViewListener mRAIDViewListener, MRAIDNativeFeatureListener mRAIDNativeFeatureListener, boolean z) {
        super(context);
        this.mraidJs = MRaidJS.value;
        this.context = context;
        this.baseUrl = str;
        this.isInterstitial = z;
        this.backgroundColor = i;
        this.state = STATE_LOADING;
        this.isViewable = false;
        this.useCustomClose = false;
        this.orientationProperties = new MRAIDOrientationProperties();
        this.resizeProperties = new MRAIDResizeProperties();
        this.nativeFeatureManager = new MRAIDNativeFeatureManager(context, Arrays.asList(strArr));
        this.listener = mRAIDViewListener;
        this.nativeFeatureListener = mRAIDNativeFeatureListener;
        this.displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(this.displayMetrics);
        this.currentPosition = new Rect();
        this.defaultPosition = new Rect();
        this.maxSize = new Size();
        this.screenSize = new Size();
        this.gestureDetector = new GestureDetector(getContext(), new C10071());
        this.handler = new Handler(Looper.getMainLooper());
        this.mraidWebChromeClient = new MRAIDWebChromeClient();
        this.mraidWebViewClient = new MRAIDWebViewClient();
        this.webView = createWebView();
        this.currentWebView = this.webView;
        addView(this.webView);
        this.htmlData = MRAIDHtmlProcessor.processRawHtml(this.mraidJs + str2);
        this.webView.loadDataWithBaseURL(str, this.htmlData, Mime.HTML_TEXT, "UTF-8", null);
    }

    public MRAIDView(Context context, String str, String str2, String[] strArr, MRAIDViewListener mRAIDViewListener, MRAIDNativeFeatureListener mRAIDNativeFeatureListener) {
        this(context, str, str2, STATE_LOADING, strArr, mRAIDViewListener, mRAIDNativeFeatureListener, false);
    }

    private void addCloseRegion(View view) {
        this.closeRegion = new ImageButton(this.context);
        this.closeRegion.setBackgroundColor(STATE_LOADING);
        this.closeRegion.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MRAIDView.this.close();
            }
        });
        this.closeRegion.setVisibility(STATE_HIDDEN);
        this.closeRegion.postDelayed(new Runnable() {
            public void run() {
                MRAIDView.this.closeRegion.setVisibility(MRAIDView.STATE_LOADING);
            }
        }, 1000);
        if (view == this.expandedView && !this.useCustomClose) {
            Drawable drawableForImage = MRaidDrawables.getDrawableForImage(this.context, "/assets/drawable/close_button_normal.png", "close_button_normal", ViewCompat.MEASURED_STATE_MASK);
            Drawable drawableForImage2 = MRaidDrawables.getDrawableForImage(this.context, "/assets/drawable/close_button_pressed.png", "close_button_pressed", ViewCompat.MEASURED_STATE_MASK);
            Drawable stateListDrawable = new StateListDrawable();
            int[] iArr = new int[STATE_DEFAULT];
            iArr[STATE_LOADING] = -16842919;
            stateListDrawable.addState(iArr, drawableForImage);
            int[] iArr2 = new int[STATE_DEFAULT];
            iArr2[STATE_LOADING] = 16842919;
            stateListDrawable.addState(iArr2, drawableForImage2);
            this.closeRegion.setImageDrawable(stateListDrawable);
            this.closeRegion.setScaleType(ScaleType.CENTER_CROP);
        }
        ((ViewGroup) view).addView(this.closeRegion);
    }

    private void applyOrientationProperties() {
    }

    private void calculateMaxSize() {
        Rect rect = new Rect();
        Window window = ((Activity) this.context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        MRAIDLog.m249v(TAG, "calculateMaxSize frame [" + rect.left + "," + rect.top + "][" + rect.right + "," + rect.bottom + "] (" + rect.width() + "x" + rect.height() + ")");
        int i = rect.top;
        this.contentViewTop = window.findViewById(16908290).getTop();
        int i2 = this.contentViewTop;
        MRAIDLog.m249v(TAG, "calculateMaxSize statusHeight " + i);
        MRAIDLog.m249v(TAG, "calculateMaxSize titleHeight " + (i2 - i));
        MRAIDLog.m249v(TAG, "calculateMaxSize contentViewTop " + this.contentViewTop);
        i2 = rect.width();
        int i3 = this.screenSize.height - this.contentViewTop;
        MRAIDLog.m249v(TAG, "calculateMaxSize max size " + i2 + "x" + i3);
        if (i2 != this.maxSize.width || i3 != this.maxSize.height) {
            this.maxSize.width = i2;
            this.maxSize.height = i3;
            if (this.isPageFinished) {
                setMaxSize();
            }
        }
    }

    private void calculatePosition(boolean z) {
        int[] iArr = new int[STATE_EXPANDED];
        View view = z ? this.currentWebView : this;
        String str = z ? "current" : Bus.DEFAULT_IDENTIFIER;
        view.getLocationOnScreen(iArr);
        int i = iArr[STATE_LOADING];
        int i2 = iArr[STATE_DEFAULT];
        MRAIDLog.m249v(TAG, "calculatePosition " + str + " locationOnScreen [" + i + "," + i2 + "]");
        MRAIDLog.m249v(TAG, "calculatePosition " + str + " contentViewTop " + this.contentViewTop);
        if (i2 < this.contentViewTop) {
            MRAIDLog.m249v(TAG, "calculatePosition " + str + " y < contentViewTop, returning");
            return;
        }
        i2 -= this.contentViewTop;
        int width = view.getWidth();
        int height = view.getHeight();
        MRAIDLog.m249v(TAG, "calculatePosition " + str + " position [" + i + "," + i2 + "] (" + width + "x" + height + ")");
        Rect rect = z ? this.currentPosition : this.defaultPosition;
        if (i != rect.left || i2 != rect.top || width != rect.width() || height != rect.height()) {
            if (z) {
                this.currentPosition = new Rect(i, i2, width + i, height + i2);
            } else {
                this.defaultPosition = new Rect(i, i2, width + i, height + i2);
            }
            if (!this.isPageFinished) {
                return;
            }
            if (z) {
                setCurrentPosition();
            } else {
                setDefaultPosition();
            }
        }
    }

    private void calculateScreenSize() {
        Object obj = STATE_DEFAULT;
        if (getResources().getConfiguration().orientation != STATE_DEFAULT) {
            obj = null;
        }
        MRAIDLog.m249v(TAG, "calculateScreenSize orientation " + (obj != null ? "portrait" : "landscape"));
        int i = this.displayMetrics.widthPixels;
        int i2 = this.displayMetrics.heightPixels;
        MRAIDLog.m249v(TAG, "calculateScreenSize screen size " + i + "x" + i2);
        if (i != this.screenSize.width || i2 != this.screenSize.height) {
            this.screenSize.width = i;
            this.screenSize.height = i2;
            if (this.isPageFinished) {
                setScreenSize();
            }
        }
    }

    private void close() {
        close(true);
    }

    private void close(boolean z) {
        MRAIDLog.m249v("MRAIDView-JS callback", "close " + this.state);
        this.handler.post(new C10104(z));
    }

    private void closeFromExpanded(boolean z) {
        if (this.state == STATE_DEFAULT && this.isInterstitial) {
            this.state = STATE_HIDDEN;
            pauseWebView(this.currentWebView);
        } else if (this.state == STATE_EXPANDED || this.state == STATE_RESIZED) {
            this.state = STATE_DEFAULT;
        }
        this.isClosing = true;
        this.expandedView.removeAllViews();
        ((FrameLayout) ((Activity) this.context).findViewById(16908290)).removeView(this.expandedView);
        this.expandedView = null;
        this.closeRegion = null;
        restoreOriginalOrientation();
        restoreOriginalScreenState();
        if (this.webViewPart2 == null) {
            addView(this.webView);
        } else {
            this.webViewPart2.setWebChromeClient(null);
            this.webViewPart2.setWebViewClient(null);
            this.webViewPart2 = null;
            this.webView.setWebChromeClient(this.mraidWebChromeClient);
            this.webView.setWebViewClient(this.mraidWebViewClient);
            this.currentWebView = this.webView;
        }
        this.handler.post(new C10158(z));
    }

    private void closeFromResized(boolean z) {
        this.state = STATE_DEFAULT;
        this.isClosing = true;
        removeResizeView();
        addView(this.webView);
        this.handler.post(new C10169(z));
    }

    private void createCalendarEvent(String str) {
        MRAIDLog.m249v("MRAIDView-JS callback", "createCalendarEvent " + str);
        if (this.nativeFeatureListener != null) {
            this.nativeFeatureListener.mraidNativeFeatureCreateCalendarEvent(str);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private WebView createWebView() {
        WebView c10082 = new C10082(this.context);
        c10082.setLayoutParams(new LayoutParams(-1, -1));
        c10082.setBackgroundColor(this.backgroundColor);
        c10082.setScrollContainer(false);
        c10082.setVerticalScrollBarEnabled(false);
        c10082.setHorizontalScrollBarEnabled(false);
        c10082.setScrollBarStyle(33554432);
        c10082.setFocusableInTouchMode(true);
        c10082.setOnTouchListener(new C10093());
        c10082.getSettings().setJavaScriptEnabled(true);
        c10082.getSettings().setDomStorageEnabled(true);
        c10082.getSettings().setDatabaseEnabled(true);
        if (VERSION.SDK_INT < 19) {
            c10082.getSettings().setDatabasePath(getContext().getFilesDir() + c10082.getContext().getPackageName() + "/databases/");
        }
        c10082.setWebChromeClient(this.mraidWebChromeClient);
        c10082.setWebViewClient(this.mraidWebViewClient);
        return c10082;
    }

    private void expand(String str) {
        MRAIDLog.m249v("MRAIDView-JS callback", "expand " + (str != null ? str : "(1-part)"));
        if (this.isInterstitial && this.state != 0) {
            return;
        }
        if (!this.isInterstitial && this.state != STATE_DEFAULT && this.state != STATE_RESIZED) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            if (this.isInterstitial || this.state == STATE_DEFAULT) {
                removeView(this.webView);
            } else if (this.state == STATE_RESIZED) {
                removeResizeView();
            }
            expandHelper();
            return;
        }
        try {
            String decode = URLDecoder.decode(str, "UTF-8");
            if (!(decode.startsWith("http://") || decode.startsWith("https://"))) {
                decode = this.baseUrl + decode;
            }
            new Thread(new C10136(decode), "2-part-content").start();
        } catch (UnsupportedEncodingException e) {
        }
    }

    private void expandHelper() {
        if (!this.isInterstitial) {
            this.state = STATE_EXPANDED;
        }
        applyOrientationProperties();
        forceFullScreen();
        this.expandedView = new RelativeLayout(this.context);
        this.expandedView.addView(this.currentWebView);
        if (!this.useCustomClose) {
            addCloseRegion(this.expandedView);
            setCloseRegionPosition(this.expandedView);
        }
        ((Activity) this.context).addContentView(this.expandedView, new LayoutParams(-1, -1));
        this.isExpandingFromDefault = true;
    }

    private void fireReadyEvent() {
        MRAIDLog.m249v(TAG, "fireReadyEvent");
        injectJavaScript("mraid.fireReadyEvent();");
    }

    @SuppressLint({"DefaultLocale"})
    private void fireStateChangeEvent() {
        MRAIDLog.m249v(TAG, "fireStateChangeEvent");
        String[] strArr = new String[]{"loading", Bus.DEFAULT_IDENTIFIER, "expanded", "resized", "hidden"};
        injectJavaScript("mraid.fireStateChangeEvent('" + strArr[this.state] + "');");
    }

    private void fireViewableChangeEvent() {
        MRAIDLog.m249v(TAG, "fireViewableChangeEvent");
        injectJavaScript("mraid.fireViewableChangeEvent(" + this.isViewable + ");");
    }

    private void forceFullScreen() {
        MRAIDLog.m249v(TAG, "forceFullScreen");
        Activity activity = (Activity) this.context;
        this.origTitleBarVisibility = -9;
        try {
            if (Activity.class.getMethod("getActionBar", new Class[STATE_LOADING]) != null) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    this.isActionBarShowing = actionBar.isShowing();
                    actionBar.hide();
                }
            }
        } catch (Exception e) {
        }
        MRAIDLog.m249v(TAG, "isActionBarShowing " + this.isActionBarShowing);
        MRAIDLog.m249v(TAG, "origTitleBarVisibility " + getVisibilityString(this.origTitleBarVisibility));
        this.isForcingFullScreen = false;
    }

    private static String getOrientationString(int i) {
        switch (i) {
            case rx.android.BuildConfig.VERSION_CODE /*-1*/:
                return "UNSPECIFIED";
            case STATE_LOADING /*0*/:
                return "LANDSCAPE";
            case STATE_DEFAULT /*1*/:
                return "PORTRAIT";
            default:
                return "UNKNOWN";
        }
    }

    private String getStringFromFileUrl(String str) {
        StringBuffer stringBuffer = new StringBuffer(BuildConfig.FLAVOR);
        String[] split = str.split("/");
        if (split[STATE_RESIZED].equals("android_asset")) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.context.getAssets().open(split[STATE_HIDDEN])));
                String readLine = bufferedReader.readLine();
                stringBuffer.append(readLine);
                while (readLine != null) {
                    readLine = bufferedReader.readLine();
                    stringBuffer.append(readLine);
                }
                bufferedReader.close();
            } catch (IOException e) {
                MRAIDLog.m246i("Error fetching file: " + e.getMessage());
            }
            return stringBuffer.toString();
        }
        MRAIDLog.m246i("Unknown location to fetch file content");
        return BuildConfig.FLAVOR;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getStringFromUrl(java.lang.String r10) {
        /*
        r9 = this;
        r2 = 0;
        r0 = "file:///";
        r0 = r10.startsWith(r0);
        if (r0 == 0) goto L_0x000e;
    L_0x0009:
        r0 = r9.getStringFromFileUrl(r10);
    L_0x000d:
        return r0;
    L_0x000e:
        r0 = new java.net.URL;	 Catch:{ IOException -> 0x00dc }
        r0.<init>(r10);	 Catch:{ IOException -> 0x00dc }
        r0 = r0.openConnection();	 Catch:{ IOException -> 0x00dc }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x00dc }
        r1 = r0.getResponseCode();	 Catch:{ IOException -> 0x00dc }
        r3 = "MRAIDView";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00dc }
        r4.<init>();	 Catch:{ IOException -> 0x00dc }
        r5 = "response code ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00dc }
        r4 = r4.append(r1);	 Catch:{ IOException -> 0x00dc }
        r4 = r4.toString();	 Catch:{ IOException -> 0x00dc }
        com.upsight.mediation.mraid.internal.MRAIDLog.m249v(r3, r4);	 Catch:{ IOException -> 0x00dc }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 != r3) goto L_0x00ee;
    L_0x0039:
        r1 = "MRAIDView";
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00dc }
        r3.<init>();	 Catch:{ IOException -> 0x00dc }
        r4 = "getContentLength ";
        r3 = r3.append(r4);	 Catch:{ IOException -> 0x00dc }
        r4 = r0.getContentLength();	 Catch:{ IOException -> 0x00dc }
        r3 = r3.append(r4);	 Catch:{ IOException -> 0x00dc }
        r3 = r3.toString();	 Catch:{ IOException -> 0x00dc }
        com.upsight.mediation.mraid.internal.MRAIDLog.m249v(r1, r3);	 Catch:{ IOException -> 0x00dc }
        r1 = r0.getInputStream();	 Catch:{ IOException -> 0x00dc }
        r3 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
        r3 = new byte[r3];	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r4.<init>();	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
    L_0x0062:
        r5 = r1.read(r3);	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r6 = -1;
        if (r5 == r6) goto L_0x009e;
    L_0x0069:
        r6 = new java.lang.String;	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r7 = 0;
        r6.<init>(r3, r7, r5);	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r4.append(r6);	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        goto L_0x0062;
    L_0x0073:
        r0 = move-exception;
        r8 = r0;
        r0 = r2;
        r2 = r1;
        r1 = r8;
    L_0x0078:
        r3 = "MRAIDView";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d0 }
        r4.<init>();	 Catch:{ all -> 0x00d0 }
        r5 = "getStringFromUrl failed ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x00d0 }
        r1 = r1.getLocalizedMessage();	 Catch:{ all -> 0x00d0 }
        r1 = r4.append(r1);	 Catch:{ all -> 0x00d0 }
        r1 = r1.toString();	 Catch:{ all -> 0x00d0 }
        com.upsight.mediation.mraid.internal.MRAIDLog.m247i(r3, r1);	 Catch:{ all -> 0x00d0 }
        if (r2 == 0) goto L_0x000d;
    L_0x0096:
        r2.close();	 Catch:{ IOException -> 0x009b }
        goto L_0x000d;
    L_0x009b:
        r1 = move-exception;
        goto L_0x000d;
    L_0x009e:
        r2 = r4.toString();	 Catch:{ IOException -> 0x0073, all -> 0x00d9 }
        r3 = "MRAIDView";
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r4.<init>();	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r5 = "getStringFromUrl ok, length=";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r5 = r2.length();	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        com.upsight.mediation.mraid.internal.MRAIDLog.m249v(r3, r4);	 Catch:{ IOException -> 0x00e0, all -> 0x00d9 }
        r8 = r1;
        r1 = r2;
        r2 = r8;
    L_0x00c1:
        r0.disconnect();	 Catch:{ IOException -> 0x00e6 }
        if (r2 == 0) goto L_0x00eb;
    L_0x00c6:
        r2.close();	 Catch:{ IOException -> 0x00cc }
        r0 = r1;
        goto L_0x000d;
    L_0x00cc:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x00d0:
        r0 = move-exception;
    L_0x00d1:
        if (r2 == 0) goto L_0x00d6;
    L_0x00d3:
        r2.close();	 Catch:{ IOException -> 0x00d7 }
    L_0x00d6:
        throw r0;
    L_0x00d7:
        r1 = move-exception;
        goto L_0x00d6;
    L_0x00d9:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00d1;
    L_0x00dc:
        r0 = move-exception;
        r1 = r0;
        r0 = r2;
        goto L_0x0078;
    L_0x00e0:
        r0 = move-exception;
        r8 = r0;
        r0 = r2;
        r2 = r1;
        r1 = r8;
        goto L_0x0078;
    L_0x00e6:
        r0 = move-exception;
        r8 = r0;
        r0 = r1;
        r1 = r8;
        goto L_0x0078;
    L_0x00eb:
        r0 = r1;
        goto L_0x000d;
    L_0x00ee:
        r1 = r2;
        goto L_0x00c1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.mraid.MRAIDView.getStringFromUrl(java.lang.String):java.lang.String");
    }

    private static String getVisibilityString(int i) {
        switch (i) {
            case STATE_LOADING /*0*/:
                return "VISIBLE";
            case STATE_HIDDEN /*4*/:
                return "INVISIBLE";
            case Place.TYPE_BANK /*8*/:
                return "GONE";
            default:
                return "UNKNOWN";
        }
    }

    private void injectMraidJs(WebView webView) {
        MRAIDLog.m249v(TAG, "injectMraidJs ok " + this.mraidJs.length());
        webView.loadUrl("javascript:" + this.mraidJs);
    }

    private void onLayoutWebView(WebView webView, boolean z, int i, int i2, int i3, int i4) {
        boolean z2 = webView == this.currentWebView;
        MRAIDLog.m249v(TAG, "onLayoutWebView " + (webView == this.webView ? "1 " : "2 ") + z2 + " (" + this.state + ") " + z + " " + i + " " + i2 + " " + i3 + " " + i4);
        if (!z2) {
            MRAIDLog.m249v(TAG, "onLayoutWebView ignored, not current");
        } else if (this.isForcingFullScreen) {
            MRAIDLog.m249v(TAG, "onLayoutWebView ignored, isForcingFullScreen");
            this.isForcingFullScreen = false;
        } else {
            if (this.state == 0 || this.state == STATE_DEFAULT) {
                calculateScreenSize();
                calculateMaxSize();
            }
            if (!this.isClosing) {
                calculatePosition(true);
                if (this.isInterstitial && !this.defaultPosition.equals(this.currentPosition)) {
                    this.defaultPosition = new Rect(this.currentPosition);
                    setDefaultPosition();
                }
            }
            if (this.isExpandingFromDefault) {
                this.isExpandingFromDefault = false;
                if (this.isInterstitial) {
                    this.state = STATE_DEFAULT;
                    this.isLaidOut = true;
                }
                if (!this.isExpandingPart2) {
                    MRAIDLog.m249v(TAG, "calling fireStateChangeEvent 1");
                    fireStateChangeEvent();
                }
                if (this.isInterstitial) {
                    fireReadyEvent();
                    if (this.isViewable) {
                        fireViewableChangeEvent();
                    }
                }
                if (this.listener != null) {
                    this.listener.mraidViewExpand(this);
                }
            }
        }
    }

    private void open(String str) {
        try {
            String decode = URLDecoder.decode(str, "UTF-8");
            MRAIDLog.m249v("MRAIDView-JS callback", "open " + decode);
            if (this.nativeFeatureListener == null) {
                return;
            }
            if (decode.startsWith(Sharing.SMS)) {
                this.nativeFeatureListener.mraidNativeFeatureSendSms(decode);
            } else if (decode.startsWith(MRAIDNativeFeature.TEL)) {
                this.nativeFeatureListener.mraidNativeFeatureCallTel(decode);
            } else {
                this.nativeFeatureListener.mraidNativeFeatureOpenBrowser(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void parseCommandUrl(String str) {
        MRAIDLog.m249v(TAG, "parseCommandUrl " + str);
        Map parseCommandUrl = new MRAIDParser().parseCommandUrl(str);
        String str2 = (String) parseCommandUrl.get("command");
        try {
            if (Arrays.asList(new String[]{"close", "resize", Validation.SUCCESS, "rewardComplete", "replay"}).contains(str2)) {
                getClass().getDeclaredMethod(str2, new Class[STATE_LOADING]).invoke(this, new Object[STATE_LOADING]);
                return;
            }
            Class cls;
            Class[] clsArr;
            if (Arrays.asList(new String[]{"createCalendarEvent", "expand", "open", "playVideo", MRAIDNativeFeature.STORE_PICTURE, "useCustomClose"}).contains(str2)) {
                cls = getClass();
                clsArr = new Class[STATE_DEFAULT];
                clsArr[STATE_LOADING] = String.class;
                Method declaredMethod = cls.getDeclaredMethod(str2, clsArr);
                Object obj = str2.equals("createCalendarEvent") ? "eventJSON" : str2.equals("useCustomClose") ? "useCustomClose" : Keys.URL;
                Object[] objArr = new Object[STATE_DEFAULT];
                objArr[STATE_LOADING] = (String) parseCommandUrl.get(obj);
                declaredMethod.invoke(this, objArr);
                return;
            }
            String[] strArr = new String[STATE_EXPANDED];
            strArr[STATE_LOADING] = "setOrientationProperties";
            strArr[STATE_DEFAULT] = "setResizeProperties";
            if (Arrays.asList(strArr).contains(str2)) {
                cls = getClass();
                clsArr = new Class[STATE_DEFAULT];
                clsArr[STATE_LOADING] = Map.class;
                Method declaredMethod2 = cls.getDeclaredMethod(str2, clsArr);
                Object[] objArr2 = new Object[STATE_DEFAULT];
                objArr2[STATE_LOADING] = parseCommandUrl;
                declaredMethod2.invoke(this, objArr2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseWebView(WebView webView) {
        MRAIDLog.m249v(TAG, "pauseWebView " + webView.toString());
        if (VERSION.SDK_INT >= 11) {
            webView.onPause();
        } else {
            webView.loadUrl("about:blank");
        }
    }

    private void playVideo(String str) {
        try {
            String decode = URLDecoder.decode(str, "UTF-8");
            MRAIDLog.m249v("MRAIDView-JS callback", "playVideo " + decode);
            if (this.nativeFeatureListener != null) {
                this.nativeFeatureListener.mraidNativeFeaturePlayVideo(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private int px2dip(int i) {
        return (i * 160) / this.displayMetrics.densityDpi;
    }

    private void removeCloseRegion(View view) {
        ((ViewGroup) view).removeView(this.closeRegion);
    }

    private void removeResizeView() {
        this.resizedView.removeAllViews();
        ((FrameLayout) ((Activity) this.context).findViewById(16908290)).removeView(this.resizedView);
        this.resizedView = null;
        this.closeRegion = null;
    }

    private void replay() {
        close(false);
        this.listener.mraidReplayVideoPressed(this);
    }

    private void resize() {
        MRAIDLog.m249v("MRAIDView-JS callback", "resize");
        if (this.listener != null) {
            if (this.listener.mraidViewResize(this, this.resizeProperties.width, this.resizeProperties.height, this.resizeProperties.offsetX, this.resizeProperties.offsetY)) {
                this.state = STATE_RESIZED;
                if (this.resizedView == null) {
                    this.resizedView = new RelativeLayout(this.context);
                    removeAllViews();
                    this.resizedView.addView(this.webView);
                    if (!this.useCustomClose) {
                        addCloseRegion(this.resizedView);
                    }
                    ((FrameLayout) getRootView().findViewById(16908290)).addView(this.resizedView);
                }
                if (!this.useCustomClose) {
                    setCloseRegionPosition(this.resizedView);
                }
                setResizedViewSize();
                setResizedViewPosition();
                this.handler.post(new C10147());
            }
        }
    }

    private void restoreOriginalOrientation() {
    }

    private void restoreOriginalScreenState() {
        Activity activity = (Activity) this.context;
        if (this.isActionBarShowing) {
            activity.getActionBar().show();
        } else if (this.titleBar != null) {
            this.titleBar.setVisibility(this.origTitleBarVisibility);
        }
    }

    private void rewardComplete() {
        MRAIDLog.m249v("MRAIDView-JS callback", "rewardComplete " + this.state);
        this.handler.post(new C10115());
    }

    private void setCloseRegionPosition(View view) {
        int applyDimension = (int) TypedValue.applyDimension(STATE_DEFAULT, 50.0f, this.displayMetrics);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(applyDimension, applyDimension);
        if (view != this.expandedView) {
            if (view == this.resizedView) {
                switch (this.resizeProperties.customClosePosition) {
                    case STATE_LOADING /*0*/:
                    case STATE_HIDDEN /*4*/:
                        layoutParams.addRule(9);
                        break;
                    case STATE_DEFAULT /*1*/:
                    case STATE_RESIZED /*3*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                        layoutParams.addRule(14);
                        break;
                    case STATE_EXPANDED /*2*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                        layoutParams.addRule(11);
                        break;
                }
                switch (this.resizeProperties.customClosePosition) {
                    case STATE_LOADING /*0*/:
                    case STATE_DEFAULT /*1*/:
                    case STATE_EXPANDED /*2*/:
                        layoutParams.addRule(10);
                        break;
                    case STATE_RESIZED /*3*/:
                        layoutParams.addRule(15);
                        break;
                    case STATE_HIDDEN /*4*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                        layoutParams.addRule(12);
                        break;
                    default:
                        break;
                }
            }
        }
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        this.closeRegion.setLayoutParams(layoutParams);
    }

    private void setCurrentPosition() {
        int i = this.currentPosition.left;
        int i2 = this.currentPosition.top;
        int width = this.currentPosition.width();
        int height = this.currentPosition.height();
        MRAIDLog.m249v(TAG, "setCurrentPosition [" + i + "," + i2 + "] (" + width + "x" + height + ")");
        injectJavaScript("mraid.setCurrentPosition(" + px2dip(i) + "," + px2dip(i2) + "," + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setDefaultPosition() {
        int i = this.defaultPosition.left;
        int i2 = this.defaultPosition.top;
        int width = this.defaultPosition.width();
        int height = this.defaultPosition.height();
        MRAIDLog.m249v(TAG, "setDefaultPosition [" + i + "," + i2 + "] (" + width + "x" + height + ")");
        injectJavaScript("mraid.setDefaultPosition(" + px2dip(i) + "," + px2dip(i2) + "," + px2dip(width) + "," + px2dip(height) + ");");
    }

    private void setMaxSize() {
        MRAIDLog.m249v(TAG, "setMaxSize");
        int i = this.maxSize.width;
        int i2 = this.maxSize.height;
        MRAIDLog.m249v(TAG, "setMaxSize " + i + "x" + i2);
        injectJavaScript("mraid.setMaxSize(" + px2dip(i) + "," + px2dip(i2) + ");");
    }

    private void setOrientationProperties(Map<String, String> map) {
        boolean parseBoolean = Boolean.parseBoolean((String) map.get("allowOrientationChange"));
        String str = (String) map.get("forceOrientation");
        MRAIDLog.m249v("MRAIDView-JS callback", "setOrientationProperties " + parseBoolean + " " + str);
        if (this.orientationProperties.allowOrientationChange != parseBoolean || this.orientationProperties.forceOrientation != MRAIDOrientationProperties.forceOrientationFromString(str)) {
            this.orientationProperties.allowOrientationChange = parseBoolean;
            this.orientationProperties.forceOrientation = MRAIDOrientationProperties.forceOrientationFromString(str);
            if (this.isInterstitial || this.state == STATE_EXPANDED) {
                applyOrientationProperties();
            }
        }
    }

    private void setResizeProperties(Map<String, String> map) {
        int parseInt = Integer.parseInt((String) map.get("width"));
        int parseInt2 = Integer.parseInt((String) map.get("height"));
        int parseInt3 = Integer.parseInt((String) map.get("offsetX"));
        int parseInt4 = Integer.parseInt((String) map.get("offsetY"));
        String str = (String) map.get("customClosePosition");
        boolean parseBoolean = Boolean.parseBoolean((String) map.get("allowOffscreen"));
        MRAIDLog.m249v("MRAIDView-JS callback", "setResizeProperties " + parseInt + " " + parseInt2 + " " + parseInt3 + " " + parseInt4 + " " + str + " " + parseBoolean);
        this.resizeProperties.width = parseInt;
        this.resizeProperties.height = parseInt2;
        this.resizeProperties.offsetX = parseInt3;
        this.resizeProperties.offsetY = parseInt4;
        this.resizeProperties.customClosePosition = MRAIDResizeProperties.customClosePositionFromString(str);
        this.resizeProperties.allowOffscreen = parseBoolean;
    }

    private void setResizedViewPosition() {
        MRAIDLog.m249v(TAG, "setResizedViewPosition");
        if (this.resizedView != null) {
            int i = this.resizeProperties.width;
            int i2 = this.resizeProperties.height;
            int i3 = this.resizeProperties.offsetX;
            int applyDimension = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) i, this.displayMetrics);
            i2 = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) i2, this.displayMetrics);
            i = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) i3, this.displayMetrics);
            i3 = (int) TypedValue.applyDimension(STATE_DEFAULT, (float) this.resizeProperties.offsetY, this.displayMetrics);
            int i4 = this.defaultPosition.left + i;
            i3 += this.defaultPosition.top;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.resizedView.getLayoutParams();
            layoutParams.leftMargin = i4;
            layoutParams.topMargin = i3;
            this.resizedView.setLayoutParams(layoutParams);
            if (i4 != this.currentPosition.left || i3 != this.currentPosition.top || applyDimension != this.currentPosition.width() || i2 != this.currentPosition.height()) {
                this.currentPosition.left = i4;
                this.currentPosition.top = i3;
                this.currentPosition.right = i4 + applyDimension;
                this.currentPosition.bottom = i2 + i3;
                setCurrentPosition();
            }
        }
    }

    private void setResizedViewSize() {
        MRAIDLog.m249v(TAG, "setResizedViewSize");
        int i = this.resizeProperties.width;
        int i2 = this.resizeProperties.height;
        Log.d(TAG, "setResizedViewSize " + i + "x" + i2);
        this.resizedView.setLayoutParams(new FrameLayout.LayoutParams((int) TypedValue.applyDimension(STATE_DEFAULT, (float) i, this.displayMetrics), (int) TypedValue.applyDimension(STATE_DEFAULT, (float) i2, this.displayMetrics)));
    }

    private void setScreenSize() {
        MRAIDLog.m249v(TAG, "setScreenSize");
        int i = this.screenSize.width;
        int i2 = this.screenSize.height;
        MRAIDLog.m249v(TAG, "setScreenSize " + i + "x" + i2);
        injectJavaScript("mraid.setScreenSize(" + px2dip(i) + "," + px2dip(i2) + ");");
    }

    private void setSupportedServices() {
        MRAIDLog.m249v(TAG, "setSupportedServices");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.CALENDAR, " + this.nativeFeatureManager.isCalendarSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.INLINEVIDEO, " + this.nativeFeatureManager.isInlineVideoSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.SMS, " + this.nativeFeatureManager.isSmsSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.STOREPICTURE, " + this.nativeFeatureManager.isStorePictureSupported() + ");");
        injectJavaScript("mraid.setSupports(mraid.SUPPORTED_FEATURES.TEL, " + this.nativeFeatureManager.isTelSupported() + ");");
    }

    private void setViewable(int i) {
        boolean z = i == 0;
        if (z != this.isViewable) {
            this.isViewable = z;
            if (this.isPageFinished && this.isLaidOut) {
                fireViewableChangeEvent();
            }
        }
    }

    private void storePicture(String str) {
        try {
            String decode = URLDecoder.decode(str, "UTF-8");
            MRAIDLog.m249v("MRAIDView-JS callback", "storePicture " + decode);
            if (this.nativeFeatureListener != null) {
                this.nativeFeatureListener.mraidNativeFeatureStorePicture(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void success() {
        this.listener.mraidViewAcceptPressed(this);
        close();
    }

    private void useCustomClose(String str) {
        MRAIDLog.m249v("MRAIDView-JS callback", "useCustomClose " + str);
        this.useCustomClose = Boolean.parseBoolean(str);
        if (!this.useCustomClose) {
            return;
        }
        if (this.expandedView != null) {
            removeCloseRegion(this.expandedView);
        } else if (this.resizedView != null) {
            removeCloseRegion(this.resizedView);
        }
    }

    public void injectJavaScript(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.currentWebView.loadUrl("javascript:" + str);
        }
    }

    protected void onAttachedToWindow() {
        MRAIDLog.m249v(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        MRAIDLog.m249v(TAG, "onConfigurationChanged " + (configuration.orientation == STATE_DEFAULT ? "portrait" : "landscape"));
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(this.displayMetrics);
    }

    protected void onDetachedFromWindow() {
        MRAIDLog.m249v(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        MRAIDLog.m249v(TAG, "onLayout (" + this.state + ") " + z + " " + i + " " + i2 + " " + i3 + " " + i4);
        if (this.isForcingFullScreen) {
            MRAIDLog.m249v(TAG, "onLayout ignored");
            return;
        }
        if (this.state == STATE_EXPANDED || this.state == STATE_RESIZED) {
            calculateScreenSize();
            calculateMaxSize();
        }
        if (this.isClosing) {
            this.isClosing = false;
            this.currentPosition = new Rect(this.defaultPosition);
            setCurrentPosition();
        } else {
            calculatePosition(false);
        }
        if (this.state == STATE_RESIZED && z) {
            this.handler.post(new Runnable() {
                public void run() {
                    MRAIDView.this.setResizedViewPosition();
                }
            });
        }
        this.isLaidOut = true;
        if (this.state == 0 && this.isPageFinished) {
            this.state = STATE_DEFAULT;
            fireStateChangeEvent();
            fireReadyEvent();
            if (this.isViewable) {
                fireViewableChangeEvent();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            motionEvent.setAction(STATE_RESIZED);
        }
        return super.onTouchEvent(motionEvent);
    }

    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        MRAIDLog.m249v(TAG, "onVisibilityChanged " + getVisibilityString(i));
        setViewable(i);
    }

    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        int visibility = getVisibility();
        MRAIDLog.m249v(TAG, "onWindowVisibilityChanged " + getVisibilityString(i) + " (actual " + getVisibilityString(visibility) + ")");
        setViewable(visibility);
    }

    public void setOrientationConfig(int i) {
        this.rotateMode = i;
    }

    protected void showAsInterstitial() {
        expand(null);
    }

    public void updateContext(Context context) {
        Activity activity = (Activity) this.context;
        activity = (Activity) context;
        this.context = context;
    }
}
