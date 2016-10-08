package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class BillboardDialogFragment_MembersInjector implements MembersInjector<BillboardDialogFragment> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Provider<MarketingContentStore> mContentStoreProvider;
    private final Provider<UpsightContext> mUpsightProvider;

    static {
        $assertionsDisabled = !BillboardDialogFragment_MembersInjector.class.desiredAssertionStatus();
    }

    public BillboardDialogFragment_MembersInjector(Provider<UpsightContext> provider, Provider<MarketingContentStore> provider2) {
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

    public static MembersInjector<BillboardDialogFragment> create(Provider<UpsightContext> provider, Provider<MarketingContentStore> provider2) {
        return new BillboardDialogFragment_MembersInjector(provider, provider2);
    }

    public static void injectMContentStore(BillboardDialogFragment billboardDialogFragment, Provider<MarketingContentStore> provider) {
        billboardDialogFragment.mContentStore = (MarketingContentStore) provider.get();
    }

    public static void injectMUpsight(BillboardDialogFragment billboardDialogFragment, Provider<UpsightContext> provider) {
        billboardDialogFragment.mUpsight = (UpsightContext) provider.get();
    }

    public void injectMembers(BillboardDialogFragment billboardDialogFragment) {
        if (billboardDialogFragment == null) {
            throw new NullPointerException("Cannot inject members into a null reference");
        }
        billboardDialogFragment.mUpsight = (UpsightContext) this.mUpsightProvider.get();
        billboardDialogFragment.mContentStore = (MarketingContentStore) this.mContentStoreProvider.get();
    }
}
