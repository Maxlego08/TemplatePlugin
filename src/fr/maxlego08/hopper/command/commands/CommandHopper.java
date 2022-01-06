package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopper extends VCommand {

	public CommandHopper(HopperPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.HOPPER_ADMIN);
		this.addSubCommand(new CommandHopperGive(plugin));
		this.addSubCommand(new CommandHopperReload(plugin));
		this.addSubCommand(new CommandHopperConvert(plugin));
		this.addSubCommand(new CommandHopperInfo(plugin));
	}

	@Override
	protected CommandType perform(HopperPlugin plugin) {

		this.subVCommands.forEach(command -> {
			message(sender, Message.COMMAND_SYNTAXE_HELP, "%syntax%", command.getSyntax(), "%description%",
					command.getDescription());
		});

		return CommandType.SUCCESS;
	}

}
