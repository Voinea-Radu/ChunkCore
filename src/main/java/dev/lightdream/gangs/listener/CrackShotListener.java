package dev.lightdream.gangs.listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CrackShotListener implements Listener {
    private GangsPlugin main;

    public CrackShotListener(GangsPlugin var1) {
        this.main = var1;
    }

    @EventHandler
    public void onWeaponDamageEntity(WeaponDamageEntityEvent var1) {
        if (var1.getVictim() instanceof Player && var1.getPlayer() != null) {
            Player var2 = var1.getPlayer();
            Player var3 = (Player)var1.getVictim();
            if (!this.main.getPlayerManager().canHurt(var2, var3)) {
                var1.setCancelled(true);
            }

            if (this.main.getPlayerManager().isPlayerLoaded(var3)) {
                PlayerData var4 = this.main.getPlayerManager().getPlayerData(var3);
                if (!var1.isCancelled()) {
                    var4.setLastDamageByPlayer(true);
                }
            }

        }
    }
}
