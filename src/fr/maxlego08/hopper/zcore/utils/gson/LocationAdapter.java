package fr.maxlego08.hopper.zcore.utils.gson;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.maxlego08.hopper.zcore.ZPlugin;

public class LocationAdapter extends TypeAdapter<Location> {

	/**
	 * @param plugin
	 */
	public LocationAdapter(ZPlugin plugin) {
		super();
	}

	@Override
	public void write(JsonWriter jsonWriter, Location location) throws IOException {
		if (location == null) {
			jsonWriter.nullValue();
			return;
		}
		jsonWriter.value(this.changeLocationToString(location));
	}

	@Override
	public Location read(JsonReader jsonReader) throws IOException {
		if (jsonReader.peek() == JsonToken.NULL) {
			jsonReader.nextNull();
			return null;
		}
		return this.changeStringLocationToLocationEye(jsonReader.nextString());
	}

	/**
	 * Change a string location to Location object
	 *
	 * @param location
	 *            as string
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

}
