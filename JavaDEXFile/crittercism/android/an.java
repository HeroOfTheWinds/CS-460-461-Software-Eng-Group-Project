package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public final class an extends af {
    public an(al alVar) {
        super(alVar);
    }

    public final boolean m330a(CharArrayBuffer charArrayBuffer) {
        try {
            RequestLine parseRequestLine = BasicLineParser.DEFAULT.parseRequestLine(charArrayBuffer, new ParserCursor(0, charArrayBuffer.length()));
            this.f295a.m325a(parseRequestLine.getMethod(), parseRequestLine.getUri());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public final af m331b() {
        return new am(this);
    }

    public final af m332c() {
        return as.f314d;
    }

    protected final int m333d() {
        return 64;
    }

    protected final int m334e() {
        return AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT;
    }
}
