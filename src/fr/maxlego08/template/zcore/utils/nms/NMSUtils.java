package fr.maxlego08.template.zcore.utils.nms;

import org.bukkit.Bukkit;

public class NMSUtils {

	public static double version = getNMSVersion();

	/**
	 * Get minecraft serveur version
	 * 
	 * @return version
	 */
	public static double getNMSVersion() {
		if (version != 0)
			return version;
		String var1 = Bukkit.getServer().getClass().getPackage().getName();
		String[] arrayOfString = var1.replace(".", ",").split(",")[3].split("_");
		String var2 = arrayOfString[0].replace("v", "");
		String var3 = arrayOfString[1];
		return version = Double.parseDouble(var2 + "." + var3);
	}

	/**
	 * Check if minecraft version has shulker
	 * 
	 * @return boolean
	 */
	public static boolean hasShulker() {
		return !isOneHand();
	}

	/**
	 * Check if minecraft version has barrel
	 * 
	 * @return booleab
	 */
	public static boolean hasBarrel() {
		final double version = getNMSVersion();
		return !(version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.11
				|| version == 1.12 || version == 1.13);
	}

	/**
	 * check if version is granther than 1.13
	 * 
	 * @return boolean
	 */
	public static boolean isNewVersion() {
		return !isOldVersion();
	}

	/**
	 * Check if version has one hand
	 * 
	 * @return boolean
	 */
	public static boolean isOneHand() {
		return getNMSVersion() == 1.7 || getNMSVersion() == 1.8;
	}

	/**
	 * Check is version is minecraft 1.7
	 * 
	 * @return boolean
	 */
	public static boolean isVeryOldVersion() {
		return getNMSVersion() == 1.7;
	}

	/**
	 * Check if version has itemmeta unbreakable
	 * 
	 * @return boolean
	 */
	public static boolean isUnbreakable() {
		return version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10;
	}

	/**
	 * Check if version is old version of minecraft with old material system
	 * 
	 * @return boolean
	 */
	public static boolean isOldVersion() {
		return version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.12
				|| version == 1.11;
	}

	/**
	 * 
	 * Check if server vesion is new version
	 * 
	 * @return boolean
	 */
	public static boolean isNewNMSVersion() {
		switch (String.valueOf(version)) {
		case "1.17":
			return true;
		default:
			return false;
		}
	}

	/**
	 * Allows to check if the version has the colors in hex
	 * 
	 * @return boolean
	 */
	public static boolean isHexColor() {
		switch (String.valueOf(version)) {
		case "1.21":
		case "1.20":
		case "1.19":
		case "1.18":
		case "1.17":
		case "1.16":
			return true;
		default:
			return false;
		}
	}
	
}
