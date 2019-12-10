package fr.maxlego08.template.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.template.exceptions.InventoryAlreadyExistException;
import fr.maxlego08.template.inventory.inventories.InventoryTestPagination;
import fr.maxlego08.template.listener.ListenerAdapter;
import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;

public class InventoryManager extends ListenerAdapter {

	private Map<Integer, VInventory> inventories = new HashMap<>();
	private Map<Player, VInventory> playerInventories = new HashMap<>();

	private InventoryManager() {

		try {
			addInventory(1, new InventoryTestPagination("§dTest §6%p%§5/§3%mp%", 54));
		} catch (InventoryAlreadyExistException e) {
			e.printStackTrace();
		}

		plugin.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
	}

	private void addInventory(int id, VInventory inventory) throws InventoryAlreadyExistException {
		if (!inventories.containsKey(id))
			inventories.put(id, inventory);
		else
			throw new InventoryAlreadyExistException("Inventory with id " + id + " already exist !");
	}

	public void createInventory(int id, Player player, int page, Object... objects) {
		VInventory inventory = getInventory(id);
		if (inventory == null)
			return;
		VInventory clonedInventory = inventory.clone();

		if (clonedInventory == null) {
			message(player, Message.INVENTORY_CLONE_NULL);
			return;
		}

		clonedInventory.setId(id);
		try {
			InventoryResult result = clonedInventory.preOpenInventory(plugin, player, page, objects);
			if (result.equals(InventoryResult.SUCCESS)) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			} else if (result.equals(InventoryResult.ERROR))
				message(player, Message.INVENTORY_OPEN_ERROR, id);
		} catch (Exception e) {
			message(player, Message.INVENTORY_OPEN_ERROR, id);
			e.printStackTrace();
		}
	}

	public void createInventory(VInventory parent, Player player) {
		createInventory(parent.getId(), player, parent.getPage(), parent.getObjets());
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event, Player player) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			VInventory gui = playerInventories.get(player);
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}
			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {
				event.setCancelled(true);
				ItemButton button = gui.getItems().getOrDefault(event.getSlot(), null);
				if (button != null)
					button.onClick(event);
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent event, Player player) {
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player);
		remove(player);
		inventory.onClose(event, plugin, player);
	}

	@Override
	protected void onInventoryDrag(InventoryDragEvent event, Player player) {
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			playerInventories.get(player).onDrag(event, plugin, player);
		}
	}

	public boolean exist(Player player) {
		return playerInventories.containsKey(player);
	}

	public void remove(Player player) {
		if (playerInventories.containsKey(player))
			playerInventories.remove(player);
	}

	private VInventory getInventory(int id) {
		return inventories.getOrDefault(id, null);
	}

	/**
	 * @param id
	 */
	public void updateAllPlayer(int... id) {
		for (int currentId : id)
			updateAllPlayer(currentId);
	}

	/**
	 * @param id
	 */
	public void closeAllPlayer(int... id) {
		for (int currentId : id)
			closeAllPlayer(currentId);
	}

	/**
	 * @param id
	 */
	private void updateAllPlayer(int id) {
		Iterator<VInventory> iterator = this.playerInventories.values().stream().filter(inv -> inv.getId() == id)
				.collect(Collectors.toList()).iterator();
		while (iterator.hasNext()) {
			VInventory inventory = iterator.next();
			Bukkit.getScheduler().runTask(ZPlugin.z(), () -> createInventory(inventory, inventory.getPlayer()));
		}
	}

	/**
	 * @param id
	 */
	private void closeAllPlayer(int id) {
		Iterator<VInventory> iterator = this.playerInventories.values().stream().filter(inv -> inv.getId() == id)
				.collect(Collectors.toList()).iterator();
		while (iterator.hasNext()) {
			VInventory inventory = iterator.next();
			inventory.getPlayer().closeInventory();
		}
	}

	/**
	 * static Singleton instance.
	 */
	private static volatile InventoryManager instance;

	/**
	 * Return a singleton instance of InventoryManager.
	 */
	public static InventoryManager getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (InventoryManager.class) {
				if (instance == null) {
					instance = new InventoryManager();
				}
			}
		}
		return instance;
	}

}
