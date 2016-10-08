package com.upsight.android.analytics.dispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.dispatcher.delivery.status")
public final class AnalyticsEventDeliveryStatus {
    @SerializedName("id")
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("failure_reason")
    @Expose
    private String mFailureReason;
    @SerializedName("source_event_id")
    @Expose
    private String mOriginEventId;
    @SerializedName("status")
    @Expose
    private boolean mStatus;

    AnalyticsEventDeliveryStatus() {
    }

    AnalyticsEventDeliveryStatus(String str, boolean z, String str2) {
        this.mOriginEventId = str;
        this.mStatus = z;
        this.mFailureReason = str2;
    }

    public static AnalyticsEventDeliveryStatus fromFailure(String str, String str2) {
        return new AnalyticsEventDeliveryStatus(str, false, str2);
    }

    public static AnalyticsEventDeliveryStatus fromSuccess(String str) {
        return new AnalyticsEventDeliveryStatus(str, true, null);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AnalyticsEventDeliveryStatus analyticsEventDeliveryStatus = (AnalyticsEventDeliveryStatus) obj;
            if (this.id == null || analyticsEventDeliveryStatus.id == null) {
                return false;
            }
            if (!this.id.equals(analyticsEventDeliveryStatus.id)) {
                return false;
            }
        }
        return true;
    }

    public String getFailureReason() {
        return this.mFailureReason;
    }

    public String getSourceEventId() {
        return this.mOriginEventId;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public boolean wasDelivered() {
        return this.mStatus;
    }
}
