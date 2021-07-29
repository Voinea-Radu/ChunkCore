package dev.lightdream.ultraprisoncore.gangs.task;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArena;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArenaState;
import org.bukkit.Bukkit;

public class FightStartTask implements Runnable {
    private final GangsPlugin main;
    private final FightArena fightArena;

    public FightStartTask(GangsPlugin var1, FightArena var2) {
        this.main = var1;
        this.fightArena = var2;
    }

    public void run() {
        this.fightArena.setState(FightArenaState.IN_PROGRESS);
        boolean var1 = true;
        if (this.fightArena.getGang1Players().size() < this.fightArena.getMembers()) {
            var1 = false;
            this.fightArena.sendMessageToGangs(Lang.MSG_FIGHTS_START_CANNOT_MEMBERS.toString().replace("%gang%", this.fightArena.getGang1().getName()));
        }

        if (this.fightArena.getGang2Players().size() < this.fightArena.getMembers()) {
            var1 = false;
            this.fightArena.sendMessageToGangs(Lang.MSG_FIGHTS_START_CANNOT_MEMBERS.toString().replace("%gang%", this.fightArena.getGang2().getName()));
        }

        if (Settings.enableModuleBank) {
            if (this.fightArena.getGang1().getBankMoney() < this.fightArena.getMoney()) {
                var1 = false;
                this.fightArena.sendMessageToGangs(Lang.MSG_FIGHTS_START_CANNOT_MONEY.toString().replace("%gang%", this.fightArena.getGang1().getName()));
            }

            if (this.fightArena.getGang2().getBankMoney() < this.fightArena.getMoney()) {
                var1 = false;
                this.fightArena.sendMessageToGangs(Lang.MSG_FIGHTS_START_CANNOT_MONEY.toString().replace("%gang%", this.fightArena.getGang2().getName()));
            }
        }

        if (var1) {
            if (Settings.enableModuleBank) {
                this.fightArena.getGang1().withdraw(this.fightArena.getMoney());
                this.fightArena.getGang2().withdraw(this.fightArena.getMoney());
            }

            if (Settings.getBroadcast("fightStart")) {
                Settings.broadcast(Lang.MSG_FIGHTS_START_STARTED_BROADCAST.toString().replace("%gang1%", this.fightArena.getGang1().getName()).replace("%gang2%", this.fightArena.getGang2().getName()));
            }

            this.fightArena.getGang1().sendMessage(Lang.MSG_FIGHTS_START_STARTED_INGANG.toString().replace("%gang%", this.fightArena.getGang2().getName()));
            this.fightArena.getGang2().sendMessage(Lang.MSG_FIGHTS_START_STARTED_INGANG.toString().replace("%gang%", this.fightArena.getGang1().getName()));
            this.fightArena.sendMessage(Lang.MSG_FIGHTS_START_STARTED_INARENA.toString());
            if (Settings.fightsEnableFightTimeLimit) {
                this.fightArena.setEndFightTaskId(Bukkit.getScheduler().runTaskLater(this.main.getCore(), new FightEndTask(this.fightArena), (long) Settings.fightsFightTimeLimit * 20L).getTaskId());
            }
        } else {
            this.fightArena.endOnStart();
        }

    }
}
