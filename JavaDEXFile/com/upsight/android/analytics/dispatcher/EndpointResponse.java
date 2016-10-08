package com.upsight.android.analytics.dispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.dispatcher.response")
public final class EndpointResponse {
    @SerializedName("id")
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("content")
    @Expose
    private String mContent;
    @SerializedName("type")
    @Expose
    private String mType;

    EndpointResponse() {
    }

    EndpointResponse(String str, String str2) {
        this.mType = str;
        this.mContent = str2;
    }

    public static EndpointResponse create(String str, String str2) {
        return new EndpointResponse(str, str2);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EndpointResponse endpointResponse = (EndpointResponse) obj;
            if (this.id == null || endpointResponse.id == null) {
                return false;
            }
            if (!this.id.equals(endpointResponse.id)) {
                return false;
            }
        }
        return true;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getType() {
        return this.mType;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
