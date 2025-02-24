package x.Entt.Keops.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class CountdownManager {
    private final JavaPlugin plugin;
    private int remainingTicks;
    private BukkitTask task;
    private boolean running = false;

    public CountdownManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startCountdown(int ticks, String head, String countdownFormat) {
        if (running) {
            return;
        }
        remainingTicks = ticks;
        running = true;
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (remainingTicks <= 0) {
                Bukkit.broadcastMessage(MSG.color(head + " " + countdownFormat.replace("%keops-time-remaining%", "0")));
                stopCountdown();
                Bukkit.shutdown();
            } else {
                Bukkit.broadcastMessage(MSG.color(head + " " + countdownFormat.replace("%keops-time-remaining%", String.valueOf(remainingTicks))));
                remainingTicks--;
            }
        }, 0L, 20L);
    }

    public void stopCountdown() {
        if (task != null) {
            task.cancel();
        }
        running = false;
    }

    public int getRemainingTicks() {
        return running ? remainingTicks : 0;
    }

    public boolean isRunning() {
        return running;
    }
}