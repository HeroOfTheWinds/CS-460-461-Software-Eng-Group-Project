package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;
import com.upsight.mediation.ads.model.AdapterLoadError;
import com.upsight.mediation.util.StringUtil;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.VASTPlayer.VASTPlayerListener;
import com.voxelbusters.nativeplugins.utilities.FileUtility;
import java.util.HashMap;
import spacemadness.com.lunarconsole.C1518R;

public class VastAdAdapter extends NetworkWrapperFuseInternal implements VASTPlayerListener {
    public static final String NAME = "VAST";
    private static final String TAG = "VastAdAdapter";
    private Activity activity;
    private VASTPlayer interstitial;
    private boolean isRewarded;

    public VastAdAdapter() {
        this.isRewarded = false;
    }

    public void displayAd() {
        if (isAdAvailable()) {
            this.interstitial.play();
        } else {
            onAdFailedToDisplay();
        }
    }

    public String getName() {
        return new StringBuilder(NAME).append(": ").append(getID()).toString();
    }

    @CallSuper
    public void init() {
    }

    public boolean isAdAvailable() {
        return this.interstitial != null && this.interstitial.isLoaded();
    }

    public void loadAd(@NonNull Activity activity, @NonNull HashMap<String, String> hashMap) {
        int parseInt;
        boolean z;
        boolean z2;
        String str;
        int parseInt2;
        this.isRewarded = Boolean.parseBoolean((String) hashMap.get(NetworkWrapper.IS_REWARDED));
        String str2 = (String) hashMap.get("cta");
        String str3 = (str2 == null || str2.length() == 0) ? "Learn More" : str2;
        String str4 = (String) hashMap.get(NetworkWrapper.MAX_FILE_SIZE);
        if (StringUtil.isNullOrEmpty(str4)) {
            onAdFailedToLoad(AdapterLoadError.INVALID_PARAMETERS);
            return;
        }
        try {
            parseInt = Integer.parseInt((String) hashMap.get(NetworkWrapper.CLOSE_BUTTON_DELAY));
        } catch (NumberFormatException e) {
            log("Could not parse close button delay");
            parseInt = -1;
            str2 = (String) hashMap.get("postroll");
            if (str2 == null) {
            }
            str2 = (String) hashMap.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
            if (str2 == null) {
            }
            str = (String) hashMap.get("endcard_script");
            parseInt2 = Integer.parseInt((String) hashMap.get(NetworkWrapper.VAST_CACHE_TO));
            str2 = (String) hashMap.get("script");
            if (str2 != null) {
                try {
                    this.interstitial = new VASTPlayer(activity, this, z, str, (long) parseInt, this.isRewarded, str4, z2, str3, parseInt2);
                    if (URLUtil.isValidUrl(str2)) {
                        this.interstitial.loadVastResponseViaXML(str2);
                        return;
                    } else {
                        this.interstitial.loadVastResponseViaURL(str2);
                        return;
                    }
                } catch (Throwable e2) {
                    logError("Vast failed to load to to unexpected error", e2);
                }
            }
            onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
        } catch (NullPointerException e3) {
            log("Could not parse close button delay");
            parseInt = -1;
            str2 = (String) hashMap.get("postroll");
            if (str2 == null) {
            }
            str2 = (String) hashMap.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
            if (str2 == null) {
            }
            str = (String) hashMap.get("endcard_script");
            parseInt2 = Integer.parseInt((String) hashMap.get(NetworkWrapper.VAST_CACHE_TO));
            str2 = (String) hashMap.get("script");
            if (str2 != null) {
                this.interstitial = new VASTPlayer(activity, this, z, str, (long) parseInt, this.isRewarded, str4, z2, str3, parseInt2);
                if (URLUtil.isValidUrl(str2)) {
                    this.interstitial.loadVastResponseViaURL(str2);
                    return;
                } else {
                    this.interstitial.loadVastResponseViaXML(str2);
                    return;
                }
            }
            onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
        }
        str2 = (String) hashMap.get("postroll");
        z = str2 == null && str2.equals("1");
        str2 = (String) hashMap.get(NetworkWrapper.SHOULD_VALIDATE_SCHEMA);
        z2 = str2 == null && Boolean.parseBoolean(str2);
        str = (String) hashMap.get("endcard_script");
        try {
            parseInt2 = Integer.parseInt((String) hashMap.get(NetworkWrapper.VAST_CACHE_TO));
        } catch (NumberFormatException e4) {
            parseInt2 = -1;
        }
        str2 = (String) hashMap.get("script");
        if (str2 != null) {
            this.interstitial = new VASTPlayer(activity, this, z, str, (long) parseInt, this.isRewarded, str4, z2, str3, parseInt2);
            if (URLUtil.isValidUrl(str2)) {
                this.interstitial.loadVastResponseViaURL(str2);
                return;
            } else {
                this.interstitial.loadVastResponseViaXML(str2);
                return;
            }
        }
        onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
    }

    public void vastClick() {
        onAdClicked();
    }

    public void vastComplete() {
        onAdCompleted();
    }

    public void vastDismiss() {
        onAdClosed();
    }

    public void vastDisplay() {
        onAdDisplayed();
    }

    public void vastError(int i) {
        log("Error: " + i);
        onVastError(i);
        switch (i) {
            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
            case VASTPlayer.ERROR_FILE_NOT_FOUND /*401*/:
            case VASTPlayer.ERROR_NO_COMPATIBLE_MEDIA_FILE /*403*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_LOAD_NOT_STARTED);
            case FileUtility.IMAGE_QUALITY /*100*/:
            case VASTPlayer.ERROR_SCHEMA_VALIDATION /*101*/:
            case VASTPlayer.ERROR_UNSUPPORTED_VERSION /*102*/:
            case VASTPlayer.ERROR_GENERAL_WRAPPER /*300*/:
            case VASTPlayer.ERROR_EXCEEDED_WRAPPER_LIMIT /*302*/:
            case VASTPlayer.ERROR_GENERAL_LINEAR /*400*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_ADAPTER_ERROR);
            case VASTPlayer.ERROR_WRAPPER_TIMEOUT /*301*/:
            case VASTPlayer.ERROR_VIDEO_TIMEOUT /*402*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_TIMED_OUT);
            case VASTPlayer.ERROR_NO_VAST_IN_WRAPPER /*303*/:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_UNDEFINED);
            case VASTPlayer.ERROR_VIDEO_PLAYBACK /*405*/:
                onAdFailedToDisplay();
            default:
                onAdFailedToLoad(AdapterLoadError.PROVIDER_UNRECOGNIZED);
        }
    }

    public void vastProgress(int i) {
        onVastProgress(i);
    }

    public void vastReady() {
        log("Ad loaded");
        onAdLoaded();
    }

    public void vastReplay() {
        onVastReplay();
    }

    public void vastRewardedVideoComplete() {
        onRewardedVideoCompleted();
    }

    public void vastSkip() {
        onAdSkipped();
        onVastSkip();
    }

    public boolean verifyParameters(HashMap<String, String> hashMap) {
        Object obj = (StringUtil.isNullOrEmpty((String) hashMap.get(NetworkWrapper.MAX_FILE_SIZE)) || StringUtil.isNullOrEmpty((String) hashMap.get("script"))) ? 1 : null;
        return obj == null;
    }
}
