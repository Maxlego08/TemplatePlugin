package fr.maxlego08.template.command.commands;

import org.bukkit.command.CommandSender;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand;

public class CommandExampleSub extends VCommand {

	public CommandExampleSub(VCommand parent) {
		super(parent);
		this.addSubCommand("test");
	}

	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {

		sendMessage("Yessssssss everything works !");	
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin";
	}

	@Override
	public String getSyntax() {
		return "/example test";
	}

}
