package fr.maxlego08.template.zcore.utils.commands;

import java.util.function.BiConsumer;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand;

public class ZCommand extends VCommand {

	private BiConsumer<VCommand, Template> command;

	@Override
	public CommandType perform(Template main) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, Template> command) {
		this.command = command;
		return this;
	}

	public VCommand sendHelp(String command) {
		this.command = (cmd, main) -> main.getCommandManager().sendHelp(command, cmd.getSender());
		return this;
	}

}
