package dev.lightdream.gangs.command.gangadmin;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

public class CmdGangAdminResetGang extends BSubCommand {
    public CmdGangAdminResetGang() {
        this.aliases.add("resetgang");
        this.aliases.add("gangreset");
        this.correctUsage = "/gadmin resetgang <kills|deaths|assists|won|lost|all> <gang>";
        this.permission = "gangsplus.gangadmin.resetgang";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 2) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.args[0].toLowerCase();
            String var2 = this.args[1];
            Gang var3 = this.main.getGangManager().getGang(var2);
            if (var3 == null) {
                this.msg(Lang.MSG_INVALIDGANG.toMsg());
            } else {
                this.reset(var1, var3);
                this.msg(Lang.MSG_GANGADMIN_RESETGANG_RESET.toMsg().replace("%gang%", var3.getName()).replace("%type%", var1));
            }
        }
    }

    private void reset(String var1, Gang var2) {
        if (var1.equalsIgnoreCase("kills")) {
            var2.setKills(0);
        } else if (var1.equalsIgnoreCase("deaths")) {
            var2.setDeaths(0);
        } else if (var1.equalsIgnoreCase("assists")) {
            var2.setAssists(0);
        } else if (var1.equalsIgnoreCase("won")) {
            var2.setFightsWon(0);
        } else if (var1.equalsIgnoreCase("lost")) {
            var2.setFightsLost(0);
        } else {
            if (!var1.equalsIgnoreCase("all")) {
                this.sendCorrectUsage();
                return;
            }

            var2.setKills(0);
            var2.setDeaths(0);
            var2.setAssists(0);
            var2.setFightsWon(0);
            var2.setFightsLost(0);
        }

        var2.save();
    }
}
