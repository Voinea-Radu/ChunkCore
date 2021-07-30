package dev.lightdream.gangs.command.fight;


import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.fight.FightArena;
import dev.lightdream.gangs.core.BSubCommand;

public class CmdFightLeave extends BSubCommand {
    public CmdFightLeave() {
        this.aliases.add("leave");
        this.aliases.add("l");
        this.permission = "gangsplus.fight.leave";
        this.requiredRank = Settings.getRequiredRank("fightLeave");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!this.main.getFightManager().isGangInArena(this.gang)) {
            this.msg(Lang.MSG_FIGHT_LEAVE_NOTFIGHTING.toMsg());
        } else if (!this.main.getFightManager().isPlayerInArena(this.player)) {
            this.msg(Lang.MSG_FIGHT_LEAVE_NOTINARENA.toMsg());
        } else {
            FightArena var1 = this.main.getFightManager().getPlayersArena(this.player);
            var1.removePlayer(this.player, true, true, false, true);
        }
    }
}
