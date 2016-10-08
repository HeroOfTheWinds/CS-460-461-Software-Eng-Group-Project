package com.mopub.volley.toolbox;

import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Cache.Entry;
import com.mopub.volley.Network;
import com.mopub.volley.Request;
import com.mopub.volley.RetryPolicy;
import com.mopub.volley.ServerError;
import com.mopub.volley.VolleyError;
import com.mopub.volley.VolleyLog;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class BasicNetwork implements Network {
    protected static final boolean DEBUG;
    private static int DEFAULT_POOL_SIZE;
    private static int SLOW_REQUEST_THRESHOLD_MS;
    protected final HttpStack mHttpStack;
    protected final ByteArrayPool mPool;

    static {
        DEBUG = VolleyLog.DEBUG;
        SLOW_REQUEST_THRESHOLD_MS = CommonStatusCodes.AUTH_API_INVALID_CREDENTIALS;
        DEFAULT_POOL_SIZE = AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
    }

    public BasicNetwork(HttpStack httpStack) {
        this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
    }

    public BasicNetwork(HttpStack httpStack, ByteArrayPool byteArrayPool) {
        this.mHttpStack = httpStack;
        this.mPool = byteArrayPool;
    }

    private void addCacheHeaders(Map<String, String> map, Entry entry) {
        if (entry != null) {
            if (entry.etag != null) {
                map.put("If-None-Match", entry.etag);
            }
            if (entry.serverDate > 0) {
                map.put("If-Modified-Since", DateUtils.formatDate(new Date(entry.serverDate)));
            }
        }
    }

    private static void attemptRetryOnException(String str, Request<?> request, VolleyError volleyError) throws VolleyError {
        RetryPolicy retryPolicy = request.getRetryPolicy();
        int timeoutMs = request.getTimeoutMs();
        try {
            retryPolicy.retry(volleyError);
            request.addMarker(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(timeoutMs)}));
        } catch (VolleyError e) {
            request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(timeoutMs)}));
            throw e;
        }
    }

    protected static Map<String, String> convertHeaders(Header[] headerArr) {
        Map<String, String> treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private byte[] entityToBytes(HttpEntity httpEntity) throws IOException, ServerError {
        PoolingByteArrayOutputStream poolingByteArrayOutputStream = new PoolingByteArrayOutputStream(this.mPool, (int) httpEntity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new ServerError();
            }
            bArr = this.mPool.getBuf(Place.TYPE_SUBLOCALITY_LEVEL_2);
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                poolingByteArrayOutputStream.write(bArr, 0, read);
            }
            byte[] toByteArray = poolingByteArrayOutputStream.toByteArray();
            return toByteArray;
        } finally {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                VolleyLog.m45v("Error occured when calling consumingContent", new Object[0]);
            }
            this.mPool.returnBuf(bArr);
            poolingByteArrayOutputStream.close();
        }
    }

    private void logSlowRequests(long j, Request<?> request, byte[] bArr, StatusLine statusLine) {
        if (DEBUG || j > ((long) SLOW_REQUEST_THRESHOLD_MS)) {
            Integer valueOf = bArr != null ? Integer.valueOf(bArr.length) : "null";
            VolleyLog.m42d("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", request, Long.valueOf(j), valueOf, Integer.valueOf(statusLine.getStatusCode()), Integer.valueOf(request.getRetryPolicy().getCurrentRetryCount()));
        }
    }

    protected void logError(String str, String str2, long j) {
        VolleyLog.m45v("HTTP ERROR(%s) %d ms to fetch %s", str, Long.valueOf(SystemClock.elapsedRealtime() - j), str2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.mopub.volley.NetworkResponse performRequest(com.mopub.volley.Request<?> r19) throws com.mopub.volley.VolleyError {
        /*
        r18 = this;
        r16 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r3 = 0;
        r6 = java.util.Collections.emptyMap();
        r2 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r4 = r19.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r0 = r18;
        r0.addCacheHeaders(r2, r4);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r0 = r18;
        r4 = r0.mHttpStack;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r0 = r19;
        r14 = r4.performRequest(r0, r2);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x00dd }
        r12 = r14.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r4 = r12.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r2 = r14.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r6 = convertHeaders(r2);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r4 != r2) goto L_0x0064;
    L_0x0035:
        r2 = r19.getCacheEntry();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        if (r2 != 0) goto L_0x004b;
    L_0x003b:
        r3 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r4 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r5 = 0;
        r7 = 1;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
    L_0x004a:
        return r3;
    L_0x004b:
        r3 = r2.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r3.putAll(r6);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r7 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r8 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r9 = r2.data;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r10 = r2.responseHeaders;	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r11 = 1;
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r12 = r2 - r16;
        r7.<init>(r8, r9, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r3 = r7;
        goto L_0x004a;
    L_0x0064:
        r2 = r14.getEntity();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        if (r2 == 0) goto L_0x009e;
    L_0x006a:
        r2 = r14.getEntity();	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        r0 = r18;
        r11 = r0.entityToBytes(r2);	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
    L_0x0074:
        r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        r8 = r2 - r16;
        r7 = r18;
        r10 = r19;
        r7.logSlowRequests(r8, r10, r11, r12);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r4 < r2) goto L_0x0089;
    L_0x0085:
        r2 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r4 <= r2) goto L_0x00a2;
    L_0x0089:
        r2 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        r2.<init>();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        throw r2;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
    L_0x008f:
        r2 = move-exception;
    L_0x0090:
        r2 = "socket";
        r3 = new com.mopub.volley.TimeoutError;
        r3.<init>();
        r0 = r19;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x009e:
        r2 = 0;
        r11 = new byte[r2];	 Catch:{ SocketTimeoutException -> 0x013a, ConnectTimeoutException -> 0x0143, MalformedURLException -> 0x0140, IOException -> 0x013d }
        goto L_0x0074;
    L_0x00a2:
        r3 = new com.mopub.volley.NetworkResponse;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        r8 = r8 - r16;
        r5 = r11;
        r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00b0, MalformedURLException -> 0x00bf, IOException -> 0x0137 }
        goto L_0x004a;
    L_0x00b0:
        r2 = move-exception;
    L_0x00b1:
        r2 = "connection";
        r3 = new com.mopub.volley.TimeoutError;
        r3.<init>();
        r0 = r19;
        attemptRetryOnException(r2, r0, r3);
        goto L_0x0004;
    L_0x00bf:
        r2 = move-exception;
    L_0x00c0:
        r3 = new java.lang.RuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Bad URL ";
        r4 = r4.append(r5);
        r5 = r19.getUrl();
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4, r2);
        throw r3;
    L_0x00dd:
        r2 = move-exception;
    L_0x00de:
        r5 = 0;
        r14 = r3;
    L_0x00e0:
        if (r14 == 0) goto L_0x0124;
    L_0x00e2:
        r2 = r14.getStatusLine();
        r4 = r2.getStatusCode();
        r2 = "Unexpected response code %d for %s";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r7 = 0;
        r8 = java.lang.Integer.valueOf(r4);
        r3[r7] = r8;
        r7 = 1;
        r8 = r19.getUrl();
        r3[r7] = r8;
        com.mopub.volley.VolleyLog.m43e(r2, r3);
        if (r5 == 0) goto L_0x0130;
    L_0x0102:
        r3 = new com.mopub.volley.NetworkResponse;
        r7 = 0;
        r8 = android.os.SystemClock.elapsedRealtime();
        r8 = r8 - r16;
        r3.<init>(r4, r5, r6, r7, r8);
        r2 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r4 == r2) goto L_0x0116;
    L_0x0112:
        r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r4 != r2) goto L_0x012a;
    L_0x0116:
        r2 = "auth";
        r4 = new com.mopub.volley.AuthFailureError;
        r4.<init>(r3);
        r0 = r19;
        attemptRetryOnException(r2, r0, r4);
        goto L_0x0004;
    L_0x0124:
        r3 = new com.mopub.volley.NoConnectionError;
        r3.<init>(r2);
        throw r3;
    L_0x012a:
        r2 = new com.mopub.volley.ServerError;
        r2.<init>(r3);
        throw r2;
    L_0x0130:
        r2 = new com.mopub.volley.NetworkError;
        r3 = 0;
        r2.<init>(r3);
        throw r2;
    L_0x0137:
        r2 = move-exception;
        r5 = r11;
        goto L_0x00e0;
    L_0x013a:
        r2 = move-exception;
        goto L_0x0090;
    L_0x013d:
        r2 = move-exception;
        r3 = r14;
        goto L_0x00de;
    L_0x0140:
        r2 = move-exception;
        goto L_0x00c0;
    L_0x0143:
        r2 = move-exception;
        goto L_0x00b1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.volley.toolbox.BasicNetwork.performRequest(com.mopub.volley.Request):com.mopub.volley.NetworkResponse");
    }
}
