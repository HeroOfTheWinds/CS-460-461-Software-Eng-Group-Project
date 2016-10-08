package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ag extends af {
    private int f299d;
    private int f300e;

    public ag(af afVar, int i) {
        super(afVar);
        this.f300e = 0;
        this.f299d = i;
    }

    public final boolean m286a(int i) {
        if (i == -1) {
            this.f295a.m323a(as.f314d);
            return true;
        }
        this.f300e++;
        this.c++;
        if (this.f300e != this.f299d) {
            return false;
        }
        this.f295a.m327b(m275a());
        this.f295a.m323a(this.f295a.m326b());
        return true;
    }

    public final boolean m287a(CharArrayBuffer charArrayBuffer) {
        return true;
    }

    public final int m288b(byte[] bArr, int i, int i2) {
        if (i2 == -1) {
            this.f295a.m323a(as.f314d);
            return -1;
        } else if (this.f300e + i2 < this.f299d) {
            this.f300e += i2;
            this.c += i2;
            return i2;
        } else {
            i2 = this.f299d - this.f300e;
            this.c += i2;
            this.f295a.m327b(m275a());
            this.f295a.m323a(this.f295a.m326b());
            return i2;
        }
    }

    public final af m289b() {
        return as.f314d;
    }

    public final af m290c() {
        return as.f314d;
    }

    protected final int m291d() {
        return 0;
    }

    protected final int m292e() {
        return 0;
    }

    public final void m293f() {
        this.f295a.m327b(m275a());
        this.f295a.m323a(as.f314d);
    }
}
