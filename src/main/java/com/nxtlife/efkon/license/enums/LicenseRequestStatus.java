package com.nxtlife.efkon.license.enums;

public enum LicenseRequestStatus {
	PENDING, ACCEPTED, REJECTED;

	public static boolean matches(String status) {
		for (ProjectProductStatus projectProductStatus : ProjectProductStatus.values()) {
			if (projectProductStatus.name().equals(status)) {
				return true;
			}
		}
		return false;
	}
}
