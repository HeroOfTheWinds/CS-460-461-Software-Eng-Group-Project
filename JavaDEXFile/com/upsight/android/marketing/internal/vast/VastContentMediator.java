package com.upsight.android.marketing.internal.vast;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityTrackEvent;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.internal.billboard.BillboardFragment;
import com.upsight.android.marketing.internal.content.MarketingContent;
import com.upsight.android.marketing.internal.content.MarketingContentActions.MarketingContentActionContext;
import com.upsight.mediation.ads.adapters.NetworkWrapper;
import com.upsight.mediation.ads.adapters.NetworkWrapper.Listener;
import com.upsight.mediation.ads.adapters.VastAdAdapter;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import spacemadness.com.lunarconsole.C1518R;

public final class VastContentMediator extends UpsightContentMediator<VastContentModel> {
    private static final String CONTENT_PROVIDER = "vast";
    public static final String LOG_TAG;
    private VastAdAdapter mAdapter;
    private Bus mBus;
    private WeakReference<Activity> mCurrentActivity;
    private UpsightLogger mLogger;

    /* renamed from: com.upsight.android.marketing.internal.vast.VastContentMediator.1 */
    class C09751 implements Listener {
        final /* synthetic */ MarketingContent val$content;
        final /* synthetic */ VastContentModel val$model;

        C09751(MarketingContent marketingContent, VastContentModel vastContentModel) {
            this.val$content = marketingContent;
            this.val$model = vastContentModel;
        }

        public int getID() {
            return this.val$model.getAdapterId().intValue();
        }

        public void onAdClicked() {
        }

        public void onAdClosed() {
            VastContentMediator.this.mLogger.m209i(VastContentMediator.LOG_TAG, "onAdClosed", new Object[0]);
            this.val$content.executeActions(this.val$content.isRewardGranted() ? MarketingContent.TRIGGER_CONTENT_DISMISSED_WITH_REWARD : MarketingContent.TRIGGER_CONTENT_DISMISSED);
        }

        public void onAdCompleted() {
        }

        public void onAdDisplayed() {
            VastContentMediator.this.mLogger.m209i(VastContentMediator.LOG_TAG, "onAdDisplayed", new Object[0]);
            this.val$content.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
        }

        public void onAdFailedToDisplay() {
            VastContentMediator.this.mLogger.m213w(VastContentMediator.LOG_TAG, "Failed to display VAST content", new Object[0]);
        }

        public void onAdFailedToLoad(AdapterLoadError adapterLoadError) {
            VastContentMediator.this.mLogger.m213w(VastContentMediator.LOG_TAG, "Failed to load VAST content", new Object[0]);
        }

        public void onAdLoaded() {
            VastContentMediator.this.mLogger.m209i(VastContentMediator.LOG_TAG, "onAdLoaded", new Object[0]);
            this.val$content.markLoaded(VastContentMediator.this.mBus);
        }

        public void onAdSkipped() {
        }

        public void onOfferAccepted() {
        }

        public void onOfferDisplayed(Offer offer) {
            VastContentMediator.this.mLogger.m209i(VastContentMediator.LOG_TAG, "onOfferDisplayed", new Object[0]);
            this.val$content.executeActions(MarketingContent.TRIGGER_CONTENT_DISPLAYED);
        }

        public void onOfferRejected() {
        }

        public void onOpenMRaidUrl(@NonNull String str) {
        }

        public void onRewardedVideoCompleted() {
            VastContentMediator.this.mLogger.m209i(VastContentMediator.LOG_TAG, "onRewardedVideoCompleted", new Object[0]);
            this.val$content.markRewardGranted();
        }

        public void onVastError(int i) {
            VastContentMediator.this.mLogger.m213w(VastContentMediator.LOG_TAG, "onVastError i=" + i, new Object[0]);
        }

        public void onVastProgress(int i) {
        }

        public void onVastReplay() {
        }

        public void onVastSkip() {
        }

        public void sendRequestToBeacon(String str) {
        }
    }

    /* renamed from: com.upsight.android.marketing.internal.vast.VastContentMediator.2 */
    static /* synthetic */ class C09762 {
        static final /* synthetic */ int[] f265xa86dd1d6;

        static {
            f265xa86dd1d6 = new int[ActivityState.values().length];
            try {
                f265xa86dd1d6[ActivityState.CREATED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f265xa86dd1d6[ActivityState.RESUMED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f265xa86dd1d6[ActivityState.STARTED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f265xa86dd1d6[ActivityState.PAUSED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    static {
        LOG_TAG = VastContentMediator.class.getSimpleName();
    }

    public VastContentMediator(UpsightLogger upsightLogger, Bus bus) {
        this.mCurrentActivity = null;
        this.mAdapter = null;
        this.mLogger = upsightLogger;
        this.mBus = bus;
        this.mBus.register(this);
    }

    public VastContentModel buildContentModel(MarketingContent<VastContentModel> marketingContent, MarketingContentActionContext marketingContentActionContext, JsonObject jsonObject) {
        try {
            return VastContentModel.from(jsonObject, marketingContentActionContext.mGson);
        } catch (IOException e) {
            this.mLogger.m207e(LOG_TAG, "Failed to parse content model", e);
            return null;
        }
    }

    public View buildContentView(MarketingContent<VastContentModel> marketingContent, MarketingContentActionContext marketingContentActionContext) {
        if (this.mCurrentActivity != null) {
            Activity activity = (Activity) this.mCurrentActivity.get();
            if (activity != null) {
                VastContentModel vastContentModel = (VastContentModel) marketingContent.getContentModel();
                this.mAdapter = new VastAdAdapter();
                this.mAdapter.init();
                HashMap settings = ((VastContentModel) marketingContent.getContentModel()).getSettings();
                settings.put(NetworkWrapper.MAX_FILE_SIZE, vastContentModel.getMaxVastFileSize());
                settings.put(NetworkWrapper.IS_REWARDED, Boolean.toString(vastContentModel.isRewarded().booleanValue()));
                settings.put(NetworkWrapper.SHOULD_VALIDATE_SCHEMA, Boolean.toString(vastContentModel.shouldValidateSchema().booleanValue()));
                this.mAdapter.setListener(new C09751(marketingContent, vastContentModel));
                this.mAdapter.loadAd(activity, settings);
            }
        }
        return null;
    }

    public void displayContent(MarketingContent<VastContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment) {
        this.mAdapter.displayAd();
    }

    public String getContentProvider() {
        return CONTENT_PROVIDER;
    }

    @Subscribe
    public void handleActivityTrackEvent(ActivityTrackEvent activityTrackEvent) {
        switch (C09762.f265xa86dd1d6[activityTrackEvent.mActivityState.ordinal()]) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                this.mCurrentActivity = new WeakReference(activityTrackEvent.mActivity);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                if (this.mCurrentActivity != null) {
                    this.mCurrentActivity.clear();
                    this.mCurrentActivity = null;
                }
            default:
        }
    }

    public void hideContent(MarketingContent<VastContentModel> marketingContent, FragmentManager fragmentManager, BillboardFragment billboardFragment) {
    }
}
