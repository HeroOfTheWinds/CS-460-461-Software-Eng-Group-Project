package crittercism.android;

import crittercism.android.ds.C1123a;

public final class dw {
    private ds f751a;
    private du f752b;

    public final du m770a() {
        du duVar;
        synchronized (this) {
            duVar = this.f752b;
        }
        return duVar;
    }

    public final void m771a(ax axVar) {
        synchronized (this) {
            this.f751a = C1123a.m756a(axVar);
            if (!this.f751a.m757a()) {
                int b = axVar.m388b(cq.SESSION_ID_SETTING.m693a(), cq.SESSION_ID_SETTING.m694b());
                if (b == 0) {
                    b = axVar.m388b(cq.OLD_SESSION_ID_SETTING.m693a(), cq.OLD_SESSION_ID_SETTING.m694b());
                }
                du duVar = new du(b);
                duVar.f749a++;
                this.f752b = duVar;
            }
        }
    }

    public final boolean m772b() {
        boolean z;
        synchronized (this) {
            z = true;
            if (this.f751a != null) {
                z = this.f751a.m757a();
            }
        }
        return z;
    }
}
