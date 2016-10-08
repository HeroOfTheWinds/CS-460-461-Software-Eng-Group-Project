package com.upsight.android.analytics.event.datacollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.data_collection")
public class UpsightDataCollectionEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightDataCollectionEvent, UpsightData> {
        private String dataBundle;
        private String format;
        private String streamId;
        private String streamStartTs;

        protected Builder(String str, String str2) {
            this.dataBundle = str;
            this.streamId = str2;
        }

        protected UpsightDataCollectionEvent build() {
            return new UpsightDataCollectionEvent("upsight.data_collection", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setFormat(String str) {
            this.format = str;
            return this;
        }

        public Builder setStreamStartTs(String str) {
            this.streamStartTs = str;
            return this;
        }
    }

    static class UpsightData {
        @SerializedName("data_bundle")
        @Expose
        String dataBundle;
        @SerializedName("format")
        @Expose
        String format;
        @SerializedName("stream_id")
        @Expose
        String streamId;
        @SerializedName("stream_start_ts")
        @Expose
        String streamStartTs;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.streamStartTs = builder.streamStartTs;
            this.streamId = builder.streamId;
            this.dataBundle = builder.dataBundle;
            this.format = builder.format;
        }

        public String getDataBundle() {
            return this.dataBundle;
        }

        public String getFormat() {
            return this.format;
        }

        public String getStreamId() {
            return this.streamId;
        }

        public String getStreamStartTs() {
            return this.streamStartTs;
        }
    }

    protected UpsightDataCollectionEvent() {
    }

    protected UpsightDataCollectionEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(String str, String str2) {
        return new Builder(str, str2);
    }
}
