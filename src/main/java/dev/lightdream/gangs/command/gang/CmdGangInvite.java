package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdGangInvite extends BSubCommand {
    public CmdGangInvite() {
        this.aliases.add("invite");
        this.aliases.add("inviteplayer");
        this.aliases.add("inv");
        this.correctUsage = "/gang invite <player>";
        this.permission = "gangsplus.gang.invite";
        this.requiredRank = Settings.getRequiredRank("invite");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            int var1 = Settings.maxMembersUseLevelBased && Settings.maxMembersLevelBased.containsKey(this.gang.getLevel()) ? (Integer)Settings.maxMembersLevelBased.get(this.gang.getLevel()) : Settings.maxMembersDefault;
            if (this.gang.getMembers().size() >= var1) {
                this.msg(Lang.MSG_GANG_INVITE_FULL.toMsg().replace("%amount%", this.str(var1)));
            } else {
                Player var2 = Bukkit.getPlayer(this.args[0]);
                if (var2 == null) {
                    this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
                } else if (this.main.getGangManager().isInGang(var2)) {
                    this.msg(Lang.MSG_GANG_INVITE_ALREADYINGANG.toMsg().replace("%player%", var2.getName()));
                } else if (this.gang.isInvited(var2)) {
                    this.msg(Lang.MSG_GANG_INVITE_ALREADYINVITED.toMsg().replace("%player%", var2.getName()));
                } else {
                    this.gang.addInvitation(var2);
                    this.gang.save();
                    var2.sendMessage(Lang.MSG_GANG_INVITE_INVITED_BY.toMsg().replace("%gang%", this.gang.getName()).replace("%player%", this.player.getName()));
                    this.msg(Lang.MSG_GANG_INVITE_INVITED_INVITED.toMsg().replace("%target%", var2.getName()));
                }
            }
        }
    }
}
