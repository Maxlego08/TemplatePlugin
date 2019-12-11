package fr.maxlego08.template.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.commands.CommandTest;
import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.enums.Message;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.ZUtils;
import fr.maxlego08.template.zcore.utils.inventory.IIventory;

public class CommandManager extends ZUtils implements CommandExecutor {

	private final Template main;
	private final List<VCommand> commands = new ArrayList<VCommand>();

	public CommandManager(Template template) {
		this.main = template;
	}

	public void registerCommands() {

		addCommand("test", new CommandTest());

		main.getLog().log("Loading " + getUniqueCommand() + " commands", LogType.SUCCESS);
		this.commandChecking();
	}

	public VCommand addCommand(VCommand command) {
		commands.add(command);
		return command;
	}

	public VCommand addCommand(String string, VCommand command) {
		commands.add(command.addSubCommand(string));
		ZPlugin.z().getCommand(string).setExecutor(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (VCommand command : commands) {
			if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
				if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
					CommandType type = processRequirements(command, sender, args);
					if (!type.equals(CommandType.CONTINUE))
						return true;
				}
			} else if (args.length >= 1 && command.getParent() != null
					&& command.getParent().getSubCommands().contains(cmd.getName().toLowerCase())
					&& canExecute(args, cmd.getName().toLowerCase(), command)) {
				CommandType type = processRequirements(command, sender, args);
				if (!type.equals(CommandType.CONTINUE))
					return true;
			}
		}
		message(sender, Message.COMMAND_NO_ARG);
		return true;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @return true if can execute
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command) {
		for (int index = args.length - 1; index > -1; index--) {
			if (command.getSubCommands().contains(args[index].toLowerCase())) {
				if (command.isIgnoreArgs())
					return true;
				if (index < args.length - 1)
					return false;
				return canExecute(args, cmd, command.getParent(), index - 1);
			}
		}
		return false;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @param index
	 * @return
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command, int index) {
		if (index < 0 && command.getSubCommands().contains(cmd.toLowerCase()))
			return true;
		else if (index < 0)
			return false;
		else if (command.getSubCommands().contains(args[index].toLowerCase()))
			return canExecute(args, cmd, command.getParent(), index - 1);
		else
			return false;
	}

	/**
	 * @param command
	 * @param sender
	 * @param strings
	 * @return
	 */
	private CommandType processRequirements(VCommand command, CommandSender sender, String[] strings) {

		if (!(sender instanceof Player) && !command.isConsoleCanUse()) {
			message(sender, Message.COMMAND_NO_CONSOLE);
			return CommandType.DEFAULT;
		}
		if (command.getPermission() == null || hasPermission(sender, command.getPermission())) {
			if (command.getInventory() != null && sender instanceof Player) {
				IIventory iIventory = command.getInventory();
				main.getInventoryManager().createInventory(iIventory.getId(), command.getPlayer(), iIventory.getPage(),
						iIventory.getArgs());
			}
			CommandType returnType = command.prePerform(main, sender, strings);
			if (returnType == CommandType.SYNTAX_ERROR) 
				message(sender, Message.COMMAND_SYNTAXE_ERROR, command.getSyntaxe());
			return returnType;
		}
		message(sender, Message.COMMAND_NO_PERMISSION);
		return CommandType.DEFAULT;
	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private int getUniqueCommand() {
		return (int) commands.stream().filter(command -> command.getParent() == null).count();
	}

	/**
	 * @param commandString
	 * @param sender
	 */
	public void sendHelp(String commandString, CommandSender sender) {
		commands.forEach(command -> {
			if (isValid(command, commandString) && command.getDescription() != null
					&& (command.getPermission() == null || hasPermission(sender, command.getPermission()))) {
				// sender.sendMessage(Lang.commandHelp.replace("%syntaxe%",
				// command.getSyntaxe()).replace("%description%",
				// command.getDescription()));
			}
		});
	}

	public boolean isValid(VCommand command, String commandString) {
		return (command.getSubCommands().contains(commandString)
				|| command.getParent() != null && command.getParent().getSubCommands().contains(commandString));
	}

	/**
	 * Check if your order is ready for use
	 */
	private void commandChecking() {
		commands.forEach(command -> {
			if (command.sameSubCommands()) {
				Logger.info(command.toString() + " command to an argument similar to its parent command !",
						LogType.ERROR);
				ZPlugin.z().getPluginLoader().disablePlugin(ZPlugin.z());
			}
		});
	}

}