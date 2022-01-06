package fr.maxlego08.hopper.zcore.utils;

import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.minecraft.nbt.NBTTagCompound;

public class NMS_1_17 extends ZUtils {

	
	public ItemStack set(ItemStack itemStack, String key, EntityType type) {

		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		compound.setString(key, type.name());
		itemStackNMS.setTag(compound);

		return CraftItemStack.asBukkitCopy(itemStackNMS);
	}

	
	public EntityType get(ItemStack itemStack, String key) {

		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		String typeAsString = compound.getString(key);

		return EntityType.valueOf(typeAsString);
	}

	
	public boolean has(ItemStack itemStack, String key) {

		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		
		if (itemStackNMS == null)
			return false;
		NBTTagCompound compound = itemStackNMS.getTag();
		if (compound == null)
			return false;

		return compound.hasKey(key);
	}

	
	public ItemStack set(ItemStack itemStack, String key, boolean value) {
		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		compound.setBoolean(key, value);
		itemStackNMS.setTag(compound);

		return CraftItemStack.asBukkitCopy(itemStackNMS);
	}

	
	public ItemStack set(ItemStack itemStack, String key, int value) {
		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		compound.setInt(key, value);
		itemStackNMS.setTag(compound);
		return CraftItemStack.asBukkitCopy(itemStackNMS);
	}

	
	public int getInteger(ItemStack itemStack, String key) {
		net.minecraft.world.item.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		return compound.getInt(key);
	}

}
