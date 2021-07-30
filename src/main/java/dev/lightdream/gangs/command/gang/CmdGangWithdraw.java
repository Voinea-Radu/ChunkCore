package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.util.NumberUtils;
import dev.lightdream.gangs.util.StringUtils;

public class CmdGangWithdraw extends BSubCommand {
    public CmdGangWithdraw() {
        this.aliases.add("withdraw");
        this.aliases.add("w");
        this.correctUsage = "/gang withdraw <amount>";
        this.permission = "gangsplus.gang.withdraw";
        this.requiredRank = Settings.getRequiredRank("withdraw");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleBank) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else if (!NumberUtils.isPositiveDouble(this.args[0])) {
            this.msg(Lang.MSG_GANG_WITHDRAW_INVALIDAMOUNT.toMsg());
        } else {
            double var1 = Double.valueOf(this.args[0]);
            if (!this.gang.has(var1)) {
                this.msg(Lang.MSG_GANG_WITHDRAW_CANTAFFORD.toMsg());
            } else {
                this.gang.withdraw(var1);
                this.main.getEconomy().depositPlayer(this.player, var1);
                this.gang.sendMessage(Lang.MSG_GANG_WITHDRAW_WITHDRAWN_INGANG.toString().replace("%player%", this.player.getName()).replace("%amount%", StringUtils.formatDoubleString(var1)));
                this.msg(Lang.MSG_GANG_WITHDRAW_WITHDRAWN_WITHDRAWN.toMsg().replace("%amount%", StringUtils.formatDoubleString(var1)));
            }
        }
    }
}
