package crittercism.android;

import crittercism.android.C1108c.C1107a;
import java.io.InputStream;

/* renamed from: crittercism.android.x */
public final class C1151x extends InputStream implements al {
    private ae f871a;
    private C1108c f872b;
    private InputStream f873c;
    private C1126e f874d;
    private af f875e;

    public C1151x(ae aeVar, InputStream inputStream, C1126e c1126e) {
        if (aeVar == null) {
            throw new NullPointerException("socket was null");
        } else if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1126e == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.f871a = aeVar;
            this.f873c = inputStream;
            this.f874d = c1126e;
            this.f875e = m867b();
            if (this.f875e == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    private void m858a(Exception exception) {
        try {
            C1108c e = m860e();
            e.m663a((Throwable) exception);
            this.f874d.m786a(e, C1107a.PARSING_INPUT_STREAM_LOG_ERROR);
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (IllegalStateException e3) {
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private void m859a(byte[] bArr, int i, int i2) {
        try {
            this.f875e.m276a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (IllegalStateException e2) {
            this.f875e = as.f314d;
        } catch (Throwable th) {
            this.f875e = as.f314d;
            dx.m777a(th);
        }
    }

    private C1108c m860e() {
        if (this.f872b == null) {
            this.f872b = this.f871a.m255b();
        }
        if (this.f872b != null) {
            return this.f872b;
        }
        throw new IllegalStateException("No statistics were queued up.");
    }

    public final af m861a() {
        return this.f875e;
    }

    public final void m862a(int i) {
        C1108c e = m860e();
        e.m668c();
        e.f576e = i;
    }

    public final void m863a(af afVar) {
        this.f875e = afVar;
    }

    public final void m864a(String str) {
    }

    public final void m865a(String str, String str2) {
    }

    public final boolean m866a(InputStream inputStream) {
        return this.f873c == inputStream;
    }

    public final int available() {
        return this.f873c.available();
    }

    public final af m867b() {
        return new ap(this);
    }

    public final void m868b(int i) {
        C1108c c1108c = null;
        C1108c c1108c2 = this.f872b;
        if (this.f872b != null) {
            int i2 = this.f872b.f576e;
            if (i2 >= 100 && i2 < 200) {
                c1108c = new C1108c(this.f872b.m656a());
                c1108c.m673e(this.f872b.f572a);
                c1108c.m671d(this.f872b.f575d);
                c1108c.f577f = this.f872b.f577f;
            }
            this.f872b.m666b((long) i);
            this.f874d.m786a(this.f872b, C1107a.INPUT_STREAM_FINISHED);
        }
        this.f872b = c1108c;
    }

    public final String m869c() {
        return m860e().f577f;
    }

    public final void close() {
        try {
            this.f875e.m285f();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
        this.f873c.close();
    }

    public final void m870d() {
        if (this.f872b != null) {
            cn cnVar = this.f872b.f578g;
            Object obj = (cnVar.f651a == co.Android.ordinal() && cnVar.f652b == cm.OK.m691a()) ? 1 : null;
            if (obj != null && this.f875e != null) {
                this.f875e.m285f();
            }
        }
    }

    public final void mark(int i) {
        this.f873c.mark(i);
    }

    public final boolean markSupported() {
        return this.f873c.markSupported();
    }

    public final int read() {
        try {
            int read = this.f873c.read();
            try {
                this.f875e.m277a(read);
            } catch (ThreadDeath e) {
                throw e;
            } catch (IllegalStateException e2) {
                this.f875e = as.f314d;
            } catch (Throwable th) {
                this.f875e = as.f314d;
                dx.m777a(th);
            }
            return read;
        } catch (Exception e3) {
            m858a(e3);
            throw e3;
        }
    }

    public final int read(byte[] bArr) {
        try {
            int read = this.f873c.read(bArr);
            m859a(bArr, 0, read);
            return read;
        } catch (Exception e) {
            m858a(e);
            throw e;
        }
    }

    public final int read(byte[] bArr, int i, int i2) {
        try {
            int read = this.f873c.read(bArr, i, i2);
            m859a(bArr, i, read);
            return read;
        } catch (Exception e) {
            m858a(e);
            throw e;
        }
    }

    public final void reset() {
        synchronized (this) {
            this.f873c.reset();
        }
    }

    public final long skip(long j) {
        return this.f873c.skip(j);
    }
}
