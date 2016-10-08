package com.upsight.android.analytics.internal.association;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.upsight.android.analytics.internal.association.Association.UpsightDataFilter;
import com.upsight.android.analytics.internal.session.Clock;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.annotation.Created;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import rx.functions.Action1;

class AssociationManagerImpl implements AssociationManager {
    static final long ASSOCIATION_EXPIRY = 604800000;
    private static final String KEY_UPSIGHT_DATA = "upsight_data";
    private final Map<String, Set<Association>> mAssociations;
    private final Clock mClock;
    private final UpsightDataStore mDataStore;
    private boolean mIsLaunched;

    /* renamed from: com.upsight.android.analytics.internal.association.AssociationManagerImpl.1 */
    class C08581 implements Action1<Association> {
        C08581() {
        }

        public void call(Association association) {
            AssociationManagerImpl.this.addAssociation(association.getWith(), association);
        }
    }

    AssociationManagerImpl(UpsightDataStore upsightDataStore, Clock clock) {
        this.mIsLaunched = false;
        this.mDataStore = upsightDataStore;
        this.mClock = clock;
        this.mAssociations = new HashMap();
    }

    void addAssociation(String str, Association association) {
        synchronized (this) {
            if (!(TextUtils.isEmpty(str) || association == null)) {
                Set set = (Set) this.mAssociations.get(str);
                if (set == null) {
                    set = new HashSet();
                }
                set.add(association);
                this.mAssociations.put(str, set);
            }
        }
    }

    public void associate(String str, JsonObject jsonObject) {
        synchronized (this) {
            associateInner(str, jsonObject);
        }
    }

    void associateInner(String str, JsonObject jsonObject) {
        synchronized (this) {
            Set set = (Set) this.mAssociations.get(str);
            if (set != null) {
                Iterator it = set.iterator();
                Object obj = null;
                while (it.hasNext()) {
                    Association association = (Association) it.next();
                    if (this.mClock.currentTimeMillis() - association.getTimestampMs() > ASSOCIATION_EXPIRY) {
                        it.remove();
                        if (!TextUtils.isEmpty(association.getId())) {
                            this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                        }
                    } else if (obj == null) {
                        UpsightDataFilter upsightDataFilter = association.getUpsightDataFilter();
                        JsonElement jsonElement = jsonObject.get(KEY_UPSIGHT_DATA);
                        if (jsonElement != null && jsonElement.isJsonObject()) {
                            JsonObject asJsonObject = jsonElement.getAsJsonObject();
                            JsonElement jsonElement2 = asJsonObject.get(upsightDataFilter.matchKey);
                            if (jsonElement2 != null && jsonElement2.isJsonPrimitive()) {
                                Iterator it2 = upsightDataFilter.matchValues.iterator();
                                while (it2.hasNext()) {
                                    if (jsonElement2.equals((JsonElement) it2.next())) {
                                        for (Entry entry : association.getUpsightData().entrySet()) {
                                            asJsonObject.add((String) entry.getKey(), (JsonElement) entry.getValue());
                                        }
                                        it.remove();
                                        if (!TextUtils.isEmpty(association.getId())) {
                                            this.mDataStore.removeObservable(Association.class, association.getId()).subscribe();
                                        }
                                        obj = 1;
                                    }
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    @Created
    public void handleCreate(Association association) {
        addAssociation(association.getWith(), association);
    }

    public void launch() {
        synchronized (this) {
            if (!this.mIsLaunched) {
                this.mIsLaunched = true;
                launchInner();
            }
        }
    }

    void launchInner() {
        synchronized (this) {
            this.mDataStore.subscribe(this);
            this.mDataStore.fetchObservable(Association.class).subscribe(new C08581());
        }
    }
}
