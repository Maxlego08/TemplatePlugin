package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.utils.NMS_1_17;
import fr.maxlego08.hopper.zcore.utils.loader.ItemStackLoader;
import fr.maxlego08.hopper.zcore.utils.loader.Loader;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;
import fr.maxlego08.hopper.zcore.utils.yaml.YamlUtils;

public class HopperManager extends YamlUtils implements Saveable {

	private final HopperPlugin plugin;

	private final String NMS_KEY = "fshopper";
	private final NMS_1_17 nms_1_17 = new NMS_1_17();

	private ItemStack itemStack;
	private List<Material> materials = new ArrayList<>();

	/**
	 * @param plugin
	 */
	public HopperManager(HopperPlugin plugin) {
		super(plugin);
		this.plugin = plugin;

	}

	@Override
	public void save(Persist persist) {

	}

	@Override
	public void load(Persist persist) {
		Loader<ItemStack> loader = new ItemStackLoader();
		this.itemStack = loader.load((YamlConfiguration) this.getConfig(), "item.");
		this.materials = this.getConfig().getStringList("drops").stream().map(e -> Material.valueOf(e))
				.collect(Collectors.toList());

		Bukkit.getScheduler().runTaskTimer(this.plugin, () -> this.hopperTaks(), Config.hopperDelay, Config.hopperDelay);
	}

	/**
	 * Permet de reload les fichiers de config
	 * 
	 * @param sender
	 */
	public void reload(CommandSender sender) {

		this.plugin.getSavers().forEach(e -> e.load(this.plugin.getPersist()));
		message(sender, Message.RELOAD);

	}

	/**
	 * Permet de donner un hopper à un joueur
	 * 
	 * @param sender
	 * @param player
	 * @param count
	 */
	public void give(CommandSender sender, Player player, int count) {

		give(player, this.createItemStack(player, count));

		message(sender, Message.GIVE_PLAYER);
		message(sender, Message.GIVE_SENDER, "%player%", player.getName());

	}

	/**
	 * 
	 * @param player
	 * @param count
	 * @return
	 */
	private ItemStack createItemStack(Player player, int count) {
		ItemStack itemStack = this.papi(this.itemStack.clone(), player);
		itemStack.setAmount(count);

		itemStack = this.nms_1_17.set(itemStack, this.NMS_KEY, true);
		return itemStack;
	}

	/**
	 * 
	 * @param player
	 * @param event
	 * @param block
	 */
	public void blockPlace(Player player, BlockPlaceEvent event, Block block) {

		ItemStack itemStack = player.getInventory().getItemInMainHand();
		ItemStack itemStack2 = player.getInventory().getItemInOffHand();
		if (this.nms_1_17.has(itemStack, this.NMS_KEY) || this.nms_1_17.has(itemStack2, this.NMS_KEY)) {

			HopperBoard board = this.plugin.getHopperBoard();
			SHopper hopper = new SHopper(block.getLocation());
			board.createHopper(hopper);

			message(player, Message.ACTION_PLACE);

		}

	}

	/**
	 * 
	 * @param player
	 * @param event
	 * @param block
	 */
	public void blockBreak(Player player, BlockBreakEvent event, Block block) {

		HopperBoard board = this.plugin.getHopperBoard();
		Optional<SHopper> optional = board.getHopper(block.getLocation());
		if (optional.isPresent()) {

			board.remove(optional.get());
			event.setDropItems(false);

			ItemStack itemStack = this.createItemStack(player, 1);
			give(player, itemStack);

			message(player, Message.ACTION_BREAK);
		}

	}

	/**
	 * Lancement de la trask
	 */
	public void hopperTaks() {

		HopperBoard board = this.plugin.getHopperBoard();
		Collection<SHopper> collection = board.getActiveHoppers();
		Iterator<SHopper> iterator = collection.iterator();
		while (iterator.hasNext()) {
			SHopper hopper = iterator.next();

			if (hopper.isValid()) {
				hopper.run(this.materials);
			} else {
				board.remove(hopper);
			}

		}
	}

	/**
	 * Quand une tnt explose
	 * 
	 * @param event
	 * @param blockList
	 */
	public void explode(EntityExplodeEvent event, List<Block> blockList) {

		HopperBoard board = this.plugin.getHopperBoard();

		Iterator<Block> iterator = blockList.iterator();
		while (iterator.hasNext()) {
			Block block = iterator.next();
			Optional<SHopper> optional = board.getHopper(block.getLocation());
			if (optional.isPresent()) {

				iterator.remove();

				ItemStack itemStack = this.itemStack.clone();
				itemStack.setAmount(1);
				itemStack = this.nms_1_17.set(itemStack, this.NMS_KEY, true);

				World world = block.getWorld();
				world.dropItem(block.getLocation(), itemStack);

				block.setType(Material.AIR);
			}
		}
	}

	/**
	 * 
	 * @param sender
	 */
	public void info(CommandSender sender) {

		HopperBoard board = this.plugin.getHopperBoard();
		message(sender, Message.INFORMATIONS, "%hopperCount%", board.countHoppers(), "%hopperActiveCount%",
				board.getActiveHoppers().size());

	}

}
