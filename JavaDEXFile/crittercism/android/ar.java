package crittercism.android;

import org.apache.http.util.CharArrayBuffer;

public final class ar extends af {
    private af f313d;

    public ar(af afVar) {
        super(afVar);
        this.f313d = afVar;
    }

    public final boolean m346a(int i) {
        if (i == -1) {
            this.f295a.m323a(as.f314d);
            return true;
        }
        this.c++;
        if (((char) i) != '\n') {
            return false;
        }
        this.f313d.m281b(m275a());
        this.f295a.m323a(this.f313d);
        return true;
    }

    public final boolean m347a(CharArrayBuffer charArrayBuffer) {
        return true;
    }

    public final af m348b() {
        return this;
    }

    public final af m349c() {
        return this;
    }

    protected final int m350d() {
        return 0;
    }

    protected final int m351e() {
        return 0;
    }
}
