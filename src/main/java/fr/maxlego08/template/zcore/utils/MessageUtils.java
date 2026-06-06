package fr.maxlego08.template.zcore.utils;

import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.MessageType;
import fr.maxlego08.template.zcore.utils.nms.NmsVersion;
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
 * Allows you to manage messages sent to players and the console.
 * Provides various utility methods for sending and formatting messages.
 * Extends {@link LocationUtils}.
 *
 * @see LocationUtils
 */
public abstract class MessageUtils extends LocationUtils {

    private final static int CENTER_PX = 154;

    /**
     * Sends a message without prefix to the specified command sender.
     *
     * @param player  the command sender to send the message to.
     * @param message the message to send.
     * @param args    the arguments for the message.
     */
    protected void messageWO(CommandSender player, Message message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    /**
     * Sends a message without prefix to the specified command sender.
     *
     * @param player  the command sender to send the message to.
     * @param message the message to send.
     * @param args    the arguments for the message.
     */
    protected void messageWO(CommandSender player, String message, Object... args) {
        player.sendMessage(getMessage(message, args));
    }

    /**
     * Sends a message with prefix to the specified command sender.
     *
     * @param sender  the command sender to send the message to.
     * @param message the message to send.
     * @param args    the arguments for the message.
     */
    protected void message(CommandSender sender, String message, Object... args) {
        sender.sendMessage(Message.PREFIX.msg() + getMessage(message, args));
    }

    /**
     * Sends a message to the specified command sender.
     *
     * @param sender  the command sender to send the message to.
     * @param message the message to send.
     */
    private void message(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    /**
     * Sends a chat message to the specified player.
     *
     * @param player  the player to send the message to.
     * @param message the message to send.
     * @param args    the arguments for the message.
     */
    private void sendTchatMessage(Player player, Message message, Object... args) {
        if (message.getMessages().size() > 0) {
            message.getMessages().forEach(msg -> message(player, this.papi(getMessage(msg, args), player)));
        } else {
            message(player, this.papi((message.getType() == MessageType.WITHOUT_PREFIX ? "" : Message.PREFIX.msg()) + getMessage(message, args), player));
        }
    }

    /**
     * Allows you to send a message to a command sender.
     *
     * @param sender  the user who sent the command.
     * @param message the message - using the Message enum for simplified message management.
     * @param args    the arguments - the arguments work in pairs, you must put for example %test% and then the value.
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
                        message.getMessages().forEach(msg -> sender.sendMessage(this.getCenteredMessage(this.papi(getMessage(msg, args), player))));
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
                    this.title(player, this.papi(this.getMessage(title, args), player), this.papi(this.getMessage(subTitle, args), player), fadeInTime, showTime, fadeOutTime);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Broadcasts a message to all online players and the console.
     *
     * @param message the message to broadcast.
     * @param args    the arguments for the message.
     */
    protected void broadcast(Message message, Object... args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            message(player, message, args);
        }
        message(Bukkit.getConsoleSender(), message, args);
    }

    /**
     * Sends an action bar message to the specified player.
     *
     * @param player  the player to send the message to.
     * @param message the message to send.
     * @param args    the arguments for the message.
     */
    protected void actionMessage(Player player, Message message, Object... args) {
        ActionBar.sendActionBar(player, color(this.papi(getMessage(message, args), player)));
    }

    /**
     * Gets the formatted message with arguments replaced.
     *
     * @param message the message to format.
     * @param args    the arguments for the message.
     * @return the formatted message.
     */
    protected String getMessage(Message message, Object... args) {
        return getMessage(message.getMessage(), args);
    }

    /**
     * Gets the formatted message with arguments replaced.
     *
     * @param message the message to format.
     * @param args    the arguments for the message.
     * @return the formatted message.
     */
    protected String getMessage(String message, Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < args.length; i += 2) {
            if (args[i] == null || args[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(args[i].toString(), args[i + 1].toString());
        }
        return message;
    }

    /**
     * Gets a class from the net.minecraft.server package.
     *
     * @param name the name of the class.
     * @return the class object, or null if not found.
     */
    protected final Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends a title to the player.
     *
     * @param player      the player to send the title to.
     * @param title       the title text.
     * @param subtitle    the subtitle text.
     * @param fadeInTime  the fade-in time in ticks.
     * @param showTime    the showtime in ticks.
     * @param fadeOutTime the fade-out time in ticks.
     */
    protected void title(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        if (NmsVersion.nmsVersion.isNewMaterial()) {
            player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
            return;
        }

        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle, fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle, fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a packet to the player.
     *
     * @param player the player to send the packet to.
     * @param packet the packet to send.
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
     * Gets a centered message.
     *
     * @param message the message to center.
     * @return the centered message.
     */
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals("")) {
            return "";
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
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
        return sb + message;
    }

    /**
     * Broadcasts a centered message to all online players.
     *
     * @param messages the list of messages to broadcast.
     */
    protected void broadcastCenterMessage(List<String> messages) {
        messages.stream().map(this::getCenteredMessage).forEach(e -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                messageWO(player, e);
            }
        });
    }

    /**
     * Broadcasts an action bar message to all online players.
     *
     * @param message the message to broadcast.
     */
    protected void broadcastAction(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ActionBar.sendActionBar(player, papi(message, player));
        }
    }

    /**
     * Translates alternate color codes in the message string.
     *
     * @param message the message to color.
     * @return the colored message.
     */
    protected String color(String message) {
        if (message == null) {
            return null;
        }
        if (NmsVersion.nmsVersion.isHexVersion()) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
                matcher = pattern.matcher(message);
            }
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
}
