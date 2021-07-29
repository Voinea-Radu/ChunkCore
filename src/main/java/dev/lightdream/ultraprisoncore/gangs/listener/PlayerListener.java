package dev.lightdream.ultraprisoncore.gangs.listener;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerAllyChatEvent;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerGangChatEvent;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArena;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArenaState;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import dev.lightdream.ultraprisoncore.gangs.player.PlayerAssistData;
import dev.lightdream.ultraprisoncore.gangs.player.PlayerData;
import dev.lightdream.ultraprisoncore.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {
    private GangsPlugin main;

    public PlayerListener(GangsPlugin var1) {
        this.main = var1;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent var1) {
        Player var2 = var1.getPlayer();
        this.main.getPlayerManager().loadData(var2);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent var1) {
        Player var2 = var1.getPlayer();
        if (this.main.getGangManager().isInGang(var2) && this.main.getFightManager().isPlayerInArena(var2)) {
            this.main.getFightManager().getPlayersArena(var2).removePlayer(var2, false, false, false, true);
        }

        this.main.getPlayerManager().unloadData(var2);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent var1) {
        Player var2 = var1.getPlayer();
        if (this.main.getFightManager().isPlayerInArena(var2) && !var2.hasPermission("gangsplus.fightscommands") && Settings.fightsDisableCommandsDuringFight.contains(var1.getMessage())) {
            var1.setCancelled(true);
            var2.sendMessage(Lang.MSG_FIGHTS_CANTUSECOMMAND.toMsg());
        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent var1) {
        Player var2 = var1.getPlayer();
        if (!this.main.getGangManager().isInGang(var2)) {
            var1.setFormat(var1.getFormat().replace("{GANG}", StringUtils.fixColors(Settings.gangNameFormatNoGang)));
        } else {
            Gang var3 = this.main.getGangManager().getPlayersGang(var2);
            var1.setFormat(var1.getFormat().replace("{GANG}", var3.getFormattedName()));
            if (this.main.getPlayerManager().isPlayerLoaded(var2)) {
                PlayerData var4 = this.main.getPlayerManager().getPlayerData(var2);
                String var5 = var1.getMessage();
                if (var4.isUsingGangChat()) {
                    var1.setCancelled(true);
                    PlayerGangChatEvent var7 = new PlayerGangChatEvent(var2, var5);
                    Bukkit.getScheduler().runTask(this.main.getCore(), () -> {
                        Bukkit.getServer().getPluginManager().callEvent(var7);
                    });
                    if (!var7.isCancelled()) {
                        var3.sendChatMessage(var7.getPlayer(), var7.getMessage());
                    }

                    return;
                }

                if (var4.isUsingAllyChat()) {
                    var1.setCancelled(true);
                    PlayerAllyChatEvent var6 = new PlayerAllyChatEvent(var2, var5);
                    Bukkit.getScheduler().runTask(this.main.getCore(), () -> {
                        Bukkit.getServer().getPluginManager().callEvent(var6);
                    });
                    if (!var6.isCancelled()) {
                        var3.sendAllyChatMessage(var6.getPlayer(), var6.getMessage());
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent var1) {
        Player var2 = var1.getEntity();
        if (this.main.getPlayerManager().isPlayerLoaded(var2)) {
            PlayerData var3 = this.main.getPlayerManager().getPlayerData(var2);
            if (var3.isLastDamageByPlayer()) {
                this.handleKill(var2);
            }

            var3.registerDeath();
        }

        if (this.main.getGangManager().isInGang(var2)) {
            Gang var6 = this.main.getGangManager().getPlayersGang(var2);
            var6.registerDeath();
            if (this.main.getFightManager().isGangInArena(var6)) {
                FightArena var4 = this.main.getFightManager().getGangsArena(var6);
                var4.removePlayer(var2, false, true, true, true);
                int var5 = var4.getGang1().equals(var6) ? var4.getGang1Players().size() : var4.getGang2Players().size();
                if (var4.getState() != FightArenaState.ENDED && var5 > 0) {
                    var4.sendMessage(Lang.MSG_FIGHTS_ELIMINATED_INARENA.toString().replace("%player%", var2.getName()).replace("%gang%", var6.getName()).replace("%amount%", String.valueOf(var5)));
                }

                var2.sendMessage(Lang.MSG_FIGHTS_ELIMINATED_ELIMINATED.toMsg());
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent var1) {
        Player var2 = var1.getPlayer();
        if (this.main.getPlayerManager().isPlayerLoaded(var2)) {
            PlayerData var3 = this.main.getPlayerManager().getPlayerData(var2);
            if (var3.isTeleportCooldownRunning()) {
                var3.cancelTeleportCooldown();
                var2.sendMessage(Lang.MSG_TELEPORT_CANCELLED.toMsg());
            }
        }

        if (this.main.getFightManager().isPlayerInArena(var2)) {
            FightArena var4 = this.main.getFightManager().getPlayersArena(var2);
            if (var4.getState() == FightArenaState.WAITING) {
                if (Settings.fightsFreezeBeforeStart && var1.getFrom().getBlockX() != var1.getTo().getBlockX() || var1.getFrom().getBlockY() != var1.getTo().getBlockY() || var1.getFrom().getBlockZ() != var1.getTo().getBlockZ()) {
                    var1.setTo(var1.getFrom());
                    var2.sendMessage(Lang.MSG_FIGHTS_CANTMOVE.toMsg());
                }
            } else if (var4.getState() == FightArenaState.IN_PROGRESS && !var4.isInsideArena(var1.getTo())) {
                var1.setTo(var1.getFrom());
                var2.sendMessage(Lang.MSG_FIGHTS_CANTLEAVEARENA.toMsg());
            }
        }

    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent var1) {
        Player var2 = var1.getPlayer();
        if (var1.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && this.main.getFightManager().isPlayerInArena(var2)) {
            FightArena var3 = this.main.getFightManager().getPlayersArena(var2);
            if (var3.getState() == FightArenaState.WAITING) {
                var1.setCancelled(true);
                var2.sendMessage(Lang.MSG_FIGHTS_CANTMOVE.toMsg());
            } else if (var3.getState() == FightArenaState.IN_PROGRESS && !var3.isInsideArena(var1.getTo())) {
                var1.setCancelled(true);
                var2.sendMessage(Lang.MSG_FIGHTS_CANTLEAVEARENA.toMsg());
            }
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent var1) {
        Player var2 = var1.getPlayer();
        if (this.main.getPlayerManager().isPlayerLoaded(var2)) {
            PlayerData var3 = this.main.getPlayerManager().getPlayerData(var2);
            if (var3.hasLastLocation()) {
                var1.setRespawnLocation(var3.getLastLocation());
            }
        }

    }

    private void handleKill(Player var1) {
        if (this.main.getPlayerManager().isPlayerLoaded(var1)) {
            PlayerData var2 = this.main.getPlayerManager().getPlayerData(var1);
            Player var3 = var2.getAssistsKiller();
            long var4 = System.currentTimeMillis();
            Iterator var6 = var2.getAssistData().entrySet().iterator();

            while(true) {
                Map.Entry var7;
                Player var8;
                do {
                    do {
                        if (!var6.hasNext()) {
                            if (var3 != null) {
                                if (this.main.getPlayerManager().isPlayerLoaded(var3)) {
                                    PlayerData var10 = this.main.getPlayerManager().getPlayerData(var3);
                                    var10.registerKill();
                                }

                                if (this.main.getGangManager().isInGang(var3)) {
                                    this.main.getGangManager().getPlayersGang(var3).registerKill();
                                }
                            }

                            var2.getAssistData().clear();
                            return;
                        }

                        var7 = (Map.Entry)var6.next();
                        var8 = Bukkit.getPlayer((UUID)var7.getKey());
                    } while(var8 == null);
                } while(var3 != null && (var3.equals(var8) || var1.equals(var8)));

                if (var4 - ((PlayerAssistData)var7.getValue()).getLastDamage() < (long)(Settings.assistDamageDelay * 1000)) {
                    if (this.main.getPlayerManager().isPlayerLoaded(var8)) {
                        PlayerData var9 = this.main.getPlayerManager().getPlayerData(var8);
                        var9.registerAssist();
                    }

                    if (this.main.getGangManager().isInGang(var8)) {
                        this.main.getGangManager().getPlayersGang(var8).registerAssist();
                    }
                }
            }
        }
    }
}
