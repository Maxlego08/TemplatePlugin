package fr.maxlego08.template.zcore.utils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public abstract class ZUtils {

	/**
	 * 
	 * */

	protected Location changeStringLocationToLocation(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		return new Location(w, x, y, z);
	}

	/**
	 * 
	 * */

	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * 
	 * */

	protected String encode(ItemStack item) {
		return ItemDecoder.serializeItemStack(item);
	}

	/**
	 * 
	 * */

	protected ItemStack decode(String item) {
		return ItemDecoder.deserializeItemStack(item);
	}

	/**
	 * 
	 * */

	protected String betterMaterial(Material material) {
		return TextUtil.getMaterialLowerAndMajAndSpace(material);
	}

	/*
	 * 
	 * */

	public int getNumberBetween(int a, int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}

	/*
	 * 
	 * */

	protected boolean hasInventoryFull(Player p) {
		int slot = 0;
		ItemStack[] arrayOfItemStack;
		int x = (arrayOfItemStack = p.getInventory().getContents()).length;
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents == null))
				slot++;
		}
		return slot == 0;
	}

	/*
	 * 
	 * */

	protected void give(Player player, ItemStack item) {
		if (hasInventoryFull(player))
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}
	
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
