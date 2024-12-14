package x.Entt.Keops;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import x.Entt.Keops.CMDs.Keops;
import x.Entt.Keops.CMDs.ReloadCommand;
import x.Entt.Keops.CMDs.StopCommand;
import x.Entt.Keops.Utils.FileHandler;
import x.Entt.Keops.Utils.Metrics;
import x.Entt.Keops.Utils.Updater;
import x.Entt.Keops.Utils.MSG;

import java.io.File;
import java.util.Objects;

public class K extends JavaPlugin {
    public String version = this.getDescription().getVersion();
    public static String prefix;
    private FileHandler fh;
    int bStats = 23563;

    @Override
    public void onEnable() {
        registerCommands();
        registerFiles();

        fh = new FileHandler(this);

        if (getConfig().getString("prefix") != null) {
            prefix = fh.getConfig().getString("prefix");
        } else {
            prefix = "&4&l[KE&E&lOPS}:";
        }

        Metrics metrics = new Metrics(this, bStats);

        metrics.addCustomChart(new Metrics.SimplePie("server_host", () -> {
            return fh.getConfig().getString("server-client", "other");
        }));

        searchUpdates();

        Bukkit.getConsoleSender().sendMessage(MSG.color
                (prefix + " &av" + version + " &2Enabled!"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MSG.color
                (prefix + " &av" + version + " &cDisabled"));
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("stop")).setExecutor(new StopCommand(this));
        Objects.requireNonNull(getCommand("restart")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(getCommand("keops")).setExecutor(new Keops(this));
    }

    private void registerFiles() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    public void searchUpdates() {
        String downloadUrl = "https://www.spigotmc.org/resources/keops-anti-autostop-plugin-1-8-1-21.120208/";
        TextComponent link = new TextComponent(MSG.color("&e&lClick here to download the update!"));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));

        boolean updateAvailable = false;
        String latestVersion = "unknown";

        try {
            Updater updater = new Updater(this, 115289);
            updateAvailable = updater.isUpdateAvailable();
            latestVersion = updater.getLatestVersion();
        } catch (Exception e) {
            logToConsole("&cError checking for updates: " + e.getMessage());
        }

        if (updateAvailable) {
            logToConsole("&2&l===========================================");
            logToConsole("&6&lNEW VERSION AVAILABLE!");
            logToConsole("&e&lCurrent Version: &f" + version);
            logToConsole("&e&lLatest Version: &f" + latestVersion);
            logToConsole("&e&lDownload it here: &f" + downloadUrl);
            logToConsole("&2&l===========================================");

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("keops.op")) {
                    player.sendMessage(MSG.color(prefix + "&e&lA new plugin update is available!"));
                    player.spigot().sendMessage(link);
                }
            }
        }
    }

    private void logToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(MSG.color(prefix + message));
    }

    public FileHandler getFH() {
        return fh;
    }
}