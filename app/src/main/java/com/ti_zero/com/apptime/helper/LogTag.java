package com.ti_zero.com.apptime.helper;

public enum LogTag {
    UI("UI"),
    DATA_OBJECTS("DATA_OBJECTS"),
    NOTIFICATION("NOTIFICATION"),
    PERSISTENZ("PERSISTENZ"), DATA_ACCESS_FACADE("DATA_ACCESS_FACADE");

    private final String key;
    LogTag(String key) {
        this.key=key;
    }
    public String getKey() {
        return key;
    }
}
