package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class aj extends af {
    public aj(af afVar) {
        super(afVar);
    }

    public final boolean m307a(int i) {
        if (i == -1) {
            this.f295a.m327b(m275a());
            this.f295a.m323a(as.f314d);
            return true;
        }
        this.c++;
        return false;
    }

    public final boolean m308a(CharArrayBuffer charArrayBuffer) {
        return true;
    }

    public final int m309b(byte[] bArr, int i, int i2) {
        if (i2 == -1) {
            this.f295a.m327b(m275a());
            this.f295a.m323a(as.f314d);
            return -1;
        }
        this.c += i2;
        return i2;
    }

    public final af m310b() {
        return as.f314d;
    }

    public final af m311c() {
        return as.f314d;
    }

    protected final int m312d() {
        return 0;
    }

    protected final int m313e() {
        return Integer.MAX_VALUE;
    }

    public final void m314f() {
        this.f295a.m327b(m275a());
        this.f295a.m323a(as.f314d);
    }
}
