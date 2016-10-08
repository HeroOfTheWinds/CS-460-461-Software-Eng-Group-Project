package crittercism.android;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import crittercism.android.bj.C1076a;
import crittercism.android.bl.C1077a;
import crittercism.android.ce.C1110a;

public final class av implements ActivityLifecycleCallbacks {
    private int f317a;
    private boolean f318b;
    private boolean f319c;
    private boolean f320d;
    private Context f321e;
    private az f322f;
    private bd f323g;

    public av(Context context, az azVar) {
        this.f317a = 0;
        this.f318b = false;
        this.f319c = false;
        this.f320d = false;
        this.f321e = context;
        this.f322f = azVar;
        this.f323g = new bd(context, azVar);
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public final void onActivityDestroyed(Activity activity) {
    }

    public final void onActivityPaused(Activity activity) {
        if (activity != null) {
            try {
                if (this.f319c) {
                    activity.unregisterReceiver(this.f323g);
                    this.f319c = false;
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
    }

    public final void onActivityResumed(Activity activity) {
        if (activity != null) {
            try {
                if (this.f318b) {
                    dx.m778b();
                    this.f318b = false;
                } else if (this.f317a == 0) {
                    this.f322f.m414a(new bl(C1077a.FOREGROUND));
                    bg.m503f();
                    if (!this.f320d) {
                        this.f320d = true;
                        C1066b a = new C1116d(this.f321e).m717a();
                        if (a != C1066b.UNKNOWN) {
                            if (a == C1066b.NOT_CONNECTED) {
                                this.f322f.m414a(new ce(C1110a.INTERNET_DOWN));
                            } else {
                                this.f322f.m414a(new ce(C1110a.INTERNET_UP));
                            }
                        }
                    }
                } else {
                    this.f322f.m414a(new bj(C1076a.ACTIVATED, activity.getClass().getName()));
                }
                this.f317a++;
                IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                activity.registerReceiver(this.f323g, intentFilter);
                this.f319c = true;
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
        if (activity != null) {
            try {
                this.f317a--;
                if (activity.isChangingConfigurations()) {
                    dx.m778b();
                    this.f318b = true;
                } else if (this.f317a == 0) {
                    this.f322f.m414a(new bl(C1077a.BACKGROUND));
                    bg.m495a(this.f322f);
                } else {
                    this.f322f.m414a(new bj(C1076a.DEACTIVATED, activity.getClass().getName()));
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                dx.m777a(th);
            }
        }
    }
}
