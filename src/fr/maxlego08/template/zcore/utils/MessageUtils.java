package fr.maxlego08.template.zcore.utils;

import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.MessageType;
import fr.maxlego08.template.zcore.utils.nms.NMSUtils;
import fr.maxlego08.template.zcore.utils.players.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows you to manage messages sent to players and the console
 *
 * @author Maxence
 */
public abstract class MessageUtils extends LocationUtils {

    private final transient static int CENTER_PX = 154;

    /**
     * @param player
     * @param message
     * @param args
     */
    protected void messageWO(CommandSender player, Message message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    /**
     * @param player
     * @param message
     * @param args
     */
    protected void messageWO(CommandSender player, String message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    /**
     * @param sender
     * @param message
     * @param args
     */
    protected void message(CommandSender sender, String message, Object... args) {
        sender.sendMessage(Message.PREFIX.msg() + getMessage(message, args));
    }

    private void message(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    private void sendTchatMessage(Player player, Message message, Object... args) {
        if (message.getMessages().size() > 0) {
            message.getMessages().forEach(msg -> message(player, this.papi(getMessage(msg, args), player)));
        } else {
            message(player, this.papi((message.getType() == MessageType.WITHOUT_PREFIX ? "" : Message.PREFIX.msg()) + getMessage(message, args), player));
        }
    }

    /**
     * Allows you to send a message to a command sender
     *
     * @param sender  User who sent the command
     * @param message The message - Using the Message enum for simplified message
     *                management
     * @param args    The arguments - The arguments work in pairs, you must put for
     *                example %test% and then the value
     */
    protected void message(CommandSender sender, Message message, Object... args) {

        if (sender instanceof ConsoleCommandSender) {
            if (message.getMessages().size() > 0) {
                message.getMessages().forEach(msg -> message(sender, getMessage(msg, args)));
            } else {
                message(sender, Message.PREFIX.msg() + getMessage(message, args));
            }
        } else {

            Player player = (Player) sender;
            switch (message.getType()) {
                case CENTER:
                    if (message.getMessages().size() > 0) {
                        message.getMessages()
                                .forEach(msg -> sender.sendMessage(this.getCenteredMessage(this.papi(getMessage(msg, args), player))));
                    } else {
                        sender.sendMessage(this.getCenteredMessage(this.papi(getMessage(message, args), player)));
                    }

                    break;
                case ACTION:
                    this.actionMessage(player, message, args);
                    break;
                case TCHAT_AND_ACTION:
                    this.actionMessage(player, message, args);
                    sendTchatMessage(player, message, args);
                    break;
                case TCHAT:
                case WITHOUT_PREFIX:
                    sendTchatMessage(player, message, args);
                    break;
                case TITLE:
                    String title = message.getTitle();
                    String subTitle = message.getSubTitle();
                    int fadeInTime = message.getStart();
                    int showTime = message.getTime();
                    int fadeOutTime = message.getEnd();
                    this.title(player, this.papi(this.getMessage(title, args), player), this.papi(this.getMessage(subTitle, args), player), fadeInTime, showTime,
                            fadeOutTime);
                    break;
                default:
                    break;
            }
        }
    }

    protected void broadcast(Message message, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            message(player, message, args);
        }
        message(Bukkit.getConsoleSender(), message, args);
    }

    protected void actionMessage(Player player, Message message, Object... args) {
        ActionBar.sendActionBar(player, color(this.papi(getMessage(message, args), player)));
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(message.getMessage(), args);
    }

    protected String getMessage(String message, Object... args) {

        if (args.length % 2 != 0)
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");

        for (int i = 0; i < args.length; i += 2) {
            if (args[i] == null || args[i + 1] == null)
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            message = message.replace(args[i].toString(), args[i + 1].toString());
        }
        return message;
    }

    protected final Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send title to player
     *
     * @param player
     * @param title
     * @param subtitle
     * @param fadeInTime
     * @param showTime
     * @param fadeOutTime
     */
    protected void title(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {

        if (NMSUtils.isNewVersion()) {
            player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
            return;
        }

        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                    fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null),
                    chatsTitle, fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param player
     * @param packet
     */
    protected final void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message
     * @return message
     */
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals(""))
            return "";
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

    protected void broadcastCenterMessage(List<String> messages) {
        messages.stream().map(e -> e = getCenteredMessage(e)).forEach(e -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                messageWO(player, e);
            }
        });
    }

    protected void broadcastAction(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActionBar.sendActionBar(player, papi(message, player));
        }
    }

    protected String color(String message) {
        if (message == null)
            return null;
        if (NMSUtils.isHexColor()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                matcher = pattern.matcher(message);
            }
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

}
