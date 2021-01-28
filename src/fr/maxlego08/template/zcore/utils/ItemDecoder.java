package fr.maxlego08.template.zcore.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class ItemDecoder {

	private static volatile Map<ItemStack, String> itemstackSerialized = new HashMap<ItemStack, String>();

	public static String serializeItemStack(ItemStack paramItemStack) {

		if (paramItemStack == null) {
			return "null";
		}

		if (itemstackSerialized.containsKey(paramItemStack))
			return itemstackSerialized.get(paramItemStack);

		ByteArrayOutputStream localByteArrayOutputStream = null;
		try {
			Class<?> localClass = getNMSClass("NBTTagCompound");
			Constructor<?> localConstructor = localClass.getConstructor(new Class[0]);
			Object localObject1 = localConstructor.newInstance(new Object[0]);
			Object localObject2 = getOBClass("inventory.CraftItemStack")
					.getMethod("asNMSCopy", new Class[] { ItemStack.class })
					.invoke(null, new Object[] { paramItemStack });
			getNMSClass("ItemStack").getMethod("save", new Class[] { localClass }).invoke(localObject2,
					new Object[] { localObject1 });
			localByteArrayOutputStream = new ByteArrayOutputStream();
			getNMSClass("NBTCompressedStreamTools").getMethod("a", new Class[] { localClass, OutputStream.class })
					.invoke(null, new Object[] { localObject1, localByteArrayOutputStream });
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		String string = Base64.encode(localByteArrayOutputStream.toByteArray());
		itemstackSerialized.put(paramItemStack, string);
		return string;
	}

	public static ItemStack deserializeItemStack(String paramString) {

		if (paramString.equals("null")) {
			return null;
		}
		ByteArrayInputStream localByteArrayInputStream = null;
		try {
			localByteArrayInputStream = new ByteArrayInputStream(Base64.decode(paramString));
		} catch (Exception localBase64DecodingException) {
		}
		Class<?> localClass1 = getNMSClass("NBTTagCompound");
		Class<?> localClass2 = getNMSClass("ItemStack");
		Object localObject1 = null;
		ItemStack localItemStack = null;
		Object localObject2 = null;
		try {
			localObject1 = getNMSClass("NBTCompressedStreamTools").getMethod("a", new Class[] { InputStream.class })
					.invoke(null, new Object[] { localByteArrayInputStream });
			if (getNMSVersion() == 1.11D || getNMSVersion() == 1.12D) {
				Constructor<?> localConstructor = localClass2.getConstructor(new Class[] { localClass1 });
				localObject2 = localConstructor.newInstance(new Object[] { localObject1 });
			}

			else if (getNMSVersion() == 1.13D || getNMSVersion() == 1.14D || getNMSVersion() == 1.15D
					|| getNMSVersion() == 1.16D) {
				localObject2 = localClass2.getMethod("a", new Class[] { localClass1 }).invoke(null,
						new Object[] { localObject1 });
			} else {
				localObject2 = localClass2.getMethod("createStack", new Class[] { localClass1 }).invoke(null,
						new Object[] { localObject1 });
			}
			localItemStack = (ItemStack) getOBClass("inventory.CraftItemStack")
					.getMethod("asBukkitCopy", new Class[] { localClass2 }).invoke(null, new Object[] { localObject2 });
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		if (localItemStack != null && !itemstackSerialized.containsKey(localItemStack))
			itemstackSerialized.put(localItemStack, paramString);
		return localItemStack;
	}

	private static Class<?> getNMSClass(String paramString) {
		String str1 = Bukkit.getServer().getClass().getPackage().getName();
		String str2 = str1.replace(".", ",").split(",")[3];
		String str3 = "net.minecraft.server." + str2 + "." + paramString;
		Class<?> localClass = null;
		try {
			localClass = Class.forName(str3);
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
			System.err.println("Unable to find reflection class " + str3 + "!");
		}
		return localClass;
	}

	private static Class<?> getOBClass(String paramString) {
		String var1 = Bukkit.getServer().getClass().getPackage().getName();
		String var2 = var1.replace(".", ",").split(",")[3];
		String var3 = "org.bukkit.craftbukkit." + var2 + "." + paramString;
		Class<?> localClass = null;
		try {
			localClass = Class.forName(var3);
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		}
		return localClass;
	}

	public static double version;

	public static double getNMSVersion() {
		if (version != 0)
			return version;
		String var1 = Bukkit.getServer().getClass().getPackage().getName();
		String[] arrayOfString = var1.replace(".", ",").split(",")[3].split("_");
		String var2 = arrayOfString[0].replace("v", "");
		String var3 = arrayOfString[1];
		return version = Double.parseDouble(var2 + "." + var3);
	}

	/**
	 * 
	 * @return true if version is granther than 1.13
	 */
	public static boolean isNewVersion() {
		return !isOldVersion();
	}

	public static boolean isOneHand() {
		return getNMSVersion() == 1.7 || getNMSVersion() == 1.8;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isOldVersion() {
		double version = getNMSVersion();
		return version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.13
				|| version == 1.12;
	}

}
