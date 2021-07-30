package dev.lightdream.gangs.command.fight;


import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.util.NumberUtils;
import dev.lightdream.gangs.util.StringUtils;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

public class CmdFightChallenge extends BSubCommand {
    public CmdFightChallenge() {
        this.aliases.add("challenge");
        this.aliases.add("c");
        this.correctUsage = "/fight challenge <players amount> <money> <gang>";
        this.permission = "gangsplus.fight.challenge";
        this.requiredRank = Settings.getRequiredRank("fightChallenge");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 3) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.buildStringFromArgs(2, this.args.length - 1);
            if (!this.main.getGangManager().isGang(var1)) {
                this.msg(Lang.MSG_INVALIDGANG.toMsg());
            } else {
                Gang var2 = this.main.getGangManager().getGang(var1);
                if (this.gang.equals(var2)) {
                    this.msg(Lang.MSG_FIGHT_CHALLENGE_OWN.toMsg());
                } else if (Settings.enableModuleAlliances && !Settings.alliancesAllowFights && this.gang.isAlly(var2)) {
                    this.msg(Lang.MSG_FIGHT_CHALLENGE_ALLY.toMsg());
                } else if (this.main.getFightManager().isChallenged(var2, this.gang)) {
                    this.msg(Lang.MSG_FIGHT_CHALLENGE_ALREADYCHALLENGED_ENEMY.toMsg().replace("%gang%", var2.getName()));
                } else if (this.main.getFightManager().isChallenged(this.gang, var2)) {
                    this.msg(Lang.MSG_FIGHT_CHALLENGE_ALREADYCHALLENGED_OWN.toMsg().replace("%gang%", var2.getName()));
                } else if (!NumberUtils.isPositiveInteger(this.args[0])) {
                    this.msg(Lang.MSG_FIGHT_CHALLENGE_INVALID_MEMBERS.toMsg());
                } else {
                    int var3 = Integer.valueOf(this.args[0]);
                    if (var3 >= Settings.fightsMinMembersAmount && var3 <= Settings.fightsMaxMembersAmount) {
                        if (var3 > this.gang.getOnlineMembers().size()) {
                            this.msg(Lang.MSG_FIGHT_CHALLENGE_NOTENOUGHMEMBERS.toMsg());
                        } else if (!NumberUtils.isPositiveDouble(this.args[1])) {
                            this.msg(Lang.MSG_INVALIDAMOUNT.toMsg());
                        } else {
                            double var4 = Double.valueOf(this.args[1]);
                            if (!(var4 < Settings.fightsMinMoneyAmount) && !(var4 > Settings.fightsMaxMoneyAmount)) {
                                if (Settings.enableModuleBank && var4 > this.gang.getBankMoney()) {
                                    this.msg(Lang.MSG_FIGHT_CHALLENGE_NOTENOUGHMONEY.toMsg());
                                } else {
                                    if (!Settings.enableModuleBank) {
                                        if (!this.main.getEconomy().has(this.player, var4)) {
                                            this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHPRIVATEMONEY.toMsg());
                                            return;
                                        }

                                        this.main.getEconomy().withdrawPlayer(this.player, var4);
                                        this.msg(Lang.MSG_FIGHT_ACCEPT_PAIDPRIVATEMONEY.toMsg().replace("%amount%", StringUtils.formatDoubleString(var4)));
                                    }

                                    this.main.getFightManager().addChallenge(this.gang, var2, var3, var4);
                                    var2.sendMessage(Lang.MSG_FIGHT_CHALLENGE_CHALLENGED_BY.toString().replace("%gang%", this.gang.getName()).replace("%membersamount%", this.str(var3)).replace("%amount%", StringUtils.formatDoubleString(var4)));
                                    this.gang.sendMessage(Lang.MSG_FIGHT_CHALLENGE_CHALLENGED_INGANG.toString().replace("%gang%", var2.getName()).replace("%membersamount%", this.str(var3)).replace("%amount%", StringUtils.formatDoubleString(var4)));
                                    this.msg(Lang.MSG_FIGHT_CHALLENGE_CHALLENGED_CHALLENGED.toMsg().replace("%gang%", var2.getName()).replace("%membersamount%", this.str(var3)).replace("%amount%", StringUtils.formatDoubleString(var4)));
                                }
                            } else {
                                this.msg(Lang.MSG_FIGHT_CHALLENGE_MONEY_RANGE.toMsg().replace("%min%", StringUtils.formatDoubleString(Settings.fightsMinMoneyAmount)).replace("%max%", StringUtils.formatDoubleString(Settings.fightsMaxMoneyAmount)));
                            }
                        }
                    } else {
                        this.msg(Lang.MSG_FIGHT_CHALLENGE_MEMBERS_RANGE.toMsg().replace("%min%", this.str(Settings.fightsMinMembersAmount)).replace("%max%", this.str(Settings.fightsMaxMembersAmount)));
                    }
                }
            }
        }
    }
}
