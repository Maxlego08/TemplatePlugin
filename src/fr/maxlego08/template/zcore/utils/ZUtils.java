package fr.maxlego08.template.zcore.utils;

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.enums.EnumInventory;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.builder.CooldownBuilder;
import fr.maxlego08.template.zcore.utils.builder.TimerBuilder;
import fr.maxlego08.template.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.template.zcore.utils.nms.NmsVersion;
import fr.maxlego08.template.zcore.utils.players.ActionBar;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public abstract class ZUtils extends MessageUtils {

    // For plugin support from 1.8 to 1.12
    private static Material[] byId;

    static {
        if (!NmsVersion.nmsVersion.isNewMaterial()) {
            byId = new Material[0];
            for (Material material : Material.values()) {
                if (byId.length <= material.getId()) {
                    byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
                }
                byId[material.getId()] = material;
            }
        }
    }

    /**
     * Encodes an ItemStack to a base64 string.
     *
     * @param item the ItemStack to encode.
     * @return the encoded item as a base64 string.
     */
    protected String encode(ItemStack item) {
        return ItemStackUtils.serializeItemStack(item);
    }

    /**
     * Decodes a base64 string to an ItemStack.
     *
     * @param item the encoded ItemStack as a base64 string.
     * @return the decoded ItemStack.
     */
    protected ItemStack decode(String item) {
        return ItemStackUtils.deserializeItemStack(item);
    }

    /**
     * Gets a random number between the specified bounds.
     *
     * @param min the lower bound (inclusive).
     * @param max the upper bound (exclusive).
     * @return a random number between a and b.
     */
    protected int getNumberBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    /**
     * Checks if the player's inventory is full.
     *
     * @param player the player to check.
     * @return true if the player's inventory is full, false otherwise.
     */
    protected boolean hasInventoryFull(Player player) {
        int slot = 0;
        PlayerInventory inventory = player.getInventory();
        for (int a = 0; a != 36; a++) {
            ItemStack itemStack = inventory.getContents()[a];
            if (itemStack == null) slot++;
        }
        return slot == 0;
    }


    /**
     * Gives an item to the player. If the player's inventory is full, the item will be dropped on the ground.
     *
     * @param player the player to receive the item.
     * @param item   the item to give to the player.
     */
    protected void give(Player player, ItemStack item) {
        if (hasInventoryFull(player)) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
    }

    /**
     * Gets a Material based on its ID. Works only for plugins from versions 1.8 to 1.12.
     *
     * @param id the ID of the material.
     * @return the Material corresponding to the ID, or Material.AIR if the ID is invalid.
     */
    protected Material getMaterial(int id) {
        return byId.length > id && id >= 0 ? byId[id] : Material.AIR;
    }

    /**
     * Checks if an ItemStack has a display name.
     *
     * @param itemStack the ItemStack to check.
     * @return true if the ItemStack has a display name, false otherwise.
     */
    protected boolean hasDisplayName(ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
    }

    /**
     * Checks if the name of an ItemStack is the same as the given string.
     *
     * @param itemStack the ItemStack to check.
     * @param name      the string to compare with the ItemStack's name.
     * @return true if the ItemStack's name is the same as the given string, false otherwise.
     */
    protected boolean same(ItemStack itemStack, String name) {
        return this.hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().equals(name);
    }

    /**
     * Checks if the name of an ItemStack contains the given string.
     *
     * @param itemStack the ItemStack to check.
     * @param name      the string to check if it is contained in the ItemStack's name.
     * @return true if the ItemStack's name contains the given string, false otherwise.
     */
    protected boolean contains(ItemStack itemStack, String name) {
        return this.hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().contains(name);
    }

    /**
     * Removes a specified number of items from the player's hand. If the amount to be removed is not specified, it defaults to 64.
     *
     * @param player the player from whose hand the items will be removed.
     */
    protected void removeItemInHand(Player player) {
        removeItemInHand(player, 64);
    }

    /**
     * Removes a specified number of items from the player's hand.
     *
     * @param player the player from whose hand the items will be removed.
     * @param how    the number of items to remove.
     */
    protected void removeItemInHand(Player player, int how) {
        if (player.getItemInHand().getAmount() > how) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - how);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR));
        }
        player.updateInventory();
    }

    /**
     * Checks if two locations are identical.
     *
     * @param firstLocation  the first location.
     * @param secondLocation the second location.
     * @return true if both locations are the same, false otherwise.
     */
    protected boolean same(Location firstLocation, Location secondLocation) {
        return (firstLocation.getBlockX() == secondLocation.getBlockX()) && (firstLocation.getBlockY() == secondLocation.getBlockY()) && (firstLocation.getBlockZ() == secondLocation.getBlockZ()) && firstLocation.getWorld().getName().equals(secondLocation.getWorld().getName());
    }

    /**
     * Formats a double value into a string with two decimal places.
     *
     * @param decimal the double value to format.
     * @return the formatted string.
     */
    protected String format(double decimal) {
        return format(decimal, "#.##");
    }


    /**
     * Formats a double value into a string according to a specified format.
     *
     * @param decimal the double value to format.
     * @param format  the format to apply.
     * @return the formatted string.
     */
    protected String format(double decimal, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(decimal);
    }

    /**
     * Removes a specified number of items from a player's inventory.
     *
     * @param player    the player whose items will be removed.
     * @param amount    the number of items to remove.
     * @param itemStack the ItemStack to be removed.
     */
    protected void removeItems(Player player, int amount, ItemStack itemStack) {
        int slot = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is != null && is.isSimilar(itemStack) && amount > 0) {
                int currentAmount = is.getAmount() - amount;
                amount -= is.getAmount();
                if (currentAmount <= 0) {
                    if (slot == 40) {
                        player.getInventory().setItemInOffHand(null);
                    } else {
                        player.getInventory().removeItem(is);
                    }
                } else {
                    is.setAmount(currentAmount);
                }
            }
            slot++;
        }
        player.updateInventory();
    }

    /**
     * Schedules a task to be run after a specified delay.
     *
     * @param delay    the delay in milliseconds before the task is executed.
     * @param runnable the task to be executed.
     */
    protected void schedule(long delay, Runnable runnable) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (runnable != null) runnable.run();
            }
        }, delay);
    }

    /**
     * Formats a string by replacing underscores with spaces and capitalizing the first letter.
     *
     * @param string the string to format.
     * @return the formatted string.
     */
    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Formats a Material name by replacing underscores with spaces and capitalizing the first letter.
     *
     * @param string the Material to format.
     * @return the formatted string.
     */
    protected String name(Material string) {
        String name = string.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Gets the name of an ItemStack.
     *
     * @param itemStack the ItemStack to get the name of.
     * @return the name of the ItemStack.
     */
    protected String name(ItemStack itemStack) {
        return this.getItemName(itemStack);
    }

    /**
     * Calculates the maximum number of pages needed to display a collection of items, assuming 45 items per page.
     *
     * @param items the collection of items.
     * @return the maximum number of pages.
     */
    protected int getMaxPage(Collection<?> items) {
        return (items.size() / 45) + 1;
    }

    /**
     * Calculates the maximum number of pages needed to display a collection of items, with a specified number of items per page.
     *
     * @param items the collection of items.
     * @param a     the number of items per page.
     * @return the maximum number of pages.
     */
    protected int getMaxPage(Collection<?> items, int a) {
        return (items.size() / a) + 1;
    }

    /**
     * Calculates the percentage of a value relative to a total.
     *
     * @param value the value.
     * @param total the total.
     * @return the percentage of the value relative to the total.
     */
    protected double percent(double value, double total) {
        return (value * 100) / total;
    }

    /**
     * Calculates the numerical value of a percentage of a total.
     *
     * @param total   the total.
     * @param percent the percentage.
     * @return the numerical value of the percentage of the total.
     */
    protected double percentNum(double total, double percent) {
        return total * (percent / 100);
    }

    /**
     * Schedules a repeated task with a specified delay and count.
     *
     * @param plugin   the plugin instance.
     * @param delay    the delay in milliseconds between each execution.
     * @param count    the number of times to execute the task.
     * @param runnable the task to be executed.
     */
    protected void schedule(Plugin plugin, long delay, int count, Runnable runnable) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int tmpCount = 0;

            @Override
            public void run() {
                if (!plugin.isEnabled()) {
                    this.cancel();
                    return;
                }

                if (tmpCount > count) {
                    this.cancel();
                    return;
                }

                tmpCount++;
                Bukkit.getScheduler().runTask(plugin, runnable);
            }
        }, 0, delay);
    }


    /**
     * Creates an inventory for a player with the specified template and inventory type.
     *
     * @param plugin    the plugin instance.
     * @param player    the player for whom the inventory is created.
     * @param inventory the type of inventory to create.
     */
    protected void createInventory(Template plugin, Player player, EnumInventory inventory) {
        createInventory(plugin, player, inventory, 1);
    }

    /**
     * Creates an inventory for a player with the specified template, inventory type, and page number.
     *
     * @param plugin    the plugin instance.
     * @param player    the player for whom the inventory is created.
     * @param inventory the type of inventory to create.
     * @param page      the page number of the inventory.
     */
    protected void createInventory(Template plugin, Player player, EnumInventory inventory, int page) {
        createInventory(plugin, player, inventory, page, new Object() {
        });
    }

    /**
     * Creates an inventory for a player with the specified template, inventory type, page number, and additional objects.
     *
     * @param plugin    the plugin instance.
     * @param player    the player for whom the inventory is created.
     * @param inventory the type of inventory to create.
     * @param page      the page number of the inventory.
     * @param objects   additional objects to be used in creating the inventory.
     */
    protected void createInventory(Template plugin, Player player, EnumInventory inventory, int page, Object... objects) {
        plugin.getInventoryManager().createInventory(inventory, player, page, objects);
    }

    /**
     * Creates an inventory for a player with the specified template, inventory ID, page number, and additional objects.
     *
     * @param plugin    the plugin instance.
     * @param player    the player for whom the inventory is created.
     * @param inventory the ID of the inventory to create.
     * @param page      the page number of the inventory.
     * @param objects   additional objects to be used in creating the inventory.
     */
    protected void createInventory(Template plugin, Player player, int inventory, int page, Object... objects) {
        plugin.getInventoryManager().createInventory(inventory, player, page, objects);
    }

    /**
     * Checks if a permissible entity has a specific permission.
     *
     * @param permissible the entity to check.
     * @param permission  the permission to check for.
     * @return true if the entity has the permission, false otherwise.
     */
    protected boolean hasPermission(Permissible permissible, Permission permission) {
        return permissible.hasPermission(permission.getPermission());
    }

    /**
     * Checks if a permissible entity has a specific permission.
     *
     * @param permissible the entity to check.
     * @param permission  the permission string to check for.
     * @return true if the entity has the permission, false otherwise.
     */
    protected boolean hasPermission(Permissible permissible, String permission) {
        return permissible.hasPermission(permission);
    }

    /**
     * Schedules a fixed-rate task with a delay.
     *
     * @param plugin   the plugin instance.
     * @param delay    the delay in milliseconds.
     * @param consumer the consumer to execute with the task and status.
     * @return the scheduled {@link TimerTask}.
     */
    protected TimerTask scheduleFix(Plugin plugin, long delay, BiConsumer<TimerTask, Boolean> consumer) {
        return this.scheduleFix(plugin, delay, delay, consumer);
    }

    /**
     * Schedules a fixed-rate task with a start delay and a subsequent delay.
     *
     * @param plugin   the plugin instance.
     * @param startAt  the initial delay in milliseconds.
     * @param delay    the subsequent delay in milliseconds.
     * @param consumer the consumer to execute with the task and status.
     * @return the scheduled {@link TimerTask}.
     */
    protected TimerTask scheduleFix(Plugin plugin, long startAt, long delay, BiConsumer<TimerTask, Boolean> consumer) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!plugin.isEnabled()) {
                    cancel();
                    consumer.accept(this, false);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(this, true));
            }
        };
        new Timer().scheduleAtFixedRate(task, startAt, delay);
        return task;
    }

    /**
     * Gets a random element from a list.
     *
     * @param elements the list of elements to choose from.
     * @param <T>      the type of elements in the list.
     * @return a random element from the list, or null if the list is empty.
     */
    protected <T> T randomElement(List<T> elements) {
        if (elements.size() == 0) {
            return null;
        }
        if (elements.size() == 1) {
            return elements.get(0);
        }
        Random random = new Random();
        return elements.get(random.nextInt(elements.size()));
    }

    /**
     * Reverses color codes in a message string.
     *
     * @param message the message string with color codes to reverse.
     * @return the message string with reversed color codes.
     */
    protected String colorReverse(String message) {
        Pattern pattern = Pattern.compile(net.md_5.bungee.api.ChatColor.COLOR_CHAR + "x[a-fA-F0-9-" + net.md_5.bungee.api.ChatColor.COLOR_CHAR + "]{12}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            String colorReplace = color.replace("§x", "#");
            colorReplace = colorReplace.replace("§", "");
            message = message.replace(color, colorReplace);
            matcher = pattern.matcher(message);
        }

        return message == null ? null : message.replace("§", "&");
    }

    /**
     * Converts a list of messages to include color codes.
     *
     * @param messages the list of messages to color.
     * @return the list of colored messages.
     */
    protected List<String> color(List<String> messages) {
        return messages.stream().map(this::color).collect(Collectors.toList());
    }

    /**
     * Reverses color codes in a list of messages.
     *
     * @param messages the list of messages with color codes to reverse.
     * @return the list of messages with reversed color codes.
     */
    protected List<String> colorReverse(List<String> messages) {
        return messages.stream().map(this::colorReverse).collect(Collectors.toList());
    }


    /**
     * Gets an ItemFlag from a string representation.
     *
     * @param flagString the string representation of the ItemFlag.
     * @return the corresponding {@link ItemFlag}, or null if not found.
     */
    protected ItemFlag getFlag(String flagString) {
        for (ItemFlag flag : ItemFlag.values()) {
            if (flag.name().equalsIgnoreCase(flagString)) return flag;
        }
        return null;
    }

    /**
     * Reverses the order of elements in a list.
     *
     * @param list the list to reverse.
     * @param <T>  the type of elements in the list.
     * @return a new list with elements in reverse order.
     */
    protected <T> List<T> reverse(List<T> list) {
        List<T> tmpList = new ArrayList<>();
        for (int index = list.size() - 1; index != -1; index--) {
            tmpList.add(list.get(index));
        }
        return tmpList;
    }

    /**
     * Formats a price with commas as thousand separators.
     *
     * @param price the price to format.
     * @return the formatted price string.
     */
    protected String price(long price) {
        return String.format("%,d", price);
    }

    /**
     * Generates a random string of specified length.
     *
     * @param length the length of the random string.
     * @return the generated random string.
     */
    protected String generateRandomString(int length) {
        RandomString randomString = new RandomString(length);
        return randomString.nextString();
    }

    /**
     * Builds a TextComponent from a message string.
     *
     * @param message the message string.
     * @return the created {@link TextComponent}.
     */
    protected TextComponent buildTextComponent(String message) {
        return new TextComponent(message);
    }

    /**
     * Sets a hover message for a TextComponent.
     *
     * @param component the TextComponent to set the hover message for.
     * @param messages  the hover messages.
     * @return the TextComponent with the hover message set.
     */
    protected TextComponent setHoverMessage(TextComponent component, String... messages) {
        BaseComponent[] list = new BaseComponent[messages.length];
        for (int a = 0; a != messages.length; a++) {
            list[a] = new TextComponent(messages[a] + (messages.length - 1 == a ? "" : "\n"));
        }
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list));
        return component;
    }

    /**
     * Sets a hover message for a TextComponent.
     *
     * @param component the TextComponent to set the hover message for.
     * @param messages  the hover messages.
     * @return the TextComponent with the hover message set.
     */
    protected TextComponent setHoverMessage(TextComponent component, List<String> messages) {
        BaseComponent[] list = new BaseComponent[messages.size()];
        for (int a = 0; a != messages.size(); a++) {
            list[a] = new TextComponent(messages.get(a) + (messages.size() - 1 == a ? "" : "\n"));
        }
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list));
        return component;
    }

    /**
     * Sets a click action for a TextComponent.
     *
     * @param component the TextComponent to set the click action for.
     * @param action    the click action.
     * @param command   the command to execute on click.
     * @return the TextComponent with the click action set.
     */
    protected TextComponent setClickAction(TextComponent component, net.md_5.bungee.api.chat.ClickEvent.Action action, String command) {
        component.setClickEvent(new ClickEvent(action, command));
        return component;
    }

    /**
     * Formats a balance value for display.
     *
     * @param value the balance value.
     * @return the formatted balance string.
     */
    protected String getDisplayBalance(double value) {
        if (value < 10000) return format(value, "#.#");
        else if (value < 1000000) return (int) (value / 1000) + "k ";
        else if (value < 1000000000) return format((value / 1000) / 1000, "#.#") + "m ";
        else if (value < 1000000000000L) return (int) (((value / 1000) / 1000) / 1000) + "M ";
        else return "too much";
    }

    /**
     * Formats a balance value for display.
     *
     * @param value the balance value.
     * @return the formatted balance string.
     */
    protected String getDisplayBalance(long value) {
        if (value < 10000) return format(value, "#.#");
        else if (value < 1000000) return (int) (value / 1000) + "k ";
        else if (value < 1000000000) return format((double) (value / 1000) / 1000, "#.#") + "m ";
        else if (value < 1000000000000L) return (int) (((value / 1000) / 1000) / 1000) + "M ";
        else return "too much";
    }

    /**
     * Counts the number of items of a specific material in an inventory.
     *
     * @param inventory the inventory to check.
     * @param material  the material to count.
     * @return the number of items of the specified material in the inventory.
     */
    protected int count(org.bukkit.inventory.Inventory inventory, Material material) {
        int count = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType().equals(material)) {
                count += itemStack.getAmount();
            }
        }
        return count;
    }

    /**
     * Gets an Enchantment from a string representation.
     *
     * @param str the string representation of the Enchantment.
     * @return the corresponding {@link Enchantment}, or null if not found.
     */
    protected Enchantment enchantFromString(String str) {
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getName().equalsIgnoreCase(str)) return enchantment;
        }
        return null;
    }


    /**
     * Gets the closest BlockFace based on a given direction.
     *
     * @param direction the direction in degrees.
     * @return the closest {@link BlockFace}.
     */
    protected BlockFace getClosestFace(float direction) {
        direction = direction % 360;

        if (direction < 0) direction += 360;

        direction = Math.round(direction / 45);

        switch ((int) direction) {
            case 1:
                return BlockFace.NORTH_WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH;
            case 7:
                return BlockFace.SOUTH_WEST;
            default:
                return BlockFace.WEST;
        }
    }

    /**
     * Formats a price by inserting periods as thousand separators.
     *
     * @param price the price to format.
     * @return the formatted price string.
     */
    protected String betterPrice(long price) {
        StringBuilder betterPrice = new StringBuilder();
        String[] splitPrice = String.valueOf(price).split("");
        int current = 0;
        for (int a = splitPrice.length - 1; a > -1; a--) {
            current++;
            if (current > 3) {
                betterPrice.append(".");
                current = 1;
            }
            betterPrice.append(splitPrice[a]);
        }
        StringBuilder builder = new StringBuilder().append(betterPrice);
        builder.reverse();
        return builder.toString();
    }

    /**
     * Checks if an item stack has a specific enchantment.
     *
     * @param enchantment the enchantment to check.
     * @param itemStack   the item stack to check.
     * @return true if the item stack has the enchantment, false otherwise.
     */
    protected boolean hasEnchant(Enchantment enchantment, ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants() && itemStack.getItemMeta().hasEnchant(enchantment);
    }

    /**
     * Formats a cooldown timer for a player.
     *
     * @param player   the player to format the timer for.
     * @param cooldown the name of the cooldown.
     * @return the formatted timer string.
     */
    protected String timerFormat(Player player, String cooldown) {
        return TimerBuilder.getStringTime(CooldownBuilder.getCooldownPlayer(cooldown, player) / 1000);
    }

    /**
     * Checks if a player is currently on a cooldown.
     *
     * @param player   the player to check.
     * @param cooldown the name of the cooldown.
     * @return true if the player is on cooldown, false otherwise.
     */
    protected boolean isCooldown(Player player, String cooldown) {
        return isCooldown(player, cooldown, 0);
    }

    /**
     * Checks if a player is currently on a cooldown, and optionally sets a new cooldown timer.
     *
     * @param player   the player to check.
     * @param cooldown the name of the cooldown.
     * @param timer    the duration of the new cooldown timer, in seconds.
     * @return true if the player is on cooldown, false otherwise.
     */
    protected boolean isCooldown(Player player, String cooldown, int timer) {
        if (CooldownBuilder.isCooldown(cooldown, player)) {
            ActionBar.sendActionBar(player, String.format("§cVous devez attendre encore §6%s §cavant de pouvoir faire cette action.", timerFormat(player, cooldown)));
            return true;
        }
        if (timer > 0) CooldownBuilder.addCooldown(cooldown, player, timer);
        return false;
    }

    /**
     * Converts a stream of strings to a formatted list string with color codes.
     *
     * @param list the stream of strings to convert.
     * @return the formatted list string.
     */
    protected String toList(Stream<String> list) {
        return toList(list.collect(Collectors.toList()), "§e", "§6");
    }

    /**
     * Converts a list of strings to a formatted list string with color codes.
     *
     * @param list the list of strings to convert.
     * @return the formatted list string.
     */
    protected String toList(List<String> list) {
        return toList(list, "§e", "§6§n");
    }

    /**
     * Converts a list of strings to a formatted list string with specified color codes.
     *
     * @param list   the list of strings to convert.
     * @param color  the primary color code.
     * @param color2 the secondary color code.
     * @return the formatted list string.
     */
    protected String toList(List<String> list, String color, String color2) {
        if (list == null || list.size() == 0) return null;
        if (list.size() == 1) return list.get(0);
        StringBuilder str = new StringBuilder();
        for (int a = 0; a != list.size(); a++) {
            if (a == list.size() - 1) str.append(color).append(" et ").append(color2);
            else if (a != 0) str.append(color).append(", ").append(color2);
            str.append(list.get(a));
        }
        return str.toString();
    }


    /**
     * Formats a long value with a default grouping separator.
     *
     * @param value the long value to format.
     * @return the formatted string.
     */
    protected String format(long value) {
        return format(value, ' ');
    }

    /**
     * Formats a long value with a specified grouping separator.
     *
     * @param l the long value to format.
     * @param c the character to use as the grouping separator.
     * @return the formatted string.
     */
    protected String format(long l, char c) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(c);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(l);
    }

    /**
     * Obtains a player's head item stack using the inventory configuration system.
     *
     * @param itemStack the original item stack.
     * @param player    the player whose head is to be represented.
     * @return the modified item stack representing the player's head.
     */
    public ItemStack playerHead(ItemStack itemStack, OfflinePlayer player) {
        String name = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : null;
        if (NmsVersion.nmsVersion.isNewMaterial()) {
            if (itemStack.getType().equals(Material.PLAYER_HEAD) && name != null && name.startsWith("HEAD")) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                name = name.replace("HEAD", "");
                if (name.length() == 0) meta.setDisplayName(null);
                else meta.setDisplayName(name);
                meta.setOwningPlayer(player);
                itemStack.setItemMeta(meta);
            }
        } else {
            if (itemStack.getType().equals(getMaterial(397)) && itemStack.getData().getData() == 3 && name != null && name.startsWith("HEAD")) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                name = name.replace("HEAD", "");
                if (name.length() == 0) meta.setDisplayName(null);
                else meta.setDisplayName(name);
                meta.setOwner(player.getName());
                itemStack.setItemMeta(meta);
            }
        }
        return itemStack;
    }

    /**
     * Creates an item stack representing a player's head.
     *
     * @return the item stack representing a player's head.
     */
    protected ItemStack playerHead() {
        return NmsVersion.nmsVersion.isNewMaterial() ? new ItemStack(Material.PLAYER_HEAD) : new ItemStack(getMaterial(397), 1, (byte) 3);
    }

    /**
     * Obtains a service provider instance of a specified class from the plugin.
     *
     * @param plugin the plugin providing the service.
     * @param classz the class of the service provider.
     * @param <T>    the type of the service provider.
     * @return the service provider instance, or null if not found.
     */
    protected <T> T getProvider(Plugin plugin, Class<T> classz) {
        RegisteredServiceProvider<T> provider = plugin.getServer().getServicesManager().getRegistration(classz);
        if (provider == null) return null;
        return provider.getProvider() != null ? provider.getProvider() : null;
    }

    /**
     * Gets the potion effect type corresponding to a given configuration string.
     *
     * @param configuration the configuration string representing the potion effect type.
     * @return the potion effect type, or null if not found.
     */
    protected PotionEffectType getPotion(String configuration) {
        for (PotionEffectType effectType : PotionEffectType.values()) {
            if (effectType.getName().equalsIgnoreCase(configuration)) {
                return effectType;
            }
        }
        return null;
    }

    /**
     * Executes a runnable asynchronously.
     *
     * @param plugin   the plugin scheduling the task.
     * @param runnable the runnable to execute.
     */
    protected void runAsync(Plugin plugin, Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }


    /**
     * Converts a given time in seconds to a formatted string representation.
     *
     * @param second the time in seconds.
     * @return the formatted time string.
     */
    protected String getStringTime(long second) {
        return TimerBuilder.getStringTime(second);
    }

    /**
     * Creates a player head item from a specified URL.
     *
     * @param url the URL of the texture to be applied to the head.
     * @return the created {@link ItemStack} representing the player head.
     */
    protected ItemStack createSkull(String url) {
        ItemStack head = playerHead();
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "random_name");

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Checks if an item stack is a player head.
     *
     * @param itemStack the item stack to check.
     * @return true if the item stack is a player head, false otherwise.
     */
    protected boolean isPlayerHead(ItemStack itemStack) {
        Material material = itemStack.getType();
        if (NmsVersion.nmsVersion.isNewMaterial()) return material.equals(Material.PLAYER_HEAD);
        return (material.equals(getMaterial(397))) && (itemStack.getDurability() == 3);
    }

    /**
     * Gets the value of a private field from an object.
     *
     * @param object the object containing the field.
     * @param field  the name of the field to retrieve.
     * @return the value of the field.
     * @throws SecurityException        if a security violation occurs.
     * @throws NoSuchFieldException     if the field does not exist.
     * @throws IllegalArgumentException if an illegal argument is provided.
     * @throws IllegalAccessException   if the field is not accessible.
     */
    protected Object getPrivateField(Object object, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField = field.equals("commandMap") ? clazz.getDeclaredField(field) : field.equals("knownCommands") ? NmsVersion.nmsVersion.isNewMaterial() ? clazz.getSuperclass().getDeclaredField(field) : clazz.getDeclaredField(field) : null;
        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }

    /**
     * Unregisters a Bukkit command from the server.
     *
     * @param plugin  the plugin that registered the command.
     * @param command the {@link PluginCommand} to unregister.
     */
    protected void unRegisterBukkitCommand(Plugin plugin, PluginCommand command) {
        try {
            Object result = getPrivateField(plugin.getServer().getPluginManager(), "commandMap");
            SimpleCommandMap commandMap = (SimpleCommandMap) result;

            Object map = getPrivateField(commandMap, "knownCommands");
            @SuppressWarnings("unchecked") HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            knownCommands.remove(command.getName());
            for (String alias : command.getAliases()) {
                knownCommands.remove(alias);
            }
            knownCommands.remove(plugin.getName() + ":" + command.getName());
            for (String alias : command.getAliases()) {
                knownCommands.remove(plugin.getName() + ":" + alias);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a glow effect to an item stack.
     *
     * @param itemStack the item stack to make glow.
     */
    public void glow(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Clears a player's inventory, removes potion effects, and resets their status.
     *
     * @param player the player to be cleared.
     */
    protected void clearPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setBoots(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setHelmet(null);
        player.getPlayer().setItemOnCursor(null);
        player.getPlayer().setFireTicks(0);
        player.getPlayer().getOpenInventory().getTopInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.getPlayer().getActivePotionEffects().forEach(e -> {
            player.getPlayer().removePotionEffect(e.getType());
        });
    }

    /**
     * Creates a progress bar string representation.
     *
     * @param current           the current value.
     * @param max               the maximum value.
     * @param totalBars         the total number of bars.
     * @param symbol            the symbol used for the progress bar.
     * @param completedColor    the color for completed parts of the bar.
     * @param notCompletedColor the color for incomplete parts of the bar.
     * @return the string representation of the progress bar.
     */
    public String getProgressBar(int current, int max, int totalBars, char symbol, String completedColor, String notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat(completedColor + symbol, progressBars) + Strings.repeat(notCompletedColor + symbol, totalBars - progressBars);
    }

    /**
     * Creates a progress bar string representation using a ProgressBar object.
     *
     * @param current     the current value.
     * @param max         the maximum value.
     * @param progressBar the ProgressBar object containing bar settings.
     * @return the string representation of the progress bar.
     */
    public String getProgressBar(int current, int max, ProgressBar progressBar) {
        return this.getProgressBar(current, max, progressBar.getLength(), progressBar.getSymbol(), progressBar.getCompletedColor(), progressBar.getNotCompletedColor());
    }

    /**
     * Checks if a player's inventory contains any items or armor.
     *
     * @param player the player whose inventory is to be checked.
     * @return true if the inventory contains any items or armor, false otherwise.
     */
    protected boolean inventoryHasItem(Player player) {
        ItemStack itemStack = player.getInventory().getBoots();
        if (itemStack != null) {
            return true;
        }

        itemStack = player.getInventory().getChestplate();
        if (itemStack != null) {
            return true;
        }

        itemStack = player.getInventory().getLeggings();
        if (itemStack != null) {
            return true;
        }

        itemStack = player.getInventory().getHelmet();
        if (itemStack != null) {
            return true;
        }

        for (ItemStack currentItemStack : player.getInventory().getContents()) {
            if (currentItemStack != null) {
                return true;
            }
        }

        return false;
    }


}
