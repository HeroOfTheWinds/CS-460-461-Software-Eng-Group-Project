package com.upsight.mediation.vast.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.upsight.mediation.vast.Postroll.Postroll;
import com.upsight.mediation.vast.Postroll.Postroll.Listener;
import com.upsight.mediation.vast.Postroll.VasButton;
import com.upsight.mediation.vast.util.Assets;

public class DefaultPostroll extends RelativeLayout implements Postroll {
    private static final int BUTTON_HEIGHT = 75;
    private static final int BUTTON_WIDTH = 256;
    private static final int MARGIN = 8;
    private static final float SCALED_DENSITY;
    public static final int TEXT_FONT_SIZE = 28;
    private static final int X_HEIGHT = 29;
    private static final int X_WIDTH = 29;
    private final String mActionButtonText;
    private Context mContext;
    private ImageView mLastFrameView;
    private LinearLayout mLayerOfDarkness;
    private Button mLearnButton;
    private final Listener mListener;
    private final boolean mShouldShowActionButton;
    private ImageView mXButtonView;

    /* renamed from: com.upsight.mediation.vast.activity.DefaultPostroll.1 */
    class C10231 implements OnClickListener {
        C10231() {
        }

        public void onClick(View view) {
            DefaultPostroll.this.mListener.infoClicked(true);
        }
    }

    /* renamed from: com.upsight.mediation.vast.activity.DefaultPostroll.2 */
    class C10242 implements OnClickListener {
        C10242() {
        }

        public void onClick(View view) {
            DefaultPostroll.this.hide();
            DefaultPostroll.this.mListener.closeClicked();
        }
    }

    static {
        SCALED_DENSITY = VASTActivity.displayMetrics.scaledDensity;
    }

    public DefaultPostroll(Context context, Listener listener, boolean z, String str) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        this.mShouldShowActionButton = z;
        this.mActionButtonText = str;
    }

    private void setUpLastFrameView(Context context) {
        this.mLastFrameView = new ImageView(context);
        this.mLastFrameView.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.mLastFrameView);
    }

    private void setUpLayerOfDarkness(Context context) {
        this.mLayerOfDarkness = new LinearLayout(context);
        this.mLayerOfDarkness.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        this.mLayerOfDarkness.setBackgroundColor(Color.argb(192, 0, 0, 0));
        addView(this.mLayerOfDarkness);
    }

    private void setUpLearn(Context context) {
        if (this.mShouldShowActionButton) {
            ViewGroup.LayoutParams layoutParams = new LayoutParams(Assets.convertToDps(256.0f), Assets.convertToDps(75.0f));
            layoutParams.addRule(13);
            this.mLearnButton = new VasButton(context);
            this.mLearnButton.setTypeface(Typeface.create("sans-serif-light", 0));
            this.mLearnButton.setMaxLines(1);
            this.mLearnButton.setLayoutParams(layoutParams);
            this.mLearnButton.setGravity(17);
            Assets.setImage(this.mLearnButton, Assets.getPostrollButton(getResources()));
            this.mLearnButton.setEnabled(true);
            this.mLearnButton.setVisibility(0);
            this.mLearnButton.setTextColor(-1);
            this.mLearnButton.setText(this.mActionButtonText);
            this.mLearnButton.setTextSize((float) (28 - (this.mActionButtonText.length() / 2)));
            this.mLearnButton.setOnClickListener(new C10231());
            addView(this.mLearnButton);
        }
    }

    private void setUpX(Context context) {
        this.mXButtonView = new ImageView(context);
        Assets.setImage(this.mXButtonView, Assets.getXButton(getResources()));
        Assets.setAlpha(this.mXButtonView, 0.8f);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(Assets.convertToDps(29.0f), Assets.convertToDps(29.0f));
        layoutParams.addRule(11);
        int convertToDps = Assets.convertToDps(8.0f);
        layoutParams.setMargins(0, convertToDps, convertToDps, 0);
        this.mXButtonView.setLayoutParams(layoutParams);
        this.mXButtonView.setId(16908327);
        this.mXButtonView.setOnClickListener(new C10242());
        addView(this.mXButtonView);
    }

    public void hide() {
        ((ViewGroup) getParent()).removeView(this);
    }

    public void init() {
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        setUpLastFrameView(this.mContext);
        setUpLayerOfDarkness(this.mContext);
        setUpLearn(this.mContext);
        setUpX(this.mContext);
    }

    public boolean isReady() {
        return true;
    }

    public void show(ViewGroup viewGroup) {
        viewGroup.addView(this);
    }
}
