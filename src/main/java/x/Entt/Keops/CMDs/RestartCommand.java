package x.Entt.Keops.CMDs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import static org.bukkit.Bukkit.getServer;

import org.jetbrains.annotations.NotNull;

import x.Entt.Keops.K;
import x.Entt.Keops.Utils.FileHandler;
import x.Entt.Keops.Utils.MSG;
import static x.Entt.Keops.K.prefix;

public class RestartCommand implements CommandExecutor {
    private final K plugin;

    public RestartCommand(K plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        boolean isPlayer = sender instanceof Player;
        Player player = isPlayer ? (Player) sender : null;
        FileHandler fh = plugin.getFH();
        FileConfiguration config = fh.getConfig();

        if (!isPlayer) {
            if (args.length == 1 && args[0].equals(config.getString("data-saved.password"))) {
                getServer().spigot().restart();
            } else {
                sender.sendMessage(MSG.color(prefix + " &cIncorrect password!"));
            }
            return true;
        }

        if (!player.hasPermission("keops.restart")) {
            sendConfigMessage(player, "messages.no-perms");
            return true;
        }

        if (!config.getStringList("data-saved.bypassed-users").contains(player.getName())) {
            String configPassword = config.getString("data-saved.password");

            if (configPassword == null || configPassword.isEmpty()) {
                player.sendMessage(MSG.color(prefix + " &cPassword is not set in the config!"));
                return true;
            }

            if (args.length != 1 || !args[0].equals(configPassword)) {
                sendConfigMessage(player, "messages.incorrect-password");
                return true;
            }
        }

        getServer().spigot().restart();
        return true;
    }

    private void sendConfigMessage(Player player, String configPath) {
        FileHandler fh = plugin.getFH();
        FileConfiguration config = fh.getConfig();
        String message = config.getString(configPath);

        if (message != null) {
            player.sendMessage(MSG.color(message.replace("$prefix$", prefix)));
        } else {
            player.sendMessage(MSG.color(prefix + " &cMessage not found in config!"));
        }
    }
}