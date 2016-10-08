package com.unity3d.player;

/* renamed from: com.unity3d.player.v */
final class C0841v {
    private static boolean f225a;
    private boolean f226b;
    private boolean f227c;
    private boolean f228d;
    private boolean f229e;

    static {
        f225a = false;
    }

    C0841v() {
        this.f226b = !C0833q.f198h;
        this.f227c = false;
        this.f228d = false;
        this.f229e = true;
    }

    static void m191a() {
        f225a = true;
    }

    static void m192b() {
        f225a = false;
    }

    static boolean m193c() {
        return f225a;
    }

    final void m194a(boolean z) {
        this.f227c = z;
    }

    final void m195b(boolean z) {
        this.f229e = z;
    }

    final void m196c(boolean z) {
        this.f228d = z;
    }

    final void m197d() {
        this.f226b = true;
    }

    final boolean m198e() {
        return this.f229e;
    }

    final boolean m199f() {
        return f225a && this.f227c && this.f226b && !this.f229e && !this.f228d;
    }

    final boolean m200g() {
        return this.f228d;
    }

    public final String toString() {
        return super.toString();
    }
}
