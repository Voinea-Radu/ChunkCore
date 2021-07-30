package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.GangMemberData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangPromote extends BSubCommand {
    public CmdGangPromote() {
        this.aliases.add("promote");
        this.correctUsage = "/gang promote <player>";
        this.permission = "gangsplus.gang.promote";
        this.requiredRank = Settings.getRequiredRank("promote");
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
                this.msg(Lang.MSG_GANG_PROMOTE_NOTINGANG.toMsg().replace("%player%", var1.getName()));
            } else {
                GangMemberData var2 = this.gang.getMemberData(var1);
                if (var2.getRank() + 1 == Settings.getHighestRank()) {
                    this.msg(Lang.MSG_GANG_PROMOTE_LEADER.toMsg().replace("%player%", var1.getName()));
                } else if (var2.getRank() == Settings.getHighestRank()) {
                    this.msg(Lang.MSG_GANG_PROMOTE_HIGHEST.toMsg().replace("%player%", var1.getName()));
                } else {
                    var2.setRank(var2.getRank() + 1);
                    this.gang.save();
                    this.gang.sendMessage(Lang.MSG_GANG_PROMOTE_PROMOTED_INGANG.toString().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                    if (var1.getPlayer() != null) {
                        var1.getPlayer().sendMessage(Lang.MSG_GANG_PROMOTE_PROMOTED_BY.toMsg().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                    }

                    this.msg(Lang.MSG_GANG_PROMOTE_PROMOTED_PROMOTED.toMsg().replace("%target%", var1.getName()).replace("%rank%", Settings.getRankName(var2.getRank())).replace("%player%", this.player.getName()));
                }
            }
        }
    }
}
