package x.Entt.Keops.CMDs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import org.jetbrains.annotations.NotNull;

import x.Entt.Keops.K;
import x.Entt.Keops.Utils.MSG;
import static x.Entt.Keops.K.prefix;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Keops implements CommandExecutor, TabCompleter {
    private final K plugin;

    public Keops(K plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        FileConfiguration config = plugin.getFH().getConfig();

        if (args.length < 1) {
            sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops <server-client|log-updates|password|bypass> [value]"));
            return true;
        }

        if (!sender.hasPermission("keops.op")) {
            sender.sendMessage(MSG.color(prefix + "&cYou don't have permissions to do that!"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "server-client":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops server-client <Falix|Aternos|other>"));
                    return true;
                }

                List<String> validClients = Arrays.asList("Falix", "Aternos", "other");
                String client = args[1];

                if (!validClients.contains(client)) {
                    sender.sendMessage(MSG.color(prefix + " &cInvalid client type. Use Falix, Aternos, or other."));
                    return true;
                }

                config.set("server-client", client);
                plugin.getFH().saveConfig();
                sender.sendMessage(MSG.color(prefix + " &aServer client updated to: " + client));
                break;

            case "reload":
                plugin.getFH().reloadConfig();
                break;

            case "log-updates":
                if (args.length < 2 || (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops log-updates <true|false>"));
                    return true;
                }

                boolean logUpdates = Boolean.parseBoolean(args[1]);
                config.set("log-updates", logUpdates);
                plugin.getFH().saveConfig();
                sender.sendMessage(MSG.color(prefix + " &aLog updates set to: " + logUpdates));
                break;

            case "password":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops password <new-password> (Max 25 characters, alphanumeric only)"));
                    return true;
                }

                String newPassword = args[1];

                if (!Pattern.matches("^[a-zA-Z0-9]+$", newPassword)) {
                    sender.sendMessage(MSG.color(prefix + " &cPassword must be alphanumeric."));
                    return true;
                }

                if (newPassword.length() > 25) {
                    newPassword = newPassword.substring(0, 25);
                    sender.sendMessage(MSG.color(prefix + " &ePassword exceeded 25 characters and was trimmed to: " + newPassword));
                } else {
                    sender.sendMessage(MSG.color(prefix + " &aPassword updated to: " + newPassword));
                }

                config.set("data-saved.password", newPassword);
                plugin.getFH().saveConfig();
                break;

            case "bypass":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops bypass <player>"));
                    return true;
                }

                List<String> bypassedUsers = config.getStringList("data-saved.bypassed-users");

                if (!bypassedUsers.contains(args[1])) {
                    bypassedUsers.add(args[1]);
                    config.set("data-saved.bypassed-users", bypassedUsers);
                    plugin.getFH().saveConfig();
                    sender.sendMessage(MSG.color(prefix + " &aPlayer " + args[1] + " added to bypassed users."));
                } else {
                    sender.sendMessage(MSG.color(prefix + " &cPlayer " + args[1] + " is already bypassed!"));
                }
                break;

            default:
                sender.sendMessage(MSG.color(prefix + " &cInvalid option. Use: server-client, log-updates, password, or bypass."));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!sender.hasPermission("keops.op")) {
            return null;
        }

        if (args.length == 1) {
            return Stream.of("server-client", "log-updates", "password", "bypass", "reload")
                    .filter(option -> option.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("server-client")) {
                return Stream.of("Falix", "Aternos", "other")
                        .filter(client -> client.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("log-updates")) {
                return Stream.of("true", "false")
                        .filter(option -> option.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return null;
    }
}