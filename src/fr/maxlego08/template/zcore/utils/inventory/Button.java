package fr.maxlego08.template.zcore.utils.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import fr.maxlego08.template.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public class Button extends ZUtils {

	private final int slot;
	private final String name;
	private final MaterialData item;
	private final List<String> lore;

	/**
	 * 
	 * @param slot
	 * @param name
	 * @param material
	 * @param data
	 * @param lore
	 */
	public Button(int slot, String name, Material material, int data, List<String> lore) {
		super();
		this.slot = slot;
		this.name = name;
		this.item = new MaterialData(material, (byte) data);
		this.lore = lore;
	}

	/**
	 * 
	 * @param slot
	 * @param name
	 * @param item
	 */
	public Button(int slot, String name, Material item) {
		this(slot, name, item, 0);
	}

	/**
	 * 
	 * @param slot
	 * @param name
	 * @param item
	 * @param lore
	 */
	public Button(int slot, String name, Material item, String... lore) {
		this(slot, name, item, 0, Arrays.asList(lore));
	}

	/**
	 * 
	 * @param slot
	 * @param name
	 * @param item
	 * @param data
	 * @param lore
	 */
	public Button(int slot, String name, Material item, int data, String... lore) {
		this(slot, name, item, data, Arrays.asList(lore));
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the item
	 */
	public MaterialData getItem() {
		return item;
	}

	/**
	 * @return the lore
	 */
	public List<String> getLore() {
		return lore;
	}

}
