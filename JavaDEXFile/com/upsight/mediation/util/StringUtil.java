package com.upsight.mediation.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class StringUtil {
    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.trim().equals(BuildConfig.FLAVOR) || str.length() == 0;
    }

    @NonNull
    public static String join(@NonNull List list, @NonNull String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(list.get(i));
            if (i < size - 1) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String join(@NonNull int[] iArr, @NonNull String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(iArr[i]);
            if (i < length - 1) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String join(@NonNull String[] strArr, @NonNull String str) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(strArr[i]);
            if (i < length - 1) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static int[] toIntArray(@NonNull String str, @NonNull String str2) {
        String[] split = str.split(str2);
        int length = split.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = Integer.parseInt(split[i]);
        }
        return iArr;
    }
}
