package com.upsight.android.marketing.internal.content;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.event.UpsightDynamicEvent;
import com.upsight.android.analytics.event.UpsightDynamicEvent.Builder;
import com.upsight.android.analytics.event.datacollection.UpsightDataCollectionEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.association.Association;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.marketing.C0949R;
import com.upsight.android.marketing.UpsightContentMediator;
import com.upsight.android.marketing.UpsightPurchase;
import com.upsight.android.marketing.UpsightReward;
import com.upsight.android.marketing.internal.content.MarketingContent.ContentLoadedEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.upsight.android.marketing.internal.content.MarketingContent.SubcontentAvailabilityEvent;
import com.upsight.android.marketing.internal.content.MarketingContent.SubdialogAvailabilityEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Scheduler.Worker;
import rx.Subscription;
import rx.functions.Action0;

public final class MarketingContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP;

    private interface InternalFactory {
        Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject);
    }

    /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1 */
    static final class C09721 extends HashMap<String, InternalFactory> {

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.1 */
        class C09631 implements InternalFactory {
            C09631() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new Trigger(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.2 */
        class C09642 implements InternalFactory {
            C09642() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new TriggerIfContentBuilt(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.3 */
        class C09653 implements InternalFactory {
            C09653() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new TriggerIfContentAvailable(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.4 */
        class C09664 implements InternalFactory {
            C09664() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentScopedContent(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.5 */
        class C09675 implements InternalFactory {
            C09675() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentScopedDialog(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.6 */
        class C09686 implements InternalFactory {
            C09686() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentContent(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.7 */
        class C09697 implements InternalFactory {
            C09697() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentDialog(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.8 */
        class C09708 implements InternalFactory {
            C09708() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentScopelessContent(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.1.9 */
        class C09719 implements InternalFactory {
            C09719() {
            }

            public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                return new PresentCloseButton(str, jsonObject, null);
            }
        }

        C09721() {
            put("action_trigger", new C09631());
            put("action_trigger_if_content_built", new C09642());
            put("action_trigger_if_content_available", new C09653());
            put("action_present_scoped_content", new C09664());
            put("action_present_scoped_dialog", new C09675());
            put("action_present_content", new C09686());
            put("action_present_dialog", new C09697());
            put("action_present_scopeless_content", new C09708());
            put("action_present_close_button", new C09719());
            put("action_destroy", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new Destroy(str, jsonObject, null);
                }
            });
            put("action_open_url", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new OpenUrl(str, jsonObject, null);
                }
            });
            put("action_send_event", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new SendEvent(str, jsonObject, null);
                }
            });
            put("action_send_form_data", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new SendFormData(str, jsonObject, null);
                }
            });
            put("action_notify_rewards", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new NotifyRewards(str, jsonObject, null);
                }
            });
            put("action_notify_purchases", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new NotifyPurchases(str, jsonObject, null);
                }
            });
            put("action_associate_once", new InternalFactory() {
                public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
                    return new AssociateOnce(str, jsonObject, null);
                }
            });
        }
    }

    static class AssociateOnce extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String UPSIGHT_DATA = "upsight_data";
        public static final String UPSIGHT_DATA_FILTER = "upsight_data_filter";
        public static final String WITH = "with";

        private AssociateOnce(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Throwable e;
            ActionContext actionContext = getActionContext();
            String optParamString = optParamString(WITH);
            JsonObject optParamJsonObject = optParamJsonObject(UPSIGHT_DATA_FILTER);
            JsonObject optParamJsonObject2 = optParamJsonObject(UPSIGHT_DATA);
            try {
                actionContext.mUpsight.getDataStore().store(Association.from(optParamString, optParamJsonObject, optParamJsonObject2, actionContext.mGson, actionContext.mClock));
            } catch (IllegalArgumentException e2) {
                e = e2;
                actionContext.mLogger.m208e(getClass().getSimpleName(), e, "Failed to parse Association with=" + optParamString + " upsightDataFilter=" + optParamJsonObject + " upsightData" + optParamJsonObject2, new Object[0]);
                marketingContent.signalActionCompleted(actionContext.mBus);
            } catch (IOException e3) {
                e = e3;
                actionContext.mLogger.m208e(getClass().getSimpleName(), e, "Failed to parse Association with=" + optParamString + " upsightDataFilter=" + optParamJsonObject + " upsightData" + optParamJsonObject2, new Object[0]);
                marketingContent.signalActionCompleted(actionContext.mBus);
            }
            marketingContent.signalActionCompleted(actionContext.mBus);
        }
    }

    static class Destroy extends Action<MarketingContent, MarketingContentActionContext> {
        private Destroy(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            String id = marketingContent.getId();
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            if (!TextUtils.isEmpty(id)) {
                marketingContentActionContext.mContentStore.remove(id);
                marketingContentActionContext.mBus.post(new DestroyEvent(null));
            }
            Bus bus = marketingContentActionContext.mBus;
            marketingContent.signalActionCompleted(bus);
            marketingContent.signalActionMapCompleted(bus);
        }
    }

    public static class DestroyEvent {
        public final String mId;

        private DestroyEvent(String str) {
            this.mId = str;
        }
    }

    public static class MarketingContentActionContext extends ActionContext {
        public final MarketingContentMediatorManager mContentMediatorManager;
        public final MarketingContentStore mContentStore;
        public final ContentTemplateWebViewClientFactory mContentTemplateWebViewClientFactory;

        public MarketingContentActionContext(UpsightContext upsightContext, Bus bus, Gson gson, Clock clock, Worker worker, UpsightLogger upsightLogger, MarketingContentMediatorManager marketingContentMediatorManager, MarketingContentStore marketingContentStore, ContentTemplateWebViewClientFactory contentTemplateWebViewClientFactory) {
            super(upsightContext, bus, gson, clock, worker, upsightLogger);
            this.mContentMediatorManager = marketingContentMediatorManager;
            this.mContentStore = marketingContentStore;
            this.mContentTemplateWebViewClientFactory = contentTemplateWebViewClientFactory;
        }
    }

    public static class MarketingContentActionFactory implements ActionFactory<MarketingContent, MarketingContentActionContext> {
        public static final String TYPE = "marketing_content_factory";

        public Action<MarketingContent, MarketingContentActionContext> create(MarketingContentActionContext marketingContentActionContext, JsonObject jsonObject) throws UpsightException {
            if (jsonObject == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String asString = jsonObject.get(ActionFactory.KEY_ACTION_TYPE).getAsString();
            JsonObject asJsonObject = jsonObject.getAsJsonObject(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory internalFactory = (InternalFactory) MarketingContentActions.FACTORY_MAP.get(asString);
            if (internalFactory != null) {
                return internalFactory.create(marketingContentActionContext, asString, asJsonObject);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class NotifyPurchases extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String PURCHASES = "purchases";

        private NotifyPurchases(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Object obj;
            Throwable th;
            List arrayList = new ArrayList();
            JsonElement optParamJsonArray = optParamJsonArray(PURCHASES);
            if (optParamJsonArray != null && optParamJsonArray.isJsonArray()) {
                ActionContext actionContext = getActionContext();
                Iterator it = optParamJsonArray.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    try {
                        optParamJsonArray = (JsonElement) it.next();
                        try {
                            arrayList.add(Purchase.from(optParamJsonArray, actionContext.mGson));
                        } catch (Throwable e) {
                            Throwable th2 = e;
                            obj = optParamJsonArray;
                            th = th2;
                            actionContext.mLogger.m208e(getClass().getSimpleName(), th, "Failed to parse Purchase purchaseJson=" + obj, new Object[0]);
                        }
                    } catch (IOException e2) {
                        th = e2;
                        obj = null;
                        actionContext.mLogger.m208e(getClass().getSimpleName(), th, "Failed to parse Purchase purchaseJson=" + obj, new Object[0]);
                    }
                }
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new PurchasesEvent(arrayList, null));
            marketingContent.signalActionCompleted(bus);
        }
    }

    static class NotifyRewards extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String REWARDS = "rewards";

        private NotifyRewards(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Object obj;
            Throwable th;
            List arrayList = new ArrayList();
            JsonElement optParamJsonArray = optParamJsonArray(REWARDS);
            if (optParamJsonArray != null && optParamJsonArray.isJsonArray()) {
                ActionContext actionContext = getActionContext();
                Iterator it = optParamJsonArray.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    try {
                        optParamJsonArray = (JsonElement) it.next();
                        try {
                            arrayList.add(Reward.from(optParamJsonArray, actionContext.mGson));
                        } catch (Throwable e) {
                            Throwable th2 = e;
                            obj = optParamJsonArray;
                            th = th2;
                            actionContext.mLogger.m208e(getClass().getSimpleName(), th, "Failed to parse Reward rewardJson=" + obj, new Object[0]);
                        }
                    } catch (IOException e2) {
                        th = e2;
                        obj = null;
                        actionContext.mLogger.m208e(getClass().getSimpleName(), th, "Failed to parse Reward rewardJson=" + obj, new Object[0]);
                    }
                }
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new RewardsEvent(arrayList, null));
            marketingContent.signalActionCompleted(bus);
        }
    }

    static class OpenUrl extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String URL = "url";

        private OpenUrl(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            String optParamString = optParamString(URL);
            if (TextUtils.isEmpty(optParamString)) {
                marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action execution failed actionType=" + getType() + " uri=" + optParamString, new Object[0]);
            } else {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(optParamString));
                intent.setFlags(268435456);
                try {
                    marketingContentActionContext.mUpsight.startActivity(intent);
                } catch (Throwable e) {
                    marketingContentActionContext.mLogger.m208e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " intent=" + intent, new Object[0]);
                }
            }
            marketingContent.signalActionCompleted(marketingContentActionContext.mBus);
        }
    }

    static class PresentCloseButton extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String DELAY_MS = "delay_ms";

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.PresentCloseButton.1 */
        class C09731 implements Action0 {
            final /* synthetic */ MarketingContent val$content;

            C09731(MarketingContent marketingContent) {
                this.val$content = marketingContent;
            }

            public void call() {
                View contentView = this.val$content.getContentView();
                if (contentView != null && contentView.getRootView() != null) {
                    ((ImageView) contentView.findViewById(C0949R.id.upsight_marketing_content_view_close_button)).setVisibility(0);
                }
            }
        }

        private PresentCloseButton(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            ((MarketingContentActionContext) getActionContext()).mMainWorker.schedule(new C09731(marketingContent), (long) optParamInt(DELAY_MS), TimeUnit.MILLISECONDS);
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class PresentContent extends Action<MarketingContent, MarketingContentActionContext> {
        private PresentContent(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new SubcontentAvailabilityEvent(marketingContent.getId()));
            marketingContent.signalActionCompleted(bus);
        }
    }

    static class PresentDialog extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String BUTTONS = "buttons";
        public static final String DISMISS_TRIGGER = "dismiss_trigger";
        public static final String MESSAGE = "message";
        public static final String TITLE = "title";

        private PresentDialog(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            String optParamString = optParamString(TITLE);
            String optParamString2 = optParamString(MESSAGE);
            JsonArray optParamJsonArray = optParamJsonArray(BUTTONS);
            String optParamString3 = optParamString(DISMISS_TRIGGER);
            String str = null;
            if (optParamJsonArray != null) {
                str = optParamJsonArray.toString();
            }
            Bus bus = ((MarketingContentActionContext) getActionContext()).mBus;
            bus.post(new SubdialogAvailabilityEvent(marketingContent.getId(), new PendingDialog(marketingContent.getId(), optParamString, optParamString2, str, optParamString3)));
            marketingContent.signalActionCompleted(bus);
        }
    }

    static class PresentScopedContent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String ID = "id";
        public static final String SCOPE_LIST = "scope_list";

        private PresentScopedContent(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Object optParamString = optParamString(ID);
            JsonElement optParamJsonArray = optParamJsonArray(SCOPE_LIST);
            if (!(TextUtils.isEmpty(optParamString) || optParamJsonArray == null || !optParamJsonArray.isJsonArray())) {
                List arrayList = new ArrayList();
                Iterator it = optParamJsonArray.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    optParamJsonArray = (JsonElement) it.next();
                    if (optParamJsonArray.isJsonPrimitive() && optParamJsonArray.getAsJsonPrimitive().isString()) {
                        arrayList.add(optParamJsonArray.getAsString());
                    }
                }
                ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopedContent(optParamString, (String[]) arrayList.toArray(new String[arrayList.size()]));
            }
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class PresentScopedDialog extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String BUTTONS = "buttons";
        public static final String DISMISS_TRIGGER = "dismiss_trigger";
        public static final String MESSAGE = "message";
        public static final String SCOPE_LIST = "scope_list";
        public static final String TITLE = "title";

        private PresentScopedDialog(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            JsonArray optParamJsonArray = optParamJsonArray(SCOPE_LIST);
            String optParamString = optParamString(TITLE);
            String optParamString2 = optParamString(MESSAGE);
            JsonArray optParamJsonArray2 = optParamJsonArray(BUTTONS);
            String optParamString3 = optParamString(DISMISS_TRIGGER);
            List arrayList = new ArrayList();
            Iterator it = optParamJsonArray.iterator();
            while (it.hasNext()) {
                JsonElement jsonElement = (JsonElement) it.next();
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                    arrayList.add(jsonElement.getAsString());
                }
            }
            String str = null;
            if (optParamJsonArray2 != null) {
                str = optParamJsonArray2.toString();
            }
            marketingContent.addPendingDialog(new PendingDialog(marketingContent.getId(), optParamString, optParamString2, str, optParamString3));
            ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopedContent(marketingContent.getId(), (String[]) arrayList.toArray(new String[arrayList.size()]));
            marketingContent.signalActionCompleted(marketingContentActionContext.mBus);
        }
    }

    static class PresentScopelessContent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String NEXT_ID = "next_id";
        public static final String SELF_ID = "self_id";

        private PresentScopelessContent(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Object optParamString = optParamString(SELF_ID);
            Object optParamString2 = optParamString(NEXT_ID);
            if (!(TextUtils.isEmpty(optParamString) || TextUtils.isEmpty(optParamString2))) {
                ((MarketingContentActionContext) getActionContext()).mContentStore.presentScopelessContent(optParamString2, optParamString);
            }
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    public static class PurchasesEvent {
        public final String mId;
        public final List<UpsightPurchase> mPurchases;

        private PurchasesEvent(String str, List<UpsightPurchase> list) {
            this.mId = str;
            this.mPurchases = list;
        }
    }

    public static class RewardsEvent {
        public final String mId;
        public final List<UpsightReward> mRewards;

        private RewardsEvent(String str, List<UpsightReward> list) {
            this.mId = str;
            this.mRewards = list;
        }
    }

    static class SendEvent extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String EVENT = "event";
        public static final String IDENTIFIERS = "identifiers";
        public static final String PUB_DATA = "pub_data";
        public static final String TYPE = "type";
        public static final String UPSIGHT_DATA = "upsight_data";

        private SendEvent(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            JsonObject optParamJsonObject = optParamJsonObject(EVENT);
            if (optParamJsonObject != null) {
                JsonElement jsonElement = optParamJsonObject.get(TYPE);
                if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                    Builder putUpsightData = UpsightDynamicEvent.createBuilder(jsonElement.getAsString()).putUpsightData(optParamJsonObject.get(UPSIGHT_DATA).getAsJsonObject());
                    if (optParamJsonObject.has(PUB_DATA)) {
                        putUpsightData.putPublisherData(optParamJsonObject.getAsJsonObject(PUB_DATA));
                    }
                    if (optParamJsonObject.has(IDENTIFIERS) && optParamJsonObject.get(IDENTIFIERS).isJsonPrimitive() && optParamJsonObject.get(IDENTIFIERS).getAsJsonPrimitive().isString()) {
                        putUpsightData.setDynamicIdentifiers(optParamJsonObject.get(IDENTIFIERS).getAsString());
                    }
                    putUpsightData.record(marketingContentActionContext.mUpsight);
                } else {
                    marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " type=" + jsonElement, new Object[0]);
                }
            } else {
                marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " event=" + optParamJsonObject, new Object[0]);
            }
            marketingContent.signalActionCompleted(marketingContentActionContext.mBus);
        }
    }

    static class SendFormData extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String DATA_KEY = "data_key";
        public static final String STREAM_ID = "stream_id";

        private SendFormData(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            String optParamString = optParamString(DATA_KEY);
            String optParamString2 = optParamString(STREAM_ID);
            if (optParamString == null || optParamString2 == null) {
                marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action failed actionType=" + getType() + " dataKey=" + optParamString, new Object[0]);
            } else {
                optParamString = marketingContent.getExtra(optParamString);
                if (optParamString != null) {
                    UpsightDataCollectionEvent.createBuilder(optParamString, optParamString2).record(marketingContentActionContext.mUpsight);
                }
            }
            marketingContent.signalActionCompleted(marketingContentActionContext.mBus);
        }
    }

    static class Trigger extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String TRIGGER = "trigger";

        private Trigger(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            Object optParamString = optParamString(TRIGGER);
            if (!TextUtils.isEmpty(optParamString)) {
                marketingContent.executeActions(optParamString);
            }
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static class TriggerIfContentAvailable extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String CONDITION_PARAMETERS = "condition_parameters";
        public static final String ELSE_TRIGGER = "else_trigger";
        public static final String ID = "id";
        public static final String THEN_TRIGGER = "then_trigger";
        public static final String TIMEOUT_MS = "timeout_ms";
        private boolean isTriggerExecuted;
        private String mConditionalContentID;
        private MarketingContent mContent;
        private Subscription mSubscription;

        /* renamed from: com.upsight.android.marketing.internal.content.MarketingContentActions.TriggerIfContentAvailable.1 */
        class C09741 implements Action0 {
            final /* synthetic */ MarketingContentActionContext val$actionContext;
            final /* synthetic */ MarketingContent val$content;

            C09741(MarketingContent marketingContent, MarketingContentActionContext marketingContentActionContext) {
                this.val$content = marketingContent;
                this.val$actionContext = marketingContentActionContext;
            }

            public void call() {
                Object access$1700 = TriggerIfContentAvailable.this.optParamString(TriggerIfContentAvailable.ELSE_TRIGGER);
                if (!(TextUtils.isEmpty(access$1700) || TriggerIfContentAvailable.this.isTriggerExecuted)) {
                    this.val$content.executeActions(access$1700);
                    TriggerIfContentAvailable.this.isTriggerExecuted = true;
                }
                this.val$actionContext.mBus.unregister(this);
            }
        }

        private TriggerIfContentAvailable(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
            this.isTriggerExecuted = false;
        }

        public void execute(MarketingContent marketingContent) {
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            this.mContent = marketingContent;
            long j = 0;
            try {
                JsonObject asJsonObject = optParamJsonObject(CONDITION_PARAMETERS).getAsJsonObject();
                this.mConditionalContentID = asJsonObject.get(ID).getAsString();
                j = asJsonObject.get(TIMEOUT_MS).getAsLong();
            } catch (Throwable e) {
                marketingContentActionContext.mLogger.m208e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " invalid CONDITION_PARAMETERS", new Object[0]);
            }
            Object optParamString;
            if (this.mConditionalContentID != null) {
                MarketingContent marketingContent2 = (MarketingContent) marketingContentActionContext.mContentStore.get(this.mConditionalContentID);
                if (marketingContent2 == null || !marketingContent2.isLoaded()) {
                    marketingContentActionContext.mBus.register(this);
                    this.mSubscription = marketingContentActionContext.mMainWorker.schedule(new C09741(marketingContent, marketingContentActionContext), j, TimeUnit.MILLISECONDS);
                } else {
                    optParamString = optParamString(THEN_TRIGGER);
                    if (!(TextUtils.isEmpty(optParamString) || this.isTriggerExecuted)) {
                        marketingContent.executeActions(optParamString);
                        this.isTriggerExecuted = true;
                    }
                }
            } else {
                optParamString = optParamString(ELSE_TRIGGER);
                if (!(TextUtils.isEmpty(optParamString) || this.isTriggerExecuted)) {
                    marketingContent.executeActions(optParamString);
                    this.isTriggerExecuted = true;
                }
            }
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }

        @Subscribe
        public void handleAvailabilityEvent(ContentLoadedEvent contentLoadedEvent) {
            if (contentLoadedEvent.getId().equals(this.mConditionalContentID)) {
                this.mSubscription.unsubscribe();
                ((MarketingContentActionContext) getActionContext()).mBus.unregister(this);
                Object optParamString = optParamString(THEN_TRIGGER);
                if (!TextUtils.isEmpty(optParamString) && !this.isTriggerExecuted) {
                    this.mContent.executeActions(optParamString);
                    this.isTriggerExecuted = true;
                }
            }
        }
    }

    static class TriggerIfContentBuilt extends Action<MarketingContent, MarketingContentActionContext> {
        public static final String CONDITION_PARAMETERS = "condition_parameters";
        public static final String CONTENT_MODEL = "content_model";
        public static final String CONTENT_PROVIDER = "content_provider";
        public static final String CONTENT_PROVIDER_NAME = "name";
        public static final String ELSE_TRIGGER = "else_trigger";
        public static final String THEN_TRIGGER = "then_trigger";

        private TriggerIfContentBuilt(MarketingContentActionContext marketingContentActionContext, String str, JsonObject jsonObject) {
            super(marketingContentActionContext, str, jsonObject);
        }

        public void execute(MarketingContent marketingContent) {
            UpsightContentMediator contentMediator;
            int i;
            Throwable e;
            Object optParamString;
            JsonElement jsonElement = null;
            MarketingContentActionContext marketingContentActionContext = (MarketingContentActionContext) getActionContext();
            JsonElement jsonElement2 = optParamJsonObject(CONDITION_PARAMETERS).get(CONTENT_PROVIDER);
            if (jsonElement2 != null) {
                jsonElement2 = jsonElement2.getAsJsonObject().get(CONTENT_PROVIDER_NAME);
                if (jsonElement2.isJsonPrimitive() && jsonElement2.getAsJsonPrimitive().isString()) {
                    contentMediator = marketingContentActionContext.mContentMediatorManager.getContentMediator(jsonElement2.getAsString());
                    if (contentMediator == null) {
                        contentMediator = marketingContentActionContext.mContentMediatorManager.getDefaultContentMediator();
                    }
                    marketingContent.setContentMediator(contentMediator);
                    jsonElement = optParamJsonObject(CONDITION_PARAMETERS).get(CONTENT_MODEL);
                    if (jsonElement == null && jsonElement.isJsonObject()) {
                        try {
                            marketingContentActionContext.mContentStore.put(marketingContent.getId(), marketingContent);
                            Object buildContentModel = contentMediator.buildContentModel(marketingContent, marketingContentActionContext, jsonElement.getAsJsonObject());
                            i = buildContentModel != null ? 1 : 0;
                            try {
                                marketingContent.setContentModel(buildContentModel);
                                if (i != 0) {
                                    marketingContent.setContentView(contentMediator.buildContentView(marketingContent, marketingContentActionContext));
                                }
                            } catch (Exception e2) {
                                e = e2;
                                marketingContentActionContext.mLogger.m208e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " model=" + jsonElement, new Object[0]);
                                if (i == 0) {
                                    optParamString = optParamString(ELSE_TRIGGER);
                                    if (!TextUtils.isEmpty(optParamString)) {
                                        marketingContent.executeActions(optParamString);
                                    }
                                } else {
                                    optParamString = optParamString(THEN_TRIGGER);
                                    if (!TextUtils.isEmpty(optParamString)) {
                                        marketingContent.executeActions(optParamString);
                                    }
                                }
                                marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
                            }
                        } catch (Exception e3) {
                            e = e3;
                            i = 0;
                            marketingContentActionContext.mLogger.m208e(getClass().getSimpleName(), e, "Action execution failed actionType=" + getType() + " model=" + jsonElement, new Object[0]);
                            if (i == 0) {
                                optParamString = optParamString(THEN_TRIGGER);
                                if (TextUtils.isEmpty(optParamString)) {
                                    marketingContent.executeActions(optParamString);
                                }
                            } else {
                                optParamString = optParamString(ELSE_TRIGGER);
                                if (TextUtils.isEmpty(optParamString)) {
                                    marketingContent.executeActions(optParamString);
                                }
                            }
                            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
                        }
                    }
                    marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action execution failed actionType=" + getType() + " model=" + jsonElement, new Object[0]);
                    i = 0;
                    if (i == 0) {
                        optParamString = optParamString(THEN_TRIGGER);
                        if (TextUtils.isEmpty(optParamString)) {
                            marketingContent.executeActions(optParamString);
                        }
                    } else {
                        optParamString = optParamString(ELSE_TRIGGER);
                        if (TextUtils.isEmpty(optParamString)) {
                            marketingContent.executeActions(optParamString);
                        }
                    }
                    marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
                }
            }
            contentMediator = null;
            if (contentMediator == null) {
                contentMediator = marketingContentActionContext.mContentMediatorManager.getDefaultContentMediator();
            }
            marketingContent.setContentMediator(contentMediator);
            try {
                jsonElement = optParamJsonObject(CONDITION_PARAMETERS).get(CONTENT_MODEL);
            } catch (Throwable e4) {
                marketingContentActionContext.mLogger.m208e(getClass().getSimpleName(), e4, "Action execution failed actionType=" + getType() + " invalid CONDITION_PARAMETERS", new Object[0]);
            }
            if (jsonElement == null) {
            }
            marketingContentActionContext.mLogger.m207e(getClass().getSimpleName(), "Action execution failed actionType=" + getType() + " model=" + jsonElement, new Object[0]);
            i = 0;
            if (i == 0) {
                optParamString = optParamString(ELSE_TRIGGER);
                if (TextUtils.isEmpty(optParamString)) {
                    marketingContent.executeActions(optParamString);
                }
            } else {
                optParamString = optParamString(THEN_TRIGGER);
                if (TextUtils.isEmpty(optParamString)) {
                    marketingContent.executeActions(optParamString);
                }
            }
            marketingContent.signalActionCompleted(((MarketingContentActionContext) getActionContext()).mBus);
        }
    }

    static {
        FACTORY_MAP = new C09721();
    }

    private MarketingContentActions() {
    }
}
