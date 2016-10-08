package com.upsight.android.googleadvertisingid.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.upsight.android.googleadvertisingid.internal.GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory */
public final class C0885xc835c10c implements Factory<GooglePlayAdvertisingProvider> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final GoogleAdvertisingProviderModule module;

    static {
        $assertionsDisabled = !C0885xc835c10c.class.desiredAssertionStatus();
    }

    public C0885xc835c10c(GoogleAdvertisingProviderModule googleAdvertisingProviderModule) {
        if ($assertionsDisabled || googleAdvertisingProviderModule != null) {
            this.module = googleAdvertisingProviderModule;
            return;
        }
        throw new AssertionError();
    }

    public static Factory<GooglePlayAdvertisingProvider> create(GoogleAdvertisingProviderModule googleAdvertisingProviderModule) {
        return new C0885xc835c10c(googleAdvertisingProviderModule);
    }

    public GooglePlayAdvertisingProvider get() {
        return (GooglePlayAdvertisingProvider) Preconditions.checkNotNull(this.module.provideGooglePlayAdvertisingProvider(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
