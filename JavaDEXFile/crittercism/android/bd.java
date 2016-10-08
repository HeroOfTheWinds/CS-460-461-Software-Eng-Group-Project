package crittercism.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.os.EnvironmentCompat;
import crittercism.android.ce.C1110a;

public final class bd extends BroadcastReceiver {
    private az f398a;
    private String f399b;
    private C1066b f400c;

    public bd(Context context, az azVar) {
        this.f398a = azVar;
        C1116d c1116d = new C1116d(context);
        this.f399b = c1116d.m718b();
        this.f400c = c1116d.m717a();
    }

    public final void onReceive(Context context, Intent intent) {
        new StringBuilder("CrittercismReceiver: INTENT ACTION = ").append(intent.getAction());
        dx.m778b();
        C1116d c1116d = new C1116d(context);
        C1066b a = c1116d.m717a();
        if (!(this.f400c == a || a == C1066b.UNKNOWN)) {
            if (a == C1066b.NOT_CONNECTED) {
                this.f398a.m414a(new ce(C1110a.INTERNET_DOWN));
            } else if (this.f400c == C1066b.NOT_CONNECTED || this.f400c == C1066b.UNKNOWN) {
                this.f398a.m414a(new ce(C1110a.INTERNET_UP));
            }
            this.f400c = a;
        }
        String b = c1116d.m718b();
        if (!b.equals(this.f399b)) {
            if (this.f399b.equals(EnvironmentCompat.MEDIA_UNKNOWN) || this.f399b.equals("disconnected")) {
                if (!(b.equals(EnvironmentCompat.MEDIA_UNKNOWN) || b.equals("disconnected"))) {
                    this.f398a.m414a(new ce(C1110a.CONN_TYPE_GAINED, b));
                }
            } else if (b.equals("disconnected")) {
                this.f398a.m414a(new ce(C1110a.CONN_TYPE_LOST, this.f399b));
            } else if (!b.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
                this.f398a.m414a(new ce(C1110a.CONN_TYPE_SWITCHED, this.f399b, b));
            }
            this.f399b = b;
        }
    }
}
