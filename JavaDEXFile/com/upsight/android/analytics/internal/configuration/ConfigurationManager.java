package com.upsight.android.analytics.internal.configuration;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.C0848R;
import com.upsight.android.analytics.configuration.UpsightConfiguration;
import com.upsight.android.analytics.dispatcher.EndpointResponse;
import com.upsight.android.analytics.event.config.UpsightConfigExpiredEvent;
import com.upsight.android.analytics.internal.DispatcherService.DestroyEvent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;

public final class ConfigurationManager {
    public static final String CONFIGURATION_RESPONSE_SUBTYPE = "upsight.configuration";
    public static final String CONFIGURATION_SUBTYPE = "upsight.configuration.configurationManager";
    private static final String LOG_TAG = "Configurator";
    private final Bus mBus;
    private final ManagerConfigParser mConfigParser;
    private Config mCurrentConfig;
    private final UpsightDataStore mDataStore;
    private UpsightSubscription mDataStoreSubscription;
    private boolean mIsLaunched;
    private boolean mIsOutOfSync;
    private final UpsightLogger mLogger;
    private final ConfigurationResponseParser mResponseParser;
    private Action0 mSyncAction;
    private final UpsightContext mUpsight;
    private final Worker mWorker;
    private Subscription mWorkerSubscription;

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.1 */
    class C08591 implements UpsightDataStoreListener<Set<UpsightConfiguration>> {
        C08591() {
        }

        public void onFailure(UpsightException upsightException) {
            ConfigurationManager.this.mLogger.m207e(ConfigurationManager.LOG_TAG, "Could not fetch existing configs from datastore", upsightException);
            if (ConfigurationManager.this.mCurrentConfig == null) {
                ConfigurationManager.this.applyDefaultConfiguration();
            }
        }

        public void onSuccess(Set<UpsightConfiguration> set) {
            if (ConfigurationManager.this.mCurrentConfig == null) {
                int i;
                if (set.size() > 0) {
                    i = 0;
                    for (UpsightConfiguration upsightConfiguration : set) {
                        if (upsightConfiguration.getScope().equals(ConfigurationManager.CONFIGURATION_SUBTYPE)) {
                            ConfigurationManager.this.mLogger.m205d(ConfigurationManager.LOG_TAG, "Apply local configurations", new Object[0]);
                            i = ConfigurationManager.this.applyConfiguration(upsightConfiguration.getConfiguration());
                        }
                    }
                } else {
                    i = 0;
                }
                if (i == 0) {
                    ConfigurationManager.this.applyDefaultConfiguration();
                }
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.2 */
    class C08602 implements Action0 {
        C08602() {
        }

        public void call() {
            ConfigurationManager.this.mLogger.m205d(ConfigurationManager.LOG_TAG, "Record config.expired", new Object[0]);
            UpsightConfigExpiredEvent.createBuilder().record(ConfigurationManager.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.configuration.ConfigurationManager.3 */
    class C08613 implements UpsightDataStoreListener<Set<UpsightConfiguration>> {
        C08613() {
        }

        public void onFailure(UpsightException upsightException) {
        }

        public void onSuccess(Set<UpsightConfiguration> set) {
            for (UpsightConfiguration remove : set) {
                ConfigurationManager.this.mDataStore.remove(remove);
            }
        }
    }

    public static final class Config {
        public final long requestInterval;

        Config(long j) {
            this.requestInterval = j;
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                if (((Config) obj).requestInterval != this.requestInterval) {
                    return false;
                }
            }
            return true;
        }

        public boolean isValid() {
            return this.requestInterval > 0;
        }
    }

    ConfigurationManager(UpsightContext upsightContext, UpsightDataStore upsightDataStore, ConfigurationResponseParser configurationResponseParser, ManagerConfigParser managerConfigParser, Scheduler scheduler, Bus bus, UpsightLogger upsightLogger) {
        this.mIsLaunched = false;
        this.mSyncAction = new C08602();
        this.mUpsight = upsightContext;
        this.mDataStore = upsightDataStore;
        this.mResponseParser = configurationResponseParser;
        this.mConfigParser = managerConfigParser;
        this.mBus = bus;
        this.mLogger = upsightLogger;
        this.mWorker = scheduler.createWorker();
    }

    private boolean applyConfiguration(String str) {
        boolean z;
        synchronized (this) {
            try {
                Config parse = this.mConfigParser.parse(str);
                if (parse == null || !parse.isValid()) {
                    this.mLogger.m213w(LOG_TAG, "Incoming config is invalid", new Object[0]);
                    z = false;
                } else {
                    if (parse.equals(this.mCurrentConfig)) {
                        this.mLogger.m213w(LOG_TAG, "Current config is equals to incoming config, rejecting", new Object[0]);
                        z = true;
                    } else {
                        if (!(this.mWorkerSubscription == null || this.mWorkerSubscription.isUnsubscribed())) {
                            this.mLogger.m205d(LOG_TAG, "Stop config.expired recording scheduler", new Object[0]);
                            this.mWorkerSubscription.unsubscribe();
                        }
                        long j = this.mIsOutOfSync ? 0 : parse.requestInterval;
                        this.mLogger.m205d(LOG_TAG, "Schedule recording of config.expired every " + parse.requestInterval + " ms, mIsOutOfSync=" + this.mIsOutOfSync, new Object[0]);
                        this.mWorkerSubscription = this.mWorker.schedulePeriodically(this.mSyncAction, j, parse.requestInterval, TimeUnit.MILLISECONDS);
                        this.mIsOutOfSync = false;
                        this.mCurrentConfig = parse;
                        z = true;
                    }
                }
            } catch (IOException e) {
                this.mLogger.m207e(LOG_TAG, "Could not parse incoming configuration", e);
                z = false;
            }
        }
        return z;
    }

    private void applyDefaultConfiguration() {
        try {
            String iOUtils = IOUtils.toString(this.mUpsight.getResources().openRawResource(C0848R.raw.configurator_config));
            this.mLogger.m205d(LOG_TAG, "Apply default configurations", new Object[0]);
            applyConfiguration(iOUtils);
        } catch (IOException e) {
            this.mLogger.m207e(LOG_TAG, "Could not read default config", e);
        }
    }

    private void fetchCurrentConfig() {
        this.mDataStore.fetch(UpsightConfiguration.class, new C08591());
    }

    @Subscribe
    public void handle(DestroyEvent destroyEvent) {
        terminate();
    }

    public void launch() {
        synchronized (this) {
            if (!this.mIsLaunched) {
                this.mIsLaunched = true;
                this.mIsOutOfSync = true;
                this.mCurrentConfig = null;
                this.mDataStoreSubscription = this.mDataStore.subscribe(this);
                this.mWorkerSubscription = null;
                this.mBus.register(this);
                fetchCurrentConfig();
            }
        }
    }

    @Created
    public void onEndpointResponse(EndpointResponse endpointResponse) {
        if (CONFIGURATION_RESPONSE_SUBTYPE.equals(endpointResponse.getType())) {
            try {
                Collection<UpsightConfiguration> parse = this.mResponseParser.parse(endpointResponse.getContent());
                this.mDataStore.fetch(UpsightConfiguration.class, new C08613());
                for (UpsightConfiguration upsightConfiguration : parse) {
                    if (upsightConfiguration.getScope().equals(CONFIGURATION_SUBTYPE)) {
                        this.mLogger.m205d(LOG_TAG, "Apply received configurations", new Object[0]);
                        if (applyConfiguration(upsightConfiguration.getConfiguration())) {
                            this.mDataStore.store(upsightConfiguration);
                        }
                    } else {
                        this.mDataStore.store(upsightConfiguration);
                    }
                }
            } catch (IOException e) {
                this.mLogger.m207e(LOG_TAG, "Could not parse incoming configurations", e);
            }
        }
    }

    public void terminate() {
        synchronized (this) {
            this.mBus.unregister(this);
            if (this.mDataStoreSubscription != null) {
                this.mDataStoreSubscription.unsubscribe();
                this.mDataStoreSubscription = null;
            }
            if (this.mWorkerSubscription != null) {
                this.mLogger.m205d(LOG_TAG, "Stop config.expired recording scheduler", new Object[0]);
                this.mWorkerSubscription.unsubscribe();
                this.mWorkerSubscription = null;
            }
            this.mCurrentConfig = null;
            this.mIsLaunched = false;
        }
    }
}
