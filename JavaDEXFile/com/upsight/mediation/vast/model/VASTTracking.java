package com.upsight.mediation.vast.model;

import com.upsight.mediation.vast.util.Assets;

public class VASTTracking {
    private boolean consumed;
    private TRACKING_EVENTS_TYPE event;
    private String offset;
    private boolean offsetRelative;
    private long parsedOffset;
    private String value;

    public TRACKING_EVENTS_TYPE getEvent() {
        return this.event;
    }

    public long getParsedOffset() {
        return this.parsedOffset;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isConsumed() {
        return this.consumed;
    }

    public boolean isOffsetRelative() {
        return this.offsetRelative;
    }

    public void setConsumed(boolean z) {
        this.consumed = z;
    }

    public void setEvent(TRACKING_EVENTS_TYPE tracking_events_type) {
        this.event = tracking_events_type;
    }

    public void setOffset(String str) {
        this.offset = str;
        if (str.endsWith("%")) {
            this.parsedOffset = Long.parseLong(str.substring(0, str.indexOf("%")));
            this.offsetRelative = true;
            return;
        }
        this.parsedOffset = Assets.parseOffset(str);
        this.offsetRelative = false;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String toString() {
        return "Tracking [event=" + this.event + ", value=" + this.value + " offset=" + this.offset + "]";
    }
}
