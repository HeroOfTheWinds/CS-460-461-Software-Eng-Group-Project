package com.upsight.android.analytics.internal.referrer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.text.TextUtils;
import com.upsight.android.Upsight;
import com.upsight.android.analytics.event.install.UpsightInstallAttributionEvent;
import com.upsight.android.internal.util.PreferencesHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

public class InstallReferrerReceiver extends BroadcastReceiver {
    private static final String ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String EXTRA_REFERRER = "referrer";
    public static final String REFERRER_PARAM_KEY_CAMPAIGN = "utm_campaign";
    public static final String REFERRER_PARAM_KEY_CONTENT = "utm_content";
    public static final String REFERRER_PARAM_KEY_MEDIUM = "utm_medium";
    public static final String REFERRER_PARAM_KEY_SOURCE = "utm_source";
    public static final String REFERRER_PARAM_KEY_TERM = "utm_term";
    public static final String SHARED_PREFERENCES_KEY_REFERRER = "referrer";

    public void onReceive(Context context, Intent intent) {
        UnsupportedEncodingException e;
        Context createContext = Upsight.createContext(context);
        if (ACTION_INSTALL_REFERRER.equals(intent.getAction()) && !PreferencesHelper.contains(createContext, SHARED_PREFERENCES_KEY_REFERRER)) {
            try {
                JSONObject parseReferrerParams = parseReferrerParams(intent.getStringExtra(SHARED_PREFERENCES_KEY_REFERRER));
                PreferencesHelper.putString(createContext, SHARED_PREFERENCES_KEY_REFERRER, parseReferrerParams.toString());
                Object optString = parseReferrerParams.optString(REFERRER_PARAM_KEY_SOURCE);
                Object optString2 = parseReferrerParams.optString(REFERRER_PARAM_KEY_CAMPAIGN);
                Object optString3 = parseReferrerParams.optString(REFERRER_PARAM_KEY_CONTENT);
                if (!TextUtils.isEmpty(optString) || !TextUtils.isEmpty(optString2) || !TextUtils.isEmpty(optString3)) {
                    UpsightInstallAttributionEvent.createBuilder().setAttributionSource(optString).setAttributionCampaign(optString2).setAttributionCreative(optString3).record(createContext);
                }
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                createContext.getLogger().m207e(Upsight.LOG_TAG, "Failed to parse referrer parameters from com.android.vending.INSTALL_REFERRER", e);
            } catch (JSONException e3) {
                e = e3;
                createContext.getLogger().m207e(Upsight.LOG_TAG, "Failed to parse referrer parameters from com.android.vending.INSTALL_REFERRER", e);
            }
        }
    }

    JSONObject parseReferrerParams(String str) throws UnsupportedEncodingException, JSONException {
        JSONObject jSONObject = new JSONObject();
        if (!TextUtils.isEmpty(str)) {
            UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
            urlQuerySanitizer.setAllowUnregisteredParamaters(true);
            urlQuerySanitizer.parseQuery(URLDecoder.decode(str, CHARSET_UTF8).trim());
            for (String str2 : urlQuerySanitizer.getParameterSet()) {
                jSONObject.put(str2, urlQuerySanitizer.getValue(str2));
            }
        }
        return jSONObject;
    }
}
