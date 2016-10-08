package crittercism.android;

import java.net.URL;

final class dh extends di {
    private bs f719a;
    private bs f720b;
    private au f721c;
    private URL f722d;
    private cy f723e;
    private cx f724f;

    dh(bs bsVar, bs bsVar2, au auVar, URL url, cy cyVar, cx cxVar) {
        this.f720b = bsVar;
        this.f719a = bsVar2;
        this.f721c = auVar;
        this.f722d = url;
        this.f723e = cyVar;
        this.f724f = cxVar;
    }

    public final void m735a() {
        this.f719a.m575a(this.f720b);
        new dj(this.f724f.m698a(this.f721c).m700a(this.f720b), new dc(this.f722d), true, this.f723e).run();
    }
}
