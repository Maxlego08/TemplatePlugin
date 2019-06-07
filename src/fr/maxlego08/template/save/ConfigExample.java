package fr.maxlego08.template.save;

import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saver;

public class ConfigExample implements Saver {

	public static String version = "0.0.0.1";
	
	private static transient ConfigExample i = new ConfigExample();
	
	@Override
	public void save(Persist persist) {
		persist.save(i);
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, ConfigExample.class, getClass().getSimpleName().toLowerCase());
	}

}
