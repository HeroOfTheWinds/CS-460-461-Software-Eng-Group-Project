package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ah extends af {
    private ai f301d;
    private int f302e;
    private int f303f;

    public ah(ai aiVar, int i) {
        super((af) aiVar);
        this.f303f = 0;
        this.f301d = aiVar;
        this.f302e = i;
    }

    public final boolean m294a(int i) {
        if (this.f303f < this.f302e + 2) {
            if (i == -1) {
                this.f295a.m327b(m275a());
                this.f295a.m323a(as.f314d);
                return true;
            }
            this.c++;
            char c = (char) i;
            this.f303f++;
            if (this.f303f > this.f302e) {
                if (c == '\n') {
                    this.f301d.m281b(m275a());
                    this.f295a.m323a(this.f301d);
                    return true;
                } else if (this.f303f == this.f302e + 2 && c != '\n') {
                    this.f295a.m323a(as.f314d);
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean m295a(CharArrayBuffer charArrayBuffer) {
        return true;
    }

    public final af m296b() {
        return this.f301d;
    }

    public final af m297c() {
        return null;
    }

    protected final int m298d() {
        return 0;
    }

    protected final int m299e() {
        return 0;
    }

    public final void m300f() {
        this.f295a.m327b(m275a());
        this.f295a.m323a(as.f314d);
    }
}
