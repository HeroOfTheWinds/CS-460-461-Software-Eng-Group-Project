package spacemadness.com.lunarconsole.ui;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import spacemadness.com.lunarconsole.C1518R;

public class SwipeGestureRecognizer extends GestureRecognizer<SwipeGestureRecognizer> {
    private final SwipeDirection direction;
    private float endX;
    private float endY;
    private float startX;
    private float startY;
    private boolean swiping;
    private final float threshold;

    public enum SwipeDirection {
        Up,
        Down,
        Left,
        Right
    }

    public SwipeGestureRecognizer(SwipeDirection swipeDirection, float f) {
        this.direction = swipeDirection;
        this.threshold = f;
    }

    private void handleSwipe(SwipeDirection swipeDirection, float f, float f2) {
        if ((swipeDirection == SwipeDirection.Down && f2 >= this.threshold) || ((swipeDirection == SwipeDirection.Up && (-f2) >= this.threshold) || ((swipeDirection == SwipeDirection.Right && f >= this.threshold) || (swipeDirection == SwipeDirection.Left && (-f) >= this.threshold)))) {
            notifyGestureRecognizer();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEventCompat.ACTION_MASK) {
            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                if (this.swiping) {
                    this.endX = motionEvent.getX(0);
                    this.endY = motionEvent.getY(0);
                    break;
                }
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                this.swiping = true;
                this.startX = motionEvent.getX(0);
                this.startY = motionEvent.getY(0);
                break;
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                if (this.swiping) {
                    handleSwipe(this.direction, this.endX - this.startX, this.endY - this.startY);
                    this.swiping = false;
                    break;
                }
                break;
        }
        return true;
    }
}
