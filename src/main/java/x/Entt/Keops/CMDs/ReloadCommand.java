package x.Entt.Keops.CMDs;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import x.Entt.Keops.K;
import x.Entt.Keops.Utils.FileHandler;
import x.Entt.Keops.Utils.MSG;

import java.util.Objects;

import static x.Entt.Keops.K.prefix;

public class ReloadCommand implements CommandExecutor {
    private final K plugin;

    public ReloadCommand(K plugin) {
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
                Bukkit.reload();
                sender.sendMessage(MSG.color(prefix + "&aServer has been reloaded."));
            } else {
                sender.sendMessage(MSG.color(prefix + "&cIncorrect password!"));
            }
            return true;
        }

        if (!player.hasPermission("keops.reload")) {
            sendConfigMessage(player, "messages.no-perms");
            return true;
        }

        if (config.getStringList("data-saved.banned-users").contains(player.getName())) {
            sendConfigMessage(player, "no-more-tries-message");
            return true;
        }

        String configPassword = config.getString("data-saved.password");
        if (configPassword == null || configPassword.isEmpty()) {
            player.sendMessage(MSG.color(prefix + "&cPassword is not set in the config!"));
            return true;
        }

        if (!config.getStringList("data-saved.bypassed-users").contains(player.getName()) &&
                !player.hasPermission(Objects.requireNonNull(config.getString("data-saved.bypassed-perm")))) {

            if (args.length != 1 || !args[0].equals(configPassword)) {
                int remaining = plugin.fam.registerFailedAttempt(player.getName(), config.getInt("max-attempts", 3));
                if (remaining <= 0) {
                    config.getStringList("data-saved.banned-users").add(player.getName());
                    fh.saveConfig();
                    sendConfigMessage(player, "no-more-tries-message");
                } else {
                    String message = MSG.color(prefix + " " + config.getString("messages.incorrect-password"));
                    player.sendMessage(message);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission(Objects.requireNonNull(config.getString("notify-permission")))) {
                            p.sendMessage(MSG.color(prefix + Objects.requireNonNull(fh.getConfig().getString("messages.failed-attempt-recorded")).replace("$user$", player.getName())));
                        }
                    }
                }
                return true;
            }
        }

        plugin.fam.resetAttempts(player.getName());
        Bukkit.reload();
        sender.sendMessage(MSG.color(prefix + "&aServer has been reloaded."));
        return true;
    }

    private void sendConfigMessage(Player player, String configPath) {
        FileHandler fh = plugin.getFH();
        FileConfiguration config = fh.getConfig();
        String message = config.getString(configPath);
        if (message != null) {
            player.sendMessage(MSG.color(prefix + message));
        } else {
            player.sendMessage(MSG.color(prefix + "&cMessage not found in config!"));
        }
    }
}