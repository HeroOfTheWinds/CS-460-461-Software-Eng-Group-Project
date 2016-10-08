package com.upsight.android.analytics.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.internal.DynamicIdentifiers;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("com.upsight.dynamic")
public final class UpsightDynamicEvent extends UpsightAnalyticsEvent<JsonElement, JsonElement> implements DynamicIdentifiers {
    private String identifiers;

    public static final class Builder {
        private static final JsonParser JSON_PARSER;
        private String identifiers;
        private JsonObject publisherData;
        private final String type;
        private JsonObject upsightData;

        static {
            JSON_PARSER = new JsonParser();
        }

        private Builder(String str) {
            this.publisherData = new JsonObject();
            this.upsightData = new JsonObject();
            this.type = str;
        }

        private UpsightDynamicEvent build() {
            return new UpsightDynamicEvent(this.type, this.identifiers, this.upsightData, this.publisherData);
        }

        private JsonObject deepCopy(JsonObject jsonObject) {
            return JSON_PARSER.parse(jsonObject.toString()).getAsJsonObject();
        }

        public Builder putPublisherData(JsonObject jsonObject) {
            this.publisherData = deepCopy(jsonObject);
            return this;
        }

        public Builder putUpsightData(JsonObject jsonObject) {
            this.upsightData = deepCopy(jsonObject);
            return this;
        }

        public final UpsightDynamicEvent record(UpsightContext upsightContext) {
            UpsightAnalyticsEvent build = build();
            UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
            if (upsightAnalyticsExtension != null) {
                upsightAnalyticsExtension.getApi().record(build);
            }
            return build;
        }

        public Builder setDynamicIdentifiers(String str) {
            this.identifiers = str;
            return this;
        }
    }

    UpsightDynamicEvent() {
    }

    UpsightDynamicEvent(String str, String str2, JsonElement jsonElement, JsonElement jsonElement2) {
        super(str, jsonElement, jsonElement2);
        this.identifiers = str2;
    }

    public static Builder createBuilder(String str) {
        return new Builder(null);
    }

    public String getIdentifiersName() {
        return this.identifiers;
    }
}
