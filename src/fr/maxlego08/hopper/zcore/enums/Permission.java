package fr.maxlego08.hopper.zcore.enums;

public enum Permission {
	HOPPER_ADMIN

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
