package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.VolleyLog;
import com.upsight.mediation.vast.activity.PlayerControls;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import spacemadness.com.lunarconsole.BuildConfig;

public class DiskBasedCache implements Cache {
    private static final int CACHE_MAGIC = 538247942;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    static class CacheHeader {
        public String etag;
        public String key;
        public long lastModified;
        public Map<String, String> responseHeaders;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;

        private CacheHeader() {
        }

        public CacheHeader(String str, Entry entry) {
            this.key = str;
            this.size = (long) entry.data.length;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.lastModified = entry.lastModified;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
            this.responseHeaders = entry.responseHeaders;
        }

        public static CacheHeader readHeader(InputStream inputStream) throws IOException {
            CacheHeader cacheHeader = new CacheHeader();
            if (DiskBasedCache.readInt(inputStream) != DiskBasedCache.CACHE_MAGIC) {
                throw new IOException();
            }
            cacheHeader.key = DiskBasedCache.readString(inputStream);
            cacheHeader.etag = DiskBasedCache.readString(inputStream);
            if (cacheHeader.etag.equals(BuildConfig.FLAVOR)) {
                cacheHeader.etag = null;
            }
            cacheHeader.serverDate = DiskBasedCache.readLong(inputStream);
            cacheHeader.lastModified = DiskBasedCache.readLong(inputStream);
            cacheHeader.ttl = DiskBasedCache.readLong(inputStream);
            cacheHeader.softTtl = DiskBasedCache.readLong(inputStream);
            cacheHeader.responseHeaders = DiskBasedCache.readStringStringMap(inputStream);
            return cacheHeader;
        }

        public Entry toCacheEntry(byte[] bArr) {
            Entry entry = new Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.lastModified = this.lastModified;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = this.responseHeaders;
            return entry;
        }

        public boolean writeHeader(OutputStream outputStream) {
            try {
                DiskBasedCache.writeInt(outputStream, DiskBasedCache.CACHE_MAGIC);
                DiskBasedCache.writeString(outputStream, this.key);
                DiskBasedCache.writeString(outputStream, this.etag == null ? BuildConfig.FLAVOR : this.etag);
                DiskBasedCache.writeLong(outputStream, this.serverDate);
                DiskBasedCache.writeLong(outputStream, this.lastModified);
                DiskBasedCache.writeLong(outputStream, this.ttl);
                DiskBasedCache.writeLong(outputStream, this.softTtl);
                DiskBasedCache.writeStringStringMap(this.responseHeaders, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.m16d("%s", e.toString());
                return false;
            }
        }
    }

    private static class CountingInputStream extends FilterInputStream {
        private int bytesRead;

        private CountingInputStream(InputStream inputStream) {
            super(inputStream);
            this.bytesRead = 0;
        }

        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += read;
            }
            return read;
        }
    }

    public DiskBasedCache(File file) {
        this(file, DEFAULT_DISK_USAGE_BYTES);
    }

    public DiskBasedCache(File file, int i) {
        this.mEntries = new LinkedHashMap(16, PlayerControls.DOWN_STATE, true);
        this.mTotalSize = 0;
        this.mRootDirectory = file;
        this.mMaxCacheSizeInBytes = i;
    }

    private String getFilenameForKey(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(String.valueOf(str.substring(0, length).hashCode()));
        String valueOf2 = String.valueOf(String.valueOf(str.substring(length).hashCode()));
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    private void pruneIfNeeded(int i) {
        if (this.mTotalSize + ((long) i) >= ((long) this.mMaxCacheSizeInBytes)) {
            int i2;
            if (VolleyLog.DEBUG) {
                VolleyLog.m19v("Pruning old cache entries.", new Object[0]);
            }
            long j = this.mTotalSize;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator it = this.mEntries.entrySet().iterator();
            int i3 = 0;
            while (it.hasNext()) {
                CacheHeader cacheHeader = (CacheHeader) ((Map.Entry) it.next()).getValue();
                if (getFileForKey(cacheHeader.key).delete()) {
                    this.mTotalSize -= cacheHeader.size;
                } else {
                    VolleyLog.m16d("Could not delete cache entry for key=%s, filename=%s", cacheHeader.key, getFilenameForKey(cacheHeader.key));
                }
                it.remove();
                i2 = i3 + 1;
                if (((float) (this.mTotalSize + ((long) i))) < ((float) this.mMaxCacheSizeInBytes) * HYSTERESIS_FACTOR) {
                    break;
                }
                i3 = i2;
            }
            i2 = i3;
            if (VolleyLog.DEBUG) {
                VolleyLog.m19v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.mTotalSize - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    private void putEntry(String str, CacheHeader cacheHeader) {
        if (this.mEntries.containsKey(str)) {
            CacheHeader cacheHeader2 = (CacheHeader) this.mEntries.get(str);
            this.mTotalSize = (cacheHeader.size - cacheHeader2.size) + this.mTotalSize;
        } else {
            this.mTotalSize += cacheHeader.size;
        }
        this.mEntries.put(str, cacheHeader);
    }

    private static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static int readInt(InputStream inputStream) throws IOException {
        return ((((read(inputStream) << 0) | 0) | (read(inputStream) << 8)) | (read(inputStream) << 16)) | (read(inputStream) << 24);
    }

    static long readLong(InputStream inputStream) throws IOException {
        return (((((((0 | ((((long) read(inputStream)) & 255) << null)) | ((((long) read(inputStream)) & 255) << 8)) | ((((long) read(inputStream)) & 255) << 16)) | ((((long) read(inputStream)) & 255) << 24)) | ((((long) read(inputStream)) & 255) << 32)) | ((((long) read(inputStream)) & 255) << 40)) | ((((long) read(inputStream)) & 255) << 48)) | ((((long) read(inputStream)) & 255) << 56);
    }

    static String readString(InputStream inputStream) throws IOException {
        return new String(streamToBytes(inputStream, (int) readLong(inputStream)), "UTF-8");
    }

    static Map<String, String> readStringStringMap(InputStream inputStream) throws IOException {
        int readInt = readInt(inputStream);
        Map<String, String> emptyMap = readInt == 0 ? Collections.emptyMap() : new HashMap(readInt);
        for (int i = 0; i < readInt; i++) {
            emptyMap.put(readString(inputStream).intern(), readString(inputStream).intern());
        }
        return emptyMap;
    }

    private void removeEntry(String str) {
        CacheHeader cacheHeader = (CacheHeader) this.mEntries.get(str);
        if (cacheHeader != null) {
            this.mTotalSize -= cacheHeader.size;
            this.mEntries.remove(str);
        }
    }

    private static byte[] streamToBytes(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
    }

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & MotionEventCompat.ACTION_MASK);
        outputStream.write((i >> 8) & MotionEventCompat.ACTION_MASK);
        outputStream.write((i >> 16) & MotionEventCompat.ACTION_MASK);
        outputStream.write((i >> 24) & MotionEventCompat.ACTION_MASK);
    }

    static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> null)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        writeLong(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    static void writeStringStringMap(Map<String, String> map, OutputStream outputStream) throws IOException {
        if (map != null) {
            writeInt(outputStream, map.size());
            for (Map.Entry entry : map.entrySet()) {
                writeString(outputStream, (String) entry.getKey());
                writeString(outputStream, (String) entry.getValue());
            }
            return;
        }
        writeInt(outputStream, 0);
    }

    public void clear() {
        synchronized (this) {
            File[] listFiles = this.mRootDirectory.listFiles();
            if (listFiles != null) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
            this.mEntries.clear();
            this.mTotalSize = 0;
            VolleyLog.m16d("Cache cleared.", new Object[0]);
        }
    }

    public Entry get(String str) {
        Entry entry;
        CountingInputStream countingInputStream;
        IOException e;
        Throwable th;
        synchronized (this) {
            CacheHeader cacheHeader = (CacheHeader) this.mEntries.get(str);
            if (cacheHeader == null) {
                entry = null;
            } else {
                File fileForKey = getFileForKey(str);
                try {
                    countingInputStream = new CountingInputStream(null);
                } catch (IOException e2) {
                    e = e2;
                    countingInputStream = null;
                    try {
                        VolleyLog.m16d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
                        remove(str);
                        if (countingInputStream == null) {
                            entry = null;
                        } else {
                            try {
                                countingInputStream.close();
                                entry = null;
                            } catch (IOException e3) {
                                entry = null;
                            }
                        }
                        return entry;
                    } catch (Throwable th2) {
                        th = th2;
                        if (countingInputStream != null) {
                            try {
                                countingInputStream.close();
                            } catch (IOException e4) {
                                entry = null;
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    countingInputStream = null;
                    if (countingInputStream != null) {
                        countingInputStream.close();
                    }
                    throw th;
                }
                try {
                    CacheHeader.readHeader(countingInputStream);
                    entry = cacheHeader.toCacheEntry(streamToBytes(countingInputStream, (int) (fileForKey.length() - ((long) countingInputStream.bytesRead))));
                    if (countingInputStream != null) {
                        try {
                            countingInputStream.close();
                        } catch (IOException e5) {
                            entry = null;
                        }
                    }
                } catch (IOException e6) {
                    e = e6;
                    VolleyLog.m16d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
                    remove(str);
                    if (countingInputStream == null) {
                        countingInputStream.close();
                        entry = null;
                    } else {
                        entry = null;
                    }
                    return entry;
                } catch (Throwable th4) {
                    th = th4;
                    if (countingInputStream != null) {
                        countingInputStream.close();
                    }
                    throw th;
                }
            }
        }
        return entry;
    }

    public File getFileForKey(String str) {
        return new File(this.mRootDirectory, getFilenameForKey(str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initialize() {
        /*
        r10 = this;
        r1 = 0;
        r0 = 0;
        monitor-enter(r10);
        r2 = r10.mRootDirectory;	 Catch:{ all -> 0x006b }
        r2 = r2.exists();	 Catch:{ all -> 0x006b }
        if (r2 != 0) goto L_0x0026;
    L_0x000b:
        r0 = r10.mRootDirectory;	 Catch:{ all -> 0x006b }
        r0 = r0.mkdirs();	 Catch:{ all -> 0x006b }
        if (r0 != 0) goto L_0x0024;
    L_0x0013:
        r0 = "Unable to create cache dir %s";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x006b }
        r2 = 0;
        r3 = r10.mRootDirectory;	 Catch:{ all -> 0x006b }
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x006b }
        r1[r2] = r3;	 Catch:{ all -> 0x006b }
        com.android.volley.VolleyLog.m17e(r0, r1);	 Catch:{ all -> 0x006b }
    L_0x0024:
        monitor-exit(r10);
        return;
    L_0x0026:
        r2 = r10.mRootDirectory;	 Catch:{ all -> 0x006b }
        r3 = r2.listFiles();	 Catch:{ all -> 0x006b }
        if (r3 == 0) goto L_0x0024;
    L_0x002e:
        r4 = r3.length;	 Catch:{ all -> 0x006b }
    L_0x002f:
        if (r0 >= r4) goto L_0x0024;
    L_0x0031:
        r5 = r3[r0];
        r2 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r6 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r6.<init>(r5);	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r2.<init>(r6);	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r6 = com.android.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r2);	 Catch:{ IOException -> 0x0072, all -> 0x0074 }
        r8 = r5.length();	 Catch:{ IOException -> 0x0072, all -> 0x0074 }
        r6.size = r8;	 Catch:{ IOException -> 0x0072, all -> 0x0074 }
        r7 = r6.key;	 Catch:{ IOException -> 0x0072, all -> 0x0074 }
        r10.putEntry(r7, r6);	 Catch:{ IOException -> 0x0072, all -> 0x0074 }
        if (r2 == 0) goto L_0x0051;
    L_0x004e:
        r2.close();	 Catch:{ IOException -> 0x006e }
    L_0x0051:
        r0 = r0 + 1;
        goto L_0x002f;
    L_0x0054:
        r2 = move-exception;
        r2 = r1;
    L_0x0056:
        if (r5 == 0) goto L_0x005b;
    L_0x0058:
        r5.delete();	 Catch:{ all -> 0x0076 }
    L_0x005b:
        if (r2 == 0) goto L_0x0051;
    L_0x005d:
        r2.close();	 Catch:{ IOException -> 0x0061 }
        goto L_0x0051;
    L_0x0061:
        r2 = move-exception;
        goto L_0x0051;
    L_0x0063:
        r0 = move-exception;
    L_0x0064:
        r2 = r1;
    L_0x0065:
        if (r2 == 0) goto L_0x006a;
    L_0x0067:
        r2.close();	 Catch:{ IOException -> 0x0070 }
    L_0x006a:
        throw r0;	 Catch:{ all -> 0x006b }
    L_0x006b:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x006e:
        r2 = move-exception;
        goto L_0x0051;
    L_0x0070:
        r1 = move-exception;
        goto L_0x006a;
    L_0x0072:
        r6 = move-exception;
        goto L_0x0056;
    L_0x0074:
        r0 = move-exception;
        goto L_0x0065;
    L_0x0076:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.volley.toolbox.DiskBasedCache.initialize():void");
    }

    public void invalidate(String str, boolean z) {
        synchronized (this) {
            Entry entry = get(str);
            if (entry != null) {
                entry.softTtl = 0;
                if (z) {
                    entry.ttl = 0;
                }
                put(str, entry);
            }
        }
    }

    public void put(String str, Entry entry) {
        synchronized (this) {
            pruneIfNeeded(entry.data.length);
            File fileForKey = getFileForKey(str);
            try {
                OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileForKey));
                CacheHeader cacheHeader = new CacheHeader(str, entry);
                if (cacheHeader.writeHeader(bufferedOutputStream)) {
                    bufferedOutputStream.write(entry.data);
                    bufferedOutputStream.close();
                    putEntry(str, cacheHeader);
                } else {
                    bufferedOutputStream.close();
                    VolleyLog.m16d("Failed to write header for %s", fileForKey.getAbsolutePath());
                    throw new IOException();
                }
            } catch (IOException e) {
                if (!fileForKey.delete()) {
                    VolleyLog.m16d("Could not clean up file %s", fileForKey.getAbsolutePath());
                }
            }
        }
    }

    public void remove(String str) {
        synchronized (this) {
            boolean delete = getFileForKey(str).delete();
            removeEntry(str);
            if (!delete) {
                VolleyLog.m16d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
            }
        }
    }
}
