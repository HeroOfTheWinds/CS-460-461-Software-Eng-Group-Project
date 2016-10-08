package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class BillboardManagementActivity_MembersInjector implements MembersInjector<BillboardManagementActivity> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<MarketingContentStore> mContentStoreProvider;
    private final Provider<UpsightContext> mUpsightProvider;

    static {
        $assertionsDisabled = !BillboardManagementActivity_MembersInjector.class.desiredAssertionStatus();
    }

    public BillboardManagementActivity_MembersInjector(Provider<UpsightContext> provider, Provider<MarketingContentStore> provider2) {
        if ($assertionsDisabled || provider != null) {
            this.mUpsightProvider = provider;
            if ($assertionsDisabled || provider2 != null) {
                this.mContentStoreProvider = provider2;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public static MembersInjector<BillboardManagementActivity> create(Provider<UpsightContext> provider, Provider<MarketingContentStore> provider2) {
        return new BillboardManagementActivity_MembersInjector(provider, provider2);
    }

    public static void injectMContentStore(BillboardManagementActivity billboardManagementActivity, Provider<MarketingContentStore> provider) {
        billboardManagementActivity.mContentStore = (MarketingContentStore) provider.get();
    }

    public static void injectMUpsight(BillboardManagementActivity billboardManagementActivity, Provider<UpsightContext> provider) {
        billboardManagementActivity.mUpsight = (UpsightContext) provider.get();
    }

    public void injectMembers(BillboardManagementActivity billboardManagementActivity) {
        if (billboardManagementActivity == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        billboardManagementActivity.mUpsight = (UpsightContext) this.mUpsightProvider.get();
        billboardManagementActivity.mContentStore = (MarketingContentStore) this.mContentStoreProvider.get();
    }
}
