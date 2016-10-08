package com.upsight.android.marketing.internal.content;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideMarketingContentStoreFactory implements Factory<MarketingContentStore> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<MarketingContentStoreImpl> implProvider;
    private final ContentModule module;

    static {
        $assertionsDisabled = !ContentModule_ProvideMarketingContentStoreFactory.class.desiredAssertionStatus();
    }

    public ContentModule_ProvideMarketingContentStoreFactory(ContentModule contentModule, Provider<MarketingContentStoreImpl> provider) {
        if ($assertionsDisabled || contentModule != null) {
            this.module = contentModule;
            if ($assertionsDisabled || provider != null) {
                this.implProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<MarketingContentStore> create(ContentModule contentModule, Provider<MarketingContentStoreImpl> provider) {
        return new ContentModule_ProvideMarketingContentStoreFactory(contentModule, provider);
    }

    public MarketingContentStore get() {
        return (MarketingContentStore) Preconditions.checkNotNull(this.module.provideMarketingContentStore((MarketingContentStoreImpl) this.implProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
