package com.upsight.android.googlepushservices.internal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightGooglePushServicesExtension;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.event.comm.UpsightCommClickEvent;
import com.upsight.android.analytics.event.comm.UpsightCommClickEvent.Builder;
import com.upsight.android.analytics.event.content.UpsightContentUnrenderedEvent;
import com.upsight.android.analytics.internal.session.ApplicationStatus;
import com.upsight.android.analytics.internal.session.ApplicationStatus.State;
import com.upsight.android.analytics.internal.session.SessionInitializerImpl;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesComponent;
import com.upsight.android.marketing.internal.content.DefaultContentMediator;
import javax.inject.Inject;
import org.json.JSONObject;

public class PushClickIntentService extends IntentService {
    private static final String BUNDLE_KEY_MESSAGE_INTENT = "messageIntent";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME = "name";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS = "parameters";
    private static final String CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID = "content_id";
    private static final String LOG_TAG;
    private static final String PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND = "isDispatchFromForeground";
    private static final String PARAM_KEY_PUSH_CONTENT_ID = "contentId";
    private static final String SERVICE_NAME = "UpsightGcmPushClickIntentService";
    @Inject
    SessionManager mSessionManager;

    static {
        LOG_TAG = PushClickIntentService.class.getSimpleName();
    }

    public PushClickIntentService() {
        super(SERVICE_NAME);
    }

    protected static Intent appendMessageIntentBundle(Intent intent, boolean z, Integer num, Integer num2, Integer num3) {
        Bundle bundle = new Bundle();
        if (num != null) {
            bundle.putInt(SessionManager.SESSION_CAMPAIGN_ID, num.intValue());
        }
        if (num2 != null) {
            bundle.putInt(SessionManager.SESSION_MESSAGE_ID, num2.intValue());
        }
        if (num3 != null) {
            bundle.putInt(PARAM_KEY_PUSH_CONTENT_ID, num3.intValue());
        }
        bundle.putBoolean(PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND, z);
        intent.putExtra(UpsightLifeCycleTracker.STARTED_FROM_PUSH, true);
        intent.addFlags(872415232);
        return intent.putExtra(SessionManager.SESSION_EXTRA, bundle);
    }

    static Intent newIntent(Context context, Intent intent, boolean z, Integer num, Integer num2, Integer num3) {
        return new Intent(context.getApplicationContext(), PushClickIntentService.class).putExtra(BUNDLE_KEY_MESSAGE_INTENT, appendMessageIntentBundle(intent, z, num, num2, num3));
    }

    protected void onHandleIntent(Intent intent) {
        UpsightContext createContext = Upsight.createContext(this);
        UpsightGooglePushServicesExtension upsightGooglePushServicesExtension = (UpsightGooglePushServicesExtension) createContext.getUpsightExtension(UpsightGooglePushServicesExtension.EXTENSION_NAME);
        if (upsightGooglePushServicesExtension != null) {
            ((UpsightGooglePushServicesComponent) upsightGooglePushServicesExtension.getComponent()).inject(this);
            Intent intent2 = (Intent) intent.getParcelableExtra(BUNDLE_KEY_MESSAGE_INTENT);
            Bundle bundleExtra = intent2.getBundleExtra(SessionManager.SESSION_EXTRA);
            SessionManager sessionManager = this.mSessionManager;
            if (State.BACKGROUND.name().equals(((ApplicationStatus) createContext.getDataStore().fetchObservable(ApplicationStatus.class).toBlocking().first()).getState().name())) {
                sessionManager.startSession(SessionInitializerImpl.fromPush(bundleExtra));
            }
            if (bundleExtra.containsKey(SessionManager.SESSION_MESSAGE_ID)) {
                Builder createBuilder = UpsightCommClickEvent.createBuilder(Integer.valueOf(bundleExtra.getInt(SessionManager.SESSION_MESSAGE_ID)));
                if (bundleExtra.containsKey(SessionManager.SESSION_CAMPAIGN_ID)) {
                    createBuilder.setMsgCampaignId(Integer.valueOf(bundleExtra.getInt(SessionManager.SESSION_CAMPAIGN_ID)));
                }
                createBuilder.record(createContext);
            }
            if (bundleExtra.containsKey(PARAM_KEY_PUSH_CONTENT_ID)) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_NAME, DefaultContentMediator.CONTENT_PROVIDER);
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_PARAMETERS_KEY_CONTENT_ID, bundleExtra.getInt(PARAM_KEY_PUSH_CONTENT_ID));
                    jSONObject.put(CONTENT_UNRENDERED_CONTENT_PROVIDER_KEY_PARAMETERS, jSONObject2);
                    UpsightContentUnrenderedEvent.Builder scope = UpsightContentUnrenderedEvent.createBuilder(jSONObject).setScope("com_upsight_push_scope");
                    if (bundleExtra.containsKey(SessionManager.SESSION_CAMPAIGN_ID)) {
                        scope.setCampaignId(Integer.valueOf(bundleExtra.getInt(SessionManager.SESSION_CAMPAIGN_ID)));
                    }
                    scope.record(createContext);
                } catch (Throwable e) {
                    createContext.getLogger().m208e(LOG_TAG, e, "Could not construct \"content_provider\" bundle in \"upsight.content.unrendered\"", new Object[0]);
                }
            }
            if (!bundleExtra.getBoolean(PARAM_KEY_IS_DISPATCH_FROM_FOREGROUND, false)) {
                ApplicationStatus applicationStatus = (ApplicationStatus) createContext.getDataStore().fetchObservable(ApplicationStatus.class).toBlocking().first();
                if (!(applicationStatus == null || State.BACKGROUND.equals(applicationStatus.getState()))) {
                    return;
                }
            }
            startActivity(intent2);
        }
    }
}
