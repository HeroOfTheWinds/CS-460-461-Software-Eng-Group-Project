package com.unity3d.player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;

/* renamed from: com.unity3d.player.t */
final class C0839t {
    public static C0839t f219a;
    private final ViewGroup f220b;
    private Set f221c;
    private View f222d;
    private View f223e;

    C0839t(ViewGroup viewGroup) {
        this.f221c = new HashSet();
        this.f220b = viewGroup;
        f219a = this;
    }

    private void m183e(View view) {
        this.f220b.addView(view, this.f220b.getChildCount());
    }

    private void m184f(View view) {
        this.f220b.removeView(view);
        this.f220b.requestLayout();
    }

    public final Context m185a() {
        return this.f220b.getContext();
    }

    public final void m186a(View view) {
        this.f221c.add(view);
        if (this.f222d != null) {
            m183e(view);
        }
    }

    public final void m187b(View view) {
        this.f221c.remove(view);
        if (this.f222d != null) {
            m184f(view);
        }
    }

    public final void m188c(View view) {
        if (this.f222d != view) {
            this.f222d = view;
            this.f220b.addView(view);
            for (View e : this.f221c) {
                m183e(e);
            }
            if (this.f223e != null) {
                this.f223e.setVisibility(4);
            }
        }
    }

    public final void m189d(View view) {
        if (this.f222d == view) {
            for (View f : this.f221c) {
                m184f(f);
            }
            this.f220b.removeView(view);
            this.f222d = null;
            if (this.f223e != null) {
                this.f223e.setVisibility(0);
            }
        }
    }
}
