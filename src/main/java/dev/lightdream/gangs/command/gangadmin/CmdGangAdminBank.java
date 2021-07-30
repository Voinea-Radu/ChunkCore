package dev.lightdream.gangs.command.gangadmin;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;
import dev.lightdream.gangs.util.NumberUtils;
import dev.lightdream.gangs.util.StringUtils;

public class CmdGangAdminBank extends BSubCommand {
    public CmdGangAdminBank() {
        this.aliases.add("bank");
        this.aliases.add("b");
        this.correctUsage = "/gadmin bank <bal|give|take|rest> [amount] [gang]";
        this.permission = "gangsplus.gangadmin.bank";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1;
            Gang var2;
            if (!this.args[0].equalsIgnoreCase("bal") && !this.args[0].equalsIgnoreCase("balance")) {
                double var3;
                if (!this.args[0].equalsIgnoreCase("give") && !this.args[0].equalsIgnoreCase("add")) {
                    if (!this.args[0].equalsIgnoreCase("take") && !this.args[0].equalsIgnoreCase("remove")) {
                        if (this.args[0].equalsIgnoreCase("reset")) {
                            if (this.args.length < 2) {
                                this.sendCorrectUsage();
                                return;
                            }

                            var1 = this.buildStringFromArgs(1, this.args.length - 1);
                            if (!this.main.getGangManager().isGang(var1)) {
                                this.msg(Lang.MSG_INVALIDGANG.toMsg());
                                return;
                            }

                            var2 = this.main.getGangManager().getGang(var1);
                            var2.setBankMoney(0.0D, true);
                            this.msg(Lang.MSG_GANGADMIN_BANK_RESET_RESET.toMsg().replace("%gang%", var2.getName()));
                        }
                    } else {
                        if (this.args.length < 3) {
                            this.sendCorrectUsage();
                            return;
                        }

                        var1 = this.buildStringFromArgs(2, this.args.length - 1);
                        if (!this.main.getGangManager().isGang(var1)) {
                            this.msg(Lang.MSG_INVALIDGANG.toMsg());
                            return;
                        }

                        if (!NumberUtils.isPositiveDouble(this.args[1])) {
                            this.msg(Lang.MSG_INVALIDAMOUNT.toMsg());
                            return;
                        }

                        var2 = this.main.getGangManager().getGang(var1);
                        var3 = Double.valueOf(this.args[1]);
                        if (!var2.has(var3)) {
                            this.msg(Lang.MSG_GANGADMIN_BANK_TAKE_TOOMUCH.toMsg().replace("%gang%", var2.getName()).replace("%amount%", StringUtils.formatDoubleString(var3)));
                            return;
                        }

                        var2.withdraw(var3);
                        this.msg(Lang.MSG_GANGADMIN_BANK_TAKE_TAKEN.toMsg().replace("%gang%", var2.getName()).replace("%amount%", StringUtils.formatDoubleString(var3)));
                    }
                } else {
                    if (this.args.length < 3) {
                        this.sendCorrectUsage();
                        return;
                    }

                    var1 = this.buildStringFromArgs(2, this.args.length - 1);
                    if (!this.main.getGangManager().isGang(var1)) {
                        this.msg(Lang.MSG_INVALIDGANG.toMsg());
                        return;
                    }

                    if (!NumberUtils.isPositiveDouble(this.args[1])) {
                        this.msg(Lang.MSG_INVALIDAMOUNT.toMsg());
                        return;
                    }

                    var2 = this.main.getGangManager().getGang(var1);
                    var3 = Double.valueOf(this.args[1]);
                    var2.deposit(var3);
                    this.msg(Lang.MSG_GANGADMIN_BANK_GIVE_GIVEN.toMsg().replace("%gang%", var2.getName()).replace("%amount%", StringUtils.formatDoubleString(var3)));
                }
            } else {
                if (this.args.length < 2) {
                    this.sendCorrectUsage();
                    return;
                }

                var1 = this.buildStringFromArgs(1, this.args.length - 1);
                if (!this.main.getGangManager().isGang(var1)) {
                    this.msg(Lang.MSG_INVALIDGANG.toMsg());
                    return;
                }

                var2 = this.main.getGangManager().getGang(var1);
                this.msg(Lang.MSG_GANGADMIN_BANK_BALANCE_BALANCE.toMsg().replace("%gang%", var2.getName()).replace("%amount%", StringUtils.formatDoubleString(var2.getBankMoney())));
            }

        }
    }
}
