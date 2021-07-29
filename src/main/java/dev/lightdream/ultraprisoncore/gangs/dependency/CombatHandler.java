package dev.lightdream.ultraprisoncore.gangs.dependency;

import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;
import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import me.iiSnipez.CombatLog.CombatLog;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.entity.Player;
import techcable.minecraft.combattag.CombatTagAPI;

public class CombatHandler {
    private final GangsPlugin main;
    private CombatTagApi combatTagAPI;
    private CombatLog combatLog;
    private CombatTagPlus combatTagPlus;

    public CombatHandler(GangsPlugin var1) {
        this.main = var1;
    }

    public boolean isTagged(Player var1) {
        if (this.hasCombatTag()) {
            return this.getCombatTagAPI().isInCombat(var1);
        } else if (this.hasCombatTagReloaded()) {
            return CombatTagAPI.isTagged(var1);
        } else if (this.hasCombatLog()) {
            return this.getCombatLog().taggedPlayers.containsKey(var1.getName());
        } else {
            return this.hasCombatTagPlus() && this.getCombatTagPlus().getTagManager().isTagged(var1.getUniqueId());
        }
    }

    public void untagPlayer(Player var1) {
        if (this.hasCombatTag()) {
            this.getCombatTagAPI().untagPlayer(var1);
        } else if (this.hasCombatTagReloaded()) {
            CombatTagAPI.setTagged(var1, false);
        } else if (this.hasCombatLog()) {
            this.getCombatLog().taggedPlayers.remove(var1.getName());
        } else if (this.hasCombatTagPlus()) {
            this.getCombatTagPlus().getTagManager().untag(var1.getUniqueId());
        }

    }

    private boolean hasCombatTag() {
        return this.main.getCore().getServer().getPluginManager().getPlugin("CombatTag") != null;
    }

    private boolean hasCombatTagReloaded() {
        return this.main.getCore().getServer().getPluginManager().getPlugin("CombatTagReloaded") != null;
    }

    private boolean hasCombatLog() {
        return this.main.getCore().getServer().getPluginManager().getPlugin("CombatLog") != null;
    }

    private boolean hasCombatTagPlus() {
        return this.main.getCore().getServer().getPluginManager().getPlugin("CombatTagPlus") != null;
    }

    private CombatTagApi getCombatTagAPI() {
        if (this.combatTagAPI == null) {
            this.combatTagAPI = new CombatTagApi((CombatTag) this.main.getCore().getServer().getPluginManager().getPlugin("CombatTag"));
        }

        return this.combatTagAPI;
    }

    private CombatLog getCombatLog() {
        if (this.combatLog == null) {
            this.combatLog = (CombatLog) this.main.getCore().getServer().getPluginManager().getPlugin("CombatLog");
        }

        return this.combatLog;
    }

    private CombatTagPlus getCombatTagPlus() {
        if (this.combatTagPlus == null) {
            this.combatTagPlus = (CombatTagPlus) this.main.getCore().getServer().getPluginManager().getPlugin("CombatTagPlus");
        }

        return this.combatTagPlus;
    }
}
