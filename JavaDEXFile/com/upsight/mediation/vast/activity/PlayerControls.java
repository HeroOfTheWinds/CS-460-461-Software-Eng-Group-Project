package com.upsight.mediation.vast.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.util.Assets;
import spacemadness.com.lunarconsole.BuildConfig;

public class PlayerControls extends RelativeLayout {
    private static final int BUTTON_HEIGHT = 38;
    private static final int BUTTON_WIDTH = 128;
    public static final float DOWN_STATE = 0.75f;
    private static final int MARGIN = 8;
    private static final int PROGRESS_RING_RADIUS = 10;
    private static final int PROGRESS_RING_VIEW_HEIGHT = 25;
    private static final int PROGRESS_RING_VIEW_WIDTH = 25;
    private static final int PROGRESS_RING_WIDTH = 25;
    public static final int TEXT_FONT_SIZE = 20;
    private static final int TIME_FONT_SIZE = 10;
    private CircleDrawable circleDrawable;
    private long elapsedTime;
    private Context mContext;
    private TextView mLearnText;
    private long mRemainder;
    private LinearLayout mSkipButton;
    private long mSkipOffset;
    private TextView mSkipText;
    private boolean mSkippable;
    private TextView mTimeText;
    private FrameLayout mTimerRing;
    private VASTModel mVastModel;
    private ImageView progressCircle;
    private OnTouchListener skipListener;

    public PlayerControls(Context context) {
        super(context);
        this.elapsedTime = 0;
        this.mContext = context;
    }

    public PlayerControls(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.elapsedTime = 0;
        this.mContext = context;
    }

    public PlayerControls(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.elapsedTime = 0;
        this.mContext = context;
    }

    private void setUpLearn(Context context) {
        String clickThrough = this.mVastModel.getVideoClicks().getClickThrough();
        if (clickThrough != null && clickThrough.length() > 0) {
            LayoutParams layoutParams = new RelativeLayout.LayoutParams(Assets.convertToDps(128.0f), Assets.convertToDps(38.0f));
            layoutParams.addRule(9);
            int convertToDps = Assets.convertToDps(8.0f);
            layoutParams.setMargins(convertToDps, convertToDps, 0, 0);
            this.mLearnText = new TextView(context);
            this.mLearnText.setText("LEARN MORE");
            this.mLearnText.setTypeface(Typeface.create("sans-serif-light", 0));
            this.mLearnText.setTextSize(20.0f);
            this.mLearnText.setMaxLines(1);
            this.mLearnText.setLayoutParams(layoutParams);
            this.mLearnText.setGravity(17);
            Assets.setImage(this.mLearnText, Assets.getPlayerUIButton(getResources()));
            this.mLearnText.setEnabled(true);
            this.mLearnText.setVisibility(0);
            addView(this.mLearnText);
        }
    }

    private void setUpSkipButton(Context context, boolean z) {
        this.mSkippable = z;
        this.mSkipButton = new LinearLayout(context);
        this.mSkipButton.setOrientation(0);
        this.mSkipButton.setGravity(17);
        Assets.setImage(this.mSkipButton, Assets.getPlayerUIButton(getResources()));
        int convertToDps = Assets.convertToDps(128.0f);
        int convertToDps2 = Assets.convertToDps(38.0f);
        int convertToDps3 = Assets.convertToDps(8.0f);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(convertToDps, convertToDps2);
        layoutParams.addRule(11);
        layoutParams.setMargins(0, convertToDps3, convertToDps3, 0);
        this.mSkipButton.setLayoutParams(layoutParams);
        setUpTimerRing(context);
        setUpSkipText(context);
        Assets.setAlpha(this.mSkipButton, DOWN_STATE);
        addView(this.mSkipButton);
    }

    private void setUpSkipText(Context context) {
        this.mSkipText = new TextView(context);
        if (this.mSkippable) {
            this.mSkipText.setText("SKIP");
        } else {
            this.mSkipText.setText("sec");
        }
        this.mSkipText.setTypeface(Typeface.create("sans-serif-light", 0));
        this.mSkipText.setTextSize(20.0f);
        this.mSkipButton.addView(this.mSkipText);
    }

    private void setUpTimerRing(Context context) {
        this.mTimerRing = new FrameLayout(context);
        this.mTimerRing.setPadding(0, 0, Assets.convertToDps(7.0f), 0);
        LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.circleDrawable = new CircleDrawable((float) Assets.convertToDps(25.0f), (float) Assets.convertToDps(10.0f));
        this.progressCircle = new ImageView(context);
        this.progressCircle.setMinimumWidth(Assets.convertToDps(25.0f));
        this.progressCircle.setMinimumHeight(Assets.convertToDps(25.0f));
        this.progressCircle.setImageDrawable(this.circleDrawable);
        this.progressCircle.setLayoutParams(layoutParams);
        this.mTimeText = new TextView(context);
        this.mTimeText.setTypeface(Typeface.create("sans-serif-light", 0));
        this.mTimeText.setTextSize(10.0f);
        this.mTimeText.setLayoutParams(layoutParams);
        this.mTimerRing.addView(this.progressCircle);
        this.mTimerRing.addView(this.mTimeText);
        this.mSkipButton.addView(this.mTimerRing);
    }

    public TextView getLearnText() {
        return this.mLearnText;
    }

    public void init(boolean z, boolean z2) {
        if (!(z2 || z) || z) {
            setUpLearn(this.mContext);
        }
        setUpSkipButton(this.mContext, z);
    }

    public void setSkipButtonListener(OnTouchListener onTouchListener) {
        this.skipListener = onTouchListener;
    }

    public void setTimes(long j, long j2) {
        this.mSkipOffset = j2;
        this.mRemainder = j;
        this.circleDrawable.setTimer(j);
    }

    public void setVastModel(VASTModel vASTModel) {
        this.mVastModel = vASTModel;
    }

    public void update(long j) {
        this.elapsedTime += j;
        this.mTimeText.setText(Math.abs((this.elapsedTime - this.mRemainder) / 1000) + BuildConfig.FLAVOR);
        if (this.elapsedTime > this.mRemainder) {
            this.circleDrawable.setSweepAngle(0.0f);
            this.progressCircle.invalidate();
            return;
        }
        if (this.elapsedTime > this.mSkipOffset && this.mSkippable) {
            this.mSkipOffset = this.mRemainder;
            this.mSkipButton.setOnTouchListener(this.skipListener);
            Assets.setAlpha(this.mSkipButton, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }
        this.circleDrawable.update(this.elapsedTime);
        this.progressCircle.invalidate();
    }
}
