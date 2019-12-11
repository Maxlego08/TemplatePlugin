package fr.maxlego08.template.command.commands;

import org.bukkit.Location;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.CommandType;
import fr.maxlego08.template.command.VCommand;

public class CommanndTestSub extends VCommand {

	public CommanndTestSub() {
		this.addSubCommand("proute");
		this.addRequireArg("ta faction");
		this.addOptionalArg("une autre faction");
	}

	@Override
	public CommandType perform(Template main) {

		Location d = argAsLocation(0);

		message(sender, "§2Mon d§3 " + d);

		return CommandType.SUCCESS;
	}

}
