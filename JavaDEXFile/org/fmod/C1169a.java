package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C1169a implements Runnable {
    private final FMODAudioDevice f897a;
    private final ByteBuffer f898b;
    private final int f899c;
    private final int f900d;
    private final int f901e;
    private volatile Thread f902f;
    private volatile boolean f903g;
    private AudioRecord f904h;
    private boolean f905i;

    C1169a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f897a = fMODAudioDevice;
        this.f899c = i;
        this.f900d = i2;
        this.f901e = 2;
        this.f898b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    private void m880d() {
        if (this.f904h != null) {
            if (this.f904h.getState() == 1) {
                this.f904h.stop();
            }
            this.f904h.release();
            this.f904h = null;
        }
        this.f898b.position(0);
        this.f905i = false;
    }

    public final int m881a() {
        return this.f898b.capacity();
    }

    public final void m882b() {
        if (this.f902f != null) {
            m883c();
        }
        this.f903g = true;
        this.f902f = new Thread(this);
        this.f902f.start();
    }

    public final void m883c() {
        while (this.f902f != null) {
            this.f903g = false;
            try {
                this.f902f.join();
                this.f902f = null;
            } catch (InterruptedException e) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f903g) {
            int i2;
            if (!this.f905i && i > 0) {
                m880d();
                this.f904h = new AudioRecord(1, this.f899c, this.f900d, this.f901e, this.f898b.capacity());
                this.f905i = this.f904h.getState() == 1;
                if (this.f905i) {
                    this.f898b.position(0);
                    this.f904h.startRecording();
                    i2 = 3;
                    if (this.f905i || this.f904h.getRecordingState() != 3) {
                        i = i2;
                    } else {
                        this.f897a.fmodProcessMicData(this.f898b, this.f904h.read(this.f898b, this.f898b.capacity()));
                        this.f898b.position(0);
                        i = i2;
                    }
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f904h.getState() + ")");
                    i--;
                    m880d();
                }
            }
            i2 = i;
            if (this.f905i) {
            }
            i = i2;
        }
        m880d();
    }
}
