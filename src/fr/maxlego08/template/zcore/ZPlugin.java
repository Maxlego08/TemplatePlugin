package fr.maxlego08.template.zcore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.template.listener.ListenerAdapter;
import fr.maxlego08.template.zcore.logger.Logger;
import fr.maxlego08.template.zcore.logger.Logger.LogType;
import fr.maxlego08.template.zcore.utils.storage.Persist;
import fr.maxlego08.template.zcore.utils.storage.Saver;

public abstract class ZPlugin extends JavaPlugin{

	private final String prefix = "§8(§bOldFight§8)";
	private final String arrow = "§6»";
	private final Logger log = new Logger(this.getDescription().getFullName());
	private Gson gson;
	private Persist persist;
	private static ZPlugin plugin;
	private long enableTime;
	private List<Saver> savers = new ArrayList<>();
	private List<ListenerAdapter> listenerAdapters = new ArrayList<>();
	
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
		
		return true;

	}

	protected void postEnable() {

		log.log("=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {

		enableTime = System.currentTimeMillis();
		log.log("=== DISABLE START ===");

	}

	protected void postDisable() {

		log.log("=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}
	
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE);
	}
	
	public void addListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	public void addListener(ListenerAdapter adapter) {
		if (adapter instanceof Saver){
			addSave((Saver)adapter);
		}
		listenerAdapters.add(adapter);
	}

	public void addSave(Saver saver){
		this.savers.add(saver);
	}	
	
	public Logger getLog(){
		return this.log;
	}
	
	public Gson getGson() {
		return gson;
	}
	
	public Persist getPersist() {
		return persist;
	}
	
	public List<Saver> getSavers() {
		return savers;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getArrow() {
		return arrow;
	}
	
	public static ZPlugin z() {
		return plugin;
	}
	
	public List<ListenerAdapter> getListenerAdapters() {
		return listenerAdapters;
	}
	
}
