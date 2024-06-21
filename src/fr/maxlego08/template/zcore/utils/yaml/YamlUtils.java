package fr.maxlego08.template.zcore.utils.yaml;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.ZUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Abstract utility class for handling YAML configuration files.
 * Extends {@link ZUtils}.
 */
public abstract class YamlUtils extends ZUtils {

	protected transient final JavaPlugin plugin;

	/**
	 * Constructs a YamlUtils object with the specified plugin.
	 *
	 * @param plugin the JavaPlugin instance.
	 */
	public YamlUtils(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	/**
	 * Gets the default configuration file of the plugin.
	 *
	 * @return the default FileConfiguration.
	 */
	protected FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	/**
	 * Loads a YAML configuration file.
	 *
	 * @param file the file to load.
	 * @return the YamlConfiguration of the file, or null if the file is null.
	 */
	protected YamlConfiguration getConfig(File file) {
		if (file == null) {
			return null;
		}
		return YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Loads a YAML configuration file from a specified path.
	 *
	 * @param path the path to the configuration file.
	 * @return the YamlConfiguration of the file, or null if the file does not exist.
	 */
	protected YamlConfiguration getConfig(String path) {
		File file = new File(plugin.getDataFolder() + "/" + path);
		if (!file.exists()) {
			return null;
		}
		return getConfig(file);
	}

	/**
	 * Sends an informational message to the console.
	 *
	 * @param message the message to send.
	 */
	protected void info(String message) {
		Logger.info(message);
	}

	/**
	 * Sends a success message to the console.
	 *
	 * @param message the message to send.
	 */
	protected void success(String message) {
		Logger.info(message, LogType.SUCCESS);
	}

	/**
	 * Sends an error message to the console.
	 *
	 * @param message the message to send.
	 */
	protected void error(String message) {
		Logger.info(message, LogType.ERROR);
	}

	/**
	 * Sends a warning message to the console.
	 *
	 * @param message the message to send.
	 */
	protected void warn(String message) {
		Logger.info(message, LogType.WARNING);
	}
}
