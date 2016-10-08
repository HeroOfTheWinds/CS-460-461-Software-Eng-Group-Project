package com.unity3d.player;

import android.app.Activity;
import android.content.ContextWrapper;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import spacemadness.com.lunarconsole.C1518R;

/* renamed from: com.unity3d.player.p */
public final class C0832p implements C0819j {
    private final Queue f188a;
    private final Activity f189b;
    private Runnable f190c;

    /* renamed from: com.unity3d.player.p.1 */
    final class C08311 implements Runnable {
        final /* synthetic */ C0832p f187a;

        C08311(C0832p c0832p) {
            this.f187a = c0832p;
        }

        private static void m158a(View view, MotionEvent motionEvent) {
            if (C0833q.f192b) {
                C0833q.f200j.m123a(view, motionEvent);
            }
        }

        public final void run() {
            while (true) {
                MotionEvent motionEvent = (MotionEvent) this.f187a.f188a.poll();
                if (motionEvent != null) {
                    View decorView = this.f187a.f189b.getWindow().getDecorView();
                    int source = motionEvent.getSource();
                    if ((source & 2) != 0) {
                        switch (motionEvent.getAction() & MotionEventCompat.ACTION_MASK) {
                            case C1518R.styleable.AdsAttrs_adSize /*0*/:
                            case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                            case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_LEFT /*4*/:
                            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                                decorView.dispatchTouchEvent(motionEvent);
                                break;
                            default:
                                C08311.m158a(decorView, motionEvent);
                                break;
                        }
                    } else if ((source & 4) != 0) {
                        decorView.dispatchTrackballEvent(motionEvent);
                    } else {
                        C08311.m158a(decorView, motionEvent);
                    }
                } else {
                    return;
                }
            }
        }
    }

    public C0832p(ContextWrapper contextWrapper) {
        this.f188a = new ConcurrentLinkedQueue();
        this.f190c = new C08311(this);
        this.f189b = (Activity) contextWrapper;
    }

    private static int m159a(PointerCoords[] pointerCoordsArr, float[] fArr, int i) {
        for (int i2 = 0; i2 < pointerCoordsArr.length; i2++) {
            PointerCoords pointerCoords = new PointerCoords();
            pointerCoordsArr[i2] = pointerCoords;
            int i3 = i + 1;
            pointerCoords.orientation = fArr[i];
            int i4 = i3 + 1;
            pointerCoords.pressure = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.size = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.toolMajor = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.toolMinor = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.touchMajor = fArr[i3];
            i3 = i4 + 1;
            pointerCoords.touchMinor = fArr[i4];
            i4 = i3 + 1;
            pointerCoords.x = fArr[i3];
            i = i4 + 1;
            pointerCoords.y = fArr[i4];
        }
        return i;
    }

    private static PointerCoords[] m161a(int i, float[] fArr) {
        PointerCoords[] pointerCoordsArr = new PointerCoords[i];
        C0832p.m159a(pointerCoordsArr, fArr, 0);
        return pointerCoordsArr;
    }

    public final void m163a(long j, long j2, int i, int i2, int[] iArr, float[] fArr, int i3, float f, float f2, int i4, int i5, int i6, int i7, int i8, long[] jArr, float[] fArr2) {
        if (this.f189b != null) {
            MotionEvent obtain = MotionEvent.obtain(j, j2, i, i2, iArr, C0832p.m161a(i2, fArr), i3, f, f2, i4, i5, i6, i7);
            int i9 = 0;
            for (int i10 = 0; i10 < i8; i10++) {
                PointerCoords[] pointerCoordsArr = new PointerCoords[i2];
                i9 = C0832p.m159a(pointerCoordsArr, fArr2, i9);
                obtain.addBatch(jArr[i10], pointerCoordsArr, i3);
            }
            this.f188a.add(obtain);
            this.f189b.runOnUiThread(this.f190c);
        }
    }
}
