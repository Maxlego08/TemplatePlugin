package fr.maxlego08.template.placeholder;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class DistantPlaceholder extends PlaceholderExpansion {

	private final LocalPlaceholder placeholder;

	/**
	 * @param placeholder
	 */
	public DistantPlaceholder(LocalPlaceholder placeholder) {
		super();
		this.placeholder = placeholder;
	}

	@Override
	public String getAuthor() {
		return this.placeholder.getPlugin().getDescription().getAuthors().get(0);
	}

	@Override
	public String getIdentifier() {
		return this.placeholder.getPrefix();
	}

	@Override
	public String getVersion() {
		return this.placeholder.getPlugin().getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onPlaceholderRequest(Player p, String params) {
		return this.placeholder.onRequest(p, params);
	}

}
