package rx.internal.operators;

import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import rx.Notification;
import rx.Notification.Kind;
import rx.Observable.Operator;
import rx.Subscriber;
import spacemadness.com.lunarconsole.C1518R;

public final class OperatorDematerialize<T> implements Operator<T, Notification<T>> {

    /* renamed from: rx.internal.operators.OperatorDematerialize.1 */
    class C13261 extends Subscriber<Notification<T>> {
        boolean terminated;
        final /* synthetic */ Subscriber val$child;

        C13261(Subscriber subscriber, Subscriber subscriber2) {
            this.val$child = subscriber2;
            super(subscriber);
        }

        public void onCompleted() {
            if (!this.terminated) {
                this.terminated = true;
                this.val$child.onCompleted();
            }
        }

        public void onError(Throwable th) {
            if (!this.terminated) {
                this.terminated = true;
                this.val$child.onError(th);
            }
        }

        public void onNext(Notification<T> notification) {
            switch (C13272.$SwitchMap$rx$Notification$Kind[notification.getKind().ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    if (!this.terminated) {
                        this.val$child.onNext(notification.getValue());
                    }
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    onError(notification.getThrowable());
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_CENTER /*3*/:
                    onCompleted();
                default:
            }
        }
    }

    /* renamed from: rx.internal.operators.OperatorDematerialize.2 */
    static /* synthetic */ class C13272 {
        static final /* synthetic */ int[] $SwitchMap$rx$Notification$Kind;

        static {
            $SwitchMap$rx$Notification$Kind = new int[Kind.values().length];
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnNext.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnError.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$rx$Notification$Kind[Kind.OnCompleted.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static final class Holder {
        static final OperatorDematerialize<Object> INSTANCE;

        static {
            INSTANCE = new OperatorDematerialize();
        }

        private Holder() {
        }
    }

    OperatorDematerialize() {
    }

    public static OperatorDematerialize instance() {
        return Holder.INSTANCE;
    }

    public Subscriber<? super Notification<T>> call(Subscriber<? super T> subscriber) {
        return new C13261(subscriber, subscriber);
    }
}
