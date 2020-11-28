package com.nxtlife.efkon.license.enums;

public enum ProjectProductStatus {
	DRAFT("Draft"), SUBMITTED("Submitted"), REVIEWED("Reviewed"), APPROVED("Approved"), REJECTED("Rejected"),
	RENEWED("Renewed");

	private String name;

	private ProjectProductStatus(String name) {
		this.name = name;
	}

	public static boolean matches(String status) {
		for (ProjectProductStatus projectProductStatus : ProjectProductStatus.values()) {
			if (projectProductStatus.name().equals(status)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

}
