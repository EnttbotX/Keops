package x.Entt.Keops;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import x.Entt.Keops.CMDs.Keops;
import x.Entt.Keops.CMDs.RestartCommand;
import x.Entt.Keops.CMDs.StopCommand;
import x.Entt.Keops.Utils.FileHandler;
import x.Entt.Keops.Utils.Metrics;
import x.Entt.Keops.Utils.Updater;
import x.Entt.Keops.Utils.MSG;

import java.io.File;
import java.util.Objects;

public class K extends JavaPlugin {
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

        updateCheck();

        Bukkit.getConsoleSender().sendMessage(MSG.color
                (prefix + " &av" + getDescription().getVersion() + " &2Enabled!"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MSG.color
                (prefix + " &av" + getDescription().getVersion() + " &cDisabled"));
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("stop")).setExecutor(new StopCommand(this));
        Objects.requireNonNull(getCommand("restart")).setExecutor(new RestartCommand(this));
        Objects.requireNonNull(getCommand("keops")).setExecutor(new Keops(this));
    }

    private void registerFiles() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    private void updateCheck() {
        int resourceId = 118563;
        Updater updater = new Updater(this, resourceId);

        try {
            if (updater.isUpdateAvailable()) {
                getLogger().info(MSG.color(prefix + "&cThere is a new update of the plugin"));
            } else {
                getLogger().info(MSG.color(prefix + "&2Plugin is up to date"));
            }
        } catch (Exception e) {
            getLogger().warning(MSG.color(prefix + "&4&lError checking for updates: " + e.getMessage()));
        }
    }

    public FileHandler getFH() {
        return fh;
    }
}