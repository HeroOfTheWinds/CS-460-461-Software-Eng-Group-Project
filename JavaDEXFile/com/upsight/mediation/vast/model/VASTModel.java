package com.upsight.mediation.vast.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.util.XmlTools;
import com.voxelbusters.nativeplugins.defines.Keys;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import spacemadness.com.lunarconsole.BuildConfig;

public class VASTModel implements Serializable {
    public static final long DOWNLOAD_TIMEOUT_LIMIT = 30000;
    private static String TAG = null;
    private static final String adSystemXPATH = "/VAST/Ad/InLine/AdSystem";
    private static final String adTitleXPATH = "/VAST/Ad/InLine/AdTitle";
    private static final String combinedTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking|/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String durationXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/Duration";
    private static final String errorUrlXPATH = "//Error";
    private static final String impressionXPATH = "/VAST/Ad/InLine/Impression";
    private static final String inlineLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String inlineNonLinearTrackingXPATH = "/VAST/Ad/InLine/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private static final String mediaFileXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear/MediaFiles/MediaFile";
    private static final long serialVersionUID = 4318368258447283733L;
    private static final String skipOffsetXPATH = "/VAST/Ad/InLine/Creatives/Creative/Linear[@skipoffset]";
    private static final String vastXPATH = "//VAST";
    private static final String videoClicksXPATH = "//VideoClicks";
    private static final String wrapperLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/Linear/TrackingEvents/Tracking";
    private static final String wrapperNonLinearTrackingXPATH = "/VAST/Ad/Wrapper/Creatives/Creative/NonLinearAds/TrackingEvents/Tracking";
    private String mediaFileDeliveryType;
    private String mediaFileLocation;
    private transient Document vastsDocument;

    /* renamed from: com.upsight.mediation.vast.model.VASTModel.1 */
    class C10331 implements Runnable {
        final /* synthetic */ Context val$c;
        final /* synthetic */ int val$downloadTimeout;
        final /* synthetic */ VASTPlayer val$vastPlayer;

        C10331(VASTPlayer vASTPlayer, Context context, int i) {
            this.val$vastPlayer = vASTPlayer;
            this.val$c = context;
            this.val$downloadTimeout = i;
        }

        public void run() {
            new DownloadTask(this.val$vastPlayer, this.val$c, this.val$downloadTimeout).execute(new String[]{VASTModel.this.mediaFileLocation});
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Integer> {
        private final Context context;
        private final long downloadTimeout;
        private final VASTPlayer mVastPlayer;

        public DownloadTask(VASTPlayer vASTPlayer, Context context, int i) {
            this.mVastPlayer = vASTPlayer;
            this.context = context;
            this.downloadTimeout = i > 0 ? (long) i : VASTModel.DOWNLOAD_TIMEOUT_LIMIT;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Integer doInBackground(java.lang.String... r23) {
            /*
            r22 = this;
            r3 = 0;
            r9 = 0;
            r11 = 0;
            r12 = 0;
            r14 = 0;
            r5 = 0;
            r4 = 0;
            r8 = 0;
            r15 = 0;
            r6 = 0;
            r10 = 0;
            r13 = 0;
            r7 = 0;
            r16 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r2 = new java.net.URL;	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r18 = 0;
            r18 = r23[r18];	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r0 = r18;
            r2.<init>(r0);	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r18 = 0;
            r18 = r23[r18];	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r19 = 0;
            r19 = r23[r19];	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r20 = 47;
            r19 = r19.lastIndexOf(r20);	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r19 = r19 + 1;
            r20 = 0;
            r20 = r23[r20];	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r20 = r20.length();	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r18 = r18.substring(r19, r20);	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r2 = r2.openConnection();	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r2 = (java.net.HttpURLConnection) r2;	 Catch:{ SocketTimeoutException -> 0x01f8, Exception -> 0x01ad, all -> 0x01cc }
            r7 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
            r2.setConnectTimeout(r7);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r2.connect();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r7 = r2.getContentLength();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10 = r2.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r13 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r10 == r13) goto L_0x008b;
        L_0x0052:
            r7 = com.upsight.mediation.vast.model.VASTModel.TAG;	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10.<init>();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r11 = "Server returned HTTP ";
            r10 = r10.append(r11);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r11 = r2.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10 = r10.append(r11);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r11 = " ";
            r10 = r10.append(r11);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r11 = r2.getResponseMessage();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10 = r10.append(r11);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r10 = r10.toString();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            com.upsight.mediation.log.FuseLog.m235d(r7, r10);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r3 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r3 = java.lang.Integer.valueOf(r3);
            if (r2 == 0) goto L_0x0226;
        L_0x0086:
            r2.disconnect();
            r2 = r3;
        L_0x008a:
            return r2;
        L_0x008b:
            r10 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r0 = r22;
            r13 = r0.context;	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r13 = r13.getCacheDir();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r19 = "fuse_vast_cache";
            r0 = r19;
            r10.<init>(r13, r0);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r13 = r10.exists();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            if (r13 != 0) goto L_0x00b5;
        L_0x00a2:
            r13 = r10.mkdir();	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            if (r13 != 0) goto L_0x00b5;
        L_0x00a8:
            r3 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
            r3 = java.lang.Integer.valueOf(r3);
            if (r2 == 0) goto L_0x0226;
        L_0x00b0:
            r2.disconnect();
            r2 = r3;
            goto L_0x008a;
        L_0x00b5:
            r13 = new java.io.File;	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r0 = r18;
            r13.<init>(r10, r0);	 Catch:{ SocketTimeoutException -> 0x020e, Exception -> 0x0224, all -> 0x021e }
            r3 = r13.exists();	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            if (r3 == 0) goto L_0x00e3;
        L_0x00c2:
            r8 = r13.length();	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r0 = (long) r7;	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r18 = r0;
            r3 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1));
            if (r3 != 0) goto L_0x00e3;
        L_0x00cd:
            r0 = r22;
            r3 = com.upsight.mediation.vast.model.VASTModel.this;	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r5 = r13.getAbsolutePath();	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r3.mediaFileLocation = r5;	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            if (r2 == 0) goto L_0x00dd;
        L_0x00da:
            r2.disconnect();
        L_0x00dd:
            r2 = 0;
            r2 = java.lang.Integer.valueOf(r2);
            goto L_0x008a;
        L_0x00e3:
            r8 = (long) r7;
            r0 = r22;
            r3 = r0.mVastPlayer;	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r18 = r3.getMaxFileSize();	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r3 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1));
            if (r3 <= 0) goto L_0x00fc;
        L_0x00f0:
            if (r2 == 0) goto L_0x00f5;
        L_0x00f2:
            r2.disconnect();
        L_0x00f5:
            r2 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r2 = java.lang.Integer.valueOf(r2);
            goto L_0x008a;
        L_0x00fc:
            r5 = r2.getInputStream();	 Catch:{ SocketTimeoutException -> 0x01fe, Exception -> 0x01eb, all -> 0x01e6 }
            r3 = new java.io.FileOutputStream;	 Catch:{ SocketTimeoutException -> 0x020c, Exception -> 0x0213, all -> 0x021a }
            r3.<init>(r13);	 Catch:{ SocketTimeoutException -> 0x020c, Exception -> 0x0213, all -> 0x021a }
            r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r4 = new byte[r4];	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
        L_0x0109:
            r6 = r5.read(r4);	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            r8 = -1;
            if (r6 == r8) goto L_0x0166;
        L_0x0110:
            r8 = java.lang.System.nanoTime();	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            r8 = r8 - r16;
            r10 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
            r8 = r8 / r10;
            r0 = r22;
            r10 = r0.downloadTimeout;	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r8 < 0) goto L_0x0128;
        L_0x0122:
            r8 = 1;
            r0 = r22;
            r0.cancel(r8);	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
        L_0x0128:
            r8 = r22.isCancelled();	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            if (r8 == 0) goto L_0x0148;
        L_0x012e:
            r5.close();	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            if (r3 == 0) goto L_0x0136;
        L_0x0133:
            r3.close();	 Catch:{ IOException -> 0x0204 }
        L_0x0136:
            if (r5 == 0) goto L_0x013b;
        L_0x0138:
            r5.close();	 Catch:{ IOException -> 0x0204 }
        L_0x013b:
            if (r2 == 0) goto L_0x0140;
        L_0x013d:
            r2.disconnect();
        L_0x0140:
            r2 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r2 = java.lang.Integer.valueOf(r2);
            goto L_0x008a;
        L_0x0148:
            r8 = 0;
            r3.write(r4, r8, r6);	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            goto L_0x0109;
        L_0x014d:
            r4 = move-exception;
            r4 = r2;
        L_0x014f:
            r2 = 402; // 0x192 float:5.63E-43 double:1.986E-321;
            r2 = java.lang.Integer.valueOf(r2);
            if (r3 == 0) goto L_0x015a;
        L_0x0157:
            r3.close();	 Catch:{ IOException -> 0x0210 }
        L_0x015a:
            if (r5 == 0) goto L_0x015f;
        L_0x015c:
            r5.close();	 Catch:{ IOException -> 0x0210 }
        L_0x015f:
            if (r4 == 0) goto L_0x008a;
        L_0x0161:
            r4.disconnect();
            goto L_0x008a;
        L_0x0166:
            r4 = r13.exists();	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            if (r4 == 0) goto L_0x0175;
        L_0x016c:
            r8 = r13.length();	 Catch:{ SocketTimeoutException -> 0x014d, Exception -> 0x01f4, all -> 0x0218 }
            r6 = (long) r7;
            r4 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
            if (r4 == 0) goto L_0x018c;
        L_0x0175:
            if (r3 == 0) goto L_0x017a;
        L_0x0177:
            r3.close();	 Catch:{ IOException -> 0x0207 }
        L_0x017a:
            if (r5 == 0) goto L_0x017f;
        L_0x017c:
            r5.close();	 Catch:{ IOException -> 0x0207 }
        L_0x017f:
            if (r2 == 0) goto L_0x0184;
        L_0x0181:
            r2.disconnect();
        L_0x0184:
            r2 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r2 = java.lang.Integer.valueOf(r2);
            goto L_0x008a;
        L_0x018c:
            if (r3 == 0) goto L_0x0191;
        L_0x018e:
            r3.close();	 Catch:{ IOException -> 0x020a }
        L_0x0191:
            if (r5 == 0) goto L_0x0196;
        L_0x0193:
            r5.close();	 Catch:{ IOException -> 0x020a }
        L_0x0196:
            if (r2 == 0) goto L_0x019b;
        L_0x0198:
            r2.disconnect();
        L_0x019b:
            r0 = r22;
            r2 = com.upsight.mediation.vast.model.VASTModel.this;
            r3 = r13.getAbsolutePath();
            r2.mediaFileLocation = r3;
            r2 = 0;
            r2 = java.lang.Integer.valueOf(r2);
            goto L_0x008a;
        L_0x01ad:
            r2 = move-exception;
            r2 = r7;
        L_0x01af:
            r5 = r2;
            r21 = r3;
            r3 = r4;
            r4 = r21;
        L_0x01b5:
            r2 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
            r2 = java.lang.Integer.valueOf(r2);
            if (r3 == 0) goto L_0x01c0;
        L_0x01bd:
            r3.close();	 Catch:{ IOException -> 0x0216 }
        L_0x01c0:
            if (r4 == 0) goto L_0x01c5;
        L_0x01c2:
            r4.close();	 Catch:{ IOException -> 0x0216 }
        L_0x01c5:
            if (r5 == 0) goto L_0x008a;
        L_0x01c7:
            r5.disconnect();
            goto L_0x008a;
        L_0x01cc:
            r2 = move-exception;
            r3 = r8;
            r4 = r9;
            r5 = r10;
        L_0x01d0:
            r21 = r4;
            r4 = r2;
            r2 = r5;
            r5 = r21;
        L_0x01d6:
            if (r3 == 0) goto L_0x01db;
        L_0x01d8:
            r3.close();	 Catch:{ IOException -> 0x021c }
        L_0x01db:
            if (r5 == 0) goto L_0x01e0;
        L_0x01dd:
            r5.close();	 Catch:{ IOException -> 0x021c }
        L_0x01e0:
            if (r2 == 0) goto L_0x01e5;
        L_0x01e2:
            r2.disconnect();
        L_0x01e5:
            throw r4;
        L_0x01e6:
            r3 = move-exception;
            r5 = r11;
        L_0x01e8:
            r4 = r3;
            r3 = r15;
            goto L_0x01d6;
        L_0x01eb:
            r3 = move-exception;
            r3 = r12;
        L_0x01ed:
            r5 = r2;
            r21 = r3;
            r3 = r4;
            r4 = r21;
            goto L_0x01b5;
        L_0x01f4:
            r4 = move-exception;
            r4 = r5;
            r5 = r2;
            goto L_0x01b5;
        L_0x01f8:
            r2 = move-exception;
            r2 = r13;
        L_0x01fa:
            r3 = r6;
            r4 = r2;
            goto L_0x014f;
        L_0x01fe:
            r3 = move-exception;
            r5 = r14;
        L_0x0200:
            r3 = r6;
            r4 = r2;
            goto L_0x014f;
        L_0x0204:
            r3 = move-exception;
            goto L_0x013b;
        L_0x0207:
            r3 = move-exception;
            goto L_0x017f;
        L_0x020a:
            r3 = move-exception;
            goto L_0x0196;
        L_0x020c:
            r3 = move-exception;
            goto L_0x0200;
        L_0x020e:
            r3 = move-exception;
            goto L_0x01fa;
        L_0x0210:
            r3 = move-exception;
            goto L_0x015f;
        L_0x0213:
            r3 = move-exception;
            r3 = r5;
            goto L_0x01ed;
        L_0x0216:
            r3 = move-exception;
            goto L_0x01c5;
        L_0x0218:
            r4 = move-exception;
            goto L_0x01d6;
        L_0x021a:
            r3 = move-exception;
            goto L_0x01e8;
        L_0x021c:
            r3 = move-exception;
            goto L_0x01e0;
        L_0x021e:
            r3 = move-exception;
            r4 = r9;
            r5 = r2;
            r2 = r3;
            r3 = r8;
            goto L_0x01d0;
        L_0x0224:
            r5 = move-exception;
            goto L_0x01af;
        L_0x0226:
            r2 = r3;
            goto L_0x008a;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.upsight.mediation.vast.model.VASTModel.DownloadTask.doInBackground(java.lang.String[]):java.lang.Integer");
        }

        protected void onCancelled() {
            this.mVastPlayer.listener.vastError(VASTPlayer.ERROR_VIDEO_TIMEOUT);
        }

        protected void onPostExecute(Integer num) {
            if (num.intValue() != 0) {
                this.mVastPlayer.listener.vastError(num.intValue());
                return;
            }
            FuseLog.m239v(VASTModel.TAG, "on execute complete");
            this.mVastPlayer.setLoaded(true);
        }
    }

    static {
        TAG = "VASTModel";
    }

    public VASTModel(Document document) {
        this.mediaFileLocation = null;
        this.mediaFileDeliveryType = null;
        this.vastsDocument = document;
    }

    private List<String> getListFromXPath(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(str, this.vastsDocument, XPathConstants.NODESET);
            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String elementValue = XmlTools.getElementValue(nodeList.item(i));
                    if (elementValue != null || !elementValue.equals(BuildConfig.FLAVOR)) {
                        arrayList.add(elementValue);
                    }
                }
            }
            return arrayList;
        } catch (Exception e) {
            return null;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.vastsDocument = XmlTools.stringToDocument((String) objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(XmlTools.xmlDocumentToString(this.vastsDocument));
    }

    public int cache(Context context, VASTPlayer vASTPlayer, int i) {
        ((Activity) context).runOnUiThread(new C10331(vASTPlayer, context, i));
        return 0;
    }

    public boolean evaluateAdSystem() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adSystemXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public boolean evaluateAdTitle() {
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate(adTitleXPATH, this.vastsDocument, XPathConstants.NODE);
            return true;
        } catch (XPathExpressionException e) {
            return false;
        }
    }

    public String getDuration() {
        FuseLog.m235d(TAG, "getDuration");
        try {
            NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(durationXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodeList == null) {
                return null;
            }
            String str = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                str = XmlTools.getElementValue(nodeList.item(i));
            }
            return str;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getErrorUrl() {
        return getListFromXPath(errorUrlXPATH);
    }

    public List<String> getImpressions() {
        return getListFromXPath(impressionXPATH);
    }

    public List<VASTMediaFile> getMediaFiles() {
        ArrayList arrayList = new ArrayList();
        try {
            NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(mediaFileXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    VASTMediaFile vASTMediaFile = new VASTMediaFile();
                    Node item = nodeList.item(i);
                    NamedNodeMap attributes = item.getAttributes();
                    Node namedItem = attributes.getNamedItem("apiFramework");
                    vASTMediaFile.setApiFramework(namedItem == null ? null : namedItem.getNodeValue());
                    Node namedItem2 = attributes.getNamedItem("bitrate");
                    vASTMediaFile.setBitrate(namedItem2 == null ? null : new BigInteger(namedItem2.getNodeValue()));
                    namedItem = attributes.getNamedItem("delivery");
                    vASTMediaFile.setDelivery(namedItem == null ? null : namedItem.getNodeValue());
                    namedItem2 = attributes.getNamedItem("height");
                    vASTMediaFile.setHeight(namedItem2 == null ? null : new BigInteger(namedItem2.getNodeValue()));
                    namedItem = attributes.getNamedItem(TriggerIfContentAvailable.ID);
                    vASTMediaFile.setId(namedItem == null ? null : namedItem.getNodeValue());
                    namedItem = attributes.getNamedItem("maintainAspectRatio");
                    vASTMediaFile.setMaintainAspectRatio(namedItem == null ? null : Boolean.valueOf(namedItem.getNodeValue()));
                    namedItem = attributes.getNamedItem("scalable");
                    vASTMediaFile.setScalable(namedItem == null ? null : Boolean.valueOf(namedItem.getNodeValue()));
                    namedItem = attributes.getNamedItem(Keys.TYPE);
                    vASTMediaFile.setType(namedItem == null ? null : namedItem.getNodeValue());
                    Node namedItem3 = attributes.getNamedItem("width");
                    vASTMediaFile.setWidth(namedItem3 == null ? null : new BigInteger(namedItem3.getNodeValue()));
                    vASTMediaFile.setValue(XmlTools.getElementValue(item));
                    arrayList.add(vASTMediaFile);
                }
            }
            return arrayList;
        } catch (Exception e) {
            return null;
        }
    }

    public String getPickedMediaFileDeliveryType() {
        return this.mediaFileDeliveryType;
    }

    public String getPickedMediaFileLocation() {
        return this.mediaFileLocation;
    }

    public String getSkipOffset() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(skipOffsetXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("skipoffset").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> getTrackingEvents() {
        HashMap<TRACKING_EVENTS_TYPE, List<VASTTracking>> hashMap = new HashMap();
        NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(combinedTrackingXPATH, this.vastsDocument, XPathConstants.NODESET);
        String str = BuildConfig.FLAVOR;
        if (nodeList != null) {
            int i = 0;
            while (i < nodeList.getLength()) {
                try {
                    String nodeValue;
                    Node item = nodeList.item(i);
                    NamedNodeMap attributes = item.getAttributes();
                    String nodeValue2 = attributes.getNamedItem(SendEvent.EVENT).getNodeValue();
                    if (nodeValue2.equals(NotificationCompatApi21.CATEGORY_PROGRESS)) {
                        try {
                            nodeValue = attributes.getNamedItem("offset").getNodeValue();
                        } catch (NullPointerException e) {
                            nodeValue = null;
                        }
                    } else {
                        nodeValue = str;
                    }
                    try {
                        TRACKING_EVENTS_TYPE valueOf = TRACKING_EVENTS_TYPE.valueOf(nodeValue2);
                        String elementValue = XmlTools.getElementValue(item);
                        VASTTracking vASTTracking = new VASTTracking();
                        vASTTracking.setEvent(valueOf);
                        vASTTracking.setValue(elementValue);
                        if (valueOf.equals(TRACKING_EVENTS_TYPE.progress) && nodeValue != null) {
                            vASTTracking.setOffset(nodeValue);
                        }
                        if (hashMap.containsKey(valueOf)) {
                            ((List) hashMap.get(valueOf)).add(vASTTracking);
                        } else {
                            List arrayList = new ArrayList();
                            arrayList.add(vASTTracking);
                            hashMap.put(valueOf, arrayList);
                        }
                    } catch (IllegalArgumentException e2) {
                    }
                    i++;
                    str = nodeValue;
                } catch (Exception e3) {
                    return null;
                }
            }
        }
        return hashMap;
    }

    public String getVastVersion() {
        try {
            return ((Node) XPathFactory.newInstance().newXPath().evaluate(vastXPATH, this.vastsDocument, XPathConstants.NODE)).getAttributes().getNamedItem("version").getNodeValue().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public VideoClicks getVideoClicks() {
        VideoClicks videoClicks = new VideoClicks();
        try {
            NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(videoClicksXPATH, this.vastsDocument, XPathConstants.NODESET);
            if (nodeList != null) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    NodeList childNodes = nodeList.item(i).getChildNodes();
                    for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                        Node item = childNodes.item(i2);
                        String nodeName = item.getNodeName();
                        if (nodeName.equalsIgnoreCase("ClickTracking")) {
                            videoClicks.getClickTracking().add(XmlTools.getElementValue(item));
                        } else if (nodeName.equalsIgnoreCase("ClickThrough")) {
                            videoClicks.setClickThrough(XmlTools.getElementValue(item));
                        } else if (nodeName.equalsIgnoreCase("CustomClick")) {
                            videoClicks.getCustomClick().add(XmlTools.getElementValue(item));
                        }
                    }
                }
            }
            return videoClicks;
        } catch (Exception e) {
            return null;
        }
    }

    public void setPickedMediaFileDeliveryType(String str) {
        this.mediaFileDeliveryType = str;
    }

    public void setPickedMediaFileLocation(String str) {
        this.mediaFileLocation = str;
    }
}
