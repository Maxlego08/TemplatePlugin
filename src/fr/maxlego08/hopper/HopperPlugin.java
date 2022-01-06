package fr.maxlego08.hopper;

import fr.maxlego08.hopper.command.CommandManager;
import fr.maxlego08.hopper.command.commands.CommandHopper;
import fr.maxlego08.hopper.inventory.InventoryManager;
import fr.maxlego08.hopper.listener.AdapterListener;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.save.MessageLoader;
import fr.maxlego08.hopper.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * https://github.com/Maxlego08/TemplatePlugin
 * 
 * @author Maxlego08
 *
 */
public class HopperPlugin extends ZPlugin {

	private final HopperManager hopperManager = new HopperManager(this);
	private final HopperBoard hopperBoard = new HopperBoard();

	@Override
	public void onEnable() {

		this.preEnable();

		this.commandManager = new CommandManager(this);
		this.inventoryManager = new InventoryManager(this);

		this.registerCommand("fshopper", new CommandHopper(this), "hopper", "ho", "fsh");

		this.saveDefaultConfig();

		/* Add Listener */

		this.addListener(new AdapterListener(this));
		this.addListener(inventoryManager);
		this.addListener(new HopperListener(this.hopperManager));

		/* Add Saver */
		this.addSave(Config.getInstance());
		this.addSave(new MessageLoader(this));
		this.addSave(this.hopperManager);
		// addSave(new CooldownBuilder());

		this.hopperBoard.load(this.getPersist());
		this.getSavers().forEach(saver -> saver.load(this.getPersist()));

		this.postEnable();
	}

	@Override
	public void onDisable() {

		this.preDisable();

		this.hopperBoard.save(this.getPersist());
		this.getSavers().forEach(saver -> saver.save(this.getPersist()));

		this.postDisable();

	}

	public HopperManager getHopperManager() {
		return hopperManager;
	}

	public HopperBoard getHopperBoard() {
		return hopperBoard;
	}
}
