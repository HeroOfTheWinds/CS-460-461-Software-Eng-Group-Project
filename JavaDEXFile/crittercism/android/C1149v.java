package crittercism.android;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

/* renamed from: crittercism.android.v */
public final class C1149v implements URLStreamHandlerFactory {
    private static final Object f862a;
    private static C1149v f863b;
    private LinkedList f864c;
    private boolean f865d;
    private boolean f866e;

    /* renamed from: crittercism.android.v.a */
    public enum C1148a {
        HTTP_ONLY,
        HTTPS_ONLY,
        ALL
    }

    static {
        f862a = new Object();
    }

    public C1149v(C1148a c1148a, C1126e c1126e, C1116d c1116d) {
        this.f864c = new LinkedList();
        this.f865d = false;
        this.f866e = false;
        if (c1148a == C1148a.ALL || c1148a == C1148a.HTTP_ONLY) {
            this.f864c.add(new C1140o(c1126e, c1116d));
        }
        if (c1148a == C1148a.ALL || c1148a == C1148a.HTTPS_ONLY) {
            this.f864c.add(new C1143q(c1126e, c1116d));
        }
    }

    public static C1149v m841a() {
        return f863b;
    }

    private boolean m842d() {
        boolean z = false;
        synchronized (this) {
            synchronized (f862a) {
                if (f863b != this) {
                    boolean z2 = this.f865d;
                } else {
                    if (this.f865d && C1149v.m843e()) {
                        this.f865d = false;
                        f863b = null;
                    }
                    z = this.f865d;
                }
            }
        }
        return z;
    }

    private static boolean m843e() {
        Field[] declaredFields = URL.class.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            if (URLStreamHandlerFactory.class.isAssignableFrom(field.getType())) {
                try {
                    ea eaVar = ea.STREAM_HANDLER_FACTORY_ANNUL_REFLECTION_FAULT;
                    field.setAccessible(true);
                    field.set(null, null);
                    field.setAccessible(false);
                    URL.setURLStreamHandlerFactory(null);
                    return true;
                } catch (IllegalAccessException e) {
                    dx.m781c();
                } catch (SecurityException e2) {
                    dx.m781c();
                } catch (Throwable th) {
                    dx.m781c();
                }
            } else {
                i++;
            }
        }
        return false;
    }

    private static boolean m844f() {
        for (Field field : URL.class.getDeclaredFields()) {
            if (Hashtable.class.isAssignableFrom(field.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Class cls = (Class) parameterizedType.getActualTypeArguments()[0];
                Class cls2 = (Class) parameterizedType.getActualTypeArguments()[1];
                if (String.class.isAssignableFrom(cls) && URLStreamHandler.class.isAssignableFrom(cls2)) {
                    try {
                        ea eaVar = ea.STREAM_HANDLER_FACTORY_CLEAR_STREAM_HANDLERS_FAULT;
                        field.setAccessible(true);
                        Hashtable hashtable = (Hashtable) field.get(null);
                        if (hashtable != null) {
                            hashtable.clear();
                        }
                        field.setAccessible(false);
                        return true;
                    } catch (IllegalArgumentException e) {
                        dx.m781c();
                    } catch (SecurityException e2) {
                        dx.m781c();
                    } catch (IllegalAccessException e3) {
                        dx.m781c();
                    }
                }
            }
        }
        return false;
    }

    public final boolean m845b() {
        boolean z = true;
        synchronized (f862a) {
            if (f863b != null) {
                if (f863b != this) {
                    z = false;
                }
                return z;
            }
            if (!(this.f865d || this.f866e)) {
                try {
                    URL.setURLStreamHandlerFactory(this);
                    this.f865d = true;
                    f863b = this;
                } catch (Throwable th) {
                }
            }
            return this.f865d;
        }
    }

    public final boolean m846c() {
        boolean z = false;
        synchronized (this) {
            m842d();
            boolean f;
            if (this.f865d) {
                this.f866e = true;
                f = C1149v.m844f();
            } else {
                f = false;
            }
            if (!this.f865d || r2) {
                z = true;
            }
        }
        return z;
    }

    public final URLStreamHandler createURLStreamHandler(String str) {
        try {
            if (!this.f866e) {
                Iterator it = this.f864c.iterator();
                while (it.hasNext()) {
                    C1138m c1138m = (C1138m) it.next();
                    if (c1138m.m827a().equals(str)) {
                        return c1138m;
                    }
                }
            }
            return null;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            this.f866e = true;
            dx.m777a(th);
            return null;
        }
    }
}
