package com.unity3d.player;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.SurfaceHolder;
import com.google.android.gms.location.LocationStatusCodes;
import com.upsight.android.internal.util.NetworkHelper;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.unity3d.player.a */
final class C0807a {
    Camera f144a;
    Parameters f145b;
    Size f146c;
    int f147d;
    int[] f148e;
    C0805b f149f;
    private final Object[] f150g;
    private final int f151h;
    private final int f152i;
    private final int f153j;
    private final int f154k;

    /* renamed from: com.unity3d.player.a.a */
    interface C0803a {
        void onCameraFrame(C0807a c0807a, byte[] bArr);
    }

    /* renamed from: com.unity3d.player.a.1 */
    final class C08041 implements PreviewCallback {
        long f136a;
        final /* synthetic */ C0803a f137b;
        final /* synthetic */ C0807a f138c;

        C08041(C0807a c0807a, C0803a c0803a) {
            this.f138c = c0807a;
            this.f137b = c0803a;
            this.f136a = 0;
        }

        public final void onPreviewFrame(byte[] bArr, Camera camera) {
            if (this.f138c.f144a == camera) {
                this.f137b.onCameraFrame(this.f138c, bArr);
            }
        }
    }

    /* renamed from: com.unity3d.player.a.2 */
    final class C08062 extends C0805b {
        Camera f142a;
        final /* synthetic */ C0807a f143b;

        C08062(C0807a c0807a) {
            this.f143b = c0807a;
            super(3);
            this.f142a = this.f143b.f144a;
        }

        public final void surfaceCreated(SurfaceHolder surfaceHolder) {
            synchronized (this.f143b.f150g) {
                if (this.f143b.f144a != this.f142a) {
                    return;
                }
                try {
                    this.f143b.f144a.setPreviewDisplay(surfaceHolder);
                    this.f143b.f144a.startPreview();
                } catch (Exception e) {
                    C0827m.Log(6, "Unable to initialize webcam data stream: " + e.getMessage());
                }
            }
        }

        public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            synchronized (this.f143b.f150g) {
                if (this.f143b.f144a != this.f142a) {
                    return;
                }
                this.f143b.f144a.stopPreview();
            }
        }
    }

    public C0807a(int i, int i2, int i3, int i4) {
        this.f150g = new Object[0];
        this.f151h = i;
        this.f152i = C0807a.m111a(i2, 640);
        this.f153j = C0807a.m111a(i3, 480);
        this.f154k = C0807a.m111a(i4, 24);
    }

    private static final int m111a(int i, int i2) {
        return i != 0 ? i : i2;
    }

    private static void m112a(Parameters parameters) {
        if (parameters.getSupportedColorEffects() != null) {
            parameters.setColorEffect(NetworkHelper.NETWORK_OPERATOR_NONE);
        }
        if (parameters.getSupportedFocusModes().contains("continuous-video")) {
            parameters.setFocusMode("continuous-video");
        }
    }

    private void m114b(C0803a c0803a) {
        synchronized (this.f150g) {
            this.f144a = Camera.open(this.f151h);
            this.f145b = this.f144a.getParameters();
            this.f146c = m117f();
            this.f148e = m116e();
            this.f147d = m115d();
            C0807a.m112a(this.f145b);
            this.f145b.setPreviewSize(this.f146c.width, this.f146c.height);
            this.f145b.setPreviewFpsRange(this.f148e[0], this.f148e[1]);
            this.f144a.setParameters(this.f145b);
            PreviewCallback c08041 = new C08041(this, c0803a);
            int i = (((this.f146c.width * this.f146c.height) * this.f147d) / 8) + AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
            this.f144a.addCallbackBuffer(new byte[i]);
            this.f144a.addCallbackBuffer(new byte[i]);
            this.f144a.setPreviewCallbackWithBuffer(c08041);
        }
    }

    private final int m115d() {
        this.f145b.setPreviewFormat(17);
        return ImageFormat.getBitsPerPixel(17);
    }

    private final int[] m116e() {
        double d = (double) (this.f154k * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
        List supportedPreviewFpsRange = this.f145b.getSupportedPreviewFpsRange();
        if (supportedPreviewFpsRange == null) {
            supportedPreviewFpsRange = new ArrayList();
        }
        int[] iArr = new int[]{this.f154k * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, this.f154k * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE};
        double d2 = Double.MAX_VALUE;
        for (int[] iArr2 : r0) {
            int[] iArr22;
            double abs = Math.abs(Math.log(d / ((double) iArr22[0]))) + Math.abs(Math.log(d / ((double) iArr22[1])));
            if (abs < d2) {
                d2 = abs;
            } else {
                iArr22 = iArr;
            }
            iArr = iArr22;
        }
        return iArr;
    }

    private final Size m117f() {
        double d = (double) this.f152i;
        double d2 = (double) this.f153j;
        Size size = null;
        double d3 = Double.MAX_VALUE;
        for (Size size2 : this.f145b.getSupportedPreviewSizes()) {
            Size size22;
            double abs = Math.abs(Math.log(d / ((double) size22.width))) + Math.abs(Math.log(d2 / ((double) size22.height)));
            if (abs >= d3) {
                size22 = size;
                abs = d3;
            }
            size = size22;
            d3 = abs;
        }
        return size;
    }

    public final int m118a() {
        return this.f151h;
    }

    public final void m119a(C0803a c0803a) {
        synchronized (this.f150g) {
            if (this.f144a == null) {
                m114b(c0803a);
            }
            if (C0833q.f191a && C0833q.f199i.m127a(this.f144a)) {
                this.f144a.startPreview();
                return;
            }
            if (this.f149f == null) {
                this.f149f = new C08062(this);
                this.f149f.m109a();
            }
        }
    }

    public final void m120a(byte[] bArr) {
        synchronized (this.f150g) {
            if (this.f144a != null) {
                this.f144a.addCallbackBuffer(bArr);
            }
        }
    }

    public final Size m121b() {
        return this.f146c;
    }

    public final void m122c() {
        synchronized (this.f150g) {
            if (this.f144a != null) {
                this.f144a.setPreviewCallbackWithBuffer(null);
                this.f144a.stopPreview();
                this.f144a.release();
                this.f144a = null;
            }
            if (this.f149f != null) {
                this.f149f.m110b();
                this.f149f = null;
            }
        }
    }
}
