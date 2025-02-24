package x.Entt.Keops.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import x.Entt.Keops.K;
import x.Entt.Keops.Utils.MSG;

public class Events implements Listener {
    private final K plugin;

    public Events(K plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        FileConfiguration config = plugin.getFH().getConfig();
        String prefix = config.getString("messages.prefix", "&4&l[KE&E&lOPS]:");

        String guiTitle = MSG.color("&4&lKeops Control Panel");
        if (!event.getView().getTitle().equals(guiTitle)) {
            return;
        }
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        switch (slot) {
            case 0:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-server-client", "&aServer Client clicked.")));
                break;
            case 1:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-log-updates", "&aLog Updates clicked.")));
                break;
            case 2:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-change-password", "&aChange Password clicked.")));
                break;
            case 3:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-manage-bypass", "&aManage Bypass Users clicked.")));
                break;
            case 4:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-manage-banned", "&aManage Banned Users clicked.")));
                break;
            case 5:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-change-notify", "&aChange Notify Permission clicked.")));
                break;
            case 6:
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-reload-config", "&aReload Config clicked.")));
                plugin.getFH().reloadConfig();
                player.sendMessage(MSG.color(prefix + config.getString("messages.gui-config-reloaded", "&aConfiguration reloaded.")));
                break;
            default:
                break;
        }
    }
}