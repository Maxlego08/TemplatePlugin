package fr.maxlego08.template.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public interface Placeholder {

	static Placeholder getPlaceholder() {
		return LP.placeholder == null ? LP.placeholder = (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ? new Api() : new Local()) : LP.placeholder;
	}

	String setPlaceholders(Player player, String string);

	List<String> setPlaceholders(Player player, List<String> list);

	class Api implements Placeholder {

		public Api() {
			PlaceholderExpansion expansion = new DistantPlaceholder(LocalPlaceholder.getInstance());
			expansion.register();
		}

		@Override
		public String setPlaceholders(Player player, String string) {
			return PlaceholderAPI.setPlaceholders(player, string);
		}

		@Override
		public List<String> setPlaceholders(Player player, List<String> list) {
			return PlaceholderAPI.setPlaceholders(player, list);
		}

	}

	class Local implements Placeholder {

		@Override
		public String setPlaceholders(Player player, String string) {
			return LocalPlaceholder.getInstance().setPlaceholders(player, string);
		}

		@Override
		public List<String> setPlaceholders(Player player, List<String> list) {
			return LocalPlaceholder.getInstance().setPlaceholders(player, list);
		}

	}

	class LP {
		public static Placeholder placeholder;
	}

}
