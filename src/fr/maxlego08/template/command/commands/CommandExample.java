package fr.maxlego08.template.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand;
import fr.maxlego08.template.save.ConfigExample;

public class CommandExample extends VCommand {

	public CommandExample() {
		super(null, true);
		addSubCommand("example");
	}
	
	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {
		main.getInventoryManager().createMenu(1, getPlayer(), 1);
		sendMessage(ConfigExample.version);
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public String getSyntax() {
		return null;
	}

}
