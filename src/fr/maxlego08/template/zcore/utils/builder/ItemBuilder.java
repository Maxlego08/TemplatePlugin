package fr.maxlego08.template.zcore.utils.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import fr.maxlego08.template.zcore.utils.CustomEnchant;

public class ItemBuilder {

	// Gestions des objets
	public static ItemStack getCreatedItem(Material Material, int Number, int id) {
		return getCreatedItemAndData(Material, Number, (byte) id, "");
	}

	public static ItemStack getCreatedItem(Material Material, int Number, String... name) {
		return getCreatedItemWithLore(Material, Number, null, name);
	}

	public static ItemStack getCreatedItem(String name, Material Material, int Number, String... lore) {
		return getCreatedItemWithLore(Material, Number, name, lore);
	}

	public static ItemStack getCreatedItem(String name, Material Material, int Number, int data, String... lore) {
		return getCreatedItemWithLoreAndShort(Material, Number, (byte) data, name, Arrays.asList(lore));
	}

	public static ItemStack getCreatedItem(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		item.setAmount(Number);
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItem(Material Material, int Number, int data, String Name) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material, Number, (byte) data);
		ItemMeta meta = item.getItemMeta();
		item.setAmount(Number);
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemFlag(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemEnchantFlag(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemAndData(Material Material, int Number, byte Data, String Name) {
		ItemStack item = new ItemStack(Material, Number, Data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithEnchantement(Material Material, int Number, String Name,
			Enchantment Enchant, int EnchantLevel, boolean Visibility) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchant, EnchantLevel, Visibility);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLore(Material material, int number, String name, List<String> strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(strings);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLore(Material material, int number, String name, String... strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(strings));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemFlag(Material material, int number, String name, String... strings) {
		return getCreatedItemFlag(material, number, name, Arrays.asList(strings));
	}

	public static ItemStack getCreatedItemFlag(Material material, int number, String name, List<String> strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(strings);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLoreAndShort(Material Material, int Number, short data, String Name,
			List<String> Lore) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material, Number, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithEnchantementAndLore(Material Material, int Number, String Name,
			Enchantment Enchant, int EnchantLevel, boolean Visibility, List<String> Lore) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchant, EnchantLevel, Visibility);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack enchant(Material material, CustomEnchant... customEnchants) {
		return enchant(material, null, null, customEnchants);
	}

	public static ItemStack enchant(Material material, String name, List<String> lore,
			CustomEnchant... customEnchants) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		for (CustomEnchant customEnchant : customEnchants)
			meta.addEnchant(customEnchant.getEnchantment(), customEnchant.getLevel(), true);
		if (lore != null)
			meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLoreTicket(Material material, int number, String name,
			String... strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(Arrays.asList(strings));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createBook(Enchantment enchantment, int level) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) item.getItemMeta();
		enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
		item.setItemMeta(enchantmentStorageMeta);
		return item;
	}

	public static ItemStack clone(ItemStack item, String... lores){
		ItemStack currentItem = item.clone();
		ItemMeta itemMeta = currentItem.getItemMeta();
		List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
		lore.addAll(Arrays.asList(lores));
		itemMeta.setLore(lore);
		currentItem.setItemMeta(itemMeta);
		return currentItem;
	}
	
}
