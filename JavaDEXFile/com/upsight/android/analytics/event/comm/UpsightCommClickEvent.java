package com.upsight.android.analytics.event.comm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.comm.click")
public class UpsightCommClickEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommClickEvent, UpsightData> {
        private Integer msgCampaignId;
        private Integer msgId;

        protected Builder(Integer num) {
            this.msgId = num;
        }

        protected UpsightCommClickEvent build() {
            return new UpsightCommClickEvent("upsight.comm.click", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setMsgCampaignId(Integer num) {
            this.msgCampaignId = num;
            return this;
        }
    }

    static class UpsightData {
        @SerializedName("msg_campaign_id")
        @Expose
        Integer msgCampaignId;
        @SerializedName("msg_id")
        @Expose
        Integer msgId;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.msgId = builder.msgId;
            this.msgCampaignId = builder.msgCampaignId;
        }

        public Integer getMsgCampaignId() {
            return this.msgCampaignId;
        }

        public Integer getMsgId() {
            return this.msgId;
        }
    }

    protected UpsightCommClickEvent() {
    }

    protected UpsightCommClickEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder(Integer num) {
        return new Builder(num);
    }
}
