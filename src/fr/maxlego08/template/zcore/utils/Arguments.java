package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public abstract class Arguments extends ZUtils {

	protected String[] args;
	protected int parentCount = 0;

	protected String argAsString(int index) {
		try {
			return args[index + parentCount];
		} catch (Exception e) {
			return null;
		}
	}

	protected String argAsString(int index, String defaultValue) {
		try {
			return args[index + parentCount];
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected int argAsInteger(int index) {
		return Integer.valueOf(argAsString(index));
	}

	protected int argAsInteger(int index, int defaultValue) {
		try {
			return Integer.valueOf(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected long argAsLong(int index) {
		return Long.valueOf(argAsString(index));
	}

	protected long argAsLong(int index, long defaultValue) {
		try {
			return Long.valueOf(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected double argAsDouble(int index, double defaultValue) {
		try {
			return Double.valueOf(argAsString(index).replace(",", "."));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected double argAsDouble(int index) {
		return Double.valueOf(argAsString(index).replace(",", "."));
	}

	protected Player argAsPlayer(int index) {
		return Bukkit.getPlayer(argAsString(index));
	}

	protected Player argAsPlayer(int index, Player defaultValue) {
		try {
			return Bukkit.getPlayer(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected Location argAsLocation(int index) {
		return changeStringLocationToLocation(argAsString(index));
	}

	protected Location argAsLocation(int index, Location defaultValue) {
		try {
			return changeStringLocationToLocation(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected EntityType argAsEntityType(int index) {
		return EntityType.valueOf(argAsString(index).toUpperCase());
	}

	protected EntityType argAsEntityType(int index, EntityType defaultValue) {
		try {
			return EntityType.valueOf(argAsString(index).toUpperCase());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	protected MaterialData argAsMaterialData(int index) {
		String str = argAsString(index);
		if (str == null)
			return null;
		MaterialData data;
		try {
			if (str.contains(":")) {
				String[] split = str.split(":");
				data = new MaterialData(getMaterial(Integer.valueOf(split[0])), Byte.valueOf(split[1]));
			} else
				data = new MaterialData(getMaterial(Integer.valueOf(str)));
			return data;
		} catch (Exception e) {
			return null;
		}
	}

}
