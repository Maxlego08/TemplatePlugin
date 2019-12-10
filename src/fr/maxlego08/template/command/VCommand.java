package fr.maxlego08.template.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.TextUtil;
import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.inventory.IIventory;

public abstract class VCommand extends ZUtils {

	/**
	 * Permission used for the command, if it is a null then everyone can
	 * execute the command
	 */
	private Permission permission;

	/**
	 * Mother command of this command
	 */
	private VCommand parent;

	/**
	 * Are all sub commands used
	 */
	private List<String> subCommands = new ArrayList<String>();

	/**
	 * If this variable is false the command will not be able to use this
	 * command
	 */
	private boolean consoleCanUse = true;

	/**
	 * This variable allows to run the main class of the command even with
	 * arguments convenient for commands like /ban <player>
	 */
	private boolean ignoreParent = false;
	private boolean ignoreArgs = false;

	/**
	 * This is the person who executes the command
	 */
	public CommandSender sender;
	public Player player;

	/**
	 * Allows you to directly open an inventory when running the command
	 */
	private IIventory inventory;

	private String syntaxe;

	/*
	 * Args
	 */
	public String[] args;

	private String description;

	private int argsMinLength;
	private int argsMaxLength;

	//
	// GETTER
	//

	/**
	 * @return the permission
	 */
	public Permission getPermission() {
		return permission;
	}

	/**
	 * @return the parent
	 */
	public VCommand getParent() {
		return parent;
	}

	/**
	 * @return the subCommand
	 */
	public List<String> getSubCommands() {
		return subCommands;
	}

	/**
	 * @return the consoleCanUse
	 */
	public boolean isConsoleCanUse() {
		return consoleCanUse;
	}

	/**
	 * @return the ignoreParent
	 */
	public boolean isIgnoreParent() {
		return ignoreParent;
	}

	public CommandSender getSender() {
		return sender;
	}

	/**
	 * @return the argsMinLength
	 */
	public int getArgsMinLength() {
		return argsMinLength;
	}

	/**
	 * @return the argsMaxLength
	 */
	public int getArgsMaxLength() {
		return argsMaxLength;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the syntaxe
	 */
	public String getSyntaxe() {
		return syntaxe;
	}

	public IIventory getInventory() {
		return inventory;
	}

	public boolean isIgnoreArgs() {
		return ignoreArgs;
	}

	public String getDescription() {
		return description;
	}

	//
	// SETTER
	//

	/**
	 * @param argsMinLength
	 *            the argsMinLength to set
	 */
	public void setArgsMinLength(int argsMinLength) {
		this.argsMinLength = argsMinLength;
	}

	/**
	 * @param argsMaxLength
	 *            the argsMaxLength to set
	 */
	public void setArgsMaxLength(int argsMaxLength) {
		this.argsMaxLength = argsMaxLength;
	}

	/**
	 * @param syntaxe
	 *            the syntaxe to set
	 */
	public VCommand setSyntaxe(String syntaxe) {
		this.syntaxe = syntaxe;
		return this;
	}

	public VCommand createInventory(int id, int page, Object... objects) {
		inventory = new IIventory(id, page, objects);
		return this;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public VCommand setPermission(Permission permission) {
		this.permission = permission;
		return this;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public VCommand setParent(VCommand parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * @param consoleCanUse
	 *            the consoleCanUse to set
	 */
	public VCommand setConsoleCanUse(boolean consoleCanUse) {
		this.consoleCanUse = consoleCanUse;
		return this;
	}

	/**
	 * @param ignoreParent
	 *            the ignoreParent to set
	 */
	public VCommand setIgnoreParent(boolean ignoreParent) {
		this.ignoreParent = ignoreParent;
		return this;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	public void setSender(CommandSender sender) {
		this.sender = sender;
		if (sender instanceof Player)
			player = (Player) sender;
	}

	public VCommand setIgnoreArgs(boolean ignoreArgs) {
		this.ignoreArgs = ignoreArgs;
		return this;
	}

	public VCommand setDescription(String description) {
		this.description = description;
		return this;
	}

	//
	// OTHER
	//

	/**
	 * Adds sub orders
	 * 
	 * @param subCommand
	 * @return this
	 */
	public VCommand addSubCommand(String subCommand) {
		this.subCommands.add(subCommand);
		return this;
	}
	
	/**
	 * Adds sub orders
	 * 
	 * @param subCommand
	 * @return this
	 */
	public VCommand addSubCommand(VCommand command) {
		command.setParent(this);
		plugin.getCommandManager().addCommand(command);
		return this;
	}

	/**
	 * Adds sub orders
	 * 
	 * @param subCommand
	 * @return this
	 */
	public VCommand addSubCommand(String... subCommand) {
		this.subCommands.addAll(Arrays.asList(subCommand));
		return this;
	}

	public void msg(String str, Object... args) {
		sender.sendMessage(TextUtil.color(TextUtil.parse(str, args)));
	}

	public void sendMessage(String msg) {
		sender.sendMessage(TextUtil.color(msg));
	}

	public void sendMessage(List<String> msgs) {
		msgs.forEach(msg -> sendMessage(msg));
	}

	/**
	 * method that allows you to execute the command
	 */
	public abstract CommandType perform(Template main);

	public boolean sameSubCommands() {
		if (parent == null)
			return false;
		for (String command : subCommands)
			if (parent.getSubCommands().contains(command))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VCommand [permission=" + permission + ", subCommands=" + subCommands + ", consoleCanUse="
				+ consoleCanUse + ", description=" + description + "]";
	}

}
