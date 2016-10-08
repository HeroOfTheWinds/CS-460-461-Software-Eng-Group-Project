package crittercism.android;

import java.util.Locale;

public final class cg {
    public static final cg f616a;
    private volatile int f617b;
    private final long f618c;

    static {
        f616a = new cg();
    }

    private cg() {
        this.f617b = 1;
        this.f618c = System.currentTimeMillis();
    }

    private int m687b() {
        int i;
        synchronized (this) {
            i = this.f617b;
            this.f617b = i + 1;
        }
        return i;
    }

    public final String m688a() {
        return String.format(Locale.US, "%d.%d.%09d", new Object[]{Integer.valueOf(1), Long.valueOf(this.f618c), Integer.valueOf(m687b())});
    }
}
