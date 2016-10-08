package crittercism.android;

import java.util.List;
import java.util.Map;

/* renamed from: crittercism.android.n */
public abstract class C1139n {
    Map f837a;

    public C1139n(Map map) {
        this.f837a = map;
    }

    private String m828c(String str) {
        List list = (List) this.f837a.get(str);
        return list != null ? (String) list.get(list.size() - 1) : null;
    }

    public final long m829a(String str) {
        long j = Long.MAX_VALUE;
        String c = m828c(str);
        if (c != null) {
            try {
                j = Long.parseLong(c);
            } catch (NumberFormatException e) {
            }
        }
        return j;
    }

    public final int m830b(String str) {
        int i = -1;
        String c = m828c(str);
        if (c != null) {
            try {
                i = Integer.parseInt(c);
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    public final String toString() {
        return this.f837a.toString();
    }
}
