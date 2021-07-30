package dev.lightdream.gangs.player;

import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.database.Callback;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerManager {
    private final GangsPlugin main;
    private final HashMap<UUID, PlayerData> playerData = new HashMap();

    public PlayerManager(GangsPlugin var1) {
        this.main = var1;
    }

    public HashMap<UUID, PlayerData> getAllPlayerData() {
        return this.playerData;
    }

    public void loadData(final Player var1) {
        this.main.getDataManager().loadPlayerData(var1, new Callback<PlayerData>() {
            public void onSuccess(PlayerData var1x) {
                if (var1x != null) {
                    PlayerManager.this.playerData.put(var1.getUniqueId(), var1x);
                }

            }

            public void onFailure(PlayerData var1x) {
            }
        });
    }

    public void registerPlayer(Player var1) {
        PlayerData var2 = new PlayerData(var1.getUniqueId(), var1.getName(), System.currentTimeMillis(), 0, 0, 0);
        this.playerData.put(var1.getUniqueId(), var2);
        this.main.getDataManager().registerPlayerData(var2);
    }

    public void unloadData(OfflinePlayer var1) {
        this.playerData.remove(var1.getUniqueId());
    }

    public boolean isPlayerLoaded(OfflinePlayer var1) {
        return this.playerData.containsKey(var1.getUniqueId());
    }

    public PlayerData getPlayerData(OfflinePlayer var1) {
        return this.playerData.get(var1.getUniqueId());
    }

    public void sendMessageToSpying(String var1) {
        Iterator var2 = this.playerData.entrySet().iterator();

        while (var2.hasNext()) {
            Entry var3 = (Entry) var2.next();
            if (Bukkit.getPlayer((UUID) var3.getKey()) != null && ((PlayerData) var3.getValue()).isEnableChatSpy()) {
                Bukkit.getPlayer((UUID) var3.getKey()).sendMessage(var1);
            }
        }

    }

    public boolean canHurt(Player var1, Player var2) {
        return !this.main.getGangManager().isInGang(var1) || !this.main.getGangManager().isInGang(var2) || (!this.main.getGangManager().getPlayersGang(var1).equals(this.main.getGangManager().getPlayersGang(var2)) || Settings.friendlyFire && !Settings.disableFriendlyFireInWorlds.contains(var2.getWorld().getName()) && (!Settings.friendlyFireTogglableByLeader || this.main.getGangManager().getPlayersGang(var1).isFriendlyFire())) && (!Settings.enableModuleAlliances || !this.main.getGangManager().getPlayersGang(var1).isAlly(this.main.getGangManager().getPlayersGang(var2)) && !this.main.getGangManager().getPlayersGang(var2).isAlly(this.main.getGangManager().getPlayersGang(var1)) || Settings.alliancesAllowPvp || this.main.getFightManager().areFighting(this.main.getGangManager().getPlayersGang(var1), this.main.getGangManager().getPlayersGang(var2)));
    }
}
