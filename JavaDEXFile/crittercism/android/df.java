package crittercism.android;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public final class df {
    private Context f705a;
    private List f706b;

    public df(Context context) {
        this.f705a = context;
        this.f706b = new ArrayList();
    }

    public final void m724a() {
        ArrayList arrayList = new ArrayList();
        for (di thread : this.f706b) {
            arrayList.add(new Thread(thread));
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((Thread) it.next()).start();
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            ((Thread) it2.next()).join();
        }
    }

    public final void m725a(bs bsVar, cz czVar, String str, String str2, String str3, au auVar, cx cxVar) {
        synchronized (this) {
            if (bsVar.m578b() > 0) {
                bs a = bsVar.m573a(this.f705a);
                cy a2 = czVar.m705a(a, bsVar, str3, this.f705a, auVar);
                this.f706b.add(new dh(a, bsVar, auVar, new db(str, str2).m720a(), a2, cxVar));
            }
        }
    }

    public final void m726a(dg dgVar, ExecutorService executorService) {
        for (Runnable runnable : this.f706b) {
            if (!dgVar.m733a(runnable)) {
                executorService.execute(runnable);
            }
        }
    }
}
