package crittercism.android;

import java.lang.Thread.UncaughtExceptionHandler;

public final class ay implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler f324a;
    private final az f325b;

    public ay(az azVar, UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.f325b = azVar;
        this.f324a = uncaughtExceptionHandler;
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        try {
            this.f325b.m420a(th);
            if (this.f324a != null && !(this.f324a instanceof ay)) {
                this.f324a.uncaughtException(Thread.currentThread(), th);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
            if (!(this.f324a == null || (this.f324a instanceof ay))) {
                this.f324a.uncaughtException(Thread.currentThread(), th);
            }
        }
    }
}
