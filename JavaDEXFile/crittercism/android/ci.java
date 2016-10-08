package crittercism.android;

import java.io.OutputStream;
import org.json.JSONArray;

public abstract class ci extends bp {
    public abstract JSONArray m536a();

    public final void m537a(OutputStream outputStream) {
        String jSONArray = m536a().toString();
        new StringBuilder("BREADCRUMB WRITING ").append(jSONArray);
        dx.m778b();
        outputStream.write(jSONArray.getBytes());
    }
}
