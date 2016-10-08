package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Session;
import com.upsight.android.analytics.internal.session.SessionInitializer;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class PushModule {
    private final UpsightContext mUpsight;

    /* renamed from: com.upsight.android.googlepushservices.internal.PushModule.1 */
    class C09011 implements SessionManager {
        C09011() {
        }

        public Session getCurrentSession() {
            return null;
        }

        public Session startSession(SessionInitializer sessionInitializer) {
            return null;
        }

        public void stopSession() {
        }
    }

    public PushModule(UpsightContext upsightContext) {
        this.mUpsight = upsightContext;
    }

    @Singleton
    @Provides
    public UpsightGooglePushServicesApi provideGooglePushServicesApi(GooglePushServices googlePushServices) {
        return googlePushServices;
    }

    @Singleton
    @Provides
    public PushConfigManager providePushConfigManager(UpsightContext upsightContext) {
        return new PushConfigManager(upsightContext);
    }

    @Singleton
    @Provides
    SessionManager provideSessionManager(UpsightContext upsightContext) {
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        return upsightAnalyticsExtension != null ? ((UpsightAnalyticsComponent) upsightAnalyticsExtension.getComponent()).sessionManager() : new C09011();
    }

    @Singleton
    @Provides
    UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }
}
