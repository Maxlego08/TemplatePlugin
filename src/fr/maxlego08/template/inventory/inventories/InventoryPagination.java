package fr.maxlego08.template.inventory.inventories;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import fr.maxlego08.template.PaginationExample;
import fr.maxlego08.template.Template;
import fr.maxlego08.template.inventory.VInventory;
import fr.maxlego08.template.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.template.zcore.utils.inventory.Pagination;

public class InventoryPagination extends VInventory {

	@Override
	protected boolean openMenu(Template plguin, Player player, int page, Object... args) {
		Inventory inventory = createInventory("§ePagination " + page +" / " + getMaxPage(plguin));
		AtomicInteger slot = new AtomicInteger(0);
		Pagination<PaginationExample> pagination = new Pagination<>();
		pagination.paginate(plguin.getExamples(), 45, page).forEach(p -> {
			inventory.setItem(slot.getAndIncrement(), ItemBuilder.getCreatedItem(Material.STONE, 1, "Test " + p.getId()));
		});
		if (page < getMaxPage(plguin))
			inventory.setItem(50, plguin.getNext().getInitButton());
		if (page != 1 && page <= getMaxPage(plguin))
			inventory.setItem(48, plguin.getPrevious().getInitButton());
		openInventory(inventory);
		return true;
	}

	public int getMaxPage(Template plguin){
		return (plguin.getExamples().size() / 45) + 1;
	}
	
	@Override
	protected void onClick(InventoryClickEvent event, Template plguin, Player player) {
		if (getItem().isSimilar(plguin.getNext().getInitButton()) && getSlot() == 50) {
			int newPage = getPage() + 1;
			plguin.getInventoryManager().createMenu(2, player, true, newPage);
		} else if (getItem().isSimilar(plguin.getPrevious().getInitButton()) && getSlot() == 48) {
			int newPage = getPage() - 1;
			plguin.getInventoryManager().createMenu(2, player, true, newPage);
		}
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {
		
	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {

	}

}
