package com.upsight.android.analytics.event.monetization;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.monetization")
public class UpsightMonetizationEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightMonetizationEvent, UpsightData> {
        private String cookie;
        private String currency;
        private JsonObject iapBundle;
        private Double price;
        private String product;
        private Integer quantity;
        private String resolution;
        private String store;
        private String streamId;
        private String streamStartTs;
        private Double totalPrice;

        protected Builder(Double d, String str) {
            this.totalPrice = d;
            this.currency = str;
        }

        protected UpsightMonetizationEvent build() {
            return new UpsightMonetizationEvent("upsight.monetization", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setCookie(String str) {
            this.cookie = str;
            return this;
        }

        public Builder setIapBundle(JSONObject jSONObject) {
            this.iapBundle = JSONObjectSerializer.toJsonObject(jSONObject);
            return this;
        }

        public Builder setPrice(Double d) {
            this.price = d;
            return this;
        }

        public Builder setProduct(String str) {
            this.product = str;
            return this;
        }

        public Builder setQuantity(Integer num) {
            this.quantity = num;
            return this;
        }

        public Builder setResolution(String str) {
            this.resolution = str;
            return this;
        }

        public Builder setStore(String str) {
            this.store = str;
            return this;
        }

        public Builder setStreamId(String str) {
            this.streamId = str;
            return this;
        }

        public Builder setStreamStartTs(String str) {
            this.streamStartTs = str;
            return this;
        }
    }

    static class UpsightData {
        @SerializedName("cookie")
        @Expose
        String cookie;
        @SerializedName("currency")
        @Expose
        String currency;
        @SerializedName("iap_bundle")
        @Expose
        JsonObject iapBundle;
        @SerializedName("price")
        @Expose
        Double price;
        @SerializedName("product")
        @Expose
        String product;
        @SerializedName("quantity")
        @Expose
        Integer quantity;
        @SerializedName("resolution")
        @Expose
        String resolution;
        @SerializedName("store")
        @Expose
        String store;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;
        @SerializedName("total_price")
        @Expose
        Double totalPrice;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.product = builder.product;
            this.totalPrice = builder.totalPrice;
            this.streamId = builder.streamId;
            this.price = builder.price;
            this.currency = builder.currency;
            this.cookie = builder.cookie;
            this.iapBundle = builder.iapBundle;
            this.streamStartTs = builder.streamStartTs;
            this.resolution = builder.resolution;
            this.store = builder.store;
            this.quantity = builder.quantity;
        }

        public String getCookie() {
            return this.cookie;
        }

        public String getCurrency() {
            return this.currency;
        }

        public JSONObject getIapBundle() {
            return JSONObjectSerializer.fromJsonObject(this.iapBundle);
        }

        public Double getPrice() {
            return this.price;
        }

        public String getProduct() {
            return this.product;
        }

        public Integer getQuantity() {
            return this.quantity;
        }

        public String getResolution() {
            return this.resolution;
        }

        public String getStore() {
            return this.store;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public Double getTotalPrice() {
            return this.totalPrice;
        }
    }

    protected UpsightMonetizationEvent() {
    }

    protected UpsightMonetizationEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(Double d, String str) {
        return new Builder(d, str);
    }
}
