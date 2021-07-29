package dev.lightdream.ultraprisoncore.gangs.listener;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.player.PlayerData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {
    private GangsPlugin main;

    public EntityListener(GangsPlugin var1) {
        this.main = var1;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent var1) {
        Entity var2 = var1.getEntity();
        if (var2 instanceof Player) {
            Player var3 = (Player)var2;
            if (this.main.getPlayerManager().isPlayerLoaded(var3)) {
                PlayerData var4 = this.main.getPlayerManager().getPlayerData(var3);
                if (var1.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && !var1.isCancelled()) {
                    var4.setLastDamageByPlayer(false);
                }
            }
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent var1) {
        Entity var2 = var1.getEntity();
        if (var2 instanceof Player) {
            Player var3 = (Player)var2;
            Player var4;
            if (var1.getDamager() instanceof Projectile && ((Projectile)var1.getDamager()).getShooter() instanceof Player) {
                var4 = (Player)((Projectile)var1.getDamager()).getShooter();
                if (!this.main.getPlayerManager().canHurt(var4, var3)) {
                    var1.setCancelled(true);
                    return;
                }

                this.handleDamage(var4, var3, var1.getFinalDamage());
            } else if (var1.getDamager() instanceof Player) {
                var4 = (Player)var1.getDamager();
                if (!this.main.getPlayerManager().canHurt(var4, var3)) {
                    var1.setCancelled(true);
                    return;
                }

                this.handleDamage(var4, var3, var1.getFinalDamage());
            }
        }

    }

    private void handleDamage(Player var1, Player var2, double var3) {
        if (var2.getHealth() > 0.0D && this.main.getPlayerManager().isPlayerLoaded(var2)) {
            PlayerData var5 = this.main.getPlayerManager().getPlayerData(var2);
            var5.registerAssistDamage(var1, var3);
            var5.setLastDamageByPlayer(true);
        }

    }
}
