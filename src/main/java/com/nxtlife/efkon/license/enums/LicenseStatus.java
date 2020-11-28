package com.nxtlife.efkon.license.enums;

public enum LicenseStatus {
    EXPIRED, ACTIVE, RENEWED, REPLACED;

    public static boolean matches(String status) {
        for (LicenseStatus licenseStatus : LicenseStatus.values()) {
            if (licenseStatus.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
