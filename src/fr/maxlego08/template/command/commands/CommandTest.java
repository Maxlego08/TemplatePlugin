package fr.maxlego08.template.command.commands;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.CommandType;
import fr.maxlego08.template.command.VCommand;

public class CommandTest extends VCommand {

	public CommandTest() {
		this.addSubCommand(new CommanndTestSub());
	}
	
	@Override
	public CommandType perform(Template main) {
		message(player, "Test");
		return CommandType.SUCCESS;
	}

}
