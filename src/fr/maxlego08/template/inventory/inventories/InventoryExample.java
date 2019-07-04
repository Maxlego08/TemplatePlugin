package fr.maxlego08.template.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.inventory.VInventory;

public class InventoryExample extends VInventory {
	
	@Override
	protected boolean openMenu(Template plguin, Player player, int page, Object... args) {

		Inventory inv = createInventory("§aExample", 9);
		
		openInventory(inv);
		
		return true;
	}

	@Override
	protected void onClick(InventoryClickEvent event, Template plguin, Player player) { }

	@Override
	protected void onClose(InventoryCloseEvent event, Template plguin, Player player) { }

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) { }

}
