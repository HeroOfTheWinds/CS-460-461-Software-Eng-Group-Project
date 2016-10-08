package com.upsight.mediation.mraid;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.upsight.mediation.log.FuseLog;
import java.io.IOException;
import java.io.InputStream;

public class MRaidDrawables {
    private static final String TAG = "MRaidDrawables";

    public static Drawable getDrawableForImage(Context context, String str, String str2, int i) {
        InputStream resourceAsStream = context.getClass().getResourceAsStream(str);
        if (resourceAsStream != null) {
            Drawable bitmapDrawable = new BitmapDrawable(context.getResources(), resourceAsStream);
            try {
                resourceAsStream.close();
                return bitmapDrawable;
            } catch (IOException e) {
                return bitmapDrawable;
            }
        }
        int identifier = context.getResources().getIdentifier(str2, "drawable", context.getPackageName());
        if (identifier != 0) {
            return context.getResources().getDrawable(identifier);
        }
        FuseLog.m240w(TAG, "Could not load button image: " + str2);
        return new ColorDrawable(i);
    }
}
