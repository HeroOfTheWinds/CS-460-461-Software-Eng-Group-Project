package com.upsight.mediation.vast.Postroll;

import android.app.Activity;
import android.view.ViewGroup;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import com.upsight.mediation.mraid.MRAIDInterstitialListener;
import com.upsight.mediation.mraid.MRAIDNativeFeatureListener;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;

public class MRaidPostroll implements Postroll, MRAIDInterstitialListener, MRAIDNativeFeatureListener {
    private static final String BASE_URL = "";
    private static final String TAG = "MRaidPostroll";
    private final Activity mActivity;
    private final String mHtml;
    private MRAIDInterstitial mInterstitial;
    private final Listener mListener;
    private boolean mReady;
    private int previousOrientation;

    public MRaidPostroll(Activity activity, String str, Listener listener) {
        this.mReady = false;
        this.mActivity = activity;
        this.mHtml = str;
        this.mListener = listener;
    }

    public void hide() {
    }

    public void init() {
        if (this.mInterstitial != null) {
            FuseLog.m240w(TAG, "Tried to call init on already init'd mraid postroll");
        } else {
            this.mInterstitial = new MRAIDInterstitial(this.mActivity, BASE_URL, this.mHtml, new String[0], this, this);
        }
    }

    public boolean isReady() {
        return this.mReady;
    }

    public void mraidInterstitialAcceptPressed(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialFailedToLoad(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialHide(MRAIDInterstitial mRAIDInterstitial) {
        this.mActivity.setRequestedOrientation(this.previousOrientation);
        this.mListener.closeClicked();
    }

    public void mraidInterstitialLoaded(MRAIDInterstitial mRAIDInterstitial) {
        this.mReady = true;
    }

    public void mraidInterstitialRejectPressed(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidInterstitialReplayVideoPressed(MRAIDInterstitial mRAIDInterstitial) {
        this.mActivity.setRequestedOrientation(this.previousOrientation);
        this.mListener.replayedClicked();
    }

    public void mraidInterstitialShow(MRAIDInterstitial mRAIDInterstitial) {
    }

    public void mraidNativeFeatureCallTel(String str) {
    }

    public void mraidNativeFeatureCreateCalendarEvent(String str) {
    }

    public void mraidNativeFeatureOpenBrowser(String str) {
        if (this.mListener != null) {
            this.mListener.infoClicked(false);
            this.mListener.onOpenMRaidUrl(str);
        }
    }

    public void mraidNativeFeatureOpenMarket(String str) {
    }

    public void mraidNativeFeaturePlayVideo(String str) {
    }

    public void mraidNativeFeatureSendSms(String str) {
    }

    public void mraidNativeFeatureStorePicture(String str) {
    }

    public void mraidRewardComplete() {
    }

    public void show(ViewGroup viewGroup) {
        this.previousOrientation = this.mActivity.getRequestedOrientation();
        this.mActivity.setRequestedOrientation(4);
        this.mInterstitial.show();
    }
}
