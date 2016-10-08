package crittercism.android;

import crittercism.android.C1108c.C1107a;
import crittercism.android.C1136k.C1135a;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public final class aa extends SSLSocket implements ae {
    private SSLSocket f269a;
    private C1126e f270b;
    private C1116d f271c;
    private final Queue f272d;
    private C1150w f273e;
    private C1151x f274f;

    public aa(SSLSocket sSLSocket, C1126e c1126e, C1116d c1116d) {
        this.f272d = new LinkedList();
        if (sSLSocket == null) {
            throw new NullPointerException("delegate was null");
        } else if (c1126e == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.f269a = sSLSocket;
            this.f270b = c1126e;
            this.f271c = c1116d;
        }
    }

    private C1108c m256a(boolean z) {
        C1108c c1108c = new C1108c();
        InetAddress inetAddress = this.f269a.getInetAddress();
        if (inetAddress != null) {
            c1108c.m664a(inetAddress);
        }
        if (z) {
            c1108c.m657a(getPort());
        }
        c1108c.m660a(C1135a.HTTPS);
        if (this.f271c != null) {
            c1108c.f581j = this.f271c.m717a();
        }
        if (bc.m475b()) {
            c1108c.m659a(bc.m473a());
        }
        return c1108c;
    }

    public final C1108c m257a() {
        return m256a(false);
    }

    public final void m258a(C1108c c1108c) {
        if (c1108c != null) {
            synchronized (this.f272d) {
                this.f272d.add(c1108c);
            }
        }
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.f269a.addHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final C1108c m259b() {
        C1108c c1108c;
        synchronized (this.f272d) {
            c1108c = (C1108c) this.f272d.poll();
        }
        return c1108c;
    }

    public final void bind(SocketAddress socketAddress) {
        this.f269a.bind(socketAddress);
    }

    public final void close() {
        this.f269a.close();
        try {
            if (this.f274f != null) {
                this.f274f.m870d();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }

    public final void connect(SocketAddress socketAddress) {
        this.f269a.connect(socketAddress);
    }

    public final void connect(SocketAddress socketAddress, int i) {
        this.f269a.connect(socketAddress, i);
    }

    public final boolean equals(Object obj) {
        return this.f269a.equals(obj);
    }

    public final SocketChannel getChannel() {
        return this.f269a.getChannel();
    }

    public final boolean getEnableSessionCreation() {
        return this.f269a.getEnableSessionCreation();
    }

    public final String[] getEnabledCipherSuites() {
        return this.f269a.getEnabledCipherSuites();
    }

    public final String[] getEnabledProtocols() {
        return this.f269a.getEnabledProtocols();
    }

    public final InetAddress getInetAddress() {
        return this.f269a.getInetAddress();
    }

    public final InputStream getInputStream() {
        InputStream inputStream = this.f269a.getInputStream();
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f274f != null && this.f274f.m866a(inputStream)) {
                return this.f274f;
            }
            this.f274f = new C1151x(this, inputStream, this.f270b);
            return this.f274f;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return inputStream;
        }
    }

    public final boolean getKeepAlive() {
        return this.f269a.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.f269a.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.f269a.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.f269a.getLocalSocketAddress();
    }

    public final boolean getNeedClientAuth() {
        return this.f269a.getNeedClientAuth();
    }

    public final boolean getOOBInline() {
        return this.f269a.getOOBInline();
    }

    public final OutputStream getOutputStream() {
        OutputStream outputStream = this.f269a.getOutputStream();
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.f273e != null && this.f273e.m854a(outputStream)) {
                return this.f273e;
            }
            C1150w c1150w = this.f273e;
            this.f273e = new C1150w(this, outputStream);
            return this.f273e;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
            return outputStream;
        }
    }

    public final int getPort() {
        return this.f269a.getPort();
    }

    public final int getReceiveBufferSize() {
        return this.f269a.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.f269a.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() {
        return this.f269a.getReuseAddress();
    }

    public final int getSendBufferSize() {
        return this.f269a.getSendBufferSize();
    }

    public final SSLSession getSession() {
        return this.f269a.getSession();
    }

    public final int getSoLinger() {
        return this.f269a.getSoLinger();
    }

    public final int getSoTimeout() {
        return this.f269a.getSoTimeout();
    }

    public final String[] getSupportedCipherSuites() {
        return this.f269a.getSupportedCipherSuites();
    }

    public final String[] getSupportedProtocols() {
        return this.f269a.getSupportedProtocols();
    }

    public final boolean getTcpNoDelay() {
        return this.f269a.getTcpNoDelay();
    }

    public final int getTrafficClass() {
        return this.f269a.getTrafficClass();
    }

    public final boolean getUseClientMode() {
        return this.f269a.getUseClientMode();
    }

    public final boolean getWantClientAuth() {
        return this.f269a.getWantClientAuth();
    }

    public final int hashCode() {
        return this.f269a.hashCode();
    }

    public final boolean isBound() {
        return this.f269a.isBound();
    }

    public final boolean isClosed() {
        return this.f269a.isClosed();
    }

    public final boolean isConnected() {
        return this.f269a.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.f269a.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.f269a.isOutputShutdown();
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.f269a.removeHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void sendUrgentData(int i) {
        this.f269a.sendUrgentData(i);
    }

    public final void setEnableSessionCreation(boolean z) {
        this.f269a.setEnableSessionCreation(z);
    }

    public final void setEnabledCipherSuites(String[] strArr) {
        this.f269a.setEnabledCipherSuites(strArr);
    }

    public final void setEnabledProtocols(String[] strArr) {
        this.f269a.setEnabledProtocols(strArr);
    }

    public final void setKeepAlive(boolean z) {
        this.f269a.setKeepAlive(z);
    }

    public final void setNeedClientAuth(boolean z) {
        this.f269a.setNeedClientAuth(z);
    }

    public final void setOOBInline(boolean z) {
        this.f269a.setOOBInline(z);
    }

    public final void setPerformancePreferences(int i, int i2, int i3) {
        this.f269a.setPerformancePreferences(i, i2, i3);
    }

    public final void setReceiveBufferSize(int i) {
        this.f269a.setReceiveBufferSize(i);
    }

    public final void setReuseAddress(boolean z) {
        this.f269a.setReuseAddress(z);
    }

    public final void setSendBufferSize(int i) {
        this.f269a.setSendBufferSize(i);
    }

    public final void setSoLinger(boolean z, int i) {
        this.f269a.setSoLinger(z, i);
    }

    public final void setSoTimeout(int i) {
        this.f269a.setSoTimeout(i);
    }

    public final void setTcpNoDelay(boolean z) {
        this.f269a.setTcpNoDelay(z);
    }

    public final void setTrafficClass(int i) {
        this.f269a.setTrafficClass(i);
    }

    public final void setUseClientMode(boolean z) {
        this.f269a.setUseClientMode(z);
    }

    public final void setWantClientAuth(boolean z) {
        this.f269a.setWantClientAuth(z);
    }

    public final void shutdownInput() {
        this.f269a.shutdownInput();
    }

    public final void shutdownOutput() {
        this.f269a.shutdownOutput();
    }

    public final void startHandshake() {
        try {
            this.f269a.startHandshake();
        } catch (Throwable e) {
            try {
                C1108c a = m256a(true);
                a.m665b();
                a.m668c();
                a.m674f();
                a.m663a(e);
                this.f270b.m786a(a, C1107a.SSL_SOCKET_START_HANDSHAKE);
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th) {
                dx.m777a(th);
            }
            throw e;
        }
    }

    public final String toString() {
        return this.f269a.toString();
    }
}
