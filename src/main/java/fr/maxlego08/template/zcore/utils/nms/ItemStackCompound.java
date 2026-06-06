package fr.maxlego08.template.zcore.utils.nms;

import fr.maxlego08.template.zcore.utils.nms.ItemStackUtils.EnumReflectionItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemStackCompound {

    private final EnumReflectionCompound reflection;

    public ItemStackCompound(EnumReflectionCompound reflection) {
        super();
        this.reflection = reflection;
    }

    /**
     * Permet de retourner le nbttag
     *
     * @param itemStack
     * @return object
     * @throws Exception
     */
    public Object getCompound(ItemStack itemStack) throws Exception {
        Object localItemStackObject = EnumReflectionItemStack.CRAFTITEMSTACK.getClassz()
                .getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke(null, new Object[]{itemStack});
        Object localCompoundObject = localItemStackObject.getClass().getMethod(this.reflection.getMethodGetTag())
                .invoke(localItemStackObject);
        if (localCompoundObject != null) {
            return localCompoundObject;
        }
        return EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz().newInstance();
    }

    /**
     * Permet d'appliquer le nbttag sur l'itemstack
     *
     * @param itemStack
     * @param compoundObject
     * @return itemstack
     * @throws Exception
     */
    public ItemStack applyCompound(ItemStack itemStack, Object compoundObject) throws Exception {
        Object localItemStackObject = EnumReflectionItemStack.CRAFTITEMSTACK.getClassz()
                .getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke(null, new Object[]{itemStack});

        localItemStackObject.getClass()
                .getMethod(this.reflection.getMethodSetTag(),
                        new Class[]{EnumReflectionItemStack.NBTTAGCOMPOUND.getClassz()})
                .invoke(localItemStackObject, compoundObject);

        return (ItemStack) EnumReflectionItemStack.CRAFTITEMSTACK.getClassz()
                .getMethod("asBukkitCopy", new Class[]{EnumReflectionItemStack.ITEMSTACK.getClassz()})
                .invoke(null, new Object[]{localItemStackObject});
    }

    public ItemStack setString(ItemStack itemStack, String key, String value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetString(), new Class[]{String.class, String.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getString(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (String) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetString(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public double getDouble(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (double) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetDouble(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public long getLong(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (long) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetLong(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public int getInt(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (int) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetInt(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public float getFloat(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (float) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetFloat(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public boolean getBoolean(ItemStack itemStack, String key) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            return (boolean) compoundObject.getClass()
                    .getMethod(this.reflection.getMethodGetBoolean(), new Class[]{String.class})
                    .invoke(compoundObject, new Object[]{key});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public ItemStack setInt(ItemStack itemStack, String key, int value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetInt(), new Class[]{String.class, int.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ItemStack setLong(ItemStack itemStack, String key, long value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetLong(), new Class[]{String.class, long.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ItemStack setFloat(ItemStack itemStack, String key, float value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetFloat(), new Class[]{String.class, float.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ItemStack setBoolean(ItemStack itemStack, String key, boolean value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetBoolean(), new Class[]{String.class, boolean.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ItemStack setDouble(ItemStack itemStack, String key, double value) {

        try {
            Object compoundObject = this.getCompound(itemStack);

            compoundObject.getClass()
                    .getMethod(this.reflection.getMethodSetDouble(), new Class[]{String.class, double.class})
                    .invoke(compoundObject, new Object[]{key, value});

            return this.applyCompound(itemStack, compoundObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Check if a key is contained in the nbttag
     *
     * @param itemStack
     * @param key
     * @return
     */
    public boolean isKey(ItemStack itemStack, String key) {

        try {

            Object nbttagCompound = this.getCompound(itemStack);
            if (nbttagCompound == null) {
                return false;
            }

            return (boolean) nbttagCompound.getClass()
                    .getMethod(this.reflection.getMethodHaskey(), new Class[]{String.class})
                    .invoke(nbttagCompound, new Object[]{key});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public enum EnumReflectionCompound {

        V1_8_8("getTag", "setTag", "hasKey", "getBoolean", "getFloat", "getDouble", "getLong", "getInt", "getString",
                "setBoolean", "setFloat", "setDouble", "setLong", "setInt", "setString"),
        V1_18_2("t", "c", "e", "q", "j", "k", "i", "h", "l", "a", "a", "a", "a", "a", "a"),
        V1_12("v", "c", "e", "q", "j", "k", "i", "h", "l", "a", "a", "a", "a", "a", "a"),
        V1_19("u", "c", "e", "q", "j", "k", "i", "h", "l", "a", "a", "a", "a", "a", "a"),
        V_OTHER("s", "c", "e", "q", "j", "k", "i", "h", "l", "a", "a", "a", "a", "a", "a"),

        ;

        private final String methodGetTag;
        private final String methodSetTag;

        private final String methodHaskey;

        private final String methodGetBoolean;
        private final String methodGetFloat;
        private final String methodGetDouble;
        private final String methodGetLong;
        private final String methodGetInt;
        private final String methodGetString;

        private final String methodSetBoolean;
        private final String methodSetFloat;
        private final String methodSetDouble;
        private final String methodSetLong;
        private final String methodSetInt;
        private final String methodSetString;

        private EnumReflectionCompound(String methodGetTag, String methodSetTag, String methodHaskey,
                                       String methodGetBoolean, String methodGetFloat, String methodGetDouble, String methodGetLong,
                                       String methodGetInt, String methodGetString, String methodSetBoolean, String methodSetFloat,
                                       String methodSetDouble, String methodSetLong, String methodSetInt, String methodSetString) {
            this.methodGetTag = methodGetTag;
            this.methodSetTag = methodSetTag;
            this.methodHaskey = methodHaskey;
            this.methodGetBoolean = methodGetBoolean;
            this.methodGetFloat = methodGetFloat;
            this.methodGetDouble = methodGetDouble;
            this.methodGetLong = methodGetLong;
            this.methodGetInt = methodGetInt;
            this.methodGetString = methodGetString;
            this.methodSetBoolean = methodSetBoolean;
            this.methodSetFloat = methodSetFloat;
            this.methodSetDouble = methodSetDouble;
            this.methodSetLong = methodSetLong;
            this.methodSetInt = methodSetInt;
            this.methodSetString = methodSetString;
        }

        public String getMethodGetTag() {
            return methodGetTag;
        }

        public String getMethodSetTag() {
            return methodSetTag;
        }

        public String getMethodHaskey() {
            return methodHaskey;
        }

        public String getMethodGetBoolean() {
            return methodGetBoolean;
        }

        public String getMethodGetFloat() {
            return methodGetFloat;
        }

        public String getMethodGetDouble() {
            return methodGetDouble;
        }

        public String getMethodGetLong() {
            return methodGetLong;
        }

        public String getMethodGetInt() {
            return methodGetInt;
        }

        public String getMethodGetString() {
            return methodGetString;
        }

        public String getMethodSetBoolean() {
            return methodSetBoolean;
        }

        public String getMethodSetFloat() {
            return methodSetFloat;
        }

        public String getMethodSetDouble() {
            return methodSetDouble;
        }

        public String getMethodSetLong() {
            return methodSetLong;
        }

        public String getMethodSetInt() {
            return methodSetInt;
        }

        public String getMethodSetString() {
            return methodSetString;
        }

    }

}
