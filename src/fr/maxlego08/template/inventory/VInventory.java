package fr.maxlego08.template.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public abstract class VInventory extends ZUtils{

	private int menuId;
	private int clickedSlot;
	private ItemStack clickeditem;
	private String guiName;
	private Player player;
	private int page;
	private Object[] args;
	
	public VInventory() {
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	protected Inventory createInventory(String name) {
		return createInventory(name, 54);
	}

	protected Inventory createInventory(String name, int size) {
		guiName = name;

		if (name.length() > 32)
			Logger.getLogger().log("The name of the menu is over 32 characters!", LogType.ERROR);

		return Bukkit.createInventory(null, size, name);
	}

	public int getSlot() {
		return clickedSlot;
	}

	public ItemStack getItem() {
		return clickeditem;
	}

	public void setItem(ItemStack clickeditem) {
		this.clickeditem = clickeditem;
	}

	public void setSlot(int clickedSlot) {
		this.clickedSlot = clickedSlot;
	}

	public String getGuiName() {
		return guiName;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getMenuId() {
		return menuId;
	}

	protected void sendMessage(String message) {
		getPlayer().sendMessage(message);
	}

	protected void openInventory(Inventory inv) {
		getPlayer().openInventory(inv);
	}

	protected abstract boolean openMenu(Template plguin, Player player, int page, Object... args);

	protected abstract void onClick(InventoryClickEvent event, Template plguin, Player player);

	protected abstract void onClose(InventoryCloseEvent event, Template plugin, Player player);

	protected abstract void onDrag(InventoryDragEvent event, Template plugin, Player player);

	private static Material[] byId;

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

	public Material getMaterial(int id) {
		return byId.length > id && id >= 0 ? byId[id] : null;
	}
	
}
