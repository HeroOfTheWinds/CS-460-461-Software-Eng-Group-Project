package com.upsight.android.marketing.internal;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Module
public final class BaseMarketingModule {
    public static final String SCHEDULER_MAIN = "main";
    private final UpsightContext mUpsight;

    public BaseMarketingModule(UpsightContext upsightContext) {
        this.mUpsight = upsightContext;
    }

    @Singleton
    @Provides
    @Named("main")
    Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Singleton
    @Provides
    UpsightMarketingApi provideMarketingApi(UpsightBillboardManager upsightBillboardManager, UpsightMarketingContentStore upsightMarketingContentStore, MarketingContentMediatorManager marketingContentMediatorManager) {
        return new Marketing(upsightBillboardManager, upsightMarketingContentStore, marketingContentMediatorManager);
    }

    @Singleton
    @Provides
    UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }
}
