package fr.maxlego08.template.zcore.utils.builder;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

    // Gestions des objets
    public static ItemStack getCreatedItem(Material Material, int Number, String Name) {
        ItemStack item = new ItemStack(Material, Number);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
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


    public static ItemStack getCreatedItemWithEnchantement(Material Material, int Number, String Name, Enchantment Enchant, int EnchantLevel, boolean Visibility) {
        ItemStack item = new ItemStack(Material, Number);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.addEnchant(Enchant, EnchantLevel, Visibility);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCreatedItemWithLore(Material Material, int Number, String Name, List<String> Lore) {
        ItemStack item = new ItemStack(Material, Number);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack getCreatedItemWithLoreAndShort(Material Material, int Number, short data, String Name, List<String> Lore) {
		ItemStack item = new ItemStack(Material, Number, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getCreatedItemWithEnchantementAndLore(Material Material, int Number, String Name, Enchantment Enchant, int EnchantLevel, boolean Visibility, List<String> Lore) {
        ItemStack item = new ItemStack(Material, Number);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.addEnchant(Enchant, EnchantLevel, Visibility);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
	
}
