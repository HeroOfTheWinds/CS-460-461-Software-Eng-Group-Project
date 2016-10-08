package com.upsight.android.analytics.event.content;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONArraySerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONArray;

@UpsightStorableType("upsight.content.click")
public class UpsightContentClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentClickEvent, UpsightData> {
        private JsonArray ads;
        private Integer contentId;
        private String impressionId;
        private String scope;
        private String streamId;
        private String streamStartTs;
        private Boolean testDevice;

        protected Builder(String str, Integer num) {
            this.streamId = str;
            this.contentId = num;
        }

        protected UpsightContentClickEvent build() {
            return new UpsightContentClickEvent("upsight.content.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setAds(JSONArray jSONArray) {
            this.ads = JSONArraySerializer.toJsonArray(jSONArray);
            return this;
        }

        public Builder setImpressionId(String str) {
            this.impressionId = str;
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
        @SerializedName("ads")
        @Expose
        JsonArray ads;
        @SerializedName("content_id")
        @Expose
        Integer contentId;
        @SerializedName("impression_id")
        @Expose
        String impressionId;
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
            this.impressionId = builder.impressionId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.contentId = builder.contentId;
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

    protected UpsightContentClickEvent() {
    }

    protected UpsightContentClickEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(String str, Integer num) {
        return new Builder(str, num);
    }
}
