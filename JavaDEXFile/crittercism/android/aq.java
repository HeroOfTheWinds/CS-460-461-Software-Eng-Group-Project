package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import org.apache.http.util.CharArrayBuffer;

public final class aq extends af {
    private boolean f312d;

    public aq(af afVar) {
        super(afVar);
        this.f312d = false;
    }

    public final boolean m341a(CharArrayBuffer charArrayBuffer) {
        boolean z = false;
        if (charArrayBuffer.substringTrimmed(0, charArrayBuffer.length()).length() == 0) {
            z = true;
        }
        this.f312d = z;
        return true;
    }

    public final af m342b() {
        if (this.f312d) {
            this.f295a.m327b(m275a());
            return this.f295a.m326b();
        }
        this.f296b.clear();
        return this;
    }

    public final af m343c() {
        this.f296b.clear();
        return new ar(this);
    }

    protected final int m344d() {
        return 8;
    }

    protected final int m345e() {
        return AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
    }
}
