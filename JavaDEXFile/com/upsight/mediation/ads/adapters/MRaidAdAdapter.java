package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import com.upsight.mediation.mraid.MRAIDInterstitialListener;
import com.upsight.mediation.mraid.MRAIDNativeFeature;
import com.upsight.mediation.mraid.MRAIDNativeFeatureListener;
import com.upsight.mediation.util.StringUtil;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Sharing;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;

public class MRaidAdAdapter extends NetworkWrapperFuseInternal implements MRAIDNativeFeatureListener, MRAIDInterstitialListener {
    private static final int DISPLAY_MRAID_ACTIVITY_REQUEST_CODE = 0;
    public static final String NAME = "MRAID";
    private static final String TAG = "MRaidAdAdapter";
    static MRaidRegistry mRaidRegistry;
    private WeakReference<MRaidActivity> MRaidActivity;
    protected Activity activity;
    protected int backgroundColor;
    String baseUrl;
    protected String clickBeacon;
    private int closeButtonDelay;
    protected boolean hasReportedClose;
    protected String htmlBody;
    protected String impressionBeacon;
    protected MRAIDInterstitial interstitial;
    private boolean isRewarded;
    protected boolean loaded;
    private String name;
    protected int registryId;
    private boolean returnToInterstitial;
    private int rewardTimer;
    protected int rotateMode;
    protected boolean shouldPreload;
    private Date startDisplayTime;

    static {
        mRaidRegistry = new MRaidRegistry();
    }

    public MRaidAdAdapter() {
        this.loaded = false;
        this.hasReportedClose = false;
    }

    private void preloadInterstitial() {
        this.loaded = false;
        this.interstitial = new MRAIDInterstitial(this.activity, this.baseUrl, this.htmlBody, this.backgroundColor, new String[]{MRAIDNativeFeature.CALENDAR, MRAIDNativeFeature.INLINE_VIDEO, MRAIDNativeFeature.STORE_PICTURE, Sharing.SMS, MRAIDNativeFeature.TEL}, this, this);
        this.interstitial.setOrientationConfig(this.rotateMode);
        this.htmlBody = null;
    }

    public void displayAd() {
        if (!isAdAvailable()) {
            onAdFailedToDisplay();
        } else if (this.shouldPreload) {
            displayInterstitial();
        } else {
            preloadInterstitial();
        }
    }

    void displayInterstitial() {
        Intent intent = new Intent(this.activity, MRaidActivity.class);
        intent.putExtra("registryId", this.registryId);
        intent.putExtra("rotate", this.rotateMode);
        intent.setFlags(AccessibilityNodeInfoCompat.ACTION_CUT);
        this.activity.startActivity(intent);
        this.hasReportedClose = false;
        this.loaded = false;
    }

    public MRAIDInterstitial getInterstitial() {
        return this.interstitial;
    }

    public MRaidActivity getMRaidActivity() {
        return this.MRaidActivity != null ? (MRaidActivity) this.MRaidActivity.get() : null;
    }

    public String getName() {
        if (this.name == null) {
            this.name = new StringBuilder(NAME).append(": ").append(getID()).toString();
        }
        return this.name;
    }

    public void init() {
        this.registryId = mRaidRegistry.register(this);
        this.baseUrl = null;
    }

    public boolean isAdAvailable() {
        boolean z = this.loaded;
        return this.shouldPreload ? z && this.interstitial != null && this.interstitial.isReady : z;
    }

    public boolean isDisplaying() {
        MRaidActivity mRaidActivity = getMRaidActivity();
        return mRaidActivity != null ? mRaidActivity.isVisible : false;
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap) {
        this.isRewarded = Boolean.parseBoolean((String) hashMap.get(NetworkWrapper.IS_REWARDED));
        this.activity = activity;
        this.htmlBody = (String) hashMap.get("script");
        if (StringUtil.isNullOrEmpty(this.htmlBody)) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
            return;
        }
        this.clickBeacon = (String) hashMap.get("beacon-click");
        this.impressionBeacon = (String) hashMap.get("beacon-impression");
        this.baseUrl = (String) hashMap.get(NetworkWrapper.BASE_URL);
        this.shouldPreload = Boolean.parseBoolean((String) hashMap.get(NetworkWrapper.SHOULD_PRELOAD));
        this.returnToInterstitial = Boolean.parseBoolean((String) hashMap.get(NetworkWrapper.REWARD_TIMER));
        try {
            this.backgroundColor = Integer.parseInt((String) hashMap.get(NetworkWrapper.BACKGROUND_COLOR));
            this.rotateMode = Integer.parseInt((String) hashMap.get(NetworkWrapper.ROTATE_MODE));
            this.rewardTimer = Integer.parseInt((String) hashMap.get(NetworkWrapper.REWARD_TIMER));
            this.closeButtonDelay = Integer.parseInt((String) hashMap.get(NetworkWrapper.REWARD_TIMER));
            if (this.shouldPreload) {
                preloadInterstitial();
                return;
            }
            this.loaded = true;
            onAdLoaded();
        } catch (NumberFormatException e) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
        }
    }

    public void mraidInterstitialAcceptPressed(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialFailedToLoad(MRAIDInterstitial mRAIDInterstitial) {
        FuseLog.m235d(TAG, "MRAID Ad Failed to Load");
        onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
    }

    public void mraidInterstitialFailedToShow() {
        onAdFailedToDisplay();
    }

    public void mraidInterstitialHide(MRAIDInterstitial mRAIDInterstitial) {
        boolean z = true;
        FuseLog.m239v(TAG, "MRAID Ad Hidden");
        if (!this.hasReportedClose) {
            this.hasReportedClose = true;
            if (this.isRewarded && this.startDisplayTime != null) {
                if (new Date().getTime() - this.startDisplayTime.getTime() <= ((long) this.rewardTimer) || this.rewardTimer <= 0) {
                    z = false;
                }
                if (z) {
                    onRewardedVideoCompleted();
                }
            }
            onAdClosed();
        }
        MRaidActivity mRaidActivity = getMRaidActivity();
        if (mRaidActivity != null) {
            mRaidActivity.finish();
        }
    }

    public void mraidInterstitialLoaded(MRAIDInterstitial mRAIDInterstitial) {
        if (this.shouldPreload) {
            FuseLog.m239v(TAG, "MRAID Ad Loaded");
            this.loaded = true;
            onAdLoaded();
            return;
        }
        displayInterstitial();
    }

    public void mraidInterstitialRejectPressed(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialReplayVideoPressed(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialShow(MRAIDInterstitial mRAIDInterstitial) {
        FuseLog.m239v(TAG, "MRAID Ad Displayed");
        onAdDisplayed();
        if (this.impressionBeacon != null) {
            sendRequestToBeacon(this.impressionBeacon);
        }
        if (this.isRewarded) {
            this.startDisplayTime = new Date();
        }
    }

    public void mraidNativeFeatureCallTel(String str) {
        FuseLog.m239v(TAG, "MRAID Ad Wants to make phone call " + str);
    }

    public void mraidNativeFeatureCreateCalendarEvent(String str) {
        FuseLog.m239v(TAG, "MRAID Ad Wants to create calendar event: " + str);
    }

    public void mraidNativeFeatureOpenBrowser(String str) {
        FuseLog.m239v(TAG, "Ad Wants to display browser: " + str);
        if (this.clickBeacon != null) {
            sendRequestToBeacon(this.clickBeacon);
        }
        onAdClicked();
        onOpenMRaidUrl(str);
        mraidInterstitialHide(this.interstitial);
    }

    public void mraidNativeFeatureOpenMarket(String str) {
        FuseLog.m239v(TAG, "Ad Wants to display market: " + str);
        if (this.clickBeacon != null) {
            sendRequestToBeacon(this.clickBeacon);
        }
        onAdClicked();
        onOpenMRaidUrl(str);
        mraidInterstitialHide(this.interstitial);
    }

    public void mraidNativeFeaturePlayVideo(String str) {
        FuseLog.m239v(TAG, "MRAID Ad Wants to play video: " + str);
        Context mRaidActivity = getMRaidActivity();
        if (mRaidActivity != null) {
            Intent intent = new Intent(mRaidActivity, MRaidVideoActivity.class);
            intent.putExtra(Keys.URL, str);
            intent.putExtra("cb_ms", this.closeButtonDelay);
            intent.putExtra("rti", this.returnToInterstitial);
            mRaidActivity.startActivityForResult(intent, 1);
        }
    }

    public void mraidNativeFeatureSendSms(String str) {
        FuseLog.m239v(TAG, "Ad Wants to send SMS: " + str);
    }

    public void mraidNativeFeatureStorePicture(String str) {
        FuseLog.m239v(TAG, "Ad Wants to store a picture: " + str);
    }

    public void mraidRewardComplete() {
        onRewardedVideoCompleted();
    }

    public void setMRaidActivity(MRaidActivity mRaidActivity) {
        this.MRaidActivity = new WeakReference(mRaidActivity);
    }

    public boolean verifyParameters(HashMap<String, String> hashMap) {
        return !StringUtil.isNullOrEmpty((String) hashMap.get("script"));
    }
}
