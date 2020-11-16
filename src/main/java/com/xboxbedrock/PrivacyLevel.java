package com.xboxbedrock;

public enum PrivacyLevel {
    PUBLIC(0),
    UNLISTED(1),
    PRIVATE(2);

    private int level;

    public static PrivacyLevel valueOf(int i) {
        switch(i) {
            case 0:
                return PUBLIC;
            case 1:
                return UNLISTED;
            case 2:
                return PRIVATE;
            default:
                return null;
        }
    }

    private PrivacyLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}