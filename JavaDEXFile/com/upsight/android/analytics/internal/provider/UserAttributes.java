package com.upsight.android.analytics.internal.provider;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.provider.UpsightUserAttributes.Entry;
import com.upsight.android.internal.util.PreferencesHelper;
import com.upsight.android.logger.UpsightLogger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class UserAttributes extends UpsightUserAttributes {
    private static final Pattern DATETIME_DEFAULT_VALUE_PATTERN;
    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String TIMEZONE_UTC = "+0000";
    private static final Pattern USER_ATTRIBUTE_PATTERN;
    private static final Pattern USER_ATTRIBUTE_PATTERN_INFER;
    private UpsightLogger mLogger;
    private UpsightContext mUpsight;
    private Map<String, Entry> mUserAttributes;
    private Set<Entry> mUserAttributesSet;

    static {
        USER_ATTRIBUTE_PATTERN = Pattern.compile("com\\.upsight\\.user_attribute\\.(string|boolean|integer|float|datetime)\\.([a-zA-Z0-9_]+)");
        USER_ATTRIBUTE_PATTERN_INFER = Pattern.compile("com\\.upsight\\.user_attribute\\.([a-zA-Z0-9_]+)");
        DATETIME_DEFAULT_VALUE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}");
    }

    @Inject
    UserAttributes(UpsightContext upsightContext) {
        this.mUserAttributes = new HashMap();
        this.mUserAttributesSet = new HashSet();
        this.mUpsight = upsightContext;
        this.mLogger = upsightContext.getLogger();
        loadDefaultAttributes();
    }

    private void loadDefaultAttributes() {
        try {
            Bundle bundle = this.mUpsight.getPackageManager().getApplicationInfo(this.mUpsight.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS).metaData;
            if (bundle != null) {
                for (String str : bundle.keySet()) {
                    Entry createEntry = createEntry(str, bundle.get(str));
                    if (createEntry != null) {
                        this.mUserAttributes.put(createEntry.getKey(), createEntry);
                        this.mUserAttributesSet.add(createEntry);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            this.mLogger.m207e(Upsight.LOG_TAG, "Unexpected error: Package name missing!?", e);
        }
    }

    Entry createEntry(String str, Object obj) throws IllegalArgumentException {
        Matcher matcher = USER_ATTRIBUTE_PATTERN.matcher(str);
        if (matcher.matches()) {
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            if ("string".equals(group)) {
                obj = String.valueOf(obj);
            } else if (!("boolean".equals(group) || "integer".equals(group) || "float".equals(group))) {
                if ("datetime".equals(group)) {
                    String str2 = (String) obj;
                    if (DATETIME_DEFAULT_VALUE_PATTERN.matcher(str2).matches()) {
                        try {
                            obj = new SimpleDateFormat(DATETIME_FORMAT, Locale.US).parse(str2 + TIMEZONE_UTC);
                        } catch (Throwable e) {
                            group2 = String.format("Failed to parse default value of %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str});
                            this.mLogger.m207e(Upsight.LOG_TAG, group2, e);
                            throw new IllegalArgumentException(group2, e);
                        }
                    }
                    String format = String.format("Invalid format for the default value of %sdatetime.%s in the Android Manifest. It must match %s (e.g. 1970-01-01T00:00:00)", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str, DATETIME_FORMAT});
                    this.mLogger.m207e(Upsight.LOG_TAG, format, new Object[0]);
                    throw new IllegalArgumentException(format);
                }
                obj = null;
            }
            return new Entry(group2, obj);
        }
        matcher = USER_ATTRIBUTE_PATTERN_INFER.matcher(str);
        return matcher.matches() ? new Entry(matcher.group(1), obj) : null;
    }

    public Boolean getBoolean(String str) {
        if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            return Boolean.valueOf(PreferencesHelper.getBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, ((Boolean) ((Entry) this.mUserAttributes.get(str)).getDefaultValue()).booleanValue()));
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type boolean", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type boolean", new Object[]{str}));
        }
    }

    public Date getDatetime(String str) {
        if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Date.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            return new Date(TimeUnit.MILLISECONDS.convert(PreferencesHelper.getLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, TimeUnit.SECONDS.convert(((Date) ((Entry) this.mUserAttributes.get(str)).getDefaultValue()).getTime(), TimeUnit.MILLISECONDS)), TimeUnit.SECONDS));
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type datetime", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type datetime", new Object[]{str}));
        }
    }

    public Set<Entry> getDefault() {
        return new HashSet(this.mUserAttributesSet);
    }

    public Float getFloat(String str) {
        if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            return Float.valueOf(PreferencesHelper.getFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, ((Float) ((Entry) this.mUserAttributes.get(str)).getDefaultValue()).floatValue()));
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type float", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type float", new Object[]{str}));
        }
    }

    public Integer getInt(String str) {
        if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            return Integer.valueOf(PreferencesHelper.getInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, ((Integer) ((Entry) this.mUserAttributes.get(str)).getDefaultValue()).intValue()));
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type integer", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type integer", new Object[]{str}));
        }
    }

    public String getString(String str) {
        if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            return PreferencesHelper.getString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, (String) ((Entry) this.mUserAttributes.get(str)).getDefaultValue());
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type string", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type string", new Object[]{str}));
        }
    }

    public void put(String str, Boolean bool) {
        if (bool == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str);
        } else if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sboolean.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Boolean.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            PreferencesHelper.putBoolean(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, bool.booleanValue());
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type boolean", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type boolean", new Object[]{str}));
        }
    }

    public void put(String str, Float f) {
        if (f == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str);
        } else if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sfloat.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Float.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            PreferencesHelper.putFloat(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, f.floatValue());
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type float", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type float", new Object[]{str}));
        }
    }

    public void put(String str, Integer num) {
        if (num == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str);
        } else if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sinteger.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Integer.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            PreferencesHelper.putInt(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, num.intValue());
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type integer", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type integer", new Object[]{str}));
        }
    }

    public void put(String str, String str2) throws IllegalArgumentException {
        if (str2 == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str);
        } else if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sstring.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (String.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            PreferencesHelper.putString(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, str2);
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type string", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type string", new Object[]{str}));
        }
    }

    public void put(String str, Date date) {
        if (date == null) {
            PreferencesHelper.clear(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str);
        } else if (!this.mUserAttributes.containsKey(str)) {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}), new Object[0]);
            throw new IllegalArgumentException(String.format("No metadata found with android:name %sdatetime.%s in the Android Manifest", new Object[]{UpsightUserAttributes.USER_ATTRIBUTES_PREFIX, str}));
        } else if (Date.class.equals(((Entry) this.mUserAttributes.get(str)).getType())) {
            PreferencesHelper.putLong(this.mUpsight, UpsightUserAttributes.USER_ATTRIBUTES_PREFIX + str, TimeUnit.SECONDS.convert(date.getTime(), TimeUnit.MILLISECONDS));
        } else {
            this.mLogger.m213w(Upsight.LOG_TAG, String.format("The user attribute %s must be of type datetime", new Object[]{str}), new Object[0]);
            throw new IllegalArgumentException(String.format("The user attribute %s must be of type datetime", new Object[]{str}));
        }
    }
}
