package dev.lightdream.gangs.command.gang;


import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

public class CmdGangAlly extends BSubCommand {
    public CmdGangAlly() {
        this.aliases.add("ally");
        this.aliases.add("a");
        this.correctUsage = "/gang ally <gang>";
        this.permission = "gangsplus.gang.ally";
        this.requiredRank = Settings.getRequiredRank("ally");
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
                    this.msg(Lang.MSG_GANG_ALLY_OWN.toMsg());
                } else if (this.gang.isAlly(var2)) {
                    this.msg(Lang.MSG_GANG_ALLY_ALREADYALLY.toMsg());
                } else {
                    if (this.gang.getAllyRequests().contains(var2)) {
                        this.gang.getAllyRequests().remove(var2);
                        this.gang.save();
                        this.gang.getAllyGangs().add(var2);
                        var2.getAllyGangs().add(this.gang);
                        this.main.getDataManager().addAlliance(this.gang, var2);
                        var2.sendMessage(Lang.MSG_GANG_ALLY_ACCEPTED_BY.toString().replace("%gang%", this.gang.getName()));
                        this.gang.sendMessage(Lang.MSG_GANG_ALLY_ACCEPTED_INGANG.toString().replace("%gang%", var2.getName()));
                        this.msg(Lang.MSG_GANG_ALLY_ACCEPTED_ACCEPTED.toMsg().replace("%gang%", var2.getName()));
                    } else {
                        var2.getAllyRequests().add(this.gang);
                        var2.sendMessage(Lang.MSG_GANG_ALLY_REQUESTED_BY.toString().replace("%gang%", this.gang.getName()));
                        this.gang.sendMessage(Lang.MSG_GANG_ALLY_REQUESTED_INGANG.toString().replace("%gang%", var2.getName()));
                        this.msg(Lang.MSG_GANG_ALLY_REQUESTED_REQUESTED.toMsg().replace("%gang%", var2.getName()));
                    }

                }
            }
        }
    }
}
