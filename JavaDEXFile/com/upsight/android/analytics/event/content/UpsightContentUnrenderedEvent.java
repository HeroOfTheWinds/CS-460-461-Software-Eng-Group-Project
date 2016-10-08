package com.upsight.android.analytics.event.content;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.analytics.internal.util.GsonHelper.JSONObjectSerializer;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import org.json.JSONObject;

@UpsightStorableType("upsight.content.unrendered")
public class UpsightContentUnrenderedEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightContentUnrenderedEvent, UpsightData> {
        private Integer campaignId;
        private JsonObject contentProvider;
        private String id;
        private String scope;
        private String streamId;
        private String streamStartTs;

        protected Builder(JSONObject jSONObject) {
            this.contentProvider = JSONObjectSerializer.toJsonObject(jSONObject);
        }

        protected UpsightContentUnrenderedEvent build() {
            return new UpsightContentUnrenderedEvent("upsight.content.unrendered", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setCampaignId(Integer num) {
            this.campaignId = num;
            return this;
        }

        public Builder setId(String str) {
            this.id = str;
            return this;
        }

        public Builder setScope(String str) {
            this.scope = str;
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
        @SerializedName("campaign_id")
        @Expose
        Integer campaignId;
        @SerializedName("content_provider")
        @Expose
        JsonObject contentProvider;
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("scope")
        @Expose
        String scope;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.contentProvider = builder.contentProvider;
            this.campaignId = builder.campaignId;
            this.streamId = builder.streamId;
            this.streamStartTs = builder.streamStartTs;
            this.scope = builder.scope;
            this.id = builder.id;
        }

        public Integer getCampaignId() {
            return this.campaignId;
        }

        public JSONObject getContentProvider() {
            return JSONObjectSerializer.fromJsonObject(this.contentProvider);
        }

        public String getId() {
            return this.id;
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
    }

    protected UpsightContentUnrenderedEvent() {
    }

    protected UpsightContentUnrenderedEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(JSONObject jSONObject) {
        return new Builder(jSONObject);
    }
}
