package com.upsight.android.googlepushservices.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.event.comm.UpsightCommRegisterEvent;
import com.upsight.android.analytics.event.comm.UpsightCommUnregisterEvent;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnRegisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServices.OnUnregisterListener;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import com.upsight.android.googlepushservices.internal.PushConfigManager.Config;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.UpsightBillboard;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action1;

@Singleton
public class GooglePushServices implements UpsightGooglePushServicesApi {
    private static final String KEY_GCM = "com.upsight.gcm";
    private static final String LOG_TAG;
    private static final String PREFERENCES_NAME = "com.upsight.android.googleadvertisingid.internal.registration";
    private static final String PROPERTY_LAST_PUSH_TOKEN_REGISTRATION_TIME = "lastPushTokenRegistrationTime";
    private static final String PROPERTY_REG_ID = "gcmRegistrationId";
    static final String PUSH_SCOPE = "com_upsight_push_scope";
    private final Scheduler mComputationScheduler;
    private UpsightLogger mLogger;
    private final Set<OnRegisterListener> mPendingRegisterListeners;
    private final Set<OnUnregisterListener> mPendingUnregisterListeners;
    private SharedPreferences mPrefs;
    private UpsightBillboard mPushBillboard;
    private PushConfigManager mPushConfigManager;
    private boolean mRegistrationIsInProgress;
    private final Handler mUiThreadHandler;
    private boolean mUnregistrationIsInProgress;
    private UpsightContext mUpsight;

    /* renamed from: com.upsight.android.googlepushservices.internal.GooglePushServices.1 */
    class C08921 implements OnSubscribe<String> {
        final /* synthetic */ String val$projectId;

        C08921(String str) {
            this.val$projectId = str;
        }

        public void call(Subscriber<? super String> subscriber) {
            try {
                CharSequence register = GoogleCloudMessaging.getInstance(GooglePushServices.this.mUpsight).register(this.val$projectId);
                if (TextUtils.isEmpty(register)) {
                    subscriber.onError(new IOException("Invalid push token returned from GoogleCloudMessaging"));
                    return;
                }
                subscriber.onNext(register);
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            }
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.GooglePushServices.2 */
    class C08932 implements Observer<String> {
        C08932() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable th) {
            synchronized (GooglePushServices.this) {
                Set<OnRegisterListener> hashSet = new HashSet(GooglePushServices.this.mPendingRegisterListeners);
                GooglePushServices.this.mPendingRegisterListeners.clear();
                GooglePushServices.this.mRegistrationIsInProgress = false;
            }
            for (OnRegisterListener onFailure : hashSet) {
                onFailure.onFailure(new UpsightException(th));
            }
        }

        public void onNext(String str) {
            synchronized (GooglePushServices.this) {
                GooglePushServices.this.registerPushToken(str);
                Set<OnRegisterListener> hashSet = new HashSet(GooglePushServices.this.mPendingRegisterListeners);
                GooglePushServices.this.mPendingRegisterListeners.clear();
                GooglePushServices.this.mRegistrationIsInProgress = false;
            }
            for (OnRegisterListener onSuccess : hashSet) {
                onSuccess.onSuccess(str);
            }
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.GooglePushServices.3 */
    class C08943 implements OnSubscribe<String> {
        C08943() {
        }

        public void call(Subscriber<? super String> subscriber) {
            try {
                GoogleCloudMessaging.getInstance(GooglePushServices.this.mUpsight).unregister();
                subscriber.onCompleted();
            } catch (Throwable e) {
                subscriber.onError(e);
            }
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.GooglePushServices.4 */
    class C08954 implements Observer<String> {
        C08954() {
        }

        public void onCompleted() {
            synchronized (GooglePushServices.this) {
                UpsightCommUnregisterEvent.createBuilder().record(GooglePushServices.this.mUpsight);
                GooglePushServices.this.removeRegistrationInfo();
                Set<OnUnregisterListener> hashSet = new HashSet(GooglePushServices.this.mPendingUnregisterListeners);
                GooglePushServices.this.mPendingUnregisterListeners.clear();
                GooglePushServices.this.mUnregistrationIsInProgress = false;
            }
            for (OnUnregisterListener onSuccess : hashSet) {
                onSuccess.onSuccess();
            }
        }

        public void onError(Throwable th) {
            synchronized (GooglePushServices.this) {
                Set<OnUnregisterListener> hashSet = new HashSet(GooglePushServices.this.mPendingUnregisterListeners);
                GooglePushServices.this.mPendingUnregisterListeners.clear();
                GooglePushServices.this.mUnregistrationIsInProgress = false;
            }
            for (OnUnregisterListener onFailure : hashSet) {
                onFailure.onFailure(new UpsightException(th));
            }
        }

        public void onNext(String str) {
        }
    }

    /* renamed from: com.upsight.android.googlepushservices.internal.GooglePushServices.5 */
    class C08965 implements Action1<Config> {
        final /* synthetic */ String val$pushToken;

        C08965(String str) {
            this.val$pushToken = str;
        }

        public void call(Config config) {
            long convert = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            if (!this.val$pushToken.equals(GooglePushServices.this.getRegistrationId()) || convert - GooglePushServices.this.getLastPushTokenRegistrationTime() > config.pushTokenTtl) {
                UpsightCommRegisterEvent.createBuilder().setToken(this.val$pushToken).record(GooglePushServices.this.mUpsight);
                GooglePushServices.this.storeRegistrationInfo(this.val$pushToken, convert);
            }
        }
    }

    static {
        LOG_TAG = GooglePushServices.class.getName();
    }

    @Inject
    GooglePushServices(UpsightContext upsightContext, PushConfigManager pushConfigManager) {
        this.mUpsight = upsightContext;
        this.mPushConfigManager = pushConfigManager;
        this.mLogger = upsightContext.getLogger();
        if (Looper.myLooper() != null) {
            this.mUiThreadHandler = new Handler(Looper.myLooper());
        } else {
            this.mUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        this.mComputationScheduler = upsightContext.getCoreComponent().subscribeOnScheduler();
        this.mRegistrationIsInProgress = false;
        this.mUnregistrationIsInProgress = false;
        this.mPendingRegisterListeners = new HashSet();
        this.mPendingUnregisterListeners = new HashSet();
        this.mPrefs = this.mUpsight.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    private long getLastPushTokenRegistrationTime() {
        return this.mPrefs.getLong(PROPERTY_LAST_PUSH_TOKEN_REGISTRATION_TIME, 0);
    }

    private String getRegistrationId() {
        CharSequence string = this.mPrefs.getString(PROPERTY_REG_ID, null);
        return TextUtils.isEmpty(string) ? null : string;
    }

    private boolean hasPlayServices() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mUpsight) == 0) {
            return true;
        }
        this.mLogger.m207e(LOG_TAG, "Google play service is not available: ", GooglePlayServicesUtil.getErrorString(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mUpsight)));
        return false;
    }

    private boolean isRegistered() {
        return getRegistrationId() != null;
    }

    private void registerInBackground(String str) {
        this.mRegistrationIsInProgress = true;
        Observable.create(new C08921(str)).subscribeOn(this.mComputationScheduler).observeOn(HandlerScheduler.from(this.mUiThreadHandler)).subscribe(new C08932());
    }

    private void registerPushToken(String str) {
        try {
            this.mPushConfigManager.fetchCurrentConfigObservable().subscribeOn(this.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(this.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new C08965(str));
        } catch (IOException e) {
            this.mLogger.m207e(LOG_TAG, "Failed to fetch push configurations", e);
        }
    }

    private void removeRegistrationInfo() {
        Editor edit = this.mPrefs.edit();
        edit.remove(PROPERTY_REG_ID);
        edit.remove(PROPERTY_LAST_PUSH_TOKEN_REGISTRATION_TIME);
        edit.apply();
    }

    private void storeRegistrationInfo(String str, long j) {
        Editor edit = this.mPrefs.edit();
        edit.putString(PROPERTY_REG_ID, str);
        edit.putLong(PROPERTY_LAST_PUSH_TOKEN_REGISTRATION_TIME, j);
        edit.apply();
    }

    private void unregisterInBackground() {
        this.mUnregistrationIsInProgress = true;
        Observable.create(new C08943()).subscribeOn(this.mComputationScheduler).observeOn(HandlerScheduler.from(this.mUiThreadHandler)).subscribe(new C08954());
    }

    public UpsightBillboard createPushBillboard(UpsightContext upsightContext, UpsightBillboard.Handler handler) throws IllegalArgumentException, IllegalStateException {
        UpsightBillboard upsightBillboard;
        synchronized (this) {
            if (this.mPushBillboard != null) {
                this.mPushBillboard.destroy();
                this.mPushBillboard = null;
            }
            this.mPushBillboard = UpsightBillboard.create(upsightContext, PUSH_SCOPE, handler);
            upsightBillboard = this.mPushBillboard;
        }
        return upsightBillboard;
    }

    public void register(OnRegisterListener onRegisterListener) {
        Object substring;
        NameNotFoundException e;
        CharSequence charSequence = null;
        synchronized (this) {
            if (onRegisterListener == null) {
                throw new IllegalArgumentException("Listener could not be null");
            }
            if (!hasPlayServices()) {
                onRegisterListener.onFailure(new UpsightException("Google Play Services are not available", new Object[0]));
            } else if (this.mUnregistrationIsInProgress) {
                onRegisterListener.onFailure(new UpsightException("Unregistration is in progress, try later", new Object[0]));
            } else {
                try {
                    Bundle bundle = this.mUpsight.getPackageManager().getApplicationInfo(this.mUpsight.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData;
                    if (bundle != null) {
                        String string = bundle.getString(KEY_GCM);
                        if (!TextUtils.isEmpty(string)) {
                            substring = string.substring(0, string.lastIndexOf(46));
                            try {
                                charSequence = string.substring(string.lastIndexOf(46) + 1);
                            } catch (NameNotFoundException e2) {
                                e = e2;
                                this.mLogger.m207e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
                                if (this.mUpsight.getPackageName().equals(substring)) {
                                }
                                this.mLogger.m207e(LOG_TAG, "Registration aborted, wrong or no value for com.upsight.gcm was defined", new Object[0]);
                                if (!this.mUpsight.getPackageName().equals(substring)) {
                                    this.mLogger.m207e(LOG_TAG, "Check that the package name of your application is specified correctly", new Object[0]);
                                }
                                if (TextUtils.isEmpty(charSequence)) {
                                    this.mLogger.m207e(LOG_TAG, "Check that your GCM sender id is specified correctly", new Object[0]);
                                }
                                onRegisterListener.onFailure(new UpsightException("GCM properties must be set in the Android Manifest with <meta-data android:name=\"com.upsight.gcm\" android:value=\"" + this.mUpsight.getPackageName() + ".GCM_SENDER_ID\" />", new Object[0]));
                            }
                            if (this.mUpsight.getPackageName().equals(substring) || TextUtils.isEmpty(charSequence)) {
                                this.mLogger.m207e(LOG_TAG, "Registration aborted, wrong or no value for com.upsight.gcm was defined", new Object[0]);
                                if (this.mUpsight.getPackageName().equals(substring)) {
                                    this.mLogger.m207e(LOG_TAG, "Check that the package name of your application is specified correctly", new Object[0]);
                                }
                                if (TextUtils.isEmpty(charSequence)) {
                                    this.mLogger.m207e(LOG_TAG, "Check that your GCM sender id is specified correctly", new Object[0]);
                                }
                                onRegisterListener.onFailure(new UpsightException("GCM properties must be set in the Android Manifest with <meta-data android:name=\"com.upsight.gcm\" android:value=\"" + this.mUpsight.getPackageName() + ".GCM_SENDER_ID\" />", new Object[0]));
                            } else {
                                this.mPendingRegisterListeners.add(onRegisterListener);
                                if (!this.mRegistrationIsInProgress) {
                                    registerInBackground(charSequence);
                                }
                            }
                        }
                    }
                    CharSequence charSequence2 = charSequence;
                } catch (NameNotFoundException e3) {
                    e = e3;
                    substring = charSequence;
                    this.mLogger.m207e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
                    if (this.mUpsight.getPackageName().equals(substring)) {
                    }
                    this.mLogger.m207e(LOG_TAG, "Registration aborted, wrong or no value for com.upsight.gcm was defined", new Object[0]);
                    if (this.mUpsight.getPackageName().equals(substring)) {
                        this.mLogger.m207e(LOG_TAG, "Check that the package name of your application is specified correctly", new Object[0]);
                    }
                    if (TextUtils.isEmpty(charSequence)) {
                        this.mLogger.m207e(LOG_TAG, "Check that your GCM sender id is specified correctly", new Object[0]);
                    }
                    onRegisterListener.onFailure(new UpsightException("GCM properties must be set in the Android Manifest with <meta-data android:name=\"com.upsight.gcm\" android:value=\"" + this.mUpsight.getPackageName() + ".GCM_SENDER_ID\" />", new Object[0]));
                }
                if (this.mUpsight.getPackageName().equals(substring)) {
                }
                this.mLogger.m207e(LOG_TAG, "Registration aborted, wrong or no value for com.upsight.gcm was defined", new Object[0]);
                if (this.mUpsight.getPackageName().equals(substring)) {
                    this.mLogger.m207e(LOG_TAG, "Check that the package name of your application is specified correctly", new Object[0]);
                }
                if (TextUtils.isEmpty(charSequence)) {
                    this.mLogger.m207e(LOG_TAG, "Check that your GCM sender id is specified correctly", new Object[0]);
                }
                onRegisterListener.onFailure(new UpsightException("GCM properties must be set in the Android Manifest with <meta-data android:name=\"com.upsight.gcm\" android:value=\"" + this.mUpsight.getPackageName() + ".GCM_SENDER_ID\" />", new Object[0]));
            }
        }
    }

    public void unregister(OnUnregisterListener onUnregisterListener) {
        synchronized (this) {
            if (onUnregisterListener == null) {
                throw new IllegalArgumentException("Listener could not be null");
            }
            if (!isRegistered()) {
                onUnregisterListener.onFailure(new UpsightException("Application is not registered to pushes yet", new Object[0]));
            } else if (this.mRegistrationIsInProgress) {
                onUnregisterListener.onFailure(new UpsightException("Registration is in progress, try later", new Object[0]));
            } else {
                this.mPendingUnregisterListeners.add(onUnregisterListener);
                if (!this.mUnregistrationIsInProgress) {
                    unregisterInBackground();
                }
            }
        }
    }
}
