package crittercism.android;

public enum cq {
    APP_LOADS_FILES("com.crittercism.apploads", "appLoadFiles"),
    HANDLED_EXCEPTION_FILES("com.crittercism.exceptions", "handledExceptionFiles"),
    SDK_CRASHES_FILES("com.crittercism.sdkcrashes", "sdkCrashFiles"),
    NDK_CRASHES_FILES("com.crittercism.ndkcrashes", "ndkCrashFiles"),
    CURRENT_BREADCRUMBS_FILES("com.crittercism.breadcrumbs", "currentBreadcrumbFiles"),
    PREVIOUS_BREADCRUMBS_FILES("com.crittercism.breadcrumbs", "previousBreadcrumbFiles"),
    NETWORK_BREADCRUMBS_FILES("com.crittercism.breadcrumbs", "networkBreadcrumbFiles"),
    CRASHED_ON_LAST_LOAD_SETTING("com.crittercism.usersettings", "crashedOnLastLoad"),
    OPT_OUT_STATUS_SETTING("com.crittercism.usersettings", "optOutStatusSettings"),
    SESSION_ID_SETTING("com.crittercism.usersettings", "sessionIDSetting"),
    OLD_SESSION_ID_SETTING("com.crittercism.prefs", "com.crittercism.prefs.sessid"),
    OLD_OPT_OUT_STATUS_SETTING("com.crittercism.prefs", "optOutStatus");
    
    private String f672m;
    private String f673n;

    private cq(String str, String str2) {
        this.f672m = str;
        this.f673n = str2;
    }

    public final String m693a() {
        return this.f672m;
    }

    public final String m694b() {
        return this.f673n;
    }
}
