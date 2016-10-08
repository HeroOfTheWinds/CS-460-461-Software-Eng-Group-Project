package com.upsight.android.analytics.event;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Map.Entry;

@JsonAdapter(DefaultTypeAdapter.class)
public class UpsightPublisherData {
    private final JsonObject mDataMap;

    public static class Builder {
        private final JsonObject mDataMap;

        public Builder() {
            this.mDataMap = new JsonObject();
        }

        Builder(JsonObject jsonObject) {
            this.mDataMap = jsonObject;
        }

        public UpsightPublisherData build() {
            return new UpsightPublisherData();
        }

        public Builder put(UpsightPublisherData upsightPublisherData) {
            if (upsightPublisherData != null) {
                for (Entry entry : upsightPublisherData.mDataMap.entrySet()) {
                    this.mDataMap.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
            }
            return this;
        }

        public Builder put(String str, char c) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, String.valueOf(c));
            }
            return this;
        }

        public Builder put(String str, double d) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, Double.valueOf(d));
            }
            return this;
        }

        public Builder put(String str, float f) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, Float.valueOf(f));
            }
            return this;
        }

        public Builder put(String str, int i) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, Integer.valueOf(i));
            }
            return this;
        }

        public Builder put(String str, long j) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, Long.valueOf(j));
            }
            return this;
        }

        public Builder put(String str, CharSequence charSequence) {
            if (!(TextUtils.isEmpty(str) || charSequence == null)) {
                this.mDataMap.addProperty(str, charSequence.toString());
            }
            return this;
        }

        public Builder put(String str, boolean z) {
            if (!TextUtils.isEmpty(str)) {
                this.mDataMap.addProperty(str, Boolean.valueOf(z));
            }
            return this;
        }
    }

    public static final class DefaultTypeAdapter extends TypeAdapter<UpsightPublisherData> {
        private static final JsonParser JSON_PARSER;

        static {
            JSON_PARSER = new JsonParser();
        }

        public UpsightPublisherData read(JsonReader jsonReader) throws IOException {
            return new Builder(JSON_PARSER.parse(jsonReader).getAsJsonObject()).build();
        }

        public void write(JsonWriter jsonWriter, UpsightPublisherData upsightPublisherData) throws IOException {
            Streams.write(upsightPublisherData.mDataMap, jsonWriter);
        }
    }

    private UpsightPublisherData(Builder builder) {
        this.mDataMap = builder.mDataMap;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UpsightPublisherData upsightPublisherData = (UpsightPublisherData) obj;
        if (this.mDataMap != null) {
            if (this.mDataMap.equals(upsightPublisherData.mDataMap)) {
                return true;
            }
        } else if (upsightPublisherData.mDataMap == null) {
            return true;
        }
        return false;
    }

    public String getData(String str) {
        return this.mDataMap.get(str).toString();
    }

    public int hashCode() {
        return this.mDataMap != null ? this.mDataMap.hashCode() : 0;
    }
}
