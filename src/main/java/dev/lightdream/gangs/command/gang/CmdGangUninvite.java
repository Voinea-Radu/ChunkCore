package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdGangUninvite extends BSubCommand {
    public CmdGangUninvite() {
        this.aliases.add("uninvite");
        this.aliases.add("uninv");
        this.aliases.add("u");
        this.correctUsage = "/gang uninvite <player>";
        this.permission = "gangsplus.gang.uninvite";
        this.requiredRank = Settings.getRequiredRank("uninvite");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            Player var1 = Bukkit.getPlayer(this.args[0]);
            if (var1 == null) {
                this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
            } else if (!this.gang.isInvited(var1)) {
                this.msg(Lang.MSG_GANG_UNINVITE_NOTINVITED.toMsg().replace("%player%", var1.getName()));
            } else {
                this.gang.removeInvitation(var1);
                this.gang.save();
                if (var1.getPlayer() != null) {
                    var1.getPlayer().sendMessage(Lang.MSG_GANG_UNINVITE_UNINVITED_BY.toMsg().replace("%gang%", this.gang.getName()).replace("%player%", this.player.getName()));
                }

                this.msg(Lang.MSG_GANG_UNINVITE_UNINVITED_UNINVITED.toMsg().replace("%gang%", this.gang.getName()).replace("%target%", var1.getName()));
            }
        }
    }
}
