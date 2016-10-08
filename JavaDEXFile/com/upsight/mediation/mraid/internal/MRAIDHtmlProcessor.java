package com.upsight.mediation.mraid.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MRAIDHtmlProcessor {
    private static final String TAG = "MRAIDHtmlProcessor";

    public static String processRawHtml(String str) {
        int i = 1;
        int i2 = 0;
        StringBuffer stringBuffer = new StringBuffer(str);
        Matcher matcher = Pattern.compile("<script\\s+[^>]*\\bsrc\\s*=\\s*([\\\"\\'])mraid\\.js\\1[^>]*>\\s*</script>\\n*", 2).matcher(stringBuffer);
        if (matcher.find()) {
            stringBuffer.delete(matcher.start(), matcher.end());
        }
        int i3 = str.indexOf("<html") != -1 ? 1 : 0;
        int i4 = str.indexOf("<head") != -1 ? 1 : 0;
        if (str.indexOf("<body") == -1) {
            i = 0;
        }
        if (i3 == 0 || r1 != 0) {
            String property = System.getProperty("line.separator");
            if (i4 == 0) {
                stringBuffer.insert(0, "<head>" + property + "</head>" + property);
            }
            if (i3 == 0) {
                stringBuffer.insert(0, "<html>" + property);
                stringBuffer.append("</html>");
            }
            String str2 = "<style>" + property + "body { margin:0; padding:0; }" + property + "*:not(input) { -webkit-touch-callout:none; -webkit-user-select:none; -webkit-text-size-adjust:none; }" + property + "</style>";
            Matcher matcher2 = Pattern.compile("<head[^>]*>", 2).matcher(stringBuffer);
            while (matcher2.find(i2)) {
                stringBuffer.insert(matcher2.end(), property + "<meta name='viewport' content='width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no' />" + property + str2);
                i2 = matcher2.end();
            }
            return stringBuffer.toString();
        }
        MRAIDLog.m247i(TAG, "have html tag but no body tag. can't randomly insert a body tag");
        return null;
    }
}
