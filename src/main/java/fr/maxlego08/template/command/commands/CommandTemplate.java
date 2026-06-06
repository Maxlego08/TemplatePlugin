package fr.maxlego08.template.command.commands;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.command.VCommand;
import fr.maxlego08.template.zcore.enums.Permission;
import fr.maxlego08.template.zcore.utils.commands.CommandType;

public class CommandTemplate extends VCommand {

	public CommandTemplate(Template plugin) {
		super(plugin);
		this.setPermission(Permission.EXAMPLE_PERMISSION);
		this.addSubCommand(new CommandTemplateReload(plugin));
	}

	@Override
	protected CommandType perform(Template plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
