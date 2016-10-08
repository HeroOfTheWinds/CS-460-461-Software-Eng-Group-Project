package com.nianticlabs.nia.network;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;

public class NiaNet {
    private static final int CHUNK_SIZE = 32768;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_OK = 200;
    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final int METHOD_DELETE = 4;
    private static final int METHOD_GET = 0;
    private static final int METHOD_HEAD = 1;
    private static final int METHOD_OPTIONS = 5;
    private static final int METHOD_POST = 2;
    private static final int METHOD_PUT = 3;
    private static final int METHOD_TRACE = 6;
    private static final int NETWORK_TIMEOUT_MS = 15000;
    private static final int POOL_THREAD_NUM = 6;
    private static final String TAG = "NiaNet";
    private static final ThreadPoolExecutor executor;
    private static Set<Integer> pendingRequestIds;
    static ThreadLocal<ByteBuffer> readBuffer;
    private static final ThreadLocal<byte[]> threadChunk;

    /* renamed from: com.nianticlabs.nia.network.NiaNet.1 */
    static final class C07761 extends ThreadLocal<byte[]> {
        C07761() {
        }

        protected byte[] initialValue() {
            return new byte[NiaNet.CHUNK_SIZE];
        }
    }

    /* renamed from: com.nianticlabs.nia.network.NiaNet.2 */
    static final class C07772 extends ThreadLocal<ByteBuffer> {
        C07772() {
        }

        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(NiaNet.CHUNK_SIZE);
        }
    }

    /* renamed from: com.nianticlabs.nia.network.NiaNet.3 */
    static final class C07783 implements Runnable {
        final /* synthetic */ ByteBuffer val$body;
        final /* synthetic */ int val$bodyOffset;
        final /* synthetic */ int val$bodySize;
        final /* synthetic */ String val$headers;
        final /* synthetic */ int val$method;
        final /* synthetic */ long val$object;
        final /* synthetic */ int val$request_id;
        final /* synthetic */ String val$url;

        C07783(long j, int i, String str, int i2, String str2, ByteBuffer byteBuffer, int i3, int i4) {
            this.val$object = j;
            this.val$request_id = i;
            this.val$url = str;
            this.val$method = i2;
            this.val$headers = str2;
            this.val$body = byteBuffer;
            this.val$bodyOffset = i3;
            this.val$bodySize = i4;
        }

        public void run() {
            NiaNet.doSyncRequest(this.val$object, this.val$request_id, this.val$url, this.val$method, this.val$headers, this.val$body, this.val$bodyOffset, this.val$bodySize);
        }
    }

    static {
        executor = new ThreadPoolExecutor(POOL_THREAD_NUM, 12, 5, TimeUnit.SECONDS, new LinkedBlockingQueue());
        pendingRequestIds = new HashSet();
        threadChunk = new C07761();
        readBuffer = new C07772();
    }

    private NiaNet() {
    }

    public static void cancel(int i) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.remove(Integer.valueOf(i));
        }
    }

    private static void doSyncRequest(long j, int i, String str, int i2, String str2, ByteBuffer byteBuffer, int i3, int i4) {
        int readDataSteam;
        int i5;
        IOException iOException;
        Throwable th;
        synchronized (pendingRequestIds) {
            if ((!pendingRequestIds.contains(Integer.valueOf(i)) ? METHOD_HEAD : null) != null) {
                return;
            }
            String str3;
            int i6;
            pendingRequestIds.remove(Integer.valueOf(i));
            HttpURLConnection httpURLConnection = null;
            int i7 = HTTP_BAD_REQUEST;
            String str4 = null;
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
                OutputStream outputStream;
                try {
                    setHeaders(httpURLConnection2, str2);
                    httpURLConnection2.setConnectTimeout(NETWORK_TIMEOUT_MS);
                    httpURLConnection2.setRequestProperty("Connection", "Keep-Alive");
                    HttpURLConnection.setFollowRedirects(false);
                    nativeSetupConnection(j, httpURLConnection2);
                    httpURLConnection2.setRequestMethod(getMethodString(i2));
                    if (byteBuffer != null && i4 > 0) {
                        httpURLConnection2.setDoOutput(true);
                        outputStream = httpURLConnection2.getOutputStream();
                        if (byteBuffer.hasArray()) {
                            outputStream.write(byteBuffer.array(), byteBuffer.arrayOffset() + i3, i4);
                        } else {
                            byte[] bArr = (byte[]) threadChunk.get();
                            while (byteBuffer.hasRemaining()) {
                                int min = Math.min(byteBuffer.remaining(), bArr.length);
                                byteBuffer.get(bArr, METHOD_GET, min);
                                outputStream.write(bArr, METHOD_GET, min);
                            }
                        }
                        outputStream.close();
                    }
                    i7 = httpURLConnection2.getResponseCode();
                    str4 = joinHeaders(httpURLConnection2);
                    readDataSteam = readDataSteam(httpURLConnection2);
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                        i5 = i7;
                        str3 = str4;
                        i6 = i5;
                        if (readDataSteam <= 0) {
                            nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
                        } else {
                            nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
                        }
                    }
                } catch (IOException e) {
                    IOException iOException2 = e;
                    httpURLConnection = httpURLConnection2;
                    iOException = iOException2;
                    try {
                        Log.e(TAG, "Network op failed: " + iOException.getMessage());
                        readDataSteam = METHOD_GET;
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                            str3 = null;
                            i6 = HTTP_BAD_REQUEST;
                            if (readDataSteam <= 0) {
                                nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
                            } else {
                                nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
                            }
                        }
                        i5 = i7;
                        str3 = str4;
                        i6 = i5;
                        if (readDataSteam <= 0) {
                            nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
                        } else {
                            nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
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
            } catch (IOException e2) {
                iOException = e2;
                Log.e(TAG, "Network op failed: " + iOException.getMessage());
                readDataSteam = METHOD_GET;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    str3 = null;
                    i6 = HTTP_BAD_REQUEST;
                    if (readDataSteam <= 0) {
                        nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
                    } else {
                        nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
                    }
                }
                i5 = i7;
                str3 = str4;
                i6 = i5;
                if (readDataSteam <= 0) {
                    nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
                } else {
                    nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
                }
            } catch (Throwable th5) {
                th = th5;
                httpURLConnection = null;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw th;
            }
            i5 = i7;
            str3 = str4;
            i6 = i5;
            if (readDataSteam <= 0) {
                nativeCallback(j, i6, str3, null, METHOD_GET, METHOD_GET);
            } else {
                nativeCallback(j, i6, str3, (ByteBuffer) readBuffer.get(), METHOD_GET, readDataSteam);
            }
        }
    }

    private static String getMethodString(int i) {
        switch (i) {
            case METHOD_GET /*0*/:
                return "GET";
            case METHOD_HEAD /*1*/:
                return "HEAD";
            case METHOD_POST /*2*/:
                return "POST";
            case METHOD_PUT /*3*/:
                return "PUT";
            case METHOD_DELETE /*4*/:
                return "DELETE";
            default:
                Log.e(TAG, "Unsupported HTTP method " + i + ", using GET.");
                return "GET";
        }
    }

    private static String joinHeaders(HttpURLConnection httpURLConnection) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = METHOD_GET;
        while (true) {
            String headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
            if (headerFieldKey == null) {
                break;
            }
            String headerField = httpURLConnection.getHeaderField(i);
            if (headerField == null) {
                break;
            }
            stringBuilder.append(headerFieldKey);
            stringBuilder.append(": ");
            stringBuilder.append(headerField);
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            i += METHOD_HEAD;
        }
        return stringBuilder.length() == 0 ? null : stringBuilder.toString();
    }

    private static native void nativeCallback(long j, int i, String str, ByteBuffer byteBuffer, int i2, int i3);

    private static native void nativeSetupConnection(long j, HttpURLConnection httpURLConnection);

    private static long parseHttpDateTime(String str) throws ParseException {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").parse(str).getTime();
    }

    private static int readDataSteam(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        int i = HTTP_OK;
        InputStream inputStream = responseCode == HTTP_OK ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
        if (inputStream == null) {
            return METHOD_GET;
        }
        ByteBuffer byteBuffer = (ByteBuffer) readBuffer.get();
        try {
            Object array = byteBuffer.array();
            responseCode = byteBuffer.arrayOffset();
            void v = METHOD_HEAD;
            int i2 = responseCode;
            int i3 = responseCode;
            Object obj = array;
            i = i3;
            while (true) {
                int available = inputStream.available();
                if (obj.length <= available + i2) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(((available + i2) - i) * METHOD_POST);
                    int i4 = i2 - i;
                    i2 = allocateDirect.arrayOffset();
                    if (i4 > 0) {
                        System.arraycopy(obj, i, allocateDirect.array(), i2, i4);
                    }
                    i = i4 + i2;
                    obj = allocateDirect.array();
                    readBuffer.set(allocateDirect);
                    i3 = i2;
                    i2 = i;
                    i = i3;
                }
                available = inputStream.read(obj, i2, obj.length - i2);
                if (available >= 0) {
                    i2 += available;
                    continue;
                } else {
                    v = METHOD_GET;
                    continue;
                }
                if (v == null) {
                    break;
                }
            }
            return i2 - i;
        } finally {
            inputStream.close();
        }
    }

    public static void request(long j, int i, String str, int i2, String str2, ByteBuffer byteBuffer, int i3, int i4) {
        synchronized (pendingRequestIds) {
            pendingRequestIds.add(Integer.valueOf(i));
        }
        executor.execute(new C07783(j, i, str, i2, str2, byteBuffer, i3, i4));
    }

    private static void setHeaders(HttpURLConnection httpURLConnection, String str) {
        if (str != null && !str.isEmpty()) {
            int i = METHOD_GET;
            do {
                int indexOf = str.indexOf(10, i);
                if (indexOf < 0) {
                    indexOf = str.length();
                }
                int indexOf2 = str.indexOf(58, i);
                if (indexOf2 < 0) {
                    indexOf2 = str.length();
                }
                String substring = str.substring(i, indexOf2);
                String substring2 = str.substring(indexOf2 + METHOD_HEAD, indexOf);
                if (IF_MODIFIED_SINCE.equals(substring)) {
                    try {
                        httpURLConnection.setIfModifiedSince(parseHttpDateTime(substring2));
                    } catch (ParseException e) {
                        Log.e(TAG, "If-Modified-Since Date/Time parse failed. " + e.getMessage());
                    }
                } else {
                    httpURLConnection.setRequestProperty(substring, substring2);
                }
                i = indexOf + METHOD_HEAD;
            } while (i < str.length());
        }
    }
}
