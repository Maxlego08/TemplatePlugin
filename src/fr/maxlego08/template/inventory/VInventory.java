package fr.maxlego08.template.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.exceptions.InventoryOpenException;
import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.builder.ItemBuilder;

public abstract class VInventory extends ZUtils implements Cloneable{

	protected int id;
	protected Template plugin;
	protected Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
	protected Player player;
	protected int page;
	protected Object[] args;
	protected Inventory inventory;
	protected String guiName;
	protected boolean disableClick = true;

	/**
	 * Id de l'inventaire
	 * @param id
	 * @return
	 */
	public VInventory setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}

	/**
	 * Permet de créer l'inventaire
	 * @param name
	 * @return this
	 */
	protected VInventory createInventory(String name) {
		return createInventory(name, 54);
	}

	/**
	 * Permet de créer l'inventaire
	 * @param name
	 * @param size
	 * @return this
	 */
	protected VInventory createInventory(String name, int size) {
		guiName = name;
		this.inventory = Bukkit.createInventory(null, size, name);
		return this;
	}

	private void createDefaultInventory(){
		if (inventory == null)
			inventory = Bukkit.createInventory(null, 54, "§cDefault Inventory");
	}
	
	/**
	 * Ajout d'un item
	 * @param slot
	 * @param item
	 * @return
	 */
	public ItemButton addItem(int slot, Material material, String name) {
		return addItem(slot, new ItemBuilder(material, name).build());
	}
	
	public ItemButton addItem(int slot, ItemStack item) {
		// Pour éviter les erreurs, on crée un inventaire
		createDefaultInventory();
		
		ItemButton button = new ItemButton(item);
		this.items.put(slot, button);
		this.inventory.setItem(slot, item);
		return button;
	}

	/**
	 * Permet de retirer un item de la liste des items
	 * @param slot
	 */
	public void removeItem(int slot) {
		this.items.remove(slot);
	}

	/**
	 * Permet de supprimer tous les items
	 */
	public void clearItem() {
		this.items.clear();
	}

	/**
	 * Permet de récupérer tous les items
	 * @return
	 */
	public Map<Integer, ItemButton> getItems() {
		return items;
	}

	/**
	 * Si le click dans l'inventaire est désactivé (se qui est par default) alors il va retourner vrai
	 * @return vrai ou faux
	 */
	public boolean isDisableClick() {
		return disableClick;
	}

	/**
	 * Changer le fait de pouvoir cliquer dans l'inventaire
	 * @param disableClick
	 */
	protected void setDisableClick(boolean disableClick) {
		this.disableClick = disableClick;
	}

	/**
	 * Permet de récupérer le joueur
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Permet de récupérer la page
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the args
	 */
	public Object[] getObjets() {
		return args;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the guiName
	 */
	public String getGuiName() {
		return guiName;
	}

	protected InventoryResult preOpenInventory(Template main, Player player, int page, Object... args) throws InventoryOpenException{
		
		this.page = page;
		this.args = args;
		this.player = player;
		this.plugin = main;
		
		return openInventory(main, player, page, args);
	}
	
	public abstract InventoryResult openInventory(Template main, Player player, int page, Object... args) throws InventoryOpenException;

	protected abstract void onClose(InventoryCloseEvent event, Template plugin, Player player);

	protected abstract void onDrag(InventoryDragEvent event, Template plugin, Player player);

	@Override
	protected VInventory clone()  {
		try {
			return (VInventory) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
