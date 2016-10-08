package com.upsight.android.analytics.internal.action;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.persistence.annotation.UpsightStorableIdentifier;
import com.upsight.android.persistence.annotation.UpsightStorableType;

@UpsightStorableType("upsight.action_map")
public final class ActionMapResponse {
    @SerializedName("action_factory")
    @Expose
    String actionFactory;
    @SerializedName("action_map")
    @Expose
    JsonArray actionMap;
    @SerializedName("id")
    @Expose
    String actionMapId;
    @UpsightStorableIdentifier
    String id;

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ActionMapResponse actionMapResponse = (ActionMapResponse) obj;
            if (this.id != null) {
                if (!this.id.equals(actionMapResponse.id)) {
                    return false;
                }
            } else if (actionMapResponse.id != null) {
                return false;
            }
        }
        return true;
    }

    public String getActionFactory() {
        return this.actionFactory;
    }

    public JsonArray getActionMap() {
        return this.actionMap;
    }

    public String getActionMapId() {
        return this.actionMapId;
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
