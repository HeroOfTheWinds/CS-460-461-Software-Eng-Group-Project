package crittercism.android;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/* renamed from: crittercism.android.p */
public final class C1142p extends C1139n {

    /* renamed from: crittercism.android.p.1 */
    final class C11411 implements Comparator {
        final /* synthetic */ C1142p f839a;

        C11411(C1142p c1142p) {
            this.f839a = c1142p;
        }

        public final /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
            String str = (String) obj;
            String str2 = (String) obj2;
            return str == str2 ? 0 : str == null ? -1 : str2 == null ? 1 : String.CASE_INSENSITIVE_ORDER.compare(str, str2);
        }
    }

    public C1142p(Map map) {
        super(map);
        Map treeMap = new TreeMap(new C11411(this));
        treeMap.putAll(map);
        this.f837a = treeMap;
    }
}
