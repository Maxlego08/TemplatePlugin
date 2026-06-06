package fr.maxlego08.template.zcore.utils.storage;

import java.io.File;
import java.lang.reflect.Type;

import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.enums.Folder;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.ZUtils;

public class Persist extends ZUtils {

	private ZPlugin p;

	public Persist(ZPlugin p) {
		this.p = p;
	}

	// ------------------------------------------------------------ //
	// GET NAME - What should we call this type of object?
	// ------------------------------------------------------------ //

	public static String getName(Class<?> clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	public static String getName(Object o) {
		return getName(o.getClass());
	}

	public static String getName(Type type) {
		return getName(type.getClass());
	}

	// ------------------------------------------------------------ //
	// GET FILE - In which file would we like to store this object?
	// ------------------------------------------------------------ //

	public File getFile(String name) {
		return new File(p.getDataFolder(), name + ".json");
	}

	public File getFile(Class<?> clazz) {
		return getFile(getName(clazz));
	}

	public File getFile(Object obj) {
		return getFile(getName(obj));
	}

	public File getFile(Type type) {
		return getFile(getName(type));
	}

	// NICE WRAPPERS

	public <T> T loadOrSaveDefault(T def, Class<T> clazz) {
		return loadOrSaveDefault(def, clazz, getFile(clazz));
	}

	public <T> T loadOrSaveDefault(T def, Class<T> clazz, String name) {
		return loadOrSaveDefault(def, clazz, getFile(name));
	}

	public <T> T loadOrSaveDefault(T def, Class<T> clazz, Folder folder, String name) {
		return loadOrSaveDefault(def, clazz, getFile(folder.toFolder() + File.separator + name));
	}

	public <T> T loadOrSaveDefault(T def, Class<T> clazz, File file) {
		if (!file.exists()) {
			p.getLog().log("Creating default: " + file, LogType.SUCCESS);
			this.save(def, file);
			return def;
		}

		T loaded = this.load(clazz, file);

		if (loaded == null) {
			p.getLog().log("Using default as I failed to load: " + file, LogType.WARNING);

			/*
			 * Create new config backup
			 */

			File backup = new File(file.getPath() + "_bad");
			if (backup.exists())
				backup.delete();
			p.getLog().log("Backing up copy of bad file to: " + backup, LogType.WARNING);

			file.renameTo(backup);

			return def;
		} else {

			p.getLog().log(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);

		}

		return loaded;
	}

	// SAVE

	public boolean save(Object instance) {
		return save(instance, getFile(instance));
	}

	public boolean save(Object instance, String name) {
		return save(instance, getFile(name));
	}

	public boolean save(Object instance, Folder folder, String name) {
		return save(instance, getFile(folder.toFolder() + File.separator + name));
	}

	public boolean save(Object instance, File file) {

		try {

			boolean b = DiscUtils.writeCatch(file, p.getGson().toJson(instance));
			p.getLog().log(file.getAbsolutePath() + " successfully saved !", LogType.SUCCESS);
			return b;

		} catch (Exception e) {

			p.getLog().log("cannot save file " + file.getAbsolutePath(), LogType.ERROR);
			e.printStackTrace();

			return false;
		}
	}

	// LOAD BY CLASS

	public <T> T load(Class<T> clazz) {
		return load(clazz, getFile(clazz));
	}

	public <T> T load(Class<T> clazz, String name) {
		return load(clazz, getFile(name));
	}

	public <T> T load(Class<T> clazz, File file) {
		String content = DiscUtils.readCatch(file);
		if (content == null) {
			return null;
		}

		try {
			T instance = p.getGson().fromJson(content, clazz);
			return instance;
		} catch (Exception ex) { // output the error message rather than full
									// stack trace; error parsing the file, most
									// likely
			p.getLog().log(ex.getMessage(), LogType.ERROR);
		}

		return null;
	}

	// LOAD BY TYPE
	@SuppressWarnings("unchecked")
	public <T> T load(Type typeOfT, String name) {
		return (T) load(typeOfT, getFile(name));
	}

	@SuppressWarnings("unchecked")
	public <T> T load(Type typeOfT, File file) {
		String content = DiscUtils.readCatch(file);
		if (content == null) {
			return null;
		}

		try {
			return (T) p.getGson().fromJson(content, typeOfT);
		} catch (Exception ex) { // output the error message rather than full
									// stack trace; error parsing the file, most
									// likely
			p.getLog().log(ex.getMessage(), LogType.ERROR);
		}

		return null;
	}

}
