package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.marketing.UpsightPurchase;
import java.io.IOException;

public final class Purchase implements UpsightPurchase {
    @SerializedName("product")
    @Expose
    String product;
    @SerializedName("quantity")
    @Expose
    int quantity;

    Purchase() {
    }

    static UpsightPurchase from(JsonElement jsonElement, Gson gson) throws IOException {
        try {
            return (UpsightPurchase) gson.fromJson(jsonElement, Purchase.class);
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    public String getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
