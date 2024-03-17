package fr.maxlego08.template.placeholder;

import org.bukkit.entity.Player;

public class AutoPlaceholder {

    private final String startWith;
    private final ReturnBiConsumer<Player, String, String> biConsumer;
    private final ReturnConsumer<Player, String> consumer;

    public AutoPlaceholder(String startWith, ReturnBiConsumer<Player, String, String> biConsumer) {
        super();
        this.startWith = startWith;
        this.biConsumer = biConsumer;
        this.consumer = null;
    }

    public AutoPlaceholder(String startWith, ReturnConsumer<Player, String> consumer) {
        this.startWith = startWith;
        this.biConsumer = null;
        this.consumer = consumer;
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

    public ReturnConsumer<Player, String> getConsumer() {
        return this.consumer;
    }

    public String accept(Player player, String value) {
        if (this.consumer != null) return this.consumer.accept(player);
        if (this.biConsumer != null) return this.biConsumer.accept(player, value);
        return "Error with consumer !";
    }

    public boolean startsWith(String string) {
        return this.consumer != null ? this.startWith.equalsIgnoreCase(string) : string.startsWith(this.startWith);
    }
}
