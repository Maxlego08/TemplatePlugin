package fr.maxlego08.template.zcore.utils.builder;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionBuilder extends ItemBuilder {

	public PotionBuilder(int data, int amount, String name, List<String> lore, List<ItemFlag> flags,
			Map<Enchantment, Integer> enchantments) {
		super(Material.POTION, data, amount, name, lore, flags, enchantments);
	}

	public PotionBuilder(int amount, int data, String name) {
		super(Material.POTION, amount, data, name);
	}

	public PotionBuilder(int amount, int data) {
		super(Material.POTION, amount, data);
	}

	public PotionBuilder(int amount, String name) {
		super(Material.POTION, amount, name);
	}

	public PotionBuilder(int amount) {
		super(Material.POTION, amount);
	}

	public PotionBuilder(ItemFlag... flags) {
		super(Material.POTION, flags);
	}

	public PotionBuilder(String... lore) {
		super(Material.POTION, lore);
	}

	public PotionBuilder(String name) {
		super(Material.POTION, name);
	}

	public PotionBuilder() {
		super(Material.POTION);
	}

	/**
	 * 
	 */
	public PotionMeta getMeta() {
		return (PotionMeta) super.getMeta();
	}
	
	@SuppressWarnings("deprecation")
	/**
	 * 
	 * @param potionEffectType
	 * @return
	 */
	public PotionBuilder effect(PotionEffectType potionEffectType) {
		this.getMeta().setMainEffect(potionEffectType);
		return this;
	}

	/**
	 * 
	 * @param potionEffectType
	 * @param duration
	 * @param values
	 * @return
	 */
	public PotionBuilder effect(PotionEffectType potionEffectType, int duration, int values) {
		this.getMeta().addCustomEffect(new PotionEffect(potionEffectType, duration, values), true);
		return this;
	}

}
