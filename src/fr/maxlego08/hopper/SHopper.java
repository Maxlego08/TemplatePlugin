package fr.maxlego08.hopper;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

public class SHopper {

	private final Location location;

	/**
	 * @param location
	 */
	public SHopper(Location location) {
		super();
		this.location = location;
	}

	public Location getLocation() {
		return this.location;
	}

	public Chunk getChunk() {
		return this.location.getChunk();
	}

	public boolean isLoad() {
		Chunk chunk = this.getChunk();
		return chunk.isLoaded();
	}

	public boolean isValid() {
		return this.location != null && this.location.getWorld() != null
				&& this.location.getBlock().getType().equals(Material.HOPPER);
	}

	public void run(List<Material> materials) {

		Chunk chunk = this.getChunk();
		World world = chunk.getWorld();

		BoundingBox boundingBox = BoundingBox.of(chunk.getBlock(0, 0, 0), chunk.getBlock(15, 255, 15));

		List<Item> items = world.getNearbyEntities(boundingBox, e -> e.getType().equals(EntityType.DROPPED_ITEM))
				.stream().map(e -> (Item) e).filter(e -> materials.contains(e.getItemStack().getType()))
				.collect(Collectors.toList());

		items.forEach(this::addItem);

	}

	/**
	 * 
	 * @param item
	 */
	private void addItem(Item item) {

		Block block = this.location.getBlock();

		Hopper hopper = (Hopper) block.getState();
		Inventory inventory = hopper.getInventory();
		int inventorySize = 5;
		ItemStack itemStack = item.getItemStack();

		for (int a = 0; a != inventorySize; a++) {

			if (itemStack.getAmount() <= 0) {
				return;
			}

			ItemStack currentItem = inventory.getItem(a);

			if (currentItem == null) {

				int newAmount = Math.min(itemStack.getMaxStackSize(), itemStack.getAmount());

				ItemStack newItemStack = itemStack.clone();
				newItemStack.setAmount(newAmount);
				itemStack.setAmount(itemStack.getAmount() - newAmount);

				inventory.addItem(newItemStack);

			} else if (itemStack.isSimilar(currentItem) && currentItem.getAmount() < currentItem.getMaxStackSize()) {

				int freeAmount = currentItem.getMaxStackSize() - currentItem.getAmount();
				int newAmount = Math.min(freeAmount, itemStack.getAmount());

				currentItem.setAmount(currentItem.getAmount() + newAmount);
				itemStack.setAmount(itemStack.getAmount() - newAmount);

			}

			if (itemStack.getAmount() <= 0) {
				item.remove();
				return;
			}

		}

	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	protected int getAmount(Item item) {
		if (Bukkit.getPluginManager().isPluginEnabled("WildStacker"))
			return WildStackerAPI.getItemAmount(item);
		else
			return item.getItemStack().getAmount();
	}

	protected void setAmount(Item item, int toRemove) {
		if (Bukkit.getPluginManager().isPluginEnabled("WildStacker")) {
			WildStackerAPI.getStackedItem(item).setStackAmount(toRemove, true);
		} else
			item.getItemStack().setAmount(toRemove);
	}

}
