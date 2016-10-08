package com.upsight.android.analytics.internal.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface GsonHelper {

    public static class JSONArraySerializer {
        private static JsonParser sJsonParser;

        static {
            sJsonParser = new JsonParser();
        }

        public static JSONArray fromJsonArray(JsonArray jsonArray) {
            JSONArray jSONArray;
            if (jsonArray != null) {
                try {
                    jSONArray = new JSONArray(jsonArray.toString());
                } catch (JSONException e) {
                    return null;
                }
            }
            jSONArray = null;
            return jSONArray;
        }

        public static JsonArray toJsonArray(JSONArray jSONArray) {
            try {
                return sJsonParser.parse(jSONArray.toString()).getAsJsonArray();
            } catch (JsonParseException e) {
                return null;
            }
        }
    }

    public static class JSONObjectSerializer {
        private static JsonParser sJsonParser;

        static {
            sJsonParser = new JsonParser();
        }

        public static JSONObject fromJsonObject(JsonObject jsonObject) {
            JSONObject jSONObject;
            if (jsonObject != null) {
                try {
                    jSONObject = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    return null;
                }
            }
            jSONObject = null;
            return jSONObject;
        }

        public static JsonObject toJsonObject(JSONObject jSONObject) {
            JsonObject jsonObject = null;
            if (jSONObject != null) {
                try {
                    jsonObject = sJsonParser.parse(jSONObject.toString()).getAsJsonObject();
                } catch (JsonParseException e) {
                }
            }
            return jsonObject;
        }
    }
}
