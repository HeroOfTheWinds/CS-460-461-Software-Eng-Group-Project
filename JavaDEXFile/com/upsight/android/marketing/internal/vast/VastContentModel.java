package com.upsight.android.marketing.internal.vast;

import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.internal.session.SessionManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class VastContentModel {
    @SerializedName("adapter_id")
    @Expose
    Integer adapterId;
    @SerializedName("is_rewarded")
    @Expose
    Boolean isRewarded;
    @SerializedName("max_vast_file_size")
    @Expose
    String maxVastFileSize;
    @SerializedName("settings")
    @Expose
    Settings settings;
    @SerializedName("should_validate_schema")
    @Expose
    Boolean shouldValidateSchema;

    /* renamed from: com.upsight.android.marketing.internal.vast.VastContentModel.1 */
    class C09771 extends HashMap<String, String> {
        C09771() {
            put("beacon-click", VastContentModel.this.settings.beacons.click);
            put("beacon-impression", VastContentModel.this.settings.beacons.impression);
            put(SessionManager.SESSION_CAMPAIGN_ID, Integer.toString(VastContentModel.this.settings.campaignId.intValue()));
            put("cb_ms", Integer.toString(VastContentModel.this.settings.cbMs.intValue()));
            put("cta", VastContentModel.this.settings.cta);
            put(TriggerIfContentAvailable.ID, Integer.toString(VastContentModel.this.settings.id.intValue()));
            put("postroll", Integer.toString(VastContentModel.this.settings.postroll.intValue()));
            put("script", VastContentModel.this.settings.script);
            put("t", Integer.toString(VastContentModel.this.settings.f266t.intValue()));
            VastContentModel.this.appendEndcard(this);
        }
    }

    public static class Settings {
        @SerializedName("beacons")
        @Expose
        Beacons beacons;
        @SerializedName("campaign_id")
        @Expose
        Integer campaignId;
        @SerializedName("cb_ms")
        @Expose
        Integer cbMs;
        @SerializedName("cta")
        @Expose
        String cta;
        @SerializedName("endcard_script")
        @Expose
        String endcardScript;
        @SerializedName("id")
        @Expose
        Integer id;
        @SerializedName("is_endcard_script_encoded")
        @Expose
        Boolean isEndcardScriptEncoded;
        @SerializedName("postroll")
        @Expose
        Integer postroll;
        @SerializedName("script")
        @Expose
        String script;
        @SerializedName("t")
        @Expose
        Integer f266t;

        public static class Beacons {
            @SerializedName("click")
            @Expose
            String click;
            @SerializedName("impression")
            @Expose
            String impression;
        }
    }

    VastContentModel() {
    }

    private void appendEndcard(Map<String, String> map) {
        if (!TextUtils.isEmpty(this.settings.endcardScript)) {
            if (this.settings.isEndcardScriptEncoded == null || !this.settings.isEndcardScriptEncoded.booleanValue()) {
                map.put("endcard_script", this.settings.endcardScript);
                return;
            }
            try {
                map.put("endcard_script", Arrays.toString(Base64.decode(this.settings.endcardScript, 0)));
            } catch (IllegalArgumentException e) {
            }
        }
    }

    static VastContentModel from(JsonElement jsonElement, Gson gson) throws IOException {
        try {
            return (VastContentModel) gson.fromJson(jsonElement, VastContentModel.class);
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    public Integer getAdapterId() {
        return this.adapterId;
    }

    public String getMaxVastFileSize() {
        return this.maxVastFileSize;
    }

    public HashMap<String, String> getSettings() {
        return new C09771();
    }

    public Boolean isRewarded() {
        return this.isRewarded;
    }

    public Boolean shouldValidateSchema() {
        return this.shouldValidateSchema;
    }
}
