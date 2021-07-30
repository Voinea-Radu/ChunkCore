package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;

public class CmdGangRegroup extends BSubCommand {
    public CmdGangRegroup() {
        this.aliases.add("regroup");
        this.aliases.add("r");
        this.permission = "gangsplus.gang.regroup";
        this.requiredRank = Settings.getRequiredRank("regroup");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleHomes) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else {
            String var1 = this.args.length < 2 ? "home" : this.args[1];
            if (!this.gang.hasHome(var1)) {
                this.msg(Lang.MSG_GANG_REGROUP_INVALIDHOME.toMsg());
            } else {
                this.gang.sendMessage(Lang.MSG_GANG_REGROUP_INGANG.toString().replace("%player%", this.player.getName()).replace("%home%", var1));
            }
        }
    }
}
