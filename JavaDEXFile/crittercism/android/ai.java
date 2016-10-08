package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import org.apache.http.util.CharArrayBuffer;

public final class ai extends af {
    private int f304d;

    public ai(af afVar) {
        super(afVar);
        this.f304d = -1;
    }

    public final boolean m301a(CharArrayBuffer charArrayBuffer) {
        int indexOf = charArrayBuffer.indexOf(59);
        int length = charArrayBuffer.length();
        if (indexOf > 0) {
            length = indexOf;
        }
        try {
            this.f304d = Integer.parseInt(charArrayBuffer.substringTrimmed(0, length), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public final af m302b() {
        int i = this.f304d;
        if (this.f304d == 0) {
            return new aq(this);
        }
        this.f296b.clear();
        return new ah(this, this.f304d);
    }

    public final af m303c() {
        return as.f314d;
    }

    protected final int m304d() {
        return 16;
    }

    protected final int m305e() {
        return AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    }

    public final void m306f() {
        this.f295a.m327b(m275a());
        this.f295a.m323a(as.f314d);
    }
}
