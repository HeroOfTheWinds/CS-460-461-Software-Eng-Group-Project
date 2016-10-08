package com.upsight.android.internal.persistence.storable;

import com.google.gson.Gson;
import com.upsight.android.UpsightException;
import com.upsight.android.persistence.UpsightStorableSerializer;

public class DefaultJsonSerializer<T> implements UpsightStorableSerializer<T> {
    private final Class<T> mClass;
    private final Gson mGson;

    public DefaultJsonSerializer(Gson gson, Class<T> cls) {
        this.mGson = gson;
        this.mClass = cls;
    }

    public T fromString(String str) throws UpsightException {
        try {
            return this.mGson.fromJson(str, this.mClass);
        } catch (Throwable e) {
            throw new UpsightException(e);
        }
    }

    public String toString(T t) {
        return this.mGson.toJson((Object) t);
    }
}
