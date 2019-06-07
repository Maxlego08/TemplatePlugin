package fr.maxlego08.template.zcore.utils.builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CooldownBuilder {

	public static HashMap<String, HashMap<UUID, Long>> Cooldown = new HashMap<>();
	
	public static HashMap<UUID, Long> getCooldownMap(String s) {
		if (Cooldown.containsKey(s)) {
			return Cooldown.get(s);
		}
		return null;
	}
	
	public static void getClearCooldown() {
		Cooldown.clear();
	}
	
	public static void getCreatedCooldown(String s) {
		if (Cooldown.containsKey(s)) {
			throw new IllegalArgumentException("Ce cooldown existe déjà.");
		}
		Cooldown.put(s, new HashMap<>());
	}
	
	public static void getRemoveCooldown(String s, Player joueur) throws IOException {
		if (!Cooldown.containsKey(s)) {
			throw new IllegalArgumentException("! Attention ! " + String.valueOf(s) + " n'existe pas.");
		}	
		(Cooldown.get(s)).remove(joueur.getUniqueId());
	}
	
	public static void addCooldown(String s, Player joueur, int seconds) throws IOException {
		if (!Cooldown.containsKey(s)) {
			throw new IllegalArgumentException(String.valueOf(s) +" n'existe pas.");
		}
		long next = System.currentTimeMillis() + seconds * 1000L;
		(Cooldown.get(s)).put(joueur.getUniqueId(), Long.valueOf(next));
	}
	
	public static boolean isCooldown(String s, Player joueur) {
		return (Cooldown.containsKey(s)) && ((Cooldown.get(s)).containsKey(joueur.getUniqueId())) && (System.currentTimeMillis() <= ((Long)(Cooldown.get(s)).get(joueur.getUniqueId())).longValue());
	}
	
	public static long getCooldownPlayerLong(String s, Player joueur) {
		return ((Long)(Cooldown.get(s)).get(joueur.getUniqueId())).longValue() - System.currentTimeMillis();
	}
	
}
