package com.upsight.mediation.ads.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.VideoView;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.mraid.MRaidDrawables;
import com.voxelbusters.nativeplugins.defines.Keys;

public class MRaidVideoActivity extends Activity implements OnCompletionListener, OnErrorListener, OnPreparedListener {
    private static final float CLOSE_REGION_HEIGHT_OFFSET = 0.01f;
    private static final float CLOSE_REGION_SIZE = 50.0f;
    private static final float CLOSE_REGION_WIDTH_OFFSET = 0.9f;
    private static final String TAG = "MRAIDVideoActivity";
    private long closeButtonDelay;
    private ImageView closeRegion;
    private float deviceHeight;
    private float deviceWidth;
    private FrameLayout layout;
    private boolean shouldReturnToInterstitial;
    private LayoutParams skipParams;
    private VideoView videoView;

    /* renamed from: com.upsight.mediation.ads.adapters.MRaidVideoActivity.1 */
    class C10021 implements OnClickListener {
        C10021() {
        }

        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("rti", MRaidVideoActivity.this.shouldReturnToInterstitial);
            MRaidVideoActivity.this.setResult(0, intent);
            MRaidVideoActivity.this.finish();
        }
    }

    /* renamed from: com.upsight.mediation.ads.adapters.MRaidVideoActivity.2 */
    class C10032 implements Runnable {
        C10032() {
        }

        public void run() {
            MRaidVideoActivity.this.closeRegion.setVisibility(0);
        }
    }

    private void addCloseRegion() {
        this.closeRegion = new ImageButton(this);
        this.closeRegion.setBackgroundColor(0);
        this.closeRegion.setOnClickListener(new C10021());
        this.closeRegion.setVisibility(4);
        if (this.closeButtonDelay != -1) {
            this.closeRegion.postDelayed(new C10032(), this.closeButtonDelay);
        }
        Drawable drawableForImage = MRaidDrawables.getDrawableForImage(this, "/assets/drawable/close_button_normal.png", "close_button_normal", ViewCompat.MEASURED_STATE_MASK);
        Drawable drawableForImage2 = MRaidDrawables.getDrawableForImage(this, "/assets/drawable/close_button_pressed.png", "close_button_pressed", ViewCompat.MEASURED_STATE_MASK);
        Drawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-16842919}, drawableForImage);
        stateListDrawable.addState(new int[]{16842919}, drawableForImage2);
        this.closeRegion.setImageDrawable(stateListDrawable);
        this.closeRegion.setScaleType(ScaleType.CENTER_CROP);
    }

    private void getScreenDimensions() {
        this.deviceHeight = (float) getResources().getDisplayMetrics().heightPixels;
        this.deviceWidth = (float) getResources().getDisplayMetrics().widthPixels;
    }

    private void setCloseRegionPosition() {
        getScreenDimensions();
        int i = (int) (this.deviceWidth * CLOSE_REGION_WIDTH_OFFSET);
        this.skipParams.topMargin = (int) (this.deviceHeight * CLOSE_REGION_HEIGHT_OFFSET);
        this.skipParams.leftMargin = i;
        this.closeRegion.setLayoutParams(this.skipParams);
    }

    private void setCloseRegionPositionAndSize() {
        int applyDimension = (int) TypedValue.applyDimension(1, CLOSE_REGION_SIZE, getResources().getDisplayMetrics());
        this.skipParams = new LayoutParams(applyDimension, applyDimension);
        setCloseRegionPosition();
        this.layout.addView(this.closeRegion);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(0, intent);
        finish();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent intent = new Intent();
        intent.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(-1, intent);
        finish();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCloseRegionPosition();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.layout = new FrameLayout(this);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.layout.setLayoutParams(layoutParams);
        String stringExtra = getIntent().getStringExtra(Keys.URL);
        this.closeButtonDelay = getIntent().getLongExtra("cb_ms", 0);
        this.shouldReturnToInterstitial = getIntent().getBooleanExtra("rti", false);
        this.videoView = new VideoView(this);
        this.videoView.setOnCompletionListener(this);
        this.videoView.setOnErrorListener(this);
        this.videoView.setOnPreparedListener(this);
        this.videoView.setVideoPath(stringExtra);
        this.videoView.setLayoutParams(layoutParams);
        this.layout.addView(this.videoView);
        addCloseRegion();
        setCloseRegionPositionAndSize();
        setContentView(this.layout);
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        FuseLog.m240w(TAG, "ERROR LOADING VIDEO!");
        Intent intent = new Intent();
        intent.putExtra("rti", this.shouldReturnToInterstitial);
        setResult(0, intent);
        finish();
        return false;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        this.videoView.start();
    }

    protected void onStop() {
        super.onStop();
        this.videoView.stopPlayback();
    }
}
