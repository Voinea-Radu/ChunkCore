package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.GangMemberData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangLeader extends BSubCommand {
    public CmdGangLeader() {
        this.aliases.add("leader");
        this.correctUsage = "/gang leader <player>";
        this.permission = "gangsplus.gang.leader";
        this.requiredRank = Settings.getHighestRank();
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
                this.msg(Lang.MSG_GANG_LEADER_NOTINGANG.toMsg().replace("%player%", var1.getName()));
            } else {
                GangMemberData var2 = this.gang.getMemberData(this.player);
                GangMemberData var3 = this.gang.getMemberData(var1);
                if (var3.getRank() == Settings.getHighestRank()) {
                    this.msg(Lang.MSG_GANG_LEADER_ALREADYLEADER.toMsg().replace("%player%", var1.getName()));
                } else {
                    var3.setRank(Settings.getHighestRank());
                    var2.setRank(Settings.getHighestRank() - 1);
                    this.gang.save();
                    if (Settings.getBroadcast("gangLeaderChange")) {
                        Settings.broadcast(Lang.MSG_GANG_LEADER_CHANGED_BROADCAST.toString().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                    }

                    this.gang.sendMessage(Lang.MSG_GANG_LEADER_CHANGED_INGANG.toString().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                    if (var1.getPlayer() != null) {
                        var1.getPlayer().sendMessage(Lang.MSG_GANG_LEADER_CHANGED_BY.toMsg().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                    }

                    this.msg(Lang.MSG_GANG_LEADER_CHANGED_CHANGED.toMsg().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                }
            }
        }
    }
}
