package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.util.NumberUtils;
import dev.lightdream.ultraprisoncore.gangs.util.StringUtils;

public class CmdGangDeposit extends BSubCommand {
    public CmdGangDeposit() {
        this.aliases.add("deposit");
        this.aliases.add("dp");
        this.correctUsage = "/gang deposit <amount>";
        this.permission = "gangsplus.gang.deposit";
        this.requiredRank = Settings.getRequiredRank("deposit");
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
            this.msg(Lang.MSG_GANG_DEPOSIT_INVALIDAMOUNT.toMsg());
        } else {
            double var1 = Double.valueOf(this.args[0]);
            if (!this.main.getEconomy().has(this.player, var1)) {
                this.msg(Lang.MSG_GANG_DEPOSIT_CANTAFFORD.toMsg());
            } else {
                this.main.getEconomy().withdrawPlayer(this.player, var1);
                this.gang.deposit(var1);
                this.gang.sendMessage(Lang.MSG_GANG_DEPOSIT_DEPOSITED_INGANG.toString().replace("%player%", this.player.getName()).replace("%amount%", StringUtils.formatDoubleString(var1)));
                this.msg(Lang.MSG_GANG_DEPOSIT_DEPOSITED_DEPOSITED.toMsg().replace("%amount%", StringUtils.formatDoubleString(var1)));
            }
        }
    }
}
