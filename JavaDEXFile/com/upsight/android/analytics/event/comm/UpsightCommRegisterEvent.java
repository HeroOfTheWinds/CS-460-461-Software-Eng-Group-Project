package com.upsight.android.analytics.event.comm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.internal.AnalyticsEvent;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.comm.register")
public class UpsightCommRegisterEvent extends AnalyticsEvent<UpsightData> {

    public static class Builder extends com.upsight.android.analytics.internal.AnalyticsEvent.Builder<UpsightCommRegisterEvent, UpsightData> {
        private String token;

        protected Builder() {
        }

        protected UpsightCommRegisterEvent build() {
            return new UpsightCommRegisterEvent("upsight.comm.register", new UpsightData(this), this.mPublisherDataBuilder.build());
        }

        public Builder setToken(String str) {
            this.token = str;
            return this;
        }
    }

    static class UpsightData {
        @SerializedName("token")
        @Expose
        String token;

        protected UpsightData() {
        }

        protected UpsightData(Builder builder) {
            this.token = builder.token;
        }

        public String getToken() {
            return this.token;
        }
    }

    protected UpsightCommRegisterEvent() {
    }

    protected UpsightCommRegisterEvent(String str, UpsightData upsightData, UpsightPublisherData upsightPublisherData) {
        super(str, upsightData, upsightPublisherData);
    }

    public static Builder createBuilder() {
        return new Builder();
    }
}
