package com.upsight.android.googlepushservices.internal;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.googlepushservices.C0886R;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import rx.Observable;
import rx.functions.Func1;

public class PushConfigManager {
    private static final String LOG_TAG;
    public static final String PUSH_CONFIGURATION_SUBTYPE = "upsight.configuration.push";
    private UpsightDataStore mDataStore;
    private Gson mGson;
    private UpsightLogger mLogger;
    private UpsightContext mUpsight;

    /* renamed from: com.upsight.android.googlepushservices.internal.PushConfigManager.1 */
    class C08971 implements Func1<Config, Boolean> {
        C08971() {
        }

        public Boolean call(Config config) {
            boolean z = config != null && config.isValid();
            return Boolean.valueOf(z);
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.PushConfigManager.2 */
    class C08982 implements Func1<UpsightConfiguration, Config> {
        C08982() {
        }

        public Config call(UpsightConfiguration upsightConfiguration) {
            return PushConfigManager.this.parseConfiguration(upsightConfiguration.getConfiguration());
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.PushConfigManager.3 */
    class C08993 implements Func1<UpsightConfiguration, Boolean> {
        C08993() {
        }

        public Boolean call(UpsightConfiguration upsightConfiguration) {
            return Boolean.valueOf(PushConfigManager.PUSH_CONFIGURATION_SUBTYPE.equals(upsightConfiguration.getScope()));
        }
    }

    public static final class Config {
        @SerializedName("auto_register")
        @Expose
        public boolean autoRegister;
        @SerializedName("push_token_ttl")
        @Expose
        public long pushTokenTtl;

        private boolean isValid() {
            return this.pushTokenTtl >= 0;
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Config config = (Config) obj;
                if (config.pushTokenTtl != this.pushTokenTtl) {
                    return false;
                }
                if (config.autoRegister != this.autoRegister) {
                    return false;
                }
            }
            return true;
        }
    }

    static {
        LOG_TAG = PushConfigManager.class.getSimpleName();
    }

    PushConfigManager(UpsightContext upsightContext) {
        this.mUpsight = upsightContext;
        this.mDataStore = this.mUpsight.getDataStore();
        this.mLogger = this.mUpsight.getLogger();
        this.mGson = upsightContext.getCoreComponent().gson();
    }

    private Config parseConfiguration(String str) {
        try {
            return (Config) this.mGson.fromJson(str, Config.class);
        } catch (JsonSyntaxException e) {
            this.mLogger.m207e(LOG_TAG, "Could not parse incoming config", e);
            return null;
        }
    }

    public Observable<Config> fetchCurrentConfigObservable() throws IOException {
        return this.mDataStore.fetchObservable(UpsightConfiguration.class).filter(new C08993()).map(new C08982()).filter(new C08971()).firstOrDefault(parseConfiguration(IOUtils.toString(this.mUpsight.getResources().openRawResource(C0886R.raw.push_config))));
    }
}
