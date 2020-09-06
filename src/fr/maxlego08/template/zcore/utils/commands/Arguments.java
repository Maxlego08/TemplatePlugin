package fr.maxlego08.template.zcore.utils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.maxlego08.template.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public abstract class Arguments extends ZUtils {

	protected String[] args;
	protected int parentCount = 0;

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected String argAsString(int index) {
		try {
			return args[index + parentCount];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected String argAsString(int index, String defaultValue) {
		try {
			return args[index + parentCount];
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected int argAsInteger(int index) {
		return Integer.valueOf(argAsString(index));
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected int argAsInteger(int index, int defaultValue) {
		try {
			return Integer.valueOf(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected long argAsLong(int index) {
		return Long.valueOf(argAsString(index));
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected long argAsLong(int index, long defaultValue) {
		try {
			return Long.valueOf(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected double argAsDouble(int index, double defaultValue) {
		try {
			return Double.valueOf(argAsString(index).replace(",", "."));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected double argAsDouble(int index) {
		return Double.valueOf(argAsString(index).replace(",", "."));
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected Player argAsPlayer(int index) {
		return Bukkit.getPlayer(argAsString(index));
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected Player argAsPlayer(int index, Player defaultValue) {
		try {
			return Bukkit.getPlayer(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected OfflinePlayer argAsOfflinePlayer(int index) {
		return Bukkit.getOfflinePlayer(argAsString(index));
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected OfflinePlayer argAsOfflinePlayer(int index, OfflinePlayer defaultValue) {
		try {
			return Bukkit.getOfflinePlayer(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected Location argAsLocation(int index) {
		return changeStringLocationToLocationEye(argAsString(index));
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected Location argAsLocation(int index, Location defaultValue) {
		try {
			return changeStringLocationToLocationEye(argAsString(index));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected EntityType argAsEntityType(int index) {
		return EntityType.valueOf(argAsString(index).toUpperCase());
	}

	/**
	 * 
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	protected EntityType argAsEntityType(int index, EntityType defaultValue) {
		try {
			return EntityType.valueOf(argAsString(index).toUpperCase());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected World argAsWorld(int index) {
		try {
			return Bukkit.getWorld(argAsString(index));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	protected World argAsWorld(int index, World world) {
		try {
			return Bukkit.getWorld(argAsString(index));
		} catch (Exception e) {
			return world;
		}
	}

}
