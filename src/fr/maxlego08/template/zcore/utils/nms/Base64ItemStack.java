package fr.maxlego08.template.zcore.utils.nms;

import fr.maxlego08.template.zcore.utils.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author sya-ri
 * Github: <a href="https://github.com/sya-ri/base64-itemstack/tree/master">https://github.com/sya-ri/base64-itemstack/tree/master</a>
 */
public class Base64ItemStack {


    public static String encode(ItemStack item) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            ObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(gzipOutputStream);
            objectOutputStream.writeObject(item);
            objectOutputStream.close();
            return Base64.encode(byteArrayOutputStream.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static ItemStack decode(String data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(data));
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            ObjectInputStream objectInputStream = new BukkitObjectInputStream(gzipInputStream);
            ItemStack item = (ItemStack) objectInputStream.readObject();
            objectInputStream.close();
            return item;
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }

}