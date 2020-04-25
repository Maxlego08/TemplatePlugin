package fr.maxlego08.template.inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.exceptions.InventoryOpenException;
import fr.maxlego08.template.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.template.zcore.utils.inventory.InventorySize;
import fr.maxlego08.template.zcore.utils.inventory.ItemButton;
import fr.maxlego08.template.zcore.utils.inventory.Pagination;

public abstract class PaginateInventory<T> extends VInventory {

	protected List<T> collections;
	protected final String inventoryName;
	protected final int inventorySize;
	protected int paginationSize = 45;
	protected int nextSlot = 50;
	protected int previousSlot = 48;
	protected int defaultSlot = 0;
	protected boolean isReverse = false;
	protected boolean disableDefaultClick = false;
	protected Material previousMaterial = Material.ARROW;

	/**
	 * 
	 * @param inventoryName
	 * @param inventorySize
	 */
	public PaginateInventory(String inventoryName, int inventorySize) {
		super();
		this.inventoryName = inventoryName;
		this.inventorySize = inventorySize;
	}

	public PaginateInventory(String inventoryName, InventorySize inventorySize) {
		this.inventoryName = inventoryName;
		this.inventorySize = inventorySize.getSize();
		this.paginationSize = inventorySize.getPaginationSize();
		this.nextSlot = inventorySize.getNextSlot();
		this.previousSlot = inventorySize.getPreviousSlot();
		this.defaultSlot = inventorySize.getDefaultSlot();
	}

	@Override
	public InventoryResult openInventory(Template main, Player player, int page, Object... args)
			throws InventoryOpenException {

		if (defaultSlot > inventorySize || nextSlot > inventorySize || previousSlot > inventorySize
				|| paginationSize > inventorySize)
			throw new InventoryOpenException("Une erreur est survenue avec la gestion des slots !");

		collections = preOpenInventory();

		super.createInventory(inventoryName.replace("%mp%", String.valueOf(getMaxPage(collections))).replace("%p%",
				String.valueOf(page)), inventorySize);

		Pagination<T> pagination = new Pagination<>();
		AtomicInteger slot = new AtomicInteger(defaultSlot);

		List<T> tmpList = isReverse ? pagination.paginateReverse(collections, paginationSize, page)
				: pagination.paginate(collections, paginationSize, page);

		tmpList.forEach(tmpItem -> {
			ItemButton button = addItem(slotChange(slot.getAndIncrement()), buildItem(tmpItem));
			ItemButton itemButton = createButton(button);
			if (!disableClick)
				itemButton.setClick((event) -> onClick(tmpItem, itemButton));
		});

		if (getPage() != 1)
			addItem(previousSlot, Material.ARROW, "§f» §7Page précédente")
					.setClick(event -> createInventory(player, getId(), getPage() - 1, args));
		if (getPage() != getMaxPage(collections))
			addItem(nextSlot, Material.ARROW, "§f» §7Page suivante")
					.setClick(event -> createInventory(player, getId(), getPage() + 1, args));

		postOpenInventory();

		return InventoryResult.SUCCESS;
	}

	/**
	 * 
	 * @param slot
	 * @return
	 */
	protected int slotChange(int slot) {
		return slot;
	}

	/**
	 * 
	 * @param button
	 * @return
	 */
	protected ItemButton createButton(ItemButton button) {
		return button;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public abstract ItemStack buildItem(T object);

	/**
	 * 
	 * @param object
	 * @param button
	 */
	public abstract void onClick(T object, ItemButton button);

	/**
	 * 
	 * @return
	 */
	public abstract List<T> preOpenInventory();

	/**
	 * Called after create inventory
	 */
	public abstract void postOpenInventory();
}
