package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.upsight.android.analytics.internal.action.ActionMap;
import com.upsight.android.analytics.internal.action.ActionMapResponse;
import com.upsight.android.managedvariables.experience.UpsightUserExperience;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionContext;
import com.upsight.android.managedvariables.internal.type.UxmContentActions.UxmContentActionFactory;
import java.util.Iterator;

public final class UxmContentFactory {
    private static final String ACTION_MODIFY_VALUE = "action_modify_value";
    private static final String ACTION_SET_BUNDLE_ID = "action_set_bundle_id";
    private static final String KEY_ACTIONS = "actions";
    private static final String KEY_ACTION_TYPE = "action_type";
    private static final UxmContentActionFactory sUxmContentActionFactory;
    private UxmContentActionContext mActionContext;
    private UpsightUserExperience mUserExperience;

    static {
        sUxmContentActionFactory = new UxmContentActionFactory();
    }

    public UxmContentFactory(UxmContentActionContext uxmContentActionContext, UpsightUserExperience upsightUserExperience) {
        this.mActionContext = uxmContentActionContext;
        this.mUserExperience = upsightUserExperience;
    }

    public UxmContent create(ActionMapResponse actionMapResponse) {
        Object actionMapId = actionMapResponse.getActionMapId();
        if (!TextUtils.isEmpty(actionMapId) && UxmContentActionFactory.TYPE.equals(actionMapResponse.getActionFactory())) {
            JsonElement actionMap = actionMapResponse.getActionMap();
            if (actionMap != null && actionMap.isJsonArray()) {
                boolean onReceive;
                Iterator it = actionMap.getAsJsonArray().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    JsonElement jsonElement = ((JsonElement) it.next()).getAsJsonObject().get(KEY_ACTIONS);
                    if (jsonElement != null && jsonElement.isJsonArray()) {
                        Iterator it2 = jsonElement.getAsJsonArray().iterator();
                        while (it2.hasNext()) {
                            jsonElement = ((JsonElement) it2.next()).getAsJsonObject().get(KEY_ACTION_TYPE);
                            if (!ACTION_SET_BUNDLE_ID.equals(jsonElement.getAsString())) {
                                if (ACTION_MODIFY_VALUE.equals(jsonElement.getAsString())) {
                                }
                            }
                            onReceive = this.mUserExperience.getHandler().onReceive();
                            break;
                        }
                    }
                    onReceive = z;
                    if (onReceive) {
                        break;
                    }
                    z = onReceive;
                }
                onReceive = z;
                return UxmContent.create(actionMapId, new ActionMap(sUxmContentActionFactory, this.mActionContext, actionMap.getAsJsonArray()), onReceive);
            }
        }
        return null;
    }
}
