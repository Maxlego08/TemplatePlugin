package fr.maxlego08.template.zcore.utils.nms;

import fr.maxlego08.template.zcore.utils.Base64;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.zip.GZIPInputStream;

public class ItemStackUtils {

    private static final NmsVersion NMS_VERSION = NmsVersion.nmsVersion;

    /**
     * Change {@link ItemStack} to {@link String}
     *
     * @return {@link String}
     */
    public static String serializeItemStack(ItemStack paramItemStack) {

        if (paramItemStack == null) return "null";

        if (NmsVersion.getCurrentVersion().isAttributItemStack()) {
            return Base64ItemStack.encode(paramItemStack);
        }

        ByteArrayOutputStream localByteArrayOutputStream = null;
        try {
            Class<?> localClass = EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz();
            Constructor<?> localConstructor = localClass.getConstructor();
            Object localObject1 = localConstructor.newInstance();
            Object localObject2 = EnumReflectionItemStack.CRAFTITEMSTACK.getClassz().getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke(null, paramItemStack);

            if (NmsVersion.nmsVersion.isNewNBTVersion()) {
                EnumReflectionItemStack.ITEMSTACK.getClassz().getMethod("b", new Class[]{localClass}).invoke(localObject2, localObject1);
            } else {
                EnumReflectionItemStack.ITEMSTACK.getClassz().getMethod("save", new Class[]{localClass}).invoke(localObject2, localObject1);
            }

            localByteArrayOutputStream = new ByteArrayOutputStream();
            EnumReflectionItemStack.NBTCOMPRESSEDSTREAMTOOLS.getClassz().getMethod("a", new Class[]{localClass, OutputStream.class}).invoke(null, localObject1, localByteArrayOutputStream);
        } catch (Exception localException) {
            // localException.printStackTrace();
        }
        return Base64.encode(localByteArrayOutputStream.toByteArray());
    }

    /**
     * Change {@link String} to {@link ItemStack}
     *
     * @return {@link ItemStack}
     */
    public static ItemStack deserializeItemStack(String paramString) {

        if (paramString == null || paramString.equals("null")) {
            return null;
        }

        if (NmsVersion.getCurrentVersion().isAttributItemStack()) {
            return Base64ItemStack.decode(paramString);
        }

        ByteArrayInputStream localByteArrayInputStream = null;
        try {
            localByteArrayInputStream = new ByteArrayInputStream(Base64.decode(paramString));
        } catch (Exception ignored) {
        }
        Class<?> localClass1 = EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz();
        Class<?> localClass2 = EnumReflectionItemStack.ITEMSTACK.getClassz();
        Object localObject1 = null;
        ItemStack localItemStack = null;
        Object localObject2 = null;
        try {
            if (NmsVersion.nmsVersion == NmsVersion.V_1_20_4) {

                DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(localByteArrayInputStream)));
                localObject1 = EnumReflectionItemStack.NBTCOMPRESSEDSTREAMTOOLS.getClassz().getMethod("a", new Class[]{DataInput.class}).invoke(null, datainputstream);
            } else {

                localObject1 = EnumReflectionItemStack.NBTCOMPRESSEDSTREAMTOOLS.getClassz().getMethod("a", new Class[]{InputStream.class}).invoke(null, localByteArrayInputStream);
            }

            if (NMS_VERSION == NmsVersion.V_1_11 || NMS_VERSION == NmsVersion.V_1_12) {
                Constructor<?> localConstructor = localClass2.getConstructor(localClass1);
                localObject2 = localConstructor.newInstance(localObject1);
            } else if (!NMS_VERSION.isItemLegacy()) {
                localObject2 = localClass2.getMethod("a", new Class[]{localClass1}).invoke(null, localObject1);
            } else {
                localObject2 = localClass2.getMethod("createStack", new Class[]{localClass1}).invoke(null, localObject1);
            }

            localItemStack = (ItemStack) EnumReflectionItemStack.CRAFTITEMSTACK.getClassz().getMethod("asBukkitCopy", new Class[]{localClass2}).invoke(null, new Object[]{localObject2});
        } catch (Exception localException) {
            // localException.printStackTrace();
        }

        return localItemStack;
    }

    /*
     * public static boolean isUnbreakable(ItemStack itemStack) { try {
     *
     * Class<?> localClass = EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz();
     * Object localObject2 = EnumReflectionItemStack.CRAFTITEMSTACK.getClassz()
     * .getMethod("asNMSCopy", new Class[] { ItemStack.class }).invoke(null, new
     * Object[] { itemStack });
     *
     * Object nbttag = EnumReflectionItemStack.ITEMSTACK.getClassz()
     * .getMethod("getTag", new Class[] { localClass }) .invoke(localObject2,
     * new Object[] { localObject2 });
     *
     * } catch (IllegalAccessException | IllegalArgumentException |
     * InvocationTargetException | NoSuchMethodException | SecurityException e)
     * { e.printStackTrace(); return false; } }
     */

    public enum EnumReflectionItemStack {

        ITEMSTACK("ItemStack", "net.minecraft.world.item.ItemStack"),

        CRAFTITEMSTACK("inventory.CraftItemStack", true),

        NBTCOMPRESSEDSTREAMTOOLS("NBTCompressedStreamTools", "net.minecraft.nbt.NBTCompressedStreamTools"),

        NBTTAGCOMPOUND("NBTTagCompound", "net.minecraft.nbt.NBTTagCompound"),

        ;

        private final String oldClassName;
        private final String newClassName;
        private final boolean isBukkit;

        EnumReflectionItemStack(String oldClassName, String newClassName, boolean isBukkit) {
            this.oldClassName = oldClassName;
            this.newClassName = newClassName;
            this.isBukkit = isBukkit;
        }

        EnumReflectionItemStack(String oldClassName, String newClassName) {
            this(oldClassName, newClassName, false);
        }

        EnumReflectionItemStack(String oldClassName, boolean isBukkit) {
            this(oldClassName, null, isBukkit);
        }

        public Class<?> getClassz() {
            String nmsPackage = Bukkit.getServer().getClass().getPackage().getName();
            String nmsVersion = nmsPackage.replace(".", ",").split(",")[3];
            String var3 = NmsVersion.nmsVersion.isNewNMSVersion() ? this.isBukkit ? "org.bukkit.craftbukkit." + nmsVersion + "." + this.oldClassName : this.newClassName : (this.isBukkit ? "org.bukkit.craftbukkit." : "net.minecraft.server.") + nmsVersion + "." + this.oldClassName;
            Class<?> localClass = null;
            try {
                localClass = Class.forName(var3);
            } catch (ClassNotFoundException localClassNotFoundException) {
                localClassNotFoundException.printStackTrace();
            }
            return localClass;
        }

    }

}
