package fr.maxlego08.template.zcore.utils.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saveable;

public class CooldownBuilder implements Saveable {

	public static Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

	/**
	 * 
	 * @param
	 * @return
	 */
	public static Map<UUID, Long> getCooldownMap(String key) {
		return cooldowns.getOrDefault(key, null);
	}

	/**
	 * 
	 */
	public static void clear() {
		cooldowns.clear();
	}

	/**
	 * 
	 * @param key
	 */
	public static void createCooldown(String key) {
		cooldowns.putIfAbsent(key, new HashMap<>());
	}

	/**
	 * 
	 * @param key
	 * @param joueur
	 */
	public static void removeCooldown(String key, UUID uuid) {

		createCooldown(key);

		getCooldownMap(key).remove(uuid);
	}

	/**
	 * 
	 * @param key
	 * @param player
	 */
	public static void removeCooldown(String key, Player player) {
		removeCooldown(key, player.getUniqueId());
	}

	/**
	 * 
	 * @param key
	 * @param joueur
	 * @param seconds
	 */
	public static void addCooldown(String key, UUID uuid, int seconds) {

		createCooldown(key);

		long next = System.currentTimeMillis() + seconds * 1000L;
		getCooldownMap(key).put(uuid, Long.valueOf(next));
	}
	
	/**
	 * 
	 * @param key
	 * @param player
	 * @param seconds
	 */
	public static void addCooldown(String key, Player player, int seconds) {
		addCooldown(key, player.getUniqueId(), seconds);
	}

	/**
	 * 
	 * @param key
	 * @param uuid
	 * @return boolean
	 */
	public static boolean isCooldown(String key, UUID uuid) {

		createCooldown(key);
		Map<UUID, Long> map = cooldowns.get(key);

		return (map.containsKey(uuid)) && (System.currentTimeMillis() <= ((Long) map.get(uuid)).longValue());
	}

	/**
	 * 
	 * @param key
	 * @param player
	 * @return boolean
	 */
	public static boolean isCooldown(String key, Player player) {
		return isCooldown(key, player.getUniqueId());
	}

	/**
	 * 
	 * @param key
	 * @param uuid
	 * @return long
	 */
	public static long getCooldown(String key, UUID uuid) {

		createCooldown(key);
		Map<UUID, Long> map = cooldowns.get(key);

		return ((Long) map.getOrDefault(uuid, 0l)).longValue() - System.currentTimeMillis();
	}

	/**
	 * 
	 * @param key
	 * @param player
	 * @return long
	 */
	public static long getCooldownPlayer(String key, Player player) {
		return getCooldown(key, player.getUniqueId());
	}

	/**
	 * 
	 * @param key
	 * @param player
	 * @return
	 */
	public static String getCooldownAsString(String key, UUID player) {
		return TimerBuilder.getStringTime(getCooldown(key, player) / 1000);
	}

	private static transient CooldownBuilder i = new CooldownBuilder();

	@Override
	public void save(Persist persist) {
		persist.save(i, "cooldowns");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, CooldownBuilder.class, "cooldowns");
	}
}
