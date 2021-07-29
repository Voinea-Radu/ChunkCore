package dev.lightdream.ultraprisoncore.gangs.command.gangadmin;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;

public class CmdGangAdminDisband extends BSubCommand {
    public CmdGangAdminDisband() {
        this.aliases.add("disband");
        this.aliases.add("d");
        this.correctUsage = "/gadmin disband <gang>";
        this.permission = "gangsplus.gangadmin.disband";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
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
                if (this.main.getFightManager().isGangInArena(var2)) {
                    this.msg(Lang.MSG_GANGADMIN_DISBAND_FIGHTING.toMsg());
                } else {
                    this.main.getGangManager().removeGang(var2);
                    if (Settings.getBroadcast("gangDisbandByAdmin")) {
                        Settings.broadcast(Lang.MSG_GANGADMIN_DISBAND_DISBANDED_BROADCAST.toString().replace("%gang%", var2.getName()).replace("%player%", this.getSenderName()));
                    }

                    this.msg(Lang.MSG_GANGADMIN_DISBAND_DISBANDED_DISBANDED.toMsg().replace("%gang%", var2.getName()));
                }
            }
        }
    }
}
