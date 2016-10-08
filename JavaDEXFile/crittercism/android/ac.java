package crittercism.android;

import android.support.v4.os.EnvironmentCompat;
import crittercism.android.C1108c.C1107a;
import crittercism.android.C1136k.C1135a;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

public final class ac extends SocketImpl implements ae {
    private static Field f278a;
    private static Field f279b;
    private static Field f280c;
    private static Field f281d;
    private static Method[] f282e;
    private static boolean f283f;
    private static Throwable f284g;
    private final Queue f285h;
    private C1126e f286i;
    private C1116d f287j;
    private SocketImpl f288k;
    private C1150w f289l;
    private C1151x f290m;

    /* renamed from: crittercism.android.ac.1 */
    static final class C10531 extends SocketImpl {
        C10531() {
        }

        protected final void accept(SocketImpl socketImpl) {
        }

        protected final int available() {
            return 0;
        }

        protected final void bind(InetAddress inetAddress, int i) {
        }

        protected final void close() {
        }

        protected final void connect(String str, int i) {
        }

        protected final void connect(InetAddress inetAddress, int i) {
        }

        protected final void connect(SocketAddress socketAddress, int i) {
        }

        protected final void create(boolean z) {
        }

        protected final FileDescriptor getFileDescriptor() {
            return null;
        }

        protected final InetAddress getInetAddress() {
            return null;
        }

        protected final InputStream getInputStream() {
            return null;
        }

        protected final int getLocalPort() {
            return 0;
        }

        public final Object getOption(int i) {
            return null;
        }

        protected final OutputStream getOutputStream() {
            return null;
        }

        protected final int getPort() {
            return 0;
        }

        protected final void listen(int i) {
        }

        protected final void sendUrgentData(int i) {
        }

        public final void setOption(int i, Object obj) {
        }

        protected final void setPerformancePreferences(int i, int i2, int i3) {
        }

        protected final void shutdownInput() {
        }

        protected final void shutdownOutput() {
        }

        protected final boolean supportsUrgentData() {
            return false;
        }

        public final String toString() {
            return null;
        }
    }

    /* renamed from: crittercism.android.ac.2 */
    static final class C10542 implements Executor {
        C10542() {
        }

        public final void execute(Runnable runnable) {
        }
    }

    static {
        f282e = new Method[20];
        f283f = false;
        f284g = null;
        try {
            f278a = SocketImpl.class.getDeclaredField("address");
            f279b = SocketImpl.class.getDeclaredField("fd");
            f280c = SocketImpl.class.getDeclaredField("localport");
            f281d = SocketImpl.class.getDeclaredField("port");
            AccessibleObject accessibleObject = f278a;
            AccessibleObject[] accessibleObjectArr = new AccessibleObject[]{f279b, f280c, f281d};
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
            }
            if (accessibleObjectArr.length > 0) {
                C1134j.m821a(accessibleObjectArr);
            }
            f282e[0] = SocketImpl.class.getDeclaredMethod("accept", new Class[]{SocketImpl.class});
            f282e[1] = SocketImpl.class.getDeclaredMethod("available", new Class[0]);
            f282e[2] = SocketImpl.class.getDeclaredMethod("bind", new Class[]{InetAddress.class, Integer.TYPE});
            f282e[3] = SocketImpl.class.getDeclaredMethod("close", new Class[0]);
            f282e[4] = SocketImpl.class.getDeclaredMethod("connect", new Class[]{InetAddress.class, Integer.TYPE});
            f282e[5] = SocketImpl.class.getDeclaredMethod("connect", new Class[]{SocketAddress.class, Integer.TYPE});
            f282e[6] = SocketImpl.class.getDeclaredMethod("connect", new Class[]{String.class, Integer.TYPE});
            f282e[7] = SocketImpl.class.getDeclaredMethod("create", new Class[]{Boolean.TYPE});
            f282e[8] = SocketImpl.class.getDeclaredMethod("getFileDescriptor", new Class[0]);
            f282e[9] = SocketImpl.class.getDeclaredMethod("getInetAddress", new Class[0]);
            f282e[10] = SocketImpl.class.getDeclaredMethod("getInputStream", new Class[0]);
            f282e[11] = SocketImpl.class.getDeclaredMethod("getLocalPort", new Class[0]);
            f282e[12] = SocketImpl.class.getDeclaredMethod("getOutputStream", new Class[0]);
            f282e[13] = SocketImpl.class.getDeclaredMethod("getPort", new Class[0]);
            f282e[14] = SocketImpl.class.getDeclaredMethod("listen", new Class[]{Integer.TYPE});
            f282e[15] = SocketImpl.class.getDeclaredMethod("sendUrgentData", new Class[]{Integer.TYPE});
            f282e[16] = SocketImpl.class.getDeclaredMethod("setPerformancePreferences", new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
            f282e[17] = SocketImpl.class.getDeclaredMethod("shutdownInput", new Class[0]);
            f282e[18] = SocketImpl.class.getDeclaredMethod("shutdownOutput", new Class[0]);
            f282e[19] = SocketImpl.class.getDeclaredMethod("supportsUrgentData", new Class[0]);
            C1134j.m821a(f282e);
            f283f = true;
        } catch (Throwable e) {
            f283f = false;
            f284g = e;
        } catch (Throwable e2) {
            Throwable th = e2;
            f283f = false;
            int i = 0;
            while (i < 20) {
                if (f282e[i] == null) {
                    break;
                }
                i++;
            }
            i = 20;
            f284g = new ck("Bad method: " + i, th);
        } catch (Throwable e22) {
            Throwable th2 = e22;
            f283f = false;
            String str = EnvironmentCompat.MEDIA_UNKNOWN;
            if (f278a == null) {
                str = "address";
            } else if (f279b == null) {
                str = "fd";
            } else if (f280c == null) {
                str = "localport";
            } else if (f281d == null) {
                str = "port";
            }
            f284g = new ck("No such field: " + str, th2);
        } catch (Throwable e222) {
            f283f = false;
            f284g = e222;
        }
    }

    public ac(C1126e c1126e, C1116d c1116d, SocketImpl socketImpl) {
        this.f285h = new LinkedList();
        if (c1126e == null) {
            throw new NullPointerException("dispatch was null");
        } else if (socketImpl == null) {
            throw new NullPointerException("delegate was null");
        } else {
            this.f286i = c1126e;
            this.f287j = c1116d;
            this.f288k = socketImpl;
            m269f();
        }
    }

    private C1108c m262a(boolean z) {
        C1108c c1108c = new C1108c();
        InetAddress inetAddress = getInetAddress();
        if (inetAddress != null) {
            c1108c.m664a(inetAddress);
        }
        int port = getPort();
        if (port > 0) {
            c1108c.m657a(port);
        }
        if (z) {
            c1108c.m660a(C1135a.HTTP);
        }
        if (this.f287j != null) {
            c1108c.f581j = this.f287j.m717a();
        }
        if (bc.m475b()) {
            c1108c.m659a(bc.m473a());
        }
        return c1108c;
    }

    private Object m263a(int i, Object... objArr) {
        Throwable e;
        try {
            f278a.set(this.f288k, this.address);
            f279b.set(this.f288k, this.fd);
            f280c.setInt(this.f288k, this.localport);
            f281d.setInt(this.f288k, this.port);
            try {
                Object invoke = f282e[i].invoke(this.f288k, objArr);
                m269f();
                return invoke;
            } catch (Throwable e2) {
                throw new ck(e2);
            } catch (Throwable e22) {
                throw new ck(e22);
            } catch (Throwable e222) {
                Throwable th = e222;
                e222 = th.getTargetException();
                if (e222 == null) {
                    throw new ck(th);
                } else if (e222 instanceof Exception) {
                    throw ((Exception) e222);
                } else if (e222 instanceof Error) {
                    throw ((Error) e222);
                } else {
                    throw new ck(e222);
                }
            } catch (Throwable e2222) {
                throw new ck(e2222);
            } catch (Throwable e22222) {
                throw new ck(e22222);
            } catch (Throwable th2) {
                m269f();
            }
        } catch (Throwable e222222) {
            throw new ck(e222222);
        } catch (Throwable e2222222) {
            throw new ck(e2222222);
        }
    }

    private Object m264b(int i, Object... objArr) {
        try {
            return m263a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    private Object m265c(int i, Object... objArr) {
        try {
            return m263a(i, objArr);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new ck(e3);
        }
    }

    public static boolean m266c() {
        return f283f;
    }

    public static Throwable m267d() {
        return f284g;
    }

    public static void m268e() {
        if (f283f) {
            SocketImpl acVar = new ac(new C1126e(new C10542()), null, new C10531());
            try {
                acVar.setOption(0, new Object());
                acVar.getOption(0);
                acVar.sendUrgentData(0);
                acVar.listen(0);
                acVar.getOutputStream();
                acVar.getInputStream();
                acVar.create(false);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.connect(null, 0);
                acVar.close();
                acVar.bind(null, 0);
                acVar.available();
                acVar.accept(acVar);
                acVar.getFileDescriptor();
                acVar.getInetAddress();
                acVar.getLocalPort();
                acVar.getPort();
                acVar.setPerformancePreferences(0, 0, 0);
                acVar.shutdownInput();
                acVar.shutdownOutput();
                acVar.supportsUrgentData();
            } catch (IOException e) {
            } catch (ck e2) {
                throw e2;
            } catch (Throwable th) {
                ck ckVar = new ck(th);
            }
        } else {
            throw new ck(f284g);
        }
    }

    private void m269f() {
        try {
            this.address = (InetAddress) f278a.get(this.f288k);
            this.fd = (FileDescriptor) f279b.get(this.f288k);
            this.localport = f280c.getInt(this.f288k);
            this.port = f281d.getInt(this.f288k);
        } catch (Throwable e) {
            throw new ck(e);
        } catch (Throwable e2) {
            throw new ck(e2);
        }
    }

    public final C1108c m270a() {
        return m262a(true);
    }

    public final void m271a(C1108c c1108c) {
        synchronized (this.f285h) {
            this.f285h.add(c1108c);
        }
    }

    public final void accept(SocketImpl socketImpl) {
        m265c(0, socketImpl);
    }

    public final int available() {
        Integer num = (Integer) m265c(1, new Object[0]);
        if (num != null) {
            return num.intValue();
        }
        throw new ck("Received a null Integer");
    }

    public final C1108c m272b() {
        C1108c c1108c;
        synchronized (this.f285h) {
            c1108c = (C1108c) this.f285h.poll();
        }
        return c1108c;
    }

    public final void bind(InetAddress inetAddress, int i) {
        m265c(2, inetAddress, Integer.valueOf(i));
    }

    public final void close() {
        m265c(3, new Object[0]);
        try {
            if (this.f290m != null) {
                this.f290m.m870d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void connect(String str, int i) {
        try {
            m265c(6, str, Integer.valueOf(i));
        } catch (Throwable e) {
            if (str != null) {
                try {
                    C1108c a = m262a(false);
                    a.m665b();
                    a.m668c();
                    a.m674f();
                    a.m667b(str);
                    a.m657a(i);
                    a.m663a(e);
                    this.f286i.m786a(a, C1107a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m777a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(InetAddress inetAddress, int i) {
        try {
            m265c(4, inetAddress, Integer.valueOf(i));
        } catch (Throwable e) {
            if (inetAddress != null) {
                try {
                    C1108c a = m262a(false);
                    a.m665b();
                    a.m668c();
                    a.m674f();
                    a.m664a(inetAddress);
                    a.m657a(i);
                    a.m663a(e);
                    this.f286i.m786a(a, C1107a.SOCKET_IMPL_CONNECT);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m777a(th);
                }
            }
            throw e;
        }
    }

    public final void connect(SocketAddress socketAddress, int i) {
        try {
            m265c(5, socketAddress, Integer.valueOf(i));
        } catch (Throwable e) {
            if (socketAddress != null) {
                try {
                    if (socketAddress instanceof InetSocketAddress) {
                        C1108c a = m262a(false);
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                        a.m665b();
                        a.m668c();
                        a.m674f();
                        a.m664a(inetSocketAddress.getAddress());
                        a.m657a(inetSocketAddress.getPort());
                        a.m663a(e);
                        this.f286i.m786a(a, C1107a.SOCKET_IMPL_CONNECT);
                    }
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    dx.m777a(th);
                }
            }
            throw e;
        }
    }

    public final void create(boolean z) {
        m265c(7, Boolean.valueOf(z));
    }

    public final FileDescriptor getFileDescriptor() {
        return (FileDescriptor) m264b(8, new Object[0]);
    }

    public final InetAddress getInetAddress() {
        return (InetAddress) m264b(9, new Object[0]);
    }

    public final InputStream getInputStream() {
        InputStream inputStream = (InputStream) m265c(10, new Object[0]);
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f290m != null && this.f290m.m866a(inputStream)) {
                return this.f290m;
            }
            this.f290m = new C1151x(this, inputStream, this.f286i);
            return this.f290m;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return inputStream;
        }
    }

    public final int getLocalPort() {
        return ((Integer) m264b(11, new Object[0])).intValue();
    }

    public final Object getOption(int i) {
        return this.f288k.getOption(i);
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = (OutputStream) m265c(12, new Object[0]);
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.f289l != null && this.f289l.m854a(outputStream)) {
                return this.f289l;
            }
            this.f289l = new C1150w(this, outputStream);
            return this.f289l;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return outputStream;
        }
    }

    public final int getPort() {
        return ((Integer) m264b(13, new Object[0])).intValue();
    }

    public final void listen(int i) {
        m265c(14, Integer.valueOf(i));
    }

    public final void sendUrgentData(int i) {
        m265c(15, Integer.valueOf(i));
    }

    public final void setOption(int i, Object obj) {
        this.f288k.setOption(i, obj);
    }

    public final void setPerformancePreferences(int i, int i2, int i3) {
        m264b(16, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }

    public final void shutdownInput() {
        m265c(17, new Object[0]);
    }

    public final void shutdownOutput() {
        m265c(18, new Object[0]);
    }

    public final boolean supportsUrgentData() {
        return ((Boolean) m264b(19, new Object[0])).booleanValue();
    }

    public final String toString() {
        return this.f288k.toString();
    }
}
