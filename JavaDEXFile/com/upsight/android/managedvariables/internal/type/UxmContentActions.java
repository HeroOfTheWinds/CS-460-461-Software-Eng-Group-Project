package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.otto.Bus;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.UpsightManagedVariablesExtension;
import com.upsight.android.analytics.event.uxm.UpsightUxmEnumerateEvent;
import com.upsight.android.analytics.internal.action.Action;
import com.upsight.android.analytics.internal.action.ActionContext;
import com.upsight.android.analytics.internal.action.ActionFactory;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.UpsightManagedVariablesComponent;
import com.upsight.android.persistence.UpsightDataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import rx.Observable;
import rx.Scheduler.Worker;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public final class UxmContentActions {
    private static final Map<String, InternalFactory> FACTORY_MAP;

    private interface InternalFactory {
        Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject);
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1 */
    static final class C09341 extends HashMap<String, InternalFactory> {

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.1 */
        class C09291 implements InternalFactory {
            C09291() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
                return new UxmEnumerate(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.2 */
        class C09302 implements InternalFactory {
            C09302() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
                return new SetBundleId(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.3 */
        class C09313 implements InternalFactory {
            C09313() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
                return new ModifyValue(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.4 */
        class C09324 implements InternalFactory {
            C09324() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
                return new NotifyUxmValuesSynchronized(str, jsonObject, null);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.1.5 */
        class C09335 implements InternalFactory {
            C09335() {
            }

            public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
                return new Destroy(str, jsonObject, null);
            }
        }

        C09341() {
            put("action_uxm_enumerate", new C09291());
            put("action_set_bundle_id", new C09302());
            put("action_modify_value", new C09313());
            put("action_notify_uxm_values_synchronized", new C09324());
            put("action_destroy", new C09335());
        }
    }

    static class Destroy extends Action<UxmContent, UxmContentActionContext> {
        private Destroy(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
            super(uxmContentActionContext, str, jsonObject);
        }

        public void execute(UxmContent uxmContent) {
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            uxmContent.signalActionCompleted(bus);
            uxmContent.signalActionMapCompleted(bus);
        }
    }

    static class ModifyValue extends Action<UxmContent, UxmContentActionContext> {
        private static final String MATCH = "match";
        private static final String OPERATOR = "operator";
        private static final String OPERATOR_SET = "set";
        private static final String PROPERTY_NAME = "property_name";
        private static final String PROPERTY_VALUE = "property_value";
        private static final String TYPE = "type";
        private static final String VALUES = "values";

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.1 */
        class C09351 implements Func1<T, JsonElement> {
            final /* synthetic */ Gson val$gson;

            C09351(Gson gson) {
                this.val$gson = gson;
            }

            public JsonElement call(T t) {
                return this.val$gson.toJsonTree(t);
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.2 */
        class C09362 implements Func1<JsonObject, Boolean> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonElement val$propertyValue;

            C09362(String str, JsonElement jsonElement) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonElement;
            }

            public Boolean call(JsonObject jsonObject) {
                return Boolean.valueOf(jsonObject.getAsJsonObject().get(this.val$propertyName).equals(this.val$propertyValue));
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.3 */
        class C09373 implements Func1<JsonObject, JsonObject> {
            final /* synthetic */ String val$propertyName;
            final /* synthetic */ JsonElement val$propertyValue;

            C09373(String str, JsonElement jsonElement) {
                this.val$propertyName = str;
                this.val$propertyValue = jsonElement;
            }

            public JsonObject call(JsonObject jsonObject) {
                jsonObject.add(this.val$propertyName, this.val$propertyValue);
                return jsonObject;
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4 */
        class C09414 implements Action1<JsonObject> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightDataStore val$dataStore;
            final /* synthetic */ Gson val$gson;
            final /* synthetic */ UpsightLogger val$logger;

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.1 */
            class C09381 implements Action1<T> {
                final /* synthetic */ JsonObject val$modelNode;

                C09381(JsonObject jsonObject) {
                    this.val$modelNode = jsonObject;
                }

                public void call(T t) {
                    C09414.this.val$logger.m205d(Upsight.LOG_TAG, "Modified managed variable of class " + C09414.this.val$clazz + " with value " + this.val$modelNode, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.2 */
            class C09392 implements Action1<Throwable> {
                C09392() {
                }

                public void call(Throwable th) {
                    C09414.this.val$logger.m208e(Upsight.LOG_TAG, th, "Failed to modify managed variable of class " + C09414.this.val$clazz, new Object[0]);
                }
            }

            /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.4.3 */
            class C09403 implements Action0 {
                C09403() {
                }

                public void call() {
                    C09414.this.val$content.signalActionCompleted(C09414.this.val$actionContext.mBus);
                }
            }

            C09414(UpsightDataStore upsightDataStore, Gson gson, Class cls, UxmContentActionContext uxmContentActionContext, UpsightLogger upsightLogger, UxmContent uxmContent) {
                this.val$dataStore = upsightDataStore;
                this.val$gson = gson;
                this.val$clazz = cls;
                this.val$actionContext = uxmContentActionContext;
                this.val$logger = upsightLogger;
                this.val$content = uxmContent;
            }

            public void call(JsonObject jsonObject) {
                try {
                    this.val$dataStore.storeObservable(this.val$gson.fromJson((JsonElement) jsonObject, this.val$clazz)).subscribeOn(this.val$actionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(this.val$actionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new C09381(jsonObject), new C09392(), new C09403());
                } catch (Throwable e) {
                    this.val$logger.m208e(Upsight.LOG_TAG, e, "Failed to parse managed variable of class " + this.val$clazz, new Object[0]);
                    this.val$content.signalActionCompleted(this.val$actionContext.mBus);
                }
            }
        }

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmContentActions.ModifyValue.5 */
        class C09425 implements Action1<Throwable> {
            final /* synthetic */ UxmContentActionContext val$actionContext;
            final /* synthetic */ Class val$clazz;
            final /* synthetic */ UxmContent val$content;
            final /* synthetic */ UpsightLogger val$logger;

            C09425(UpsightLogger upsightLogger, Class cls, UxmContent uxmContent, UxmContentActionContext uxmContentActionContext) {
                this.val$logger = upsightLogger;
                this.val$clazz = cls;
                this.val$content = uxmContent;
                this.val$actionContext = uxmContentActionContext;
            }

            public void call(Throwable th) {
                this.val$logger.m208e(Upsight.LOG_TAG, th, "Failed to fetch managed variable of class " + this.val$clazz, new Object[0]);
                this.val$content.signalActionCompleted(this.val$actionContext.mBus);
            }
        }

        private ModifyValue(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
            super(uxmContentActionContext, str, jsonObject);
        }

        private <T> void modifyValue(UxmContent uxmContent, Class<T> cls, JsonArray jsonArray, JsonArray jsonArray2) {
            UxmContentActionContext uxmContentActionContext = (UxmContentActionContext) getActionContext();
            Gson gson = uxmContentActionContext.mGson;
            UpsightLogger logger = uxmContentActionContext.mUpsight.getLogger();
            UpsightDataStore dataStore = uxmContentActionContext.mUpsight.getDataStore();
            Observable cast = dataStore.fetchObservable(cls).map(new C09351(gson)).cast(JsonObject.class);
            JsonObject jsonObject = new JsonObject();
            Iterator it = jsonArray.iterator();
            Observable observable = cast;
            while (it.hasNext()) {
                JsonElement jsonElement = (JsonElement) it.next();
                String asString = jsonElement.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                JsonElement jsonElement2 = jsonElement.getAsJsonObject().get(PROPERTY_VALUE);
                cast = observable.filter(new C09362(asString, jsonElement2));
                jsonObject.add(asString, jsonElement2);
                observable = cast;
            }
            cast = observable.defaultIfEmpty(jsonObject);
            Iterator it2 = jsonArray2.iterator();
            observable = cast;
            while (it2.hasNext()) {
                jsonElement = (JsonElement) it2.next();
                String asString2 = jsonElement.getAsJsonObject().get(OPERATOR).getAsString();
                asString = jsonElement.getAsJsonObject().get(PROPERTY_NAME).getAsString();
                jsonElement = jsonElement.getAsJsonObject().get(PROPERTY_VALUE);
                if (OPERATOR_SET.equals(asString2)) {
                    observable = observable.map(new C09373(asString, jsonElement));
                }
            }
            observable.subscribeOn(uxmContentActionContext.mUpsight.getCoreComponent().subscribeOnScheduler()).observeOn(uxmContentActionContext.mUpsight.getCoreComponent().observeOnScheduler()).subscribe(new C09414(dataStore, gson, cls, uxmContentActionContext, logger, uxmContent), new C09425(logger, cls, uxmContent, uxmContentActionContext));
        }

        public void execute(UxmContent uxmContent) {
            int i;
            ActionContext actionContext = getActionContext();
            if (uxmContent.shouldApplyBundle()) {
                Object optParamString = optParamString(TYPE);
                JsonArray optParamJsonArray = optParamJsonArray(MATCH);
                JsonArray optParamJsonArray2 = optParamJsonArray(VALUES);
                if (!(TextUtils.isEmpty(optParamString) || optParamJsonArray == null || optParamJsonArray2 == null)) {
                    Class cls = null;
                    if ("com.upsight.uxm.string".equals(optParamString)) {
                        cls = Model.class;
                    } else if ("com.upsight.uxm.boolean".equals(optParamString)) {
                        cls = Model.class;
                    } else if ("com.upsight.uxm.integer".equals(optParamString)) {
                        cls = Model.class;
                    } else if ("com.upsight.uxm.float".equals(optParamString)) {
                        cls = Model.class;
                    }
                    if (cls != null) {
                        modifyValue(uxmContent, cls, optParamJsonArray, optParamJsonArray2);
                        i = 0;
                    } else {
                        actionContext.mLogger.m207e(Upsight.LOG_TAG, "Failed to execute action_modify_value due to unknown managed variable type " + optParamString, new Object[0]);
                        i = 1;
                    }
                    if (i != 0) {
                        uxmContent.signalActionCompleted(actionContext.mBus);
                    }
                }
            }
            i = 1;
            if (i != 0) {
                uxmContent.signalActionCompleted(actionContext.mBus);
            }
        }
    }

    static class NotifyUxmValuesSynchronized extends Action<UxmContent, UxmContentActionContext> {
        private static final String TAGS = "tags";

        private NotifyUxmValuesSynchronized(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
            super(uxmContentActionContext, str, jsonObject);
        }

        public void execute(UxmContent uxmContent) {
            List arrayList = new ArrayList();
            JsonArray optParamJsonArray = optParamJsonArray(TAGS);
            if (uxmContent.shouldApplyBundle() && optParamJsonArray != null) {
                Iterator it = optParamJsonArray.iterator();
                while (it.hasNext()) {
                    JsonElement jsonElement = (JsonElement) it.next();
                    if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
                        arrayList.add(jsonElement.getAsString());
                    }
                }
            }
            Bus bus = ((UxmContentActionContext) getActionContext()).mBus;
            bus.post(new ScheduleSyncNotificationEvent(arrayList, null));
            uxmContent.signalActionCompleted(bus);
        }
    }

    public static class ScheduleSyncNotificationEvent {
        public final String mId;
        public final List<String> mTags;

        private ScheduleSyncNotificationEvent(String str, List<String> list) {
            this.mId = str;
            this.mTags = list;
        }
    }

    static class SetBundleId extends Action<UxmContent, UxmContentActionContext> {
        private static final String BUNDLE_ID = "bundle.id";

        private SetBundleId(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
            super(uxmContentActionContext, str, jsonObject);
        }

        public void execute(UxmContent uxmContent) {
            if (uxmContent.shouldApplyBundle()) {
                PreferencesHelper.putString(((UxmContentActionContext) getActionContext()).mUpsight, UxmContent.PREFERENCES_KEY_UXM_BUNDLE_ID, optParamString(BUNDLE_ID));
            }
            uxmContent.signalActionCompleted(((UxmContentActionContext) getActionContext()).mBus);
        }
    }

    public static class UxmContentActionContext extends ActionContext {
        public UxmContentActionContext(UpsightContext upsightContext, Bus bus, Gson gson, Clock clock, Worker worker, UpsightLogger upsightLogger) {
            super(upsightContext, bus, gson, clock, worker, upsightLogger);
        }
    }

    public static class UxmContentActionFactory implements ActionFactory<UxmContent, UxmContentActionContext> {
        public static final String TYPE = "datastore_factory";

        public Action<UxmContent, UxmContentActionContext> create(UxmContentActionContext uxmContentActionContext, JsonObject jsonObject) throws UpsightException {
            if (jsonObject == null) {
                throw new UpsightException("Failed to create Action. JSON is null.", new Object[0]);
            }
            String asString = jsonObject.get(ActionFactory.KEY_ACTION_TYPE).getAsString();
            JsonObject asJsonObject = jsonObject.getAsJsonObject(ActionFactory.KEY_ACTION_PARAMS);
            InternalFactory internalFactory = (InternalFactory) UxmContentActions.FACTORY_MAP.get(asString);
            if (internalFactory != null) {
                return internalFactory.create(uxmContentActionContext, asString, asJsonObject);
            }
            throw new UpsightException("Failed to create Action. Unknown action type.", new Object[0]);
        }
    }

    static class UxmEnumerate extends Action<UxmContent, UxmContentActionContext> {
        private UxmEnumerate(UxmContentActionContext uxmContentActionContext, String str, JsonObject jsonObject) {
            super(uxmContentActionContext, str, jsonObject);
        }

        public void execute(UxmContent uxmContent) {
            ActionContext actionContext = getActionContext();
            UpsightManagedVariablesExtension upsightManagedVariablesExtension = (UpsightManagedVariablesExtension) actionContext.mUpsight.getUpsightExtension(UpsightManagedVariablesExtension.EXTENSION_NAME);
            if (upsightManagedVariablesExtension != null) {
                try {
                    UpsightUxmEnumerateEvent.createBuilder(new JSONArray(((UpsightManagedVariablesComponent) upsightManagedVariablesExtension.getComponent()).uxmSchema().mSchemaJsonString)).record(actionContext.mUpsight);
                } catch (Throwable e) {
                    actionContext.mUpsight.getLogger().m208e(Upsight.LOG_TAG, e, "Failed to send UXM enumerate event", new Object[0]);
                }
            }
            uxmContent.signalActionCompleted(actionContext.mBus);
        }
    }

    static {
        FACTORY_MAP = new C09341();
    }

    private UxmContentActions() {
    }
}
