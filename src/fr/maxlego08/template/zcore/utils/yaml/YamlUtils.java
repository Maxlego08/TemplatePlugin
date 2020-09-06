package fr.maxlego08.template.zcore.utils.yaml;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.ZUtils;

public abstract class YamlUtils extends ZUtils {

	protected final JavaPlugin plugin;

	/**
	 * @param plugin
	 */
	public YamlUtils(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	/**
	 * 
	 * @return file confirguration
	 */
	protected FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	/**
	 * Get config
	 * 
	 * @param path
	 * @return {@link YamlConfiguration}
	 */
	protected YamlConfiguration getConfig(File file) {
		if (file == null)
			return null;
		return YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Get config
	 * 
	 * @param path
	 * @return {@link YamlConfiguration}
	 * @throws InventoryFileNotFoundException
	 */
	protected YamlConfiguration getConfig(String path) {
		File file = new File(plugin.getDataFolder() + "/" + path);
		if (!file.exists())
			return null;
		return getConfig(file);
	}

	/**
	 * Send info to console
	 * 
	 * @param message
	 */
	protected void info(String message) {
		Logger.info(message);
	}

	/**
	 * Send success to console
	 * 
	 * @param message
	 */
	protected void success(String message) {
		Logger.info(message, LogType.SUCCESS);
	}

	/**
	 * Send error to console
	 * 
	 * @param message
	 */
	protected void error(String message) {
		Logger.info(message, LogType.ERROR);
	}

	/**
	 * Send warn to console
	 * 
	 * @param message
	 */
	protected void warn(String message) {
		Logger.info(message, LogType.WARNING);
	}

}
