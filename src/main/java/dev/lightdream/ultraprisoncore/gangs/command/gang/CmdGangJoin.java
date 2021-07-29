package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerJoinGangEvent;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import org.bukkit.Bukkit;

public class CmdGangJoin extends BSubCommand {
    public CmdGangJoin() {
        this.aliases.add("join");
        this.aliases.add("j");
        this.correctUsage = "/gang join <gang>";
        this.permission = "gangsplus.gang.join";
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = true;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.buildStringFromArgs(0, this.args.length - 1);
            if (!this.main.getGangManager().isGang(var1)) {
                this.msg(Lang.MSG_INVALIDGANG.toMsg());
            } else {
                Gang var2 = this.main.getGangManager().getGang(var1);
                if (!var2.isInvited(this.player)) {
                    this.msg(Lang.MSG_GANG_JOIN_NOTINVITED.toMsg());
                } else {
                    int var3 = Settings.maxMembersUseLevelBased && Settings.maxMembersLevelBased.containsKey(var2.getLevel()) ? (Integer)Settings.maxMembersLevelBased.get(var2.getLevel()) : Settings.maxMembersDefault;
                    if (var2.getMembers().size() >= var3) {
                        this.msg(Lang.MSG_GANG_JOIN_FULL.toMsg().replace("%amount%", this.str(var3)));
                    } else {
                        PlayerJoinGangEvent var4 = new PlayerJoinGangEvent(this.player, var2);
                        Bukkit.getServer().getPluginManager().callEvent(var4);
                        if (!var4.isCancelled()) {
                            var2.removeInvitation(this.player);
                            var2.addMember(this.player, 1);
                            var2.save();
                            if (Settings.getBroadcast("gangJoin")) {
                                Settings.broadcast(Lang.MSG_GANG_JOIN_JOINED_BROADCAST.toString().replace("%player%", this.player.getName()).replace("%gang%", var2.getName()));
                            }

                            var2.sendMessage(Lang.MSG_GANG_JOIN_JOINED_INGANG.toString().replace("%player%", this.player.getName()).replace("%gang%", var2.getName()));
                            this.msg(Lang.MSG_GANG_JOIN_JOINED_JOINED.toMsg().replace("%player%", this.player.getName()).replace("%gang%", var2.getName()));
                        }

                    }
                }
            }
        }
    }
}
