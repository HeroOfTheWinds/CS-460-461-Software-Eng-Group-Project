package com.upsight.mediation.vast.util;

import android.text.TextUtils;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTools {
    private static final String TAG;

    /* renamed from: com.upsight.mediation.vast.util.HttpTools.1 */
    static final class C10351 extends Thread {
        final /* synthetic */ VASTPlayer val$currentPlayer;
        final /* synthetic */ String val$type;
        final /* synthetic */ String val$url;

        C10351(String str, String str2, VASTPlayer vASTPlayer) {
            this.val$url = str;
            this.val$type = str2;
            this.val$currentPlayer = vASTPlayer;
        }

        public void run() {
            Exception e;
            Throwable th;
            HttpURLConnection httpURLConnection = null;
            try {
                FuseLog.m239v(HttpTools.TAG, "connection to URL:" + this.val$url);
                URL url = new URL(this.val$url);
                HttpURLConnection.setFollowRedirects(true);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                try {
                    httpURLConnection2.setConnectTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
                    httpURLConnection2.setRequestProperty("Connection", "close");
                    httpURLConnection2.setRequestMethod("GET");
                    int responseCode = httpURLConnection2.getResponseCode();
                    FuseLog.m239v(HttpTools.TAG, "response code:" + responseCode + ", for URL:" + this.val$url);
                    if (responseCode < 200 && responseCode > 226 && this.val$type.equals("impression")) {
                        this.val$currentPlayer.listener.vastError(VASTPlayer.ERROR_UNDEFINED);
                    }
                    if (httpURLConnection2 != null) {
                        try {
                            httpURLConnection2.disconnect();
                        } catch (Exception e2) {
                            FuseLog.m240w(HttpTools.TAG, e2.toString());
                        }
                    }
                } catch (Exception e3) {
                    Exception exception = e3;
                    httpURLConnection = httpURLConnection2;
                    e2 = exception;
                    try {
                        FuseLog.m240w(HttpTools.TAG, this.val$url + ": " + e2.getMessage() + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + e2.toString());
                        if (this.val$type.equals("impression")) {
                            this.val$currentPlayer.listener.vastError(VASTPlayer.ERROR_UNDEFINED);
                        }
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e22) {
                                FuseLog.m240w(HttpTools.TAG, e22.toString());
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e32) {
                                FuseLog.m240w(HttpTools.TAG, e32.toString());
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    httpURLConnection = httpURLConnection2;
                    th = th4;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e22 = e4;
                FuseLog.m240w(HttpTools.TAG, this.val$url + ": " + e22.getMessage() + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR + e22.toString());
                if (this.val$type.equals("impression")) {
                    this.val$currentPlayer.listener.vastError(VASTPlayer.ERROR_UNDEFINED);
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }

    static {
        TAG = HttpTools.class.getName();
    }

    public static void httpGetURL(String str, String str2, VASTPlayer vASTPlayer) {
        if (!TextUtils.isEmpty(str2)) {
            new C10351(str2, str, vASTPlayer).start();
        }
    }
}
