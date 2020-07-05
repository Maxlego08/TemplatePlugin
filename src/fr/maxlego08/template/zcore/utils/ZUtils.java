package fr.maxlego08.template.zcore.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.enums.Inventory;
import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.builder.CooldownBuilder;
import fr.maxlego08.template.zcore.utils.builder.TimerBuilder;
import fr.maxlego08.template.zcore.utils.players.ActionBar;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_15_R1.PacketPlayOutTitle.EnumTitleAction;

@SuppressWarnings("deprecation")
public abstract class ZUtils extends MessageUtils{

	private static transient List<String> teleportPlayers = new ArrayList<String>();
	protected transient Template plugin = (Template) ZPlugin.z();

	/**
	 * @param location
	 *            as String
	 * @return string as location
	 */
	protected Location changeStringLocationToLocation(String s) {
		String[] a = s.split(",");
		if (a.length == 6)
			return changeStringLocationToLocationEye(s);
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		return new Location(w, x, y, z);
	}

	/**
	 * @param location
	 *            as string
	 * @return string as locaiton
	 */
	protected Location changeStringLocationToLocationEye(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		if (a.length == 6) {
			float yaw = Float.parseFloat(a[4]);
			float pitch = Float.parseFloat(a[5]);
			return new Location(w, x, y, z, yaw, pitch);
		}
		return new Location(w, x, y, z);
	}

	/**
	 * @param location
	 * @return location as string
	 */
	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * @param location
	 * @return location as String
	 */
	protected String changeLocationToStringEye(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ() + "," + location.getYaw() + "," + location.getPitch();
		return ret;
	}

	/**
	 * @param chunk
	 * @return string as Chunk
	 */
	protected Chunk changeStringChuncToChunk(String chunk) {
		String[] a = chunk.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		return w.getChunkAt(Integer.valueOf(a[1]), Integer.valueOf(a[2]));
	}

	/**
	 * @param chunk
	 * @return chunk as string
	 */
	protected String changeChunkToString(Chunk chunk) {
		String c = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
		return c;
	}

	/**
	 * @param {@link
	 * 			Cuboid}
	 * @return cuboid as string
	 */
	protected String changeCuboidToString(Cuboid cuboid) {
		return cuboid.getWorld().getName() + "," + cuboid.getLowerX() + "," + cuboid.getLowerY() + ","
				+ cuboid.getLowerZ() + "," + ";" + cuboid.getWorld().getName() + "," + cuboid.getUpperX() + ","
				+ cuboid.getUpperY() + "," + cuboid.getUpperZ();
	}

	/**
	 * @param str
	 * @return {@link Cuboid}
	 */
	protected Cuboid changeStringToCuboid(String str) {

		String parsedCuboid[] = str.split(";");
		String parsedFirstLoc[] = parsedCuboid[0].split(",");
		String parsedSecondLoc[] = parsedCuboid[1].split(",");

		String firstWorldName = parsedFirstLoc[0];
		double firstX = Double.valueOf(parsedFirstLoc[1]);
		double firstY = Double.valueOf(parsedFirstLoc[2]);
		double firstZ = Double.valueOf(parsedFirstLoc[3]);

		String secondWorldName = parsedSecondLoc[0];
		double secondX = Double.valueOf(parsedSecondLoc[1]);
		double secondY = Double.valueOf(parsedSecondLoc[2]);
		double secondZ = Double.valueOf(parsedSecondLoc[3]);

		Location l1 = new Location(Bukkit.getWorld(firstWorldName), firstX, firstY, firstZ);

		Location l2 = new Location(Bukkit.getWorld(secondWorldName), secondX, secondY, secondZ);

		return new Cuboid(l1, l2);

	}

	/**
	 * @param item
	 * @return the encoded item
	 */
	protected String encode(ItemStack item) {
		return ItemDecoder.serializeItemStack(item);
	}

	/**
	 * @param item
	 * @return the decoded item
	 */
	protected ItemStack decode(String item) {
		return ItemDecoder.deserializeItemStack(item);
	}

	/**
	 * @param material
	 * @return he name of the material with a better format
	 */
	protected String betterMaterial(Material material) {
		return TextUtil.getMaterialLowerAndMajAndSpace(material);
	}

	/**
	 * @param a
	 * @param b
	 * @return number between a and b
	 */
	protected int getNumberBetween(int a, int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}

	/**
	 * @param player
	 * @return true if the player's inventory is full
	 */
	protected boolean hasInventoryFull(Player player) {
		int slot = 0;
		ItemStack[] arrayOfItemStack;
		int x = (arrayOfItemStack = player.getInventory().getContents()).length;
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents == null))
				slot++;
		}
		return slot == 0;
	}

	protected boolean give(ItemStack item, Player player) {
		if (hasInventoryFull(player))
			return false;
		player.getInventory().addItem(item);
		return true;
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

	private static transient Material[] byId;

	static {
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

	/**
	 * @param id
	 * @return the material according to his id
	 */
	protected Material getMaterial(int id) {
		return byId.length > id && id >= 0 ? byId[id] : null;
	}

	/**
	 * Check if the item name is the same as the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name is the same as string
	 */
	protected boolean same(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().equals(name);
	}

	/**
	 * Check if the item name contains the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name contains the string
	 */
	protected boolean contains(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().contains(name);
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
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
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

	private transient Economy economy = ZPlugin.z().getEconomy();

	/**
	 * Player bank
	 * 
	 * @param player
	 * @return player bank
	 */
	protected double getBalance(Player player) {
		return economy.getBalance(player);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param int
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, int value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param float
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, float value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param long
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, long value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param double
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, double value) {
		return getBalance(player) >= value;
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void depositMoney(Player player, double value) {
		economy.depositPlayer(player, value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void depositMoney(Player player, long value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void depositMoney(Player player, int value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void depositMoney(Player player, float value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void withdrawMoney(Player player, double value) {
		economy.withdrawPlayer(player, value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void withdrawMoney(Player player, long value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void withdrawMoney(Player player, int value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void withdrawMoney(Player player, float value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * 
	 * @return {@link Economy}
	 */
	protected Economy getEconomy() {
		return economy;
	}

	/**
	 * 
	 * @param player
	 * @param item
	 * @param itemStack
	 */
	protected void removeItems(Player player, int item, ItemStack itemStack) {
		for (ItemStack is : player.getInventory().getContents()) {
			if (is != null && is.isSimilar(itemStack)) {
				int currentAmount = is.getAmount() - item;
				item -= is.getAmount();
				if (currentAmount <= 0)
					player.getInventory().removeItem(is);
				else
					is.setAmount(currentAmount);
			}
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
		return TextUtil.name(string);
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
	 * 
	 * @param delay
	 * @param count
	 * @param runnable
	 */
	protected void schedule(long delay, int count, Runnable runnable) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int tmpCount = 0;

			@Override
			public void run() {

				if (!ZPlugin.z().isEnabled()) {
					cancel();
					return;
				}

				if (tmpCount > count) {
					cancel();
					return;
				}

				tmpCount++;
				Bukkit.getScheduler().runTask(ZPlugin.z(), runnable);

			}
		}, 0, delay);
	}

	/**
	 * 
	 * @param player
	 * @param inventoryId
	 */
	protected void createInventory(Player player, Inventory inventory) {
		createInventory(player, inventory, 1);
	}

	/**
	 * 
	 * @param player
	 * @param inventoryId
	 * @param page
	 */
	protected void createInventory(Player player, Inventory inventory, int page) {
		createInventory(player, inventory, page, new Object() {
		});
	}

	/**
	 * 
	 * @param player
	 * @param inventoryId
	 * @param page
	 * @param objects
	 */
	protected void createInventory(Player player, Inventory inventory, int page, Object... objects) {
		plugin.getInventoryManager().createInventory(inventory, player, page, objects);
	}

	/**
	 * 
	 * @param player
	 * @param inventory
	 * @param page
	 * @param objects
	 */
	protected void createInventory(Player player, int inventory, int page, Object... objects) {
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
	 * @param delay
	 * @param runnable
	 */
	protected void scheduleFix(long delay, BiConsumer<TimerTask, Boolean> runnable) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!ZPlugin.z().isEnabled()) {
					cancel();
					runnable.accept(this, false);
					return;
				}
				Bukkit.getScheduler().runTask(ZPlugin.z(), () -> runnable.accept(this, true));
			}
		}, delay, delay);
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	protected <T> T randomElement(List<T> element) {
		if (element.size() == 0)
			return null;
		if (element.size() == 1)
			return element.get(0);
		Random random = new Random();
		return element.get(random.nextInt(element.size() - 1));
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	protected String getItemName(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
			return item.getItemMeta().getDisplayName();
		String name = item.serialize().get("type").toString().replace("_", " ").toLowerCase();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected String color(String message) {
		return message.replace("&", "§");
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String colorReverse(String message) {
		return message.replace("§", "&");
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
	public List<String> colorReverse(List<String> messages) {
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
	 * Permet de générer un string
	 * 
	 * @param length
	 * @return
	 */
	protected String generateRandomString(int length) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
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
	 * Permet de retirer les items d'un inventaire en fonction d'un item stack
	 * et d'un nombre
	 * 
	 * @param inventory
	 * @param removeItemStack
	 * @param amount
	 */
	protected void removeItems(org.bukkit.inventory.Inventory inventory, ItemStack removeItemStack, int amount) {
		for (ItemStack itemStack : inventory.getContents()) {
			if (itemStack != null && itemStack.isSimilar(itemStack) && amount > 0) {
				int currentAmount = itemStack.getAmount() - amount;
				amount -= itemStack.getAmount();
				if (currentAmount <= 0)
					inventory.removeItem(itemStack);
				else
					itemStack.setAmount(currentAmount);
			}
		}
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
	 * Permet de conter le nombre d'item
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
	 * @param player
	 * @param title
	 * @param subtitle
	 * @param start
	 * @param time
	 * @param end
	 */
	public void sendTitle(Player player, String title, String subtitle, int start, int time, int end) {

		CraftPlayer craftPlayer = (CraftPlayer) player;

		PacketPlayOutTitle packetTimes = new PacketPlayOutTitle(start, time, end);
		craftPlayer.getHandle().playerConnection.sendPacket(packetTimes);

		if (title != null) {
			PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE,
					CraftChatMessage.fromString(title)[0], start, time, end);
			craftPlayer.getHandle().playerConnection.sendPacket(packetTitle);
		}

		if (subtitle != null) {
			PacketPlayOutTitle packetSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
					CraftChatMessage.fromString(subtitle)[0], start, time, end);
			craftPlayer.getHandle().playerConnection.sendPacket(packetSubtitle);
		}
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String removeColor(String message) {
		for (ChatColor color : ChatColor.values())
			message = message.replace("§" + color.getChar(), "").replace("&" + color.getChar(), "");
		return message;
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	public String format(long l) {
		return format(l, ' ');
	}

	/**
	 * 
	 * @param l
	 * @param c
	 * @return
	 */
	public String format(long l, char c) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(c);
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(l);
	}

}
