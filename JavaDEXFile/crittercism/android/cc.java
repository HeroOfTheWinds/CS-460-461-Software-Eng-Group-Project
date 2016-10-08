package crittercism.android;

import android.os.Build.VERSION;
import org.json.JSONArray;

public final class cc implements cb {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.json.JSONArray m679a(java.io.InputStream r4) {
        /*
        r0 = new org.json.JSONArray;
        r0.<init>();
        r2 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r4);
        r2.<init>(r1);
        r1 = 0;
    L_0x0010:
        r3 = r2.readLine();	 Catch:{ IOException -> 0x0028 }
        if (r3 == 0) goto L_0x001f;
    L_0x0016:
        r0.put(r3);	 Catch:{ IOException -> 0x0028 }
        r1 = r1 + 1;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r1 <= r3) goto L_0x0010;
    L_0x001f:
        r2.close();	 Catch:{ IOException -> 0x0023 }
    L_0x0022:
        return r0;
    L_0x0023:
        r1 = move-exception;
        crittercism.android.dx.m773a();
        goto L_0x0022;
    L_0x0028:
        r1 = move-exception;
        crittercism.android.dx.m773a();	 Catch:{ all -> 0x0035 }
        r2.close();	 Catch:{ IOException -> 0x0030 }
        goto L_0x0022;
    L_0x0030:
        r1 = move-exception;
        crittercism.android.dx.m773a();
        goto L_0x0022;
    L_0x0035:
        r0 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x003a }
    L_0x0039:
        throw r0;
    L_0x003a:
        r1 = move-exception;
        crittercism.android.dx.m773a();
        goto L_0x0039;
        */
        throw new UnsupportedOperationException("Method not decompiled: crittercism.android.cc.a(java.io.InputStream):org.json.JSONArray");
    }

    public final JSONArray m680a() {
        Process process = null;
        JSONArray jSONArray = new JSONArray();
        if (VERSION.SDK_INT < 10) {
            jSONArray.put("Logcat data is not collected for Android APIs before 10 (Gingerbread)");
            jSONArray.put("API level is " + VERSION.SDK_INT + "(" + VERSION.CODENAME + ")");
        } else {
            try {
                process = new ProcessBuilder(new String[]{"logcat", "-t", Integer.toString(100), "-v", "time"}).redirectErrorStream(true).start();
                jSONArray = m679a(process.getInputStream());
                if (process != null) {
                    process.destroy();
                }
            } catch (Throwable e) {
                dx.m780b("Unable to collect logcat data", e);
                jSONArray.put("Unable to collect logcat data due to a(n) " + e.getClass().getName());
                String message = e.getMessage();
                if (message != null) {
                    jSONArray.put(message);
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Throwable th) {
                if (process != null) {
                    process.destroy();
                }
            }
        }
        return jSONArray;
    }
}
