package com.upsight.android.marketing.internal.content;

import com.upsight.android.UpsightContext;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import rx.Scheduler;

public final class ContentModule_ProvideMarketingContentFactoryFactory implements Factory<MarketingContentFactory> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<MarketingContentMediatorManager> contentMediatorManagerProvider;
    private final Provider<MarketingContentStore> contentStoreProvider;
    private final Provider<ContentTemplateWebViewClientFactory> contentTemplateWebViewClientFactoryProvider;
    private final ContentModule module;
    private final Provider<Scheduler> schedulerProvider;
    private final Provider<UpsightContext> upsightProvider;

    static {
        $assertionsDisabled = !ContentModule_ProvideMarketingContentFactoryFactory.class.desiredAssertionStatus();
    }

    public ContentModule_ProvideMarketingContentFactoryFactory(ContentModule contentModule, Provider<UpsightContext> provider, Provider<Scheduler> provider2, Provider<MarketingContentMediatorManager> provider3, Provider<MarketingContentStore> provider4, Provider<ContentTemplateWebViewClientFactory> provider5) {
        if ($assertionsDisabled || contentModule != null) {
            this.module = contentModule;
            if ($assertionsDisabled || provider != null) {
                this.upsightProvider = provider;
                if ($assertionsDisabled || provider2 != null) {
                    this.schedulerProvider = provider2;
                    if ($assertionsDisabled || provider3 != null) {
                        this.contentMediatorManagerProvider = provider3;
                        if ($assertionsDisabled || provider4 != null) {
                            this.contentStoreProvider = provider4;
                            if ($assertionsDisabled || provider5 != null) {
                                this.contentTemplateWebViewClientFactoryProvider = provider5;
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
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static Factory<MarketingContentFactory> create(ContentModule contentModule, Provider<UpsightContext> provider, Provider<Scheduler> provider2, Provider<MarketingContentMediatorManager> provider3, Provider<MarketingContentStore> provider4, Provider<ContentTemplateWebViewClientFactory> provider5) {
        return new ContentModule_ProvideMarketingContentFactoryFactory(contentModule, provider, provider2, provider3, provider4, provider5);
    }

    public MarketingContentFactory get() {
        return (MarketingContentFactory) Preconditions.checkNotNull(this.module.provideMarketingContentFactory((UpsightContext) this.upsightProvider.get(), (Scheduler) this.schedulerProvider.get(), (MarketingContentMediatorManager) this.contentMediatorManagerProvider.get(), (MarketingContentStore) this.contentStoreProvider.get(), (ContentTemplateWebViewClientFactory) this.contentTemplateWebViewClientFactoryProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }
}
