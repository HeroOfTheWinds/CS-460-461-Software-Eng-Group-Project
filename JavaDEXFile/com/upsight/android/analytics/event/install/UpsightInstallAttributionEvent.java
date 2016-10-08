package com.upsight.android.analytics.event.install;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.install.attribution")
public class UpsightInstallAttributionEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightInstallAttributionEvent, UpsightData> {
        private String attributionCampaign;
        private String attributionCreative;
        private String attributionSource;
        private String streamId;
        private String streamStartTs;

        protected Builder() {
        }

        protected UpsightInstallAttributionEvent build() {
            return new UpsightInstallAttributionEvent("upsight.install.attribution", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setAttributionCampaign(String str) {
            this.attributionCampaign = str;
            return this;
        }

        public Builder setAttributionCreative(String str) {
            this.attributionCreative = str;
            return this;
        }

        public Builder setAttributionSource(String str) {
            this.attributionSource = str;
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
        @SerializedName("attribution_campaign")
        @Expose
        String attributionCampaign;
        @SerializedName("attribution_creative")
        @Expose
        String attributionCreative;
        @SerializedName("attribution_source")
        @Expose
        String attributionSource;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.attributionCampaign = builder.attributionCampaign;
            this.attributionCreative = builder.attributionCreative;
            this.attributionSource = builder.attributionSource;
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
        }

        public String getAttributionCampaign() {
            return this.attributionCampaign;
        }

        public String getAttributionCreative() {
            return this.attributionCreative;
        }

        public String getAttributionSource() {
            return this.attributionSource;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }
    }

    protected UpsightInstallAttributionEvent() {
    }

    protected UpsightInstallAttributionEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder() {
        return new Builder();
    }
}
