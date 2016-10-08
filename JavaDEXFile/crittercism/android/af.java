package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public abstract class af {
    al f295a;
    CharArrayBuffer f296b;
    protected int f297c;
    private int f298d;

    public af(af afVar) {
        m273a(afVar.f295a, afVar.f297c);
    }

    public af(al alVar) {
        m273a(alVar, 0);
    }

    private void m273a(al alVar, int i) {
        this.f295a = alVar;
        this.f298d = m284e();
        this.f296b = new CharArrayBuffer(m283d());
        this.f297c = i;
    }

    private void m274g() {
        this.f295a.m323a(as.f314d);
    }

    public final int m275a() {
        return this.f297c;
    }

    public final void m276a(byte[] bArr, int i, int i2) {
        int b = m279b(bArr, i, i2);
        while (b > 0 && b < i2) {
            int b2 = this.f295a.m321a().m279b(bArr, i + b, i2 - b);
            if (b2 > 0) {
                b += b2;
            } else {
                return;
            }
        }
    }

    public boolean m277a(int i) {
        if (i == -1) {
            m274g();
        } else {
            af b;
            this.f297c++;
            char c = (char) i;
            if (c == '\n') {
                b = m278a(this.f296b) ? m280b() : as.f314d;
            } else if (this.f296b.length() < this.f298d) {
                this.f296b.append(c);
                b = this;
            } else {
                b = m282c();
            }
            if (b != this) {
                this.f295a.m323a(b);
            }
            if (b == this) {
                return false;
            }
        }
        return true;
    }

    public abstract boolean m278a(CharArrayBuffer charArrayBuffer);

    protected int m279b(byte[] bArr, int i, int i2) {
        boolean z = false;
        int i3 = -1;
        if (i2 == -1) {
            m274g();
        } else if (!(bArr == null || i2 == 0)) {
            i3 = 0;
            while (!z && i3 < i2) {
                z = m277a((char) bArr[i + i3]);
                i3++;
            }
        }
        return i3;
    }

    public abstract af m280b();

    public final void m281b(int i) {
        this.f297c = i;
    }

    public abstract af m282c();

    protected abstract int m283d();

    protected abstract int m284e();

    public void m285f() {
        if (this.f295a != null) {
            this.f295a.m323a(as.f314d);
        }
    }

    public final String toString() {
        return this.f296b.toString();
    }
}
