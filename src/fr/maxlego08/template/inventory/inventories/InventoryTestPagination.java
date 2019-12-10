package fr.maxlego08.template.inventory.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.inventory.ItemButton;
import fr.maxlego08.template.inventory.PaginateInventory;
import fr.maxlego08.template.inventory.VInventory;
import fr.maxlego08.template.zcore.utils.builder.ItemBuilder;

public class InventoryTestPagination extends PaginateInventory<String> {

	public InventoryTestPagination(String inventoryName, int inventorySize) {
		super(inventoryName, inventorySize);
	}

	@Override
	public List<String> preOpenInventory() {
		System.out.println("pre open");
		
		List<String> tmpList = new ArrayList<>();
		
		for(int a = 0; a != 157; a++)
			tmpList.add(UUID.randomUUID().toString());
		
		return tmpList;
	}

	@Override
	public ItemStack buildItem(String t) {
		return ItemBuilder.getCreatedItem(Material.DIAMOND, 1, t);
	}

	@Override
	public void onClick(String t, ItemButton button) {
	}

	@Override
	public void postOpenInventory() {
		System.out.println("post open");
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryTestPagination(inventoryName, inventorySize);
	}

}
