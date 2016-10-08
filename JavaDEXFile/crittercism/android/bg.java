package crittercism.android;

import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import com.crittercism.app.Transaction;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bg extends Transaction implements ch {
    private static ExecutorService f427b;
    private static ScheduledExecutorService f428c;
    private static List f429o;
    private static volatile long f430p;
    private static volatile long f431q;
    private static final int[] f432r;
    private static bg f433s;
    private static bh f434t;
    private String f435d;
    private long f436e;
    private int f437f;
    private long f438g;
    private long f439h;
    private long f440i;
    private C1075a f441j;
    private Map f442k;
    private String f443l;
    private long f444m;
    private ScheduledFuture f445n;

    /* renamed from: crittercism.android.bg.1 */
    static final class C10671 extends di {
        final /* synthetic */ List f404a;
        final /* synthetic */ az f405b;

        C10671(List list, az azVar) {
            this.f404a = list;
            this.f405b = azVar;
        }

        public final void m482a() {
            for (bg bgVar : this.f404a) {
                synchronized (bgVar) {
                    if (bgVar.f441j == C1075a.STARTED) {
                        this.f405b.f373n.m579b(bgVar);
                    }
                }
            }
        }
    }

    /* renamed from: crittercism.android.bg.2 */
    static final class C10682 extends di {
        final /* synthetic */ az f406a;

        C10682(az azVar) {
            this.f406a = azVar;
        }

        public final void m483a() {
            ea eaVar = ea.TXN_CRASH_ALL_FAULT;
            this.f406a.f373n.m574a();
        }
    }

    /* renamed from: crittercism.android.bg.3 */
    static final class C10693 extends di {
        final /* synthetic */ az f407a;
        final /* synthetic */ bg f408b;

        C10693(az azVar, bg bgVar) {
            this.f407a = azVar;
            this.f408b = bgVar;
        }

        public final void m484a() {
            this.f407a.f376q.f707a.block();
            this.f407a.f373n.m577a(this.f408b);
        }
    }

    /* renamed from: crittercism.android.bg.4 */
    final class C10704 extends di {
        final /* synthetic */ bg f409a;
        final /* synthetic */ bg f410b;

        C10704(bg bgVar, bg bgVar2) {
            this.f410b = bgVar;
            this.f409a = bgVar2;
        }

        public final void m485a() {
            this.f410b.a.f376q.f707a.block();
            this.f410b.a.f373n.m577a(this.f409a);
        }
    }

    /* renamed from: crittercism.android.bg.5 */
    final class C10715 extends di {
        final /* synthetic */ bg f411a;

        C10715(bg bgVar) {
            this.f411a = bgVar;
        }

        public final void m486a() {
            this.f411a.m513s();
        }
    }

    /* renamed from: crittercism.android.bg.6 */
    final class C10736 extends di {
        final /* synthetic */ bg f413a;
        final /* synthetic */ bg f414b;

        /* renamed from: crittercism.android.bg.6.1 */
        final class C10721 implements Runnable {
            final /* synthetic */ C10736 f412a;

            C10721(C10736 c10736) {
                this.f412a = c10736;
            }

            public final void run() {
            }
        }

        C10736(bg bgVar, bg bgVar2) {
            this.f414b = bgVar;
            this.f413a = bgVar2;
        }

        public final void m487a() {
            if (this.f413a.f441j != C1075a.SUCCESS) {
                Runnable c10721 = new C10721(this);
                Executor executor = this.f414b.a.f378s;
                Object futureTask = new FutureTask(c10721, null);
                executor.execute(futureTask);
                try {
                    futureTask.get();
                } catch (Throwable e) {
                    dx.m777a(e);
                } catch (Throwable e2) {
                    dx.m777a(e2);
                }
            }
            this.f414b.a.f376q.f707a.block();
            this.f414b.a.f373n.m576a(this.f414b.f443l);
            this.f414b.a.f374o.m577a(this.f413a);
        }
    }

    /* renamed from: crittercism.android.bg.7 */
    final class C10747 extends di {
        final /* synthetic */ bg f415a;
        final /* synthetic */ bg f416b;

        C10747(bg bgVar, bg bgVar2) {
            this.f416b = bgVar;
            this.f415a = bgVar2;
        }

        public final void m488a() {
            this.f416b.a.f376q.f707a.block();
            this.f416b.a.f373n.m577a(this.f415a);
        }
    }

    /* renamed from: crittercism.android.bg.a */
    enum C1075a {
        CREATED,
        STARTED,
        SUCCESS,
        SLOW,
        FAILED,
        TIMEOUT,
        CRASHED,
        ABORTED,
        INTERRUPTED
    }

    static {
        f427b = Executors.newSingleThreadExecutor(new dz());
        f428c = Executors.newScheduledThreadPool(1, new dz());
        f429o = new LinkedList();
        f430p = 0;
        f431q = 0;
        f432r = new int[]{32, 544, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 8224};
        f433s = null;
        f434t = new bh();
    }

    public bg(az azVar, String str) {
        int i = -1;
        this.f436e = -1;
        this.f437f = -1;
        this.f445n = null;
        if (str.length() > MotionEventCompat.ACTION_MASK) {
            dx.m782c("Transaction name exceeds 255 characters! Truncating to first 255 characters.");
            this.f435d = str.substring(0, MotionEventCompat.ACTION_MASK);
        } else {
            this.f435d = str;
        }
        this.f441j = C1075a.CREATED;
        this.f442k = new HashMap();
        this.a = azVar;
        this.f443l = cg.f616a.m688a();
        this.f436e = -1;
        JSONObject optJSONObject = f434t.f449d.optJSONObject(str);
        if (optJSONObject != null) {
            i = optJSONObject.optInt(GameServices.SCORE_VALUE, -1);
        }
        this.f437f = i;
    }

    private bg(bg bgVar) {
        this.f436e = -1;
        this.f437f = -1;
        this.f445n = null;
        this.f435d = bgVar.f435d;
        this.f436e = bgVar.f436e;
        this.f437f = bgVar.f437f;
        this.f438g = bgVar.f438g;
        this.f439h = bgVar.f439h;
        this.f441j = bgVar.f441j;
        this.f442k = bgVar.f442k;
        this.f443l = bgVar.f443l;
        this.f440i = bgVar.f440i;
        this.f444m = bgVar.f444m;
    }

    public bg(JSONArray jSONArray) {
        this.f436e = -1;
        this.f437f = -1;
        this.f445n = null;
        this.f435d = jSONArray.getString(0);
        this.f441j = C1075a.values()[jSONArray.getInt(1)];
        this.f436e = (long) ((int) (jSONArray.getDouble(2) * 1000.0d));
        this.f437f = jSONArray.optInt(3, -1);
        this.f442k = new HashMap();
        JSONObject jSONObject = jSONArray.getJSONObject(4);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            this.f442k.put(str, jSONObject.getString(str));
        }
        this.f438g = ed.f784a.m795a(jSONArray.getString(5));
        this.f439h = ed.f784a.m795a(jSONArray.getString(6));
        this.f440i = (long) (jSONArray.optDouble(7, 0.0d) * Math.pow(10.0d, 9.0d));
        this.f443l = cg.f616a.m688a();
    }

    public static List m492a(az azVar, boolean z) {
        List linkedList = new LinkedList();
        synchronized (f429o) {
            linkedList.addAll(f429o);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        for (int size = linkedList.size() - 1; size >= 0; size--) {
            bg bgVar = (bg) linkedList.get(size);
            synchronized (bgVar) {
                if (bgVar.f441j == C1075a.STARTED) {
                    bgVar.f439h = currentTimeMillis;
                    bgVar.f441j = C1075a.CRASHED;
                    if (m506l()) {
                        bgVar.f440i = (nanoTime - Math.max(f430p, bgVar.f444m)) + bgVar.f440i;
                    }
                } else {
                    linkedList.remove(size);
                }
                bgVar.m512r();
            }
        }
        Object futureTask = new FutureTask(new C10682(azVar), null);
        synchronized (f427b) {
            f427b.execute(futureTask);
            if (z) {
                azVar.f385z.clear();
            } else {
                f427b.shutdown();
            }
        }
        try {
            futureTask.get();
            return linkedList;
        } catch (Throwable e) {
            dx.m777a(e);
            return linkedList;
        } catch (Throwable e2) {
            dx.m777a(e2);
            return linkedList;
        }
    }

    private void m493a(long j) {
        if (m506l()) {
            this.f445n = f428c.schedule(new C10715(this), j, TimeUnit.MILLISECONDS);
        }
    }

    public static void m494a(aw awVar) {
        try {
            bs w = awVar.m381w();
            List<bq> c = w.m580c();
            long currentTimeMillis = System.currentTimeMillis();
            for (bq bqVar : c) {
                JSONArray jSONArray = (JSONArray) ((bz) bqVar).m654a();
                if (jSONArray != null) {
                    try {
                        ch bgVar = new bg(jSONArray);
                        bgVar.f439h = currentTimeMillis;
                        bgVar.f441j = C1075a.ABORTED;
                        awVar.m382x().m577a(bgVar);
                    } catch (Throwable e) {
                        dx.m777a(e);
                    } catch (Throwable e2) {
                        dx.m777a(e2);
                    }
                }
            }
            w.m574a();
        } catch (ThreadDeath e3) {
            throw e3;
        } catch (Throwable e22) {
            dx.m777a(e22);
        }
    }

    public static void m495a(az azVar) {
        f431q = System.nanoTime();
        List<bg> linkedList = new LinkedList();
        synchronized (f429o) {
            linkedList.addAll(f429o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.f441j == C1075a.STARTED) {
                    if (bgVar.f444m < f430p) {
                        bgVar.f440i += f431q - f430p;
                    } else if (bgVar.f444m <= f431q) {
                        bgVar.f440i += f431q - bgVar.f444m;
                    }
                }
                bgVar.m512r();
            }
        }
        Object futureTask = new FutureTask(new C10671(linkedList, azVar), null);
        synchronized (f427b) {
            f427b.execute(futureTask);
        }
        try {
            futureTask.get();
        } catch (Throwable e) {
            dx.m777a(e);
        } catch (Throwable e2) {
            dx.m777a(e2);
        }
    }

    private void m496a(C1075a c1075a) {
        if (!(c1075a == C1075a.SUCCESS || c1075a == C1075a.FAILED)) {
            C1075a c1075a2 = C1075a.INTERRUPTED;
        }
        if (this.f441j == C1075a.STARTED) {
            m512r();
            m500b(c1075a);
        } else if (this.f441j != C1075a.TIMEOUT) {
            dx.m780b("Transaction " + this.f435d + " is not running. Either it has not been started or it has been stopped.", new IllegalStateException("Transaction is not running"));
        }
    }

    public static void m497a(bh bhVar) {
        f434t = bhVar;
    }

    private void m498b(int i) {
        synchronized (this) {
            if (i < 0) {
                dx.m782c("Ignoring Transaction.setValue(int) call. Negative parameter provided.");
            } else if (this.f441j == C1075a.CREATED) {
                this.f437f = i;
            } else if (this.f441j == C1075a.STARTED) {
                this.f437f = i;
                Runnable c10747 = new C10747(this, new bg(this));
                synchronized (f427b) {
                    f427b.execute(c10747);
                }
            } else {
                dx.m780b("Transaction " + this.f435d + " no longer in progress. Ignoring setValue(int) call.", new IllegalStateException("Transaction no longer in progress"));
            }
        }
    }

    public static void m499b(az azVar) {
        try {
            bg bgVar = new bg(azVar, "App Load");
            f433s = bgVar;
            synchronized (bgVar) {
                long m = m507m();
                if (m != -1) {
                    f433s.f441j = C1075a.STARTED;
                    f433s.f438g = System.currentTimeMillis() - (SystemClock.elapsedRealtime() - m);
                    bg bgVar2 = f433s;
                    m = TimeUnit.NANOSECONDS.convert(m, TimeUnit.MILLISECONDS);
                    bgVar2.f444m = System.nanoTime() - (TimeUnit.NANOSECONDS.convert(SystemClock.elapsedRealtime(), TimeUnit.MILLISECONDS) - m);
                    f433s.f436e = f434t.m526a(f433s.f435d);
                    synchronized (f429o) {
                        f429o.add(f433s);
                    }
                    Runnable c10693 = new C10693(azVar, new bg(f433s));
                    synchronized (f427b) {
                        f427b.execute(c10693);
                        f433s.m493a(f433s.f436e);
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    private void m500b(C1075a c1075a) {
        this.f441j = c1075a;
        this.f439h = System.currentTimeMillis();
        long nanoTime = System.nanoTime();
        if (m506l()) {
            this.f440i = (nanoTime - Math.max(f430p, this.f444m)) + this.f440i;
        }
        synchronized (f429o) {
            f429o.remove(this);
        }
        Runnable c10736 = new C10736(this, new bg(this));
        synchronized (f427b) {
            f427b.execute(c10736);
        }
    }

    public static void m503f() {
        f430p = System.nanoTime();
        List<bg> linkedList = new LinkedList();
        synchronized (f429o) {
            linkedList.addAll(f429o);
        }
        if (f433s != null && f431q == 0) {
            synchronized (f433s) {
                bg bgVar = f433s;
                bgVar.f440i += f430p - f433s.f444m;
            }
        }
        for (bg bgVar2 : linkedList) {
            synchronized (bgVar2) {
                if (bgVar2.f441j == C1075a.STARTED) {
                    if (bgVar2.f445n != null && bgVar2.f445n.isCancelled()) {
                        bgVar2.m493a(bgVar2.f436e - TimeUnit.MILLISECONDS.convert(bgVar2.f440i, TimeUnit.NANOSECONDS));
                    } else if (bgVar2.f445n == null) {
                        bgVar2.m493a(bgVar2.f436e);
                    }
                }
            }
        }
    }

    public static void m504g() {
        try {
            if (f433s != null) {
                f433s.m518b();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public static void m505i() {
        List<bg> linkedList = new LinkedList();
        synchronized (f429o) {
            linkedList.addAll(f429o);
        }
        for (bg bgVar : linkedList) {
            synchronized (bgVar) {
                if (bgVar.f441j == C1075a.STARTED) {
                    bgVar.f436e = f434t.m526a(bgVar.f435d);
                    bgVar.m512r();
                    bgVar.m493a(bgVar.f436e);
                }
            }
        }
    }

    private static boolean m506l() {
        return f430p > f431q;
    }

    private static long m507m() {
        long[] jArr = new long[1];
        String str = "/proc/" + Process.myPid() + "/stat";
        try {
            return !((Boolean) Process.class.getDeclaredMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class}).invoke(null, new Object[]{str, f432r, null, jArr, null})).booleanValue() ? -1 : jArr[0] * 10;
        } catch (Throwable e) {
            dx.m777a(e);
            return -1;
        } catch (Throwable e2) {
            dx.m777a(e2);
            return -1;
        } catch (Throwable e22) {
            dx.m777a(e22);
            return -1;
        } catch (Throwable e222) {
            dx.m777a(e222);
            return -1;
        }
    }

    private void m508n() {
        synchronized (this) {
            if (this.f441j == C1075a.CREATED) {
                this.f441j = C1075a.STARTED;
                this.f438g = System.currentTimeMillis();
                this.f444m = System.nanoTime();
                this.f436e = f434t.m526a(this.f435d);
                synchronized (f429o) {
                    f429o.add(this);
                }
                Runnable c10704 = new C10704(this, new bg(this));
                synchronized (f427b) {
                    f427b.execute(c10704);
                    m493a(this.f436e);
                }
            } else {
                dx.m780b("Transaction " + this.f435d + " has already been started.", new IllegalStateException("Transaction has already started"));
            }
        }
    }

    private void m509o() {
        synchronized (this) {
            m496a(C1075a.SUCCESS);
        }
    }

    private void m510p() {
        synchronized (this) {
            m496a(C1075a.FAILED);
        }
    }

    private void m511q() {
        synchronized (this) {
            m496a(C1075a.INTERRUPTED);
        }
    }

    private void m512r() {
        synchronized (this) {
            if (this.f445n != null) {
                this.f445n.cancel(false);
            }
        }
    }

    private void m513s() {
        synchronized (this) {
            if (this.f441j == C1075a.STARTED) {
                m500b(C1075a.TIMEOUT);
            }
        }
    }

    private int m514t() {
        int i;
        synchronized (this) {
            i = this.f437f;
        }
        return i;
    }

    public final void m515a() {
        try {
            m508n();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void m516a(int i) {
        try {
            m498b(i);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void m517a(OutputStream outputStream) {
        JSONArray jSONArray = null;
        try {
            jSONArray = m523j();
        } catch (JSONException e) {
        }
        if (jSONArray != null) {
            outputStream.write(jSONArray.toString().getBytes());
        }
    }

    public final void m518b() {
        try {
            m509o();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void m519c() {
        try {
            m510p();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final int m520d() {
        try {
            return m514t();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return -1;
        }
    }

    public final String m521e() {
        return this.f443l;
    }

    public final void m522h() {
        try {
            m511q();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final JSONArray m523j() {
        JSONArray put = new JSONArray().put(this.f435d).put(this.f441j.ordinal()).put(((double) this.f436e) / 1000.0d).put(this.f437f == -1 ? JSONObject.NULL : Integer.valueOf(this.f437f)).put(new JSONObject(this.f442k)).put(ed.f784a.m797a(new Date(this.f438g))).put(ed.f784a.m797a(new Date(this.f439h)));
        if (VERSION.SDK_INT >= 14) {
            put.put(((double) Math.round((((double) this.f440i) / Math.pow(10.0d, 9.0d)) * 1000.0d)) / 1000.0d);
        } else {
            put.put(JSONObject.NULL);
        }
        return put;
    }

    public final C1075a m524k() {
        return this.f441j;
    }
}
