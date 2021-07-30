package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

public class CmdGangNeutral extends BSubCommand {
    public CmdGangNeutral() {
        this.aliases.add("neutral");
        this.aliases.add("enemy");
        this.aliases.add("n");
        this.correctUsage = "/gang neutral <gang>";
        this.permission = "gangsplus.gang.neutral";
        this.requiredRank = Settings.getRequiredRank("neutral");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleAlliances) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.buildStringFromArgs(0, this.args.length - 1);
            if (!this.main.getGangManager().isGang(var1)) {
                this.msg(Lang.MSG_INVALIDGANG.toMsg());
            } else {
                Gang var2 = this.main.getGangManager().getGang(var1);
                if (this.gang.equals(var2)) {
                    this.msg(Lang.MSG_GANG_NEUTRAL_OWN.toMsg());
                } else if (!this.gang.isAlly(var2)) {
                    this.msg(Lang.MSG_GANG_NEUTRAL_NOTALLY.toMsg().replace("%gang%", var2.getName()));
                } else {
                    this.gang.getAllyGangs().remove(var2);
                    var2.getAllyGangs().remove(this.gang);
                    this.main.getDataManager().removeAlliance(this.gang, var2);
                    var2.sendMessage(Lang.MSG_GANG_NEUTRAL_NEUTRAL_INGANG.toString().replace("%gang%", this.gang.getName()));
                    this.gang.sendMessage(Lang.MSG_GANG_NEUTRAL_NEUTRAL_INGANG.toString().replace("%gang%", var2.getName()));
                    this.msg(Lang.MSG_GANG_NEUTRAL_NEUTRAL_NEUTRAL.toMsg().replace("%gang%", var2.getName()));
                }
            }
        }
    }
}
