package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BaseMarketingModule_ProvideMarketingApiFactory implements Factory<UpsightMarketingApi> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<UpsightBillboardManager> billboardManagerProvider;
    private final Provider<MarketingContentMediatorManager> contentMediatorManagerProvider;
    private final Provider<UpsightMarketingContentStore> contentStoreProvider;
    private final BaseMarketingModule module;

    static {
        $assertionsDisabled = !BaseMarketingModule_ProvideMarketingApiFactory.class.desiredAssertionStatus();
    }

    public BaseMarketingModule_ProvideMarketingApiFactory(BaseMarketingModule baseMarketingModule, Provider<UpsightBillboardManager> provider, Provider<UpsightMarketingContentStore> provider2, Provider<MarketingContentMediatorManager> provider3) {
        if ($assertionsDisabled || baseMarketingModule != null) {
            this.module = baseMarketingModule;
            if ($assertionsDisabled || provider != null) {
                this.billboardManagerProvider = provider;
                if ($assertionsDisabled || provider2 != null) {
                    this.contentStoreProvider = provider2;
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
        throw new AssertionError();
    }

    public static Factory<UpsightMarketingApi> create(BaseMarketingModule baseMarketingModule, Provider<UpsightBillboardManager> provider, Provider<UpsightMarketingContentStore> provider2, Provider<MarketingContentMediatorManager> provider3) {
        return new BaseMarketingModule_ProvideMarketingApiFactory(baseMarketingModule, provider, provider2, provider3);
    }

    public UpsightMarketingApi get() {
        return (UpsightMarketingApi) Preconditions.checkNotNull(this.module.provideMarketingApi((UpsightBillboardManager) this.billboardManagerProvider.get(), (UpsightMarketingContentStore) this.contentStoreProvider.get(), (MarketingContentMediatorManager) this.contentMediatorManagerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
