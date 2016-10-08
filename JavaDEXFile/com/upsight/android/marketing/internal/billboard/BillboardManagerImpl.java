package com.upsight.android.marketing.internal.billboard;

import android.content.Intent;
import android.text.TextUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboard.AttachParameters;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContent.ScopedAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.PurchasesEvent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.RewardsEvent;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import java.util.HashMap;
import java.util.Map;

class BillboardManagerImpl implements UpsightBillboardManager {
    private final MarketingContentStore mContentStore;
    private final Map<String, Billboard> mUnfilledBillboards;
    private final UpsightContext mUpsight;

    BillboardManagerImpl(UpsightContext upsightContext, MarketingContentStore marketingContentStore, Bus bus) {
        this.mUnfilledBillboards = new HashMap();
        this.mUpsight = upsightContext;
        this.mContentStore = marketingContentStore;
        bus.register(this);
    }

    private boolean tryAttachBillboard(String str, Billboard billboard) {
        MarketingContent marketingContent = (MarketingContent) this.mContentStore.get(str);
        if (!(billboard == null || marketingContent == null || !marketingContent.isAvailable() || marketingContent.getContentMediator() == null)) {
            AttachParameters onAttach = billboard.getHandler().onAttach(billboard.getScope());
            if (!(onAttach == null || onAttach.getActivity() == null)) {
                this.mUnfilledBillboards.remove(billboard.getScope());
                marketingContent.bindBillboard(billboard);
                Intent addFlags = new Intent(this.mUpsight, BillboardManagementActivity.class).putExtra("marketingContentId", marketingContent.getId()).putExtra("marketingContentPreferredStyle", onAttach.getPreferredPresentationStyle()).addFlags(268435456);
                Integer dialogTheme = onAttach.getDialogTheme();
                if (dialogTheme != null) {
                    addFlags.putExtra("marketingContentDialogTheme", dialogTheme.intValue());
                }
                this.mUpsight.startActivity(addFlags);
                return true;
            }
        }
        return false;
    }

    @Subscribe
    public void handleActionEvent(PurchasesEvent purchasesEvent) {
        synchronized (this) {
            MarketingContent marketingContent = (MarketingContent) this.mContentStore.get(purchasesEvent.mId);
            if (marketingContent != null) {
                Billboard boundBillboard = marketingContent.getBoundBillboard();
                if (boundBillboard != null) {
                    boundBillboard.getHandler().onPurchases(purchasesEvent.mPurchases);
                }
            }
        }
    }

    @Subscribe
    public void handleActionEvent(RewardsEvent rewardsEvent) {
        synchronized (this) {
            MarketingContent marketingContent = (MarketingContent) this.mContentStore.get(rewardsEvent.mId);
            if (marketingContent != null) {
                Billboard boundBillboard = marketingContent.getBoundBillboard();
                if (boundBillboard != null) {
                    boundBillboard.getHandler().onRewards(rewardsEvent.mRewards);
                }
            }
        }
    }

    @Subscribe
    public void handleAvailabilityEvent(ScopedAvailabilityEvent scopedAvailabilityEvent) {
        synchronized (this) {
            for (String str : scopedAvailabilityEvent.getScopes()) {
                if (tryAttachBillboard(scopedAvailabilityEvent.getId(), (Billboard) this.mUnfilledBillboards.get(str))) {
                    break;
                }
            }
        }
    }

    public boolean registerBillboard(Billboard billboard) {
        boolean z;
        synchronized (this) {
            z = false;
            if (billboard != null) {
                Object scope = billboard.getScope();
                if (!(TextUtils.isEmpty(scope) || billboard.getHandler() == null || this.mUnfilledBillboards.get(scope) != null)) {
                    this.mUnfilledBillboards.put(scope, billboard);
                    for (String tryAttachBillboard : this.mContentStore.getIdsForScope(scope)) {
                        if (tryAttachBillboard(tryAttachBillboard, billboard)) {
                            z = true;
                            break;
                        }
                    }
                    z = true;
                }
            }
        }
        return z;
    }

    public boolean unregisterBillboard(Billboard billboard) {
        boolean z;
        synchronized (this) {
            z = this.mUnfilledBillboards.remove(billboard.getScope()) != null;
        }
        return z;
    }
}
