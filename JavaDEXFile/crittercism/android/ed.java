package crittercism.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class ed {
    public static final ed f784a;
    private ee f785b;
    private ThreadLocal f786c;

    /* renamed from: crittercism.android.ed.a */
    final class C1128a implements ee {
        final /* synthetic */ ed f783a;

        private C1128a(ed edVar) {
            this.f783a = edVar;
        }

        public final Date m793a() {
            return new Date();
        }
    }

    static {
        f784a = new ed();
    }

    private ed() {
        this.f785b = new C1128a();
        this.f786c = new ThreadLocal();
    }

    private SimpleDateFormat m794b() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) this.f786c.get();
        if (simpleDateFormat != null) {
            return simpleDateFormat;
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        simpleDateFormat.setLenient(false);
        this.f786c.set(simpleDateFormat);
        return simpleDateFormat;
    }

    public final long m795a(String str) {
        return m794b().parse(str).getTime();
    }

    public final String m796a() {
        return m797a(this.f785b.m792a());
    }

    public final String m797a(Date date) {
        return m794b().format(date);
    }
}
