package crittercism.android;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;

public final class bz extends bq {

    /* renamed from: crittercism.android.bz.a */
    public static final class C1106a extends cj {
        public final bq m653a(File file) {
            return new bz((byte) 0);
        }
    }

    private bz(File file) {
        super(file);
    }

    public final Object m654a() {
        try {
            return new JSONArray((String) super.m559a());
        } catch (JSONException e) {
            return null;
        }
    }
}
