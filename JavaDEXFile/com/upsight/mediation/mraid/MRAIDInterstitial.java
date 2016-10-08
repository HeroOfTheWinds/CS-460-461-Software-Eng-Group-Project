package com.upsight.mediation.mraid;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.upsight.mediation.mraid.internal.MRAIDLog;

public class MRAIDInterstitial implements MRAIDViewListener {
    private static final String TAG = "MRAIDInterstitial";
    private Handler handler;
    public boolean isReady;
    private MRAIDInterstitialListener listener;
    public MRAIDView mraidView;

    /* renamed from: com.upsight.mediation.mraid.MRAIDInterstitial.1 */
    class C10061 implements Runnable {
        C10061() {
        }

        public void run() {
            if (!MRAIDInterstitial.this.isReady || MRAIDInterstitial.this.listener == null) {
                MRAIDLog.m247i(MRAIDInterstitial.TAG, "No longer ready");
            } else {
                MRAIDInterstitial.this.listener.mraidInterstitialLoaded(MRAIDInterstitial.this);
            }
        }
    }

    public MRAIDInterstitial(Context context, String str, String str2, int i, @NonNull String[] strArr, MRAIDInterstitialListener mRAIDInterstitialListener, MRAIDNativeFeatureListener mRAIDNativeFeatureListener) {
        this.listener = mRAIDInterstitialListener;
        this.handler = new Handler(Looper.getMainLooper());
        this.mraidView = new MRAIDView(context, str, str2, i, strArr, this, mRAIDNativeFeatureListener, true);
    }

    public MRAIDInterstitial(Context context, String str, String str2, String[] strArr, MRAIDInterstitialListener mRAIDInterstitialListener, MRAIDNativeFeatureListener mRAIDNativeFeatureListener) {
        this(context, str, str2, 0, strArr, mRAIDInterstitialListener, mRAIDNativeFeatureListener);
    }

    public void injectJavaScript(String str) {
        this.mraidView.injectJavaScript(str);
    }

    public void mraidReplayVideoPressed(MRAIDView mRAIDView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialReplayVideoPressed(this);
        }
    }

    public void mraidViewAcceptPressed(MRAIDView mRAIDView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialAcceptPressed(this);
        }
    }

    public void mraidViewClose(MRAIDView mRAIDView) {
        MRAIDLog.m247i("MRAIDInterstitial-MRAIDViewListener", "mraidViewClose");
        this.isReady = false;
        if (this.listener != null) {
            this.listener.mraidInterstitialHide(this);
        }
    }

    public void mraidViewExpand(MRAIDView mRAIDView) {
        MRAIDLog.m247i("MRAIDInterstitial-MRAIDViewListener", "mraidViewExpand");
        if (this.listener != null) {
            this.listener.mraidInterstitialShow(this);
        }
    }

    public void mraidViewFailedToLoad(MRAIDView mRAIDView) {
        this.isReady = false;
        if (this.listener != null) {
            this.listener.mraidInterstitialFailedToLoad(this);
        }
    }

    public void mraidViewLoaded(MRAIDView mRAIDView) {
        MRAIDLog.m249v("MRAIDInterstitial-MRAIDViewListener", "mraidViewLoaded");
        this.isReady = true;
        this.handler.postDelayed(new C10061(), 250);
    }

    public void mraidViewRejectPressed(MRAIDView mRAIDView) {
        if (this.listener != null) {
            this.listener.mraidInterstitialRejectPressed(this);
        }
    }

    public boolean mraidViewResize(MRAIDView mRAIDView, int i, int i2, int i3, int i4) {
        return true;
    }

    public void setOrientationConfig(int i) {
        this.mraidView.setOrientationConfig(i);
    }

    public boolean show() {
        if (this.isReady) {
            this.mraidView.showAsInterstitial();
            this.isReady = false;
            return true;
        }
        MRAIDLog.m247i(TAG, "interstitial is not ready to show");
        return false;
    }

    public void updateContext(Context context) {
        this.mraidView.updateContext(context);
    }
}
