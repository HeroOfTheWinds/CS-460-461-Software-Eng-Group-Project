package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.concurrent.ExecutorService;
import org.json.JSONException;

public final class ec {
    aw f779a;
    ExecutorService f780b;
    dg f781c;
    dw f782d;

    /* renamed from: crittercism.android.ec.1 */
    final class C11271 implements Runnable {
        final /* synthetic */ Throwable f776a;
        final /* synthetic */ long f777b;
        final /* synthetic */ ec f778c;

        C11271(ec ecVar, Throwable th, long j) {
            this.f778c = ecVar;
            this.f776a = th;
            this.f777b = j;
        }

        public final void run() {
            try {
                if (!this.f778c.f782d.m772b()) {
                    ch bkVar = new bk(this.f776a, this.f777b);
                    bkVar.f474f = "he";
                    try {
                        bkVar.f475g.put("app_version", CrittercismConfig.API_VERSION);
                    } catch (JSONException e) {
                    }
                    bkVar.f475g.remove("logcat");
                    this.f778c.f779a.m374p().m577a(bkVar);
                }
            } catch (ThreadDeath e2) {
            } catch (Throwable th) {
                ec ecVar = this.f778c;
                Throwable th2 = this.f776a;
            }
        }
    }

    public ec(aw awVar, ExecutorService executorService, dg dgVar, dw dwVar) {
        this.f779a = awVar;
        this.f780b = executorService;
        this.f781c = dgVar;
        this.f782d = dwVar;
    }
}
