package com.upsight.mediation.vast.model;

public enum VAST_DOC_ELEMENTS {
    vastVersion("2.0"),
    vasts("VASTS"),
    vastAdTagURI("VASTAdTagURI"),
    vastVersionAttribute("version");
    
    private String value;

    private VAST_DOC_ELEMENTS(String str) {
        this.value = str;
    }

    public String getValue() {
        return this.value;
    }
}
