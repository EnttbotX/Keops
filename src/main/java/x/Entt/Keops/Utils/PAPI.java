package x.Entt.Keops.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import x.Entt.Keops.K;

public class PAPI extends PlaceholderExpansion {

    private final K plugin;

    public PAPI(K plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "EnttbotX";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "keops";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        FileConfiguration config = plugin.getFH().getConfig();

        if (params.equalsIgnoreCase("time-remaining")) {
            if (!plugin.countdownManager.isRunning()) {
                return "N/A";
            }
            int remainingTicks = plugin.countdownManager.getRemainingTicks();
            return String.valueOf(remainingTicks);
        }

        if (params.equalsIgnoreCase("attempts-remaining")) {
            int maxAttempts = config.getInt("max-attempts", 3);
            int currentAttempts = plugin.fam.getCurrentAttempts(player.getName());
            int remaining = Math.max(maxAttempts - currentAttempts, 0);
            return String.valueOf(remaining);
        }

        return null;
    }
}