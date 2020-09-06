package fr.maxlego08.template.zcore.utils.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.maxlego08.template.listener.ListenerAdapter;
import fr.maxlego08.template.zcore.logger.Logger;

public class VersionChecker extends ListenerAdapter {

	private final Plugin plugin;

	/**
	 * @param plugin
	 * @param useLastVersion
	 */
	public VersionChecker(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	private boolean useLastVersion = false;

	public void useLastVersion(Plugin plugin) {
		UpdateChecker checker = new UpdateChecker(plugin, 74073);
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		checker.getVersion(version -> {

			long ver = Long.valueOf(version.replace(".", ""));
			long plVersion = Long.valueOf(plugin.getDescription().getVersion().replace(".", ""));
			atomicBoolean.set(plVersion >= ver);
			useLastVersion = atomicBoolean.get();
			if (atomicBoolean.get())
				Logger.info("There is not a new update available.");
			else
				Logger.info("There is a new update available. Your version: " + plugin.getDescription().getVersion()
						+ ", Laste version: " + version);
		});

	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!useLastVersion && event.getPlayer().hasPermission("zplugin.notifs")) {
					message(player,
							"§cYou do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!");
				}
			}
		}.runTaskLater(plugin, 20 * 2);
	}

	public class UpdateChecker {

		private Plugin plugin;
		private int resourceId;

		public UpdateChecker(Plugin plugin, int resourceId) {
			this.plugin = plugin;
			this.resourceId = resourceId;
		}

		/**
		 * 
		 * @param consumer
		 */
		public void getVersion(final Consumer<String> consumer) {
			Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
				try (InputStream inputStream = new URL(
						"https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
						Scanner scanner = new Scanner(inputStream)) {
					if (scanner.hasNext()) {
						consumer.accept(scanner.next());
					}
				} catch (IOException exception) {
					this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
				}
			});
		}
	}

}
