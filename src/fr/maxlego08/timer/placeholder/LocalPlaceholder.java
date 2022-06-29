package fr.maxlego08.template.placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;

public class LocalPlaceholder {
	
	private Template plugin;
	private String prefix = "template";
	private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");
	private final List<AutoPlaceholder> autoPlaceholders = new ArrayList<AutoPlaceholder>();

	/**
	 * Set plugin instance
	 * 
	 * @param plugin
	 */
	public void setPlugin(Template plugin) {
		this.plugin = plugin;
	}

	/**
	 * static Singleton instance.
	 */
	private static volatile LocalPlaceholder instance;

	/**
	 * Private constructor for singleton.
	 */
	private LocalPlaceholder() {
	}

	/**
	 * Return a singleton instance of ZPlaceholderApi.
	 */
	public static LocalPlaceholder getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (LocalPlaceholder.class) {
				if (instance == null) {
					instance = new LocalPlaceholder();
				}
			}
		}
		return instance;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * 
	 * @param player
	 * @param displayName
	 * @return
	 */
	public String setPlaceholders(Player player, String placeholder) {

		if (placeholder == null || !placeholder.contains("%")) {
			return placeholder;
		}

		final String realPrefix = this.prefix + "_";

		Matcher matcher = this.pattern.matcher(placeholder);
		while (matcher.find()) {
			String stringPlaceholder = matcher.group(0);
			String regex = matcher.group(1).replace(realPrefix, "");
			String replace = this.onRequest(player, regex);
			if (replace != null) {
				placeholder = placeholder.replace(stringPlaceholder, replace);
			}
		}
		
		return placeholder;
	}

	/**
	 * 
	 * @param player
	 * @param lore
	 * @return
	 */
	public List<String> setPlaceholders(Player player, List<String> lore) {
		return lore == null ? null
				: lore.stream().map(e -> e = setPlaceholders(player, e)).collect(Collectors.toList());
	}

	/**
	 * Custom placeholder
	 * 
	 * @param player
	 * @param string
	 * @return
	 */
	public String onRequest(Player player, String string) {
		
		Optional<AutoPlaceholder> optional = this.autoPlaceholders.stream()
				.filter(e -> string.startsWith(e.getStartWith())).findFirst();
		if (optional.isPresent()) {

			AutoPlaceholder autoPlaceholder = optional.get();
			String value = string.replace(autoPlaceholder.getStartWith(), "");
			return autoPlaceholder.accept(player, value);

		}
		
		return null;
	}
	
	public void register(String startWith, ReturnBiConsumer<Player, String, String> biConsumer) {
		this.autoPlaceholders.add(new AutoPlaceholder(startWith, biConsumer));
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public Template getPlugin() {
		return plugin;
	}
	
}
