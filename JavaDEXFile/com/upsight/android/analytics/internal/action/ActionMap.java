package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upsight.android.logger.UpsightLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ActionMap<T extends Actionable, U extends ActionContext> extends HashMap<String, List<Action<T, U>>> {
    private static final String ACTIONS = "actions";
    private static final String LOG_TEMPLATE_ACTION = "  -> %1$s";
    private static final String LOG_TEMPLATE_TRIGGER = "%1$s on %2$s:";
    private static final String TAG;
    private static final String TRIGGER = "trigger";
    private int mActiveActionCount;
    private boolean mIsActionMapCompleted;
    private UpsightLogger mLogger;

    static {
        TAG = ActionMap.class.getSimpleName();
    }

    public ActionMap(ActionFactory<T, U> actionFactory, U u, JsonArray jsonArray) {
        this.mActiveActionCount = 0;
        this.mIsActionMapCompleted = false;
        this.mLogger = u.mLogger;
        if (jsonArray != null && jsonArray.isJsonArray()) {
            Iterator it = jsonArray.getAsJsonArray().iterator();
            while (it.hasNext()) {
                JsonObject asJsonObject = ((JsonElement) it.next()).getAsJsonObject();
                JsonElement jsonElement = asJsonObject.get(TRIGGER);
                JsonElement jsonElement2 = asJsonObject.get(ACTIONS);
                if (jsonElement != null && jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString() && jsonElement2 != null && jsonElement2.isJsonArray()) {
                    int size = jsonElement2.getAsJsonArray().size();
                    if (size > 0) {
                        List arrayList = new ArrayList(size);
                        for (int i = 0; i < size; i++) {
                            Object obj = null;
                            try {
                                obj = jsonElement2.getAsJsonArray().get(i).getAsJsonObject();
                                arrayList.add(actionFactory.create(u, obj));
                            } catch (Throwable e) {
                                u.mLogger.m208e(TAG, e, "Unable to create action from actionJSON=" + obj, new Object[0]);
                            }
                        }
                        if (arrayList.size() > 0) {
                            put(jsonElement.getAsString(), arrayList);
                        }
                    }
                }
            }
        }
    }

    private boolean isFinished() {
        return this.mIsActionMapCompleted && this.mActiveActionCount == 0;
    }

    public void executeActions(String str, T t) {
        synchronized (this) {
            this.mLogger.m209i(TAG, LOG_TEMPLATE_TRIGGER, str, t.getId());
            List<Action> list = (List) get(str);
            if (list != null) {
                for (Action action : list) {
                    this.mLogger.m209i(TAG, LOG_TEMPLATE_ACTION, action.getType());
                    this.mActiveActionCount++;
                    action.execute(t);
                }
            }
        }
    }

    public boolean signalActionCompleted() {
        boolean isFinished;
        synchronized (this) {
            this.mActiveActionCount--;
            isFinished = isFinished();
        }
        return isFinished;
    }

    public boolean signalActionMapCompleted() {
        boolean isFinished;
        synchronized (this) {
            this.mIsActionMapCompleted = true;
            isFinished = isFinished();
        }
        return isFinished;
    }
}
