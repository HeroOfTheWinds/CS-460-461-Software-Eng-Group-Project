package crittercism.android;

import com.crittercism.app.Transaction;

public final class be extends Transaction {
    public be() {
        dx.m782c("Creating no-op transaction");
    }

    public final void m476a() {
        dx.m780b("No-op transaction. Ignoring Transaction.start() call.", new IllegalStateException("No-op transaction"));
    }

    public final void m477a(int i) {
        dx.m780b("No-op transaction. Ignoring Transaction.setValue(double) call.", new IllegalStateException("No-op transaction"));
    }

    public final void m478b() {
        dx.m780b("No-op transaction. Ignoring Transaction.stop() call.", new IllegalStateException("No-op transaction"));
    }

    public final void m479c() {
        dx.m780b("No-op transaction. Ignoring Transaction.fail() call.", new IllegalStateException("No-op transaction"));
    }

    public final int m480d() {
        dx.m780b("No-op transaction. Ignoring Transaction.getValue() call.", new IllegalStateException("No-op transaction"));
        return -1;
    }
}
