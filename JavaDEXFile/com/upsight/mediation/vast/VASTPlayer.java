package com.upsight.mediation.vast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.location.LocationStatusCodes;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.activity.VASTActivity;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.processor.VASTProcessor;
import com.upsight.mediation.vast.util.DefaultMediaPicker;
import com.upsight.mediation.vast.util.NetworkTools;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VASTPlayer {
    public static final int ERROR_EXCEEDED_WRAPPER_LIMIT = 302;
    public static final int ERROR_FILE_NOT_FOUND = 401;
    public static final int ERROR_GENERAL_LINEAR = 400;
    public static final int ERROR_GENERAL_WRAPPER = 300;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_NO_COMPATIBLE_MEDIA_FILE = 403;
    public static final int ERROR_NO_NETWORK = 1;
    public static final int ERROR_NO_VAST_IN_WRAPPER = 303;
    public static final int ERROR_SCHEMA_VALIDATION = 101;
    public static final int ERROR_UNDEFINED = 900;
    public static final int ERROR_UNSUPPORTED_VERSION = 102;
    public static final int ERROR_VIDEO_PLAYBACK = 405;
    public static final int ERROR_VIDEO_TIMEOUT = 402;
    public static final int ERROR_WRAPPER_TIMEOUT = 301;
    public static final int ERROR_XML_PARSE = 100;
    private static final String TAG = "VASTPlayer";
    public static final String VERSION = "1.3";
    public static VASTPlayer currentPlayer;
    private String actionText;
    private Context context;
    private final int downloadTimeout;
    private final String endCardHtml;
    private boolean isRewarded;
    public VASTPlayerListener listener;
    private boolean loaded;
    private String maxVideoFileSize;
    private boolean postroll;
    private boolean shouldValidateSchema;
    private long skipOffset;
    private VASTModel vastModel;

    public interface VASTPlayerListener {
        void vastClick();

        void vastComplete();

        void vastDismiss();

        void vastDisplay();

        void vastError(int i);

        void vastProgress(int i);

        void vastReady();

        void vastReplay();

        void vastRewardedVideoComplete();

        void vastSkip();
    }

    /* renamed from: com.upsight.mediation.vast.VASTPlayer.1 */
    class C10201 implements Runnable {
        final /* synthetic */ String val$urlString;

        C10201(String str) {
            this.val$urlString = str;
        }

        public void run() {
            BufferedReader bufferedReader;
            Throwable th;
            Throwable th2;
            Throwable th3;
            BufferedReader bufferedReader2 = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new URL(this.val$urlString).openStream()));
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        stringBuffer.append(readLine).append(System.getProperty("line.separator"));
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    VASTPlayer.this.loadVastResponseViaXML(stringBuffer.toString());
                } catch (Exception e2) {
                    try {
                        VASTPlayer.this.sendError(VASTPlayer.ERROR_XML_PARSE);
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                            }
                        }
                    } catch (Throwable th22) {
                        th = th22;
                        bufferedReader2 = bufferedReader;
                        th3 = th;
                        th = th3;
                        bufferedReader = bufferedReader2;
                        th22 = th;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw th22;
                    }
                } catch (Throwable th4) {
                    th22 = th4;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw th22;
                }
            } catch (Exception e5) {
                bufferedReader = null;
                VASTPlayer.this.sendError(VASTPlayer.ERROR_XML_PARSE);
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Throwable th5) {
                th3 = th5;
                th = th3;
                bufferedReader = bufferedReader2;
                th22 = th;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th22;
            }
        }
    }

    /* renamed from: com.upsight.mediation.vast.VASTPlayer.2 */
    class C10212 implements Runnable {
        final /* synthetic */ String val$xmlData;

        C10212(String str) {
            this.val$xmlData = str;
        }

        public void run() {
            VASTProcessor vASTProcessor = new VASTProcessor(new DefaultMediaPicker(VASTPlayer.this.context), VASTPlayer.this);
            int process = vASTProcessor.process(VASTPlayer.this.context, this.val$xmlData, VASTPlayer.this.shouldValidateSchema, VASTPlayer.this.downloadTimeout);
            if (process == 0) {
                VASTPlayer.this.vastModel = vASTProcessor.getModel();
            } else {
                VASTPlayer.this.sendError(process);
            }
        }
    }

    /* renamed from: com.upsight.mediation.vast.VASTPlayer.3 */
    class C10223 implements Runnable {
        final /* synthetic */ int val$error;

        C10223(int i) {
            this.val$error = i;
        }

        public void run() {
            VASTPlayer.this.listener.vastError(this.val$error);
        }
    }

    public VASTPlayer(Context context, VASTPlayerListener vASTPlayerListener, boolean z, String str, long j, boolean z2, String str2, boolean z3, String str3, int i) {
        this.loaded = false;
        this.context = context;
        this.listener = vASTPlayerListener;
        this.postroll = z;
        this.skipOffset = j;
        this.isRewarded = z2;
        this.maxVideoFileSize = str2;
        this.shouldValidateSchema = z3;
        this.actionText = str3;
        this.endCardHtml = str;
        this.downloadTimeout = i;
    }

    private void sendError(int i) {
        if (this.listener != null) {
            ((Activity) this.context).runOnUiThread(new C10223(i));
        }
    }

    public long getMaxFileSize() {
        return (long) (Float.parseFloat(this.maxVideoFileSize) * 1000000.0f);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void loadVastResponseViaURL(String str) {
        this.vastModel = null;
        if (NetworkTools.connectedToInternet(this.context)) {
            new Thread(new C10201(str)).start();
        } else {
            sendError(ERROR_NO_NETWORK);
        }
    }

    public void loadVastResponseViaXML(String str) {
        this.vastModel = null;
        if (NetworkTools.connectedToInternet(this.context)) {
            new Thread(new C10212(str)).start();
        } else {
            sendError(ERROR_NO_NETWORK);
        }
    }

    public void play() {
        if (this.vastModel != null) {
            currentPlayer = this;
            Intent intent = new Intent(this.context, VASTActivity.class);
            intent.putExtra("com.nexage.android.vast.player.vastModel", this.vastModel);
            intent.putExtra("postroll", this.postroll);
            intent.putExtra("endCardHtml", this.endCardHtml);
            String skipOffset = this.vastModel.getSkipOffset();
            String[] split = skipOffset != null ? skipOffset.split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR) : new String[ERROR_NONE];
            if (split.length == 3) {
                try {
                    long parseInt = (long) ((Integer.parseInt(split[2]) * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) + ((3600000 * Integer.parseInt(split[ERROR_NONE])) + (60000 * Integer.parseInt(split[ERROR_NO_NETWORK]))));
                    FuseLog.m239v(TAG, "Overriding server sent skip offset with VAST offset from XML: " + parseInt);
                    this.skipOffset = parseInt;
                } catch (NumberFormatException e) {
                    FuseLog.m238i(TAG, "Could not parse skip offset from xml: " + skipOffset + ", using cb_ms instead");
                }
            }
            intent.putExtra("skipOffset", this.skipOffset);
            intent.putExtra("rewarded", this.isRewarded);
            intent.putExtra("actionText", this.actionText);
            this.context.startActivity(intent);
            return;
        }
        FuseLog.m235d(TAG, "vastModel is null; nothing to play");
    }

    public void setLoaded(boolean z) {
        this.loaded = z;
        if (z) {
            this.listener.vastReady();
        }
    }
}
