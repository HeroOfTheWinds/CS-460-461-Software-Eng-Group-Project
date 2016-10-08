package com.unity3d.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NativeActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import com.google.android.gms.location.places.Place;
import com.unity3d.player.C0807a.C0803a;
import com.upsight.android.marketing.internal.BaseMarketingModule;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UnityPlayer extends FrameLayout implements C0803a {
    private static Lock f95D;
    public static Activity currentActivity;
    private static boolean f96p;
    private ProgressBar f97A;
    private Runnable f98B;
    private Runnable f99C;
    C0802b f100a;
    C0838s f101b;
    private boolean f102c;
    private boolean f103d;
    private boolean f104e;
    private final C0819j f105f;
    private final C0839t f106g;
    private boolean f107h;
    private C0841v f108i;
    private final ConcurrentLinkedQueue f109j;
    private BroadcastReceiver f110k;
    private boolean f111l;
    private ContextWrapper f112m;
    private SurfaceView f113n;
    private WindowManager f114o;
    private boolean f115q;
    private boolean f116r;
    private int f117s;
    private int f118t;
    private final C0834r f119u;
    private String f120v;
    private NetworkInfo f121w;
    private Bundle f122x;
    private List f123y;
    private C0843w f124z;

    /* renamed from: com.unity3d.player.UnityPlayer.c */
    private abstract class C0790c implements Runnable {
        final /* synthetic */ UnityPlayer f36f;

        private C0790c(UnityPlayer unityPlayer) {
            this.f36f = unityPlayer;
        }

        public abstract void m54a();

        public final void run() {
            if (!this.f36f.isFinishing()) {
                m54a();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.10 */
    final class AnonymousClass10 extends C0790c {
        final /* synthetic */ boolean f37a;
        final /* synthetic */ String f38b;
        final /* synthetic */ int f39c;
        final /* synthetic */ UnityPlayer f40d;

        AnonymousClass10(UnityPlayer unityPlayer, boolean z, String str, int i) {
            this.f40d = unityPlayer;
            this.f37a = z;
            this.f38b = str;
            this.f39c = i;
            super((byte) 0);
        }

        public final void m55a() {
            if (this.f37a) {
                this.f40d.nativeSetInputCanceled(true);
            } else if (this.f38b != null) {
                this.f40d.nativeSetInputString(this.f38b);
            }
            if (this.f39c == 1) {
                this.f40d.nativeSoftInputClosed();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.11 */
    final class AnonymousClass11 extends C0790c {
        final /* synthetic */ int f41a;
        final /* synthetic */ byte[] f42b;
        final /* synthetic */ Size f43c;
        final /* synthetic */ C0807a f44d;
        final /* synthetic */ UnityPlayer f45e;

        AnonymousClass11(UnityPlayer unityPlayer, int i, byte[] bArr, Size size, C0807a c0807a) {
            this.f45e = unityPlayer;
            this.f41a = i;
            this.f42b = bArr;
            this.f43c = size;
            this.f44d = c0807a;
            super((byte) 0);
        }

        public final void m56a() {
            this.f45e.nativeVideoFrameCallback(this.f41a, this.f42b, this.f43c.width, this.f43c.height);
            this.f44d.m120a(this.f42b);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.12 */
    final class AnonymousClass12 implements Runnable {
        final /* synthetic */ String f46a;
        final /* synthetic */ int f47b;
        final /* synthetic */ int f48c;
        final /* synthetic */ int f49d;
        final /* synthetic */ boolean f50e;
        final /* synthetic */ int f51f;
        final /* synthetic */ int f52g;
        final /* synthetic */ UnityPlayer f53h;

        AnonymousClass12(UnityPlayer unityPlayer, String str, int i, int i2, int i3, boolean z, int i4, int i5) {
            this.f53h = unityPlayer;
            this.f46a = str;
            this.f47b = i;
            this.f48c = i2;
            this.f49d = i3;
            this.f50e = z;
            this.f51f = i4;
            this.f52g = i5;
        }

        public final void run() {
            if (this.f53h.f124z == null) {
                this.f53h.pause();
                this.f53h.f124z = new C0843w(this.f53h, this.f53h.f112m, this.f46a, this.f47b, this.f48c, this.f49d, this.f50e, (long) this.f51f, (long) this.f52g);
                this.f53h.addView(this.f53h.f124z);
                this.f53h.f124z.requestFocus();
                this.f53h.f106g.m189d(this.f53h.f113n);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.17 */
    final class AnonymousClass17 implements Runnable {
        final /* synthetic */ Semaphore f59a;
        final /* synthetic */ UnityPlayer f60b;

        AnonymousClass17(UnityPlayer unityPlayer, Semaphore semaphore) {
            this.f60b = unityPlayer;
            this.f59a = semaphore;
        }

        public final void run() {
            this.f60b.m88f();
            this.f59a.release();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.18 */
    final class AnonymousClass18 implements Runnable {
        final /* synthetic */ Semaphore f61a;
        final /* synthetic */ UnityPlayer f62b;

        AnonymousClass18(UnityPlayer unityPlayer, Semaphore semaphore) {
            this.f62b = unityPlayer;
            this.f61a = semaphore;
        }

        public final void run() {
            if (this.f62b.nativePause()) {
                this.f62b.f115q = true;
                this.f62b.m88f();
                this.f61a.release(2);
                return;
            }
            this.f61a.release();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.1 */
    final class C07921 implements OnClickListener {
        final /* synthetic */ UnityPlayer f64a;

        C07921(UnityPlayer unityPlayer) {
            this.f64a = unityPlayer;
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            this.f64a.m77b();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.20 */
    final class AnonymousClass20 implements Runnable {
        final /* synthetic */ boolean f65a;
        final /* synthetic */ UnityPlayer f66b;

        AnonymousClass20(UnityPlayer unityPlayer, boolean z) {
            this.f66b = unityPlayer;
            this.f65a = z;
        }

        public final void run() {
            this.f66b.nativeFocusChanged(this.f65a);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.2 */
    final class C07932 implements Runnable {
        final /* synthetic */ UnityPlayer f67a;

        C07932(UnityPlayer unityPlayer) {
            this.f67a = unityPlayer;
        }

        public final void run() {
            int l = this.f67a.nativeActivityIndicatorStyle();
            if (l >= 0) {
                if (this.f67a.f97A == null) {
                    this.f67a.f97A = new ProgressBar(this.f67a.f112m, null, new int[]{16842874, 16843401, 16842873, 16843400}[l]);
                    this.f67a.f97A.setIndeterminate(true);
                    this.f67a.f97A.setLayoutParams(new LayoutParams(-2, -2, 51));
                    this.f67a.addView(this.f67a.f97A);
                }
                this.f67a.f97A.setVisibility(0);
                this.f67a.bringChildToFront(this.f67a.f97A);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.3 */
    class C07943 extends BroadcastReceiver {
        final /* synthetic */ UnityPlayer f68a;

        public void onReceive(Context context, Intent intent) {
            this.f68a.m77b();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.4 */
    final class C07954 implements Runnable {
        final /* synthetic */ UnityPlayer f69a;

        C07954(UnityPlayer unityPlayer) {
            this.f69a = unityPlayer;
        }

        public final void run() {
            if (this.f69a.f97A != null) {
                this.f69a.f97A.setVisibility(8);
                this.f69a.removeView(this.f69a.f97A);
                this.f69a.f97A = null;
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.5 */
    final class C07965 implements Runnable {
        final /* synthetic */ boolean f70a;
        final /* synthetic */ UnityPlayer f71b;

        C07965(UnityPlayer unityPlayer, boolean z) {
            this.f71b = unityPlayer;
            this.f70a = z;
        }

        public final void run() {
            C0833q.f199i.m126a(this.f71b, this.f70a);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.6 */
    final class C07976 implements Runnable {
        final /* synthetic */ UnityPlayer f72a;
        final /* synthetic */ String f73b;
        final /* synthetic */ int f74c;
        final /* synthetic */ boolean f75d;
        final /* synthetic */ boolean f76e;
        final /* synthetic */ boolean f77f;
        final /* synthetic */ boolean f78g;
        final /* synthetic */ String f79h;
        final /* synthetic */ UnityPlayer f80i;

        C07976(UnityPlayer unityPlayer, UnityPlayer unityPlayer2, String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2) {
            this.f80i = unityPlayer;
            this.f72a = unityPlayer2;
            this.f73b = str;
            this.f74c = i;
            this.f75d = z;
            this.f76e = z2;
            this.f77f = z3;
            this.f78g = z4;
            this.f79h = str2;
        }

        public final void run() {
            this.f80i.f101b = new C0838s(this.f80i.f112m, this.f72a, this.f73b, this.f74c, this.f75d, this.f76e, this.f77f, this.f79h);
            this.f80i.f101b.show();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.7 */
    final class C07987 implements Runnable {
        final /* synthetic */ UnityPlayer f81a;

        C07987(UnityPlayer unityPlayer) {
            this.f81a = unityPlayer;
        }

        public final void run() {
            if (this.f81a.f101b != null) {
                this.f81a.f101b.dismiss();
                this.f81a.f101b = null;
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.8 */
    final class C07998 extends C0790c {
        final /* synthetic */ Runnable f82a;
        final /* synthetic */ UnityPlayer f83b;

        C07998(UnityPlayer unityPlayer, Runnable runnable) {
            this.f83b = unityPlayer;
            this.f82a = runnable;
            super((byte) 0);
        }

        public final void m57a() {
            this.f83b.m105b(this.f82a);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.9 */
    final class C08009 implements Runnable {
        final /* synthetic */ String f84a;
        final /* synthetic */ UnityPlayer f85b;

        C08009(UnityPlayer unityPlayer, String str) {
            this.f85b = unityPlayer;
            this.f84a = str;
        }

        public final void run() {
            if (this.f85b.f101b != null && this.f84a != null) {
                this.f85b.f101b.m182a(this.f84a);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.a */
    enum C0801a {
        PAUSE,
        RESUME,
        QUIT,
        FOCUS_GAINED,
        FOCUS_LOST
    }

    /* renamed from: com.unity3d.player.UnityPlayer.b */
    private final class C0802b extends Thread {
        ArrayBlockingQueue f92a;
        boolean f93b;
        final /* synthetic */ UnityPlayer f94c;

        C0802b(UnityPlayer unityPlayer) {
            this.f94c = unityPlayer;
            this.f93b = false;
            this.f92a = new ArrayBlockingQueue(32);
        }

        private void m58a(C0801a c0801a) {
            try {
                this.f92a.put(c0801a);
            } catch (InterruptedException e) {
                interrupt();
            }
        }

        public final void m59a() {
            m58a(C0801a.QUIT);
        }

        public final void m60a(boolean z) {
            m58a(z ? C0801a.FOCUS_GAINED : C0801a.FOCUS_LOST);
        }

        public final void m61b() {
            m58a(C0801a.RESUME);
        }

        public final void m62c() {
            m58a(C0801a.PAUSE);
        }

        public final void run() {
            setName("UnityMain");
            while (true) {
                try {
                    C0801a c0801a = (C0801a) this.f92a.take();
                    if (c0801a != C0801a.QUIT) {
                        if (c0801a == C0801a.RESUME) {
                            this.f93b = true;
                        } else if (c0801a == C0801a.PAUSE) {
                            this.f93b = false;
                            this.f94c.executeGLThreadJobs();
                        } else if (c0801a == C0801a.FOCUS_LOST && !this.f93b) {
                            this.f94c.executeGLThreadJobs();
                        }
                        if (this.f93b) {
                            do {
                                this.f94c.executeGLThreadJobs();
                                if (this.f92a.peek() != null) {
                                    break;
                                } else if (!(this.f94c.isFinishing() || this.f94c.nativeRender())) {
                                    this.f94c.m77b();
                                }
                            } while (!C0802b.interrupted());
                        }
                    } else {
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    static {
        currentActivity = null;
        new C0840u().m190a();
        f96p = false;
        f96p = loadLibraryStatic(BaseMarketingModule.SCHEDULER_MAIN);
        f95D = new ReentrantLock();
    }

    public UnityPlayer(ContextWrapper contextWrapper) {
        super(contextWrapper);
        this.f102c = false;
        this.f103d = false;
        this.f104e = true;
        this.f107h = false;
        this.f108i = new C0841v();
        this.f109j = new ConcurrentLinkedQueue();
        this.f110k = null;
        this.f111l = false;
        this.f100a = new C0802b(this);
        this.f116r = true;
        this.f117s = 0;
        this.f118t = 0;
        this.f120v = null;
        this.f121w = null;
        this.f122x = new Bundle();
        this.f123y = new ArrayList();
        this.f101b = null;
        this.f97A = null;
        this.f98B = new C07932(this);
        this.f99C = new C07954(this);
        if (contextWrapper instanceof Activity) {
            currentActivity = (Activity) contextWrapper;
        }
        this.f106g = new C0839t(this);
        this.f112m = contextWrapper;
        this.f105f = contextWrapper instanceof Activity ? new C0832p(contextWrapper) : null;
        this.f119u = new C0834r(contextWrapper, this);
        m66a();
        if (C0833q.f191a) {
            C0833q.f199i.m125a((View) this);
        }
        setFullscreen(true);
        m68a(this.f112m.getApplicationInfo());
        if (C0841v.m193c()) {
            initJni(contextWrapper);
            nativeFile(this.f112m.getPackageCodePath());
            m95j();
            this.f113n = new SurfaceView(contextWrapper);
            this.f113n.getHolder().setFormat(2);
            this.f113n.getHolder().addCallback(new Callback() {
                final /* synthetic */ UnityPlayer f54a;

                {
                    this.f54a = r1;
                }

                public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                    this.f54a.m67a(0, surfaceHolder.getSurface());
                }

                public final void surfaceCreated(SurfaceHolder surfaceHolder) {
                    this.f54a.m67a(0, surfaceHolder.getSurface());
                }

                public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    this.f54a.m67a(0, null);
                }
            });
            this.f113n.setFocusable(true);
            this.f113n.setFocusableInTouchMode(true);
            this.f106g.m188c(this.f113n);
            this.f115q = false;
            m81c();
            nativeInitWWW(WWW.class);
            nativeInitWebRequest(UnityWebRequest.class);
            if (C0833q.f195e) {
                C0833q.f202l.m137a(this, this.f112m);
            }
            if (C0833q.f198h) {
                if (currentActivity != null) {
                    C0833q.f203m.m141a(currentActivity, new Runnable() {
                        final /* synthetic */ UnityPlayer f57a;

                        /* renamed from: com.unity3d.player.UnityPlayer.15.1 */
                        final class C07911 implements Runnable {
                            final /* synthetic */ AnonymousClass15 f56a;

                            C07911(AnonymousClass15 anonymousClass15) {
                                this.f56a = anonymousClass15;
                            }

                            public final void run() {
                                this.f56a.f57a.f108i.m197d();
                                this.f56a.f57a.m90g();
                            }
                        }

                        {
                            this.f57a = r1;
                        }

                        public final void run() {
                            this.f57a.m105b(new C07911(this));
                        }
                    });
                } else {
                    this.f108i.m197d();
                }
            }
            if (C0833q.f194d) {
                C0833q.f201k.m140a(this);
            }
            this.f114o = (WindowManager) this.f112m.getSystemService("window");
            m97k();
            this.f100a.start();
            return;
        }
        AlertDialog create = new Builder(this.f112m).setTitle("Failure to initialize!").setPositiveButton("OK", new C07921(this)).setMessage("Your hardware does not support this application, sorry!").create();
        create.setCancelable(false);
        create.show();
    }

    public static native void UnitySendMessage(String str, String str2, String str3);

    private static String m65a(String str) {
        byte[] digest;
        int i = 0;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(str);
            long length = new File(str).length();
            fileInputStream.skip(length - Math.min(length, 65558));
            byte[] bArr = new byte[Place.TYPE_SUBLOCALITY_LEVEL_2];
            for (int i2 = 0; i2 != -1; i2 = fileInputStream.read(bArr)) {
                instance.update(bArr, 0, i2);
            }
            digest = instance.digest();
        } catch (FileNotFoundException e) {
            digest = null;
        } catch (IOException e2) {
            digest = null;
        } catch (NoSuchAlgorithmException e3) {
            digest = null;
        }
        if (digest == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (i < digest.length) {
            stringBuffer.append(Integer.toString((digest[i] & MotionEventCompat.ACTION_MASK) + AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, 16).substring(1));
            i++;
        }
        return stringBuffer.toString();
    }

    private void m66a() {
        try {
            File file = new File(this.f112m.getPackageCodePath(), "assets/bin/Data/settings.xml");
            InputStream fileInputStream = file.exists() ? new FileInputStream(file) : this.f112m.getAssets().open("bin/Data/settings.xml");
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(fileInputStream, null);
            String str = null;
            int eventType = newPullParser.getEventType();
            String str2 = null;
            while (eventType != 1) {
                String name;
                String str3;
                if (eventType == 2) {
                    name = newPullParser.getName();
                    str3 = str;
                    str2 = str3;
                    for (int i = 0; i < newPullParser.getAttributeCount(); i++) {
                        if (newPullParser.getAttributeName(i).equalsIgnoreCase(Twitter.NAME)) {
                            str2 = newPullParser.getAttributeValue(i);
                        }
                    }
                    str3 = name;
                    name = str2;
                    str2 = str3;
                } else if (eventType == 3) {
                    str2 = null;
                    name = str;
                } else if (eventType != 4 || str == null) {
                    name = str;
                } else {
                    if (str2.equalsIgnoreCase("integer")) {
                        this.f122x.putInt(str, Integer.parseInt(newPullParser.getText()));
                    } else if (str2.equalsIgnoreCase("string")) {
                        this.f122x.putString(str, newPullParser.getText());
                    } else if (str2.equalsIgnoreCase("bool")) {
                        this.f122x.putBoolean(str, Boolean.parseBoolean(newPullParser.getText()));
                    } else if (str2.equalsIgnoreCase("float")) {
                        this.f122x.putFloat(str, Float.parseFloat(newPullParser.getText()));
                    }
                    name = null;
                }
                str3 = name;
                eventType = newPullParser.next();
                str = str3;
            }
        } catch (Exception e) {
            C0827m.Log(6, "Unable to locate player settings. " + e.getLocalizedMessage());
            m77b();
        }
    }

    private void m67a(int i, Surface surface) {
        if (!this.f102c) {
            m79b(0, surface);
        }
    }

    private static void m68a(ApplicationInfo applicationInfo) {
        if (f96p && NativeLoader.load(applicationInfo.nativeLibraryDir)) {
            C0841v.m191a();
        }
    }

    private void m69a(C0790c c0790c) {
        if (!isFinishing()) {
            m82c((Runnable) c0790c);
        }
    }

    static void m74a(Runnable runnable) {
        new Thread(runnable).start();
    }

    private static String[] m76a(Context context) {
        String packageName = context.getPackageName();
        Vector vector = new Vector();
        try {
            int i = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/Android/obb/" + packageName);
                if (file.exists()) {
                    if (i > 0) {
                        String str = file + File.separator + "main." + i + "." + packageName + ".obb";
                        if (new File(str).isFile()) {
                            vector.add(str);
                        }
                    }
                    if (i > 0) {
                        packageName = file + File.separator + "patch." + i + "." + packageName + ".obb";
                        if (new File(packageName).isFile()) {
                            vector.add(packageName);
                        }
                    }
                }
            }
            String[] strArr = new String[vector.size()];
            vector.toArray(strArr);
            return strArr;
        } catch (NameNotFoundException e) {
            return new String[0];
        }
    }

    private void m77b() {
        if ((this.f112m instanceof Activity) && !((Activity) this.f112m).isFinishing()) {
            ((Activity) this.f112m).finish();
        }
    }

    private boolean m79b(int i, Surface surface) {
        if (!C0841v.m193c()) {
            return false;
        }
        nativeRecreateGfxState(i, surface);
        return true;
    }

    private void m81c() {
        C0830o c0830o = new C0830o((Activity) this.f112m);
        if (this.f112m instanceof NativeActivity) {
            boolean a = c0830o.m157a();
            this.f111l = a;
            nativeForwardEventsToDalvik(a);
        }
    }

    private void m82c(Runnable runnable) {
        if (!C0841v.m193c()) {
            return;
        }
        if (Thread.currentThread() == this.f100a) {
            runnable.run();
        } else {
            this.f109j.add(runnable);
        }
    }

    private void m83d() {
        for (C0807a c : this.f123y) {
            c.m122c();
        }
    }

    private void m85e() {
        for (C0807a c0807a : this.f123y) {
            try {
                c0807a.m119a((C0803a) this);
            } catch (Exception e) {
                C0827m.Log(6, "Unable to initialize camera: " + e.getMessage());
                c0807a.m122c();
            }
        }
    }

    private void m88f() {
        nativeDone();
    }

    private void m90g() {
        if (!this.f108i.m199f()) {
            return;
        }
        if (this.f124z != null) {
            this.f124z.onResume();
            return;
        }
        this.f108i.m196c(true);
        m85e();
        this.f119u.m174e();
        this.f120v = null;
        this.f121w = null;
        if (C0841v.m193c()) {
            m95j();
        }
        m82c(new Runnable() {
            final /* synthetic */ UnityPlayer f63a;

            {
                this.f63a = r1;
            }

            public final void run() {
                this.f63a.nativeResume();
            }
        });
        this.f100a.m61b();
    }

    private static void m91h() {
        if (C0841v.m193c()) {
            lockNativeAccess();
            if (NativeLoader.unload()) {
                C0841v.m192b();
                unlockNativeAccess();
                return;
            }
            unlockNativeAccess();
            throw new UnsatisfiedLinkError("Unable to unload libraries from libmain.so");
        }
    }

    private boolean m93i() {
        return this.f112m.getPackageManager().hasSystemFeature("android.hardware.camera") || this.f112m.getPackageManager().hasSystemFeature("android.hardware.camera.front");
    }

    private final native void initJni(Context context);

    private void m95j() {
        if (this.f122x.getBoolean("useObb")) {
            for (String str : m76a(this.f112m)) {
                String a = m65a(str);
                if (this.f122x.getBoolean(a)) {
                    nativeFile(str);
                }
                this.f122x.remove(a);
            }
        }
    }

    private void m97k() {
        if (this.f112m instanceof Activity) {
            ((Activity) this.f112m).getWindow().setFlags(Place.TYPE_SUBLOCALITY_LEVEL_2, Place.TYPE_SUBLOCALITY_LEVEL_2);
        }
    }

    protected static boolean loadLibraryStatic(String str) {
        try {
            System.loadLibrary(str);
            return true;
        } catch (UnsatisfiedLinkError e) {
            C0827m.Log(6, "Unable to find " + str);
            return false;
        } catch (Exception e2) {
            C0827m.Log(6, "Unknown error " + e2);
            return false;
        }
    }

    protected static void lockNativeAccess() {
        f95D.lock();
    }

    private final native int nativeActivityIndicatorStyle();

    private final native void nativeDone();

    private final native void nativeFile(String str);

    private final native void nativeFocusChanged(boolean z);

    private final native void nativeInitWWW(Class cls);

    private final native void nativeInitWebRequest(Class cls);

    private final native boolean nativeInjectEvent(InputEvent inputEvent);

    private final native boolean nativePause();

    private final native void nativeRecreateGfxState(int i, Surface surface);

    private final native boolean nativeRender();

    private final native void nativeResume();

    private final native void nativeSetExtras(Bundle bundle);

    private final native void nativeSetInputCanceled(boolean z);

    private final native void nativeSetInputString(String str);

    private final native void nativeSetTouchDeltaY(float f);

    private final native void nativeSoftInputClosed();

    private final native void nativeVideoFrameCallback(int i, byte[] bArr, int i2, int i3);

    protected static void unlockNativeAccess() {
        f95D.unlock();
    }

    protected boolean Location_IsServiceEnabledByUser() {
        return this.f119u.m169a();
    }

    protected void Location_SetDesiredAccuracy(float f) {
        this.f119u.m171b(f);
    }

    protected void Location_SetDistanceFilter(float f) {
        this.f119u.m168a(f);
    }

    protected void Location_StartUpdatingLocation() {
        this.f119u.m170b();
    }

    protected void Location_StopUpdatingLocation() {
        this.f119u.m172c();
    }

    final void m105b(Runnable runnable) {
        if (this.f112m instanceof Activity) {
            ((Activity) this.f112m).runOnUiThread(runnable);
        } else {
            C0827m.Log(5, "Not running Unity from an Activity; ignored...");
        }
    }

    protected void closeCamera(int i) {
        for (C0807a c0807a : this.f123y) {
            if (c0807a.m118a() == i) {
                c0807a.m122c();
                this.f123y.remove(c0807a);
                return;
            }
        }
    }

    public void configurationChanged(Configuration configuration) {
        if (this.f113n instanceof SurfaceView) {
            this.f113n.getHolder().setSizeFromLayout();
        }
        if (this.f124z != null) {
            this.f124z.updateVideoLayout();
        }
    }

    protected void disableLogger() {
        C0827m.f181a = true;
    }

    public boolean displayChanged(int i, Surface surface) {
        if (i == 0) {
            this.f102c = surface != null;
            m105b(new Runnable() {
                final /* synthetic */ UnityPlayer f58a;

                {
                    this.f58a = r1;
                }

                public final void run() {
                    if (this.f58a.f102c) {
                        this.f58a.f106g.m189d(this.f58a.f113n);
                    } else {
                        this.f58a.f106g.m188c(this.f58a.f113n);
                    }
                }
            });
        }
        return m79b(i, surface);
    }

    protected void executeGLThreadJobs() {
        while (true) {
            Runnable runnable = (Runnable) this.f109j.poll();
            if (runnable != null) {
                runnable.run();
            } else {
                return;
            }
        }
    }

    protected void forwardMotionEventToDalvik(long j, long j2, int i, int i2, int[] iArr, float[] fArr, int i3, float f, float f2, int i4, int i5, int i6, int i7, int i8, long[] jArr, float[] fArr2) {
        this.f105f.m142a(j, j2, i, i2, iArr, fArr, i3, f, f2, i4, i5, i6, i7, i8, jArr, fArr2);
    }

    protected int getCameraOrientation(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(i, cameraInfo);
        return cameraInfo.orientation;
    }

    protected int getNumCameras() {
        return !m93i() ? 0 : Camera.getNumberOfCameras();
    }

    public Bundle getSettings() {
        return this.f122x;
    }

    protected int getSplashMode() {
        return this.f122x.getInt("splash_mode");
    }

    public View getView() {
        return this;
    }

    protected void hideSoftInput() {
        Runnable c07987 = new C07987(this);
        if (C0833q.f197g) {
            m69a(new C07998(this, c07987));
        } else {
            m105b(c07987);
        }
    }

    protected void hideVideoPlayer() {
        m105b(new Runnable() {
            final /* synthetic */ UnityPlayer f55a;

            {
                this.f55a = r1;
            }

            public final void run() {
                if (this.f55a.f124z != null) {
                    this.f55a.f106g.m188c(this.f55a.f113n);
                    this.f55a.removeView(this.f55a.f124z);
                    this.f55a.f124z = null;
                    this.f55a.resume();
                }
            }
        });
    }

    public void init(int i, boolean z) {
    }

    protected int[] initCamera(int i, int i2, int i3, int i4) {
        C0807a c0807a = new C0807a(i, i2, i3, i4);
        try {
            c0807a.m119a((C0803a) this);
            this.f123y.add(c0807a);
            Size b = c0807a.m121b();
            int i5 = b.width;
            int i6 = b.height;
            return new int[]{i5, i6};
        } catch (Exception e) {
            C0827m.Log(6, "Unable to initialize camera: " + e.getMessage());
            c0807a.m122c();
            return null;
        }
    }

    public boolean injectEvent(InputEvent inputEvent) {
        return nativeInjectEvent(inputEvent);
    }

    protected boolean installPresentationDisplay(int i) {
        return C0833q.f195e ? C0833q.f202l.m138a(this, this.f112m, i) : false;
    }

    protected boolean isCameraFrontFacing(int i) {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(i, cameraInfo);
        return cameraInfo.facing == 1;
    }

    protected boolean isFinishing() {
        if (!this.f115q) {
            boolean z = (this.f112m instanceof Activity) && ((Activity) this.f112m).isFinishing();
            this.f115q = z;
            if (!z) {
                return false;
            }
        }
        return true;
    }

    protected void kill() {
        Process.killProcess(Process.myPid());
    }

    protected boolean loadLibrary(String str) {
        return loadLibraryStatic(str);
    }

    protected final native void nativeAddVSyncTime(long j);

    final native void nativeForwardEventsToDalvik(boolean z);

    protected native void nativeSetLocation(float f, float f2, float f3, float f4, double d, float f5);

    protected native void nativeSetLocationStatus(int i);

    public void onCameraFrame(C0807a c0807a, byte[] bArr) {
        m69a(new AnonymousClass11(this, c0807a.m118a(), bArr, c0807a.m121b(), c0807a));
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public void pause() {
        if (this.f124z != null) {
            this.f124z.onPause();
            return;
        }
        reportSoftInputStr(null, 1, true);
        if (this.f108i.m200g()) {
            if (C0841v.m193c()) {
                Semaphore semaphore = new Semaphore(0);
                if (isFinishing()) {
                    m82c(new AnonymousClass17(this, semaphore));
                } else {
                    m82c(new AnonymousClass18(this, semaphore));
                }
                try {
                    if (!semaphore.tryAcquire(4, TimeUnit.SECONDS)) {
                        C0827m.Log(5, "Timeout while trying to pause the Unity Engine.");
                    }
                } catch (InterruptedException e) {
                    C0827m.Log(5, "UI thread got interrupted while trying to pause the Unity Engine.");
                }
                if (semaphore.drainPermits() > 0) {
                    quit();
                }
            }
            this.f108i.m196c(false);
            this.f108i.m195b(true);
            m83d();
            this.f100a.m62c();
            this.f119u.m173d();
        }
    }

    public void quit() {
        this.f115q = true;
        if (!this.f108i.m198e()) {
            pause();
        }
        this.f100a.m59a();
        try {
            this.f100a.join(4000);
        } catch (InterruptedException e) {
            this.f100a.interrupt();
        }
        if (this.f110k != null) {
            this.f112m.unregisterReceiver(this.f110k);
        }
        this.f110k = null;
        if (C0841v.m193c()) {
            removeAllViews();
        }
        if (C0833q.f195e) {
            C0833q.f202l.m136a(this.f112m);
        }
        if (C0833q.f194d) {
            C0833q.f201k.m139a();
        }
        kill();
        m91h();
    }

    protected void reportSoftInputStr(String str, int i, boolean z) {
        if (i == 1) {
            hideSoftInput();
        }
        m69a(new AnonymousClass10(this, z, str, i));
    }

    public void resume() {
        if (C0833q.f191a) {
            C0833q.f199i.m128b(this);
        }
        this.f108i.m195b(false);
        m90g();
    }

    protected void setFullscreen(boolean z) {
        this.f104e = z;
        if (C0833q.f191a) {
            m105b(new C07965(this, z));
        }
    }

    protected void setSoftInputStr(String str) {
        m105b(new C08009(this, str));
    }

    protected void showSoftInput(String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2) {
        m105b(new C07976(this, this, str, i, z, z2, z3, z4, str2));
    }

    protected void showVideoPlayer(String str, int i, int i2, int i3, boolean z, int i4, int i5) {
        m105b(new AnonymousClass12(this, str, i, i2, i3, z, i4, i5));
    }

    protected void startActivityIndicator() {
        m105b(this.f98B);
    }

    protected void stopActivityIndicator() {
        m105b(this.f99C);
    }

    public void windowFocusChanged(boolean z) {
        this.f108i.m194a(z);
        if (z && this.f101b != null) {
            reportSoftInputStr(null, 1, false);
        }
        if (C0833q.f191a && z) {
            C0833q.f199i.m128b(this);
        }
        m82c(new AnonymousClass20(this, z));
        this.f100a.m60a(z);
        m90g();
    }
}
