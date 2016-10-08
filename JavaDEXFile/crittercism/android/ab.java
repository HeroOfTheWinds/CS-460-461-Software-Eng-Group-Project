package crittercism.android;

import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public final class ab extends SSLSocketFactory {
    private SSLSocketFactory f275a;
    private C1126e f276b;
    private C1116d f277c;

    public ab(SSLSocketFactory sSLSocketFactory, C1126e c1126e, C1116d c1116d) {
        this.f275a = sSLSocketFactory;
        this.f276b = c1126e;
        this.f277c = c1116d;
    }

    private Socket m260a(Socket socket) {
        if (socket == null) {
            return socket;
        }
        try {
            if (!(socket instanceof SSLSocket)) {
                return socket;
            }
            return new aa((SSLSocket) socket, this.f276b, this.f277c);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return socket;
        }
    }

    public final SSLSocketFactory m261a() {
        return this.f275a;
    }

    public final Socket createSocket() {
        return m260a(this.f275a.createSocket());
    }

    public final Socket createSocket(String str, int i) {
        return m260a(this.f275a.createSocket(str, i));
    }

    public final Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        return m260a(this.f275a.createSocket(str, i, inetAddress, i2));
    }

    public final Socket createSocket(InetAddress inetAddress, int i) {
        return m260a(this.f275a.createSocket(inetAddress, i));
    }

    public final Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        return m260a(this.f275a.createSocket(inetAddress, i, inetAddress2, i2));
    }

    public final Socket createSocket(Socket socket, String str, int i, boolean z) {
        return m260a(this.f275a.createSocket(socket, str, i, z));
    }

    public final String[] getDefaultCipherSuites() {
        return this.f275a.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.f275a.getSupportedCipherSuites();
    }
}
