package fr.maxlego08.template;

import fr.maxlego08.template.command.CommandManager;
import fr.maxlego08.template.inventory.InventoryManager;
import fr.maxlego08.template.listener.AdapterListener;
import fr.maxlego08.template.save.ConfigExample;
import fr.maxlego08.template.zcore.ZPlugin;

public class Template extends ZPlugin{

	private CommandManager commandManager;
	private InventoryManager inventoryManager;
	
	@Override
	public void onEnable() {
		
		preEnable();
		
		commandManager = new CommandManager(this);
		inventoryManager = new InventoryManager(this);
		
		/* Add Listener */
		
		addListener(new AdapterListener(this));
		addListener(inventoryManager);
		
		/* Add Saver */
		
		addSave(new ConfigExample());
		
		getSavers().forEach(saver -> saver.load(getPersist()));
		
		postEnable();
		
	}
	
	@Override
	public void onDisable() {
		
		preDisable();
		
		getSavers().forEach(saver -> saver.save(getPersist()));
		
		postDisable();
		
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
	
}
