package fr.maxlego08.template.zcore.utils;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.template.placeholder.Placeholder;

public class PapiUtils extends TranslationHelper {

	private transient Placeholder placeholder;

	private void mapPlaceholders() {
		if (this.placeholder == null) {
			this.placeholder = Placeholder.getPlaceholder();
		}
	}

	/**
	 * 
	 * @param itemStack
	 * @param player
	 * @return itemstack
	 */
	protected ItemStack papi(ItemStack itemStack, Player player) {

		if (itemStack == null) {
			return itemStack;
		}

		this.mapPlaceholders();

		ItemMeta itemMeta = itemStack.getItemMeta();

		if (itemMeta.hasDisplayName()) {
			itemMeta.setDisplayName(this.placeholder.setPlaceholders(player, itemMeta.getDisplayName()));
		}

		if (itemMeta.hasLore()) {
			itemMeta.setLore(this.placeholder.setPlaceholders(player, itemMeta.getLore()));
		}

		itemStack.setItemMeta(itemMeta);
		return itemStack;

	}

	/**
	 * 
	 * @param placeHolder
	 * @param player
	 * @return string
	 */
	public String papi(String placeHolder, Player player) {
		this.mapPlaceholders();
		return this.placeholder.setPlaceholders(player, placeHolder);
	}

	/**
	 * Transforms a list into a list with placeholder API
	 * 
	 * @param placeHolder
	 * @param player
	 * @return placeholders
	 */
	public List<String> papi(List<String> placeHolder, Player player) {
		this.mapPlaceholders();
		return this.placeholder.setPlaceholders(player, placeHolder);
	}

}