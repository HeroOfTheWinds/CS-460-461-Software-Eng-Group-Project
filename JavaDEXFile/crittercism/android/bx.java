package crittercism.android;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.gms.location.places.Place;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.math.BigInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class bx {
    private static at f550a;
    private static Context f551b;
    private static bf f552c;
    private static cb f553d;

    /* renamed from: crittercism.android.bx.a */
    public static final class C1080a implements bw {
        private String f530a;

        public C1080a() {
            String str = null;
            this.f530a = null;
            bx.f552c;
            bx.f551b;
            if (bx.f552c.f402b) {
                str = ((RunningTaskInfo) ((ActivityManager) bx.f551b.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.flattenToShortString().replace("/", BuildConfig.FLAVOR);
            }
            this.f530a = str;
        }

        public final String m586a() {
            return "activity";
        }

        public final /* bridge */ /* synthetic */ Object m587b() {
            return this.f530a;
        }
    }

    public static final class aa implements bw {
        private Float f531a;

        public aa() {
            this.f531a = null;
            bx.f551b;
            this.f531a = Float.valueOf(bx.f551b.getResources().getDisplayMetrics().ydpi);
        }

        public final String m588a() {
            return "ydpi";
        }

        public final /* bridge */ /* synthetic */ Object m589b() {
            return this.f531a;
        }
    }

    /* renamed from: crittercism.android.bx.b */
    public static final class C1081b implements bw {
        private Integer f532a;

        public C1081b() {
            this.f532a = null;
            bx.f550a;
            this.f532a = Integer.valueOf(bx.f550a.f316b);
        }

        public final String m590a() {
            return "app_version_code";
        }

        public final /* bridge */ /* synthetic */ Object m591b() {
            return this.f532a;
        }
    }

    /* renamed from: crittercism.android.bx.c */
    public static final class C1082c implements bw {
        private String f533a;

        public C1082c() {
            this.f533a = null;
            bx.f550a;
            this.f533a = bx.f550a.f315a;
        }

        public final String m592a() {
            return "app_version";
        }

        public final /* bridge */ /* synthetic */ Object m593b() {
            return this.f533a;
        }
    }

    /* renamed from: crittercism.android.bx.d */
    public static final class C1083d implements bw {
        public final String m594a() {
            return "arch";
        }

        public final /* synthetic */ Object m595b() {
            return System.getProperty("os.arch");
        }
    }

    /* renamed from: crittercism.android.bx.e */
    public static final class C1084e implements bw {
        private Double f534a;

        public C1084e() {
            this.f534a = null;
            bx.f551b;
            double d = 1.0d;
            Intent registerReceiver = bx.f551b.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int intExtra = registerReceiver.getIntExtra("level", -1);
            double intExtra2 = (double) registerReceiver.getIntExtra("scale", -1);
            if (intExtra >= 0 && intExtra2 > 0.0d) {
                d = ((double) intExtra) / intExtra2;
            }
            this.f534a = Double.valueOf(d);
        }

        public final String m596a() {
            return "battery_level";
        }

        public final /* bridge */ /* synthetic */ Object m597b() {
            return this.f534a;
        }
    }

    /* renamed from: crittercism.android.bx.f */
    public static final class C1085f implements bw {
        public String f535a;

        public C1085f() {
            String networkOperatorName;
            this.f535a = null;
            bx.f551b;
            try {
                networkOperatorName = ((TelephonyManager) bx.f551b.getSystemService("phone")).getNetworkOperatorName();
            } catch (Exception e) {
                networkOperatorName = Build.BRAND;
            }
            this.f535a = networkOperatorName;
            new StringBuilder("carrier == ").append(this.f535a);
            dx.m778b();
        }

        public final String m598a() {
            return "carrier";
        }

        public final /* bridge */ /* synthetic */ Object m599b() {
            return this.f535a;
        }
    }

    /* renamed from: crittercism.android.bx.g */
    static class C1086g implements bw {
        private JSONObject f536a;

        public C1086g(int i) {
            this.f536a = null;
            bx.f551b;
            bx.f552c;
            this.f536a = C1086g.m600a(i);
        }

        private static JSONObject m600a(int i) {
            Object obj = 1;
            if (!bx.f552c.f403c) {
                return null;
            }
            if (!ConnectivityManager.isNetworkTypeValid(i)) {
                return null;
            }
            NetworkInfo networkInfo = ((ConnectivityManager) bx.f551b.getSystemService("connectivity")).getNetworkInfo(i);
            JSONObject jSONObject = new JSONObject();
            if (networkInfo != null) {
                try {
                    jSONObject.put("available", networkInfo.isAvailable());
                    jSONObject.put("connected", networkInfo.isConnected());
                    if (!networkInfo.isConnected()) {
                        jSONObject.put("connecting", networkInfo.isConnectedOrConnecting());
                    }
                    jSONObject.put("failover", networkInfo.isFailover());
                    if (i != 0) {
                        obj = null;
                    }
                    if (obj == null) {
                        return jSONObject;
                    }
                    jSONObject.put("roaming", networkInfo.isRoaming());
                    return jSONObject;
                } catch (JSONException e) {
                    dx.m781c();
                    return null;
                }
            }
            jSONObject.put("available", false);
            jSONObject.put("connected", false);
            jSONObject.put("connecting", false);
            jSONObject.put("failover", false);
            if (i != 0) {
                obj = null;
            }
            if (obj == null) {
                return jSONObject;
            }
            jSONObject.put("roaming", false);
            return jSONObject;
        }

        public String m601a() {
            return null;
        }

        public final /* synthetic */ Object m602b() {
            return m603c();
        }

        public JSONObject m603c() {
            return this.f536a;
        }
    }

    /* renamed from: crittercism.android.bx.h */
    public static final class C1087h implements bw {
        private Float f537a;

        public C1087h() {
            this.f537a = null;
            bx.f551b;
            this.f537a = Float.valueOf(bx.f551b.getResources().getDisplayMetrics().density);
        }

        public final String m604a() {
            return "dpi";
        }

        public final /* bridge */ /* synthetic */ Object m605b() {
            return this.f537a;
        }
    }

    /* renamed from: crittercism.android.bx.i */
    public static final class C1088i implements bw {
        private String f538a;

        public C1088i() {
            this.f538a = null;
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                this.f538a = BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.f538a = null;
            }
        }

        public final String m606a() {
            return "disk_space_free";
        }

        public final /* bridge */ /* synthetic */ Object m607b() {
            return this.f538a;
        }
    }

    /* renamed from: crittercism.android.bx.j */
    public static final class C1089j implements bw {
        private String f539a;

        public C1089j() {
            this.f539a = null;
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                this.f539a = BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.f539a = null;
            }
        }

        public final String m608a() {
            return "disk_space_total";
        }

        public final /* bridge */ /* synthetic */ Object m609b() {
            return this.f539a;
        }
    }

    /* renamed from: crittercism.android.bx.k */
    public static final class C1090k implements bw {
        public String f540a;

        public C1090k() {
            this.f540a = null;
            bx.f551b;
            this.f540a = bx.f551b.getResources().getConfiguration().locale.getLanguage();
            if (this.f540a == null || this.f540a.length() == 0) {
                this.f540a = "en";
            }
        }

        public final String m610a() {
            return "locale";
        }

        public final /* bridge */ /* synthetic */ Object m611b() {
            return this.f540a;
        }
    }

    /* renamed from: crittercism.android.bx.l */
    public static final class C1091l implements bw {
        private JSONArray f541a;

        public C1091l() {
            this.f541a = null;
            bx.f552c;
            bx.f553d;
            if (bx.f552c.f401a) {
                this.f541a = bx.f553d.m678a();
            }
        }

        public final String m612a() {
            return "logcat";
        }

        public final /* bridge */ /* synthetic */ Object m613b() {
            return this.f541a;
        }
    }

    /* renamed from: crittercism.android.bx.m */
    public static final class C1092m implements bw {
        private Long f542a;

        public C1092m() {
            this.f542a = null;
            this.f542a = Long.valueOf(Runtime.getRuntime().maxMemory());
        }

        public final String m614a() {
            return "memory_total";
        }

        public final /* bridge */ /* synthetic */ Object m615b() {
            return this.f542a;
        }
    }

    /* renamed from: crittercism.android.bx.n */
    public static final class C1093n implements bw {
        private Integer f543a;

        public C1093n() {
            this.f543a = null;
            MemoryInfo memoryInfo = new MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            this.f543a = Integer.valueOf((memoryInfo.otherPss + (memoryInfo.dalvikPss + memoryInfo.nativePss)) * Place.TYPE_SUBLOCALITY_LEVEL_2);
        }

        public final String m616a() {
            return "memory_usage";
        }

        public final /* bridge */ /* synthetic */ Object m617b() {
            return this.f543a;
        }
    }

    /* renamed from: crittercism.android.bx.o */
    public static final class C1094o implements bw {
        public Integer f544a;

        public C1094o() {
            this.f544a = Integer.valueOf(0);
            bx.f551b;
            try {
                String networkOperator = ((TelephonyManager) bx.f551b.getSystemService("phone")).getNetworkOperator();
                if (networkOperator != null) {
                    this.f544a = Integer.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
                }
                new StringBuilder("mobileCountryCode == ").append(this.f544a);
                dx.m778b();
            } catch (Exception e) {
            }
        }

        public final String m618a() {
            return "mobile_country_code";
        }

        public final /* bridge */ /* synthetic */ Object m619b() {
            return this.f544a;
        }
    }

    /* renamed from: crittercism.android.bx.p */
    public static final class C1095p implements bw {
        public Integer f545a;

        public C1095p() {
            this.f545a = Integer.valueOf(0);
            bx.f551b;
            try {
                String networkOperator = ((TelephonyManager) bx.f551b.getSystemService("phone")).getNetworkOperator();
                if (networkOperator != null) {
                    this.f545a = Integer.valueOf(Integer.parseInt(networkOperator.substring(3)));
                }
                new StringBuilder("mobileNetworkCode == ").append(this.f545a);
                dx.m778b();
            } catch (Exception e) {
            }
        }

        public final String m620a() {
            return "mobile_network_code";
        }

        public final /* bridge */ /* synthetic */ Object m621b() {
            return this.f545a;
        }
    }

    /* renamed from: crittercism.android.bx.q */
    public static final class C1096q extends C1086g {
        public C1096q() {
            super(0);
        }

        public final String m622a() {
            return "mobile_network";
        }

        public final /* bridge */ /* synthetic */ JSONObject m623c() {
            return super.m603c();
        }
    }

    /* renamed from: crittercism.android.bx.r */
    public static final class C1097r implements bw {
        public final String m624a() {
            return Models.CONTENT_DIRECTORY;
        }

        public final /* bridge */ /* synthetic */ Object m625b() {
            return Build.MODEL;
        }
    }

    /* renamed from: crittercism.android.bx.s */
    public static final class C1098s implements bw {
        public final String m626a() {
            return Twitter.NAME;
        }

        public final /* synthetic */ Object m627b() {
            return new String();
        }
    }

    /* renamed from: crittercism.android.bx.t */
    public static final class C1099t implements bw {
        private Integer f546a;

        public C1099t() {
            this.f546a = null;
            bx.f551b;
            int i = bx.f551b.getResources().getConfiguration().orientation;
            if (i == 0) {
                Display defaultDisplay = ((WindowManager) bx.f551b.getSystemService("window")).getDefaultDisplay();
                i = defaultDisplay.getWidth() == defaultDisplay.getHeight() ? 3 : defaultDisplay.getWidth() > defaultDisplay.getHeight() ? 2 : 1;
            }
            this.f546a = Integer.valueOf(i);
        }

        public final String m628a() {
            return "orientation";
        }

        public final /* bridge */ /* synthetic */ Object m629b() {
            return this.f546a;
        }
    }

    /* renamed from: crittercism.android.bx.u */
    public static final class C1100u implements bw {
        private String f547a;

        public C1100u() {
            this.f547a = null;
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                this.f547a = BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.f547a = null;
            }
        }

        public final String m630a() {
            return "sd_space_free";
        }

        public final /* bridge */ /* synthetic */ Object m631b() {
            return this.f547a;
        }
    }

    /* renamed from: crittercism.android.bx.v */
    public static final class C1101v implements bw {
        private String f548a;

        public C1101v() {
            this.f548a = null;
            try {
                BigInteger.valueOf(-1);
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                this.f548a = BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                this.f548a = null;
            }
        }

        public final String m632a() {
            return "sd_space_total";
        }

        public final /* bridge */ /* synthetic */ Object m633b() {
            return this.f548a;
        }
    }

    /* renamed from: crittercism.android.bx.w */
    public static final class C1102w implements bw {
        public final String m634a() {
            return "system";
        }

        public final /* bridge */ /* synthetic */ Object m635b() {
            return "android";
        }
    }

    /* renamed from: crittercism.android.bx.x */
    public static final class C1103x implements bw {
        public final String m636a() {
            return "system_version";
        }

        public final /* bridge */ /* synthetic */ Object m637b() {
            return VERSION.RELEASE;
        }
    }

    /* renamed from: crittercism.android.bx.y */
    public static final class C1104y extends C1086g {
        public C1104y() {
            super(1);
        }

        public final String m638a() {
            return "wifi";
        }

        public final /* bridge */ /* synthetic */ JSONObject m639c() {
            return super.m603c();
        }
    }

    /* renamed from: crittercism.android.bx.z */
    public static final class C1105z implements bw {
        private Float f549a;

        public C1105z() {
            this.f549a = null;
            bx.f551b;
            this.f549a = Float.valueOf(bx.f551b.getResources().getDisplayMetrics().xdpi);
        }

        public final String m640a() {
            return "xdpi";
        }

        public final /* bridge */ /* synthetic */ Object m641b() {
            return this.f549a;
        }
    }

    static {
        f550a = null;
        f551b = null;
        f552c = null;
        f553d = null;
    }

    public static void m643a(Context context) {
        f551b = context;
    }

    public static void m644a(at atVar) {
        f550a = atVar;
    }

    public static void m645a(bf bfVar) {
        f552c = bfVar;
    }

    public static void m646a(cb cbVar) {
        f553d = cbVar;
    }
}
