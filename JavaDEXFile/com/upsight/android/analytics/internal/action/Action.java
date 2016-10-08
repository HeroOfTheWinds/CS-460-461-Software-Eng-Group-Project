package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Action<T extends Actionable, U extends ActionContext> {
    private U mActionContext;
    private JsonObject mParams;
    private String mType;

    protected Action(U u, String str, JsonObject jsonObject) {
        this.mActionContext = u;
        this.mType = str;
        this.mParams = jsonObject;
    }

    public abstract void execute(T t);

    public U getActionContext() {
        return this.mActionContext;
    }

    public String getType() {
        return this.mType;
    }

    protected int optParamInt(String str) {
        if (this.mParams != null) {
            JsonElement jsonElement = this.mParams.get(str);
            if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
                return jsonElement.getAsInt();
            }
        }
        return 0;
    }

    protected JsonArray optParamJsonArray(String str) {
        if (this.mParams != null) {
            JsonElement jsonElement = this.mParams.get(str);
            if (jsonElement != null && jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray();
            }
        }
        return null;
    }

    protected JsonObject optParamJsonObject(String str) {
        if (this.mParams != null) {
            JsonElement jsonElement = this.mParams.get(str);
            if (jsonElement != null && jsonElement.isJsonObject()) {
                return jsonElement.getAsJsonObject();
            }
        }
        return null;
    }

    protected String optParamString(String str) {
        if (this.mParams != null) {
            JsonElement jsonElement = this.mParams.get(str);
            if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                return jsonElement.getAsString();
            }
        }
        return null;
    }
}
