package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {
    private static int f886h;
    private static int f887i;
    private static int f888j;
    private static int f889k;
    private volatile Thread f890a;
    private volatile boolean f891b;
    private AudioTrack f892c;
    private boolean f893d;
    private ByteBuffer f894e;
    private byte[] f895f;
    private volatile C1169a f896g;

    static {
        f886h = 0;
        f887i = 1;
        f888j = 2;
        f889k = 3;
    }

    public FMODAudioDevice() {
        this.f890a = null;
        this.f891b = false;
        this.f892c = null;
        this.f893d = false;
        this.f894e = null;
        this.f895f = null;
    }

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        if (this.f892c != null) {
            if (this.f892c.getState() == 1) {
                this.f892c.stop();
            }
            this.f892c.release();
            this.f892c = null;
        }
        this.f894e = null;
        this.f895f = null;
        this.f893d = false;
    }

    public void close() {
        synchronized (this) {
            stop();
        }
    }

    native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f890a != null && this.f890a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f891b) {
            int i2;
            if (this.f893d || i <= 0) {
                i2 = i;
            } else {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f886h);
                int round = Math.round(((float) AudioTrack.getMinBufferSize(fmodGetInfo, 3, 2)) * 1.1f) & -4;
                int fmodGetInfo2 = fmodGetInfo(f887i);
                i2 = fmodGetInfo(f888j);
                if ((fmodGetInfo2 * i2) * 4 > round) {
                    round = (i2 * fmodGetInfo2) * 4;
                }
                this.f892c = new AudioTrack(3, fmodGetInfo, 3, 2, round, 1);
                this.f893d = this.f892c.getState() == 1;
                if (this.f893d) {
                    this.f894e = ByteBuffer.allocateDirect((fmodGetInfo2 * 2) * 2);
                    this.f895f = new byte[this.f894e.capacity()];
                    this.f892c.play();
                    i2 = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f892c.getState() + ")");
                    releaseAudioTrack();
                    i2 = i - 1;
                }
            }
            if (!this.f893d) {
                i = i2;
            } else if (fmodGetInfo(f889k) == 1) {
                fmodProcess(this.f894e);
                this.f894e.get(this.f895f, 0, this.f894e.capacity());
                this.f892c.write(this.f895f, 0, this.f894e.capacity());
                this.f894e.position(0);
                i = i2;
            } else {
                releaseAudioTrack();
                i = i2;
            }
        }
        releaseAudioTrack();
    }

    public void start() {
        synchronized (this) {
            if (this.f890a != null) {
                stop();
            }
            this.f890a = new Thread(this, "FMODAudioDevice");
            this.f890a.setPriority(10);
            this.f891b = true;
            this.f890a.start();
            if (this.f896g != null) {
                this.f896g.m882b();
            }
        }
    }

    public int startAudioRecord(int i, int i2, int i3) {
        int a;
        synchronized (this) {
            if (this.f896g == null) {
                this.f896g = new C1169a(this, i, i2);
                this.f896g.m882b();
            }
            a = this.f896g.m881a();
        }
        return a;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stop() {
        /*
        r1 = this;
        monitor-enter(r1);
    L_0x0001:
        r0 = r1.f890a;	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x0013;
    L_0x0005:
        r0 = 0;
        r1.f891b = r0;	 Catch:{ all -> 0x001e }
        r0 = r1.f890a;	 Catch:{ InterruptedException -> 0x0011 }
        r0.join();	 Catch:{ InterruptedException -> 0x0011 }
        r0 = 0;
        r1.f890a = r0;	 Catch:{ InterruptedException -> 0x0011 }
        goto L_0x0001;
    L_0x0011:
        r0 = move-exception;
        goto L_0x0001;
    L_0x0013:
        r0 = r1.f896g;	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x001c;
    L_0x0017:
        r0 = r1.f896g;	 Catch:{ all -> 0x001e }
        r0.m883c();	 Catch:{ all -> 0x001e }
    L_0x001c:
        monitor-exit(r1);
        return;
    L_0x001e:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.fmod.FMODAudioDevice.stop():void");
    }

    public void stopAudioRecord() {
        synchronized (this) {
            if (this.f896g != null) {
                this.f896g.m883c();
                this.f896g = null;
            }
        }
    }
}
