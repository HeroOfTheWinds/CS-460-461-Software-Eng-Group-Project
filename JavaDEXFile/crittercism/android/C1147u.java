package crittercism.android;

import java.io.OutputStream;

/* renamed from: crittercism.android.u */
public final class C1147u extends OutputStream {
    private final OutputStream f856a;
    private final C1108c f857b;

    public C1147u(OutputStream outputStream, C1108c c1108c) {
        if (outputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1108c == null) {
            throw new NullPointerException("stats were null");
        } else {
            this.f856a = outputStream;
            this.f857b = c1108c;
        }
    }

    public final void close() {
        this.f856a.close();
    }

    public final void flush() {
        this.f856a.flush();
    }

    public final void write(int i) {
        try {
            if (this.f857b != null) {
                this.f857b.m665b();
                this.f857b.m669c(1);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        this.f856a.write(i);
    }

    public final void write(byte[] bArr) {
        if (this.f857b != null) {
            this.f857b.m665b();
            if (bArr != null) {
                this.f857b.m669c((long) bArr.length);
            }
        }
        this.f856a.write(bArr);
    }

    public final void write(byte[] bArr, int i, int i2) {
        if (this.f857b != null) {
            this.f857b.m665b();
            if (bArr != null) {
                this.f857b.m669c((long) i2);
            }
        }
        this.f856a.write(bArr, i, i2);
    }
}
