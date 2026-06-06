package fr.maxlego08.template.zcore.utils.players;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.template.zcore.utils.interfaces.StringConsumer;

public class BarApi {

	private final Plugin plugin;
	private String message;
	private BarColor color = BarColor.BLUE;
	private BarStyle style = BarStyle.SOLID;
	private BarFlag[] flags = new BarFlag[] {};
	private long delay = 5;
	private boolean addAll = true;
	private StringConsumer<Player> consumer;
	private boolean personnal = false;
	private Player player;

	public BarApi(Plugin plugin, String message, BarColor color, BarStyle style, BarFlag... flags) {
		this(plugin);
		this.message = message;
		this.color = color;
		this.style = style;
		this.flags = flags;
	}

	public BarApi(Plugin plugin) {
		this.plugin = plugin;
	}

	public BarApi(Plugin plugin, String message) {
		this(plugin);
		this.message = message;
	}

	public BarApi delay(long delay) {
		this.delay = delay;
		return this;
	}

	public BarApi color(BarColor color) {
		this.color = color;
		return this;
	}

	public BarApi style(BarStyle style) {
		this.style = style;
		return this;
	}

	public BarApi flags(BarFlag... flags) {
		this.flags = flags;
		return this;
	}

	public BarApi consumer(StringConsumer<Player> consumer) {
		this.consumer = consumer;
		return this;
	}

	public BarApi all() {
		addAll = true;
		return this;
	}

	public BarApi personnal() {
		personnal = true;
		return this;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the color
	 */
	public BarColor getColor() {
		return color;
	}

	/**
	 * @return the style
	 */
	public BarStyle getStyle() {
		return style;
	}

	/**
	 * @return the flags
	 */
	public BarFlag[] getFlags() {
		return flags;
	}

	public void start() {

		if (player != null)
			startPersonnal(player);
		else if (personnal)
			startPersonnal();
		else {
			BossBar bar = Bukkit.createBossBar(message, color, style, flags);
			if (addAll)
				Bukkit.getOnlinePlayers().forEach(tmpPlayer -> bar.addPlayer(tmpPlayer));
			barTask(bar, null);
		}
	}

	private void startPersonnal() {
		Bukkit.getOnlinePlayers().forEach(tmpPlayer -> startPersonnal(tmpPlayer));
	}

	private void startPersonnal(Player player) {

		BossBar bar = Bukkit.createBossBar(consumer != null ? consumer.accept(player) : message, color, style, flags);
		bar.addPlayer(player);
		barTask(bar, () -> bar.setTitle(consumer != null ? consumer.accept(player) : message));

	}

	private void barTask(BossBar bar, Runnable runnable) {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			private double barC = 1.0;

			@Override
			public void run() {

				if (!plugin.isEnabled()) {
					cancel();
					return;
				}

				if (barC <= 0.0) {
					cancel();
					bar.removeAll();
					return;
				}

				if (runnable != null)
					runnable.run();

				bar.setProgress(barC);
				barC -= 0.001;

			}
		}, 0, delay);
	}

	public BarApi user(Player player) {
		this.player = player;
		return this;
	}

}
