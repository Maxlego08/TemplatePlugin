package fr.maxlego08.hopper.zcore.utils.commands;

import java.util.function.BiConsumer;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;

public class ZCommand extends VCommand {

	public ZCommand(HopperPlugin plugin) {
		super(plugin);
	}

	private BiConsumer<VCommand, HopperPlugin> command;

	@Override
	public CommandType perform(HopperPlugin main) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, HopperPlugin> command) {
		this.command = command;
		return this;
	}

	public VCommand sendHelp(String command) {
		this.command = (cmd, main) -> main.getCommandManager().sendHelp(command, cmd.getSender());
		return this;
	}

}
