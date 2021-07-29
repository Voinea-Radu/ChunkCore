package dev.lightdream.ultraprisoncore.gangs.fight;

import com.google.common.collect.Lists;
import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class FightManager {
    private final GangsPlugin main;
    private final List<FightChallenge> fightChallenges = Lists.newArrayList();
    private List<FightArena> fightArenas = Lists.newArrayList();

    public FightManager(GangsPlugin var1) {
        this.main = var1;
    }

    public void setFightArenas(List<FightArena> var1) {
        this.fightArenas = var1;
    }

    public List<FightArena> getAllArenas() {
        return this.fightArenas;
    }

    public boolean isChallenged(Gang var1, Gang var2) {
        Iterator var3 = this.fightChallenges.iterator();

        FightChallenge var4;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            var4 = (FightChallenge) var3.next();
        } while (!var4.getChallengerGang().equals(var1) || !var4.getChallengedGang().equals(var2));

        return true;
    }

    public FightChallenge getChallenge(Gang var1, Gang var2) {
        Iterator var3 = this.fightChallenges.iterator();

        FightChallenge var4;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            var4 = (FightChallenge) var3.next();
        } while ((!var4.getChallengerGang().equals(var1) || !var4.getChallengedGang().equals(var2)) && (!var4.getChallengerGang().equals(var2) || !var4.getChallengedGang().equals(var1)));

        return var4;
    }

    public void addChallenge(Gang var1, Gang var2, int var3, double var4) {
        this.fightChallenges.add(new FightChallenge(var1, var2, var3, var4));
    }

    public void removeChallenge(FightChallenge var1) {
        this.fightChallenges.remove(var1);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void removeChallenge(Gang var1, Gang var2) {
        FightChallenge var3 = this.getChallenge(var1, var2);
        if (var3 != null) {
            this.fightChallenges.remove(var3);
        }

    }

    public void loadArenas() {
        this.main.getDataManager().loadArenas();
    }

    public void removeArena(FightArena var1) {
        this.main.getDataManager().removeArena(var1);
        this.fightArenas.remove(var1);
    }

    public FightArena getArena(String var1) {
        Iterator var2 = this.fightArenas.iterator();

        FightArena var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (FightArena) var2.next();
        } while (!var3.getId().equalsIgnoreCase(var1));

        return var3;
    }

    public boolean isArena(String var1) {
        return this.getArena(var1) != null;
    }

    public FightArena getEmptyArena() {
        Iterator var1 = this.fightArenas.iterator();

        FightArena var2;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            var2 = (FightArena) var1.next();
        } while (var2.getState() != FightArenaState.EMPTY);

        return var2;
    }

    public boolean isEmptyArena() {
        return this.getEmptyArena() != null;
    }

    public FightArena getPlayersArena(Player var1) {
        Iterator var2 = this.fightArenas.iterator();

        FightArena var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (FightArena) var2.next();
        } while ((var3.getGang1Players() == null || !var3.getGang1Players().contains(var1.getUniqueId())) && (var3.getGang2Players() == null || !var3.getGang2Players().contains(var1.getUniqueId())));

        return var3;
    }

    public boolean isPlayerInArena(Player var1) {
        return this.getPlayersArena(var1) != null;
    }

    public FightArena getGangsArena(Gang var1) {
        Iterator var2 = this.fightArenas.iterator();

        FightArena var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (FightArena) var2.next();
        } while ((var3.getGang1() == null || !var3.getGang1().equals(var1)) && (var3.getGang2() == null || !var3.getGang2().equals(var1)));

        return var3;
    }

    public boolean isGangInArena(Gang var1) {
        return this.getGangsArena(var1) != null;
    }

    public boolean areFighting(Gang var1, Gang var2) {
        return this.isGangInArena(var1) && this.isGangInArena(var2) && this.getGangsArena(var1).equals(this.getGangsArena(var2));
    }
}
