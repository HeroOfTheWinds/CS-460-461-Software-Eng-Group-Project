package crittercism.android;

import android.util.SparseArray;

/* renamed from: crittercism.android.b */
public enum C1066b {
    MOBILE(0),
    WIFI(1),
    UNKNOWN(2),
    NOT_CONNECTED(3);
    
    private static SparseArray f390e;
    private int f392f;

    static {
        SparseArray sparseArray = new SparseArray();
        f390e = sparseArray;
        sparseArray.put(0, MOBILE);
        f390e.put(1, WIFI);
    }

    private C1066b(int i) {
        this.f392f = i;
    }

    public static C1066b m451a(int i) {
        C1066b c1066b = (C1066b) f390e.get(i);
        return c1066b == null ? UNKNOWN : c1066b;
    }

    public final int m452a() {
        return this.f392f;
    }
}
