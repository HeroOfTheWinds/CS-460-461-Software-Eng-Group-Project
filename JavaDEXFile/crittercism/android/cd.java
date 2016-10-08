package crittercism.android;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import crittercism.android.bx.C1081b;
import crittercism.android.bx.C1082c;
import crittercism.android.bx.C1083d;
import crittercism.android.bx.C1085f;
import crittercism.android.bx.C1087h;
import crittercism.android.bx.C1089j;
import crittercism.android.bx.C1090k;
import crittercism.android.bx.C1092m;
import crittercism.android.bx.C1094o;
import crittercism.android.bx.C1095p;
import crittercism.android.bx.C1097r;
import crittercism.android.bx.C1098s;
import crittercism.android.bx.C1102w;
import crittercism.android.bx.C1103x;
import crittercism.android.bx.C1105z;
import crittercism.android.bx.aa;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public final class cd implements ch {
    private JSONObject f590a;
    private JSONObject f591b;
    private JSONArray f592c;
    private JSONArray f593d;
    private File f594e;
    private String f595f;

    public cd(File file, bs bsVar, bs bsVar2, bs bsVar3, bs bsVar4) {
        file.exists();
        this.f595f = cg.f616a.m688a();
        this.f594e = file;
        this.f590a = new bu().m582a(new C1082c()).m582a(new C1081b()).m582a(new C1083d()).m582a(new C1085f()).m582a(new C1094o()).m582a(new C1095p()).m582a(new C1089j()).m582a(new C1087h()).m582a(new C1105z()).m582a(new aa()).m582a(new C1090k()).m582a(new C1097r()).m582a(new C1092m()).m582a(new C1098s()).m582a(new C1102w()).m582a(new C1103x()).m583a();
        Map hashMap = new HashMap();
        hashMap.put("crashed_session", new bo(bsVar).f499a);
        if (bsVar2.m578b() > 0) {
            hashMap.put("previous_session", new bo(bsVar2).f499a);
        }
        this.f591b = new JSONObject(hashMap);
        this.f592c = new bo(bsVar3).f499a;
        this.f593d = new bo(bsVar4).f499a;
    }

    public final void m681a(OutputStream outputStream) {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f590a);
        hashMap.put("breadcrumbs", this.f591b);
        hashMap.put("endpoints", this.f592c);
        hashMap.put("systemBreadcrumbs", this.f593d);
        Object obj = new byte[0];
        Object obj2 = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD];
        InputStream fileInputStream = new FileInputStream(this.f594e);
        while (true) {
            int read = fileInputStream.read(obj2);
            if (read != -1) {
                Object obj3 = new byte[(obj.length + read)];
                System.arraycopy(obj, 0, obj3, 0, obj.length);
                System.arraycopy(obj2, 0, obj3, obj.length, read);
                obj = obj3;
            } else {
                fileInputStream.close();
                Map hashMap2 = new HashMap();
                hashMap2.put("dmp_name", this.f594e.getName());
                hashMap2.put("dmp_file", cr.m695a(obj));
                hashMap.put("ndk_dmp_info", new JSONObject(hashMap2));
                outputStream.write(new JSONObject(hashMap).toString().getBytes());
                return;
            }
        }
    }

    public final String m682e() {
        return this.f595f;
    }
}
