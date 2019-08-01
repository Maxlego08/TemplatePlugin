package fr.maxlego08.template.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand;

public class CommandExamplePaginate extends VCommand {

	public CommandExamplePaginate(VCommand parent, boolean isNoConsole) {
		super(parent, isNoConsole);
		this.addSubCommand("pagination");
	}

	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {
		main.getInventoryManager().createMenu(2, getPlayer(), 1);
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return null;
	}

}
