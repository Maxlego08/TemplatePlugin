package fr.maxlego08.template.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BarAnimation extends BukkitRunnable {

    private final BossBar bossBar;
    private final double totalTime;
    private double remainingTime;

    /**
     * Creates a new smooth BossBar animation for a group of players.
     *
     * @param players The list of players to display the BossBar to.
     * @param text    The text to display on the BossBar.
     * @param seconds The total duration of the animation in seconds.
     */
    public BarAnimation(List<Player> players, String text, int seconds, BarColor barColor, BarStyle barStyle) {
        this.bossBar = Bukkit.createBossBar(text, barColor, barStyle);
        this.totalTime = seconds * 20.0; // Convert seconds to ticks (20 ticks = 1 second)
        this.remainingTime = totalTime;

        for (Player player : players) {
            this.bossBar.addPlayer(player);
        }

        this.bossBar.setVisible(true);
        // Schedule the task to run every tick (1L) for the smoothest animation
        this.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0L, 1L);
    }

    @Override
    public void run() {
        double progress = remainingTime / totalTime;
        bossBar.setProgress(progress);

        if (remainingTime <= 0) {
            bossBar.removeAll();
            this.cancel(); // Stop the task when the BossBar is empty
        }

        remainingTime -= 1; // Decrease by 1 tick at each update
    }
}
