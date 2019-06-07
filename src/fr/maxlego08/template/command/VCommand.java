package fr.maxlego08.template.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.zcore.utils.TextUtil;
import fr.maxlego08.template.zcore.utils.ZUtils;

public abstract class VCommand extends ZUtils {


	public enum CommandType{
		
		SUCCESS,
		SYNTAX_ERROR,
		EXCEPTION_ERROR,
		DEFAULT;
		
	}
	
	private final VCommand parent;
	private final boolean isNoConsole;
	private final List<String> subCommands;
	
	/**
	 * Contructor Vcommand
	 * 
	 * @param parent Set parent of this VCommand 
	 * @param isNoConsole Set if the console can execute the command
	 * @param subCommands Set list of sub command
	 */
	
	public VCommand(VCommand parent, boolean isNoConsole, List<String> subCommands) {
		this.parent = parent;
		this.isNoConsole = isNoConsole;
		this.subCommands = subCommands;
	}
	/**
	 * Contructor Vcommand
	 * 
	 * @param parent Set parent of this VCommand 
	 * @param isNoConsole Set if the console can execute the command
	 */
	
	public VCommand(VCommand parent, boolean isNoConsole) {
		this(parent, isNoConsole, new ArrayList<>());
	}
	
	/**
	 * Contructor Vcommand
	 * 
	 * @param parent Set parent of this VCommand 
	 */
	public VCommand(VCommand parent){
		this(parent, false);
	}
	
	/**
	 * Contructor Vcommand
	 * 
	 */
	
	public VCommand(){
		this(null);
	}
	
	/**
	 * @return the parent
	 */
	public VCommand getParent() {
		return parent;
	}
	
	/**
	 * @return the isNoConsole
	 */
	public boolean isNoConsole() {
		return isNoConsole;
	}
	
	/**
	 * @return the subCommands
	 */
	public List<String> getSubCommands() {
		return subCommands;
	}

	/**
	 * Add subCommand to subCommands list
	 * 
	 * @param subCommand String
	 * */
	
	public void addSubCommand(String subCommand){
		this.getSubCommands().add(subCommand);
	}
	
	protected abstract CommandType perform(Template main, CommandSender sender, String... args);
	
	public abstract String getPermission();
	
	public abstract String getSyntax();
	
	private CommandSender sender;

	public void setSender(CommandSender sender) {
		this.sender = sender;
	}

	public CommandSender getSender() {
		return sender;
	}

	public void msg(String str, Object... args) {
		sender.sendMessage(TextUtil.color(TextUtil.parse(str, args)));
	}

	public void sendMessage(String msg) {
		sender.sendMessage(TextUtil.color(msg));
	}

	public void sendMessage(List<String> msgs) {
		msgs.forEach(msg -> sendMessage(msg));
	}

	public Player getPlayer() {
		if (sender instanceof Player)
			return (Player) sender;
		throw new IllegalArgumentException("Sender is not a player !");
	}

}
