package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import fr.maxlego08.template.zcore.utils.plugins.Plugins;
import fr.maxlego08.ztranslator.api.Translator;

public abstract class TranslationHelper {

	/**
	 * Allows to translate the item name, if the zTranslator plugin is active, then the translated name will be retrieved
	 * 
	 * @param offlinePlayer
	 * @param itemStack
	 * @return item name
	 */
	protected String getItemName(OfflinePlayer offlinePlayer, ItemStack itemStack) {

		if (itemStack == null) {
			return "";

		}
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return itemStack.getItemMeta().getDisplayName();
		}

		if (Bukkit.getPluginManager().isPluginEnabled(Plugins.ZTRANSLATOR.getName())) {

			RegisteredServiceProvider<Translator> provider = Bukkit.getServer().getServicesManager()
					.getRegistration(Translator.class);
			Translator translator = provider.getProvider();
			return translator.translate(offlinePlayer, itemStack);
		}
		
		String name = itemStack.serialize().get("type").toString().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * Allows to translate the item name, if the zTranslator plugin is active, then the translated name will be retrieved
	 * 
	 * @param itemStack
	 * @return item name
	 */
	protected String getItemName(ItemStack itemStack) {

		if (itemStack == null) {
			return "";

		}
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return itemStack.getItemMeta().getDisplayName();
		}

		if (Bukkit.getPluginManager().isPluginEnabled(Plugins.ZTRANSLATOR.getName())) {

			RegisteredServiceProvider<Translator> provider = Bukkit.getServer().getServicesManager()
					.getRegistration(Translator.class);
			Translator translator = provider.getProvider();
			return translator.translate(itemStack);
		}
		
		String name = itemStack.serialize().get("type").toString().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
