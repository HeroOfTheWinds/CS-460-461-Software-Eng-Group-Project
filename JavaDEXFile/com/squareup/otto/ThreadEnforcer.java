package com.squareup.otto;

import android.os.Looper;

public interface ThreadEnforcer {
    public static final ThreadEnforcer ANY;
    public static final ThreadEnforcer MAIN;

    /* renamed from: com.squareup.otto.ThreadEnforcer.1 */
    static final class C07861 implements ThreadEnforcer {
        C07861() {
        }

        public void enforce(Bus bus) {
        }
    }

    /* renamed from: com.squareup.otto.ThreadEnforcer.2 */
    static final class C07872 implements ThreadEnforcer {
        C07872() {
        }

        public void enforce(Bus bus) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper());
            }
        }
    }

    static {
        ANY = new C07861();
        MAIN = new C07872();
    }

    void enforce(Bus bus);
}
