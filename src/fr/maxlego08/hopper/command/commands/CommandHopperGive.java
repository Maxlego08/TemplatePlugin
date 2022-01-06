package fr.maxlego08.hopper.command.commands;

import org.bukkit.OfflinePlayer;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperGive extends VCommand {

	public CommandHopperGive(HopperPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.HOPPER_ADMIN);
		this.setDescription(Message.DESCRIPTION_GIVE);
		this.addSubCommand("give");
		this.addRequireArg("joueur");
		this.addOptionalArg("nombre");
	}

	@Override
	protected CommandType perform(HopperPlugin plugin) {

		OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
		int count = this.argAsInteger(1, 1);
		count = count > 64 ? 64 : count < 1 ? 1 : count;

		if (offlinePlayer == null || !offlinePlayer.isOnline()) {
			message(sender, Message.GIVE_OFFLINE, "%player%", this.argAsString(0));
			return CommandType.SUCCESS;
		}

		this.plugin.getHopperManager().give(sender, player, count);

		return CommandType.SUCCESS;
	}

}
