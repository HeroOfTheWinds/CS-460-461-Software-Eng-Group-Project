package crittercism.android;

public abstract class di implements Runnable {
    public abstract void m390a();

    public final void run() {
        try {
            m390a();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            dx.m777a(th);
        }
    }
}
