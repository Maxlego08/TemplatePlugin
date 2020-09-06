package fr.maxlego08.template.zcore.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.utils.players.ActionBar;

public abstract class MessageUtils extends LocationUtils{

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, Message message) {
		player.sendMessage(Message.PREFIX.msg() + " " + message.msg());
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, String message) {
		player.sendMessage(Message.PREFIX.msg() + " " + message);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void message(CommandSender player, String message, Object... args) {
		player.sendMessage(Message.PREFIX.msg() + " " + String.format(message, args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void messageWO(CommandSender player, Message message) {
		player.sendMessage(message.msg());
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void messageWO(CommandSender player, String message) {
		player.sendMessage(message);
	}

	/**
	 * 
	 * @param player
	 * @param message
	 */
	protected void messageWO(CommandSender player, String message, Object... args) {
		player.sendMessage(String.format(message, args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void messageWO(CommandSender player, Message message, Object... args) {
		player.sendMessage(String.format(message.msg(), args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void message(CommandSender player, Message message, Object... args) {
		player.sendMessage(Message.PREFIX.msg() + " " + String.format(message.msg(), args));
	}

	/**
	 * 
	 * @param player
	 * @param message
	 * @param args
	 */
	protected void actionMessage(Player player, Message message, Object... args) {
		ActionBar.sendActionBar(player, String.format(message.msg(), args));
	}

}
