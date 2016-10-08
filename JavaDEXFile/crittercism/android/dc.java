package crittercism.android;

import com.crittercism.app.CrittercismConfig;
import com.mopub.volley.DefaultRetryPolicy;
import com.voxelbusters.nativeplugins.defines.Keys.Mime;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public final class dc {
    private static SSLSocketFactory f694a;
    private URL f695b;
    private Map f696c;
    private int f697d;
    private boolean f698e;
    private boolean f699f;
    private String f700g;
    private boolean f701h;
    private int f702i;

    static {
        f694a = null;
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, null, null);
            SSLSocketFactory socketFactory = instance.getSocketFactory();
            if (socketFactory == null) {
                return;
            }
            if (socketFactory instanceof ab) {
                f694a = ((ab) socketFactory).m261a();
            } else {
                f694a = socketFactory;
            }
        } catch (GeneralSecurityException e) {
            f694a = null;
        }
    }

    public dc(URL url) {
        this.f696c = new HashMap();
        this.f697d = 0;
        this.f698e = true;
        this.f699f = true;
        this.f700g = "POST";
        this.f701h = false;
        this.f702i = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
        this.f695b = url;
        this.f696c.put("User-Agent", Arrays.asList(new String[]{CrittercismConfig.API_VERSION}));
        this.f696c.put("Content-Type", Arrays.asList(new String[]{"application/json"}));
        this.f696c.put("Accept", Arrays.asList(new String[]{Mime.PLAIN_TEXT, "application/json"}));
    }

    public final HttpURLConnection m721a() {
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.f695b.openConnection();
        for (Entry entry : this.f696c.entrySet()) {
            for (String addRequestProperty : (List) entry.getValue()) {
                httpURLConnection.addRequestProperty((String) entry.getKey(), addRequestProperty);
            }
        }
        httpURLConnection.setConnectTimeout(this.f702i);
        httpURLConnection.setReadTimeout(this.f702i);
        httpURLConnection.setDoInput(this.f698e);
        httpURLConnection.setDoOutput(this.f699f);
        if (this.f701h) {
            httpURLConnection.setChunkedStreamingMode(this.f697d);
        }
        httpURLConnection.setRequestMethod(this.f700g);
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            if (f694a != null) {
                httpsURLConnection.setSSLSocketFactory(f694a);
            } else {
                throw new GeneralSecurityException();
            }
        }
        return httpURLConnection;
    }
}
