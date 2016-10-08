package crittercism.android;

public final class cn {
    public int f651a;
    public int f652b;

    public cn(Throwable th) {
        this.f651a = co.Android.ordinal();
        this.f652b = cm.OK.ordinal();
        if (th != null) {
            this.f651a = co.m692a(th);
            if (this.f651a == co.Android.ordinal()) {
                this.f652b = cm.m689a(th).m691a();
            } else {
                this.f652b = Integer.parseInt(th.getMessage());
            }
        }
    }
}
