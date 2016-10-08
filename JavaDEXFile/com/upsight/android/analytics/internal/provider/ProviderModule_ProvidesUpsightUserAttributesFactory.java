package com.upsight.android.analytics.internal.provider;

import com.upsight.android.analytics.provider.UpsightUserAttributes;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ProviderModule_ProvidesUpsightUserAttributesFactory implements Factory<UpsightUserAttributes> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ProviderModule module;
    private final Provider<UserAttributes> userAttributesProvider;

    static {
        $assertionsDisabled = !ProviderModule_ProvidesUpsightUserAttributesFactory.class.desiredAssertionStatus();
    }

    public ProviderModule_ProvidesUpsightUserAttributesFactory(ProviderModule providerModule, Provider<UserAttributes> provider) {
        if ($assertionsDisabled || providerModule != null) {
            this.module = providerModule;
            if ($assertionsDisabled || provider != null) {
                this.userAttributesProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<UpsightUserAttributes> create(ProviderModule providerModule, Provider<UserAttributes> provider) {
        return new ProviderModule_ProvidesUpsightUserAttributesFactory(providerModule, provider);
    }

    public UpsightUserAttributes get() {
        return (UpsightUserAttributes) Preconditions.checkNotNull(this.module.providesUpsightUserAttributes((UserAttributes) this.userAttributesProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
