package com.upsight.mediation.vast.activity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import com.upsight.mediation.vast.util.Assets;

public class CircleDrawable extends Drawable {
    private static final float STROKE_WIDTH = 2.0f;
    private RectF oval;
    private Paint paint;
    private float startAngle;
    private float sweepAngle;
    private long totalTime;

    public CircleDrawable(float f, float f2) {
        this.oval = new RectF((f / STROKE_WIDTH) - f2, (f / STROKE_WIDTH) - f2, (f / STROKE_WIDTH) + f2, (f / STROKE_WIDTH) + f2);
        this.startAngle = 270.0f;
        this.sweepAngle = 0.0f;
        this.paint = new Paint(1);
        this.paint.setStyle(Style.STROKE);
        this.paint.setColor(-1);
        this.paint.setAlpha(MotionEventCompat.ACTION_MASK);
        this.paint.setStrokeWidth((float) Assets.convertToDps(STROKE_WIDTH));
    }

    public void draw(Canvas canvas) {
        canvas.drawArc(this.oval, this.startAngle, this.sweepAngle, false, this.paint);
    }

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setSweepAngle(float f) {
        this.sweepAngle = f;
    }

    public void setTimer(long j) {
        this.totalTime = j;
    }

    public void update(long j) {
        this.sweepAngle = 360.0f - ((((float) j) / ((float) this.totalTime)) * 360.0f);
    }
}
