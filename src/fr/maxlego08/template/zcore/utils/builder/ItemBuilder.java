package fr.maxlego08.template.zcore.utils.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.maxlego08.template.zcore.utils.ZUtils;

/**
 * 
 * @author Maxlego08
 *
 */
public class ItemBuilder extends ZUtils implements Cloneable {

	private ItemStack item;
	private final Material material;
	private ItemMeta meta;
	private int data;
	private int amount;
	private String name;
	private List<String> lore;
	private List<ItemFlag> flags;
	private int durability;
	private Map<Enchantment, Integer> enchantments;

	/**
	 * 
	 * @param material
	 * @param data
	 * @param amount
	 * @param name
	 * @param lore
	 * @param flags
	 * @param enchantments
	 */
	public ItemBuilder(Material material, int data, int amount, String name, List<String> lore, List<ItemFlag> flags,
			Map<Enchantment, Integer> enchantments) {
		super();
		this.material = material;
		this.data = data;
		this.amount = amount;
		this.name = name;
		this.lore = lore;
		this.flags = flags;
		this.enchantments = enchantments;
	}

	/**
	 * 
	 * @param material
	 */
	public ItemBuilder(Material material) {
		this(material, 1);
	}

	/**
	 * 
	 * @param material
	 * @param amount
	 */
	public ItemBuilder(Material material, int amount) {
		this(material, amount, 0);
	}

	/**
	 * 
	 * @param material
	 * @param amount
	 * @param data
	 */
	public ItemBuilder(Material material, int amount, int data) {
		this(material, amount, data, null);
	}

	/**
	 * 
	 * @param material
	 * @param amount
	 * @param data
	 * @param name
	 */
	public ItemBuilder(Material material, int amount, int data, String name) {
		this(material, data, amount, name, null, null, null);
	}

	/**
	 * 
	 * @param material
	 * @param amount
	 * @param name
	 */
	public ItemBuilder(Material material, int amount, String name) {
		this(material, 0, amount, name, null, null, null);
	}

	/**
	 * 
	 * @param material
	 * @param name
	 */
	public ItemBuilder(Material material, String name) {
		this(material, 0, 1, name, null, null, null);
	}

	/**
	 * 
	 * @param material
	 * @param flags
	 */
	public ItemBuilder(Material material, ItemFlag... flags) {
		this(material, 0, 1, null, null, Arrays.asList(flags), null);
	}

	/**
	 * 
	 * @param material
	 * @param lore
	 */
	public ItemBuilder(Material material, String... lore) {
		this(material, 0, 1, null, Arrays.asList(lore), null, null);
	}

	/**
	 * add enchant
	 * 
	 * @param enchantment
	 * @param value
	 * @return
	 */
	public ItemBuilder addEnchant(Enchantment enchantment, int value) {
		if (enchantments == null)
			enchantments = new HashMap<Enchantment, Integer>();
		enchantments.put(enchantment, value);
		return this;
	}

	/**
	 * add flags
	 * 
	 * @param flags
	 * @return
	 */
	public ItemBuilder setFlag(ItemFlag... flags) {
		this.flags = Arrays.asList(flags);
		return this;
	}

	/**
	 * 
	 * @param flag
	 * @return
	 */
	public ItemBuilder setFlag(ItemFlag flag) {
		if (flags == null)
			flags = new ArrayList<ItemFlag>();
		this.flags.add(flag);
		return this;
	}

	/**
	 * 
	 * @param format
	 * @param args
	 * @return
	 */
	public ItemBuilder addLine(String format, Object... args) {
		if (lore == null)
			lore = new ArrayList<String>();
		lore.add(String.format(format, args));
		return this;
	}

	/**
	 * 
	 * @param format
	 * @param args
	 * @return
	 */
	public ItemBuilder addLine(String format) {
		if (lore == null)
			lore = new ArrayList<String>();
		lore.add(format);
		return this;
	}

	/**
	 * 
	 * @param lores
	 * @return
	 */
	public ItemBuilder setLore(String... lores) {
		this.lore = Arrays.asList(lores);
		return this;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ItemBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @param durability
	 * @return
	 */
	public ItemBuilder durability(int durability) {
		this.durability = durability;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public ItemBuilder glow() {
		addEnchant(material != Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK, 10);
		setFlag(ItemFlag.HIDE_ENCHANTS);
		return this;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ItemBuilder owner(Player name) {
		return owner(name.getName());
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder owner(String name) {
		if ((material == getMaterial(144)) || (material == getMaterial(397))) {
			SkullMeta smeta = (SkullMeta) meta;
			smeta.setOwner(name);
			if (meta == null)
				build();
			meta = smeta;
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemStack build() {
		item = new ItemStack(material, amount, (short) data);

		if (meta == null)
			meta = item.getItemMeta();

		if (flags != null)
			flags.forEach(flag -> meta.addItemFlags(flag));

		if (name != null)
			meta.setDisplayName(name);

		if (lore != null)
			meta.setLore(lore);

		if (enchantments != null)
			enchantments.forEach((e, l) -> meta.addEnchant(e, l, true));

		if (durability != 0)
			item.setDurability((short) durability);

		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Clone
	 */
	public ItemBuilder clone() {
		try {
			return (ItemBuilder) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return the meta
	 */
	@SuppressWarnings("deprecation")
	public ItemMeta getMeta() {
		if (meta == null)
			meta = new ItemStack(material, amount, (short) data).getItemMeta();
		return meta;
	}

	/**
	 * @return the data
	 */
	public int getData() {
		return data;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lore
	 */
	public List<String> getLore() {
		return lore;
	}

	/**
	 * @return the flags
	 */
	public List<ItemFlag> getFlags() {
		return flags;
	}

	/**
	 * @return the durability
	 */
	public int getDurability() {
		return durability;
	}

	/**
	 * @return the enchantments
	 */
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

}
