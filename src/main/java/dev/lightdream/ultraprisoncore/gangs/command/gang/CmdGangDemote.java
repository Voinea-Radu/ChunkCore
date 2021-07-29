package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.gang.GangMemberData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangDemote extends BSubCommand {
    public CmdGangDemote() {
        this.aliases.add("demote");
        this.aliases.add("dm");
        this.correctUsage = "/gang demote <player>";
        this.permission = "gangsplus.gang.demote";
        this.requiredRank = Settings.getRequiredRank("demote");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            OfflinePlayer var1 = Bukkit.getOfflinePlayer(this.args[0]);
            if (var1 == null) {
                this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
            } else if (!this.gang.isMember(var1)) {
                this.msg(Lang.MSG_GANG_DEMOTE_NOTINGANG.toMsg().replace("%player%", var1.getName()));
            } else {
                GangMemberData var2 = this.gang.getMemberData(var1);
                if (var2.getRank() == Settings.getLowestRank()) {
                    this.msg(Lang.MSG_GANG_DEMOTE_LOWEST.toMsg().replace("%player%", var1.getName()));
                } else if (var2.getRank() == Settings.getHighestRank()) {
                    this.msg(Lang.MSG_GANG_DEMOTE_HIGHEST.toMsg().replace("%player%", var1.getName()));
                } else {
                    var2.setRank(var2.getRank() - 1);
                    this.gang.save();
                    this.gang.sendMessage(Lang.MSG_GANG_DEMOTE_DEMOTED_INGANG.toString().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                    if (var1.getPlayer() != null) {
                        var1.getPlayer().sendMessage(Lang.MSG_GANG_DEMOTE_DEMOTED_BY.toMsg().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                    }

                    this.msg(Lang.MSG_GANG_DEMOTE_DEMOTED_DEMOTED.toMsg().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                }
            }
        }
    }
}
