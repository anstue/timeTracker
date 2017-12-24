package com.ti_zero.com.apptime.helper;

public enum LogTag {
    UI("UI"),
    DATA_OBJECTS("DATA_OBJECTS"),
    PERSISTENZ("PERSISTENZ");

    private final String key;
    LogTag(String key) {
        this.key=key;
    }
    public String getKey() {
        return key;
    }
}
