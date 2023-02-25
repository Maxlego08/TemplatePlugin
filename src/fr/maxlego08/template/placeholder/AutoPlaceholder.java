package fr.maxlego08.template.placeholder;

import org.bukkit.entity.Player;

public class AutoPlaceholder {

	private final String startWith;
	private final ReturnBiConsumer<Player, String, String> biConsumer;

	/**
	 * @param startWith
	 * @param biConsumer
	 */
	public AutoPlaceholder(String startWith, ReturnBiConsumer<Player, String, String> biConsumer) {
		super();
		this.startWith = startWith;
		this.biConsumer = biConsumer;
	}

	/**
	 * @return the startWith
	 */
	public String getStartWith() {
		return startWith;
	}

	/**
	 * @return the biConsumer
	 */
	public ReturnBiConsumer<Player, String, String> getBiConsumer() {
		return biConsumer;
	}

	public String accept(Player player, String value) {
		return this.biConsumer.accept(player, value);
	}

}
