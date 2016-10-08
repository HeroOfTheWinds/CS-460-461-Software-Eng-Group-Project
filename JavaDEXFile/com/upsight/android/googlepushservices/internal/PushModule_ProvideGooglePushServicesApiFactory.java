package com.upsight.android.googlepushservices.internal;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PushModule_ProvideGooglePushServicesApiFactory implements Factory<UpsightGooglePushServicesApi> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<GooglePushServices> googlePushServicesProvider;
    private final PushModule module;

    static {
        $assertionsDisabled = !PushModule_ProvideGooglePushServicesApiFactory.class.desiredAssertionStatus();
    }

    public PushModule_ProvideGooglePushServicesApiFactory(PushModule pushModule, Provider<GooglePushServices> provider) {
        if ($assertionsDisabled || pushModule != null) {
            this.module = pushModule;
            if ($assertionsDisabled || provider != null) {
                this.googlePushServicesProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<UpsightGooglePushServicesApi> create(PushModule pushModule, Provider<GooglePushServices> provider) {
        return new PushModule_ProvideGooglePushServicesApiFactory(pushModule, provider);
    }

    public UpsightGooglePushServicesApi get() {
        return (UpsightGooglePushServicesApi) Preconditions.checkNotNull(this.module.provideGooglePushServicesApi((GooglePushServices) this.googlePushServicesProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
