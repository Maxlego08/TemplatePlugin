package fr.maxlego08.template.zcore.utils;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.enums.EnumInventory;
import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.builder.CooldownBuilder;
import fr.maxlego08.template.zcore.utils.builder.TimerBuilder;
import fr.maxlego08.template.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.template.zcore.utils.nms.NMSUtils;
import fr.maxlego08.template.zcore.utils.players.ActionBar;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public abstract class ZUtils extends MessageUtils {

	private static transient List<String> teleportPlayers = new ArrayList<String>();

	/**
	 * Allows to encode an itemstack in base64
	 *
	 * @param item
	 *            - ItemStack
	 * @return the encoded item
	 */
	protected String encode(ItemStack item) {
		return ItemStackUtils.serializeItemStack(item);
	}

	/**
	 * Allows to decode a string in ItemStack
	 *
	 * @param item
	 *            - the encoded itemstack
	 * @return the decoded item
	 */
	protected ItemStack decode(String item) {
		return ItemStackUtils.deserializeItemStack(item);
	}

	/**
	 * Allows to obtain a random number between a and b
	 * 
	 * @param a
	 * @param b
	 * @return number between a and b
	 */
	protected int getNumberBetween(int a, int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}

	/**
	 * Allows you to check if the inventory is full
	 * 
	 * @param player
	 * @return true if the player's inventory is full
	 */
	protected boolean hasInventoryFull(Player player) {
		int slot = 0;
		PlayerInventory inventory = player.getInventory();
		for (int a = 0; a != 36; a++) {
			ItemStack itemStack = inventory.getContents()[a];
			if (itemStack == null)
				slot++;
		}
		return slot == 0;
	}

	/**
	 * Gives an item to the player, if the player's inventory is full then the
	 * item will drop to the ground
	 * 
	 * @param player
	 * @param item
	 */
	protected void give(Player player, ItemStack item) {
		if (hasInventoryFull(player))
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}

	// For plugin support from 1.8 to 1.12
	private static transient Material[] byId;

	static {
		if (!NMSUtils.isNewVersion()) {
			byId = new Material[0];
			for (Material material : Material.values()) {
				if (byId.length > material.getId()) {
					byId[material.getId()] = material;
				} else {
					byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
					byId[material.getId()] = material;
				}
			}
		}
	}

	/**
	 * Allows to return a material according to its ID Works only for plugins
	 * from 1.8 to 1.12
	 * 
	 * @param id
	 * @return the material according to his id
	 */
	protected Material getMaterial(int id) {
		return byId.length > id && id >= 0 ? byId[id] : Material.AIR;
	}

	/**
	 * Allows to check if an itemstack has a display name
	 *
	 * @return boolean
	 */
	protected boolean hasDisplayName(ItemStack itemStack) {
		return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
	}

	/**
	 * Check if the item name is the same as the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name is the same as string
	 */
	protected boolean same(ItemStack itemStack, String name) {
		return this.hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().equals(name);
	}

	/**
	 * Check if the item name contains the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name contains the string
	 */
	protected boolean contains(ItemStack itemStack, String name) {
		return this.hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().contains(name);
	}

	/**
	 * Remove the item from the player's hand
	 * 
	 * @param player
	 * @param number
	 *            of items to withdraw
	 */
	protected void removeItemInHand(Player player) {
		removeItemInHand(player, 64);
	}

	/**
	 * Remove the item from the player's hand
	 * 
	 * @param player
	 * @param number
	 *            of items to withdraw
	 */
	protected void removeItemInHand(Player player, int how) {
		if (player.getItemInHand().getAmount() > how)
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - how);
		else
			player.setItemInHand(new ItemStack(Material.AIR));
		player.updateInventory();
	}

	/**
	 * Check if two locations are identical
	 * 
	 * @param first
	 *            location
	 * @param second
	 *            location
	 * @return true if both rentals are the same
	 */
	protected boolean same(Location l, Location l2) {
		return (l.getBlockX() == l2.getBlockX()) && (l.getBlockY() == l2.getBlockY())
				&& (l.getBlockZ() == l2.getBlockZ()) && l.getWorld().getName().equals(l2.getWorld().getName());
	}

	/**
	 * Teleport a player to a given location with a given delay
	 * 
	 * @param player
	 *            who will be teleported
	 * @param delay
	 *            before the teleportation of the player
	 * @param location
	 *            where the player will be teleported
	 */
	protected void teleport(Player player, int delay, Location location) {
		teleport(player, delay, location, null);
	}

	/**
	 * Teleport a player to a given location with a given delay
	 * 
	 * @param player
	 *            who will be teleported
	 * @param delay
	 *            before the teleportation of the player
	 * @param location
	 *            where the player will be teleported
	 * @param code
	 *            executed when the player is teleported or not
	 */
	protected void teleport(Player player, int delay, Location location, Consumer<Boolean> cmd) {
		if (teleportPlayers.contains(player.getName())) {
			message(player, Message.TELEPORT_ERROR);
			return;
		}
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		Location playerLocation = player.getLocation();
		AtomicInteger verif = new AtomicInteger(delay);
		teleportPlayers.add(player.getName());
		if (!location.getChunk().isLoaded())
			location.getChunk().load();
		ses.scheduleWithFixedDelay(() -> {
			if (!same(playerLocation, player.getLocation())) {
				message(player, Message.TELEPORT_MOVE);
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				if (cmd != null)
					cmd.accept(false);
				return;
			}
			int currentSecond = verif.getAndDecrement();
			if (!player.isOnline()) {
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				return;
			}
			if (currentSecond == 0) {
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				player.teleport(location);
				message(player, Message.TELEPORT_SUCCESS);
				if (cmd != null)
					cmd.accept(true);
			} else
				message(player, Message.TELEPORT_MESSAGE, currentSecond);
		}, 0, 1, TimeUnit.SECONDS);
	}

	/**
	 * Format a double in a String
	 * 
	 * @param decimal
	 * @return formatting current duplicate
	 */
	protected String format(double decimal) {
		return format(decimal, "#.##");
	}

	/**
	 * Format a double in a String
	 * 
	 * @param decimal
	 * @param format
	 * @return formatting current double according to the given format
	 */
	protected String format(double decimal, String format) {
		DecimalFormat decimalFormat = new DecimalFormat(format);
		return decimalFormat.format(decimal);
	}

	/**
	 * Remove a certain number of items from a player's inventory
	 * 
	 * @param player
	 *            - Player who will have items removed
	 * @param amount
	 *            - Number of items to remove
	 * @param itemStack
	 *            - ItemStack to be removed
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
	 * @param delay
	 * @param runnable
	 */
	protected void schedule(long delay, Runnable runnable) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				if (runnable != null)
					runnable.run();
			}
		}, delay);
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	protected String name(String string) {
		String name = string.replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	protected String name(Material string) {
		String name = string.name().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	protected String name(ItemStack itemStack) {
		return this.getItemName(itemStack);
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	protected int getMaxPage(Collection<?> items) {
		return (items.size() / 45) + 1;
	}

	/**
	 * 
	 * @param items
	 * @param a
	 * @return
	 */
	protected int getMaxPage(Collection<?> items, int a) {
		return (items.size() / a) + 1;
	}

	/**
	 * 
	 * @param value
	 * @param total
	 * @return
	 */
	protected double percent(double value, double total) {
		return (double) ((value * 100) / total);
	}

	/**
	 * 
	 * @param total
	 * @param percent
	 * @return
	 */
	protected double percentNum(double total, double percent) {
		return (double) (total * (percent / 100));
	}

	/**
	 * Schedule task with timer
	 * 
	 * @param delay
	 * @param count
	 * @param runnable
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
	 * 
	 * @param player
	 * @param inventoryId
	 */
	protected void createInventory(Template plugin, Player player, EnumInventory inventory) {
		createInventory(plugin, player, inventory, 1);
	}

	/**
	 * 
	 * @param player
	 * @param inventoryId
	 * @param page
	 */
	protected void createInventory(Template plugin, Player player, EnumInventory inventory, int page) {
		createInventory(plugin, player, inventory, page, new Object() {
		});
	}

	/**
	 * 
	 * @param player
	 * @param inventoryId
	 * @param page
	 * @param objects
	 */
	protected void createInventory(Template plugin, Player player, EnumInventory inventory, int page,
			Object... objects) {
		plugin.getInventoryManager().createInventory(inventory, player, page, objects);
	}

	/**
	 * 
	 * @param player
	 * @param inventory
	 * @param page
	 * @param objects
	 */
	protected void createInventory(Template plugin, Player player, int inventory, int page, Object... objects) {
		plugin.getInventoryManager().createInventory(inventory, player, page, objects);
	}

	/**
	 * 
	 * @param permissible
	 * @param permission
	 * @return
	 */
	protected boolean hasPermission(Permissible permissible, Permission permission) {
		return permissible.hasPermission(permission.getPermission());
	}

	/**
	 * 
	 * @param permissible
	 * @param permission
	 * @return
	 */
	protected boolean hasPermission(Permissible permissible, String permission) {
		return permissible.hasPermission(permission);
	}

	/**
	 * @param delay
	 * @param runnable
	 */
	protected TimerTask scheduleFix(Plugin plugin, long delay, BiConsumer<TimerTask, Boolean> consumer) {
		return this.scheduleFix(plugin, delay, delay, consumer);
	}

	/**
	 * @param plugin
	 * @param startAt
	 * @param delay
	 * @param runnable
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
	 * Get random element from list
	 * 
	 * @param elements
	 * @return element
	 */
	protected <T> T randomElement(List<T> element) {
		if (element.size() == 0) {
			return null;
		}
		if (element.size() == 1) {
			return element.get(0);
		}
		
		Random random = new Random();
		return element.get(random.nextInt(element.size()));
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected String color(String message) {
		if (message == null)
			return null;
		if (NMSUtils.isHexColor()) {
			Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
			Matcher matcher = pattern.matcher(message);
			while (matcher.find()) {
				String color = message.substring(matcher.start(), matcher.end());
				message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
				matcher = pattern.matcher(message);
			}
		}
		return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected String colorReverse(String message) {
		Pattern pattern = Pattern.compile(net.md_5.bungee.api.ChatColor.COLOR_CHAR + "x[a-fA-F0-9-"
				+ net.md_5.bungee.api.ChatColor.COLOR_CHAR + "]{12}");
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
	 * 
	 * @param messages
	 * @return
	 */
	protected List<String> color(List<String> messages) {
		return messages.stream().map(message -> color(message)).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param messages
	 * @return
	 */
	protected List<String> colorReverse(List<String> messages) {
		return messages.stream().map(message -> colorReverse(message)).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param flagString
	 * @return
	 */
	protected ItemFlag getFlag(String flagString) {
		for (ItemFlag flag : ItemFlag.values()) {
			if (flag.name().equalsIgnoreCase(flagString))
				return flag;
		}
		return null;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	protected <T> List<T> reverse(List<T> list) {
		List<T> tmpList = new ArrayList<>();
		for (int index = list.size() - 1; index != -1; index--)
			tmpList.add(list.get(index));
		return tmpList;
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	protected String price(long price) {
		return String.format("%,d", price);
	}

	/**
	 * Allows to generate a string
	 * 
	 * @param length
	 * @return
	 */
	protected String generateRandomString(int length) {
		RandomString randomString = new RandomString(length);
		return randomString.nextString();
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected TextComponent buildTextComponent(String message) {
		return new TextComponent(message);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected TextComponent setHoverMessage(TextComponent component, String... messages) {
		BaseComponent[] list = new BaseComponent[messages.length];
		for (int a = 0; a != messages.length; a++)
			list[a] = new TextComponent(messages[a] + (messages.length - 1 == a ? "" : "\n"));
		component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, list));
		return component;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected TextComponent setHoverMessage(TextComponent component, List<String> messages) {
		BaseComponent[] list = new BaseComponent[messages.size()];
		for (int a = 0; a != messages.size(); a++)
			list[a] = new TextComponent(messages.get(a) + (messages.size() - 1 == a ? "" : "\n"));
		component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, list));
		return component;
	}

	/**
	 * 
	 * @param component
	 * @param action
	 * @param command
	 * @return
	 */
	protected TextComponent setClickAction(TextComponent component, net.md_5.bungee.api.chat.ClickEvent.Action action,
			String command) {
		component.setClickEvent(new ClickEvent(action, command));
		return component;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected String getDisplayBalence(double value) {
		if (value < 10000)
			return format(value, "#.#");
		else if (value < 1000000)
			return String.valueOf(Integer.valueOf((int) (value / 1000))) + "k ";
		else if (value < 1000000000)
			return String.valueOf(format((value / 1000) / 1000, "#.#")) + "m ";
		else if (value < 1000000000000l)
			return String.valueOf(Integer.valueOf((int) (((value / 1000) / 1000) / 1000))) + "M ";
		else
			return "to much";
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected String getDisplayBalence(long value) {
		if (value < 10000)
			return format(value, "#.#");
		else if (value < 1000000)
			return String.valueOf(Integer.valueOf((int) (value / 1000))) + "k ";
		else if (value < 1000000000)
			return String.valueOf(format((value / 1000) / 1000, "#.#")) + "m ";
		else if (value < 1000000000000l)
			return String.valueOf(Integer.valueOf((int) (((value / 1000) / 1000) / 1000))) + "M ";
		else
			return "to much";
	}

	/**
	 * Allows you to count the number of items in inventory
	 * 
	 * @param inventory
	 * @param material
	 * @return
	 */
	protected int count(org.bukkit.inventory.Inventory inventory, Material material) {
		int count = 0;
		for (ItemStack itemStack : inventory.getContents())
			if (itemStack != null && itemStack.getType().equals(material))
				count += itemStack.getAmount();
		return count;
	}

	protected Enchantment enchantFromString(String str) {
		for (Enchantment enchantment : Enchantment.values())
			if (enchantment.getName().equalsIgnoreCase(str))
				return enchantment;
		return null;
	}

	/**
	 * 
	 * @param direction
	 * @return
	 */
	protected BlockFace getClosestFace(float direction) {

		direction = direction % 360;

		if (direction < 0)
			direction += 360;

		direction = Math.round(direction / 45);

		switch ((int) direction) {
		case 0:
			return BlockFace.WEST;
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
	 * 
	 * @param price
	 * @return
	 */
	protected String betterPrice(long price) {
		String betterPrice = "";
		String[] splitPrice = String.valueOf(price).split("");
		int current = 0;
		for (int a = splitPrice.length - 1; a > -1; a--) {
			current++;
			if (current > 3) {
				betterPrice += ".";
				current = 1;
			}
			betterPrice += splitPrice[a];
		}
		StringBuilder builder = new StringBuilder().append(betterPrice);
		builder.reverse();
		return builder.toString();
	}

	/**
	 * 
	 * @param enchantment
	 * @param itemStack
	 * @return
	 */
	protected boolean hasEnchant(Enchantment enchantment, ItemStack itemStack) {
		return itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()
				&& itemStack.getItemMeta().hasEnchant(enchantment);
	}

	/**
	 * 
	 * @param player
	 * @param cooldown
	 * @return
	 */
	protected String timerFormat(Player player, String cooldown) {
		return TimerBuilder.getStringTime(CooldownBuilder.getCooldownPlayer(cooldown, player) / 1000);
	}

	/**
	 * 
	 * @param player
	 * @param cooldown
	 * @return
	 */
	protected boolean isCooldown(Player player, String cooldown) {
		return isCooldown(player, cooldown, 0);
	}

	/**
	 * 
	 * @param player
	 * @param cooldown
	 * @param timer
	 * @return
	 */
	protected boolean isCooldown(Player player, String cooldown, int timer) {
		if (CooldownBuilder.isCooldown(cooldown, player)) {
			ActionBar.sendActionBar(player,
					String.format("§cVous devez attendre encore §6%s §cavant de pouvoir faire cette action.",
							timerFormat(player, cooldown)));
			return true;
		}
		if (timer > 0)
			CooldownBuilder.addCooldown(cooldown, player, timer);
		return false;
	}

	/**
	 * @param list
	 * @return
	 */
	protected String toList(Stream<String> list) {
		return toList(list.collect(Collectors.toList()), "§e", "§6");
	}

	/**
	 * @param list
	 * @return
	 */
	protected String toList(List<String> list) {
		return toList(list, "§e", "§6§n");
	}

	/**
	 * @param list
	 * @param color
	 * @param color2
	 * @return
	 */
	protected String toList(List<String> list, String color, String color2) {
		if (list == null || list.size() == 0)
			return null;
		if (list.size() == 1)
			return list.get(0);
		String str = "";
		for (int a = 0; a != list.size(); a++) {
			if (a == list.size() - 1 && a != 0)
				str += color + " et " + color2;
			else if (a != 0)
				str += color + ", " + color2;
			str += list.get(a);
		}
		return str;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected String removeColor(String message) {
		for (ChatColor color : ChatColor.values())
			message = message.replace("§" + color.getChar(), "").replace("&" + color.getChar(), "");
		return message;
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	protected String format(long l) {
		return format(l, ' ');
	}

	/**
	 * 
	 * @param l
	 * @param c
	 * @return
	 */
	protected String format(long l, char c) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(c);
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(l);
	}

	/**
	 * Permet d'obtenir la tÃªte d'un joueur en utilisation le systÃ¨me de
	 * configuration des inventaires
	 * 
	 * @param itemStack
	 * @param player
	 * @return itemstack
	 */
	public ItemStack playerHead(ItemStack itemStack, OfflinePlayer player) {
		String name = itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()
				? itemStack.getItemMeta().getDisplayName() : null;
		if (NMSUtils.isNewVersion()) {
			if (itemStack.getType().equals(Material.PLAYER_HEAD) && name != null && name.startsWith("HEAD")) {
				SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
				name = name.replace("HEAD", "");
				if (name.length() == 0)
					meta.setDisplayName(null);
				else
					meta.setDisplayName(name);
				meta.setOwningPlayer(player);
				itemStack.setItemMeta(meta);
			}
		} else {
			if (itemStack.getType().equals(getMaterial(397)) && itemStack.getData().getData() == 3 && name != null
					&& name.startsWith("HEAD")) {
				SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
				name = name.replace("HEAD", "");
				if (name.length() == 0)
					meta.setDisplayName(null);
				else
					meta.setDisplayName(name);
				meta.setOwner(player.getName());
				itemStack.setItemMeta(meta);
			}
		}
		return itemStack;
	}

	/**
	 * Allows you to get an itemstack to create a player's head
	 *
	 * @return itemstack
	 */
	protected ItemStack playerHead() {
		return NMSUtils.isNewVersion() ? new ItemStack(Material.PLAYER_HEAD)
				: new ItemStack(getMaterial(397), 1, (byte) 3);
	}

	/**
	 * Allows to obtain a class according to the provider
	 * 
	 * @param plugin
	 * @param classz
	 * @return T
	 */
	protected <T> T getProvider(Plugin plugin, Class<T> classz) {
		RegisteredServiceProvider<T> provider = plugin.getServer().getServicesManager().getRegistration(classz);
		if (provider == null)
			return null;
		return provider.getProvider() != null ? (T) provider.getProvider() : null;
	}

	/**
	 * 
	 * @param configuration
	 * @return
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
	 * Allows to execute a runnable in an asynmetrical way
	 *
	 * @param runnable
	 */
	protected void runAsync(Plugin plugin, Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
	}

	/**
	 * Turns back time for a human
	 *
	 * @param second
	 * @return string
	 */
	protected String getStringTime(long second) {
		return TimerBuilder.getStringTime(second);
	}

	/**
	 * Allows you to create a head from a URL
	 *
	 * @param url
	 * @return itemstack
	 */
	protected ItemStack createSkull(String url) {

		ItemStack head = playerHead();
		if (url.isEmpty())
			return head;

		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

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
	 * Allows to check if an itemstack and a head
	 *
	 * @param itemStack
	 * @return boolean
	 */
	protected boolean isPlayerHead(ItemStack itemStack) {
		Material material = itemStack.getType();
		if (NMSUtils.isNewVersion())
			return material.equals(Material.PLAYER_HEAD);
		return (material.equals(getMaterial(397))) && (itemStack.getDurability() == 3);
	}

	/**
	 * 
	 * @param object
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected Object getPrivateField(Object object, String field)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		Field objectField = field.equals("commandMap") ? clazz.getDeclaredField(field)
				: field.equals("knownCommands") ? NMSUtils.isNewVersion()
						? clazz.getSuperclass().getDeclaredField(field) : clazz.getDeclaredField(field) : null;
		objectField.setAccessible(true);
		Object result = objectField.get(object);
		objectField.setAccessible(false);
		return result;
	}

	/**
	 * Unregister a bukkit command
	 * 
	 * @param plugin
	 * @param command
	 */
	protected void unRegisterBukkitCommand(Plugin plugin, PluginCommand command) {
		try {
			Object result = getPrivateField(plugin.getServer().getPluginManager(), "commandMap");
			SimpleCommandMap commandMap = (SimpleCommandMap) result;

			Object map = getPrivateField(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
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
	 * Allows to make an itemstack shine
	 * 
	 * @param itemStack
	 */
	public void glow(ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		if (NMSUtils.getNMSVersion() != 1.7)
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		itemStack.setItemMeta(itemMeta);
	}

	/**
	 * Allows you to clear a player's inventory, remove potion effects and put
	 * him on life support
	 * 
	 * @param player
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
	 * Create a progress bar
	 * https://www.spigotmc.org/threads/progress-bars-and-percentages.276020/
	 * 
	 * @param current
	 * @param max
	 * @param totalBars
	 * @param symbol
	 * @param completedColor
	 * @param notCompletedColor
	 * @return string
	 */
	public String getProgressBar(int current, int max, int totalBars, char symbol, String completedColor,
			String notCompletedColor) {
		float percent = (float) current / max;
		int progressBars = (int) (totalBars * percent);

		return Strings.repeat(completedColor + symbol, progressBars)
				+ Strings.repeat(notCompletedColor + symbol, totalBars - progressBars);
	}

	/**
	 * Create a progress bar
	 * 
	 * @param current
	 * @param max
	 * @param progressBar
	 * @return string
	 */
	public String getProgressBar(int current, int max, ProgressBar progressBar) {
		return this.getProgressBar(current, max, progressBar.getLenght(), progressBar.getSymbol(),
				progressBar.getCompletedColor(), progressBar.getNotCompletedColor());
	}

	/**
	 * Allows you to check if an inventory will contain armor or items
	 * 
	 * @param player
	 * @return boolean
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

		for (ItemStack itemStack1 : player.getInventory().getContents()) {
			if (itemStack1 != null) {
				return true;
			}
		}

		return false;
	}

}
