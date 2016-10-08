package com.upsight.android.marketing.internal.content;

import com.squareup.otto.Bus;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightCoreComponent;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.android.marketing.internal.vast.VastContentMediator;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;

@Module
public final class ContentModule {
    @Singleton
    @Provides
    DefaultContentMediator provideDefaultContentMediator() {
        return new DefaultContentMediator();
    }

    @Singleton
    @Provides
    MarketingContentFactory provideMarketingContentFactory(UpsightContext upsightContext, @Named("main") Scheduler scheduler, MarketingContentMediatorManager marketingContentMediatorManager, MarketingContentStore marketingContentStore, ContentTemplateWebViewClientFactory contentTemplateWebViewClientFactory) {
        UpsightCoreComponent coreComponent = upsightContext.getCoreComponent();
        return new MarketingContentFactory(new MarketingContentActionContext(upsightContext, coreComponent.bus(), coreComponent.gson(), ((UpsightAnalyticsComponent) ((UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME)).getComponent()).clock(), scheduler.createWorker(), upsightContext.getLogger(), marketingContentMediatorManager, marketingContentStore, contentTemplateWebViewClientFactory));
    }

    @Singleton
    @Provides
    MarketingContentMediatorManager provideMarketingContentMediatorManager(DefaultContentMediator defaultContentMediator) {
        return new MarketingContentMediatorManager(defaultContentMediator);
    }

    @Singleton
    @Provides
    MarketingContentStore provideMarketingContentStore(MarketingContentStoreImpl marketingContentStoreImpl) {
        return marketingContentStoreImpl;
    }

    @Singleton
    @Provides
    MarketingContentStoreImpl provideMarketingContentStoreImpl(UpsightContext upsightContext) {
        Bus bus;
        Clock clock = null;
        UpsightCoreComponent coreComponent = upsightContext.getCoreComponent();
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) upsightContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (coreComponent == null || upsightAnalyticsExtension == null) {
            bus = null;
        } else {
            Bus bus2 = coreComponent.bus();
            clock = ((UpsightAnalyticsComponent) upsightAnalyticsExtension.getComponent()).clock();
            bus = bus2;
        }
        return new MarketingContentStoreImpl(bus, clock, upsightContext.getLogger());
    }

    @Singleton
    @Provides
    UpsightMarketingContentStore provideUpsightMarketingContentStore(MarketingContentStoreImpl marketingContentStoreImpl) {
        return marketingContentStoreImpl;
    }

    @Singleton
    @Provides
    VastContentMediator provideVastContentMediator(UpsightContext upsightContext) {
        return new VastContentMediator(upsightContext.getLogger(), upsightContext.getCoreComponent().bus());
    }
}
