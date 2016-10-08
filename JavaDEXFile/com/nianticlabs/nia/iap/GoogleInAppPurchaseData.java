package com.nianticlabs.nia.iap;

import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

final class GoogleInAppPurchaseData {
    private static final String TAG = "GoogleInAppPurchaseData";
    private String developerPayload;
    private String orderId;
    private String packageName;
    private String productId;
    private String purchaseState;
    private long purchaseTime;

    GoogleInAppPurchaseData() {
    }

    static GoogleInAppPurchaseData fromJson(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            GoogleInAppPurchaseData googleInAppPurchaseData = new GoogleInAppPurchaseData();
            googleInAppPurchaseData.orderId = stringFromJson(jSONObject, "orderId");
            googleInAppPurchaseData.packageName = stringFromJson(jSONObject, "packageName");
            googleInAppPurchaseData.productId = stringFromJson(jSONObject, Billing.PRODUCT_IDENTIFIER);
            googleInAppPurchaseData.purchaseTime = longFromJson(jSONObject, "purchaseTime");
            googleInAppPurchaseData.purchaseState = stringFromJson(jSONObject, "purchaseState");
            googleInAppPurchaseData.developerPayload = stringFromJson(jSONObject, "developerPayload");
            return googleInAppPurchaseData;
        } catch (Throwable e) {
            Log.e(TAG, "Failed to parse GoogleInAppPurchaseData: %s", e);
            return null;
        }
    }

    private static long longFromJson(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getLong(str);
        } catch (JSONException e) {
            return 0;
        }
    }

    private static String stringFromJson(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException e) {
            return BuildConfig.FLAVOR;
        }
    }

    String getDeveloperPayload() {
        return this.developerPayload;
    }

    String getOrderId() {
        return this.orderId;
    }

    String getPackageName() {
        return this.packageName;
    }

    String getProductId() {
        return this.productId;
    }

    String getPurchaseState() {
        return this.purchaseState;
    }

    long getPurchaseTime() {
        return this.purchaseTime;
    }
}
