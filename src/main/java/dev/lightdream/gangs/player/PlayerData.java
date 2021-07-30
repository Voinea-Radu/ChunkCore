package dev.lightdream.gangs.player;

import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerData {
    private UUID id;
    private String name;
    private int kills;
    private int deaths;
    private int assists;
    private long registered;
    private boolean requiresUpdate;
    private Location lastLocation;
    private Map<UUID, PlayerAssistData> assistData;
    private boolean lastDamageByPlayer;
    private int teleportCooldownId;
    private boolean usingGangChat;
    private boolean usingAllyChat;
    private boolean enableChatSpy;
    private PlayerLastAction lastAction;
    private boolean confirmedLastAction;

    public PlayerData(UUID var1, String var2, long var3, int var5, int var6, int var7) {
        this.id = var1;
        this.name = var2;
        this.registered = var3;
        this.kills = var5;
        this.deaths = var6;
        this.assists = var7;
        this.lastLocation = null;
        this.assistData = new HashMap();
        this.lastDamageByPlayer = false;
        this.teleportCooldownId = -1;
        this.usingGangChat = false;
        this.usingAllyChat = false;
        this.enableChatSpy = false;
        this.lastAction = PlayerLastAction.NONE;
        this.confirmedLastAction = false;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID var1) {
        this.id = var1;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public long getRegistered() {
        return this.registered;
    }

    public void setRegistered(long var1) {
        this.registered = var1;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int var1) {
        this.kills = var1;
    }

    public void registerKill() {
        ++this.kills;
        this.save();
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int var1) {
        this.deaths = var1;
    }

    public void registerDeath() {
        ++this.deaths;
        this.save();
    }

    public double getKdRatio() {
        return this.deaths != 0 ? (double) this.kills / (double) this.deaths : (double) this.kills;
    }

    public double getKdr() {
        return this.deaths == 0 ? this.kills : this.kills / this.deaths;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int var1) {
        this.assists = var1;
    }

    public void registerAssist() {
        ++this.assists;
        this.save();
    }

    public boolean isRequiresUpdate() {
        return this.requiresUpdate;
    }

    public void setRequiresUpdate(boolean var1) {
        this.requiresUpdate = var1;
    }

    public Map<UUID, PlayerAssistData> getAssistData() {
        return this.assistData;
    }

    public void setAssistData(Map<UUID, PlayerAssistData> var1) {
        this.assistData = var1;
    }

    public boolean isLastDamageByPlayer() {
        return this.lastDamageByPlayer;
    }

    public void setLastDamageByPlayer(boolean var1) {
        this.lastDamageByPlayer = var1;
    }

    public int getTeleportCooldownId() {
        return this.teleportCooldownId;
    }

    public void setTeleportCooldownId(int var1) {
        this.teleportCooldownId = var1;
    }

    public boolean isUsingGangChat() {
        return this.usingGangChat;
    }

    public void setUsingGangChat(boolean var1) {
        this.usingGangChat = var1;
    }

    public boolean isUsingAllyChat() {
        return this.usingAllyChat;
    }

    public void setUsingAllyChat(boolean var1) {
        this.usingAllyChat = var1;
    }

    public boolean isEnableChatSpy() {
        return this.enableChatSpy;
    }

    public void setEnableChatSpy(boolean var1) {
        this.enableChatSpy = var1;
    }

    public PlayerLastAction getLastAction() {
        return this.lastAction;
    }

    public void setLastAction(PlayerLastAction var1) {
        this.lastAction = var1;
        this.confirmedLastAction = false;
    }

    public boolean isConfirmedLastAction() {
        return this.confirmedLastAction;
    }

    public void setConfirmedLastAction(boolean var1) {
        this.confirmedLastAction = var1;
    }

    public void teleportToLastLocation() {
        if (this.lastLocation != null) {
            Bukkit.getPlayer(this.id).teleport(this.lastLocation);
            this.setLastLocation(null);
        }
    }

    public boolean hasLastLocation() {
        return this.lastLocation != null;
    }

    public Location getLastLocation() {
        Location var1 = this.lastLocation;
        this.lastLocation = null;
        return var1;
    }

    public void setLastLocation(Location var1) {
        this.lastLocation = var1;
    }

    public void registerAssistDamage(Player var1, double var2) {
        if (!this.assistData.containsKey(var1.getUniqueId())) {
            this.assistData.put(var1.getUniqueId(), new PlayerAssistData(var2, System.currentTimeMillis()));
        } else {
            PlayerAssistData var4 = this.assistData.get(var1.getUniqueId());
            var4.addDamage(var2);
            var4.setLastDamage(System.currentTimeMillis());
        }

    }

    public Player getAssistsKiller() {
        long var1 = 0L;
        Player var3 = null;
        Iterator var4 = this.getAssistData().entrySet().iterator();

        while (var4.hasNext()) {
            Entry var5 = (Entry) var4.next();
            if (((PlayerAssistData) var5.getValue()).getLastDamage() >= var1 && Bukkit.getPlayer((UUID) var5.getKey()) != null) {
                var1 = ((PlayerAssistData) var5.getValue()).getLastDamage();
                var3 = Bukkit.getPlayer((UUID) var5.getKey());
            }
        }

        return var3;
    }

    public boolean isTeleportCooldownRunning() {
        return this.teleportCooldownId != -1 && (Bukkit.getScheduler().isCurrentlyRunning(this.teleportCooldownId) || Bukkit.getScheduler().isQueued(this.teleportCooldownId));
    }

    public void cancelTeleportCooldown() {
        Bukkit.getScheduler().cancelTask(this.teleportCooldownId);
        this.teleportCooldownId = -1;
    }

    public void save() {
        if (Settings.saveDataPeriodically) {
            this.requiresUpdate = true;
        } else {
            GangsPlugin.getInstance().getDataManager().updatePlayerData(this);
        }

    }

    public String formatPlaceholders(String var1) {
        return var1.replace("%id%", String.valueOf(this.id)).replace("%name%", this.name).replace("%date%", Settings.formatDate(this.registered)).replace("%kills%", String.valueOf(this.kills)).replace("%deaths%", String.valueOf(this.deaths)).replace("%assists%", String.valueOf(this.assists)).replace("%kdr%", StringUtils.formatDoubleString(this.getKdRatio()));
    }
}
