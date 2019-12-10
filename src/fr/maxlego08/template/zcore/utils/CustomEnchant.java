package fr.maxlego08.template.zcore.utils;

import org.bukkit.enchantments.Enchantment;

public class CustomEnchant {

	private final Enchantment enchantment;
	private final int level;

	public CustomEnchant(Enchantment enchantment, int level) {
		super();
		this.enchantment = enchantment;
		this.level = level;
	}

	/**
	 * @return the enchantment
	 */
	public Enchantment getEnchantment() {
		return enchantment;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

}
