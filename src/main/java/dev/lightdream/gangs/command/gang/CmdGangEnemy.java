package dev.lightdream.gangs.command.gang;


import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

public class CmdGangEnemy extends BSubCommand {
    public CmdGangEnemy() {
        this.aliases.add("enemy");
        this.aliases.add("e");
        this.correctUsage = "/gang enemy <gang>";
        this.permission = "gangsplus.gang.enemy";
        this.requiredRank = Settings.getRequiredRank("enemy");
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
                    this.msg(Lang.MSG_GANG_ENEMY_OWN.toMsg());
                } else if (this.gang.isAlly(var2)) {
                    this.msg(Lang.MSG_GANG_ENEMY_ALREADYENEMY.toMsg());
                } else {
                    if(this.gang.getEnemyGangs().contains(var2)){
                        gang.sendMessage(Lang.MSG_GANG_ALREADY_ENEMY.toMsg().replace("%gang%", this.gang.getName()));
                        return;
                    }
                    if(this.gang.getEnemyGangs().size()>=Settings.maxEnemyGangs){
                        gang.sendMessage(Lang.MSG_GANG_ENEMY_LIMIT.toMsg());
                        return;
                    }
                    this.gang.getEnemyGangs().add(var2);
                    this.gang.getAllyGangs().remove(var2);
                    var2.getEnemyGangs().add(this.gang);
                    var2.getAllyGangs().remove(this.gang);

                    var2.sendMessage(Lang.MSG_GANG_ENEMY.toMsg().replace("%gang%", this.gang.getName()));
                    this.msg(Lang.MSG_GANG_ENEMY.toMsg().replace("%gang%", var2.getName()));
                }
            }
        }
    }
}
