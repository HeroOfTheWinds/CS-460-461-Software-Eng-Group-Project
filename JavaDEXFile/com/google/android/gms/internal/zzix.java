package com.google.android.gms.internal;

import android.view.View;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;

abstract class zzix {
    private final WeakReference<View> zzJS;

    public zzix(View view) {
        this.zzJS = new WeakReference(view);
    }

    public final void detach() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver != null) {
            zzb(viewTreeObserver);
        }
    }

    protected ViewTreeObserver getViewTreeObserver() {
        ViewTreeObserver viewTreeObserver;
        View view = (View) this.zzJS.get();
        if (view == null) {
            viewTreeObserver = null;
        } else {
            viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver == null) {
                return null;
            }
            if (!viewTreeObserver.isAlive()) {
                return null;
            }
        }
        return viewTreeObserver;
    }

    protected abstract void zza(ViewTreeObserver viewTreeObserver);

    protected abstract void zzb(ViewTreeObserver viewTreeObserver);

    public final void zzgW() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver != null) {
            zza(viewTreeObserver);
        }
    }
}
