package fr.maxlego08.template.zcore.utils.loader;

import org.bukkit.configuration.file.YamlConfiguration;

public interface Loader<T> {

	T load(YamlConfiguration configuration, String path);
	
	void save(T object, YamlConfiguration configuration, String path);
	
}
