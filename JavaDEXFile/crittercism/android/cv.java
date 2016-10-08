package crittercism.android;

public final class cv {
    private long f689a;
    private long f690b;

    public cv(long j) {
        this.f689a = 0;
        this.f690b = j;
    }

    public final boolean m715a() {
        boolean z;
        synchronized (this) {
            z = System.nanoTime() - this.f689a > this.f690b;
        }
        return z;
    }

    public final void m716b() {
        synchronized (this) {
            this.f689a = System.nanoTime();
        }
    }
}
