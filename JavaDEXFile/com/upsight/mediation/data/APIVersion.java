package com.upsight.mediation.data;

import com.upsight.mediation.util.StringUtil;
import java.util.Arrays;

public class APIVersion implements Comparable {
    public final int[] versionComponents;

    public APIVersion(String str) {
        this.versionComponents = StringUtil.toIntArray(str, "\\.");
    }

    public APIVersion(int... iArr) {
        if (iArr == null || iArr.length == 0) {
            throw new IllegalArgumentException("At least one version component must be specified");
        }
        this.versionComponents = iArr;
    }

    public int compareTo(Object obj) {
        if (equals((APIVersion) obj)) {
            return 0;
        }
        for (int i = 0; i < this.versionComponents.length; i++) {
            int versionComponent = getVersionComponent(i);
            int versionComponent2 = ((APIVersion) obj).getVersionComponent(i);
            if (versionComponent > versionComponent2) {
                return i;
            }
            if (versionComponent < versionComponent2) {
                return -i;
            }
        }
        return 0;
    }

    public boolean equals(Object obj) {
        return Arrays.equals(this.versionComponents, ((APIVersion) obj).versionComponents);
    }

    public int getVersionComponent(int i) {
        return this.versionComponents.length > i ? this.versionComponents[i] : 0;
    }

    public String toString() {
        return StringUtil.join(this.versionComponents, ".");
    }
}
