package fr.maxlego08.template.command;

import java.util.function.Consumer;

import org.bukkit.command.CommandSender;

import fr.maxlego08.template.Template;

public class ZCommand extends VCommand {

	private String permission = null;
	private String syntaxe = null;
	private String description = null;
	private Consumer<VCommand> command;
	
	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {
		if (command != null)
			command.accept(this);
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return permission;
	}

	@Override
	public String getSyntax() {
		return syntaxe;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public ZCommand setCommand(Consumer<VCommand> command){
		this.command = command;
		return this;
	}
	
	public ZCommand setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public ZCommand setSyntaxe(String syntaxe) {
		this.syntaxe = syntaxe;
		return this;
	}
	
	public ZCommand setDescription(String description) {
		this.description = description;
		return this;
	}

}
