package com.upsight.android.unity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightCustomEvent;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.install.UpsightInstallAttributionEvent;
import com.upsight.android.analytics.event.milestone.UpsightMilestoneEvent;
import com.upsight.android.analytics.event.milestone.UpsightMilestoneEvent.Builder;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationEvent;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class UpsightPlugin {
    protected static final String TAG = "Upsight-Unity";
    @NonNull
    private Set<IUpsightExtensionManager> mExtensions;
    protected UpsightContext mUpsight;

    /* renamed from: com.upsight.android.unity.UpsightPlugin.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ String val$properties;
        final /* synthetic */ String val$scope;

        AnonymousClass10(String str, String str2) {
            this.val$scope = str;
            this.val$properties = str2;
        }

        public void run() {
            Builder createBuilder = UpsightMilestoneEvent.createBuilder(this.val$scope);
            createBuilder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            createBuilder.record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ String val$currency;
        final /* synthetic */ double val$price;
        final /* synthetic */ String val$product;
        final /* synthetic */ String val$properties;
        final /* synthetic */ int val$quantity;
        final /* synthetic */ String val$resolution;
        final /* synthetic */ double val$totalPrice;

        AnonymousClass11(double d, String str, String str2, String str3, double d2, String str4, int i) {
            this.val$totalPrice = d;
            this.val$currency = str;
            this.val$properties = str2;
            this.val$product = str3;
            this.val$price = d2;
            this.val$resolution = str4;
            this.val$quantity = i;
        }

        public void run() {
            UpsightMonetizationEvent.Builder createBuilder = UpsightMonetizationEvent.createBuilder(Double.valueOf(this.val$totalPrice), this.val$currency);
            createBuilder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            if (this.val$product != null) {
                createBuilder.setProduct(this.val$product);
            }
            if (this.val$price >= 0.0d) {
                createBuilder.setPrice(Double.valueOf(this.val$price));
            }
            if (this.val$resolution != null) {
                createBuilder.setResolution(this.val$resolution);
            }
            if (this.val$quantity > 0) {
                createBuilder.setQuantity(Integer.valueOf(this.val$quantity));
            }
            createBuilder.record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ String val$currency;
        final /* synthetic */ String val$inAppDataSignature;
        final /* synthetic */ String val$inAppPurchaseData;
        final /* synthetic */ double val$price;
        final /* synthetic */ String val$product;
        final /* synthetic */ String val$properties;
        final /* synthetic */ int val$quantity;
        final /* synthetic */ int val$reponseCode;
        final /* synthetic */ double val$totalPrice;

        AnonymousClass12(String str, int i, String str2, String str3, int i2, String str4, double d, double d2, String str5) {
            this.val$properties = str;
            this.val$reponseCode = i;
            this.val$inAppPurchaseData = str2;
            this.val$inAppDataSignature = str3;
            this.val$quantity = i2;
            this.val$currency = str4;
            this.val$price = d;
            this.val$totalPrice = d2;
            this.val$product = str5;
        }

        public void run() {
            UpsightPublisherData.Builder builder = new UpsightPublisherData.Builder();
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            try {
                Intent intent = new Intent();
                intent.putExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, this.val$reponseCode);
                intent.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, this.val$inAppPurchaseData);
                intent.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, this.val$inAppDataSignature);
                UpsightGooglePlayHelper.trackPurchase(UpsightPlugin.this.mUpsight, this.val$quantity, this.val$currency, this.val$price, this.val$totalPrice, this.val$product, intent, builder.build());
            } catch (UpsightException e) {
                Log.i(UpsightPlugin.TAG, "Failed to recordGooglePlayPurchase: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.13 */
    class AnonymousClass13 implements Runnable {
        final /* synthetic */ String val$campaign;
        final /* synthetic */ String val$creative;
        final /* synthetic */ String val$properties;
        final /* synthetic */ String val$source;

        AnonymousClass13(String str, String str2, String str3, String str4) {
            this.val$campaign = str;
            this.val$creative = str2;
            this.val$source = str3;
            this.val$properties = str4;
        }

        public void run() {
            UpsightInstallAttributionEvent.createBuilder().setAttributionCampaign(this.val$campaign).setAttributionCreative(this.val$creative).setAttributionSource(this.val$source).put(UpsightPlugin.publisherDataFromJsonString(this.val$properties)).record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.1 */
    class C09841 implements Runnable {
        final /* synthetic */ Activity val$activity;

        C09841(Activity activity) {
            this.val$activity = activity;
        }

        public void run() {
            UpsightLifeCycleTracker.track(UpsightPlugin.this.mUpsight, this.val$activity, ActivityState.STARTED);
            Log.i(UpsightPlugin.TAG, "Upsight initialization finished");
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.2 */
    class C09852 implements Runnable {
        final /* synthetic */ double val$lat;
        final /* synthetic */ double val$lon;

        C09852(double d, double d2) {
            this.val$lat = d;
            this.val$lon = d2;
        }

        public void run() {
            UpsightLocationTracker.track(UpsightPlugin.this.mUpsight, Data.create(this.val$lat, this.val$lon));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.3 */
    class C09863 implements Runnable {
        C09863() {
        }

        public void run() {
            UpsightLocationTracker.purge(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.4 */
    class C09874 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ String val$value;

        C09874(String str, String str2) {
            this.val$key = str;
            this.val$value = str2;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, this.val$value);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.5 */
    class C09885 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ float val$value;

        C09885(String str, float f) {
            this.val$key = str;
            this.val$value = f;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Float.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.6 */
    class C09896 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ int val$value;

        C09896(String str, int i) {
            this.val$key = str;
            this.val$value = i;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Integer.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.7 */
    class C09907 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ boolean val$value;

        C09907(String str, boolean z) {
            this.val$key = str;
            this.val$value = z;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Boolean.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.8 */
    class C09918 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ long val$value;

        C09918(long j, String str) {
            this.val$value = j;
            this.val$key = str;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, new Date(TimeUnit.MILLISECONDS.convert(this.val$value, TimeUnit.SECONDS)));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.9 */
    class C09929 implements Runnable {
        final /* synthetic */ String val$eventName;
        final /* synthetic */ String val$properties;

        C09929(String str, String str2) {
            this.val$eventName = str;
            this.val$properties = str2;
        }

        public void run() {
            UpsightCustomEvent.Builder createBuilder = UpsightCustomEvent.createBuilder(this.val$eventName);
            createBuilder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            createBuilder.record(UpsightPlugin.this.mUpsight);
        }
    }

    public UpsightPlugin() {
        this.mExtensions = new HashSet(2);
        try {
            Context activity = UnityBridge.getActivity();
            this.mUpsight = Upsight.createContext(activity);
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.ERROR));
            UnityBridge.runSafelyOnUiThread(new C09841(activity));
        } catch (Throwable e) {
            Log.e(TAG, "Critical Error: Exception thrown while initializing. Upsight will NOT work!", e);
            throw e;
        }
    }

    @NonNull
    private static UpsightPublisherData publisherDataFromJsonString(@Nullable String str) {
        UpsightPublisherData.Builder builder = new UpsightPublisherData.Builder();
        if (str != null && str.length() > 0) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str2 = (String) keys.next();
                    try {
                        Object obj = jSONObject.get(str2);
                        if (obj instanceof String) {
                            builder.put(str2, (String) obj);
                        } else if (obj instanceof Float) {
                            builder.put(str2, ((Float) obj).floatValue());
                        } else if (obj instanceof Double) {
                            builder.put(str2, ((Double) obj).doubleValue());
                        } else if (obj instanceof Long) {
                            builder.put(str2, ((Long) obj).longValue());
                        } else if (obj instanceof Boolean) {
                            builder.put(str2, ((Boolean) obj).booleanValue());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return builder.build();
    }

    @NonNull
    public String getAppToken() {
        return this.mUpsight.getApplicationToken();
    }

    public boolean getManagedBool(@NonNull String str) {
        try {
            UpsightManagedBoolean fetch = UpsightManagedBoolean.fetch(this.mUpsight, str);
            if (fetch != null) {
                return ((Boolean) fetch.get()).booleanValue();
            }
            Log.e(TAG, "Unknown tag " + str + " for managed bool, please check your UXM schema");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public float getManagedFloat(@NonNull String str) {
        try {
            UpsightManagedFloat fetch = UpsightManagedFloat.fetch(this.mUpsight, str);
            if (fetch != null) {
                return ((Float) fetch.get()).floatValue();
            }
            Log.e(TAG, "Unknown tag " + str + " for managed float, please check your UXM schema");
            return 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public int getManagedInt(@NonNull String str) {
        try {
            UpsightManagedInt fetch = UpsightManagedInt.fetch(this.mUpsight, str);
            if (fetch != null) {
                return ((Integer) fetch.get()).intValue();
            }
            Log.e(TAG, "Unknown tag " + str + " for managed int, please check your UXM schema");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Nullable
    public String getManagedString(@NonNull String str) {
        try {
            UpsightManagedString fetch = UpsightManagedString.fetch(this.mUpsight, str);
            if (fetch != null) {
                return (String) fetch.get();
            }
            Log.e(TAG, "Unknown tag " + str + " for managed string, please check your UXM schema");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getOptOutStatus() {
        try {
            return UpsightOptOutStatus.get(this.mUpsight);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @NonNull
    public String getPluginVersion() {
        return this.mUpsight.getSdkPlugin();
    }

    @NonNull
    public String getPublicKey() {
        return this.mUpsight.getPublicKey();
    }

    @NonNull
    public String getSid() {
        return this.mUpsight.getSid();
    }

    public boolean getUserAttributesBool(@NonNull String str) {
        try {
            Boolean bool = UpsightUserAttributes.getBoolean(this.mUpsight, str);
            if (bool != null) {
                return bool.booleanValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getUserAttributesDatetime(@NonNull String str) {
        try {
            Date datetime = UpsightUserAttributes.getDatetime(this.mUpsight, str);
            if (datetime != null) {
                return TimeUnit.SECONDS.convert(datetime.getTime(), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float getUserAttributesFloat(@NonNull String str) {
        try {
            Float f = UpsightUserAttributes.getFloat(this.mUpsight, str);
            if (f != null) {
                return f.floatValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public int getUserAttributesInt(@NonNull String str) {
        try {
            Integer integer = UpsightUserAttributes.getInteger(this.mUpsight, str);
            if (integer != null) {
                return integer.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Nullable
    public String getUserAttributesString(@NonNull String str) {
        try {
            return UpsightUserAttributes.getString(this.mUpsight, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onApplicationPaused() {
        for (IUpsightExtensionManager onApplicationPaused : this.mExtensions) {
            onApplicationPaused.onApplicationPaused();
        }
    }

    public void onApplicationResumed() {
        for (IUpsightExtensionManager onApplicationResumed : this.mExtensions) {
            onApplicationResumed.onApplicationResumed();
        }
    }

    public void purgeLocation() {
        UnityBridge.runSafelyOnUiThread(new C09863());
    }

    public void recordAnalyticsEvent(@NonNull String str, @NonNull String str2) {
        UnityBridge.runSafelyOnUiThread(new C09929(str, str2));
    }

    public void recordAttributionEvent(@Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass13(str, str2, str3, str4));
    }

    public void recordGooglePlayPurchase(int i, @NonNull String str, double d, double d2, @NonNull String str2, int i2, @NonNull String str3, @NonNull String str4, @NonNull String str5) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass12(str5, i2, str3, str4, i, str, d, d2, str2));
    }

    public void recordMilestoneEvent(@NonNull String str, @NonNull String str2) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass10(str, str2));
    }

    public void recordMonetizationEvent(double d, @NonNull String str, @Nullable String str2, double d2, @Nullable String str3, int i, @Nullable String str4) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass11(d, str, str4, str2, d2, str3, i));
    }

    public void registerExtension(IUpsightExtensionManager iUpsightExtensionManager) {
        if (this.mExtensions.add(iUpsightExtensionManager)) {
            iUpsightExtensionManager.init(this.mUpsight);
        }
    }

    public void setLocation(double d, double d2) {
        UnityBridge.runSafelyOnUiThread(new C09852(d, d2));
    }

    public void setLoggerLevel(@NonNull String str) {
        try {
            if (str.toLowerCase().equals("verbose")) {
                Log.i(TAG, "enabling verbose logs");
                this.mUpsight.getLogger().setLogLevel(".*", EnumSet.allOf(Level.class));
                return;
            }
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.valueOf(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOptOutStatus(boolean z) {
        try {
            UpsightOptOutStatus.set(this.mUpsight, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserAttributesBool(@NonNull String str, boolean z) {
        UnityBridge.runSafelyOnUiThread(new C09907(str, z));
    }

    public void setUserAttributesDatetime(@NonNull String str, long j) {
        UnityBridge.runSafelyOnUiThread(new C09918(j, str));
    }

    public void setUserAttributesFloat(@NonNull String str, float f) {
        UnityBridge.runSafelyOnUiThread(new C09885(str, f));
    }

    public void setUserAttributesInt(@NonNull String str, int i) {
        UnityBridge.runSafelyOnUiThread(new C09896(str, i));
    }

    public void setUserAttributesString(@NonNull String str, @NonNull String str2) {
        UnityBridge.runSafelyOnUiThread(new C09874(str, str2));
    }
}
