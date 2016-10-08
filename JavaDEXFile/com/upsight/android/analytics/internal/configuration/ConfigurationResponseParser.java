package com.upsight.android.analytics.internal.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.analytics.internal.session.SessionManager;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ConfigurationResponseParser {
    private Gson mGson;
    private SessionManager mSessionManager;

    public static class ConfigJson {
        @SerializedName("configuration")
        @Expose
        public JsonElement configuration;
        @SerializedName("type")
        @Expose
        public String type;
    }

    public static class ConfigResponseJson {
        @SerializedName("configurationList")
        @Expose
        public ConfigJson[] configs;
    }

    @Inject
    ConfigurationResponseParser(@Named("config-gson") Gson gson, SessionManager sessionManager) {
        this.mGson = gson;
        this.mSessionManager = sessionManager;
    }

    public Collection<UpsightConfiguration> parse(String str) throws IOException {
        try {
            ConfigResponseJson configResponseJson = (ConfigResponseJson) this.mGson.fromJson(str, ConfigResponseJson.class);
            Collection<UpsightConfiguration> linkedList = new LinkedList();
            for (ConfigJson configJson : configResponseJson.configs) {
                linkedList.add(UpsightConfiguration.create(configJson.type, configJson.configuration.toString(), this.mSessionManager.getCurrentSession().getSessionNumber()));
            }
            return linkedList;
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }
}
