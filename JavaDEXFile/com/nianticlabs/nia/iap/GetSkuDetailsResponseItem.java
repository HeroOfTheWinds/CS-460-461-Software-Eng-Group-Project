package com.nianticlabs.nia.iap;

import android.util.Log;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Billing;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

class GetSkuDetailsResponseItem {
    private static final String TAG = "SkuDetailsResponseItem";
    private String description;
    private String price;
    private String price_amount_micros;
    private String price_currency_code;
    private String productId;
    private String title;
    private String type;

    GetSkuDetailsResponseItem() {
    }

    static GetSkuDetailsResponseItem fromJson(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            GetSkuDetailsResponseItem getSkuDetailsResponseItem = new GetSkuDetailsResponseItem();
            getSkuDetailsResponseItem.productId = stringFromJson(jSONObject, Billing.PRODUCT_IDENTIFIER);
            getSkuDetailsResponseItem.type = stringFromJson(jSONObject, Keys.TYPE);
            getSkuDetailsResponseItem.price = stringFromJson(jSONObject, "price");
            getSkuDetailsResponseItem.price_amount_micros = stringFromJson(jSONObject, "price_amount_micros");
            getSkuDetailsResponseItem.price_currency_code = stringFromJson(jSONObject, Billing.PRODUCT_CURRENCY_CODE);
            getSkuDetailsResponseItem.title = stringFromJson(jSONObject, Keys.TITLE);
            getSkuDetailsResponseItem.description = stringFromJson(jSONObject, "description");
            return getSkuDetailsResponseItem;
        } catch (Throwable e) {
            Log.e(TAG, "Failed to parse GetSkuDetailsResponseItem", e);
            return null;
        }
    }

    static GetSkuDetailsResponseItem fromPurchasableItemDetails(PurchasableItemDetails purchasableItemDetails) {
        GetSkuDetailsResponseItem getSkuDetailsResponseItem = new GetSkuDetailsResponseItem();
        getSkuDetailsResponseItem.productId = purchasableItemDetails.getItemId();
        getSkuDetailsResponseItem.type = "inapp";
        getSkuDetailsResponseItem.price = purchasableItemDetails.getPrice();
        getSkuDetailsResponseItem.price_amount_micros = BuildConfig.FLAVOR;
        getSkuDetailsResponseItem.price_currency_code = BuildConfig.FLAVOR;
        getSkuDetailsResponseItem.title = purchasableItemDetails.getTitle();
        getSkuDetailsResponseItem.description = purchasableItemDetails.getDescription();
        return getSkuDetailsResponseItem;
    }

    private static String stringFromJson(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException e) {
            return BuildConfig.FLAVOR;
        }
    }

    static PurchasableItemDetails toPurchasableItemDetails(GetSkuDetailsResponseItem getSkuDetailsResponseItem) {
        return new PurchasableItemDetails(getSkuDetailsResponseItem.productId, getSkuDetailsResponseItem.title, getSkuDetailsResponseItem.description, getSkuDetailsResponseItem.price);
    }

    String getCurrencyCode() {
        return this.price_currency_code;
    }

    int getPriceE6() {
        try {
            return Integer.parseInt(this.price_amount_micros);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    String getProductId() {
        return this.productId;
    }
}
