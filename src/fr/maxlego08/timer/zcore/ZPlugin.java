package fr.maxlego08.timer.zcore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
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

import fr.maxlego08.timer.TimerPlugin;
import fr.maxlego08.timer.command.CommandManager;
import fr.maxlego08.timer.command.VCommand;
import fr.maxlego08.timer.exceptions.ListenerNullException;
import fr.maxlego08.timer.inventory.VInventory;
import fr.maxlego08.timer.inventory.ZInventoryManager;
import fr.maxlego08.timer.listener.ListenerAdapter;
import fr.maxlego08.timer.placeholder.LocalPlaceholder;
import fr.maxlego08.timer.placeholder.Placeholder;
import fr.maxlego08.timer.zcore.enums.EnumInventory;
import fr.maxlego08.timer.zcore.logger.Logger;
import fr.maxlego08.timer.zcore.logger.Logger.LogType;
import fr.maxlego08.timer.zcore.utils.gson.LocationAdapter;
import fr.maxlego08.timer.zcore.utils.gson.PotionEffectAdapter;
import fr.maxlego08.timer.zcore.utils.plugins.Plugins;
import fr.maxlego08.timer.zcore.utils.storage.Persist;
import fr.maxlego08.timer.zcore.utils.storage.Saveable;

public abstract class ZPlugin extends JavaPlugin {

	private final Logger log = new Logger(this.getDescription().getFullName());
	private final List<Saveable> savers = new ArrayList<>();
	private final List<ListenerAdapter> listenerAdapters = new ArrayList<>();

	private Gson gson;
	private Persist persist;
	private long enableTime;

	protected CommandManager commandManager;
	protected ZInventoryManager inventoryManager;

	protected void preEnable() {

		LocalPlaceholder.getInstance().setPlugin((TimerPlugin) this);
		
		this.enableTime = System.currentTimeMillis();

		this.log.log("=== ENABLE START ===");
		this.log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

		this.getDataFolder().mkdirs();

		this.gson = getGsonBuilder().create();
		this.persist = new Persist(this);
		
		Placeholder.register();
	}

	protected void postEnable() {

		if (this.inventoryManager != null) {
			this.inventoryManager.sendLog();
		}

		if (this.commandManager != null) {
			this.commandManager.validCommands();
		}

		this.log.log(
				"=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {
		this.enableTime = System.currentTimeMillis();
		this.log.log("=== DISABLE START ===");
	}

	protected void postDisable() {
		this.log.log(
				"=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	/**
	 * Build gson
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
				.registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter(this))
				.registerTypeAdapter(Location.class, new LocationAdapter(this));
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener) {
		if (listener instanceof Saveable)
			this.addSave((Saveable) listener);
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * Add a listener from ListenerAdapter
	 * 
	 * @param adapter
	 */
	public void addListener(ListenerAdapter adapter) {
		if (adapter == null)
			throw new ListenerNullException("Warning, your listener is null");
		if (adapter instanceof Saveable)
			this.addSave((Saveable) adapter);
		this.listenerAdapters.add(adapter);
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
	public ZInventoryManager getInventoryManager() {
		return inventoryManager;
	}

	/**
	 * Check if plugin is enable
	 * 
	 * @param pluginName
	 * @return
	 */
	protected boolean isEnable(Plugins pl) {
		Plugin plugin = getPlugin(pl);
		return plugin == null ? false : plugin.isEnabled();
	}

	/**
	 * Get plugin for plugins enum
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
		commandManager.registerCommand(command, vCommand, Arrays.asList(aliases));
	}

	/**
	 * Register Inventory
	 * 
	 * @param inventory
	 * @param vInventory
	 */
	protected void registerInventory(EnumInventory inventory, VInventory vInventory) {
		inventoryManager.registerInventory(inventory, vInventory);
	}

}
