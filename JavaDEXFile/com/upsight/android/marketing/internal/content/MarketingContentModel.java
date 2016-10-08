package com.upsight.android.marketing.internal.content;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;

public final class MarketingContentModel {
    @SerializedName("content_id")
    @Expose
    String contentId;
    @SerializedName("context")
    @Expose
    JsonElement context;
    @SerializedName("presentation")
    @Expose
    Presentation presentation;
    @SerializedName("url")
    @Expose
    String templateUrl;

    public static class Presentation {
        public static final String STYLE_DIALOG = "dialog";
        public static final String STYLE_FULLSCREEN = "fullscreen";
        @SerializedName("layout")
        @Expose
        DialogLayout layout;
        @SerializedName("style")
        @Expose
        String style;

        public static class DialogLayout {
            @SerializedName("landscape")
            @Expose
            public Dimensions landscape;
            @SerializedName("portrait")
            @Expose
            public Dimensions portrait;

            public static class Dimensions {
                @SerializedName("h")
                @Expose
                public int f261h;
                @SerializedName("w")
                @Expose
                public int f262w;
                @SerializedName("x")
                @Expose
                public int f263x;
                @SerializedName("y")
                @Expose
                public int f264y;
            }
        }
    }

    MarketingContentModel() {
    }

    static MarketingContentModel from(JsonElement jsonElement, Gson gson) throws IOException {
        try {
            return (MarketingContentModel) gson.fromJson(jsonElement, MarketingContentModel.class);
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    public String getContentID() {
        return this.contentId;
    }

    public JsonElement getContext() {
        return this.context;
    }

    public DialogLayout getDialogLayouts() {
        return this.presentation != null ? this.presentation.layout : null;
    }

    public String getPresentationStyle() {
        return this.presentation != null ? this.presentation.style : null;
    }

    public String getTemplateUrl() {
        return this.templateUrl;
    }
}
