package x.Entt.Keops.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import x.Entt.Keops.K;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileHandler {

    private final K plugin;
    private File configFile;
    private FileConfiguration config;

    public FileHandler(K plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");

        reloadConfig();
    }

    /**
     * Reloads the config from the file.
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
        plugin.getLogger().log(Level.INFO, "Config file reloaded.");
    }

    /**
     * Retrieves the current FileConfiguration.
     *
     * @return FileConfiguration instance.
     */
    public FileConfiguration getConfig() {
        if (this.config == null) {
            reloadConfig();
        }
        return this.config;
    }

    /**
     * Saves the current config to the file.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
            plugin.getLogger().log(Level.INFO, "Config file saved.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config file!", e);
        }
    }
}