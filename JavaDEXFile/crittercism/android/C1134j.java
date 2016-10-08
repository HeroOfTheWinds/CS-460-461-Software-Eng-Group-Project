package crittercism.android;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/* renamed from: crittercism.android.j */
public final class C1134j {
    public static Object m819a(Field field, Object obj) {
        if (field == null || field == null) {
            return null;
        }
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cl clVar = new cl("Unable to get value of field", th);
        }
    }

    public static Field m820a(Class cls, Class cls2) {
        Field[] declaredFields = cls.getDeclaredFields();
        Field field = null;
        for (int i = 0; i < declaredFields.length; i++) {
            if (cls2.isAssignableFrom(declaredFields[i].getType())) {
                if (field != null) {
                    throw new cl("Field is ambiguous: " + field.getName() + ", " + declaredFields[i].getName());
                }
                field = declaredFields[i];
            }
        }
        if (field == null) {
            throw new cl("Could not find field matching type: " + cls2.getName());
        }
        field.setAccessible(true);
        return field;
    }

    public static void m821a(AccessibleObject[] accessibleObjectArr) {
        for (AccessibleObject accessibleObject : accessibleObjectArr) {
            if (accessibleObject != null) {
                accessibleObject.setAccessible(true);
            }
        }
    }
}
