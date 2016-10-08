package com.unity3d.player;

import android.os.Build.VERSION;

/* renamed from: com.unity3d.player.q */
public final class C0833q {
    static final boolean f191a;
    static final boolean f192b;
    static final boolean f193c;
    static final boolean f194d;
    static final boolean f195e;
    static final boolean f196f;
    static final boolean f197g;
    static final boolean f198h;
    static final C0814f f199i;
    static final C0810e f200j;
    static final C0817h f201k;
    static final C0816g f202l;
    static final C0818i f203m;

    static {
        C0818i c0818i = null;
        boolean z = true;
        f191a = VERSION.SDK_INT >= 11;
        f192b = VERSION.SDK_INT >= 12;
        f193c = VERSION.SDK_INT >= 14;
        f194d = VERSION.SDK_INT >= 16;
        f195e = VERSION.SDK_INT >= 17;
        f196f = VERSION.SDK_INT >= 19;
        f197g = VERSION.SDK_INT >= 21;
        if (VERSION.SDK_INT < 23) {
            z = false;
        }
        f198h = z;
        f199i = f191a ? new C0815d() : null;
        f200j = f192b ? new C0811c() : null;
        f201k = f194d ? new C0826l() : null;
        f202l = f195e ? new C0824k() : null;
        if (f198h) {
            c0818i = new C0829n();
        }
        f203m = c0818i;
    }
}
