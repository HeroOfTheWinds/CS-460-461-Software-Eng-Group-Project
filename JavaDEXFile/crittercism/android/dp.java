package crittercism.android;

import com.google.android.gms.common.GooglePlayServicesUtil;
import crittercism.android.C1122do.C1119a.C1118a;
import crittercism.android.C1122do.C1121b.C1120a;
import java.util.HashMap;
import java.util.Map;

public final class dp {
    private static Map f741a;

    static {
        Map hashMap = new HashMap();
        f741a = hashMap;
        hashMap.put("com.amazon.venezia", new C1118a());
        f741a.put(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, new C1120a());
    }

    public static dn m750a(String str) {
        return (str == null || !f741a.containsKey(str)) ? null : (dn) f741a.get(str);
    }
}
