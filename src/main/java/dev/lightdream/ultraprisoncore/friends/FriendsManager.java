package dev.lightdream.ultraprisoncore.friends;

import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import me.lucko.helper.text3.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsManager {

    private final UltraPrisonCore core;
    private final UltraPrisonFriends module;

    public FriendsManager(UltraPrisonCore core, UltraPrisonFriends module) {
        this.core = core;
        this.module = module;
    }

    public void addFriend(Player player, UUID friend) {
        if (friend == player.getUniqueId()) {
            player.sendMessage(Text.colorize(core.getMessages().cannotFriendYourself));
            return;
        }
        if (core.getPluginDatabase().checkFriends(player.getUniqueId(), friend)) {
            player.sendMessage(Text.colorize(core.getMessages().alreadyFriend));
            return;
        }
        int limit = 0;
        for (String permission : module.getFriendsConfig().permissionMap.keySet()) {
            if (player.hasPermission(permission)) {
                limit = module.getFriendsConfig().permissionMap.get(permission);
                break;
            }
        }
        if (core.getPluginDatabase().getFriends(player.getUniqueId()).size() >= limit) {
            player.sendMessage(Text.colorize(core.getMessages().friendsLimit));
            return;
        }
        core.getPluginDatabase().addFriend(player.getUniqueId(), friend);
        player.sendMessage(Text.colorize(parse(core.getMessages().friends, Bukkit.getOfflinePlayer(friend).getName())));
    }

    public void removeFriend(Player player, UUID friend) {
        if (!core.getPluginDatabase().checkFriends(player.getUniqueId(), friend)) {
            player.sendMessage(Text.colorize(core.getMessages().notFriend));
            return;
        }
        core.getPluginDatabase().removeFriend(player.getUniqueId(), friend);
        player.sendMessage(Text.colorize(parse(core.getMessages().notFriends, Bukkit.getOfflinePlayer(friend).getName())));

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
