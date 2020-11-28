package com.nxtlife.efkon.license.enums;

public enum LicenseTypeEnum {
	COMMERCIAL("CL", 240), DEMO("DM", 2);

	private String code;
	private Integer defaultLimit;

	private LicenseTypeEnum(String code, Integer defaultLimit) {
		this.code = code;
		this.defaultLimit = defaultLimit;
	}

	public String getCode() {
		return code;
	}

	public Integer getDefaultLimit() {
		return defaultLimit;
	}

	public static boolean matches(String type) {
		for (LicenseTypeEnum licenseType : LicenseTypeEnum.values()) {
			if (licenseType.name().equals(type)) {
				return true;
			}
		}
		return false;
	}

}
