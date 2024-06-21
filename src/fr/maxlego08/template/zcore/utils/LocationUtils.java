package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Abstract utility class for handling location-related conversions and operations.
 * Extends {@link PapiUtils}.
 */
public abstract class LocationUtils extends PapiUtils {

    /**
     * Converts a string representation of a location to a Location object.
     *
     * @param string the string representation of the location.
     * @return the Location object.
     */
    protected Location changeStringLocationToLocation(String string) {
        return changeStringLocationToLocationEye(string);
    }

    /**
     * Converts a string representation of a location with yaw and pitch to a Location object.
     *
     * @param string the string representation of the location.
     * @return the Location object.
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
     * Converts a Location object to its string representation.
     *
     * @param location the Location object.
     * @return the string representation of the location.
     */
    protected String changeLocationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    /**
     * Converts a Location object with yaw and pitch to its string representation.
     *
     * @param location the Location object.
     * @return the string representation of the location.
     */
    protected String changeLocationToStringEye(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    /**
     * Converts a string representation of a chunk to a Chunk object.
     *
     * @param chunk the string representation of the chunk.
     * @return the Chunk object.
     */
    protected Chunk changeStringChuncToChunk(String chunk) {
        String[] args = chunk.split(",");
        World world = Bukkit.getServer().getWorld(args[0]);
        return world.getChunkAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }

    /**
     * Converts a Chunk object to its string representation.
     *
     * @param chunk the Chunk object.
     * @return the string representation of the chunk.
     */
    protected String changeChunkToString(Chunk chunk) {
        return chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
    }

    /**
     * Converts a Cuboid object to its string representation.
     *
     * @param cuboid the Cuboid object.
     * @return the string representation of the cuboid.
     */
    protected String changeCuboidToString(Cuboid cuboid) {
        return cuboid.getWorld().getName() + "," + cuboid.getLowerX() + "," + cuboid.getLowerY() + "," + cuboid.getLowerZ() + "," + ";" + cuboid.getWorld().getName() + "," + cuboid.getUpperX() + "," + cuboid.getUpperY() + "," + cuboid.getUpperZ();
    }

    /**
     * Converts a string representation of a cuboid to a Cuboid object.
     *
     * @param str the string representation of the cuboid.
     * @return the Cuboid object.
     */
    protected Cuboid changeStringToCuboid(String str) {
        String[] parsedCuboid = str.split(";");
        String[] parsedFirstLoc = parsedCuboid[0].split(",");
        String[] parsedSecondLoc = parsedCuboid[1].split(",");

        String firstWorldName = parsedFirstLoc[0];
        double firstX = Double.parseDouble(parsedFirstLoc[1]);
        double firstY = Double.parseDouble(parsedFirstLoc[2]);
        double firstZ = Double.parseDouble(parsedFirstLoc[3]);

        String secondWorldName = parsedSecondLoc[0];
        double secondX = Double.parseDouble(parsedSecondLoc[1]);
        double secondY = Double.parseDouble(parsedSecondLoc[2]);
        double secondZ = Double.parseDouble(parsedSecondLoc[3]);

        Location l1 = new Location(Bukkit.getWorld(firstWorldName), firstX, firstY, firstZ);
        Location l2 = new Location(Bukkit.getWorld(secondWorldName), secondX, secondY, secondZ);

        return new Cuboid(l1, l2);
    }
}
