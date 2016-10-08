package com.upsight.android.googlepushservices.internal;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.UpsightContext;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PushIntentService_MembersInjector implements MembersInjector<PushIntentService> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<GoogleCloudMessaging> mGcmProvider;
    private final Provider<UpsightContext> mUpsightProvider;

    static {
        $assertionsDisabled = !PushIntentService_MembersInjector.class.desiredAssertionStatus();
    }

    public PushIntentService_MembersInjector(Provider<GoogleCloudMessaging> provider, Provider<UpsightContext> provider2) {
        if ($assertionsDisabled || provider != null) {
            this.mGcmProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.mUpsightProvider = provider2;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<PushIntentService> create(Provider<GoogleCloudMessaging> provider, Provider<UpsightContext> provider2) {
        return new PushIntentService_MembersInjector(provider, provider2);
    }

    public static void injectMGcm(PushIntentService pushIntentService, Provider<GoogleCloudMessaging> provider) {
        pushIntentService.mGcm = (GoogleCloudMessaging) provider.get();
    }

    public static void injectMUpsight(PushIntentService pushIntentService, Provider<UpsightContext> provider) {
        pushIntentService.mUpsight = (UpsightContext) provider.get();
    }

    public void injectMembers(PushIntentService pushIntentService) {
        if (pushIntentService == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        pushIntentService.mGcm = (GoogleCloudMessaging) this.mGcmProvider.get();
        pushIntentService.mUpsight = (UpsightContext) this.mUpsightProvider.get();
    }
}
