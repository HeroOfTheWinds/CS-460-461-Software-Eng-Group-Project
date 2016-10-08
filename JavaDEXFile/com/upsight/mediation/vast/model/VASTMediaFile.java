package com.upsight.mediation.vast.model;

import java.math.BigInteger;

public class VASTMediaFile {
    private String apiFramework;
    private BigInteger bitrate;
    private String delivery;
    private BigInteger height;
    private String id;
    private Boolean maintainAspectRatio;
    private Boolean scalable;
    private String type;
    private String value;
    private BigInteger width;

    public String getApiFramework() {
        return this.apiFramework;
    }

    public BigInteger getBitrate() {
        return this.bitrate;
    }

    public String getDelivery() {
        return this.delivery;
    }

    public BigInteger getHeight() {
        return this.height;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public BigInteger getWidth() {
        return this.width;
    }

    public Boolean isMaintainAspectRatio() {
        return this.maintainAspectRatio;
    }

    public Boolean isScalable() {
        return this.scalable;
    }

    public void setApiFramework(String str) {
        this.apiFramework = str;
    }

    public void setBitrate(BigInteger bigInteger) {
        this.bitrate = bigInteger;
    }

    public void setDelivery(String str) {
        this.delivery = str;
    }

    public void setHeight(BigInteger bigInteger) {
        this.height = bigInteger;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setMaintainAspectRatio(Boolean bool) {
        this.maintainAspectRatio = bool;
    }

    public void setScalable(Boolean bool) {
        this.scalable = bool;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public void setWidth(BigInteger bigInteger) {
        this.width = bigInteger;
    }

    public String toString() {
        return "MediaFile [value=" + this.value + ", id=" + this.id + ", delivery=" + this.delivery + ", type=" + this.type + ", bitrate=" + this.bitrate + ", width=" + this.width + ", height=" + this.height + ", scalable=" + this.scalable + ", maintainAspectRatio=" + this.maintainAspectRatio + ", apiFramework=" + this.apiFramework + "]";
    }
}
