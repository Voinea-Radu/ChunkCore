package dev.lightdream.gangs.task;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.fight.FightArena;
import dev.lightdream.gangs.fight.FightArenaState;

public class FightEndTask implements Runnable {
    private final FightArena fightArena;

    public FightEndTask(FightArena var1) {
        this.fightArena = var1;
    }

    public void run() {
        this.fightArena.setState(FightArenaState.ENDED);
        if (Settings.getBroadcast("fightEnd")) {
            Settings.broadcast(Lang.MSG_FIGHTS_END_ENDED_BROADCAST.toString().replace("%gang1%", this.fightArena.getGang1().getName()).replace("%gang2%", this.fightArena.getGang2().getName()));
        }

        this.fightArena.finish();
    }
}
