package com.nianticlabs.nia.contextservice;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public abstract class ContextService {
    private static final Handler handler;
    private static final HandlerThread handlerThread;
    private static final Handler mainHandler;
    protected final long NULL_POINTER;
    protected final Object callbackLock;
    protected final Context context;
    protected long nativeClassPointer;
    private Runnable runOnPause;
    private Runnable runOnResume;
    private Runnable runOnStart;
    private Runnable runOnStop;

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.1 */
    class C07491 implements Runnable {
        C07491() {
        }

        public void run() {
            ContextService.this.onStart();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.2 */
    class C07502 implements Runnable {
        C07502() {
        }

        public void run() {
            ContextService.this.onStop();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.3 */
    class C07513 implements Runnable {
        C07513() {
        }

        public void run() {
            ContextService.this.onPause();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.4 */
    class C07524 implements Runnable {
        C07524() {
        }

        public void run() {
            ContextService.this.onResume();
        }
    }

    static {
        handlerThread = new HandlerThread("ContextService");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public ContextService(Context context, long j) {
        this.NULL_POINTER = 0;
        this.callbackLock = new Object();
        this.runOnStart = new C07491();
        this.runOnStop = new C07502();
        this.runOnPause = new C07513();
        this.runOnResume = new C07524();
        this.context = context;
        this.nativeClassPointer = j;
    }

    public static void assertOnServiceThread() {
        if (!onServiceThread()) {
            throw new RuntimeException("Must be on the service thread");
        }
    }

    public static Handler getServiceHandler() {
        return handler;
    }

    public static Looper getServiceLooper() {
        return handlerThread.getLooper();
    }

    private void invokeOnPause() {
        runOnServiceHandler(this.runOnPause);
    }

    private void invokeOnResume() {
        runOnServiceHandler(this.runOnResume);
    }

    private void invokeOnStart() {
        runOnServiceHandler(this.runOnStart);
    }

    private void invokeOnStop() {
        runOnServiceHandler(this.runOnStop);
    }

    public static boolean onServiceThread() {
        return Looper.myLooper() == handlerThread.getLooper();
    }

    public static boolean onUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnServiceHandler(Runnable runnable) {
        handler.post(runnable);
    }

    public static void runOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public static native void setActivityProviderClass(String str);

    public Context getContext() {
        return this.context;
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public final void resetNativeClassPointer() {
        synchronized (this.callbackLock) {
            this.nativeClassPointer = 0;
        }
    }
}
