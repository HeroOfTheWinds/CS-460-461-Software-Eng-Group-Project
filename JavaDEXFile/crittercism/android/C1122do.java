package crittercism.android;

/* renamed from: crittercism.android.do */
public final class C1122do {

    /* renamed from: crittercism.android.do.a */
    public static final class C1119a extends dm {
        private String f739a;

        /* renamed from: crittercism.android.do.a.a */
        public static final class C1118a implements dn {
            public final /* synthetic */ dm m746a(String str) {
                if (str != null) {
                    return new C1119a((byte) 0);
                }
                throw new NullPointerException("packageName cannot be null");
            }
        }

        private C1119a(String str) {
            this.f739a = str;
        }

        public final String m747a() {
            return "http://www.amazon.com/gp/mas/dl/android?p=" + this.f739a;
        }
    }

    /* renamed from: crittercism.android.do.b */
    public static final class C1121b extends dm {
        private String f740a;

        /* renamed from: crittercism.android.do.b.a */
        public static final class C1120a implements dn {
            public final /* synthetic */ dm m748a(String str) {
                if (str != null) {
                    return new C1121b((byte) 0);
                }
                throw new NullPointerException("packageName cannot be null");
            }
        }

        private C1121b(String str) {
            this.f740a = str;
        }

        public final String m749a() {
            return "market://details?id=" + this.f740a;
        }
    }
}
