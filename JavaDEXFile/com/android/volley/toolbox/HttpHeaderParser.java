package com.android.volley.toolbox;

import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

public class HttpHeaderParser {
    public static Entry parseCacheHeaders(NetworkResponse networkResponse) {
        Object obj;
        Object obj2;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = networkResponse.headers;
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        String str = (String) map.get("Date");
        if (str != null) {
            j = parseDateAsEpoch(str);
        }
        str = (String) map.get("Cache-Control");
        if (str != null) {
            String[] split = str.split(",");
            j2 = 0;
            Object obj3 = null;
            long j4 = 0;
            for (String trim : split) {
                String trim2 = trim2.trim();
                if (trim2.equals("no-cache") || trim2.equals("no-store")) {
                    return null;
                }
                if (trim2.startsWith("max-age=")) {
                    try {
                        j4 = Long.parseLong(trim2.substring(8));
                    } catch (Exception e) {
                    }
                } else if (trim2.startsWith("stale-while-revalidate=")) {
                    try {
                        j2 = Long.parseLong(trim2.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (trim2.equals("must-revalidate") || trim2.equals("proxy-revalidate")) {
                    obj3 = 1;
                }
            }
            obj = 1;
            long j5 = j2;
            j2 = j4;
            obj2 = obj3;
            j3 = j5;
        } else {
            obj2 = null;
            obj = null;
        }
        str = (String) map.get("Expires");
        long parseDateAsEpoch = str != null ? parseDateAsEpoch(str) : 0;
        str = (String) map.get("Last-Modified");
        long parseDateAsEpoch2 = str != null ? parseDateAsEpoch(str) : 0;
        str = (String) map.get("ETag");
        if (obj != null) {
            j2 = (j2 * 1000) + currentTimeMillis;
            j3 = obj2 != null ? j2 : (j3 * 1000) + j2;
        } else if (j <= 0 || parseDateAsEpoch < j) {
            j2 = 0;
            j3 = 0;
        } else {
            j2 = (parseDateAsEpoch - j) + currentTimeMillis;
            j3 = j2;
        }
        Entry entry = new Entry();
        entry.data = networkResponse.data;
        entry.etag = str;
        entry.softTtl = j2;
        entry.ttl = j3;
        entry.serverDate = j;
        entry.lastModified = parseDateAsEpoch2;
        entry.responseHeaders = map;
        return entry;
    }

    public static String parseCharset(Map<String, String> map) {
        return parseCharset(map, "ISO-8859-1");
    }

    public static String parseCharset(Map<String, String> map, String str) {
        String str2 = (String) map.get("Content-Type");
        if (str2 == null) {
            return str;
        }
        String[] split = str2.split(";");
        for (int i = 1; i < split.length; i++) {
            String[] split2 = split[i].trim().split("=");
            if (split2.length == 2 && split2[0].equals("charset")) {
                return split2[1];
            }
        }
        return str;
    }

    public static long parseDateAsEpoch(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }
}
