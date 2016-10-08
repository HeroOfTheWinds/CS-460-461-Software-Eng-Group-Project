package com.upsight.android.marketing.internal.content;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ContentModule_ProvideMarketingContentMediatorManagerFactory implements Factory<MarketingContentMediatorManager> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<DefaultContentMediator> defaultContentMediatorProvider;
    private final ContentModule module;

    static {
        $assertionsDisabled = !ContentModule_ProvideMarketingContentMediatorManagerFactory.class.desiredAssertionStatus();
    }

    public ContentModule_ProvideMarketingContentMediatorManagerFactory(ContentModule contentModule, Provider<DefaultContentMediator> provider) {
        if ($assertionsDisabled || contentModule != null) {
            this.module = contentModule;
            if ($assertionsDisabled || provider != null) {
                this.defaultContentMediatorProvider = provider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<MarketingContentMediatorManager> create(ContentModule contentModule, Provider<DefaultContentMediator> provider) {
        return new ContentModule_ProvideMarketingContentMediatorManagerFactory(contentModule, provider);
    }

    public MarketingContentMediatorManager get() {
        return (MarketingContentMediatorManager) Preconditions.checkNotNull(this.module.provideMarketingContentMediatorManager((DefaultContentMediator) this.defaultContentMediatorProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
