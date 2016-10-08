package com.unity3d.player;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.google.android.gms.location.LocationStatusCodes;

/* renamed from: com.unity3d.player.d */
public final class C0815d implements C0814f {
    private static final SurfaceTexture f161a;
    private static final int f162b;
    private volatile boolean f163c;

    /* renamed from: com.unity3d.player.d.1 */
    final class C08121 implements OnSystemUiVisibilityChangeListener {
        final /* synthetic */ View f157a;
        final /* synthetic */ C0815d f158b;

        C08121(C0815d c0815d, View view) {
            this.f158b = c0815d;
            this.f157a = view;
        }

        public final void onSystemUiVisibilityChange(int i) {
            this.f158b.m129a(this.f157a, (int) LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
        }
    }

    /* renamed from: com.unity3d.player.d.2 */
    final class C08132 implements Runnable {
        final /* synthetic */ View f159a;
        final /* synthetic */ C0815d f160b;

        C08132(C0815d c0815d, View view) {
            this.f160b = c0815d;
            this.f159a = view;
        }

        public final void run() {
            this.f160b.m133a(this.f159a, this.f160b.f163c);
        }
    }

    static {
        f161a = new SurfaceTexture(-1);
        f162b = C0833q.f196f ? 5894 : 1;
    }

    private void m129a(View view, int i) {
        Handler handler = view.getHandler();
        if (handler == null) {
            m133a(view, this.f163c);
        } else {
            handler.postDelayed(new C08132(this, view), 1000);
        }
    }

    public final void m132a(View view) {
        if (!C0833q.f197g) {
            view.setOnSystemUiVisibilityChangeListener(new C08121(this, view));
        }
    }

    public final void m133a(View view, boolean z) {
        this.f163c = z;
        view.setSystemUiVisibility(this.f163c ? view.getSystemUiVisibility() | f162b : view.getSystemUiVisibility() & (f162b ^ -1));
    }

    public final boolean m134a(Camera camera) {
        try {
            camera.setPreviewTexture(f161a);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final void m135b(View view) {
        if (!C0833q.f196f && this.f163c) {
            m133a(view, false);
            this.f163c = true;
        }
        m129a(view, (int) LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
    }
}
