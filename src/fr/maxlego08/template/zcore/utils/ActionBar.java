package fr.maxlego08.template.zcore.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar {
	private static String nmsver;
	private static boolean useOldMethods = false;

	public static void sendActionBar(Player player, String message) {
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_")) {
			useOldMethods = true;
		}

		if (player.isOnline()) {
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						Class<?> var2 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
						Object var3 = var2.cast(player);
						Class<?> var4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
						Class<?> var5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
						Object var6;
						Class<?> var7;
						Class<?> var8;
						Object message0;
						if (useOldMethods) {
							var7 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
							var8 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
							Method var20 = var7.getDeclaredMethod("a", new Class[] { String.class });
							message0 = var8
									.cast(var20.invoke(var7, new Object[] { "{\"text\": \"" + message + "\"}" }));
							var6 = var4.getConstructor(new Class[] { var8, Byte.TYPE })
									.newInstance(new Object[] { message0, Byte.valueOf((byte) 2) });
						} else {
							var7 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
							var8 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");

							try {
								Class<?> var9 = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
								Object[] var22 = var9.getEnumConstants();
								Object message1 = null;
								Object[] message2 = var22;
								int message3 = var22.length;

								for (int message4 = 0; message4 < message3; ++message4) {
									Object message5 = message2[message4];
									if (message5.toString().equals("GAME_INFO")) {
										message1 = message5;
									}
								}

								Object var24 = var7.getConstructor(new Class[] { String.class })
										.newInstance(new Object[] { message });
								var6 = var4.getConstructor(new Class[] { var8, var9 })
										.newInstance(new Object[] { var24, message1 });
							} catch (ClassNotFoundException message6) {
								message0 = var7.getConstructor(new Class[] { String.class })
										.newInstance(new Object[] { message });
								var6 = var4.getConstructor(new Class[] { var8, Byte.TYPE })
										.newInstance(new Object[] { message0, Byte.valueOf((byte) 2) });
							}
						}

						Method message8 = var2.getDeclaredMethod("getHandle", new Class[0]);
						Object message9 = message8.invoke(var3, new Object[0]);
						Field var21 = message9.getClass().getDeclaredField("playerConnection");
						message0 = var21.get(message9);
						Method var23 = message0.getClass().getDeclaredMethod("sendPacket", new Class[] { var5 });
						var23.invoke(message0, new Object[] { var6 });
					} catch (Exception message7) {
						message7.printStackTrace();
					}
				}
			}, 1);

		}
	}

	public static void sendActionBar(Player player, String message, int sec) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int currentSec = sec;

			@Override
			public void run() {
				if (currentSec <= 0)
					cancel();
				sendActionBar(player, message);
				currentSec--;
			}
		}, 1000, 1000);
	}

	public static void broadcastActionMessage(String paramString) {
		broadcastActionMessage(paramString, -1);
	}

	public static void broadcastActionMessage(String paramString, int timer) {
		for (Player localPlayer : Bukkit.getOnlinePlayers())
			sendActionBar(localPlayer, paramString);
	}
}
