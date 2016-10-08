package crittercism.android;

import java.io.InputStream;

/* renamed from: crittercism.android.t */
public final class C1146t extends InputStream {
    private final InputStream f853a;
    private final C1126e f854b;
    private final C1108c f855c;

    public C1146t(InputStream inputStream, C1126e c1126e, C1108c c1108c) {
        if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1126e == null) {
            throw new NullPointerException("dispatch was null");
        } else if (c1108c == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.f853a = inputStream;
            this.f854b = c1126e;
            this.f855c = c1108c;
        }
    }

    private void m839a(int i, int i2) {
        try {
            if (this.f855c == null) {
                return;
            }
            if (i == -1) {
                this.f854b.m785a(this.f855c);
            } else {
                this.f855c.m658a((long) i2);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private void m840a(Exception exception) {
        try {
            this.f855c.m663a((Throwable) exception);
            this.f854b.m785a(this.f855c);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final int available() {
        return this.f853a.available();
    }

    public final void close() {
        this.f853a.close();
    }

    public final void mark(int i) {
        this.f853a.mark(i);
    }

    public final boolean markSupported() {
        return this.f853a.markSupported();
    }

    public final int read() {
        try {
            int read = this.f853a.read();
            m839a(read, 1);
            return read;
        } catch (Exception e) {
            m840a(e);
            throw e;
        }
    }

    public final int read(byte[] bArr) {
        try {
            int read = this.f853a.read(bArr);
            m839a(read, read);
            return read;
        } catch (Exception e) {
            m840a(e);
            throw e;
        }
    }

    public final int read(byte[] bArr, int i, int i2) {
        try {
            int read = this.f853a.read(bArr, i, i2);
            m839a(read, read);
            return read;
        } catch (Exception e) {
            m840a(e);
            throw e;
        }
    }

    public final void reset() {
        synchronized (this) {
            this.f853a.reset();
        }
    }

    public final long skip(long j) {
        long skip = this.f853a.skip(j);
        try {
            if (this.f855c != null) {
                this.f855c.m658a(skip);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        return skip;
    }
}
