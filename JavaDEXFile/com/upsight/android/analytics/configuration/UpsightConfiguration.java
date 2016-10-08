package com.upsight.android.analytics.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.configuration")
public final class UpsightConfiguration {
    @SerializedName("id")
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("scope")
    @Expose
    private String mScope;
    @SerializedName("session_num_created")
    @Expose
    private int mSessionNumCreated;
    @SerializedName("value")
    @Expose
    private String mValue;

    UpsightConfiguration() {
    }

    UpsightConfiguration(String str, String str2, int i) {
        this.mScope = str;
        this.mValue = str2;
        this.mSessionNumCreated = i;
    }

    public static UpsightConfiguration create(String str, String str2, int i) {
        return new UpsightConfiguration(str, str2, i);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            UpsightConfiguration upsightConfiguration = (UpsightConfiguration) obj;
            if (this.id == null || upsightConfiguration.id == null) {
                return false;
            }
            if (!this.id.equals(upsightConfiguration.id)) {
                return false;
            }
        }
        return true;
    }

    public String getConfiguration() {
        return this.mValue;
    }

    public String getId() {
        return this.id;
    }

    public String getScope() {
        return this.mScope;
    }

    public int getSessionNumberCreated() {
        return this.mSessionNumCreated;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
