package dev.lightdream.ultraprisoncore.gangs.command.fight;


import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArena;
import dev.lightdream.ultraprisoncore.gangs.fight.FightChallenge;
import dev.lightdream.ultraprisoncore.gangs.util.StringUtils;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;

public class CmdFightAccept extends BSubCommand {
    public CmdFightAccept() {
        this.aliases.add("accept");
        this.aliases.add("a");
        this.correctUsage = "/fight accept <gang>";
        this.permission = "gangsplus.fight.accept";
        this.requiredRank = Settings.getRequiredRank("fightAccept");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
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
                if (this.gang.equals(var2)) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_OWN.toMsg());
                } else if (Settings.enableModuleAlliances && !Settings.alliancesAllowFights && this.gang.isAlly(var2)) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_ALLY.toMsg());
                } else if (!this.main.getFightManager().isChallenged(var2, this.gang)) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_NOTCHALLENGED.toMsg().replace("%gang%", var2.getName()));
                } else if (this.main.getFightManager().isGangInArena(this.gang)) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_ALREADYFIGHTING_OWN.toMsg());
                } else if (this.main.getFightManager().isGangInArena(var2)) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_ALREADYFIGHTING_ENEMY.toMsg().replace("%gang%", var2.getName()));
                } else if (!this.main.getFightManager().isEmptyArena()) {
                    this.msg(Lang.MSG_FIGHT_ACCEPT_NOEMPTYARENA.toMsg());
                } else {
                    FightChallenge var3 = this.main.getFightManager().getChallenge(this.gang, var2);
                    if (var2.getOnlineMembers().size() < var3.getMembers()) {
                        this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHMEMBERS_ENEMY.toMsg().replace("%gang%", var2.getName()));
                    } else if (Settings.enableModuleBank && var2.getBankMoney() < var3.getMoney()) {
                        this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHMONEY_ENEMY.toMsg().replace("%gang%", var2.getName()));
                    } else if (this.gang.getOnlineMembers().size() < var3.getMembers()) {
                        this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHMEMBERS_OWN.toMsg());
                    } else if (Settings.enableModuleBank && this.gang.getBankMoney() < var3.getMoney()) {
                        this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHMONEY_OWN.toMsg().replace("%amount%", StringUtils.formatDoubleString(var3.getMoney())));
                    } else {
                        if (!Settings.enableModuleBank) {
                            if (!this.main.getEconomy().has(this.player, var3.getMoney())) {
                                this.msg(Lang.MSG_FIGHT_ACCEPT_NOTENOUGHPRIVATEMONEY.toMsg());
                                return;
                            }

                            this.main.getEconomy().withdrawPlayer(this.player, var3.getMoney());
                            this.msg(Lang.MSG_FIGHT_ACCEPT_PAIDPRIVATEMONEY.toMsg().replace("%amount%", StringUtils.formatDoubleString(var3.getMoney())));
                        }

                        FightArena var4 = this.main.getFightManager().getEmptyArena();
                        var4.beginFight(var3);
                        this.main.getFightManager().removeChallenge(var3);
                    }
                }
            }
        }
    }
}
