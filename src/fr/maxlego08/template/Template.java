package fr.maxlego08.template;

import fr.maxlego08.template.command.CommandManager;
import fr.maxlego08.template.inventory.InventoryManager;
import fr.maxlego08.template.listener.AdapterListener;
import fr.maxlego08.template.save.Config;
import fr.maxlego08.template.zcore.ZPlugin;
import fr.maxlego08.template.zcore.utils.builder.CooldownBuilder;

public class Template extends ZPlugin {

	private CommandManager commandManager;
	private InventoryManager inventoryManager;

	@Override
	public void onEnable() {

		instance = this;
		
		preEnable();

		commandManager = new CommandManager(this);
		commandManager.registerCommands();
		
		if (!isEnabled())
			return;
		inventoryManager = InventoryManager.getInstance();

		/* Add Listener */

		addListener(new AdapterListener(this));
		addListener(inventoryManager);

		/* Add Saver */

		addSave(Config.getInstance());
		addSave(new CooldownBuilder());

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

	/**
	 * static Singleton instance.
	 */
	private static volatile Template instance;


	/**
	 * Return a singleton instance of Template.
	 */
	public static Template getInstance() {
		return instance;
	}
	
}
