package crittercism.android;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.InetAddress;

/* renamed from: crittercism.android.k */
public final class C1136k {
    InetAddress f824a;
    String f825b;
    public String f826c;
    C1135a f827d;
    int f828e;
    boolean f829f;

    /* renamed from: crittercism.android.k.a */
    public enum C1135a {
        HTTP(Scheme.HTTP, 80),
        HTTPS(Scheme.HTTPS, 443);
        
        private String f822c;
        private int f823d;

        private C1135a(String str, int i) {
            this.f822c = str;
            this.f823d = i;
        }
    }

    public C1136k() {
        this.f826c = "/";
        this.f827d = null;
        this.f828e = -1;
        this.f829f = false;
    }
}
