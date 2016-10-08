package com.upsight.mediation.vast.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.upsight.mediation.vast.model.VASTMediaFile;
import com.upsight.mediation.vast.processor.VASTMediaPicker;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DefaultMediaPicker implements VASTMediaPicker {
    private static final String TAG = "DefaultMediaPicker";
    private static final int maxPixels = 5000;
    String SUPPORTED_VIDEO_TYPE_REGEX;
    private Context context;
    private int deviceArea;
    private int deviceHeight;
    private int deviceWidth;

    private class AreaComparator implements Comparator<VASTMediaFile> {
        private AreaComparator() {
        }

        public int compare(VASTMediaFile vASTMediaFile, VASTMediaFile vASTMediaFile2) {
            int intValue = vASTMediaFile.getWidth().intValue();
            int intValue2 = vASTMediaFile.getHeight().intValue();
            int intValue3 = vASTMediaFile2.getWidth().intValue();
            int intValue4 = vASTMediaFile2.getHeight().intValue();
            intValue = Math.abs((intValue * intValue2) - DefaultMediaPicker.this.deviceArea);
            intValue2 = Math.abs((intValue3 * intValue4) - DefaultMediaPicker.this.deviceArea);
            return intValue < intValue2 ? -1 : intValue > intValue2 ? 1 : 0;
        }
    }

    public DefaultMediaPicker(int i, int i2) {
        this.SUPPORTED_VIDEO_TYPE_REGEX = "video/.*(?i)(mp4|3gpp|mp2t|webm|matroska)";
        setDeviceWidthHeight(i, i2);
    }

    public DefaultMediaPicker(Context context) {
        this.SUPPORTED_VIDEO_TYPE_REGEX = "video/.*(?i)(mp4|3gpp|mp2t|webm|matroska)";
        this.context = context;
        setDeviceWidthHeight();
    }

    private VASTMediaFile getBestMatch(List<VASTMediaFile> list) {
        for (VASTMediaFile vASTMediaFile : list) {
            if (isMediaFileCompatible(vASTMediaFile)) {
                return vASTMediaFile;
            }
        }
        return null;
    }

    private boolean isMediaFileCompatible(VASTMediaFile vASTMediaFile) {
        return vASTMediaFile.getType().matches(this.SUPPORTED_VIDEO_TYPE_REGEX);
    }

    private int prefilterMediaFiles(List<VASTMediaFile> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            VASTMediaFile vASTMediaFile = (VASTMediaFile) it.next();
            if (TextUtils.isEmpty(vASTMediaFile.getType())) {
                it.remove();
            } else {
                BigInteger height = vASTMediaFile.getHeight();
                if (height == null) {
                    it.remove();
                } else {
                    int intValue = height.intValue();
                    if (intValue <= 0 || intValue >= maxPixels) {
                        it.remove();
                    } else {
                        height = vASTMediaFile.getWidth();
                        if (height == null) {
                            it.remove();
                        } else {
                            intValue = height.intValue();
                            if (intValue <= 0 || intValue >= maxPixels) {
                                it.remove();
                            } else if (TextUtils.isEmpty(vASTMediaFile.getValue())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
        return list.size();
    }

    private void setDeviceWidthHeight() {
        DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
        this.deviceWidth = displayMetrics.widthPixels;
        this.deviceHeight = displayMetrics.heightPixels;
        this.deviceArea = this.deviceWidth * this.deviceHeight;
    }

    private void setDeviceWidthHeight(int i, int i2) {
        this.deviceWidth = i;
        this.deviceHeight = i2;
        this.deviceArea = this.deviceWidth * this.deviceHeight;
    }

    public VASTMediaFile pickVideo(List<VASTMediaFile> list) {
        if (list == null || prefilterMediaFiles(list) == 0) {
            return null;
        }
        Collections.sort(list, new AreaComparator());
        return getBestMatch(list);
    }
}
