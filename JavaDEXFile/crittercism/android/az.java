package crittercism.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.ConditionVariable;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.os.Process;
import com.crittercism.app.CritterRateMyAppButtons;
import com.crittercism.app.CrittercismConfig;
import com.crittercism.app.Transaction;
import com.crittercism.integrations.PluginException;
import com.mopub.volley.toolbox.HttpClientStack.HttpPatch;
import crittercism.android.C1108c.C1107a;
import crittercism.android.bx.C1085f;
import crittercism.android.bx.C1094o;
import crittercism.android.bx.C1095p;
import crittercism.android.cs.C1113b;
import crittercism.android.ct.C1114a;
import crittercism.android.cu.C1115a;
import crittercism.android.da.C1117a;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class az implements au, aw, ax, C1065f {
    static az f350a;
    public dt f351A;
    int f352B;
    public boolean f353C;
    private String f354D;
    private bs f355E;
    private bs f356F;
    private C1129g f357G;
    private at f358H;
    private boolean f359I;
    private String f360J;
    public boolean f361b;
    public Context f362c;
    public final ConditionVariable f363d;
    public final ConditionVariable f364e;
    public dw f365f;
    bs f366g;
    bs f367h;
    bs f368i;
    bs f369j;
    bs f370k;
    bs f371l;
    bs f372m;
    bs f373n;
    bs f374o;
    cv f375p;
    public dg f376q;
    ExecutorService f377r;
    public ExecutorService f378s;
    public boolean f379t;
    public bb f380u;
    protected C1126e f381v;
    public dr f382w;
    dv f383x;
    public bi f384y;
    public Map f385z;

    /* renamed from: crittercism.android.az.10 */
    final class AnonymousClass10 implements OnClickListener {
        final /* synthetic */ String f326a;
        final /* synthetic */ az f327b;

        AnonymousClass10(az azVar, String str) {
            this.f327b = azVar;
            this.f326a = str;
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            try {
                az.m400A().m416a(this.f326a);
            } catch (Exception e) {
                dx.m782c("YES button failed.  Email support@crittercism.com.");
            }
        }
    }

    /* renamed from: crittercism.android.az.1 */
    public final class C10551 extends di {
        final /* synthetic */ az f330a;

        public C10551(az azVar) {
            this.f330a = azVar;
        }

        public final void m391a() {
            if (!this.f330a.f365f.m772b()) {
                ch chVar = this.f330a.f376q.f708b;
                if (chVar != null) {
                    this.f330a.f366g.m577a(chVar);
                }
                df dfVar = new df(this.f330a.f362c);
                dfVar.m725a(this.f330a.f366g, new C1114a(), this.f330a.f380u.m470e(), "/v0/appload", this.f330a.f380u.m467b(), az.f350a, new C1113b());
                dfVar.m725a(this.f330a.f367h, new C1117a(), this.f330a.f380u.m467b(), "/android_v2/handle_exceptions", null, az.f350a, new C1115a());
                dfVar.m725a(this.f330a.f368i, new C1117a(), this.f330a.f380u.m467b(), "/android_v2/handle_ndk_crashes", null, az.f350a, new C1115a());
                dfVar.m725a(this.f330a.f369j, new C1117a(), this.f330a.f380u.m467b(), "/android_v2/handle_crashes", null, az.f350a, new C1115a());
                dfVar.m726a(this.f330a.f376q, this.f330a.f377r);
            }
        }
    }

    /* renamed from: crittercism.android.az.2 */
    final class C10562 extends di {
        final /* synthetic */ az f331a;
        final /* synthetic */ JSONObject f332b;
        final /* synthetic */ az f333c;

        C10562(az azVar, az azVar2, JSONObject jSONObject) {
            this.f333c = azVar;
            this.f331a = azVar2;
            this.f332b = jSONObject;
        }

        public final void m392a() {
            if (!this.f331a.f365f.m772b()) {
                this.f331a.f383x.m767a(this.f332b);
                if (this.f331a.f383x.m769b()) {
                    this.f331a.m407E();
                }
            }
        }
    }

    /* renamed from: crittercism.android.az.3 */
    final class C10573 extends di {
        final /* synthetic */ az f334a;
        final /* synthetic */ az f335b;

        C10573(az azVar, az azVar2) {
            this.f335b = azVar;
            this.f334a = azVar2;
        }

        public final void m393a() {
            if (!this.f334a.f365f.m772b()) {
                cw cuVar = new cu(this.f334a);
                cuVar.f688a.put("metadata", this.f334a.f383x.m766a());
                new dj(cuVar, new dc(new db(this.f335b.f380u.m467b(), "/android_v2/update_user_metadata").m720a()), new dd(this.f334a.f383x)).run();
            }
        }
    }

    /* renamed from: crittercism.android.az.4 */
    public static final /* synthetic */ class C10584 {
        public static final /* synthetic */ int[] f336a;

        static {
            f336a = new int[CritterRateMyAppButtons.values().length];
            try {
                f336a[CritterRateMyAppButtons.YES.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f336a[CritterRateMyAppButtons.NO.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f336a[CritterRateMyAppButtons.LATER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* renamed from: crittercism.android.az.5 */
    final class C10595 extends di {
        final /* synthetic */ Throwable f337a;
        final /* synthetic */ long f338b;
        final /* synthetic */ az f339c;

        C10595(az azVar, Throwable th, long j) {
            this.f339c = azVar;
            this.f337a = th;
            this.f338b = j;
        }

        public final void m394a() {
            if (!this.f339c.f365f.m772b()) {
                synchronized (this.f339c.f375p) {
                    if (this.f339c.f352B < 10) {
                        bk bkVar = new bk(this.f337a, this.f338b);
                        bkVar.m546a("current_session", this.f339c.f370k);
                        bkVar.m544a(this.f339c.f371l);
                        bkVar.f474f = "he";
                        if (this.f339c.f375p.m715a()) {
                            new dj(new cu(az.f350a).m712a(br.HAND_EXCS.m565f(), new JSONArray().put(bkVar.m548b())), new dc(new db(this.f339c.f380u.m467b(), "/android_v2/handle_exceptions").m720a()), null).run();
                            az azVar = this.f339c;
                            azVar.f352B++;
                            this.f339c.f375p.m716b();
                        }
                    }
                }
            }
        }
    }

    /* renamed from: crittercism.android.az.6 */
    final class C10606 extends di {
        final /* synthetic */ Throwable f340a;
        final /* synthetic */ long f341b;
        final /* synthetic */ az f342c;

        C10606(az azVar, Throwable th, long j) {
            this.f342c = azVar;
            this.f340a = th;
            this.f341b = j;
        }

        public final void m395a() {
            if (!this.f342c.f365f.m772b()) {
                ch bkVar = new bk(this.f340a, this.f341b);
                bkVar.m546a("current_session", this.f342c.f370k);
                bkVar.f474f = "he";
                if (this.f342c.f367h.m577a(bkVar)) {
                    az.f350a.m414a(new by(bkVar.f471c, bkVar.f472d));
                    if (this.f342c.f375p.m715a()) {
                        df dfVar = new df(this.f342c.f362c);
                        dfVar.m725a(this.f342c.f367h, new C1117a(), this.f342c.f380u.m467b(), "/android_v2/handle_exceptions", null, az.f350a, new C1115a());
                        dfVar.m726a(this.f342c.f376q, this.f342c.f377r);
                        this.f342c.f375p.m716b();
                    }
                }
            }
        }
    }

    /* renamed from: crittercism.android.az.7 */
    public final class C10617 extends di {
        final /* synthetic */ cf f343a;
        final /* synthetic */ az f344b;

        public C10617(az azVar, cf cfVar) {
            this.f344b = azVar;
            this.f343a = cfVar;
        }

        public final void m396a() {
            this.f344b.f370k.m577a(this.f343a);
        }
    }

    /* renamed from: crittercism.android.az.8 */
    final class C10628 extends di {
        final /* synthetic */ C1108c f345a;
        final /* synthetic */ az f346b;

        C10628(az azVar, C1108c c1108c) {
            this.f346b = azVar;
            this.f345a = c1108c;
        }

        public final void m397a() {
            this.f346b.f371l.m577a(this.f345a);
        }
    }

    /* renamed from: crittercism.android.az.9 */
    final class C10639 extends di {
        final /* synthetic */ ci f347a;
        final /* synthetic */ az f348b;

        C10639(az azVar, ci ciVar) {
            this.f348b = azVar;
            this.f347a = ciVar;
        }

        public final void m398a() {
            this.f348b.f372m.m577a(this.f347a);
        }
    }

    /* renamed from: crittercism.android.az.a */
    static final class C1064a implements IdleHandler {
        private boolean f349a;

        private C1064a() {
            this.f349a = false;
        }

        public final boolean queueIdle() {
            synchronized (this) {
                if (!this.f349a) {
                    this.f349a = true;
                    bg.m504g();
                }
            }
            return true;
        }
    }

    protected az() {
        this.f361b = false;
        this.f362c = null;
        this.f354D = null;
        this.f363d = new ConditionVariable(false);
        this.f364e = new ConditionVariable(false);
        this.f365f = new dw();
        this.f375p = null;
        this.f376q = null;
        this.f357G = null;
        this.f377r = Executors.newCachedThreadPool(new dz());
        this.f378s = Executors.newSingleThreadExecutor(new dz());
        this.f359I = false;
        this.f379t = false;
        this.f360J = BuildConfig.FLAVOR;
        this.f383x = null;
        this.f385z = new HashMap();
        this.f351A = null;
        this.f352B = 0;
        this.f353C = false;
        this.f381v = new C1126e(this.f378s);
    }

    public static az m400A() {
        if (f350a == null) {
            f350a = new az();
        }
        return f350a;
    }

    private static boolean m401F() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (stackTraceElement.getMethodName().equals("onCreate") || stackTraceElement.getMethodName().equals("onResume")) {
                return true;
            }
        }
        return false;
    }

    private void m402G() {
        int myUid = Process.myUid();
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) this.f362c.getSystemService("activity");
        int i = 0;
        for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            i = runningAppProcessInfo.uid == myUid ? i + 1 : i;
        }
        if (i <= 1) {
            this.f379t = false;
            return;
        }
        for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.pid == myPid) {
                this.f379t = true;
                return;
            }
        }
    }

    private String m403H() {
        try {
            if (this.f360J == null || this.f360J.equals(BuildConfig.FLAVOR)) {
                this.f360J = this.f362c.getPackageName();
            }
        } catch (Exception e) {
            dx.m782c("Call to getPackageName() failed.  Please contact us at support@crittercism.com.");
            this.f360J = new String();
        }
        return this.f360J;
    }

    public final boolean m404B() {
        this.f363d.block();
        return this.f365f.m772b();
    }

    public final void m405C() {
        dt dtVar = this.f351A;
        if (this.f351A != null) {
            this.f351A.m762d();
        }
    }

    public final String m406D() {
        PackageManager packageManager = this.f362c.getPackageManager();
        String H = m403H();
        if (H == null || H.length() <= 0) {
            return null;
        }
        dn a = dp.m750a(packageManager.getInstallerPackageName(H));
        if (a != null) {
            return a.m745a(H).m744a();
        }
        dx.m782c("Could not find app market for this app.  Will try rate-my-app test target in config.");
        return this.f380u.getRateMyAppTestTarget();
    }

    public final void m407E() {
        if (!this.f379t) {
            Runnable c10573 = new C10573(this, this);
            if (!this.f376q.m733a(c10573)) {
                this.f377r.execute(c10573);
            }
        }
    }

    public final AlertDialog m408a(Context context, String str, String str2) {
        AlertDialog alertDialog = null;
        Object obj = null;
        if (this.f365f.m772b()) {
            dx.m779b("User has opted out of crittercism.  generateRateMyAppAlertDialog returning null.");
        } else if (!(context instanceof Activity)) {
            dx.m779b("Context object must be an instance of Activity for AlertDialog to form correctly.  generateRateMyAppAlertDialog returning null.");
        } else if (str2 == null || (str2 != null && str2.length() == 0)) {
            dx.m779b("Message has to be a non-empty string.  generateRateMyAppAlertDialog returning null.");
        } else if (VERSION.SDK_INT < 5) {
            dx.m779b("Rate my app not supported below api level 5");
        } else {
            obj = 1;
        }
        if (obj != null) {
            String D = m406D();
            if (D == null) {
                dx.m779b("Cannot create proper URI to open app market.  Returning null.");
            } else {
                Builder builder = new Builder(context);
                builder.setTitle(str).setMessage(str2);
                try {
                    alertDialog = builder.create();
                    alertDialog.setButton(-1, "Yes", new AnonymousClass10(this, D));
                    alertDialog.setButton(-2, "No", new OnClickListener() {
                        final /* synthetic */ az f328a;

                        {
                            this.f328a = r1;
                        }

                        public final void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                az.m400A().m405C();
                            } catch (Exception e) {
                                dx.m782c("NO button failed.  Email support@crittercism.com.");
                            }
                        }
                    });
                    alertDialog.setButton(-3, "Maybe Later", new OnClickListener() {
                        final /* synthetic */ az f329a;

                        {
                            this.f329a = r1;
                        }

                        public final void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                az.m400A();
                            } catch (Exception e) {
                                dx.m782c("MAYBE LATER button failed.  Email support@crittercism.com.");
                            }
                        }
                    });
                } catch (Exception e) {
                    dx.m779b("Failed to create AlertDialog instance from AlertDialog.Builder.  Did you remember to call Looper.prepare() before calling Crittercism.generateRateMyAppAlertDialog()?");
                }
            }
        }
        return alertDialog;
    }

    public final String m409a() {
        String str = this.f354D;
        return str == null ? BuildConfig.FLAVOR : str;
    }

    public final String m410a(String str, String str2) {
        SharedPreferences sharedPreferences = this.f362c.getSharedPreferences(str, 0);
        return sharedPreferences != null ? sharedPreferences.getString(str2, null) : null;
    }

    public final void m411a(Context context, String str, CrittercismConfig crittercismConfig) {
        dx.m775a("Initializing Crittercism 5.0.8 for App ID " + str);
        bn bnVar = new bn(str);
        this.f354D = str;
        this.f380u = new bb(bnVar, crittercismConfig);
        this.f362c = context;
        this.f358H = new at(this.f362c, this.f380u);
        this.f360J = context.getPackageName();
        this.f382w = new dr(context);
        m402G();
        long j = 60000000000L;
        if (this.f379t) {
            j = 12000000000L;
        }
        this.f375p = new cv(j);
        if (!m401F()) {
            dx.m782c("Crittercism should be initialized in onCreate() of MainActivity");
        }
        bx.m644a(this.f358H);
        bx.m643a(this.f362c);
        bx.m646a(new cc());
        bx.m645a(new bf(this.f362c, this.f380u));
        try {
            this.f381v.m788a(this.f380u.m466a());
            this.f381v.m789b(this.f380u.getPreserveQueryStringPatterns());
            this.f357G = new C1129g(this, new URL(this.f380u.m468c() + "/api/apm/network"));
            this.f381v.m787a(this.f357G);
            this.f381v.m787a((C1065f) this);
            new dy(this.f357G, "OPTMZ").start();
            if (!C1130h.m805a(this.f362c).exists() && this.f380u.isServiceMonitoringEnabled()) {
                this.f359I = new C1133i(this.f381v, new C1116d(this.f362c)).m816a();
                new StringBuilder("installedApm = ").append(this.f359I);
                dx.m778b();
            }
        } catch (Exception e) {
            new StringBuilder("Exception in startApm: ").append(e.getClass().getName());
            dx.m778b();
            dx.m781c();
        }
        this.f376q = new dg(this.f380u, context, this, this, this);
        if (!this.f379t) {
            dx.m774a(new ec(this, this.f378s, this.f376q, this.f365f));
        }
        UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(defaultUncaughtExceptionHandler instanceof ay)) {
            Thread.setDefaultUncaughtExceptionHandler(new ay(this, defaultUncaughtExceptionHandler));
        }
        if (VERSION.SDK_INT < 14) {
            dx.m775a("API Level is less than 14. Automatic breadcrumbs are not supported.");
        } else if (this.f362c instanceof Application) {
            dx.m778b();
            ((Application) this.f362c).registerActivityLifecycleCallbacks(new av(this.f362c, this));
        } else {
            dx.m782c("Application context not provided. Automatic breadcrumbs will not be recorded.");
        }
        if (!this.f379t) {
            bg.m499b(this);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Looper.myQueue().addIdleHandler(new C1064a());
            }
        }
        new dy(this.f376q).start();
        this.f361b = true;
    }

    public final void m412a(bh bhVar) {
        bi biVar = this.f384y;
        if (this.f384y != null) {
            bg.m497a(bhVar);
            bg.m505i();
            if (bhVar.f446a) {
                this.f384y.m532a(bhVar.f447b, TimeUnit.SECONDS);
                this.f384y.m533b();
            }
        }
    }

    public final void m413a(C1108c c1108c) {
        Runnable c10628 = new C10628(this, c1108c);
        if (!this.f376q.m733a(c10628)) {
            this.f378s.execute(c10628);
        }
    }

    public final void m414a(ci ciVar) {
        if (!this.f365f.m772b()) {
            Runnable c10639 = new C10639(this, ciVar);
            if (!this.f376q.m733a(c10639)) {
                this.f378s.execute(c10639);
            }
        }
    }

    public final void m415a(C1130h c1130h) {
        if (this.f357G != null && c1130h.f797a && !c1130h.f799c) {
            dx.m775a("Enabling OPTMZ");
            this.f357G.m803a(c1130h.f800d, TimeUnit.SECONDS);
            this.f357G.m802a();
        }
    }

    public final void m416a(String str) {
        dt dtVar = this.f351A;
        if (this.f351A != null) {
            this.f351A.m762d();
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(Uri.parse(str));
        this.f362c.startActivity(intent);
    }

    public final void m417a(String str, String str2, int i) {
        SharedPreferences sharedPreferences = this.f362c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            Editor edit = sharedPreferences.edit();
            if (edit != null) {
                edit.remove(str2);
                edit.putInt(str2, i);
                edit.commit();
            }
        }
    }

    public final void m418a(String str, String str2, String str3) {
        SharedPreferences sharedPreferences = this.f362c.getSharedPreferences(str, 0);
        if (sharedPreferences != null) {
            Editor edit = sharedPreferences.edit();
            if (edit != null) {
                edit.remove(str2);
                edit.putString(str2, str3);
                edit.commit();
            }
        }
    }

    public final void m419a(String str, URL url, long j, long j2, long j3, int i, Exception exception, long j4) {
        if (str == null) {
            dx.m779b("Null HTTP request method provided. Endpoint will not be logged.");
            return;
        }
        String toUpperCase = str.toUpperCase(Locale.US);
        Set hashSet = new HashSet();
        hashSet.add("GET");
        hashSet.add("POST");
        hashSet.add("HEAD");
        hashSet.add("PUT");
        hashSet.add("DELETE");
        hashSet.add("TRACE");
        hashSet.add("OPTIONS");
        hashSet.add("CONNECT");
        hashSet.add(HttpPatch.METHOD_NAME);
        if (!hashSet.contains(toUpperCase)) {
            dx.m782c("Logging endpoint with invalid HTTP request method: " + str);
        }
        if (url == null) {
            dx.m779b("Null URL provided. Endpoint will not be logged");
        } else if (j2 < 0 || j3 < 0) {
            dx.m779b("Invalid byte values. Bytes need to be non-negative. Endpoint will not be logged.");
        } else {
            if (i != 0) {
                if (i < 100 || i >= 600) {
                    dx.m782c("Logging endpoint with invalid HTTP response code: " + Integer.toString(i));
                }
            } else if (exception == null) {
                dx.m782c("Logging endpoint with null error and response code of 0.");
            }
            C1066b a = new C1116d(this.f362c).m717a();
            if (j < 0) {
                dx.m779b("Invalid latency. Endpoint will not be logged.");
            } else if (j4 < 0) {
                dx.m779b("Invalid start time. Endpoint will not be logged.");
            } else {
                C1108c c1108c = new C1108c();
                c1108c.f577f = toUpperCase;
                c1108c.m662a(url.toExternalForm());
                c1108c.m666b(j2);
                c1108c.m671d(j3);
                c1108c.f576e = i;
                c1108c.f581j = a;
                c1108c.m673e(j4);
                c1108c.m675f(j4 + j);
                if (bc.m475b()) {
                    c1108c.m659a(bc.m473a());
                }
                c1108c.m663a((Throwable) exception);
                this.f381v.m786a(c1108c, C1107a.LOG_ENDPOINT);
            }
        }
    }

    public final void m420a(Throwable th) {
        if (this.f376q == null) {
            dx.m779b("Unable to handle application crash. Crittercism not yet initialized");
            return;
        }
        this.f376q.m734b();
        dq.m752a(this.f362c, true);
        if (!this.f365f.m772b()) {
            if (this.f379t) {
                new dj(new cu(this).m712a(br.SDK_CRASHES.m565f(), new JSONArray().put(new bk(th, Thread.currentThread().getId()).m548b())), new dc(new db(this.f380u.m467b(), "/android_v2/handle_crashes").m720a()), null).run();
                return;
            }
            List a = bg.m492a(this, th instanceof PluginException);
            ch bkVar = new bk(th, Thread.currentThread().getId());
            bkVar.m546a("crashed_session", this.f370k);
            if (this.f356F.m578b() > 0) {
                bkVar.m546a("previous_session", this.f356F);
            }
            bkVar.m544a(this.f371l);
            bkVar.f470b = new bo(this.f372m).f499a;
            bkVar.m543a();
            bkVar.m547a(a);
            this.f369j.m577a(bkVar);
            df dfVar = new df(this.f362c);
            dfVar.m725a(this.f366g, new C1117a(), this.f380u.m470e(), "/v0/appload", null, this, new C1113b());
            dfVar.m725a(this.f367h, new C1117a(), this.f380u.m467b(), "/android_v2/handle_exceptions", null, this, new C1115a());
            dfVar.m725a(this.f368i, new C1117a(), this.f380u.m467b(), "/android_v2/handle_ndk_crashes", null, this, new C1115a());
            dfVar.m725a(this.f369j, new C1117a(), this.f380u.m467b(), "/android_v2/handle_crashes", null, this, new C1115a());
            try {
                dfVar.m724a();
            } catch (InterruptedException e) {
                new StringBuilder("InterruptedException in logCrashException: ").append(e.getMessage());
                dx.m778b();
                dx.m781c();
            } catch (Throwable th2) {
                new StringBuilder("Unexpected throwable in logCrashException: ").append(th2.getMessage());
                dx.m778b();
                dx.m781c();
            }
        }
    }

    public final void m421a(JSONObject jSONObject) {
        if (!this.f379t) {
            Runnable c10562 = new C10562(this, this, jSONObject);
            if (!this.f376q.m733a(c10562)) {
                this.f378s.execute(c10562);
            }
        }
    }

    public final int m422b(String str) {
        if (this.f379t) {
            dx.m782c("Transactions are not supported for services. Returning default value of -1 for " + str + ".");
            return -1;
        }
        int d;
        synchronized (this.f385z) {
            Transaction transaction = (Transaction) this.f385z.get(str);
            d = transaction != null ? transaction.m39d() : -1;
        }
        return d;
    }

    public final int m423b(String str, String str2) {
        SharedPreferences sharedPreferences = this.f362c.getSharedPreferences(str, 0);
        return sharedPreferences != null ? sharedPreferences.getInt(str2, 0) : 0;
    }

    public final String m424b() {
        return this.f358H.f315a;
    }

    public final void m425b(Throwable th) {
        synchronized (this) {
            if (th == null) {
                dx.m782c("Calling logHandledException with a null java.lang.Throwable. Nothing will be reported to Crittercism");
            } else if (this.f379t) {
                r0 = new C10595(this, th, Thread.currentThread().getId());
                if (!this.f376q.m733a(r0)) {
                    this.f378s.execute(r0);
                }
            } else {
                r0 = new C10606(this, th, Thread.currentThread().getId());
                if (!this.f376q.m733a(r0)) {
                    this.f378s.execute(r0);
                }
            }
        }
    }

    public final String m426c() {
        return this.f382w != null ? this.f382w.m755a() : BuildConfig.FLAVOR;
    }

    public final boolean m427c(String str, String str2) {
        SharedPreferences sharedPreferences = this.f362c.getSharedPreferences(str, 0);
        return sharedPreferences != null ? sharedPreferences.getBoolean(str2, false) : false;
    }

    public final String m428d() {
        return CrittercismConfig.API_VERSION;
    }

    public final int m429e() {
        return this.f365f != null ? Integer.valueOf(this.f365f.m770a().f749a).intValue() : -1;
    }

    public final String m430f() {
        return new C1085f().f535a;
    }

    public final int m431g() {
        return new C1094o().f544a.intValue();
    }

    public final int m432h() {
        return new C1095p().f545a.intValue();
    }

    public final String m433i() {
        return "Android";
    }

    public final String m434j() {
        return Build.MODEL;
    }

    public final String m435k() {
        return VERSION.RELEASE;
    }

    public final dw m436l() {
        return this.f365f;
    }

    public final dt m437m() {
        return this.f351A;
    }

    public final bs m438n() {
        return this.f366g;
    }

    public final bs m439o() {
        return this.f367h;
    }

    public final bs m440p() {
        return this.f355E;
    }

    public final bs m441q() {
        return this.f368i;
    }

    public final bs m442r() {
        return this.f369j;
    }

    public final bs m443s() {
        return this.f370k;
    }

    public final bs m444t() {
        return this.f371l;
    }

    public final bs m445u() {
        return this.f356F;
    }

    public final bs m446v() {
        return this.f372m;
    }

    public final bs m447w() {
        return this.f373n;
    }

    public final bs m448x() {
        return this.f374o;
    }

    public final dv m449y() {
        return this.f383x;
    }

    public final void m450z() {
        if (this.f379t) {
            this.f370k = new bs(this.f362c, br.CURR_BCS).m573a(this.f362c);
        } else {
            this.f370k = new bs(this.f362c, br.CURR_BCS);
        }
        this.f356F = new bs(this.f362c, br.PREV_BCS);
        this.f371l = new bs(this.f362c, br.NW_BCS);
        this.f372m = new bs(this.f362c, br.SYSTEM_BCS);
        this.f366g = new bs(this.f362c, br.APP_LOADS);
        this.f367h = new bs(this.f362c, br.HAND_EXCS);
        this.f355E = new bs(this.f362c, br.INTERNAL_EXCS);
        this.f368i = new bs(this.f362c, br.NDK_CRASHES);
        this.f369j = new bs(this.f362c, br.SDK_CRASHES);
        this.f373n = new bs(this.f362c, br.STARTED_TXNS);
        this.f374o = new bs(this.f362c, br.FINISHED_TXNS);
        if (!this.f379t) {
            this.f383x = new dv(this.f362c, this.f354D);
        }
    }
}
