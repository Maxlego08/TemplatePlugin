package fr.maxlego08.hopper.zcore.utils.inventory;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;

public class ItemButton {

	private final ItemStack displayItem;
	private Consumer<InventoryClickEvent> onClick;
	private Consumer<InventoryClickEvent> onMiddleClick;
	private Consumer<InventoryClickEvent> onLeftClick;
	private Consumer<InventoryClickEvent> onRightClick;

	public ItemButton(ItemStack displayItem) {
		super();
		this.displayItem = displayItem;
	}

	public ItemButton(Material material, int id, String name, String... lore) {
		this(new ItemBuilder(material, id, 1, name, Arrays.asList(lore), null, null).build());
	}

	public ItemButton(Material material, String name, String... lore) {
		this(new ItemBuilder(material, name).setLore(lore).build());
	}

	public ItemButton(Material material) {
		this(new ItemBuilder(material).build());
	}

	public ItemButton(Material material, String name) {
		this(new ItemBuilder(material, name).build());
	}

	public ItemButton setClick(Consumer<InventoryClickEvent> onClick) {
		this.onClick = onClick;
		return this;
	}

	public ItemButton setMiddleClick(Consumer<InventoryClickEvent> onMiddleClick) {
		this.onMiddleClick = onMiddleClick;
		return this;
	}

	/**
	 * @param onLeftClick
	 *            the onLeftClick to set
	 */
	public ItemButton setLeftClick(Consumer<InventoryClickEvent> onLeftClick) {
		this.onLeftClick = onLeftClick;
		return this;
	}

	/**
	 * @param onRightClick
	 *            the onRightClick to set
	 */
	public ItemButton setRightClick(Consumer<InventoryClickEvent> onRightClick) {
		this.onRightClick = onRightClick;
		return this;
	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	/**
	 * Permet de gérer le click du joueur
	 * @param event
	 */
	public void onClick(InventoryClickEvent event) {
		if (onClick != null)
			onClick.accept(event);
		if (event.getClick().equals(ClickType.MIDDLE) && onMiddleClick != null)
			onMiddleClick.accept(event);
		else if (event.getClick().equals(ClickType.RIGHT) && onRightClick != null)
			onRightClick.accept(event);
		else if (event.getClick().equals(ClickType.LEFT) && onLeftClick != null)
			onLeftClick.accept(event);
	}

}
