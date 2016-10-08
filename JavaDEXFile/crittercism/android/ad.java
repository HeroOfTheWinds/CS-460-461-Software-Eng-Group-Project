package crittercism.android;

import java.net.SocketImpl;
import java.net.SocketImplFactory;

public final class ad implements SocketImplFactory {
    private Class f291a;
    private SocketImplFactory f292b;
    private C1126e f293c;
    private C1116d f294d;

    public ad(Class cls, C1126e c1126e, C1116d c1116d) {
        this.f293c = c1126e;
        this.f294d = c1116d;
        this.f291a = cls;
        Class cls2 = this.f291a;
        if (cls2 == null) {
            throw new cl("Class was null");
        }
        try {
            cls2.newInstance();
        } catch (Throwable th) {
            cl clVar = new cl("Unable to create new instance", th);
        }
    }

    public ad(SocketImplFactory socketImplFactory, C1126e c1126e, C1116d c1116d) {
        this.f293c = c1126e;
        this.f294d = c1116d;
        this.f292b = socketImplFactory;
        SocketImplFactory socketImplFactory2 = this.f292b;
        if (socketImplFactory2 == null) {
            throw new cl("Factory was null");
        }
        try {
            if (socketImplFactory2.createSocketImpl() == null) {
                throw new cl("Factory does not work");
            }
        } catch (Throwable th) {
            cl clVar = new cl("Factory does not work", th);
        }
    }

    public final SocketImpl createSocketImpl() {
        SocketImpl socketImpl = null;
        if (this.f292b != null) {
            socketImpl = this.f292b.createSocketImpl();
        } else {
            Class cls = this.f291a;
            try {
                socketImpl = (SocketImpl) this.f291a.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            }
        }
        return socketImpl != null ? new ac(this.f293c, this.f294d, socketImpl) : socketImpl;
    }
}
