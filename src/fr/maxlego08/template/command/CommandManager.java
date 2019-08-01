package fr.maxlego08.template.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand.CommandType;
import fr.maxlego08.template.command.commands.CommandExample;
import fr.maxlego08.template.command.commands.CommandExamplePaginate;
import fr.maxlego08.template.command.commands.CommandExampleSub;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.TextUtil;

public class CommandManager implements CommandExecutor {

	private final Template main;
	private List<VCommand> commands = new ArrayList<>();

	public CommandManager(Template main) {
		this.main = main;

		VCommand command = addCommand("example", new CommandExample());
		addCommand(new CommandExampleSub(command));
		addCommand(new CommandExamplePaginate(command, true));

		main.getLog().log("Loading " + getUniqueCommand() + " commands", LogType.SUCCESS);

	}

	private void addCommand(VCommand command) {
		commands.add(command);
	}

	private VCommand addCommand(String string, VCommand command) {
		commands.add(command);
		main.getCommand(string).setExecutor(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

		for (VCommand vcommand : commands) {
			if (vcommand.getSubCommands().contains(command.getName().toLowerCase())) {
				if (strings.length == 0 && vcommand.getParent() == null) {
					processRequirements(vcommand, sender, strings);
					return true;
				}
			} else if (strings.length != 0 && vcommand.getParent() != null
					&& vcommand.getParent().getSubCommands().contains(command.getName().toLowerCase())) {
				String cmd = strings[0].toLowerCase();
				if (vcommand.getSubCommands().contains(cmd)) {
					processRequirements(vcommand, sender, strings);
					return true;
				}
			}
		}
		sender.sendMessage(TextUtil
				.color(main.getPrefix() + " &cCette commande n'existe pas ou votre syntaxe contient une erreur"));
		return true;
	}

	private void processRequirements(VCommand command, CommandSender sender, String[] strings) {
		if (!(sender instanceof Player) && command.isNoConsole()) {
			sender.sendMessage(
					TextUtil.color(main.getPrefix() + " &eVous devez être un joueur pour faire cette commande"));
			return;
		}
		if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
			command.setSender(sender);
			command.setArgs(strings);
			CommandType returnType = command.perform(main, sender, strings);
			if (returnType == CommandType.SYNTAX_ERROR) {
				sender.sendMessage(
						TextUtil.color(main.getPrefix() + " &eVous devez faire la commande comme ceci &6%command%"
								.replace("%command%", command.getSyntax())));
			}
			return;
		}
		sender.sendMessage(TextUtil.color(main.getPrefix()
				+ " &cVous n'avez pas la permission de faire cette commande ! Vous pouvez en obtenir avec le /boutique, sur le site ou avec l'Hôtel des ventes !"));

	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private int getUniqueCommand() {
		return (int) commands.stream().filter(command -> command.getParent() == null).count();
	}

	public void sendHelp(String commandString, CommandSender sender) {
		commands.forEach(command -> {
			if (command.getParent() != null && command.getParent().getSubCommands().contains(commandString)
					&& command.getDescription() != null) {
				sender.sendMessage("§a» §6" + command.getSyntax() + " §7- §e" + command.getDescription());
			}
		});
	}

}
