package com.unity3d.player;

import android.util.Log;

/* renamed from: com.unity3d.player.m */
final class C0827m {
    protected static boolean f181a;

    static {
        f181a = false;
    }

    protected static void Log(int i, String str) {
        if (!f181a) {
            if (i == 6) {
                Log.e("Unity", str);
            }
            if (i == 5) {
                Log.w("Unity", str);
            }
        }
    }
}
