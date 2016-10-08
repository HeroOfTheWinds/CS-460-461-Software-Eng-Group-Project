package crittercism.android;

import crittercism.android.bs.C1079a;
import crittercism.android.bz.C1106a;
import crittercism.android.ca.C1109a;

public enum br {
    APP_LOADS("app_loads_2", 10, Integer.MAX_VALUE, new C1079a(0), new C1109a(), null),
    HAND_EXCS("exceptions", 5, 50, new C1079a(0), new C1109a(), "exceptions"),
    INTERNAL_EXCS("internal_excs", 3, 3, new C1079a(0), new C1109a(), "exceptions"),
    NDK_CRASHES("ndk_crashes", 5, Integer.MAX_VALUE, new C1079a(0), new C1109a(), "crashes"),
    SDK_CRASHES("sdk_crashes", 5, Integer.MAX_VALUE, new C1079a(0), new C1109a(), "crashes"),
    CURR_BCS("current_bcs", 50, Integer.MAX_VALUE, new C1079a(1), new C1106a(), null),
    NW_BCS("network_bcs", 10, Integer.MAX_VALUE, new C1079a(0), new C1106a(), null),
    PREV_BCS("previous_bcs", 50, Integer.MAX_VALUE, new C1079a(0), new C1106a(), null),
    STARTED_TXNS("started_txns", 50, Integer.MAX_VALUE, new C1079a(0), new C1106a(), null),
    FINISHED_TXNS("finished_txns", Integer.MAX_VALUE, Integer.MAX_VALUE, new C1079a(0), new C1106a(), null),
    SYSTEM_BCS("system_bcs", 100, Integer.MAX_VALUE, new C1079a(0), new C1106a(), null);
    
    private String f513l;
    private int f514m;
    private int f515n;
    private C1079a f516o;
    private cj f517p;
    private String f518q;

    private br(String str, int i, int i2, C1079a c1079a, cj cjVar, String str2) {
        this.f513l = str;
        this.f514m = i;
        this.f515n = i2;
        this.f516o = c1079a;
        this.f517p = cjVar;
        this.f518q = str2;
    }

    public final String m560a() {
        return this.f513l;
    }

    public final int m561b() {
        return this.f514m;
    }

    public final C1079a m562c() {
        return this.f516o;
    }

    public final cj m563d() {
        return this.f517p;
    }

    public final int m564e() {
        return this.f515n;
    }

    public final String m565f() {
        return this.f518q;
    }
}
