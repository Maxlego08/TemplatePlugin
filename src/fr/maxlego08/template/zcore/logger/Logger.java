package fr.maxlego08.template.zcore.logger;

import org.bukkit.Bukkit;

public class Logger {

	private final String prefix;
	private static Logger logger;
	
	public Logger(String prefix) {
		this.prefix = prefix;
		logger = this;
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static void info(String message, LogType type){
		getLogger().log(message, type);
	}
	
	public static void info(String message){
		getLogger().log(message, LogType.INFO);
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public enum LogType{
		ERROR("§c"),
		INFO("§7"),
		WARNING("§6"),
		SUCCESS("§2");
		
		private final String color;
		
		private LogType(String color) {
			this.color = color;
		}
		
		public String getColor() {
			return color;
		}
	}
	
	public void log(String message, LogType type){
		Bukkit.getConsoleSender().sendMessage("§8[§e"+prefix+"§8] " + type.getColor() + getColoredMessage(message));
	}
	
	public void log(String message){
		Bukkit.getConsoleSender().sendMessage("§8[§e"+prefix+"§8] §e" + getColoredMessage(message));
	}
	
	public void log(String message, Object... args){
		log(String.format(message, args));
	}
	
	public void log(String message,  LogType type, Object... args){
		log(String.format(message, args), type);
	}
	
	public void log(String[] messages, LogType type){
		for(String message : messages){
			log(message, type);
		}
	}
	
	public String getColoredMessage(String message){
		return message.replace("<&>", "§");
	}
}
