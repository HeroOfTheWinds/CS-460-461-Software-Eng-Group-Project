package com.upsight.android;

import com.upsight.android.googleadvertisingid.internal.GooglePlayAdvertisingProvider;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGoogleAdvertisingIdExtension_MembersInjector implements MembersInjector<UpsightGoogleAdvertisingIdExtension> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<GooglePlayAdvertisingProvider> mAdvertisingIdProvider;

    static {
        $assertionsDisabled = !UpsightGoogleAdvertisingIdExtension_MembersInjector.class.desiredAssertionStatus();
    }

    public UpsightGoogleAdvertisingIdExtension_MembersInjector(Provider<GooglePlayAdvertisingProvider> provider) {
        if ($assertionsDisabled || provider != null) {
            this.mAdvertisingIdProvider = provider;
            return;
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightGoogleAdvertisingIdExtension> create(Provider<GooglePlayAdvertisingProvider> provider) {
        return new UpsightGoogleAdvertisingIdExtension_MembersInjector(provider);
    }

    public static void injectMAdvertisingIdProvider(UpsightGoogleAdvertisingIdExtension upsightGoogleAdvertisingIdExtension, Provider<GooglePlayAdvertisingProvider> provider) {
        upsightGoogleAdvertisingIdExtension.mAdvertisingIdProvider = (GooglePlayAdvertisingProvider) provider.get();
    }

    public void injectMembers(UpsightGoogleAdvertisingIdExtension upsightGoogleAdvertisingIdExtension) {
        if (upsightGoogleAdvertisingIdExtension == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        upsightGoogleAdvertisingIdExtension.mAdvertisingIdProvider = (GooglePlayAdvertisingProvider) this.mAdvertisingIdProvider.get();
    }
}
