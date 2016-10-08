package com.upsight.android.analytics.provider;

import com.google.gson.annotations.Expose;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

public abstract class UpsightLocationTracker {

    @UpsightStorableType("upsight.model.location")
    public static final class Data {
        @UpsightStorableIdentifier
        String id;
        @Expose
        double latitude;
        @Expose
        double longitude;

        Data() {
        }

        private Data(double d, double d2) {
            this.latitude = d;
            this.longitude = d2;
        }

        public static Data create(double d, double d2) {
            return new Data(d, d2);
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Data data = (Data) obj;
                if (this.id != null) {
                    if (!this.id.equals(data.id)) {
                        return false;
                    }
                } else if (data.id != null) {
                    return false;
                }
            }
            return true;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public int hashCode() {
            return this.id != null ? this.id.hashCode() : 0;
        }

        public void setLatitude(double d) {
            this.latitude = d;
        }

        public void setLongitude(double d) {
            this.longitude = d;
        }
    }

    public static void purge(UpsightContext upsightContext) {
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (upsightAnalyticsExtension != null) {
            upsightAnalyticsExtension.getApi().purgeLocation();
        } else {
            upsightContext.getLogger().m207e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public static void track(UpsightContext upsightContext, Data data) {
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (upsightAnalyticsExtension != null) {
            upsightAnalyticsExtension.getApi().trackLocation(data);
        } else {
            upsightContext.getLogger().m207e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        }
    }

    public abstract void purge();

    public abstract void track(Data data);
}
