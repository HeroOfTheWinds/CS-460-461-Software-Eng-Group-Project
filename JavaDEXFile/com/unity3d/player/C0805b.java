package com.unity3d.player;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/* renamed from: com.unity3d.player.b */
abstract class C0805b implements Callback {
    private final Activity f139a;
    private final int f140b;
    private SurfaceView f141c;

    /* renamed from: com.unity3d.player.b.1 */
    final class C08081 implements Runnable {
        final /* synthetic */ C0805b f155a;

        C08081(C0805b c0805b) {
            this.f155a = c0805b;
        }

        public final void run() {
            if (this.f155a.f141c == null) {
                this.f155a.f141c = new SurfaceView(C0839t.f219a.m185a());
                this.f155a.f141c.getHolder().setType(this.f155a.f140b);
                this.f155a.f141c.getHolder().addCallback(this.f155a);
                C0839t.f219a.m186a(this.f155a.f141c);
                this.f155a.f141c.setVisibility(0);
            }
        }
    }

    /* renamed from: com.unity3d.player.b.2 */
    final class C08092 implements Runnable {
        final /* synthetic */ C0805b f156a;

        C08092(C0805b c0805b) {
            this.f156a = c0805b;
        }

        public final void run() {
            if (this.f156a.f141c != null) {
                C0839t.f219a.m187b(this.f156a.f141c);
            }
            this.f156a.f141c = null;
        }
    }

    C0805b(int i) {
        this.f139a = (Activity) C0839t.f219a.m185a();
        this.f140b = 3;
    }

    final void m109a() {
        this.f139a.runOnUiThread(new C08081(this));
    }

    final void m110b() {
        this.f139a.runOnUiThread(new C08092(this));
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
