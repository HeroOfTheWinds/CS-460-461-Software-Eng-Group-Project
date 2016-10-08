package com.upsight.mediation.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.upsight.mediation.data.APIVersion;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HashMapCaster {
    private final HashMap<String, String> map;

    public HashMapCaster(HashMap<String, String> hashMap) {
        this.map = hashMap;
    }

    @Nullable
    public String get(String str) {
        return (String) this.map.get(str);
    }

    @Nullable
    public APIVersion getAPIVersion(String str) {
        String str2 = (String) this.map.get(str);
        return (str2 == null || str2.length() == 0) ? null : new APIVersion(str2);
    }

    @NonNull
    public ArrayList<APIVersion> getAPIVersions(String str) {
        int i = 0;
        ArrayList<APIVersion> arrayList = new ArrayList();
        String str2 = (String) this.map.get(str);
        String[] split = str2.length() > 0 ? str2.split(",") : new String[0];
        int length = split.length;
        while (i < length) {
            arrayList.add(new APIVersion(split[i]));
            i++;
        }
        return arrayList;
    }

    public boolean getBool(String str) {
        String str2 = (String) this.map.get(str);
        return str2 != null ? str2.equals("1") : false;
    }

    @Nullable
    public Date getDate(String str) {
        String str2 = (String) this.map.get(str);
        return (str2 == null || str2.length() == 0) ? null : new Date(1000 * Long.parseLong(str2));
    }

    public float getFloat(String str) {
        return getFloat(str, -1.0f);
    }

    public float getFloat(String str, float f) {
        String str2 = (String) this.map.get(str);
        return (str2 == null || str2.length() <= 0) ? f : Float.parseFloat(str2);
    }

    public int getInt(String str) {
        return getInt(str, -1);
    }

    public int getInt(String str, int i) {
        String str2 = (String) this.map.get(str);
        return (str2 == null || str2.length() <= 0) ? i : Integer.parseInt((String) this.map.get(str));
    }

    public int[] getIntArray(String str) {
        int i = 0;
        String str2 = (String) this.map.get(str);
        String[] split = str2.length() > 0 ? str2.split(",") : new String[0];
        int[] iArr = new int[split.length];
        while (i < iArr.length) {
            iArr[i] = Integer.parseInt(split[i]);
            i++;
        }
        return iArr;
    }

    public long getLong(String str) {
        String str2 = (String) this.map.get(str);
        return (str2 == null || str2.length() <= 0) ? -1 : Long.parseLong(str2);
    }
}
