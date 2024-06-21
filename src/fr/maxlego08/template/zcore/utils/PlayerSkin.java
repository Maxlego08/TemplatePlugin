package fr.maxlego08.template.zcore.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.maxlego08.template.zcore.utils.nms.NmsVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

/**
 * Utility class for managing player skins, including fetching textures and signatures.
 * Based on https://www.spigotmc.org/threads/how-to-get-a-players-texture.244966/
 */
public class PlayerSkin {

	private static final Map<String, String> textures = new HashMap<>();
	private static final ExecutorService pool = Executors.newCachedThreadPool();

	/**
	 * Gets the texture of a player.
	 *
	 * @param player the player whose texture is to be retrieved.
	 * @return the texture of the player, or null if it cannot be retrieved.
	 */
	public static String getTexture(Player player) {
		if (textures.containsKey(player.getName())) {
			return textures.get(player.getName());
		}
		String[] textures = getFromPlayer(player);
		try {
			String texture = textures[0];
			PlayerSkin.textures.put(player.getName(), texture);
			return texture;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the texture of a player by their name.
	 *
	 * @param name the name of the player whose texture is to be retrieved.
	 * @return the texture of the player, or null if it cannot be retrieved.
	 */
	public static String getTexture(String name) {
		if (textures.containsKey(name)) {
			return textures.get(name);
		}

		Player player = Bukkit.getPlayer(name);
		if (player != null) {
			return getTexture(player);
		}

		pool.execute(() -> {
			String[] textures = getFromName(name);
			try {
				String texture = textures[0];
				PlayerSkin.textures.put(name, texture);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

	/**
	 * Gets the texture and signature of a player from their player object.
	 *
	 * @param playerBukkit the player whose texture and signature are to be retrieved.
	 * @return an array containing the texture and signature of the player.
	 */
	public static String[] getFromPlayer(Player playerBukkit) {
		GameProfile profile = getProfile(playerBukkit);
		Property property = profile.getProperties().get("textures").iterator().next();
		String texture = property.getValue();
		String signature = property.getSignature();

		return new String[]{texture, signature};
	}

	/**
	 * Gets the texture and signature of a player from their name.
	 *
	 * @param name the name of the player whose texture and signature are to be retrieved.
	 * @return an array containing the texture and signature of the player.
	 */
	@SuppressWarnings("deprecation")
	public static String[] getFromName(String name) {
		try {
			URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
			String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

			URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
			JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties")
					.getAsJsonArray().get(0).getAsJsonObject();
			String texture = textureProperty.get("value").getAsString();
			String signature = textureProperty.get("signature").getAsString();

			return new String[]{texture, signature};
		} catch (IOException e) {
			System.err.println("Could not get skin data from session servers!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the GameProfile of a player.
	 *
	 * @param player the player whose GameProfile is to be retrieved.
	 * @return the GameProfile of the player.
	 */
	public static GameProfile getProfile(Player player) {
		try {
			Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
			return (GameProfile) entityPlayer.getClass().getMethod(getMethodName()).invoke(entityPlayer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the method name for retrieving the GameProfile based on the server version.
	 *
	 * @return the method name for retrieving the GameProfile.
	 */
	public static String getMethodName() {
		double nmsVersion = NmsVersion.nmsVersion.getVersion();
		if (nmsVersion >= NmsVersion.V_1_19.getVersion() && nmsVersion <= NmsVersion.V_1_19_2.getVersion()) {
			return "fz";
		} else if (nmsVersion >= NmsVersion.V_1_18.getVersion() && nmsVersion <= NmsVersion.V_1_18_2.getVersion()) {
			return "fp";
		}
		return "getProfile";
	}
}
