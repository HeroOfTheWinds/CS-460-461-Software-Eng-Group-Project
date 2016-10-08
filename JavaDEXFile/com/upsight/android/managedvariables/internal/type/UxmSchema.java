package com.upsight.android.managedvariables.internal.type;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.upsight.android.Upsight;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class UxmSchema {
    private static final String ITEM_SCHEMA_KEY_DEFAULT = "default";
    private static final String ITEM_SCHEMA_KEY_TAG = "tag";
    private static final String ITEM_SCHEMA_KEY_TYPE = "type";
    private static final Map<Class<? extends ManagedVariable>, Class<? extends BaseSchema>> sClassSchemaMap;
    private static final Map<String, Class<? extends BaseSchema>> sModelTypeSchemaMap;
    private static final Map<String, String> sTypeSchemaMap;
    private List<BaseSchema> mItemList;
    private Map<String, BaseSchema> mItemSchemaMap;
    private UpsightLogger mLogger;
    public final String mSchemaJsonString;

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.1 */
    static final class C09431 extends HashMap<Class<? extends ManagedVariable>, Class<? extends BaseSchema>> {
        C09431() {
            put(UpsightManagedString.class, StringSchema.class);
            put(UpsightManagedBoolean.class, BooleanSchema.class);
            put(UpsightManagedInt.class, IntSchema.class);
            put(UpsightManagedFloat.class, FloatSchema.class);
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.2 */
    static final class C09442 extends HashMap<String, String> {
        C09442() {
            put("string", "com.upsight.uxm.string");
            put("boolean", "com.upsight.uxm.boolean");
            put("integer", "com.upsight.uxm.integer");
            put("float", "com.upsight.uxm.float");
        }
    }

    /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.3 */
    static final class C09453 extends HashMap<String, Class<? extends BaseSchema>> {
        C09453() {
            put("com.upsight.uxm.string", StringSchema.class);
            put("com.upsight.uxm.boolean", BooleanSchema.class);
            put("com.upsight.uxm.integer", IntSchema.class);
            put("com.upsight.uxm.float", FloatSchema.class);
        }
    }

    public static abstract class BaseSchema<T> {
        private static final Set<String> BASE_KEYS;
        @SerializedName("default")
        @Expose
        public T defaultValue;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("tag")
        @Expose
        public String tag;
        @SerializedName("type")
        @Expose
        public String type;

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.BaseSchema.1 */
        static final class C09461 extends HashSet<String> {
            C09461() {
                add(UxmSchema.ITEM_SCHEMA_KEY_TAG);
                add(UxmSchema.ITEM_SCHEMA_KEY_TYPE);
                add("description");
                add(UxmSchema.ITEM_SCHEMA_KEY_DEFAULT);
            }
        }

        static {
            BASE_KEYS = new C09461();
        }

        private void validate(JsonElement jsonElement) throws IllegalArgumentException {
            if (jsonElement == null) {
                throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to null JSON element");
            } else if (jsonElement.isJsonObject()) {
                for (Entry key : jsonElement.getAsJsonObject().entrySet()) {
                    String str = (String) key.getKey();
                    if (!BASE_KEYS.contains(str) && !getTypeSpecificKeys().contains(str)) {
                        throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to unknown key");
                    }
                }
                if (!isDefaultValueValid(jsonElement.getAsJsonObject().get(UxmSchema.ITEM_SCHEMA_KEY_DEFAULT))) {
                    throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to invalid default value");
                }
            } else {
                throw new IllegalArgumentException(getClass().getSimpleName() + " validation failed due to invalid JSON element type");
            }
        }

        abstract Set<String> getTypeSpecificKeys();

        abstract boolean isDefaultValueValid(JsonElement jsonElement);
    }

    public static class BooleanSchema extends BaseSchema<Boolean> {
        private static final Set<String> TYPE_SPECIFIC_KEYS;

        static {
            TYPE_SPECIFIC_KEYS = new HashSet();
        }

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isBoolean();
        }
    }

    public static class FloatSchema extends BaseSchema<Float> {
        private static final Set<String> TYPE_SPECIFIC_KEYS;
        @SerializedName("max")
        @Expose
        public Float max;
        @SerializedName("min")
        @Expose
        public Float min;

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.FloatSchema.1 */
        static final class C09471 extends HashSet<String> {
            C09471() {
                add("min");
                add("max");
            }
        }

        static {
            TYPE_SPECIFIC_KEYS = new C09471();
        }

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber();
        }
    }

    public static class IntSchema extends BaseSchema<Integer> {
        private static final Set<String> TYPE_SPECIFIC_KEYS;
        @SerializedName("max")
        @Expose
        public Integer max;
        @SerializedName("min")
        @Expose
        public Integer min;

        /* renamed from: com.upsight.android.managedvariables.internal.type.UxmSchema.IntSchema.1 */
        static final class C09481 extends HashSet<String> {
            C09481() {
                add("min");
                add("max");
            }
        }

        static {
            TYPE_SPECIFIC_KEYS = new C09481();
        }

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber();
        }
    }

    public static class StringSchema extends BaseSchema<String> {
        private static final Set<String> TYPE_SPECIFIC_KEYS;

        static {
            TYPE_SPECIFIC_KEYS = new HashSet();
        }

        Set<String> getTypeSpecificKeys() {
            return TYPE_SPECIFIC_KEYS;
        }

        boolean isDefaultValueValid(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString();
        }
    }

    static {
        sClassSchemaMap = new C09431();
        sTypeSchemaMap = new C09442();
        sModelTypeSchemaMap = new C09453();
    }

    UxmSchema(UpsightLogger upsightLogger) {
        this.mItemList = new ArrayList();
        this.mItemSchemaMap = new HashMap();
        this.mLogger = upsightLogger;
        this.mSchemaJsonString = null;
    }

    private UxmSchema(List<BaseSchema> list, Map<String, BaseSchema> map, UpsightLogger upsightLogger, String str) {
        this.mItemList = new ArrayList();
        this.mItemSchemaMap = new HashMap();
        this.mItemList = list;
        this.mItemSchemaMap = map;
        this.mLogger = upsightLogger;
        this.mSchemaJsonString = str;
    }

    public static UxmSchema create(String str, Gson gson, JsonParser jsonParser, UpsightLogger upsightLogger) throws IllegalArgumentException {
        String str2;
        List arrayList = new ArrayList();
        Map hashMap = new HashMap();
        try {
            JsonElement parse = jsonParser.parse(str);
            String str3;
            if (parse == null || !parse.isJsonArray()) {
                str3 = "UXM schema must be a JSON array: " + str;
                upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                throw new IllegalArgumentException(str3);
            }
            JsonArray asJsonArray = parse.getAsJsonArray();
            Iterator it = asJsonArray.iterator();
            while (it.hasNext()) {
                JsonElement jsonElement = (JsonElement) it.next();
                if (jsonElement.isJsonObject()) {
                    JsonElement jsonElement2 = jsonElement.getAsJsonObject().get(ITEM_SCHEMA_KEY_TAG);
                    if (jsonElement2 != null && jsonElement2.isJsonPrimitive() && jsonElement2.getAsJsonPrimitive().isString()) {
                        parse = jsonElement.getAsJsonObject().get(ITEM_SCHEMA_KEY_TYPE);
                        if (parse == null || !parse.isJsonPrimitive() || !parse.getAsJsonPrimitive().isString()) {
                            str3 = "Managed variable schema must contain a type: " + jsonElement;
                            upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                            throw new IllegalArgumentException(str3);
                        } else if (jsonElement.getAsJsonObject().has(ITEM_SCHEMA_KEY_DEFAULT)) {
                            str3 = (String) sTypeSchemaMap.get(parse.getAsString());
                            if (TextUtils.isEmpty(str3)) {
                                str3 = "Managed variable contains invalid types: " + jsonElement;
                                upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                                throw new IllegalArgumentException(str3);
                            }
                            jsonElement.getAsJsonObject().addProperty(ITEM_SCHEMA_KEY_TYPE, str3);
                            String asString = jsonElement2.getAsString();
                            Class cls = (Class) sModelTypeSchemaMap.get(str3);
                            if (cls != null) {
                                try {
                                    BaseSchema baseSchema = (BaseSchema) gson.fromJson(jsonElement, cls);
                                    baseSchema.validate(jsonElement);
                                    arrayList.add(baseSchema);
                                    hashMap.put(asString, baseSchema);
                                } catch (Throwable e) {
                                    str2 = "Managed variable contains invalid fields: " + jsonElement;
                                    upsightLogger.m208e(Upsight.LOG_TAG, e, str2, new Object[0]);
                                    throw new IllegalArgumentException(str2, e);
                                }
                            }
                            str3 = "Unknown managed variable type: " + str3;
                            upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                            throw new IllegalArgumentException(str3);
                        } else {
                            str3 = "Managed variable schema must contain a default value: " + jsonElement;
                            upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                            throw new IllegalArgumentException(str3);
                        }
                    }
                    str3 = "Managed variable schema must contain a tag: " + jsonElement;
                    upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                    throw new IllegalArgumentException(str3);
                }
                str3 = "Managed variable schema must be a JSON object: " + jsonElement;
                upsightLogger.m207e(Upsight.LOG_TAG, str3, new Object[0]);
                throw new IllegalArgumentException(str3);
            }
            return new UxmSchema(arrayList, hashMap, upsightLogger, asJsonArray.toString());
        } catch (Throwable e2) {
            str2 = "Failed to parse UXM schema JSON: " + str;
            upsightLogger.m208e(Upsight.LOG_TAG, e2, str2, new Object[0]);
            throw new IllegalArgumentException(str2, e2);
        }
    }

    public <T extends ManagedVariable> BaseSchema get(Class<T> cls, String str) {
        BaseSchema baseSchema = (BaseSchema) this.mItemSchemaMap.get(str);
        if (baseSchema == null) {
            return null;
        }
        Class cls2 = (Class) sClassSchemaMap.get(cls);
        Class cls3 = (Class) sModelTypeSchemaMap.get(baseSchema.type);
        if (cls2 != null && cls3 != null && cls3.equals(cls2)) {
            return baseSchema;
        }
        String str2 = "The tag is not of the expected class: " + str;
        this.mLogger.m207e(Upsight.LOG_TAG, str2, new Object[0]);
        throw new IllegalArgumentException(str2);
    }

    public List<BaseSchema> getAllOrdered() {
        return new ArrayList(this.mItemList);
    }
}
