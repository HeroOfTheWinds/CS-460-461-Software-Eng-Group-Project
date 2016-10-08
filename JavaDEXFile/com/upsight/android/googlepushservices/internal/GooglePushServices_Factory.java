package com.upsight.android.googlepushservices.internal;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GooglePushServices_Factory implements Factory<GooglePushServices> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<PushConfigManager> pushConfigManagerProvider;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !GooglePushServices_Factory.class.desiredAssertionStatus();
    }

    public GooglePushServices_Factory(Provider<UpsightContext> provider, Provider<PushConfigManager> provider2) {
        if ($assertionsDisabled || provider != null) {
            this.upsightProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.pushConfigManagerProvider = provider2;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<GooglePushServices> create(Provider<UpsightContext> provider, Provider<PushConfigManager> provider2) {
        return new GooglePushServices_Factory(provider, provider2);
    }

    public GooglePushServices get() {
        return new GooglePushServices((UpsightContext) this.upsightProvider.get(), (PushConfigManager) this.pushConfigManagerProvider.get());
    }
}
