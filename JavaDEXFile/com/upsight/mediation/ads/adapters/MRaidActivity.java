package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRAIDInterstitial;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1518R;

public class MRaidActivity extends Activity {
    private static final String TAG = "MRaidActivity";
    private boolean firstResume;
    private MRAIDInterstitial interstitial;
    public boolean isVisible;
    private MRaidAdAdapter provider;
    private int registryId;
    private int rotateMode;

    public MRaidActivity() {
        this.firstResume = true;
    }

    private int getOrientationValue(int i) {
        switch (i) {
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                return 14;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                return 6;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                return 7;
            default:
                return -1;
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            if (i2 == -1) {
                this.provider.onAdCompleted();
            } else if (i2 == 0) {
                this.provider.onAdSkipped();
            }
            this.provider.mraidInterstitialHide(this.interstitial);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.provider.mraidInterstitialHide(this.interstitial);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.registryId = getIntent().getIntExtra("registryId", -1);
        this.provider = MRaidAdAdapter.mRaidRegistry.getProvider(this.registryId);
        this.provider.setMRaidActivity(this);
        this.rotateMode = getIntent().getIntExtra("rotate", 1);
        setRequestedOrientation(getOrientationValue(this.rotateMode));
        this.interstitial = this.provider.getInterstitial();
        this.interstitial.updateContext(this);
        FuseLog.m236e(TAG, getRequestedOrientation() + BuildConfig.FLAVOR);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        this.isVisible = false;
    }

    protected void onResume() {
        super.onResume();
        this.isVisible = true;
        if (this.firstResume) {
            this.firstResume = false;
            if (this.interstitial == null) {
                finish();
            } else if (!this.interstitial.show()) {
                this.provider.mraidInterstitialFailedToShow();
                finish();
            }
        }
    }
}
