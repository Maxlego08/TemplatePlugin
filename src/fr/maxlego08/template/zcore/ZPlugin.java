package fr.maxlego08.template.zcore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.template.command.CommandManager;
import fr.maxlego08.template.command.VCommand;
import fr.maxlego08.template.inventory.InventoryManager;
import fr.maxlego08.template.inventory.VInventory;
import fr.maxlego08.template.listener.ListenerAdapter;
import fr.maxlego08.template.zcore.enums.EnumInventory;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.gson.LocationAdapter;
import fr.maxlego08.template.zcore.utils.gson.PotionEffectAdapter;
import fr.maxlego08.template.zcore.utils.plugins.Plugins;
import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saveable;
import net.milkbowl.vault.economy.Economy;

public abstract class ZPlugin extends JavaPlugin {

	private final Logger log = new Logger(this.getDescription().getFullName());
	private Gson gson;
	private Persist persist;
	private static ZPlugin plugin;
	private long enableTime;
	private List<Saveable> savers = new ArrayList<>();
	private List<ListenerAdapter> listenerAdapters = new ArrayList<>();
	private Economy economy = null;

	protected CommandManager commandManager;
	protected InventoryManager inventoryManager;

	public ZPlugin() {
		plugin = this;
	}

	protected boolean preEnable() {

		enableTime = System.currentTimeMillis();

		log.log("=== ENABLE START ===");
		log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

		getDataFolder().mkdirs();

		gson = getGsonBuilder().create();
		persist = new Persist(this);

		if (getPlugin(Plugins.VAULT) != null)
			economy = getProvider(Economy.class);

		return true;

	}

	protected void postEnable() {

		if (inventoryManager != null)
			inventoryManager.sendLog();

		if (commandManager != null)
			commandManager.registerCommands();

		log.log("=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {

		enableTime = System.currentTimeMillis();
		log.log("=== DISABLE START ===");

	}

	protected void postDisable() {

		log.log("=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	/**
	 * Build gson
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
				.registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter())
				.registerTypeAdapter(Location.class, new LocationAdapter());
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener) {
		if (listener instanceof Saveable)
			addSave((Saveable) listener);
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * Add a listener from ListenerAdapter
	 * 
	 * @param adapter
	 */
	public void addListener(ListenerAdapter adapter) {
		if (adapter instanceof Saveable)
			addSave((Saveable) adapter);
		listenerAdapters.add(adapter);
	}

	/**
	 * Add a Saveable
	 * 
	 * @param saver
	 */
	public void addSave(Saveable saver) {
		this.savers.add(saver);
	}

	/**
	 * Get logger
	 * 
	 * @return loggers
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Get gson
	 * 
	 * @return {@link Gson}
	 */
	public Gson getGson() {
		return gson;
	}

	public Persist getPersist() {
		return persist;
	}

	/**
	 * Get all saveables
	 * 
	 * @return savers
	 */
	public List<Saveable> getSavers() {
		return savers;
	}

	public static ZPlugin z() {
		return plugin;
	}

	/**
	 * 
	 * @param classz
	 * @return
	 */
	protected <T> T getProvider(Class<T> classz) {
		RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
		if (provider == null) {
			log.log("Unable to retrieve the provider " + classz.toString(), LogType.WARNING);
			return null;
		}
		return provider.getProvider() != null ? (T) provider.getProvider() : null;
	}

	public Economy getEconomy() {
		return economy;
	}

	/**
	 * 
	 * @return listenerAdapters
	 */
	public List<ListenerAdapter> getListenerAdapters() {
		return listenerAdapters;
	}

	/**
	 * @return the commandManager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * @return the inventoryManager
	 */
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	/**
	 * 
	 * @param pluginName
	 * @return
	 */
	protected boolean isEnable(Plugins pl) {
		Plugin plugin = getPlugin(pl);
		return plugin == null ? false : plugin.isEnabled();
	}

	/**
	 * 
	 * @param pluginName
	 * @return
	 */
	protected Plugin getPlugin(Plugins plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin.getName());
	}

	/**
	 * Register command
	 * 
	 * @param command
	 * @param vCommand
	 * @param aliases
	 */
	protected void registerCommand(String command, VCommand vCommand, String... aliases) {
		commandManager.registerCommand(command, vCommand, aliases);
	}

	/**
	 * Register Inventory
	 * 
	 * @param inventory
	 * @param vInventory
	 */
	protected void registerInventory(EnumInventory inventory, VInventory vInventory) {
		inventoryManager.addInventory(inventory, vInventory);
	}

}
