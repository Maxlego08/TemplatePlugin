package fr.maxlego08.template.zcore.utils.loader;

import org.bukkit.configuration.file.YamlConfiguration;

public interface Loader<T> {

	/**
	 * Load object from yml
	 * @param configuration
	 * @param path
	 * @return
	 */
	T load(YamlConfiguration configuration, String path);
	
	/**
	 * Save object to yml
	 * @param object
	 * @param configuration
	 * @param path
	 */
	void save(T object, YamlConfiguration configuration, String path);
	
}
