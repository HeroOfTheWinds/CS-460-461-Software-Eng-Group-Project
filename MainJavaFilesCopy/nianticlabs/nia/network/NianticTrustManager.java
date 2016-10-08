package com.nianticlabs.nia.network;

import android.content.Context;
import com.nianticlabs.nia.contextservice.ContextService;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class NianticTrustManager extends ContextService implements X509TrustManager {
    public NianticTrustManager(Context context, long j) {
        super(context, j);
    }

    public static X509TrustManager getTrustManager(String str, KeyStore keyStore) {
        try {
            TrustManagerFactory instance = TrustManagerFactory.getInstance(str);
            instance.init(keyStore);
            for (TrustManager trustManager : instance.getTrustManagers()) {
                if (trustManager != null) {
                    if (trustManager instanceof X509TrustManager) {
                        return (X509TrustManager) trustManager;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyStoreException e2) {
        }
        return null;
    }

    public static X509TrustManager getTrustManager(KeyStore keyStore) {
        return getTrustManager(TrustManagerFactory.getDefaultAlgorithm(), keyStore);
    }

    private native void nativeCheckClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException;

    private native void nativeCheckServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException;

    private native X509Certificate[] nativeGetAcceptedIssuers();

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        synchronized (this.callbackLock) {
            nativeCheckServerTrusted(x509CertificateArr, str);
        }
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        synchronized (this.callbackLock) {
            nativeCheckServerTrusted(x509CertificateArr, str);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] nativeGetAcceptedIssuers;
        synchronized (this.callbackLock) {
            nativeGetAcceptedIssuers = nativeGetAcceptedIssuers();
        }
        return nativeGetAcceptedIssuers;
    }
}
