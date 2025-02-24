package x.Entt.Keops.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import x.Entt.Keops.K;

import java.util.Arrays;

public class KeopsGUI implements InventoryHolder {
    private final Inventory inv;
    private final K plugin;

    public KeopsGUI(K plugin) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(this, 9, ChatColor.translateAlternateColorCodes('&', "&4&lKeops Control Panel"));
        initializeItems();
    }

    private void initializeItems() {
        inv.setItem(0, createGuiItem(Material.COMPASS, "&aServer Client", "&7Click to modify server client"));

        inv.setItem(1, createGuiItem(Material.PAPER, "&aLog Updates", "&7Click to toggle log updates"));

        inv.setItem(2, createGuiItem(Material.BOOK, "&aChange Password", "&7Click to change password"));

        inv.setItem(3, createGuiItem(Material.ENCHANTED_BOOK, "&aBypass Users", "&7Click to manage bypassed users"));

        inv.setItem(4, createGuiItem(Material.BARRIER, "&aBanned Users", "&7Click to manage banned users"));

        inv.setItem(5, createGuiItem(Material.NOTE_BLOCK, "&aNotify Permission", "&7Click to change notify permission"));

        inv.setItem(6, createGuiItem(Material.REDSTONE, "&aReload Config", "&7Click to reload configuration"));
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}