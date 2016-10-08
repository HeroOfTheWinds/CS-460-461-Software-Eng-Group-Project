package crittercism.android;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public final class ca extends bq {

    /* renamed from: crittercism.android.ca.a */
    public static final class C1109a extends cj {
        public final bq m676a(File file) {
            return new ca((byte) 0);
        }
    }

    private ca(File file) {
        super(file);
    }

    public final Object m677a() {
        try {
            return new JSONObject((String) super.m559a());
        } catch (JSONException e) {
            return null;
        }
    }
}
