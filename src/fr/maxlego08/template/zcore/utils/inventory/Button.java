package fr.maxlego08.template.zcore.utils.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.template.zcore.utils.ZUtils;

public class Button extends ZUtils {

	private String name;
	private int item;
	private List<String> lore;

	public Button(String name, int item, List<String> lore) {
		this.name = name;
		this.lore = lore;
		this.item = item;
	}

	public Button(String name, int item) {
		this.name = name;
		this.item = item;
		this.lore = null;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public ItemStack getItem() {
		return new ItemStack(getMaterial(item));
	}

	public boolean hasLore() {
		return lore != null;
	}

	public ItemStack getInitButton() {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getName());
		if (hasLore()) {
			itemM.setLore(getLore());
		}
		item.setItemMeta(itemM);
		return item;
	}

	public ItemStack getInitButton(String replaceItem, String replacer) {
		return getInitButton(replaceItem, replacer, 1);
	}

	public ItemStack getInitButton(String replaceItem, String replacer, int how) {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(getName());
		if (hasLore()) {
			List<String> list = new ArrayList<>();
			for (String d : getLore())
				list.add(d.replace(replacer, replaceItem));
			itemM.setLore(list);
		}
		item.setAmount(how);
		item.setItemMeta(itemM);
		return item;
	}

}
