package com.upsight.android.marketing.internal;

import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightMarketingApi;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import com.upsight.android.marketing.internal.billboard.Billboard;
import com.upsight.android.marketing.internal.content.MarketingContentMediatorManager;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class Marketing implements UpsightMarketingApi {
    private UpsightBillboardManager mBillboardManager;
    private MarketingContentMediatorManager mContentMediationManager;
    private UpsightMarketingContentStore mMarketingContentStore;

    @Inject
    public Marketing(UpsightBillboardManager upsightBillboardManager, UpsightMarketingContentStore upsightMarketingContentStore, MarketingContentMediatorManager marketingContentMediatorManager) {
        this.mBillboardManager = upsightBillboardManager;
        this.mMarketingContentStore = upsightMarketingContentStore;
        this.mContentMediationManager = marketingContentMediatorManager;
    }

    public boolean isContentReady(String str) {
        return this.mMarketingContentStore.isContentReady(str);
    }

    public boolean registerBillboard(Billboard billboard) {
        return this.mBillboardManager.registerBillboard(billboard);
    }

    public void registerContentMediator(UpsightContentMediator upsightContentMediator) {
        this.mContentMediationManager.register(upsightContentMediator);
    }

    public boolean unregisterBillboard(Billboard billboard) {
        return this.mBillboardManager.unregisterBillboard(billboard);
    }
}
