package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperReload extends VCommand {

	public CommandHopperReload(HopperPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.HOPPER_ADMIN);
		this.setDescription(Message.DESCRIPTION_RELOAD);
		this.addSubCommand("reload", "rl");
	}

	@Override
	protected CommandType perform(HopperPlugin plugin) {

		this.plugin.getHopperManager().reload(this.sender);

		return CommandType.SUCCESS;
	}

}
