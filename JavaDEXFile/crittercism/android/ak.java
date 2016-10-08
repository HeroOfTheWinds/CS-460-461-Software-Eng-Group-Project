package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.voxelbusters.nativeplugins.defines.Keys.WebView;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.message.BasicLineParser;
import org.apache.http.util.CharArrayBuffer;

public abstract class ak extends af {
    boolean f305d;
    int f306e;
    boolean f307f;
    private boolean f308g;
    private boolean f309h;

    public ak(af afVar) {
        super(afVar);
        this.f305d = false;
        this.f308g = false;
        this.f309h = false;
        this.f307f = false;
    }

    public final boolean m315a(CharArrayBuffer charArrayBuffer) {
        int length = this.f296b.length();
        boolean z = length == 0 || (length == 1 && this.f296b.charAt(0) == '\r');
        if (z) {
            this.f309h = true;
        } else {
            try {
                Header parseHeader = BasicLineParser.DEFAULT.parseHeader(charArrayBuffer);
                if (!this.f305d && parseHeader.getName().equalsIgnoreCase("content-length")) {
                    length = Integer.parseInt(parseHeader.getValue());
                    if (length < 0) {
                        return false;
                    }
                    this.f305d = true;
                    this.f306e = length;
                    return true;
                } else if (parseHeader.getName().equalsIgnoreCase("transfer-encoding")) {
                    this.f307f = parseHeader.getValue().equalsIgnoreCase("chunked");
                    return true;
                } else if (!this.f308g && parseHeader.getName().equalsIgnoreCase(WebView.HOST)) {
                    String value = parseHeader.getValue();
                    if (value != null) {
                        this.f308g = true;
                        this.f295a.m324a(value);
                        return true;
                    }
                }
            } catch (ParseException e) {
                return false;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
        return true;
    }

    public final af m316b() {
        if (this.f309h) {
            return m320g();
        }
        this.f296b.clear();
        return this;
    }

    public final af m317c() {
        this.f296b.clear();
        return new ar(this);
    }

    protected final int m318d() {
        return 32;
    }

    protected final int m319e() {
        return AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
    }

    protected abstract af m320g();
}
