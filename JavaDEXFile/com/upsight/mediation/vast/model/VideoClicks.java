package com.upsight.mediation.vast.model;

import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.BuildConfig;

public class VideoClicks {
    private String clickThrough;
    private List<String> clickTracking;
    private List<String> customClick;

    private String listToString(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        if (list == null) {
            return BuildConfig.FLAVOR;
        }
        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(((String) list.get(i)).toString());
        }
        return stringBuffer.toString();
    }

    public String getClickThrough() {
        return this.clickThrough;
    }

    public List<String> getClickTracking() {
        if (this.clickTracking == null) {
            this.clickTracking = new ArrayList();
        }
        return this.clickTracking;
    }

    public List<String> getCustomClick() {
        if (this.customClick == null) {
            this.customClick = new ArrayList();
        }
        return this.customClick;
    }

    public void setClickThrough(String str) {
        this.clickThrough = str;
    }

    public String toString() {
        return "VideoClicks [clickThrough=" + this.clickThrough + ", clickTracking=[" + listToString(this.clickTracking) + "], customClick=[" + listToString(this.customClick) + "] ]";
    }
}
