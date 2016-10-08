package crittercism.android;

import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

public final class ap extends af {
    private int f311d;

    public ap(al alVar) {
        super(alVar);
        this.f311d = -1;
    }

    public final boolean m336a(CharArrayBuffer charArrayBuffer) {
        try {
            StatusLine parseStatusLine = BasicLineParser.DEFAULT.parseStatusLine(charArrayBuffer, new ParserCursor(0, charArrayBuffer.length()));
            this.f311d = parseStatusLine.getStatusCode();
            this.f295a.m322a(parseStatusLine.getStatusCode());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public final af m337b() {
        return new ao(this, this.f311d);
    }

    public final af m338c() {
        return as.f314d;
    }

    protected final int m339d() {
        return 20;
    }

    protected final int m340e() {
        return 64;
    }
}
