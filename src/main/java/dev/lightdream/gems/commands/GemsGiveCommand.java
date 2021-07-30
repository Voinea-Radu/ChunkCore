package dev.lightdream.gems.commands;

import com.google.common.collect.ImmutableList;
import dev.lightdream.api.enums.ReceiveCause;
import dev.lightdream.gems.UltraPrisonGems;
import me.lucko.helper.utils.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class GemsGiveCommand extends GemsCommand {

    public GemsGiveCommand(UltraPrisonGems plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, ImmutableList<String> args) {

        if(!sender.isOp()) {
            return false;
        }

        if(args.size() == 2) {
            try {
                long amount = Long.parseLong(args.get(1));
                OfflinePlayer target = Players.getOfflineNullable(args.get(0));
                plugin.getGemsManager().giveGems(target, amount, sender, ReceiveCause.GIVE);
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessage("not_a_number").replace("%input%", String.valueOf(args.get(0))));
            }
        }
        return false;
    }
}