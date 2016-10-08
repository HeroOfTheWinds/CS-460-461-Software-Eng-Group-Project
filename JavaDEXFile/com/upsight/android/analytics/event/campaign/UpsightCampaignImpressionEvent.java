package com.upsight.android.analytics.event.campaign;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.campaign.impression")
public class UpsightCampaignImpressionEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCampaignImpressionEvent, UpsightData> {
        private Integer adGameId;
        private Integer adTypeId;
        private JsonArray ads;
        private Integer campaignId;
        private Integer contentId;
        private Integer contentTypeId;
        private Integer creativeId;
        private String impressionId;
        private Integer ordinal;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(String str, Integer num, Integer num2, Integer num3) {
            this.streamId = str;
            this.campaignId = num;
            this.creativeId = num2;
            this.contentId = num3;
        }

        protected UpsightCampaignImpressionEvent build() {
            return new UpsightCampaignImpressionEvent("upsight.campaign.impression", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setAdGameId(Integer num) {
            this.adGameId = num;
            return this;
        }

        public Builder setAdTypeId(Integer num) {
            this.adTypeId = num;
            return this;
        }

        public Builder setAds(JSONArray jSONArray) {
            this.ads = JSONArraySerializer.toJsonArray(jSONArray);
            return this;
        }

        public Builder setContentTypeId(Integer num) {
            this.contentTypeId = num;
            return this;
        }

        public Builder setImpressionId(String str) {
            this.impressionId = str;
            return this;
        }

        public Builder setOrdinal(Integer num) {
            this.ordinal = num;
            return this;
        }

        public Builder setScope(String str) {
            this.scope = str;
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
        @SerializedName("ad_game_id")
        @Expose
        Integer adGameId;
        @SerializedName("ad_type_id")
        @Expose
        Integer adTypeId;
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("campaign_id")
        @Expose
        Integer campaignId;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("content_type_id")
        @Expose
        Integer contentTypeId;
        @SerializedName("creative_id")
        @Expose
        Integer creativeId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
        @SerializedName("ordinal")
        @Expose
        Integer ordinal;
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
            this.ordinal = builder.ordinal;
            this.impressionId = builder.impressionId;
            this.ads = builder.ads;
            this.creativeId = builder.creativeId;
            this.campaignId = builder.campaignId;
            this.adTypeId = builder.adTypeId;
            this.streamId = builder.streamId;
            this.adGameId = builder.adGameId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
            this.testDevice = builder.testDevice;
            this.contentTypeId = builder.contentTypeId;
        }

        public Integer getAdGameId() {
            return this.adGameId;
        }

        public Integer getAdTypeId() {
            return this.adTypeId;
        }

        public JSONArray getAds() {
            return JSONArraySerializer.fromJsonArray(this.ads);
        }

        public Integer getCampaignId() {
            return this.campaignId;
        }

        public Integer getContentId() {
            return this.contentId;
        }

        public Integer getContentTypeId() {
            return this.contentTypeId;
        }

        public Integer getCreativeId() {
            return this.creativeId;
        }

        public String getImpressionId() {
            return this.impressionId;
        }

        public Integer getOrdinal() {
            return this.ordinal;
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

    protected UpsightCampaignImpressionEvent() {
    }

    protected UpsightCampaignImpressionEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(String str, Integer num, Integer num2, Integer num3) {
        return new Builder(str, num, num2, num3);
    }
}
