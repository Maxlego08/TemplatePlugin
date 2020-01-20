package fr.maxlego08.template.inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.template.zcore.utils.inventory.Pagination;

public abstract class PaginateInventory<T> extends VInventory {

	private List<T> collections;
	protected final String inventoryName;
	protected final int inventorySize;
	private int paginationSize = 45;
	private int nextSlot = 50;
	private int previousSlot = 48;
	private int defaultSlot = 0;
	private boolean isReverse = false;

	public PaginateInventory(String inventoryName, int inventorySize) {
		super();
		this.inventoryName = inventoryName;
		this.inventorySize = inventorySize;
	}

	@Override
	public InventoryResult openInventory(Template main, Player player, int page, Object... args) throws Exception {

		preOpenInventory();
		
		super.createInventory(inventoryName.replace("%mp%", String.valueOf(getMaxPage(collections))).replace("%p%",
				String.valueOf(page)), inventorySize);

		Pagination<T> pagination = new Pagination<>();
		AtomicInteger slot = new AtomicInteger(defaultSlot);

		List<T> tmpList = isReverse ? pagination.paginateReverse(collections, paginationSize, page)
				: pagination.paginate(collections, paginationSize, page);

		tmpList.forEach(tmpItem -> {
			ItemButton button = addItem(slot.getAndIncrement(), buildItem(tmpItem));
			button.setClick((event) -> onClick(tmpItem, button));
		});

		if (getPage() != 1)
			addItem(previousSlot, ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page précédente"))
					.setClick(event -> createInventory(player, getId(), getPage() - 1, args));
		if (getPage() != getMaxPage(collections))
			addItem(nextSlot, ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page suivante"))
					.setClick(event -> createInventory(player, getId(), getPage() + 1, args));

		postOpenInventory();
		
		return InventoryResult.SUCCESS;
	}

	protected void setCollections(List<T> collections) {
		this.collections = collections;
	}

	protected void setPaginationSize(int paginationSize) {
		this.paginationSize = paginationSize;
	}

	protected void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	protected void setNextSlot(int nextSlot) {
		this.nextSlot = nextSlot;
	}
	
	protected void setPreviousSlot(int previousSlot) {
		this.previousSlot = previousSlot;
	}
	
	protected void setDefaultSlot(int defaultSlot) {
		this.defaultSlot = defaultSlot;
	}
	
	public abstract ItemStack buildItem(T object);
	
	public abstract void onClick(T object, ItemButton button);
	
	public abstract List<T> preOpenInventory();
	
	public abstract void postOpenInventory();
}
