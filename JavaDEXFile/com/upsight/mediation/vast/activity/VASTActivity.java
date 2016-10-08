package com.upsight.mediation.vast.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.Postroll.MRaidPostroll;
import com.upsight.mediation.vast.Postroll.Postroll;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.model.TRACKING_EVENTS_TYPE;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.model.VASTTracking;
import com.upsight.mediation.vast.util.Assets;
import com.upsight.mediation.vast.util.HttpTools;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import spacemadness.com.lunarconsole.BuildConfig;
import spacemadness.com.lunarconsole.C1518R;

public class VASTActivity extends Activity implements OnCompletionListener, OnErrorListener, OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, Callback, Listener {
    private static final long QUARTILE_TIMER_INTERVAL = 250;
    private static String TAG = null;
    private static final long TOOLBAR_HIDE_DELAY = 3000;
    private static final long VIDEO_PROGRESS_TIMER_INTERVAL = 33;
    public static DisplayMetrics displayMetrics;
    private String mActionText;
    private int mCurrentOrientation;
    private int mCurrentVideoPosition;
    private String mEndCardHtml;
    private Handler mHandler;
    private boolean mIsCompleted;
    private boolean mIsPlayBackError;
    private boolean mIsProcessedImpressions;
    private boolean mIsRewarded;
    private boolean mIsVideoPaused;
    private final int mMaxProgressTrackingPoints;
    private MediaPlayer mMediaPlayer;
    private RelativeLayout mOverlay;
    private PlayerControls mPlayerControls;
    private Postroll mPostroll;
    private boolean mPostrollFlag;
    private ProgressBar mProgressBar;
    private int mQuartile;
    private RelativeLayout mRootLayout;
    private int mScreenHeight;
    private int mScreenWidth;
    private long mSkipOffset;
    private boolean mSkipOffsetRelative;
    private long mSkipOffsetServer;
    private Timer mStartVideoProgressTimer;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Timer mToolBarTimer;
    private HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> mTrackingEventMap;
    private Timer mTrackingEventTimer;
    public VASTModel mVastModel;
    private String mVersion;
    private int mVideoDuration;
    private int mVideoHeight;
    private LinkedList<Integer> mVideoProgressTracker;
    private int mVideoWidth;
    private Rect rekt;
    private boolean shouldPlayOnResume;
    private boolean showingPostroll;

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.1 */
    class C10251 implements OnTouchListener {
        C10251() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            VASTActivity.this.overlayClicked();
            return false;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.2 */
    class C10262 implements OnTouchListener {
        C10262() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case C1518R.styleable.AdsAttrs_adSize /*0*/:
                    VASTActivity.this.rekt = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    Assets.setAlpha(view, PlayerControls.DOWN_STATE);
                    break;
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    if (VASTActivity.this.rekt.contains(view.getLeft() + ((int) motionEvent.getX()), view.getTop() + ((int) motionEvent.getY()))) {
                        Assets.setAlpha(view, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        VASTActivity.this.infoClicked(true);
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.3 */
    class C10273 implements OnTouchListener {
        C10273() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case C1518R.styleable.AdsAttrs_adSize /*0*/:
                    VASTActivity.this.rekt = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    Assets.setAlpha(view, PlayerControls.DOWN_STATE);
                    break;
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    if (VASTActivity.this.rekt.contains(view.getLeft() + ((int) motionEvent.getX()), view.getTop() + ((int) motionEvent.getY()))) {
                        Assets.setAlpha(view, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        VASTActivity.this.skipClicked();
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.4 */
    class C10294 extends TimerTask {

        /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.4.1 */
        class C10281 implements Runnable {
            C10281() {
            }

            public void run() {
                VASTActivity.this.mPlayerControls.setVisibility(8);
            }
        }

        C10294() {
        }

        public void run() {
            VASTActivity.this.mHandler.post(new C10281());
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.5 */
    class C10305 extends TimerTask {
        final /* synthetic */ List val$events;
        final /* synthetic */ int val$videoDuration;

        C10305(int i, List list) {
            this.val$videoDuration = i;
            this.val$events = list;
        }

        public void run() {
            try {
                int currentPosition = VASTActivity.this.mMediaPlayer.getCurrentPosition();
                if (currentPosition != 0) {
                    int i = (currentPosition * 100) / this.val$videoDuration;
                    if (i >= VASTActivity.this.mQuartile * 25) {
                        if (VASTActivity.this.mQuartile == 0) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.start);
                            VASTPlayer.currentPlayer.listener.vastProgress(0);
                        } else if (VASTActivity.this.mQuartile == 1) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.firstQuartile);
                            VASTPlayer.currentPlayer.listener.vastProgress(25);
                        } else if (VASTActivity.this.mQuartile == 2) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.midpoint);
                            VASTPlayer.currentPlayer.listener.vastProgress(50);
                        } else if (VASTActivity.this.mQuartile == 3) {
                            VASTActivity.this.processEvent(TRACKING_EVENTS_TYPE.thirdQuartile);
                            VASTPlayer.currentPlayer.listener.vastProgress(75);
                            VASTActivity.this.stopTrackingEventTimer();
                        }
                        VASTActivity.this.mQuartile = VASTActivity.this.mQuartile + 1;
                    }
                    if (VASTActivity.this.mVersion.equals("3.0") && this.val$events != null) {
                        for (VASTTracking vASTTracking : this.val$events) {
                            if (vASTTracking.isOffsetRelative()) {
                                if (((long) i) >= vASTTracking.getParsedOffset() && !vASTTracking.isConsumed()) {
                                    VASTActivity.this.processProgressEvent(vASTTracking);
                                    vASTTracking.setConsumed(true);
                                    VASTPlayer.currentPlayer.listener.vastProgress(i);
                                }
                            } else if (((long) currentPosition) >= vASTTracking.getParsedOffset() && !vASTTracking.isConsumed()) {
                                VASTActivity.this.processProgressEvent(vASTTracking);
                                vASTTracking.setConsumed(true);
                                VASTPlayer.currentPlayer.listener.vastProgress(i);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                FuseLog.m240w(VASTActivity.TAG, "mediaPlayer.getCurrentPosition exception: " + e.getMessage());
                cancel();
            }
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.6 */
    class C10326 extends TimerTask {
        int maxAmountInList;

        /* renamed from: com.upsight.mediation.vast.activity.VASTActivity.6.1 */
        class C10311 implements Runnable {
            C10311() {
            }

            public void run() {
                if (VASTActivity.this.mPlayerControls != null) {
                    VASTActivity.this.mPlayerControls.update(VASTActivity.VIDEO_PROGRESS_TIMER_INTERVAL);
                }
            }
        }

        C10326() {
            this.maxAmountInList = 19;
        }

        public void run() {
            if (VASTActivity.this.mMediaPlayer != null) {
                if (VASTActivity.this.mVideoProgressTracker.size() == this.maxAmountInList && ((Integer) VASTActivity.this.mVideoProgressTracker.getLast()).intValue() > ((Integer) VASTActivity.this.mVideoProgressTracker.getFirst()).intValue()) {
                    VASTActivity.this.mVideoProgressTracker.removeFirst();
                }
                try {
                    int currentPosition = VASTActivity.this.mMediaPlayer.getCurrentPosition();
                    VASTActivity.this.runOnUiThread(new C10311());
                    VASTActivity.this.mVideoProgressTracker.addLast(Integer.valueOf(currentPosition));
                } catch (Exception e) {
                }
            }
        }
    }

    static {
        TAG = "VASTActivity";
    }

    public VASTActivity() {
        this.mVideoProgressTracker = null;
        this.mMaxProgressTrackingPoints = 20;
        this.mVastModel = null;
        this.mEndCardHtml = BuildConfig.FLAVOR;
        this.mIsVideoPaused = false;
        this.mIsPlayBackError = false;
        this.mIsProcessedImpressions = false;
        this.mIsCompleted = false;
        this.mQuartile = 0;
        this.shouldPlayOnResume = false;
    }

    private void activateButtons(boolean z) {
        if (this.mPlayerControls != null) {
            if (z) {
                this.mPlayerControls.setVisibility(0);
            } else {
                this.mPlayerControls.setVisibility(8);
            }
        }
    }

    private void calculateAspectRatio() {
        if (this.mVideoWidth == 0 || this.mVideoHeight == 0) {
            FuseLog.m235d(TAG, "mVideoWidth or mVideoHeight is 0, skipping calculateAspectRatio");
            return;
        }
        double min = Math.min((((double) this.mScreenWidth) * 1.0d) / ((double) this.mVideoWidth), (((double) this.mScreenHeight) * 1.0d) / ((double) this.mVideoHeight));
        int i = (int) (((double) this.mVideoWidth) * min);
        int i2 = (int) (min * ((double) this.mVideoHeight));
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(i, i2);
        layoutParams.addRule(13);
        this.mSurfaceView.setLayoutParams(layoutParams);
        this.mSurfaceHolder.setFixedSize(i, i2);
        FuseLog.m239v(TAG, " screen size: " + this.mScreenWidth + "x" + this.mScreenHeight);
        FuseLog.m239v(TAG, " video size:  " + this.mVideoWidth + "x" + this.mVideoHeight);
        FuseLog.m239v(TAG, "surface size: " + i + "x" + i2);
    }

    private void cleanActivityUp() {
        cleanUpMediaPlayer();
        stopTrackingEventTimer();
        stopVideoProgressTimer();
        stopToolBarTimer();
        if (this.mPlayerControls != null) {
            this.mPlayerControls.setVisibility(8);
            this.mOverlay.removeView(this.mPlayerControls);
            this.mPlayerControls = null;
        }
    }

    private void cleanUpMediaPlayer() {
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.setOnCompletionListener(null);
            this.mMediaPlayer.setOnErrorListener(null);
            this.mMediaPlayer.setOnPreparedListener(null);
            this.mMediaPlayer.setOnVideoSizeChangedListener(null);
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void createMediaPlayer() {
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(this);
        this.mMediaPlayer.setOnErrorListener(this);
        this.mMediaPlayer.setOnPreparedListener(this);
        this.mMediaPlayer.setOnVideoSizeChangedListener(this);
        this.mMediaPlayer.setOnSeekCompleteListener(this);
        this.mMediaPlayer.setAudioStreamType(3);
    }

    private void createOverlay(RelativeLayout.LayoutParams layoutParams) {
        this.mOverlay = new RelativeLayout(this);
        this.mOverlay.setLayoutParams(layoutParams);
        this.mOverlay.setPadding(0, 0, 0, 0);
        this.mOverlay.setBackgroundColor(0);
        this.mOverlay.setOnTouchListener(new C10251());
        this.mRootLayout.addView(this.mOverlay);
    }

    private void createPlayerControls() {
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10);
        resolveSkipOffset();
        this.mPlayerControls = new PlayerControls(this);
        this.mPlayerControls.setVastModel(this.mVastModel);
        this.mPlayerControls.init(this.mSkipOffset != -1, this.mPostrollFlag);
        this.mPlayerControls.setLayoutParams(layoutParams);
        this.mPlayerControls.setVisibility(8);
        TextView learnText = this.mPlayerControls.getLearnText();
        if (learnText != null) {
            learnText.setText(this.mActionText);
            learnText.setTextSize((float) (20 - (this.mActionText.length() / 2)));
            learnText.setOnTouchListener(new C10262());
        }
        this.mPlayerControls.setSkipButtonListener(new C10273());
        this.mOverlay.addView(this.mPlayerControls);
    }

    private void createPostroll() {
        if (this.mEndCardHtml == null || this.mEndCardHtml.length() <= 0) {
            String clickThrough = this.mVastModel.getVideoClicks().getClickThrough();
            boolean z = clickThrough != null && clickThrough.length() > 0;
            this.mPostroll = new DefaultPostroll(this, this, z, this.mActionText);
        } else {
            this.mPostroll = new MRaidPostroll(this, this.mEndCardHtml, this);
        }
        this.mPostroll.init();
    }

    private void createProgressBar() {
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13);
        this.mProgressBar = new ProgressBar(this);
        this.mProgressBar.setLayoutParams(layoutParams);
        this.mRootLayout.addView(this.mProgressBar);
        this.mProgressBar.setVisibility(8);
    }

    private void createRootLayout(RelativeLayout.LayoutParams layoutParams) {
        this.mRootLayout = new RelativeLayout(this);
        this.mRootLayout.setLayoutParams(layoutParams);
        this.mRootLayout.setPadding(0, 0, 0, 0);
        this.mRootLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void createSurface(RelativeLayout.LayoutParams layoutParams) {
        this.mSurfaceView = new SurfaceView(this);
        this.mSurfaceView.setLayoutParams(layoutParams);
        this.mSurfaceHolder = this.mSurfaceView.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(3);
        this.mRootLayout.addView(this.mSurfaceView);
    }

    private void createUIComponents() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        createRootLayout(layoutParams);
        createSurface(layoutParams);
        createMediaPlayer();
        createOverlay(layoutParams);
        createPlayerControls();
        if (this.mPostrollFlag) {
            createPostroll();
        }
        setContentView(this.mRootLayout);
        createProgressBar();
    }

    private void finishVAST() {
        cleanActivityUp();
        try {
            VASTPlayer.currentPlayer.listener.vastDismiss();
        } catch (NullPointerException e) {
        }
        VASTPlayer.currentPlayer.setLoaded(false);
        finish();
    }

    private void fireUrl(String str, String str2) {
        if (str2 != null) {
            HttpTools.httpGetURL(str, str2, VASTPlayer.currentPlayer);
        } else {
            FuseLog.m235d(TAG, "\turl is null");
        }
    }

    private void fireUrls(String str, List<String> list) {
        if (list != null) {
            for (String httpGetURL : list) {
                HttpTools.httpGetURL(str, httpGetURL, VASTPlayer.currentPlayer);
            }
            return;
        }
        FuseLog.m235d(TAG, "\turl list is null");
    }

    private void hideProgressBar() {
        this.mProgressBar.setVisibility(8);
    }

    private void hideTitleStatusBars() {
        requestWindowFeature(1);
        getWindow().setFlags(Place.TYPE_SUBLOCALITY_LEVEL_2, Place.TYPE_SUBLOCALITY_LEVEL_2);
    }

    private void overlayClicked() {
        startToolBarTimer();
    }

    private void processClickThroughEvent() {
        String clickThrough = this.mVastModel.getVideoClicks().getClickThrough();
        FuseLog.m239v(TAG, "clickThrough url: " + clickThrough);
        fireUrls("click", this.mVastModel.getVideoClicks().getClickTracking());
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(clickThrough));
            if (getPackageManager().resolveActivity(intent, 32) == null) {
                FuseLog.m235d(TAG, "Clickthrough error occured, uri unresolvable");
                if (((double) this.mCurrentVideoPosition) >= ((double) this.mMediaPlayer.getCurrentPosition()) * 0.99d) {
                    this.mMediaPlayer.start();
                }
                activateButtons(true);
                return;
            }
            startActivity(intent);
        } catch (NullPointerException e) {
        }
    }

    private void processErrorEvent() {
        fireUrls(GameServices.ERROR, this.mVastModel.getErrorUrl());
    }

    private void processEvent(TRACKING_EVENTS_TYPE tracking_events_type) {
        List<VASTTracking> list = (List) this.mTrackingEventMap.get(tracking_events_type);
        List arrayList = new ArrayList();
        if (list != null) {
            for (VASTTracking value : list) {
                arrayList.add(value.getValue());
            }
            fireUrls(tracking_events_type.name(), arrayList);
        }
    }

    private void processImpressions() {
        this.mIsProcessedImpressions = true;
        fireUrls("impression", this.mVastModel.getImpressions());
    }

    private void processPauseSteps() {
        this.mIsVideoPaused = true;
        this.mMediaPlayer.pause();
        this.shouldPlayOnResume = true;
        stopVideoProgressTimer();
        stopToolBarTimer();
    }

    private void processPlaySteps() {
        this.mIsVideoPaused = false;
        this.mMediaPlayer.start();
        startToolBarTimer();
        startVideoProgressTimer();
    }

    private void processProgressEvent(VASTTracking vASTTracking) {
        if (vASTTracking != null) {
            fireUrl(vASTTracking.getEvent().toString(), vASTTracking.getValue());
        }
    }

    private void resetPlayerToBeginning() {
        this.showingPostroll = false;
        this.mIsCompleted = false;
        this.mQuartile = 0;
        createPlayerControls();
        surfaceCreated(this.mSurfaceHolder);
    }

    private void resolveSkipOffset() {
        if (this.mVersion.equals("3.0")) {
            String skipOffset = this.mVastModel.getSkipOffset();
            if (skipOffset == null || skipOffset.length() == 0) {
                this.mSkipOffset = this.mSkipOffsetServer;
            } else if (skipOffset.endsWith("%")) {
                this.mSkipOffsetRelative = true;
                this.mSkipOffset = Long.parseLong(skipOffset.substring(0, skipOffset.indexOf("%")));
            } else {
                this.mSkipOffset = Assets.parseOffset(skipOffset);
            }
        } else {
            this.mSkipOffset = this.mSkipOffsetServer;
        }
        FuseLog.m239v(TAG, "skipOffset:  " + this.mSkipOffset);
    }

    private void showPostroll() {
        this.showingPostroll = true;
        cleanActivityUp();
        this.mPostroll.show(this.mRootLayout);
    }

    private void showProgressBar() {
        this.mProgressBar.setVisibility(0);
    }

    private void skipClicked() {
        if (this.mPostrollFlag) {
            cleanActivityUp();
            showPostroll();
        } else {
            finishVAST();
        }
        if (this.mVersion.equals("3.0")) {
            processEvent(TRACKING_EVENTS_TYPE.skip);
        }
        VASTPlayer.currentPlayer.listener.vastSkip();
    }

    private void startToolBarTimer() {
        if (this.mQuartile != 4) {
            if (this.mMediaPlayer != null && this.mMediaPlayer.isPlaying()) {
                stopToolBarTimer();
                this.mToolBarTimer = new Timer();
                this.mToolBarTimer.schedule(new C10294(), TOOLBAR_HIDE_DELAY);
                this.mPlayerControls.setVisibility(0);
            }
            if (this.mIsVideoPaused) {
                activateButtons(true);
            }
        }
    }

    private void startTrackingEventTimer() {
        stopTrackingEventTimer();
        if (!this.mIsCompleted) {
            List list;
            int duration = this.mMediaPlayer.getDuration();
            try {
                list = (List) this.mTrackingEventMap.get(TRACKING_EVENTS_TYPE.progress);
            } catch (Exception e) {
                list = null;
            }
            this.mTrackingEventTimer = new Timer();
            this.mTrackingEventTimer.scheduleAtFixedRate(new C10305(duration, list), 0, QUARTILE_TIMER_INTERVAL);
        }
    }

    private void startVideoProgressTimer() {
        this.mStartVideoProgressTimer = new Timer();
        this.mVideoProgressTracker = new LinkedList();
        this.mPlayerControls.setTimes((long) this.mVideoDuration, this.mSkipOffset);
        this.mStartVideoProgressTimer.schedule(new C10326(), 0, VIDEO_PROGRESS_TIMER_INTERVAL);
    }

    private void stopToolBarTimer() {
        if (this.mToolBarTimer != null) {
            this.mToolBarTimer.cancel();
            this.mToolBarTimer = null;
        }
    }

    private void stopTrackingEventTimer() {
        if (this.mTrackingEventTimer != null) {
            this.mTrackingEventTimer.cancel();
            this.mTrackingEventTimer = null;
        }
    }

    private void stopVideoProgressTimer() {
        if (this.mStartVideoProgressTimer != null) {
            this.mStartVideoProgressTimer.cancel();
        }
    }

    public void closeClicked() {
        cleanActivityUp();
        if (!this.mIsPlayBackError) {
            processEvent(TRACKING_EVENTS_TYPE.close);
        }
        finishVAST();
    }

    public void infoClicked(boolean z) {
        if (VASTPlayer.currentPlayer.listener != null) {
            VASTPlayer.currentPlayer.listener.vastClick();
        }
        activateButtons(false);
        if (z) {
            processClickThroughEvent();
        }
        closeClicked();
    }

    public void onBackPressed() {
        if (this.mSkipOffset > 0) {
            if (!this.mIsCompleted) {
                VASTPlayer.currentPlayer.listener.vastSkip();
            }
            closeClicked();
            super.onBackPressed();
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        cleanActivityUp();
        if (!(this.mIsPlayBackError || this.mIsCompleted)) {
            this.mIsCompleted = true;
            processEvent(TRACKING_EVENTS_TYPE.complete);
            if (VASTPlayer.currentPlayer.listener != null) {
                VASTPlayer.currentPlayer.listener.vastProgress(100);
                VASTPlayer.currentPlayer.listener.vastComplete();
                if (this.mIsRewarded) {
                    VASTPlayer.currentPlayer.listener.vastRewardedVideoComplete();
                }
            }
        }
        if (this.mPostrollFlag) {
            showPostroll();
        } else {
            closeClicked();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        this.mPostrollFlag = intent.getBooleanExtra("postroll", false);
        this.mEndCardHtml = intent.getStringExtra("endCardHtml");
        this.mSkipOffsetServer = intent.getLongExtra("skipOffset", 0);
        this.mIsRewarded = intent.getBooleanExtra("rewarded", true);
        this.mActionText = intent.getStringExtra("actionText");
        this.mVastModel = (VASTModel) intent.getSerializableExtra("com.nexage.android.vast.player.vastModel");
        if (this.mVastModel == null) {
            FuseLog.m235d(TAG, "vastModel is null. Stopping activity.");
            finishVAST();
        } else if (VASTPlayer.currentPlayer == null) {
            FuseLog.m235d(TAG, "currentPlayer is null. Stopping activity.");
            finishVAST();
        } else {
            hideTitleStatusBars();
            this.mHandler = new Handler();
            displayMetrics = getResources().getDisplayMetrics();
            this.mScreenWidth = displayMetrics.widthPixels;
            this.mScreenHeight = displayMetrics.heightPixels;
            this.mVersion = this.mVastModel.getVastVersion();
            this.mTrackingEventMap = this.mVastModel.getTrackingEvents();
            createUIComponents();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        cleanActivityUp();
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        this.mIsPlayBackError = true;
        FuseLog.m240w(TAG, "Shutting down Activity due to Media Player errors: WHAT:" + i + ": EXTRA:" + i2 + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR);
        VASTPlayer.currentPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_PLAYBACK);
        processErrorEvent();
        closeClicked();
        return true;
    }

    public void onOpenMRaidUrl(String str) {
        FuseLog.m238i(TAG, "Opening MRAID Postroll click through link: " + str);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mMediaPlayer != null) {
            processPauseSteps();
            this.mCurrentVideoPosition = this.mMediaPlayer.getCurrentPosition();
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mVideoDuration = this.mMediaPlayer.getDuration();
        this.mVideoWidth = this.mMediaPlayer.getVideoWidth();
        this.mVideoHeight = this.mMediaPlayer.getVideoHeight();
        if (this.mSkipOffsetRelative) {
            this.mSkipOffset = (long) ((((float) this.mVideoDuration) / 100.0f) * ((float) this.mSkipOffset));
        }
        calculateAspectRatio();
        hideProgressBar();
        if (this.mIsVideoPaused) {
            this.mMediaPlayer.pause();
        } else {
            startVideoProgressTimer();
        }
        if (this.mCurrentVideoPosition > 0) {
            this.mMediaPlayer.seekTo(this.mCurrentVideoPosition);
        }
        if (!(this.mMediaPlayer.isPlaying() || this.mIsVideoPaused)) {
            this.mMediaPlayer.start();
            VASTPlayer.currentPlayer.listener.vastDisplay();
            startTrackingEventTimer();
            startToolBarTimer();
        }
        if (!this.mIsProcessedImpressions && this.mMediaPlayer.isPlaying()) {
            processImpressions();
        }
    }

    protected void onRestart() {
        super.onRestart();
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }

    protected void onResume() {
        super.onResume();
        if (this.shouldPlayOnResume && this.mMediaPlayer != null) {
            surfaceCreated(this.mSurfaceHolder);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void onSeekComplete(MediaPlayer mediaPlayer) {
        this.mMediaPlayer.start();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        this.mVideoWidth = i;
        this.mVideoHeight = i2;
        calculateAspectRatio();
        FuseLog.m239v(TAG, "video size: " + this.mVideoWidth + "x" + this.mVideoHeight);
    }

    public void replayedClicked() {
        createPostroll();
        resetPlayerToBeginning();
        VASTPlayer.currentPlayer.listener.vastReplay();
    }

    public void setRequestedOrientation(int i) {
        super.setRequestedOrientation(i);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!this.showingPostroll) {
            try {
                if (this.mMediaPlayer == null) {
                    createMediaPlayer();
                }
                showProgressBar();
                this.mMediaPlayer.setDisplay(surfaceHolder);
                if (this.mMediaPlayer == null || !this.mIsVideoPaused) {
                    this.mMediaPlayer.setDataSource(this.mVastModel.getPickedMediaFileLocation());
                    if (this.mVastModel.getPickedMediaFileDeliveryType().equals("streaming")) {
                        this.mMediaPlayer.prepareAsync();
                        return;
                    } else {
                        this.mMediaPlayer.prepare();
                        return;
                    }
                }
                processPlaySteps();
                hideProgressBar();
            } catch (Throwable e) {
                FuseLog.m241w(TAG, e.getMessage(), e);
                VASTPlayer.currentPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_PLAYBACK);
                finishVAST();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.shouldPlayOnResume = false;
    }
}
