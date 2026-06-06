package fr.maxlego08.template.zcore.utils.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CollectionBiConsumer {
	
	/**
	 * Accept consumer
	 * 
	 * @param sender
	 * @param args
	 * @return list of string
	 */
	List<String> accept(CommandSender sender, String[] args);
	
}
