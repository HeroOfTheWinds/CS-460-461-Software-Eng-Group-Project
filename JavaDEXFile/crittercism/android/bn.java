package crittercism.android;

import java.util.HashMap;
import java.util.Map;

public final class bn {
    private static final Map f494a;
    private String f495b;
    private String f496c;
    private String f497d;
    private String f498e;

    /* renamed from: crittercism.android.bn.a */
    public static final class C1078a extends Exception {
        public C1078a(String str) {
            super(str);
        }
    }

    static {
        Map hashMap = new HashMap();
        f494a = hashMap;
        hashMap.put("00555300", "crittercism.com");
        f494a.put("00555304", "crit-ci.com");
        f494a.put("00555305", "crit-staging.com");
        f494a.put("00444503", "eu.crittercism.com");
    }

    public bn(String str) {
        if (str == null) {
            throw new C1078a("Given null appId");
        } else if (!str.matches("[0-9a-fA-F]+")) {
            throw new C1078a("Invalid appId: '" + str + "'. AppId must be hexadecimal characters");
        } else if (str.length() == 24 || str.length() == 40) {
            Object obj = null;
            if (str.length() == 24) {
                obj = "00555300";
            } else if (str.length() == 40) {
                obj = str.substring(str.length() - 8);
            }
            String str2 = (String) f494a.get(obj);
            if (str2 == null) {
                throw new C1078a("Invalid appId: '" + str + "'. Invalid app locator code");
            }
            this.f495b = System.getProperty("com.crittercism.apmUrl", "https://apm." + str2);
            this.f496c = System.getProperty("com.crittercism.apiUrl", "https://api." + str2);
            this.f497d = System.getProperty("com.crittercism.txnUrl", "https://txn.ingest." + str2);
            this.f498e = System.getProperty("com.crittercism.appLoadUrl", "https://appload.ingest." + str2);
        } else {
            throw new C1078a("Invalid appId: '" + str + "'. AppId must be either 24 or 40 characters");
        }
    }

    public final String m555a() {
        return this.f496c;
    }

    public final String m556b() {
        return this.f495b;
    }

    public final String m557c() {
        return this.f498e;
    }

    public final String m558d() {
        return this.f497d;
    }
}
