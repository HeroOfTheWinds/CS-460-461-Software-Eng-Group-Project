package com.upsight.android.marketing.internal.content;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import com.google.gson.JsonObject;
import com.upsight.android.Upsight;
import com.upsight.android.marketing.C0949R;
import com.upsight.android.marketing.UpsightBillboard.Dimensions;
import com.upsight.android.marketing.UpsightBillboard.Dimensions.LayoutOrientation;
import com.upsight.android.marketing.UpsightBillboard.PresentationStyle;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.billboard.BillboardFragment;
import com.upsight.android.marketing.internal.billboard.BillboardFragment.BackPressHandler;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.android.marketing.internal.content.MarketingContentModel.Presentation;
import com.upsight.android.marketing.internal.content.MarketingContentModel.Presentation.DialogLayout;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class DefaultContentMediator extends UpsightContentMediator<MarketingContentModel> {
    public static final String CONTENT_PROVIDER = "upsight";

    /* renamed from: com.upsight.android.marketing.internal.content.DefaultContentMediator.1 */
    class C09601 implements OnClickListener {
        final /* synthetic */ MarketingContent val$content;

        C09601(MarketingContent marketingContent) {
            this.val$content = marketingContent;
        }

        public void onClick(View view) {
            this.val$content.executeActions(MarketingContent.TRIGGER_CONTENT_DISMISSED);
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.content.DefaultContentMediator.2 */
    class C09612 implements BackPressHandler {
        private boolean mIsDismissed;
        final /* synthetic */ MarketingContent val$content;

        C09612(MarketingContent marketingContent) {
            this.val$content = marketingContent;
            this.mIsDismissed = false;
        }

        public boolean onBackPress() {
            if (!this.mIsDismissed) {
                this.val$content.executeActions(MarketingContent.TRIGGER_CONTENT_DISMISSED);
                this.mIsDismissed = true;
            }
            return true;
        }
    }

    DefaultContentMediator() {
    }

    public MarketingContentModel buildContentModel(MarketingContent<MarketingContentModel> marketingContent, MarketingContentActionContext marketingContentActionContext, JsonObject jsonObject) {
        try {
            return MarketingContentModel.from(jsonObject, marketingContentActionContext.mGson);
        } catch (IOException e) {
            marketingContentActionContext.mLogger.m207e(Upsight.LOG_TAG, "Failed to parse content model", e);
            return null;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public View buildContentView(MarketingContent<MarketingContentModel> marketingContent, MarketingContentActionContext marketingContentActionContext) {
        View inflate = LayoutInflater.from(marketingContentActionContext.mUpsight).inflate(C0949R.layout.upsight_marketing_content_view, null);
        ((ImageView) inflate.findViewById(C0949R.id.upsight_marketing_content_view_close_button)).setOnClickListener(new C09601(marketingContent));
        WebView webView = (WebView) inflate.findViewById(C0949R.id.upsight_marketing_content_view_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(marketingContentActionContext.mContentTemplateWebViewClientFactory.create(marketingContent));
        webView.loadUrl(((MarketingContentModel) marketingContent.getContentModel()).getTemplateUrl());
        return inflate;
    }

    public void displayContent(MarketingContent<MarketingContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment) {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        billboardFragment.getContentViewContainer().addView(marketingContent.getContentView(), layoutParams);
        billboardFragment.setBackPressHandler(new C09612(marketingContent));
        if (!billboardFragment.isAdded()) {
            billboardFragment.show(fragmentManager, null);
        }
        marketingContent.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
    }

    public String getContentProvider() {
        return CONTENT_PROVIDER;
    }

    public Set<Dimensions> getDimensions(MarketingContent<MarketingContentModel> marketingContent) {
        Set<Dimensions> hashSet = new HashSet();
        DialogLayout dialogLayouts = ((MarketingContentModel) marketingContent.getContentModel()).getDialogLayouts();
        if (dialogLayouts != null) {
            if (dialogLayouts.portrait != null && dialogLayouts.portrait.f262w > 0 && dialogLayouts.portrait.f261h > 0) {
                hashSet.add(new Dimensions(LayoutOrientation.Portrait, dialogLayouts.portrait.f262w, dialogLayouts.portrait.f261h));
            }
            if (dialogLayouts.landscape != null && dialogLayouts.landscape.f262w > 0 && dialogLayouts.landscape.f261h > 0) {
                hashSet.add(new Dimensions(LayoutOrientation.Landscape, dialogLayouts.landscape.f262w, dialogLayouts.landscape.f261h));
            }
        }
        return hashSet;
    }

    public PresentationStyle getPresentationStyle(MarketingContent<MarketingContentModel> marketingContent) {
        String presentationStyle = ((MarketingContentModel) marketingContent.getContentModel()).getPresentationStyle();
        return Presentation.STYLE_DIALOG.equals(presentationStyle) ? PresentationStyle.Dialog : Presentation.STYLE_FULLSCREEN.equals(presentationStyle) ? PresentationStyle.Fullscreen : PresentationStyle.None;
    }

    public void hideContent(MarketingContent<MarketingContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment) {
        billboardFragment.getContentViewContainer().removeAllViews();
    }
}
