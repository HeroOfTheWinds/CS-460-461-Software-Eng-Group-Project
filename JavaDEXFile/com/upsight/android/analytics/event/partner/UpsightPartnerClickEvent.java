package com.upsight.android.analytics.event.partner;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.partner.click")
public class UpsightPartnerClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightPartnerClickEvent, UpsightData> {
        private JsonArray ads;
        private Integer contentId;
        private String impressionId;
        private Integer partnerId;
        private String partnerName;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(Integer num, String str, String str2, Integer num2) {
            this.partnerId = num;
            this.scope = str;
            this.streamId = str2;
            this.contentId = num2;
        }

        protected UpsightPartnerClickEvent build() {
            return new UpsightPartnerClickEvent("upsight.partner.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setAds(JSONArray jSONArray) {
            this.ads = JSONArraySerializer.toJsonArray(jSONArray);
            return this;
        }

        public Builder setImpressionId(String str) {
            this.impressionId = str;
            return this;
        }

        public Builder setPartnerName(String str) {
            this.partnerName = str;
            return this;
        }

        public Builder setStreamStartTs(String str) {
            this.streamStartTs = str;
            return this;
        }

        public Builder setTestDevice(Boolean bool) {
            this.testDevice = bool;
            return this;
        }
    }

    static class UpsightData {
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
        @SerializedName("partner_id")
        @Expose
        Integer partnerId;
        @SerializedName("partner_name")
        @Expose
        String partnerName;
        @SerializedName("scope")
        @Expose
        String scope;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;
        @SerializedName("test_device")
        @Expose
        Boolean testDevice;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.ads = builder.ads;
            this.partnerName = builder.partnerName;
            this.impressionId = builder.impressionId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.partnerId = builder.partnerId;
            this.testDevice = builder.testDevice;
        }

        public JSONArray getAds() {
            return JSONArraySerializer.fromJsonArray(this.ads);
        }

        public Integer getContentId() {
            return this.contentId;
        }

        public String getImpressionId() {
            return this.impressionId;
        }

        public Integer getPartnerId() {
            return this.partnerId;
        }

        public String getPartnerName() {
            return this.partnerName;
        }

        public String getScope() {
            return this.scope;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }

        public Boolean getTestDevice() {
            return this.testDevice;
        }
    }

    protected UpsightPartnerClickEvent() {
    }

    protected UpsightPartnerClickEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(Integer num, String str, String str2, Integer num2) {
        return new Builder(num, str, str2, num2);
    }
}
