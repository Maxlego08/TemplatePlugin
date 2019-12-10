package fr.maxlego08.template.zcore.utils.builder;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saveable;

public class CooldownBuilder implements Saveable{

	public static HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<>();

	public static HashMap<UUID, Long> getCooldownMap(String s) {
		if (cooldowns.containsKey(s)) {
			return cooldowns.get(s);
		}
		return null;
	}

	public static void clear() {
		cooldowns.clear();
	}

	public static void createCooldown(String s) {
		if (!cooldowns.containsKey(s)) {
			cooldowns.put(s, new HashMap<>());
		}
	}

	public static void removeCooldown(String s, Player joueur) {
		if (!cooldowns.containsKey(s)) {
			throw new IllegalArgumentException("! Attention ! " + String.valueOf(s) + " n'existe pas.");
		}
		(cooldowns.get(s)).remove(joueur.getUniqueId());
	}

	public static void addCooldown(String s, Player joueur, int seconds){
		if (!cooldowns.containsKey(s)) {
			throw new IllegalArgumentException(String.valueOf(s) + " n'existe pas.");
		}
		long next = System.currentTimeMillis() + seconds * 1000L;
		(cooldowns.get(s)).put(joueur.getUniqueId(), Long.valueOf(next));
	}

	public static boolean isCooldown(String s, Player joueur) {
		return (cooldowns.containsKey(s)) && ((cooldowns.get(s)).containsKey(joueur.getUniqueId()))
				&& (System.currentTimeMillis() <= ((Long) (cooldowns.get(s)).get(joueur.getUniqueId())).longValue());
	}

	public static long getCooldownPlayer(String s, Player joueur) {
		return ((Long) (cooldowns.get(s)).getOrDefault(joueur.getUniqueId(), 0l)).longValue() - System.currentTimeMillis();
	}

	public static String getCooldownAsString(String s, Player player) {
		return TimerBuilder.getStringTime(getCooldownPlayer(s, player) / 1000);
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
