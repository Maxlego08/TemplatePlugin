package fr.maxlego08.template.command.commands;

import org.bukkit.Location;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.CommandType;
import fr.maxlego08.template.command.VCommand;

public class CommanndTestSub extends VCommand {

	public CommanndTestSub() {
		this.addSubCommand("proute");
		this.addRequireArg("location");
	}

	@Override
	public CommandType perform(Template main) {

		Location location = argAsLocation(0);
		player.teleport(location);
		
		return CommandType.SUCCESS;
	}

}
