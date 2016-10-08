package crittercism.android;

public final class ao extends ak {
    private int f310g;

    public ao(af afVar, int i) {
        super(afVar);
        this.f310g = i;
    }

    protected final af m335g() {
        Object obj = (this.f295a.m328c().equals("HEAD") || ((this.f310g >= 100 && this.f310g <= 199) || this.f310g == 204 || this.f310g == 304)) ? 1 : null;
        if (obj != null) {
            this.f295a.m327b(m275a());
            return this.f295a.m326b();
        } else if (this.f307f) {
            return new ai(this);
        } else {
            if (this.f305d) {
                if (this.f306e > 0) {
                    return new ag(this, this.f306e);
                }
                this.f295a.m327b(m275a());
                return this.f295a.m326b();
            } else if (!this.f295a.m328c().equals("CONNECT")) {
                return new aj(this);
            } else {
                this.f295a.m327b(m275a());
                return this.f295a.m326b();
            }
        }
    }
}
