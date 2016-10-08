package com.mopub.volley.toolbox;

import com.mopub.volley.Cache.Entry;
import com.mopub.volley.NetworkResponse;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

public class HttpHeaderParser {
    public static Entry parseCacheHeaders(NetworkResponse networkResponse) {
        Object obj;
        long currentTimeMillis = System.currentTimeMillis();
        Map map = networkResponse.headers;
        long j = 0;
        long j2 = 0;
        String str = (String) map.get("Date");
        if (str != null) {
            j = parseDateAsEpoch(str);
        }
        str = (String) map.get("Cache-Control");
        if (str != null) {
            String[] split = str.split(",");
            int i = 0;
            long j3 = 0;
            while (i < split.length) {
                String trim = split[i].trim();
                if (!trim.equals("no-cache")) {
                    if (!trim.equals("no-store")) {
                        if (trim.startsWith("max-age=")) {
                            try {
                                j3 = Long.parseLong(trim.substring(8));
                            } catch (Exception e) {
                            }
                        } else {
                            if (!trim.equals("must-revalidate")) {
                                if (!trim.equals("proxy-revalidate")) {
                                }
                            }
                            j3 = 0;
                        }
                        i++;
                    }
                }
                return null;
            }
            j2 = j3;
            obj = 1;
        } else {
            obj = null;
        }
        str = (String) map.get("Expires");
        long parseDateAsEpoch = str != null ? parseDateAsEpoch(str) : 0;
        str = (String) map.get("ETag");
        j2 = obj != null ? (j2 * 1000) + currentTimeMillis : (j <= 0 || parseDateAsEpoch < j) ? 0 : (parseDateAsEpoch - j) + currentTimeMillis;
        Entry entry = new Entry();
        entry.data = networkResponse.data;
        entry.etag = str;
        entry.softTtl = j2;
        entry.ttl = entry.softTtl;
        entry.serverDate = j;
        entry.responseHeaders = map;
        return entry;
    }

    public static String parseCharset(Map<String, String> map) {
        String str = (String) map.get("Content-Type");
        if (str != null) {
            String[] split = str.split(";");
            for (int i = 1; i < split.length; i++) {
                String[] split2 = split[i].trim().split("=");
                if (split2.length == 2 && split2[0].equals("charset")) {
                    return split2[1];
                }
            }
        }
        return "ISO-8859-1";
    }

    public static long parseDateAsEpoch(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0;
        }
    }
}
