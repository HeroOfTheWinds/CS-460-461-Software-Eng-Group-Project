package com.upsight.android;

import com.upsight.android.managedvariables.UpsightManagedVariablesApi;
import com.upsight.android.managedvariables.internal.type.UxmBlockProvider;
import com.upsight.android.managedvariables.internal.type.UxmContentFactory;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightManagedVariablesExtension_MembersInjector implements MembersInjector<UpsightManagedVariablesExtension> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<UpsightManagedVariablesApi> mManagedVariablesProvider;
    private final Provider<UxmBlockProvider> mUxmBlockProvider;
    private final Provider<UxmContentFactory> mUxmContentFactoryProvider;

    static {
        $assertionsDisabled = !UpsightManagedVariablesExtension_MembersInjector.class.desiredAssertionStatus();
    }

    public UpsightManagedVariablesExtension_MembersInjector(Provider<UpsightManagedVariablesApi> provider, Provider<UxmContentFactory> provider2, Provider<UxmBlockProvider> provider3) {
        if ($assertionsDisabled || provider != null) {
            this.mManagedVariablesProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.mUxmContentFactoryProvider = provider2;
                if ($assertionsDisabled || provider3 != null) {
                    this.mUxmBlockProvider = provider3;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightManagedVariablesExtension> create(Provider<UpsightManagedVariablesApi> provider, Provider<UxmContentFactory> provider2, Provider<UxmBlockProvider> provider3) {
        return new UpsightManagedVariablesExtension_MembersInjector(provider, provider2, provider3);
    }

    public static void injectMManagedVariables(UpsightManagedVariablesExtension upsightManagedVariablesExtension, Provider<UpsightManagedVariablesApi> provider) {
        upsightManagedVariablesExtension.mManagedVariables = (UpsightManagedVariablesApi) provider.get();
    }

    public static void injectMUxmBlockProvider(UpsightManagedVariablesExtension upsightManagedVariablesExtension, Provider<UxmBlockProvider> provider) {
        upsightManagedVariablesExtension.mUxmBlockProvider = (UxmBlockProvider) provider.get();
    }

    public static void injectMUxmContentFactory(UpsightManagedVariablesExtension upsightManagedVariablesExtension, Provider<UxmContentFactory> provider) {
        upsightManagedVariablesExtension.mUxmContentFactory = (UxmContentFactory) provider.get();
    }

    public void injectMembers(UpsightManagedVariablesExtension upsightManagedVariablesExtension) {
        if (upsightManagedVariablesExtension == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        upsightManagedVariablesExtension.mManagedVariables = (UpsightManagedVariablesApi) this.mManagedVariablesProvider.get();
        upsightManagedVariablesExtension.mUxmContentFactory = (UxmContentFactory) this.mUxmContentFactoryProvider.get();
        upsightManagedVariablesExtension.mUxmBlockProvider = (UxmBlockProvider) this.mUxmBlockProvider.get();
    }
}
