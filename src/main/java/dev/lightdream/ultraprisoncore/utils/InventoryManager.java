package dev.lightdream.ultraprisoncore.utils;

import dev.lightdream.ultraprisoncore.friends.gui.GUI;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class InventoryManager implements Listener {

    private final UltraPrisonCore plugin;

    public InventoryManager(UltraPrisonCore plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof GUI) {
            ((GUI) event.getInventory().getHolder()).onInventoryClick(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() != null && event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof GUI) {
            ((GUI) event.getInventory().getHolder()).onInventoryClose(event);
        }
    }


}
