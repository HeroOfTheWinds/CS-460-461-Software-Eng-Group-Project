package com.upsight.android.analytics.internal.session;

import android.app.Activity;
import android.content.Intent;
import com.squareup.otto.Bus;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityTrackEvent;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import spacemadness.com.lunarconsole.C1518R;

@Singleton
class ManualTracker extends UpsightLifeCycleTracker {
    private static final String LOG_TAG;
    private Set<WeakReference<Activity>> mActivitySet;
    private Bus mBus;
    private UpsightDataStore mDataStore;
    private boolean mIsTaskRootStopped;
    private UpsightLogger mLogger;
    private SessionManager mSessionManager;

    /* renamed from: com.upsight.android.analytics.internal.session.ManualTracker.1 */
    class C08801 implements UpsightDataStoreListener<Set<ApplicationStatus>> {
        C08801() {
        }

        public void onFailure(UpsightException upsightException) {
        }

        public void onSuccess(Set<ApplicationStatus> set) {
            if (set.isEmpty()) {
                ManualTracker.this.mDataStore.store(new ApplicationStatus(State.FOREGROUND));
                ManualTracker.this.mLogger.m205d(ManualTracker.LOG_TAG, "Create application state " + State.FOREGROUND, new Object[0]);
                return;
            }
            int i = 0;
            for (ApplicationStatus applicationStatus : set) {
                if (i == 0) {
                    applicationStatus.state = State.FOREGROUND;
                    ManualTracker.this.mDataStore.store(applicationStatus);
                    i = 1;
                    ManualTracker.this.mLogger.m205d(ManualTracker.LOG_TAG, "Update application state to " + applicationStatus.state, new Object[0]);
                } else {
                    ManualTracker.this.mDataStore.remove(applicationStatus);
                    ManualTracker.this.mLogger.m213w(ManualTracker.LOG_TAG, "Remove duplicate application state " + applicationStatus.state, new Object[0]);
                }
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.session.ManualTracker.2 */
    class C08812 implements UpsightDataStoreListener<Set<ApplicationStatus>> {
        C08812() {
        }

        public void onFailure(UpsightException upsightException) {
        }

        public void onSuccess(Set<ApplicationStatus> set) {
            if (set.isEmpty()) {
                ManualTracker.this.mDataStore.store(new ApplicationStatus(State.BACKGROUND));
                ManualTracker.this.mLogger.m205d(ManualTracker.LOG_TAG, "Create application state " + State.BACKGROUND, new Object[0]);
                return;
            }
            Iterator it = set.iterator();
            int i = 0;
            while (it.hasNext()) {
                ApplicationStatus applicationStatus = (ApplicationStatus) it.next();
                if (i == 0) {
                    applicationStatus.state = State.BACKGROUND;
                    ManualTracker.this.mDataStore.store(applicationStatus);
                    i = 1;
                    ManualTracker.this.mLogger.m205d(ManualTracker.LOG_TAG, "Update application state to " + applicationStatus.state, new Object[0]);
                } else {
                    ManualTracker.this.mDataStore.remove(applicationStatus);
                    it.remove();
                    ManualTracker.this.mLogger.m213w(ManualTracker.LOG_TAG, "Remove duplicate application state " + applicationStatus.state, new Object[0]);
                }
            }
        }
    }

    /* renamed from: com.upsight.android.analytics.internal.session.ManualTracker.3 */
    static /* synthetic */ class C08823 {
        static final /* synthetic */ int[] f257xa86dd1d6;

        static {
            f257xa86dd1d6 = new int[ActivityState.values().length];
            try {
                f257xa86dd1d6[ActivityState.STARTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f257xa86dd1d6[ActivityState.STOPPED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    static {
        LOG_TAG = ManualTracker.class.getSimpleName();
    }

    @Inject
    public ManualTracker(SessionManager sessionManager, UpsightContext upsightContext) {
        this.mIsTaskRootStopped = false;
        this.mSessionManager = sessionManager;
        this.mDataStore = upsightContext.getDataStore();
        this.mBus = upsightContext.getCoreComponent().bus();
        this.mLogger = upsightContext.getLogger();
        this.mActivitySet = new HashSet();
    }

    private static boolean isPurgeable(Activity activity) {
        return activity == null;
    }

    private static void removeAndPurge(Set<WeakReference<Activity>> set, Activity activity) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Activity activity2 = (Activity) ((WeakReference) it.next()).get();
            if (activity2 == activity || isPurgeable(activity2)) {
                it.remove();
            }
        }
    }

    public void track(Activity activity, ActivityState activityState, SessionInitializer sessionInitializer) {
        if (activity != null && activityState != null) {
            switch (C08823.f257xa86dd1d6[activityState.ordinal()]) {
                case C1518R.styleable.LoadingImageView_imageAspectRatio /*1*/:
                    this.mLogger.m205d(LOG_TAG, "Track starting of " + activity + " isTaskRoot=" + activity.isTaskRoot(), new Object[0]);
                    if (this.mActivitySet.isEmpty()) {
                        this.mDataStore.fetch(ApplicationStatus.class, new C08801());
                        Intent intent = activity.getIntent();
                        if (intent == null || !intent.hasExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH)) {
                            this.mSessionManager.startSession(sessionInitializer);
                            this.mLogger.m205d(LOG_TAG, "Request to start new Upsight session", new Object[0]);
                        }
                    }
                    this.mActivitySet.add(new WeakReference(activity));
                    break;
                case C1518R.styleable.LoadingImageView_circleCrop /*2*/:
                    this.mLogger.m205d(LOG_TAG, "Track stopping of " + activity, new Object[0]);
                    removeAndPurge(this.mActivitySet, activity);
                    if (activity.isTaskRoot()) {
                        this.mIsTaskRootStopped = true;
                        this.mLogger.m205d(LOG_TAG, "Clear task root stopped condition with task root Activity " + activity, new Object[0]);
                    }
                    if (this.mIsTaskRootStopped && !activity.isChangingConfigurations() && this.mActivitySet.isEmpty()) {
                        this.mDataStore.fetch(ApplicationStatus.class, new C08812());
                        this.mSessionManager.stopSession();
                        this.mLogger.m205d(LOG_TAG, "Request to stop current Upsight session", new Object[0]);
                        break;
                    }
            }
            this.mBus.post(new ActivityTrackEvent(activity, activityState));
        }
    }
}
