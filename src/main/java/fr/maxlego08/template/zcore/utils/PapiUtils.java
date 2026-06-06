package fr.maxlego08.template.zcore.utils;

import fr.maxlego08.template.placeholder.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Utility class for applying PlaceholderAPI transformations to various objects.
 * Extends {@link TranslationHelper}.
 *
 * @see TranslationHelper
 */
public class PapiUtils extends TranslationHelper {

    /**
     * Applies PlaceholderAPI transformations to the display name and lore of an ItemStack.
     *
     * @param itemStack the ItemStack to transform.
     * @param player    the player context for the placeholders.
     * @return the transformed ItemStack.
     */
    protected ItemStack papi(ItemStack itemStack, Player player) {
        if (itemStack == null) {
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(Placeholder.getPlaceholder().setPlaceholders(player, itemMeta.getDisplayName()));
        }

        if (itemMeta.hasLore()) {
            itemMeta.setLore(Placeholder.getPlaceholder().setPlaceholders(player, itemMeta.getLore()));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Applies PlaceholderAPI transformations to a string.
     *
     * @param placeHolder the string to transform.
     * @param player      the player context for the placeholders.
     * @return the transformed string.
     */
    public String papi(String placeHolder, Player player) {
        return Placeholder.getPlaceholder().setPlaceholders(player, placeHolder);
    }

    /**
     * Applies PlaceholderAPI transformations to a list of strings.
     *
     * @param placeHolder the list of strings to transform.
     * @param player      the player context for the placeholders.
     * @return the transformed list of strings.
     */
    public List<String> papi(List<String> placeHolder, Player player) {
        return Placeholder.getPlaceholder().setPlaceholders(player, placeHolder);
    }
}
