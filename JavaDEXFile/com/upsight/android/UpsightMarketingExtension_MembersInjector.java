package com.upsight.android;

import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import com.upsight.android.marketing.internal.content.MarketingContentFactory;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpsightMarketingExtension_MembersInjector implements MembersInjector<UpsightMarketingExtension> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<DefaultContentMediator> mDefaultContentMediatorProvider;
    private final Provider<MarketingContentFactory> mMarketingContentFactoryProvider;
    private final Provider<UpsightMarketingApi> mMarketingProvider;

    static {
        $assertionsDisabled = !UpsightMarketingExtension_MembersInjector.class.desiredAssertionStatus();
    }

    public UpsightMarketingExtension_MembersInjector(Provider<UpsightMarketingApi> provider, Provider<MarketingContentFactory> provider2, Provider<DefaultContentMediator> provider3) {
        if ($assertionsDisabled || provider != null) {
            this.mMarketingProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.mMarketingContentFactoryProvider = provider2;
                if ($assertionsDisabled || provider3 != null) {
                    this.mDefaultContentMediatorProvider = provider3;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<UpsightMarketingExtension> create(Provider<UpsightMarketingApi> provider, Provider<MarketingContentFactory> provider2, Provider<DefaultContentMediator> provider3) {
        return new UpsightMarketingExtension_MembersInjector(provider, provider2, provider3);
    }

    public static void injectMDefaultContentMediator(UpsightMarketingExtension upsightMarketingExtension, Provider<DefaultContentMediator> provider) {
        upsightMarketingExtension.mDefaultContentMediator = (DefaultContentMediator) provider.get();
    }

    public static void injectMMarketing(UpsightMarketingExtension upsightMarketingExtension, Provider<UpsightMarketingApi> provider) {
        upsightMarketingExtension.mMarketing = (UpsightMarketingApi) provider.get();
    }

    public static void injectMMarketingContentFactory(UpsightMarketingExtension upsightMarketingExtension, Provider<MarketingContentFactory> provider) {
        upsightMarketingExtension.mMarketingContentFactory = (MarketingContentFactory) provider.get();
    }

    public void injectMembers(UpsightMarketingExtension upsightMarketingExtension) {
        if (upsightMarketingExtension == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        upsightMarketingExtension.mMarketing = (UpsightMarketingApi) this.mMarketingProvider.get();
        upsightMarketingExtension.mMarketingContentFactory = (MarketingContentFactory) this.mMarketingContentFactoryProvider.get();
        upsightMarketingExtension.mDefaultContentMediator = (DefaultContentMediator) this.mDefaultContentMediatorProvider.get();
    }
}
