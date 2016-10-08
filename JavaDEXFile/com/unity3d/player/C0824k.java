package com.unity3d.player;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

/* renamed from: com.unity3d.player.k */
public final class C0824k implements C0816g {
    private Object f172a;
    private Presentation f173b;
    private DisplayListener f174c;

    /* renamed from: com.unity3d.player.k.1 */
    final class C08201 implements DisplayListener {
        final /* synthetic */ UnityPlayer f164a;
        final /* synthetic */ C0824k f165b;

        C08201(C0824k c0824k, UnityPlayer unityPlayer) {
            this.f165b = c0824k;
            this.f164a = unityPlayer;
        }

        public final void onDisplayAdded(int i) {
            this.f164a.displayChanged(-1, null);
        }

        public final void onDisplayChanged(int i) {
            this.f164a.displayChanged(-1, null);
        }

        public final void onDisplayRemoved(int i) {
            this.f164a.displayChanged(-1, null);
        }
    }

    /* renamed from: com.unity3d.player.k.2 */
    final class C08232 implements Runnable {
        final /* synthetic */ Context f168a;
        final /* synthetic */ Display f169b;
        final /* synthetic */ UnityPlayer f170c;
        final /* synthetic */ C0824k f171d;

        /* renamed from: com.unity3d.player.k.2.1 */
        final class C08221 extends Presentation {
            final /* synthetic */ C08232 f167a;

            /* renamed from: com.unity3d.player.k.2.1.1 */
            final class C08211 implements Callback {
                final /* synthetic */ C08221 f166a;

                C08211(C08221 c08221) {
                    this.f166a = c08221;
                }

                public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                    this.f166a.f167a.f170c.displayChanged(1, surfaceHolder.getSurface());
                }

                public final void surfaceCreated(SurfaceHolder surfaceHolder) {
                    this.f166a.f167a.f170c.displayChanged(1, surfaceHolder.getSurface());
                }

                public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    this.f166a.f167a.f170c.displayChanged(1, null);
                }
            }

            C08221(C08232 c08232, Context context, Display display) {
                this.f167a = c08232;
                super(context, display);
            }

            protected final void onCreate(Bundle bundle) {
                View surfaceView = new SurfaceView(this.f167a.f168a);
                surfaceView.getHolder().addCallback(new C08211(this));
                setContentView(surfaceView);
            }

            public final void onDisplayRemoved() {
                dismiss();
                synchronized (this.f167a.f171d.f172a) {
                    this.f167a.f171d.f173b = null;
                }
            }
        }

        C08232(C0824k c0824k, Context context, Display display, UnityPlayer unityPlayer) {
            this.f171d = c0824k;
            this.f168a = context;
            this.f169b = display;
            this.f170c = unityPlayer;
        }

        public final void run() {
            synchronized (this.f171d.f172a) {
                if (this.f171d.f173b != null) {
                    this.f171d.f173b.dismiss();
                }
                this.f171d.f173b = new C08221(this, this.f168a, this.f169b);
                this.f171d.f173b.show();
            }
        }
    }

    public C0824k() {
        this.f172a = new Object[0];
    }

    public final void m146a(Context context) {
        if (this.f174c != null) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            if (displayManager != null) {
                displayManager.unregisterDisplayListener(this.f174c);
            }
        }
    }

    public final void m147a(UnityPlayer unityPlayer, Context context) {
        DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
        if (displayManager != null) {
            displayManager.registerDisplayListener(new C08201(this, unityPlayer), null);
        }
    }

    public final boolean m148a(UnityPlayer unityPlayer, Context context, int i) {
        synchronized (this.f172a) {
            Display display;
            if (this.f173b != null && this.f173b.isShowing()) {
                display = this.f173b.getDisplay();
                if (display != null && display.getDisplayId() == i) {
                    return true;
                }
            }
            DisplayManager displayManager = (DisplayManager) context.getSystemService("display");
            if (displayManager == null) {
                return false;
            }
            display = displayManager.getDisplay(i);
            if (display == null) {
                return false;
            }
            unityPlayer.m105b(new C08232(this, context, display, unityPlayer));
            return true;
        }
    }
}
