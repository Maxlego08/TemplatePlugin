package fr.maxlego08.template.zcore.utils.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import fr.maxlego08.template.zcore.ZPlugin;
import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class ItemStackAdapter extends TypeAdapter<ItemStack>{

	private static Type seriType = new TypeToken<Map<String, Object>>(){}.getType();
	
	@Override
	public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
		if(itemStack == null) {
			jsonWriter.nullValue();
			return;
		}
		jsonWriter.value(getRaw(removeSlotNBT(itemStack)));
	}

	@Override
	public ItemStack read(JsonReader jsonReader) throws IOException {
		if(jsonReader.peek() == JsonToken.NULL) {
			jsonReader.nextNull();
			return null;
		}
		return fromRaw(jsonReader.nextString());
	}

	private String getRaw (ItemStack item) {
		Map<String, Object> serial = item.serialize();

		if(serial.get("meta") != null) {
			ItemMeta itemMeta = item.getItemMeta();

			Map<String, Object> originalMeta = itemMeta.serialize();
			Map<String, Object> meta = new HashMap<String, Object>();
			for(Entry<String, Object> entry : originalMeta.entrySet())
				meta.put(entry.getKey(), entry.getValue());
			Object o;
			for(Entry<String, Object> entry : meta.entrySet()) {
				o = entry.getValue();
				if(o instanceof ConfigurationSerializable) {
					ConfigurationSerializable serializable = (ConfigurationSerializable) o;
					Map<String, Object> serialized = recursiveSerialization(serializable);
					meta.put(entry.getKey(), serialized);
				}
			}
			serial.put("meta", meta);
		}

		return ZPlugin.z().getGson().toJson(serial);
	}

	@SuppressWarnings("unchecked")
	private ItemStack fromRaw (String raw) {
		Map<String, Object> keys = ZPlugin.z().getGson().fromJson(raw, seriType);

		if(keys.get("amount") != null) {
			Double d = (Double) keys.get("amount");
			Integer i = d.intValue();
			keys.put("amount", i);
		}

		ItemStack item;
		try {
			item = ItemStack.deserialize(keys);
		}catch(Exception e) {
			return null;
		}

		if(item == null)
			return null;

		if(keys.containsKey("meta")) {
			Map<String, Object> itemmeta = (Map<String, Object>) keys.get("meta");
			itemmeta = recursiveDoubleToInteger(itemmeta);
			ItemMeta meta = (ItemMeta) ConfigurationSerialization.deserializeObject(itemmeta, ConfigurationSerialization.getClassByAlias("ItemMeta"));
			item.setItemMeta(meta);
		}

		return item;
	}
	
	private static ItemStack removeSlotNBT (ItemStack item) {
		if (item == null)
			return null;
		net.minecraft.server.v1_15_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		if (nmsi == null)
			return null;
		NBTTagCompound nbtt = nmsi.getTag();
		if(nbtt != null) {
			nbtt.remove("Slot");
			nmsi.setTag(nbtt);
		}
		return item;
	}
	@SuppressWarnings("unchecked")
	private static Map<String, Object> recursiveDoubleToInteger(Map<String, Object> originalMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(Entry<String, Object> entry : originalMap.entrySet()) {
			Object o = entry.getValue();
			if(o instanceof Double) {
				Double d = (Double) o;
				Integer i = d.intValue();
				map.put(entry.getKey(), i);
			}else if(o instanceof Map) {
				Map<String, Object> subMap = (Map<String, Object>) o;
				map.put(entry.getKey(), recursiveDoubleToInteger(subMap));
			}else{
				map.put(entry.getKey(), o);
			}
		}
		return map;
	}
	
	private static Map<String, Object> recursiveSerialization(ConfigurationSerializable o) {
		Map<String, Object> originalMap = o.serialize();
		Map<String, Object> map = new HashMap<String, Object>();
		for(Entry<String, Object> entry : originalMap.entrySet()) {
			Object o2 = entry.getValue();
			if(o2 instanceof ConfigurationSerializable) {
				ConfigurationSerializable serializable = (ConfigurationSerializable) o2;
				Map<String, Object> newMap = recursiveSerialization(serializable);
				newMap.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias(serializable.getClass()));
				map.put(entry.getKey(), newMap);
			}
		}
		map.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias(o.getClass()));
		return map;
	}
	
}
