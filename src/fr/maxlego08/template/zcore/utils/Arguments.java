package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

	protected int argAsInteger(int index) {
		return Integer.valueOf(argAsString(index));
	}

	protected long argAsLong(int index) {
		return Long.valueOf(argAsString(index));
	}

	protected double argAsDouble(int index) {
		return Double.valueOf(argAsString(index).replace(",", "."));
	}

	protected Player argAsPlayer(int index) {
		return Bukkit.getPlayer(argAsString(index));
	}

	protected Location argAsLocation(int index) {
		return changeStringLocationToLocation(argAsString(index));
	}

}
