package crittercism.android;

import java.io.OutputStream;

/* renamed from: crittercism.android.w */
public final class C1150w extends OutputStream implements al {
    private ae f867a;
    private OutputStream f868b;
    private C1108c f869c;
    private af f870d;

    public C1150w(ae aeVar, OutputStream outputStream) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (outputStream == null) {
            throw new NullPointerException("output stream was null");
        } else {
            this.f867a = aeVar;
            this.f868b = outputStream;
            this.f870d = m855b();
            if (this.f870d == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    private void m847a(byte[] bArr, int i, int i2) {
        try {
            this.f870d.m276a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            this.f870d = as.f314d;
        }
    }

    private C1108c m848d() {
        if (this.f869c == null) {
            this.f869c = this.f867a.m253a();
        }
        C1108c c1108c = this.f869c;
        return this.f869c;
    }

    public final af m849a() {
        return this.f870d;
    }

    public final void m850a(int i) {
    }

    public final void m851a(af afVar) {
        this.f870d = afVar;
    }

    public final void m852a(String str) {
        C1108c d = m848d();
        if (d != null) {
            d.m667b(str);
        }
    }

    public final void m853a(String str, String str2) {
        C1108c d = m848d();
        d.m665b();
        d.f577f = str;
        d.f580i = null;
        C1136k c1136k = d.f579h;
        if (str2 != null) {
            c1136k.f826c = str2;
        }
        this.f867a.m254a(d);
    }

    public final boolean m854a(OutputStream outputStream) {
        return this.f868b == outputStream;
    }

    public final af m855b() {
        return new an(this);
    }

    public final void m856b(int i) {
        C1108c c1108c = this.f869c;
        this.f869c = null;
        if (c1108c != null) {
            c1108c.m671d((long) i);
        }
    }

    public final String m857c() {
        C1108c d = m848d();
        return d != null ? d.f577f : null;
    }

    public final void close() {
        this.f868b.close();
    }

    public final void flush() {
        this.f868b.flush();
    }

    public final void write(int i) {
        this.f868b.write(i);
        try {
            this.f870d.m277a(i);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            this.f870d = as.f314d;
        }
    }

    public final void write(byte[] bArr) {
        this.f868b.write(bArr);
        if (bArr != null) {
            m847a(bArr, 0, bArr.length);
        }
    }

    public final void write(byte[] bArr, int i, int i2) {
        this.f868b.write(bArr, i, i2);
        if (bArr != null) {
            m847a(bArr, i, i2);
        }
    }
}
