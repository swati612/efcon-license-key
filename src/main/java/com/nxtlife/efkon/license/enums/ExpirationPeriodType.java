package com.nxtlife.efkon.license.enums;

public enum ExpirationPeriodType {
    LIMITED,LIFETIME;

    public static boolean matches(String type) {
        for (ExpirationPeriodType expirationPeriodType : ExpirationPeriodType.values()) {
            if (expirationPeriodType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
