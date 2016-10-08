package crittercism.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.ConditionVariable;
import com.crittercism.app.CrittercismNDK;
import crittercism.android.cs.C1113b;
import crittercism.android.ct.C1114a;
import crittercism.android.cu.C1115a;
import crittercism.android.da.C1117a;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class dg extends di {
    public ConditionVariable f707a;
    public bm f708b;
    private ConditionVariable f709c;
    private bb f710d;
    private Context f711e;
    private aw f712f;
    private ax f713g;
    private au f714h;
    private List f715i;
    private boolean f716j;
    private boolean f717k;
    private Exception f718l;

    public dg(bb bbVar, Context context, aw awVar, ax axVar, au auVar) {
        this.f709c = new ConditionVariable();
        this.f707a = new ConditionVariable();
        this.f715i = new ArrayList();
        this.f716j = false;
        this.f708b = null;
        this.f718l = null;
        this.f710d = bbVar;
        this.f711e = context;
        this.f712f = awVar;
        this.f713g = axVar;
        this.f714h = auVar;
        this.f717k = false;
    }

    private void m727a(File file) {
        boolean z = this.f717k;
        az A = az.m400A();
        if (!A.f379t) {
            if (file != null && file.exists()) {
                file.isFile();
            }
            aw awVar = this.f712f;
            bs s = this.f712f.m377s();
            bs t = this.f712f.m378t();
            bs u = this.f712f.m379u();
            bs v = this.f712f.m380v();
            bs q = this.f712f.m375q();
            if (file != null) {
                dq.f742a = true;
                A.f364e.open();
                q.m577a(new cd(file, s, u, t, v));
                file.delete();
                this.f712f.m381w().m574a();
            } else {
                A.f364e.open();
                bg.m494a(this.f712f);
            }
            u.m574a();
            t.m574a();
            v.m574a();
            s.m575a(u);
        }
    }

    private void m728c() {
        synchronized (this) {
            this.f716j = true;
        }
    }

    private boolean m729d() {
        boolean z;
        synchronized (this) {
            z = this.f716j;
        }
        return z;
    }

    private File m730e() {
        int i = 0;
        File file = new File(this.f711e.getFilesDir().getAbsolutePath() + "/" + this.f710d.m472g());
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                if (listFiles.length == 1) {
                    File file2 = listFiles[0];
                    file2.isFile();
                    if (file2.isFile()) {
                        return file2;
                    }
                } else if (listFiles.length > 1) {
                    int length = listFiles.length;
                    while (i < length) {
                        File file3 = listFiles[i];
                        file3.isFile();
                        file3.delete();
                        i++;
                    }
                }
            }
        }
        return null;
    }

    private void m731f() {
        if (!az.m400A().f379t) {
            boolean z = this.f717k;
            bs n = this.f712f.m372n();
            bs o = this.f712f.m373o();
            bs p = this.f712f.m374p();
            bs q = this.f712f.m375q();
            bs r = this.f712f.m376r();
            dv y = this.f712f.m383y();
            this.f710d.m467b();
            this.f708b = new bm(this.f714h);
            if (!this.f710d.delaySendingAppLoad()) {
                n.m577a(this.f708b);
                df dfVar = new df(this.f711e);
                dfVar.m725a(n, new C1114a(), this.f710d.m470e(), "/v0/appload", this.f710d.m467b(), this.f714h, new C1113b());
                dfVar.m725a(o, new C1117a(), this.f710d.m467b(), "/android_v2/handle_exceptions", null, this.f714h, new C1115a());
                dfVar.m725a(q, new C1117a(), this.f710d.m467b(), "/android_v2/handle_ndk_crashes", null, this.f714h, new C1115a());
                dfVar.m725a(r, new C1117a(), this.f710d.m467b(), "/android_v2/handle_crashes", null, this.f714h, new C1115a());
                dfVar.m725a(p, new C1117a(), this.f710d.m467b(), "/android_v2/handle_exceptions", null, new ba(this.f714h, this.f710d), new C1115a());
                dfVar.m724a();
            }
            if (y.m769b()) {
                az.m400A().m407E();
            }
        }
    }

    public final void m732a() {
        dx.m778b();
        File file = new File(this.f711e.getFilesDir().getAbsolutePath() + "/com.crittercism/pending");
        if (!file.exists() || file.isDirectory()) {
            try {
                eb.m790a(file);
            } catch (Exception e) {
                new StringBuilder("Exception in run(): ").append(e.getMessage());
                dx.m778b();
                dx.m781c();
                this.f718l = e;
            } finally {
                this.f709c.open();
            }
        } else {
            dx.m778b();
        }
        az A = az.m400A();
        A.f382w.m755a();
        dw l = this.f714h.m370l();
        A.f363d.open();
        ax axVar = this.f713g;
        Context context = this.f711e;
        l.m771a(axVar);
        dq.f742a = dq.m751a(this.f711e).booleanValue();
        dq.m752a(this.f711e, false);
        if (!l.m772b()) {
            dt dtVar = new dt(this.f711e);
            dtVar.f748a.edit().putInt("numAppLoads", dtVar.m758a() + 1).commit();
            az.m400A().f351A = dtVar;
            l.m770a().m763a(this.f713g, cq.SESSION_ID_SETTING.m693a(), cq.SESSION_ID_SETTING.m694b());
        }
        this.f717k = l.m772b();
        File e2 = m730e();
        boolean z;
        if (this.f717k) {
            z = this.f717k;
            if (!az.m400A().f379t) {
                if (e2 != null && e2.exists()) {
                    e2.isFile();
                }
                new bs(this.f711e, br.APP_LOADS).m574a();
                new bs(this.f711e, br.HAND_EXCS).m574a();
                new bs(this.f711e, br.INTERNAL_EXCS).m574a();
                new bs(this.f711e, br.NDK_CRASHES).m574a();
                new bs(this.f711e, br.SDK_CRASHES).m574a();
                new bs(this.f711e, br.CURR_BCS).m574a();
                new bs(this.f711e, br.PREV_BCS).m574a();
                new bs(this.f711e, br.NW_BCS).m574a();
                new bs(this.f711e, br.SYSTEM_BCS).m574a();
                if (e2 != null) {
                    e2.delete();
                }
            }
            C1130h.m806b(this.f711e);
        } else {
            Context context2 = this.f711e;
            C1130h c1130h = new C1130h(context2);
            SharedPreferences sharedPreferences = context2.getSharedPreferences("com.crittercism.optmz.config", 0);
            if (sharedPreferences.contains("interval")) {
                c1130h.f800d = sharedPreferences.getInt("interval", 10);
                if (sharedPreferences.contains("kill")) {
                    c1130h.f799c = sharedPreferences.getBoolean("kill", false);
                    if (sharedPreferences.contains("persist")) {
                        c1130h.f798b = sharedPreferences.getBoolean("persist", false);
                        if (sharedPreferences.contains("enabled")) {
                            c1130h.f797a = sharedPreferences.getBoolean("enabled", false);
                        } else {
                            c1130h = null;
                        }
                    } else {
                        c1130h = null;
                    }
                } else {
                    c1130h = null;
                }
            } else {
                c1130h = null;
            }
            if (c1130h != null) {
                az.m400A().m415a(c1130h);
            }
            z = this.f717k;
            this.f712f.m384z();
            if (!az.m400A().f379t) {
                bh a = bh.m525a(this.f711e);
                try {
                    Runnable biVar = new bi(this.f711e, this.f714h, this.f712f.m382x(), this.f712f.m377s(), this.f712f.m378t(), this.f712f.m380v(), new URL(this.f710d.m469d() + "/api/v1/transactions"));
                    az A2 = az.m400A();
                    A2.f384y = biVar;
                    new dy(biVar, "TXN Thread").start();
                    A2.m412a(a);
                } catch (MalformedURLException e3) {
                    dx.m773a();
                }
            }
            m727a(e2);
            this.f707a.open();
            this.f712f.m377s().m577a(cf.f611a);
            if (!az.m400A().f379t && this.f710d.isNdkCrashReportingEnabled()) {
                dx.m778b();
                try {
                    CrittercismNDK.installNdkLib(this.f711e, this.f710d.m472g());
                } catch (Throwable th) {
                    new StringBuilder("Exception installing ndk library: ").append(th.getClass().getName());
                    dx.m778b();
                }
            }
            m731f();
        }
        m728c();
        for (Runnable biVar2 : this.f715i) {
            biVar2.run();
        }
    }

    public final boolean m733a(Runnable runnable) {
        boolean z;
        synchronized (this) {
            if (m729d()) {
                z = false;
            } else {
                this.f715i.add(runnable);
                z = true;
            }
        }
        return z;
    }

    public final void m734b() {
        this.f709c.block();
    }
}
