package fr.maxlego08.timer;

import fr.maxlego08.timer.command.CommandManager;
import fr.maxlego08.timer.save.Config;
import fr.maxlego08.timer.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * https://github.com/Maxlego08/TemplatePlugin
 * 
 * @author Maxlego08
 *
 */
public class TimerPlugin extends ZPlugin {


	@Override
	public void onEnable() {

		new TimerHelper();
		
		this.preEnable();

		this.commandManager = new CommandManager(this);
		// this.inventoryManager = new ZInventoryManager(this);

		/* Add Listener */

		// this.addListener(new AdapterListener(this));
		// this.addListener(inventoryManager);

		/* Add Saver */
		this.addSave(Config.getInstance());
		// this.addSave(new MessageLoader(this));
		// addSave(new CooldownBuilder());

		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

		this.postEnable();
	}

	@Override
	public void onDisable() {

		this.preDisable();

		this.getSavers().forEach(saver -> saver.save(this.getPersist()));

		this.postDisable();

	}

}
