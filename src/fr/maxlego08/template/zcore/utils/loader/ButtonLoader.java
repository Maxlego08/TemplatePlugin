package fr.maxlego08.template.zcore.utils.loader;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.inventory.Button;

public class ButtonLoader extends ZUtils implements Loader<Button> {

	@Override
	public Button load(YamlConfiguration configuration, String path) {

		String name = configuration.getString(path + "name") == null ? null
				: color(configuration.getString(path + "name"));
		int item = configuration.getInt(path + "item", 7);
		int data = configuration.getInt(path + "data", 0);
		List<String> lore = configuration.getStringList(path + "lore");

		return new Button(name, item, data, lore);
	}

	@Override
	public void save(Button object, YamlConfiguration configuration, String path) {

		configuration.set(path + "name", object.getName() != null ? colorReverse(object.getName()) : null);
		configuration.set(path + "item", object.getItemInInteger());
		configuration.set(path + "data", object.getData());
		configuration.set(path + "lore", object.getLore() == null ? null : object.getLore());

	}

}
