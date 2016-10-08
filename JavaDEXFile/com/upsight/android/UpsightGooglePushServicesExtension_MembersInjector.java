package com.upsight.android;

import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.internal.PushConfigManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightGooglePushServicesExtension_MembersInjector implements MembersInjector<UpsightGooglePushServicesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<PushConfigManager> mPushConfigManagerProvider;
    private final Provider<UpsightGooglePushServicesApi> mUpsightPushProvider;

    static {
        $assertionsDisabled = !UpsightGooglePushServicesExtension_MembersInjector.class.desiredAssertionStatus();
    }

    public UpsightGooglePushServicesExtension_MembersInjector(Provider<UpsightGooglePushServicesApi> provider, Provider<PushConfigManager> provider2) {
        if ($assertionsDisabled || provider != null) {
            this.mUpsightPushProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.mPushConfigManagerProvider = provider2;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightGooglePushServicesExtension> create(Provider<UpsightGooglePushServicesApi> provider, Provider<PushConfigManager> provider2) {
        return new UpsightGooglePushServicesExtension_MembersInjector(provider, provider2);
    }

    public static void injectMPushConfigManager(UpsightGooglePushServicesExtension upsightGooglePushServicesExtension, Provider<PushConfigManager> provider) {
        upsightGooglePushServicesExtension.mPushConfigManager = (PushConfigManager) provider.get();
    }

    public static void injectMUpsightPush(UpsightGooglePushServicesExtension upsightGooglePushServicesExtension, Provider<UpsightGooglePushServicesApi> provider) {
        upsightGooglePushServicesExtension.mUpsightPush = (UpsightGooglePushServicesApi) provider.get();
    }

    public void injectMembers(UpsightGooglePushServicesExtension upsightGooglePushServicesExtension) {
        if (upsightGooglePushServicesExtension == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        upsightGooglePushServicesExtension.mUpsightPush = (UpsightGooglePushServicesApi) this.mUpsightPushProvider.get();
        upsightGooglePushServicesExtension.mPushConfigManager = (PushConfigManager) this.mPushConfigManagerProvider.get();
    }
}
