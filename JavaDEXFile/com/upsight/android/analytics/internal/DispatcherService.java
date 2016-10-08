package com.upsight.android.analytics.internal;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.configuration.ConfigurationManager;
import com.upsight.android.analytics.internal.dispatcher.Dispatcher;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import com.upsight.android.persistence.annotation.Updated;
import java.util.Set;
import javax.inject.Inject;

public class DispatcherService extends Service {
    private static final String LOG_TAG;
    private static final long STATUS_CHECK_INTERVAL = 25000;
    private static final int STOP_AFTER_DEAD_INTERVALS = 4;
    private Bus mBus;
    @Inject
    ConfigurationManager mConfigurationManager;
    private UpsightSubscription mDataStoreSubscription;
    private int mDeadIntervalsInARow;
    @Inject
    Dispatcher mDispatcher;
    private Handler mHandler;
    private UpsightLogger mLogger;
    private Runnable mSelfStopTask;

    /* renamed from: com.upsight.android.analytics.internal.DispatcherService.1 */
    class C08551 implements Runnable {
        C08551() {
        }

        public void run() {
            boolean hasPendingRecords = DispatcherService.this.mDispatcher.hasPendingRecords();
            DispatcherService.this.mLogger.m205d(DispatcherService.LOG_TAG, "Check for idle hasPendingRecords=" + hasPendingRecords + " mDeadIntervalsInARow=" + DispatcherService.this.mDeadIntervalsInARow, new Object[0]);
            if (hasPendingRecords) {
                DispatcherService.this.mDeadIntervalsInARow = 0;
                DispatcherService.this.mHandler.postDelayed(DispatcherService.this.mSelfStopTask, DispatcherService.STATUS_CHECK_INTERVAL);
            } else if (DispatcherService.this.mDeadIntervalsInARow == DispatcherService.STOP_AFTER_DEAD_INTERVALS) {
                DispatcherService.this.mLogger.m205d(DispatcherService.LOG_TAG, "Request to destroy", new Object[0]);
                DispatcherService.this.stopSelf();
            } else {
                DispatcherService.this.mDeadIntervalsInARow = DispatcherService.this.mDeadIntervalsInARow + 1;
                DispatcherService.this.mHandler.postDelayed(DispatcherService.this.mSelfStopTask, DispatcherService.STATUS_CHECK_INTERVAL);
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.DispatcherService.2 */
    class C08562 implements UpsightDataStoreListener<Set<ApplicationStatus>> {
        C08562() {
        }

        public void onFailure(UpsightException upsightException) {
        }

        public void onSuccess(Set<ApplicationStatus> set) {
            for (ApplicationStatus access$500 : set) {
                DispatcherService.this.handle(access$500);
            }
        }
    }

    public static final class DestroyEvent {
    }

    static {
        LOG_TAG = DispatcherService.class.getSimpleName();
    }

    public DispatcherService() {
        this.mSelfStopTask = new C08551();
    }

    private void handle(ApplicationStatus applicationStatus) {
        if (applicationStatus.getState() == State.BACKGROUND) {
            this.mDeadIntervalsInARow = 0;
            this.mHandler.postDelayed(this.mSelfStopTask, STATUS_CHECK_INTERVAL);
            return;
        }
        this.mHandler.removeCallbacks(this.mSelfStopTask);
    }

    @Created
    @Updated
    public void onApplicationStatus(ApplicationStatus applicationStatus) {
        handle(applicationStatus);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        UpsightContext createContext = Upsight.createContext(this);
        UpsightAnalyticsExtension upsightAnalyticsExtension = (UpsightAnalyticsExtension) createContext.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (upsightAnalyticsExtension != null) {
            ((UpsightAnalyticsComponent) upsightAnalyticsExtension.getComponent()).inject(this);
            this.mBus = createContext.getCoreComponent().bus();
            this.mLogger = createContext.getLogger();
            this.mLogger.m205d(LOG_TAG, "onCreate()", new Object[0]);
            this.mHandler = new Handler();
            this.mDataStoreSubscription = createContext.getDataStore().subscribe(this);
            this.mDispatcher.launch();
            this.mConfigurationManager.launch();
            createContext.getDataStore().fetch(ApplicationStatus.class, new C08562());
        }
    }

    public void onDestroy() {
        if (this.mBus != null) {
            this.mBus.post(new DestroyEvent());
        }
        this.mHandler.removeCallbacks(this.mSelfStopTask);
        this.mDataStoreSubscription.unsubscribe();
        this.mLogger.m205d(LOG_TAG, "onDestroy()", new Object[0]);
        super.onDestroy();
    }
}
