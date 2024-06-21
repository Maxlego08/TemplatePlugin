package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import fr.maxlego08.template.zcore.utils.plugins.Plugins;
import fr.maxlego08.ztranslator.api.Translator;

/**
 * Abstract helper class for translating item names.
 * Provides methods for getting the translated name of an item stack, optionally using the zTranslator plugin.
 */
public abstract class TranslationHelper {

	/**
	 * Translates the name of an item stack for a given offline player.
	 * If the item stack has a display name, it returns the display name.
	 * If the zTranslator plugin is active, it retrieves the translated name from the plugin.
	 * Otherwise, it returns a formatted version of the item type name.
	 *
	 * @param offlinePlayer the offline player for context.
	 * @param itemStack     the item stack whose name is to be translated.
	 * @return the translated item name, or an empty string if the item stack is null.
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
	 * Translates the name of an item stack.
	 * If the item stack has a display name, it returns the display name.
	 * If the zTranslator plugin is active, it retrieves the translated name from the plugin.
	 * Otherwise, it returns a formatted version of the item type name.
	 *
	 * @param itemStack the item stack whose name is to be translated.
	 * @return the translated item name, or an empty string if the item stack is null.
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
