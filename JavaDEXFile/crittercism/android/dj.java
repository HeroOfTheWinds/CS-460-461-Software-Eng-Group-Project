package crittercism.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import org.json.JSONException;
import org.json.JSONObject;

public final class dj extends di {
    private cw f725a;
    private dc f726b;
    private boolean f727c;
    private cy f728d;

    public dj(cw cwVar, dc dcVar, cy cyVar) {
        this(cwVar, dcVar, false, cyVar);
    }

    public dj(cw cwVar, dc dcVar, boolean z, cy cyVar) {
        this.f725a = cwVar;
        this.f726b = dcVar;
        this.f727c = z;
        this.f728d = cyVar;
    }

    public final void m736a() {
        int responseCode;
        JSONObject jSONObject;
        int i;
        boolean z;
        UnsupportedEncodingException e;
        SocketTimeoutException e2;
        IOException e3;
        JSONException e4;
        try {
            URLConnection a = this.f726b.m721a();
            if (a != null) {
                try {
                    this.f725a.m701a(a.getOutputStream());
                    responseCode = a.getResponseCode();
                    try {
                        if (this.f727c) {
                            StringBuilder stringBuilder = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a.getInputStream()));
                            while (true) {
                                int read = bufferedReader.read();
                                if (read == -1) {
                                    break;
                                }
                                stringBuilder.append((char) read);
                            }
                            bufferedReader.close();
                            jSONObject = new JSONObject(stringBuilder.toString());
                        } else {
                            jSONObject = null;
                        }
                        i = responseCode;
                        z = false;
                    } catch (UnsupportedEncodingException e5) {
                        e = e5;
                        new StringBuilder("UnsupportedEncodingException in proceed(): ").append(e.getMessage());
                        dx.m778b();
                        dx.m781c();
                        jSONObject = null;
                        i = responseCode;
                        z = false;
                        a.disconnect();
                        if (this.f728d != null) {
                            this.f728d.m707a(z, i, jSONObject);
                        }
                    } catch (SocketTimeoutException e6) {
                        e2 = e6;
                        new StringBuilder("SocketTimeoutException in proceed(): ").append(e2.getMessage());
                        dx.m778b();
                        i = responseCode;
                        z = true;
                        jSONObject = null;
                        a.disconnect();
                        if (this.f728d != null) {
                            this.f728d.m707a(z, i, jSONObject);
                        }
                    } catch (IOException e7) {
                        e3 = e7;
                        new StringBuilder("IOException in proceed(): ").append(e3.getMessage());
                        dx.m778b();
                        dx.m781c();
                        jSONObject = null;
                        i = responseCode;
                        z = false;
                        a.disconnect();
                        if (this.f728d != null) {
                            this.f728d.m707a(z, i, jSONObject);
                        }
                    } catch (JSONException e8) {
                        e4 = e8;
                        new StringBuilder("JSONException in proceed(): ").append(e4.getMessage());
                        dx.m778b();
                        dx.m781c();
                        jSONObject = null;
                        i = responseCode;
                        z = false;
                        a.disconnect();
                        if (this.f728d != null) {
                            this.f728d.m707a(z, i, jSONObject);
                        }
                    }
                } catch (UnsupportedEncodingException e9) {
                    e = e9;
                    responseCode = -1;
                    new StringBuilder("UnsupportedEncodingException in proceed(): ").append(e.getMessage());
                    dx.m778b();
                    dx.m781c();
                    jSONObject = null;
                    i = responseCode;
                    z = false;
                    a.disconnect();
                    if (this.f728d != null) {
                        this.f728d.m707a(z, i, jSONObject);
                    }
                } catch (SocketTimeoutException e10) {
                    e2 = e10;
                    responseCode = -1;
                    new StringBuilder("SocketTimeoutException in proceed(): ").append(e2.getMessage());
                    dx.m778b();
                    i = responseCode;
                    z = true;
                    jSONObject = null;
                    a.disconnect();
                    if (this.f728d != null) {
                        this.f728d.m707a(z, i, jSONObject);
                    }
                } catch (IOException e11) {
                    e3 = e11;
                    responseCode = -1;
                    new StringBuilder("IOException in proceed(): ").append(e3.getMessage());
                    dx.m778b();
                    dx.m781c();
                    jSONObject = null;
                    i = responseCode;
                    z = false;
                    a.disconnect();
                    if (this.f728d != null) {
                        this.f728d.m707a(z, i, jSONObject);
                    }
                } catch (JSONException e12) {
                    e4 = e12;
                    responseCode = -1;
                    new StringBuilder("JSONException in proceed(): ").append(e4.getMessage());
                    dx.m778b();
                    dx.m781c();
                    jSONObject = null;
                    i = responseCode;
                    z = false;
                    a.disconnect();
                    if (this.f728d != null) {
                        this.f728d.m707a(z, i, jSONObject);
                    }
                }
                a.disconnect();
                if (this.f728d != null) {
                    this.f728d.m707a(z, i, jSONObject);
                }
            }
        } catch (IOException e13) {
        } catch (GeneralSecurityException e14) {
        }
    }
}
