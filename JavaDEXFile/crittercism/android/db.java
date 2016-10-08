package crittercism.android;

import java.net.MalformedURLException;
import java.net.URL;

public final class db {
    private String f692a;
    private String f693b;

    public db(String str, String str2) {
        str.endsWith("/");
        str2.startsWith("/");
        this.f692a = str;
        this.f693b = str2;
    }

    public final URL m720a() {
        try {
            return new URL(this.f692a + this.f693b);
        } catch (MalformedURLException e) {
            new StringBuilder("Invalid url: ").append(this.f692a).append(this.f693b);
            dx.m778b();
            return null;
        }
    }
}
