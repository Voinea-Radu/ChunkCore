package dev.lightdream.friends.gui;

import dev.lightdream.UltraPrisonCore;
import dev.lightdream.friends.UltraPrisonFriends;
import dev.lightdream.utils.Item;
import dev.lightdream.utils.ItemStackUtils;
import dev.lightdream.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsGUI implements GUI {

    private final UltraPrisonCore core;
    private final UltraPrisonFriends module;
    private final UUID player;
    private final int page;

    public FriendsGUI(UltraPrisonCore core, UltraPrisonFriends module, UUID player, int page) {
        this.core = core;
        this.module = module;
        this.player = player;
        this.page = page;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (item == null) {
            return;
        }
        if (item.getType().equals(Material.AIR)) {
            return;
        }

        Object guiProtect = Utils.getNBT(item, "gui_protect");
        Object guiUse = Utils.getNBT(item, "gui_use");

        if (guiProtect != null && (Boolean) guiProtect) {
            event.setCancelled(true);
        }

        if (guiUse != null) {
            switch ((String) guiUse) {
                case "back":
                    player.openInventory(new FriendsGUI(core, module, this.player, page - 1).getInventory());
                    break;
                case "next":
                    player.openInventory(new FriendsGUI(core, module, this.player, page + 1).getInventory());
                    break;
                case "player":
                    UUID friend = UUID.fromString((String) Utils.getNBT(item, "player"));
                    switch (event.getClick()) {
                        case RIGHT:
                            module.getFriendsManager().removeFriend(player, friend);
                            break;
                        case MIDDLE:
                            String command = parse(module.getFriendsGUIConfig().muteCommand, Bukkit.getOfflinePlayer(friend).getName());
                            Bukkit.dispatchCommand(player, command);
                            break;
                    }
                    break;
                case "open_online_players_gui":
                    player.openInventory(new OnlinePlayersGUI(core, module, this.player, 0).getInventory());
                    break;
            }
        }

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 54, Utils.color(module.getFriendsGUIConfig().title));
        Utils.fillInventory(inventory, ItemStackUtils.makeItem(module.getFriendsGUIConfig().fillItem), module.getFriendsGUIConfig().fillItemPositions);

        List<Integer> availablePositions = new ArrayList<>();
        List<UUID> friends = new ArrayList<>();

        inventory.setItem(module.getFriendsGUIConfig().backItem.slot, Utils.setNBT(Utils.setNBT(ItemStackUtils.makeItem(module.getFriendsGUIConfig().backItem), "gui_use", "back"), "gui_protect", true));
        inventory.setItem(module.getFriendsGUIConfig().nextItem.slot, Utils.setNBT(Utils.setNBT(ItemStackUtils.makeItem(module.getFriendsGUIConfig().nextItem), "gui_use", "next"), "gui_protect", true));
        inventory.setItem(module.getFriendsGUIConfig().openOnlinePlayerGUI.slot, Utils.setNBT(Utils.setNBT(ItemStackUtils.makeItem(module.getFriendsGUIConfig().openOnlinePlayerGUI), "gui_use", "open_online_players_gui"), "gui_protect", true));

        for (int i = 0; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                availablePositions.add(i);
            }
        }

        for (int i = availablePositions.size() * page; i < availablePositions.size() * (page + 1); i++) {
            if (core.getPluginDatabase().getFriends(player).size() > i) {
                friends.add(core.getPluginDatabase().getFriends(player).get(i));
            }
        }

        if (page == 0) {
            inventory.setItem(module.getFriendsGUIConfig().backItem.slot, null);
        }

        if (page == core.getPluginDatabase().getFriends(player).size() / availablePositions.size()) {
            inventory.setItem(module.getFriendsGUIConfig().nextItem.slot, null);
        }

        Item friendTemplate = module.getFriendsGUIConfig().playerItem;

        for (int i = 0; i < Math.min(availablePositions.size(), friends.size()); i++) {
            Item head = friendTemplate.clone();
            UUID friend = friends.get(i);

            head.headOwner = Bukkit.getOfflinePlayer(friend).getName();
            head.displayName = parse(head.displayName, head.headOwner);
            head.lore = parse(head.lore, head.headOwner);

            ItemStack item = ItemStackUtils.makeItem(head);
            item = Utils.setNBT(item, "gui_protect", true);
            item = Utils.setNBT(item, "gui_use", "player");
            item = Utils.setNBT(item, "player", friend.toString());

            inventory.setItem(availablePositions.get(i), item);
        }

        return inventory;
    }

    private String parse(String raw, String player) {
        String parsed = raw;

        parsed = parsed.replace("%player%", player);

        return parsed;
    }

    private List<String> parse(List<String> raw, String player) {
        List<String> parsed = new ArrayList<>();

        raw.forEach(line -> parsed.add(parse(line, player)));

        return parsed;
    }

}
