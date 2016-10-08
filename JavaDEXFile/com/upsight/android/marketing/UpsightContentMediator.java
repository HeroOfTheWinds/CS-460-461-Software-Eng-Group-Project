package com.upsight.android.marketing;

import android.app.FragmentManager;
import android.view.View;
import com.google.gson.JsonObject;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightMarketingExtension;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.internal.billboard.BillboardFragment;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import java.util.HashSet;
import java.util.Set;

public abstract class UpsightContentMediator<T> {
    protected UpsightContentMediator() {
    }

    public static void register(UpsightContext upsightContext, UpsightContentMediator upsightContentMediator) {
        UpsightMarketingExtension upsightMarketingExtension = (UpsightMarketingExtension) upsightContext.getUpsightExtension(UpsightMarketingExtension.EXTENSION_NAME);
        if (upsightMarketingExtension != null) {
            upsightMarketingExtension.getApi().registerContentMediator(upsightContentMediator);
        } else {
            upsightContext.getLogger().m207e(Upsight.LOG_TAG, "com.upsight.extension.marketing must be registered in your Android Manifest", new Object[0]);
        }
    }

    public abstract T buildContentModel(MarketingContent<T> marketingContent, MarketingContentActionContext marketingContentActionContext, JsonObject jsonObject);

    public abstract View buildContentView(MarketingContent<T> marketingContent, MarketingContentActionContext marketingContentActionContext);

    public abstract void displayContent(MarketingContent<T> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment);

    public abstract String getContentProvider();

    public Set<Dimensions> getDimensions(MarketingContent<T> marketingContent) {
        return new HashSet();
    }

    public PresentationStyle getPresentationStyle(MarketingContent<T> marketingContent) {
        return PresentationStyle.None;
    }

    public abstract void hideContent(MarketingContent<T> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment);
}
