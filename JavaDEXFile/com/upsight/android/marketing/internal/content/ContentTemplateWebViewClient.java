package com.upsight.android.marketing.internal.content;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.upsight.android.logger.UpsightLogger;
import java.util.Map.Entry;

class ContentTemplateWebViewClient extends WebViewClient {
    private static final String DISPATCH_CALLBACK = "javascript:PlayHaven.nativeAPI.callback(\"%s\", %s, %s, %s)";
    private static final String DISPATCH_CALLBACK_PROTOCOL = "javascript:window.PlayHavenDispatchProtocolVersion=7";
    private static final String DISPATCH_LOAD_CONTEXT = "ph://loadContext";
    private static final String DISPATCH_PARAM_KEY_CALLBACK_ID = "callback";
    private static final String DISPATCH_PARAM_KEY_CONTEXT = "context";
    private static final String DISPATCH_PARAM_KEY_TRIGGER = "trigger";
    private static final String DISPATCH_PARAM_KEY_VIEW_DATA = "view_data";
    private static final String DISPATCH_SCHEME = "ph://";
    private final Bus mBus;
    private final Gson mGson;
    private boolean mIsTemplateLoaded;
    private final JsonParser mJsonParser;
    private final UpsightLogger mLogger;
    private final MarketingContent<MarketingContentModel> mMarketingContent;

    public ContentTemplateWebViewClient(MarketingContent<MarketingContentModel> marketingContent, Bus bus, Gson gson, JsonParser jsonParser, UpsightLogger upsightLogger) {
        this.mIsTemplateLoaded = false;
        this.mMarketingContent = marketingContent;
        this.mBus = bus;
        this.mGson = gson;
        this.mJsonParser = jsonParser;
        this.mLogger = upsightLogger;
    }

    private boolean handleActionDispatch(String str) {
        boolean z;
        if (str == null || !str.startsWith(DISPATCH_SCHEME)) {
            z = false;
        } else {
            String queryParameter = Uri.parse(str).getQueryParameter(DISPATCH_PARAM_KEY_CONTEXT);
            if (!TextUtils.isEmpty(queryParameter)) {
                try {
                    JsonElement parse = this.mJsonParser.parse(queryParameter);
                    if (parse.isJsonObject()) {
                        JsonObject asJsonObject = parse.getAsJsonObject();
                        JsonElement jsonElement = asJsonObject.get(DISPATCH_PARAM_KEY_TRIGGER);
                        parse = asJsonObject.get(DISPATCH_PARAM_KEY_VIEW_DATA);
                        if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                            this.mMarketingContent.executeActions(jsonElement.getAsString());
                            return true;
                        } else if (parse != null && parse.isJsonObject()) {
                            for (Entry entry : parse.getAsJsonObject().entrySet()) {
                                jsonElement = (JsonElement) entry.getValue();
                                this.mMarketingContent.putExtra((String) entry.getKey(), jsonElement != null ? jsonElement.toString() : null);
                            }
                        }
                    } else {
                        this.mLogger.m207e(getClass().getSimpleName(), "Failed to parse context into JsonObject context=" + queryParameter, new Object[0]);
                        z = true;
                    }
                } catch (Throwable e) {
                    this.mLogger.m208e(getClass().getSimpleName(), e, "Failed to parse context into JsonElement context=" + queryParameter, new Object[0]);
                    return true;
                }
            }
            z = true;
        }
        return z;
    }

    @TargetApi(19)
    private boolean handleLoadContextDispatch(WebView webView, String str) {
        boolean z = false;
        if (str != null && str.startsWith(DISPATCH_LOAD_CONTEXT)) {
            String format = String.format(DISPATCH_CALLBACK, new Object[]{Uri.parse(str).getQueryParameter(DISPATCH_PARAM_KEY_CALLBACK_ID), ((MarketingContentModel) this.mMarketingContent.getContentModel()).getContext(), null, null});
            webView.loadUrl(DISPATCH_CALLBACK_PROTOCOL);
            if (VERSION.SDK_INT >= 19) {
                webView.evaluateJavascript(format, null);
                z = true;
            } else {
                webView.loadUrl(format);
                return true;
            }
        }
        return z;
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (!this.mIsTemplateLoaded) {
            this.mIsTemplateLoaded = true;
            this.mMarketingContent.markLoaded(this.mBus);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return handleLoadContextDispatch(webView, str) || handleActionDispatch(str) || super.shouldOverrideUrlLoading(webView, str);
    }
}
