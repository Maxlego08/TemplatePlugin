package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public abstract class LocationUtils extends PapiUtils{

	/**
	 * Change a string location to Location object
	 * 
	 * @param location as String
	 * @return string as location
	 */
	protected Location changeStringLocationToLocation(String string) {
		return changeStringLocationToLocationEye(string);
	}

	/**
	 * Change a string location to Location object
	 *
	 * @param location as string
	 * @return string as locaiton
	 */
	protected Location changeStringLocationToLocationEye(String string) {
		String[] locationArray = string.split(",");
		World w = Bukkit.getServer().getWorld(locationArray[0]);
		float x = Float.parseFloat(locationArray[1]);
		float y = Float.parseFloat(locationArray[2]);
		float z = Float.parseFloat(locationArray[3]);
		if (locationArray.length == 6) {
			float yaw = Float.parseFloat(locationArray[4]);
			float pitch = Float.parseFloat(locationArray[5]);
			return new Location(w, x, y, z, yaw, pitch);
		}
		return new Location(w, x, y, z);
	}

	/**
	 * @param location
	 * @return location as string
	 */
	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * @param location
	 * @return location as String
	 */
	protected String changeLocationToStringEye(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ() + "," + location.getYaw() + "," + location.getPitch();
		return ret;
	}

	/**
	 * @param chunk
	 * @return string as Chunk
	 */
	protected Chunk changeStringChuncToChunk(String chunk) {
		String[] a = chunk.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		return w.getChunkAt(Integer.valueOf(a[1]), Integer.valueOf(a[2]));
	}

	/**
	 * @param chunk
	 * @return chunk as string
	 */
	protected String changeChunkToString(Chunk chunk) {
		return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
	}

	/**
	 * @param {@link
	 * 			Cuboid}
	 * @return cuboid as string
	 */
	protected String changeCuboidToString(Cuboid cuboid) {
		return cuboid.getWorld().getName() + "," + cuboid.getLowerX() + "," + cuboid.getLowerY() + ","
				+ cuboid.getLowerZ() + "," + ";" + cuboid.getWorld().getName() + "," + cuboid.getUpperX() + ","
				+ cuboid.getUpperY() + "," + cuboid.getUpperZ();
	}

	/**
	 * @param str
	 * @return {@link Cuboid}
	 */
	protected Cuboid changeStringToCuboid(String str) {

		String parsedCuboid[] = str.split(";");
		String parsedFirstLoc[] = parsedCuboid[0].split(",");
		String parsedSecondLoc[] = parsedCuboid[1].split(",");

		String firstWorldName = parsedFirstLoc[0];
		double firstX = Double.valueOf(parsedFirstLoc[1]);
		double firstY = Double.valueOf(parsedFirstLoc[2]);
		double firstZ = Double.valueOf(parsedFirstLoc[3]);

		String secondWorldName = parsedSecondLoc[0];
		double secondX = Double.valueOf(parsedSecondLoc[1]);
		double secondY = Double.valueOf(parsedSecondLoc[2]);
		double secondZ = Double.valueOf(parsedSecondLoc[3]);

		Location l1 = new Location(Bukkit.getWorld(firstWorldName), firstX, firstY, firstZ);

		Location l2 = new Location(Bukkit.getWorld(secondWorldName), secondX, secondY, secondZ);

		return new Cuboid(l1, l2);

	}

}
