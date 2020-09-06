package fr.maxlego08.template.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.commands.Arguments;
import fr.maxlego08.template.zcore.utils.commands.CommandType;
import fr.maxlego08.template.zcore.utils.commands.Tab;

public abstract class VCommand extends Arguments {

	/**
	 * Permission used for the command, if it is a null then everyone can
	 * execute the command
	 */
	private String permission;

	/**
	 * Mother command of this command
	 */
	protected VCommand parent;

	/**
	 * Are all sub commands used
	 */
	private List<String> subCommands = new ArrayList<String>();
	protected List<VCommand> subVCommands = new ArrayList<VCommand>();

	private List<String> requireArgs = new ArrayList<String>();
	private List<String> optionalArgs = new ArrayList<String>();

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
	protected boolean DEBUG = false;
	protected boolean runAsync = false;
	private CommandType tabCompleter = CommandType.DEFAULT;

	/**
	 * This is the person who executes the command
	 */
	protected CommandSender sender;
	protected Player player;

	private String syntaxe;
	private String description;
	private int argsMinLength;
	private int argsMaxLength;

	//
	// GETTER
	//

	/**
	 * @return the permission
	 */
	public String getPermission() {
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
		if (syntaxe == null) {
			syntaxe = generateDefaultSyntaxe("");
		}
		return syntaxe;
	}

	public boolean isIgnoreArgs() {
		return ignoreArgs;
	}

	public String getDescription() {
		return description == null ? "no description" : description;
	}

	public CommandType getTabCompleter() {
		return tabCompleter;
	}

	/*
	 * 
	 */
	protected void setTabCompletor() {
		this.tabCompleter = CommandType.SUCCESS;
	}

	//
	// SETTER
	//

	public void setIgnoreArgs(boolean ignoreArgs) {
		this.ignoreArgs = ignoreArgs;
	}

	public void setIgnoreParent(boolean ignoreParent) {
		this.ignoreParent = ignoreParent;
	}

	/**
	 * @param syntaxe
	 *            the syntaxe to set
	 */
	protected VCommand setSyntaxe(String syntaxe) {
		this.syntaxe = syntaxe;
		return this;
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	protected VCommand setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * @param permission
	 *            the permission to set
	 */
	protected VCommand setPermission(Permission permission) {
		this.permission = permission.getPermission();
		return this;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	protected VCommand setParent(VCommand parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * @param consoleCanUse
	 *            the consoleCanUse to set
	 */
	protected VCommand setConsoleCanUse(boolean consoleCanUse) {
		this.consoleCanUse = consoleCanUse;
		return this;
	}

	/**
	 * Mettre la description de la commande
	 * 
	 * @param description
	 * @return
	 */
	protected VCommand setDescription(String description) {
		this.description = description;
		return this;
	}

	/*
	 * Ajouter un argument obligatoire
	 */
	protected void addRequireArg(String message) {
		this.requireArgs.add(message);
		this.ignoreParent = parent == null ? true : false;
		this.ignoreArgs = true;
	}

	/**
	 * Ajouter un argument optionel
	 * 
	 * @param message
	 */
	protected void addOptionalArg(String message) {
		this.optionalArgs.add(message);
		this.ignoreParent = parent == null ? true : false;
		this.ignoreArgs = true;
	}

	/**
	 * Mettre la description de la commande
	 * 
	 * @param description
	 * @return
	 */
	protected VCommand setDescription(Message description) {
		this.description = description.getMessage();
		return this;
	}

	/**
	 * 
	 * @return first command
	 */
	public String getFirst() {
		return subCommands.get(0);
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
		this.subVCommands.add(command);
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

	/**
	 * Permet de générer la syntaxe de la commande manuellement Mais vous pouvez
	 * la mettre vous même avec le setSyntaxe()
	 * 
	 * @param syntaxe
	 * @return generate syntaxe
	 */
	private String generateDefaultSyntaxe(String syntaxe) {

		String tmpString = subCommands.get(0);

		boolean update = syntaxe.equals("");

		if (requireArgs.size() != 0 && update)
			for (String requireArg : requireArgs) {
				requireArg = "<" + requireArg + ">";
				syntaxe += " " + requireArg;
			}
		if (optionalArgs.size() != 0 && update)
			for (String optionalArg : optionalArgs) {
				optionalArg = "[<" + optionalArg + ">]";
				syntaxe += " " + optionalArg;
			}

		tmpString += syntaxe;

		if (parent == null)
			return "/" + tmpString;

		return parent.generateDefaultSyntaxe(" " + tmpString);
	}

	/**
	 * Permet de savoir le nombre de parent de façon récursive
	 * 
	 * @param defaultParent
	 * @return
	 */
	private int parentCount(int defaultParent) {
		return parent == null ? defaultParent : parent.parentCount(defaultParent + 1);
	}

	public CommandType prePerform(Template main, CommandSender commandSender, String[] args) {

		// On met à jour le nombre d'argument en fonction du nombre de parent

		parentCount = parentCount(0);
		argsMaxLength = requireArgs.size() + optionalArgs.size() + parentCount;
		argsMinLength = requireArgs.size() + parentCount;

		// On génère le syntaxe de base s'il y est impossible de la trouver
		if (syntaxe == null)
			syntaxe = generateDefaultSyntaxe("");

		this.args = args;

		String defaultString = argAsString(0);

		if (defaultString != null) {
			for (VCommand subCommand : subVCommands) {
				if (subCommand.getSubCommands().contains(defaultString.toLowerCase()))
					return CommandType.CONTINUE;
			}
		}

		if (argsMinLength != 0 && argsMaxLength != 0
				&& !(args.length >= argsMinLength && args.length <= argsMaxLength)) {
			return CommandType.SYNTAX_ERROR;
		}

		this.sender = commandSender;
		if (sender instanceof Player)
			player = (Player) commandSender;

		try {
			return perform(main);
		} catch (Exception e) {
			if (DEBUG)
				e.printStackTrace();
			return CommandType.SYNTAX_ERROR;
		}
	}

	/**
	 * method that allows you to execute the command
	 */
	protected abstract CommandType perform(Template main);

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

	/**
	 * 
	 * @param plugin
	 * @param sender2
	 * @param args
	 * @return
	 */
	public List<String> toTab(Template plugin, CommandSender sender2, String[] args) {
		return null;
	}

	/**
	 * Generate list for tab completer
	 * 
	 * @param startWith
	 * @param strings
	 * @return
	 */
	protected List<String> generateList(String startWith, String... strings) {
		return generateList(Arrays.asList(strings), startWith);
	}

	/**
	 * Generate list for tab completer
	 * 
	 * @param startWith
	 * @param strings
	 * @return
	 */
	protected List<String> generateList(Tab tab, String startWith, String... strings) {
		return generateList(Arrays.asList(strings), startWith, tab);
	}

	/**
	 * Generate list for tab completer
	 * 
	 * @param defaultList
	 * @param startWith
	 * @return
	 */
	protected List<String> generateList(List<String> defaultList, String startWith) {
		return generateList(defaultList, startWith, Tab.START);
	}

	/**
	 * Generate list for tab completer
	 * 
	 * @param defaultList
	 * @param startWith
	 * @param tab
	 * @return
	 */
	protected List<String> generateList(List<String> defaultList, String startWith, Tab tab) {
		List<String> newList = new ArrayList<>();
		for (String str : defaultList)
			if (startWith.length() == 0
					|| (tab.equals(Tab.START) ? str.toLowerCase().startsWith(startWith.toLowerCase())
							: str.toLowerCase().contains(startWith.toLowerCase())))
				newList.add(str);
		return newList.size() == 0 ? null : newList;
	}

}
