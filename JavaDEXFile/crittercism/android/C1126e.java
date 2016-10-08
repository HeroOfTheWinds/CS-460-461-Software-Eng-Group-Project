package crittercism.android;

import crittercism.android.C1108c.C1107a;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/* renamed from: crittercism.android.e */
public final class C1126e {
    List f761a;
    final Set f762b;
    final Set f763c;
    private Executor f764d;

    /* renamed from: crittercism.android.e.a */
    final class C1125a implements Runnable {
        final /* synthetic */ C1126e f759a;
        private C1108c f760b;

        private C1125a(C1126e c1126e, C1108c c1108c) {
            this.f759a = c1126e;
            this.f760b = c1108c;
        }

        private boolean m783a(C1108c c1108c) {
            String a = c1108c.m656a();
            synchronized (this.f759a.f762b) {
                for (String contains : this.f759a.f762b) {
                    if (a.contains(contains)) {
                        return true;
                    }
                }
                return false;
            }
        }

        private boolean m784a(String str) {
            synchronized (this.f759a.f763c) {
                for (String contains : this.f759a.f763c) {
                    if (str.contains(contains)) {
                        return false;
                    }
                }
                return true;
            }
        }

        public final void run() {
            if (!m783a(this.f760b)) {
                String a = this.f760b.m656a();
                if (m784a(a)) {
                    int indexOf = a.indexOf("?");
                    if (indexOf != -1) {
                        this.f760b.m662a(a.substring(0, indexOf));
                    }
                }
                synchronized (this.f759a.f761a) {
                    for (C1065f a2 : this.f759a.f761a) {
                        a2.m399a(this.f760b);
                    }
                }
            }
        }
    }

    public C1126e(Executor executor) {
        this(executor, new LinkedList(), new LinkedList());
    }

    private C1126e(Executor executor, List list, List list2) {
        this.f761a = new LinkedList();
        this.f762b = new HashSet();
        this.f763c = new HashSet();
        this.f764d = executor;
        m788a(list);
        m789b(list2);
    }

    @Deprecated
    public final void m785a(C1108c c1108c) {
        m786a(c1108c, C1107a.LEGACY_JAVANET);
    }

    public final void m786a(C1108c c1108c, C1107a c1107a) {
        if (!c1108c.f573b) {
            c1108c.f573b = true;
            c1108c.f574c = c1107a;
            this.f764d.execute(new C1125a(c1108c, (byte) 0));
        }
    }

    public final void m787a(C1065f c1065f) {
        synchronized (this.f761a) {
            this.f761a.add(c1065f);
        }
    }

    public final void m788a(List list) {
        synchronized (this.f762b) {
            this.f762b.addAll(list);
            this.f762b.remove(null);
        }
    }

    public final void m789b(List list) {
        synchronized (this.f763c) {
            this.f763c.addAll(list);
            this.f763c.remove(null);
        }
    }
}
