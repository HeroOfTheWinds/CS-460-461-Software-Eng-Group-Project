package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import java.util.List;

public final class bb extends CrittercismConfig {
    private String f395b;
    private bn f396c;

    public bb(bn bnVar, CrittercismConfig crittercismConfig) {
        super(crittercismConfig);
        this.f395b = "524c99a04002057fcd000001";
        this.f396c = bnVar;
    }

    public final List m466a() {
        List a = super.m33a();
        a.add(this.f396c.m556b());
        return a;
    }

    public final String m467b() {
        return this.f396c.m555a();
    }

    public final String m468c() {
        return this.f396c.m556b();
    }

    public final String m469d() {
        return this.f396c.m558d();
    }

    public final String m470e() {
        return this.f396c.m557c();
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof bb)) {
            return false;
        }
        bb bbVar = (bb) obj;
        return super.equals(obj) && CrittercismConfig.m30a(this.f396c.m555a(), bbVar.f396c.m555a()) && CrittercismConfig.m30a(this.f396c.m556b(), bbVar.f396c.m556b()) && CrittercismConfig.m30a(this.f396c.m558d(), bbVar.f396c.m558d()) && CrittercismConfig.m30a(this.f396c.m557c(), bbVar.f396c.m557c()) && CrittercismConfig.m30a(this.f395b, bbVar.f395b);
    }

    public final String m471f() {
        return this.f395b;
    }

    public final String m472g() {
        return this.a;
    }

    public final int hashCode() {
        return (((((((((super.hashCode() * 31) + this.f396c.m555a().hashCode()) * 31) + this.f396c.m556b().hashCode()) * 31) + this.f396c.m558d().hashCode()) * 31) + this.f396c.m557c().hashCode()) * 31) + this.f395b.hashCode();
    }
}
