package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class Marketing_Factory implements Factory<Marketing> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<MarketingContentMediatorManager> contentMediatorManagerProvider;
    private final Provider<UpsightMarketingContentStore> marketingContentStoreProvider;

    static {
        $assertionsDisabled = !Marketing_Factory.class.desiredAssertionStatus();
    }

    public Marketing_Factory(Provider<UpsightBillboardManager> provider, Provider<UpsightMarketingContentStore> provider2, Provider<MarketingContentMediatorManager> provider3) {
        if ($assertionsDisabled || provider != null) {
            this.billboardManagerProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.marketingContentStoreProvider = provider2;
                if ($assertionsDisabled || provider3 != null) {
                    this.contentMediatorManagerProvider = provider3;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<Marketing> create(Provider<UpsightBillboardManager> provider, Provider<UpsightMarketingContentStore> provider2, Provider<MarketingContentMediatorManager> provider3) {
        return new Marketing_Factory(provider, provider2, provider3);
    }

    public Marketing get() {
        return new Marketing((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.marketingContentStoreProvider.get(), (MarketingContentMediatorManager) this.contentMediatorManagerProvider.get());
    }
}
