package com.xboxbedrock;

public enum AccountType {
    NORMAL(0),
    PRO(1);

    private int i;

    public static AccountType valueOf(int i) {
        switch(i) {
            case 0:
                return NORMAL;
            case 1:
                return PRO;
            default:
                return null;
        }
    }

    private AccountType(int i) {
        this.i = i;
    }

    public int getI() {
        return this.i;
    }
}
