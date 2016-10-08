package com.unity3d.player;

import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.unity3d.player.l */
public final class C0826l implements C0817h {
    private Choreographer f177a;
    private long f178b;
    private FrameCallback f179c;
    private Lock f180d;

    /* renamed from: com.unity3d.player.l.1 */
    final class C08251 implements FrameCallback {
        final /* synthetic */ UnityPlayer f175a;
        final /* synthetic */ C0826l f176b;

        C08251(C0826l c0826l, UnityPlayer unityPlayer) {
            this.f176b = c0826l;
            this.f175a = unityPlayer;
        }

        public final void doFrame(long j) {
            UnityPlayer.lockNativeAccess();
            if (C0841v.m193c()) {
                this.f175a.nativeAddVSyncTime(j);
            }
            UnityPlayer.unlockNativeAccess();
            this.f176b.f180d.lock();
            if (this.f176b.f177a != null) {
                this.f176b.f177a.postFrameCallback(this.f176b.f179c);
            }
            this.f176b.f180d.unlock();
        }
    }

    public C0826l() {
        this.f177a = null;
        this.f178b = 0;
        this.f180d = new ReentrantLock();
    }

    public final void m152a() {
        this.f180d.lock();
        if (this.f177a != null) {
            this.f177a.removeFrameCallback(this.f179c);
        }
        this.f177a = null;
        this.f180d.unlock();
    }

    public final void m153a(UnityPlayer unityPlayer) {
        this.f180d.lock();
        if (this.f177a == null) {
            this.f177a = Choreographer.getInstance();
            if (this.f177a != null) {
                C0827m.Log(4, "Choreographer available: Enabling VSYNC timing");
                this.f179c = new C08251(this, unityPlayer);
                this.f177a.postFrameCallback(this.f179c);
            }
        }
        this.f180d.unlock();
    }
}
