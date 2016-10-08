package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.data.Offer;
import com.upsight.mediation.log.FuseLog;
import java.util.HashMap;

public abstract class NetworkWrapper {
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String BASE_URL = "baseUrl";
    public static final String CLOSE_BUTTON_DELAY = "closeButtonDelay";
    public static final String FUSE_SDK_VERSION = "fuse_sdk_version";
    public static final String IS_REWARDED = "isRewarded";
    public static final String IS_VIDEO = "isVideo";
    public static final String MAX_FILE_SIZE = "maxFileSize";
    public static final String REWARD_TIMER = "rewardTimer";
    public static final String ROTATE_MODE = "rotateMode";
    public static final String SHOULD_PRELOAD = "shouldPreload";
    public static final String SHOULD_VALIDATE_SCHEMA = "shouldValidateSchema";
    public static final String TIMEOUT = "timeout";
    public static final String VAST_CACHE_TO = "vast_cache_to";
    Listener listener;

    public interface Listener {
        int getID();

        void onAdClicked();

        void onAdClosed();

        void onAdCompleted();

        void onAdDisplayed();

        void onAdFailedToDisplay();

        void onAdFailedToLoad(AdapterLoadError adapterLoadError);

        void onAdLoaded();

        void onAdSkipped();

        void onOfferAccepted();

        void onOfferDisplayed(Offer offer);

        void onOfferRejected();

        void onOpenMRaidUrl(@NonNull String str);

        void onRewardedVideoCompleted();

        void onVastError(int i);

        void onVastProgress(int i);

        void onVastReplay();

        void onVastSkip();

        void sendRequestToBeacon(String str);
    }

    public abstract void displayAd();

    public int getID() {
        return this.listener.getID();
    }

    public abstract String getName();

    public abstract void init();

    public abstract boolean isAdAvailable();

    protected boolean isLandscape(Context context) {
        return !isPortrait(context);
    }

    protected boolean isPortrait(Context context) {
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels >= displayMetrics.widthPixels;
    }

    protected boolean isTablet() {
        return false;
    }

    public abstract void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap);

    protected void log(String str) {
        FuseLog.m235d(getName(), str);
    }

    protected void logError(String str) {
        FuseLog.m240w(getName(), str);
    }

    protected void logError(String str, Throwable th) {
        FuseLog.m241w(getName(), str, th);
    }

    protected final void onAdClicked() {
        if (this.listener != null) {
            this.listener.onAdClicked();
        }
    }

    protected final void onAdClosed() {
        if (this.listener != null) {
            this.listener.onAdClosed();
        }
    }

    protected final void onAdCompleted() {
        if (this.listener != null) {
            this.listener.onAdCompleted();
        }
    }

    protected final void onAdDisplayed() {
        if (this.listener != null) {
            this.listener.onAdDisplayed();
        }
    }

    protected final void onAdFailedToDisplay() {
        if (this.listener != null) {
            this.listener.onAdFailedToDisplay();
        }
    }

    protected final void onAdFailedToLoad(AdapterLoadError adapterLoadError) {
        if (this.listener != null) {
            this.listener.onAdFailedToLoad(adapterLoadError);
        }
    }

    protected final void onAdLoaded() {
        if (this.listener != null) {
            this.listener.onAdLoaded();
        }
    }

    protected final void onAdSkipped() {
        if (this.listener != null) {
            this.listener.onAdSkipped();
        }
    }

    protected final void onRewardedVideoCompleted() {
        if (this.listener != null) {
            this.listener.onRewardedVideoCompleted();
        }
    }

    public void resumedAfterAdDisplay() {
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
