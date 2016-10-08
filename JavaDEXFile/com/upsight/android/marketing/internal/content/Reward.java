package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.marketing.UpsightReward;
import java.io.IOException;
import org.json.JSONObject;

public final class Reward implements UpsightReward {
    @SerializedName("product")
    @Expose
    String product;
    @SerializedName("quantity")
    @Expose
    int quantity;
    @SerializedName("signature_data")
    @Expose
    JsonObject signatureData;

    Reward() {
    }

    static UpsightReward from(JsonElement jsonElement, Gson gson) throws IOException {
        try {
            return (UpsightReward) gson.fromJson(jsonElement, Reward.class);
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

    public JSONObject getSignatureData() {
        return JSONObjectSerializer.fromJsonObject(this.signatureData);
    }
}
