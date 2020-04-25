package fr.maxlego08.template.zcore.utils.plugins;

public enum Plugins {

	VAULT("Vault"),
	ESSENTIALS("Essentials"),
	
	;

	private final String name;

	private Plugins(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
