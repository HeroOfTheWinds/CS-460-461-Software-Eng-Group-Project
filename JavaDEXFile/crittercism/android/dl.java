package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys;
import java.util.HashMap;
import java.util.Map;

public final class dl extends di {
    public Map f732a;
    private dw f733b;
    private au f734c;
    private boolean f735d;
    private boolean f736e;
    private boolean f737f;
    private boolean f738g;

    public dl(au auVar) {
        this.f732a = new HashMap();
        this.f735d = false;
        this.f736e = false;
        this.f737f = false;
        this.f738g = false;
        this.f734c = auVar;
        this.f733b = auVar.m370l();
    }

    private void m738a(String str, Object obj) {
        synchronized (this) {
            this.f732a.put(str, obj);
        }
    }

    public final void m739a() {
        boolean z = false;
        boolean b = this.f733b.m772b();
        if (this.f735d) {
            m738a("optOutStatus", Boolean.valueOf(b));
        }
        if (!b) {
            if (this.f736e) {
                m738a("crashedOnLastLoad", Boolean.valueOf(dq.f742a));
            }
            if (this.f737f) {
                m738a("userUUID", this.f734c.m361c());
            }
            if (this.f738g) {
                dt dtVar = az.m400A().f351A;
                if (dtVar != null) {
                    if (dtVar.f748a.getBoolean("rateMyAppEnabled", false) && !dtVar.f748a.getBoolean("hasRatedApp", false)) {
                        int a = dtVar.m758a();
                        int i = dtVar.f748a.getInt("rateAfterNumLoads", 5);
                        if (a >= i && (a - i) % dtVar.f748a.getInt("remindAfterNumLoads", 5) == 0) {
                            z = true;
                        }
                    }
                    m738a("shouldShowRateAppAlert", Boolean.valueOf(z));
                    m738a(Keys.MESSAGE, dtVar.m760b());
                    m738a(Keys.TITLE, dtVar.m761c());
                }
            }
        }
    }

    public final void m740b() {
        this.f735d = true;
    }

    public final void m741c() {
        this.f736e = true;
    }

    public final void m742d() {
        this.f737f = true;
    }

    public final void m743e() {
        this.f738g = true;
    }
}
