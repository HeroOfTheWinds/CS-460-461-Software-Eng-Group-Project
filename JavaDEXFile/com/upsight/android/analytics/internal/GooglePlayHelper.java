package com.upsight.android.analytics.internal;

import android.content.Intent;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationIapEvent;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.C1518R;

class GooglePlayHelper extends UpsightGooglePlayHelper {
    private static final String STORE_NAME = "google_play";
    private Gson mGson;
    private UpsightContext mUpsight;

    static class PurchaseData {
        @SerializedName("developerPayload")
        @Expose
        String developerPayload;
        @SerializedName("orderId")
        @Expose
        String orderId;
        @SerializedName("packageName")
        @Expose
        String packageName;
        @SerializedName("productId")
        @Expose
        String productId;
        @SerializedName("purchaseState")
        @Expose
        int purchaseState;
        @SerializedName("purchaseTime")
        @Expose
        long purchaseTime;
        @SerializedName("purchaseToken")
        @Expose
        String purchaseToken;

        PurchaseData() {
        }
    }

    public enum Resolution {
        buy,
        cancel,
        refund
    }

    GooglePlayHelper(UpsightContext upsightContext, Gson gson) {
        this.mUpsight = upsightContext;
        this.mGson = gson;
    }

    private JSONObject createIapBundle(int i, String str, String str2) throws JSONException {
        return new JSONObject().put(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, i).put(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, str).put(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, str2);
    }

    public void trackPurchase(int i, String str, double d, double d2, String str2, Intent intent, UpsightPublisherData upsightPublisherData) throws UpsightException {
        int intExtra = intent.getIntExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, ExploreByTouchHelper.INVALID_ID);
        switch (intExtra) {
            case C1518R.styleable.AdsAttrs_adSize /*0*/:
                String stringExtra = intent.getStringExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA);
                Object stringExtra2 = intent.getStringExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE);
                if (TextUtils.isEmpty(stringExtra)) {
                    this.mUpsight.getLogger().m207e(Upsight.LOG_TAG, "Failed to track Google Play purchase due to null or empty purchase data.", new Object[0]);
                    throw new UpsightException("Failed to track Google Play purchase due to null or empty purchase data.", new Object[0]);
                } else if (TextUtils.isEmpty(stringExtra2)) {
                    this.mUpsight.getLogger().m207e(Upsight.LOG_TAG, "Failed to track Google Play purchase due to null or empty data signature.", new Object[0]);
                    throw new UpsightException("Failed to track Google Play purchase due to null or empty data signature.", new Object[0]);
                } else {
                    try {
                        PurchaseData purchaseData = (PurchaseData) this.mGson.fromJson(stringExtra, PurchaseData.class);
                        if (purchaseData != null) {
                            Resolution resolution;
                            switch (purchaseData.purchaseState) {
                                case C1518R.styleable.AdsAttrs_adSize /*0*/:
                                    resolution = Resolution.buy;
                                    break;
                                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                                    resolution = Resolution.cancel;
                                    break;
                                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                                    resolution = Resolution.refund;
                                    break;
                                default:
                                    this.mUpsight.getLogger().m207e(Upsight.LOG_TAG, "Failed to track Google Play purchase. Invalid purchase state.", new Object[0]);
                                    throw new UpsightException("Failed to track Google Play purchase. Invalid purchase state.", new Object[0]);
                            }
                            try {
                                UpsightMonetizationIapEvent.createBuilder(STORE_NAME, createIapBundle(intExtra, stringExtra, stringExtra2), Double.valueOf(d2), Double.valueOf(d), Integer.valueOf(i), str, str2).setResolution(resolution.toString()).put(upsightPublisherData).record(this.mUpsight);
                                return;
                            } catch (Throwable e) {
                                this.mUpsight.getLogger().m208e(Upsight.LOG_TAG, e, "Failed to track Google Play purchase. Unable to create iap_bundle.", new Object[0]);
                                throw new UpsightException(e, "Failed to track Google Play purchase. Unable to create iap_bundle.", new Object[0]);
                            }
                        }
                        this.mUpsight.getLogger().m207e(Upsight.LOG_TAG, "Failed to track Google Play purchase due to missing fields in purchase data.", new Object[0]);
                        throw new UpsightException("Failed to track Google Play purchase due to missing fields in purchase data.", new Object[0]);
                    } catch (Throwable e2) {
                        this.mUpsight.getLogger().m208e(Upsight.LOG_TAG, e2, "Failed to track Google Play purchase due to malformed purchase data JSON.", new Object[0]);
                        throw new UpsightException(e2, "Failed to track Google Play purchase due to malformed purchase data JSON.", new Object[0]);
                    }
                }
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                UpsightMonetizationIapEvent.createBuilder(STORE_NAME, null, Double.valueOf(d2), Double.valueOf(d), Integer.valueOf(i), str, str2).setResolution(Resolution.cancel.toString()).put(upsightPublisherData).record(this.mUpsight);
            default:
                String str3 = "Failed to track Google Play purchase. See response code for details. responseCode=" + intExtra;
                this.mUpsight.getLogger().m207e(Upsight.LOG_TAG, str3, new Object[0]);
                throw new UpsightException(str3, new Object[0]);
        }
    }
}
