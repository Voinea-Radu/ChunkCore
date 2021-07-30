package dev.lightdream.gangs.command.fight;


import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.fight.FightArena;
import dev.lightdream.gangs.fight.FightArenaState;
import dev.lightdream.gangs.core.BSubCommand;

public class CmdFightJoin extends BSubCommand {
    public CmdFightJoin() {
        this.aliases.add("join");
        this.aliases.add("j");
        this.permission = "gangsplus.fight.join";
        this.requiredRank = Settings.getRequiredRank("fightJoin");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!this.main.getFightManager().isGangInArena(this.gang)) {
            this.msg(Lang.MSG_FIGHT_JOIN_NOTFIGHTING.toMsg());
        } else {
            FightArena var1 = this.main.getFightManager().getGangsArena(this.gang);
            if (var1.getState() != FightArenaState.WAITING) {
                this.msg(Lang.MSG_FIGHT_JOIN_CANNOTJOIN.toMsg());
            } else {
                var1.addPlayer(this.player);
            }
        }
    }
}
