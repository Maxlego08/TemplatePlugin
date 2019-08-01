package fr.maxlego08.template;

import java.util.ArrayList;
import java.util.List;

import fr.maxlego08.template.command.CommandManager;
import fr.maxlego08.template.inventory.InventoryManager;
import fr.maxlego08.template.listener.AdapterListener;
import fr.maxlego08.template.save.ConfigExample;
import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.utils.inventory.Button;

public class Template extends ZPlugin{

	private CommandManager commandManager;
	private InventoryManager inventoryManager;
	
	private List<PaginationExample> examples = new ArrayList<PaginationExample>();
	private Button next = new Button("§eNext", 360);
	private Button previous = new Button("§ePrevious", 360);
	
	@Override
	public void onEnable() {
		
		preEnable();
		
		commandManager = new CommandManager(this);
		inventoryManager = new InventoryManager(this);
		
		for(int a = 0; a != 147; a++)
			examples.add(new PaginationExample(a));
		
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

	public List<PaginationExample> getExamples() {
		return examples;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
	
	public Button getNext() {
		return next;
	}
	
	public Button getPrevious() {
		return previous;
	}

	
}
