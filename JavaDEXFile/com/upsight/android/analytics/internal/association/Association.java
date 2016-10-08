package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;
import java.io.IOException;

@UpsightStorableType("upsight.association")
public class Association {
    @UpsightStorableIdentifier
    @Expose
    String id;
    @SerializedName("timestamp_ms")
    @Expose
    long timestampMs;
    @SerializedName("upsight_data")
    @Expose
    JsonObject upsightData;
    @SerializedName("upsight_data_filter")
    @Expose
    UpsightDataFilter upsightDataFilter;
    @SerializedName("with")
    @Expose
    String with;

    public static class UpsightDataFilter {
        @SerializedName("match_key")
        @Expose
        String matchKey;
        @SerializedName("match_values")
        @Expose
        JsonArray matchValues;

        public String getMatchKey() {
            return this.matchKey;
        }

        public JsonArray getMatchValues() {
            return this.matchValues;
        }
    }

    Association() {
    }

    private Association(String str, UpsightDataFilter upsightDataFilter, JsonObject jsonObject, long j) {
        this.with = str;
        this.upsightDataFilter = upsightDataFilter;
        this.upsightData = jsonObject;
        this.timestampMs = j;
    }

    public static Association from(String str, JsonObject jsonObject, JsonObject jsonObject2, Gson gson, Clock clock) throws IllegalArgumentException, IOException {
        if (TextUtils.isEmpty(str) || jsonObject == null || jsonObject2 == null) {
            throw new IllegalArgumentException("Illegal arguments");
        }
        try {
            return new Association(str, (UpsightDataFilter) gson.fromJson((JsonElement) jsonObject, UpsightDataFilter.class), jsonObject2, clock.currentTimeMillis());
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    public String getId() {
        return this.id;
    }

    public long getTimestampMs() {
        return this.timestampMs;
    }

    public JsonObject getUpsightData() {
        return this.upsightData;
    }

    public UpsightDataFilter getUpsightDataFilter() {
        return this.upsightDataFilter;
    }

    public String getWith() {
        return this.with;
    }
}
