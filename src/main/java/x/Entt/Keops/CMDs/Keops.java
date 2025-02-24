package x.Entt.Keops.CMDs;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import x.Entt.Keops.Utils.KeopsGUI;
import x.Entt.Keops.K;
import x.Entt.Keops.Utils.MSG;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static x.Entt.Keops.K.prefix;

public class Keops implements CommandExecutor, TabCompleter {
    private final K plugin;

    public Keops(K plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        FileConfiguration config = plugin.getFH().getConfig();

        if (args.length < 1) {
            sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops <gui|server-client|log-updates|password|bypass|bypassed-perm|banned-add|banned-remove|banned-clear|notify-permission> [value]"));
            return true;
        }

        if (!sender.hasPermission("keops.op")) {
            sender.sendMessage(MSG.color(prefix + "&cYou don't have permissions to do that!"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "gui":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MSG.color(prefix + " &cThis command can only be used by players."));
                    return true;
                }
                Player player = (Player) sender;
                player.openInventory(new KeopsGUI(plugin).getInventory());
                break;
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
            case "bypassed-perm":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops bypassed-perm <new-permission>"));
                    return true;
                }
                String newBypassPerm = args[1];
                config.set("data-saved.bypassed-perm", newBypassPerm);
                plugin.getFH().saveConfig();
                sender.sendMessage(MSG.color(prefix + " &aBypass permission updated to: " + newBypassPerm));
                break;
            case "banned-add":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops banned-add <player>"));
                    return true;
                }
                List<String> bannedUsersAdd = config.getStringList("data-saved.banned-users");
                if (!bannedUsersAdd.contains(args[1])) {
                    bannedUsersAdd.add(args[1]);
                    config.set("data-saved.banned-users", bannedUsersAdd);
                    plugin.getFH().saveConfig();
                    sender.sendMessage(MSG.color(prefix + " &aPlayer " + args[1] + " added to banned users."));
                } else {
                    sender.sendMessage(MSG.color(prefix + " &cPlayer " + args[1] + " is already banned."));
                }
                break;
            case "banned-remove":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops banned-remove <player>"));
                    return true;
                }
                List<String> bannedUsersRemove = config.getStringList("data-saved.banned-users");
                if (bannedUsersRemove.contains(args[1])) {
                    bannedUsersRemove.remove(args[1]);
                    config.set("data-saved.banned-users", bannedUsersRemove);
                    plugin.getFH().saveConfig();
                    sender.sendMessage(MSG.color(prefix + " &aPlayer " + args[1] + " removed from banned users."));
                } else {
                    sender.sendMessage(MSG.color(prefix + " &cPlayer " + args[1] + " is not in the banned list."));
                }
                break;
            case "banned-clear":
                config.set("data-saved.banned-users", Collections.emptyList());
                plugin.getFH().saveConfig();
                sender.sendMessage(MSG.color(prefix + " &aAll banned users have been cleared."));
                break;
            case "notify-permission":
                if (args.length < 2) {
                    sender.sendMessage(MSG.color(prefix + " &c&lUSE:&f /keops notify-permission <new-permission>"));
                    return true;
                }
                String newNotifyPerm = args[1];
                config.set("notify-permission", newNotifyPerm);
                plugin.getFH().saveConfig();
                sender.sendMessage(MSG.color(prefix + " &aNotify permission updated to: " + newNotifyPerm));
                break;
            default:
                sender.sendMessage(MSG.color(prefix + " &cInvalid option. Use: gui, server-client, log-updates, password, bypass, bypassed-perm, banned-add, banned-remove, banned-clear, or notify-permission."));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, String[] args) {
        if (!sender.hasPermission("keops.op")) {
            return Collections.emptyList();
        }

        List<String> subcommands = Arrays.asList(
                "gui", "server-client", "log-updates", "password", "bypass",
                "bypassed-perm", "banned-add", "banned-remove", "banned-clear", "notify-permission", "reload"
        );

        if (args.length == 1) {
            String current = args[0].toLowerCase();
            return subcommands.stream()
                    .filter(sub -> sub.toLowerCase().startsWith(current))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            String currentArg = args[1].toLowerCase();
            switch (subcommand) {
                case "server-client":
                    return Stream.of("Falix", "Aternos", "Server.Pro", "Otro")
                            .filter(option -> option.toLowerCase().startsWith(currentArg))
                            .collect(Collectors.toList());
                case "log-updates":
                    return Stream.of("true", "false")
                            .filter(option -> option.startsWith(currentArg))
                            .collect(Collectors.toList());
                case "banned-remove":
                    List<String> banned = plugin.getFH().getConfig().getStringList("data-saved.banned-users");
                    return banned.stream()
                            .filter(name -> name.toLowerCase().startsWith(currentArg))
                            .collect(Collectors.toList());
                case "banned-add":
                case "bypass":
                    return Arrays.stream(org.bukkit.Bukkit.getOfflinePlayers())
                            .map(OfflinePlayer::getName)
                            .filter(name -> name != null && name.toLowerCase().startsWith(currentArg))
                            .distinct()
                            .collect(Collectors.toList());
                default:
                    return Collections.emptyList();
            }
        }

        return Collections.emptyList();
    }
}