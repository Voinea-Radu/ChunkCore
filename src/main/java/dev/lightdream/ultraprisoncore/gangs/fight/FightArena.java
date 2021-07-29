package dev.lightdream.ultraprisoncore.gangs.fight;

import dev.lightdream.ultraprisoncore.gangs.player.PlayerData;
import dev.lightdream.ultraprisoncore.gangs.task.FightEndTeleportTask;
import dev.lightdream.ultraprisoncore.gangs.task.FightStartTask;
import dev.lightdream.ultraprisoncore.gangs.util.LocationUtils;
import dev.lightdream.ultraprisoncore.gangs.util.TimeUtils;
import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import dev.lightdream.ultraprisoncore.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.*;

public class FightArena {
    private int databaseId;
    private String id;
    private String name;
    private Location corner1;
    private Location corner2;
    private Location spawn1;
    private Location spawn2;
    private FightArenaState state;
    private double money;
    private int members;
    private Gang gang1;
    private Gang gang2;
    private Set<UUID> gang1Players;
    private Set<UUID> gang2Players;
    private int endFightTaskId;

    public FightArena(int var1, String var2, String var3, Location var4, Location var5, Location var6, Location var7, FightArenaState var8) {
        this.databaseId = var1;
        this.id = var2;
        this.name = var3;
        this.corner1 = var4;
        this.corner2 = var5;
        this.spawn1 = var6;
        this.spawn2 = var7;
        this.state = var8;
        this.money = 0.0D;
        this.members = 0;
        this.gang1 = null;
        this.gang2 = null;
        this.gang1Players = new HashSet();
        this.gang2Players = new HashSet();
        this.endFightTaskId = -1;
    }

    public int getDatabaseId() {
        return this.databaseId;
    }

    public void setDatabaseId(int var1) {
        this.databaseId = var1;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String var1) {
        this.id = var1;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public Location getCorner1() {
        return this.corner1;
    }

    public void setCorner1(Location var1) {
        this.corner1 = var1;
    }

    public Location getCorner2() {
        return this.corner2;
    }

    public void setCorner2(Location var1) {
        this.corner2 = var1;
    }

    public boolean isInsideArena(Location var1) {
        return LocationUtils.isLocationInside(this.corner1, this.corner2, var1);
    }

    public Location getSpawn1() {
        return this.spawn1;
    }

    public void setSpawn1(Location var1) {
        this.spawn1 = var1;
    }

    public Location getSpawn2() {
        return this.spawn2;
    }

    public void setSpawn2(Location var1) {
        this.spawn2 = var1;
    }

    public World getLocationsWorld() {
        if (this.corner1 != null) {
            return this.corner1.getWorld();
        } else if (this.corner2 != null) {
            return this.corner2.getWorld();
        } else if (this.spawn1 != null) {
            return this.spawn1.getWorld();
        } else {
            return this.spawn2 != null ? this.spawn2.getWorld() : null;
        }
    }

    public FightArenaState getState() {
        return this.state;
    }

    public void setState(FightArenaState var1) {
        this.state = var1;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double var1) {
        this.money = var1;
    }

    public int getMembers() {
        return this.members;
    }

    public void setMembers(int var1) {
        this.members = var1;
    }

    public Gang getGang1() {
        return this.gang1;
    }

    public void setGang1(Gang var1) {
        this.gang1 = var1;
    }

    public Gang getGang2() {
        return this.gang2;
    }

    public void setGang2(Gang var1) {
        this.gang2 = var1;
    }

    public Set<UUID> getGang1Players() {
        return this.gang1Players;
    }

    public void setGang1Players(Set<UUID> var1) {
        this.gang1Players = var1;
    }

    public Set<UUID> getGang2Players() {
        return this.gang2Players;
    }

    public void setGang2Players(Set<UUID> var1) {
        this.gang2Players = var1;
    }

    public int getEndFightTaskId() {
        return this.endFightTaskId;
    }

    public void setEndFightTaskId(int var1) {
        this.endFightTaskId = var1;
    }

    public void sendMessage(String var1) {
        ArrayList var2 = new ArrayList();
        var2.addAll(this.gang1Players);
        var2.addAll(this.gang2Players);
        Iterator var3 = var2.iterator();

        while (var3.hasNext()) {
            UUID var4 = (UUID) var3.next();
            if (Bukkit.getPlayer(var4) != null) {
                Bukkit.getPlayer(var4).sendMessage(StringUtils.fixColors(Settings.messagesInArena.replace("%message%", var1)));
            }
        }

    }

    public void sendMessageToGangs(String var1) {
        if (this.gang1 != null) {
            this.gang1.sendMessage(var1);
        }

        if (this.gang2 != null) {
            this.gang2.sendMessage(var1);
        }

    }

    private Gang getWinner() {
        return this.gang1Players.size() < 1 ? this.gang2 : this.gang1;
    }

    private Set<UUID> getWinnerPlayers() {
        return this.gang1Players.size() < 1 ? this.getGang2Players() : this.getGang1Players();
    }

    private Gang getLoser() {
        return this.gang1Players.size() < 1 ? this.gang1 : this.gang2;
    }

    public void addPlayer(Player var1) {
        if (GangsPlugin.getInstance().getGangManager().isInGang(var1)) {
            Gang var2 = GangsPlugin.getInstance().getGangManager().getPlayersGang(var1);
            PlayerData var3;
            if (this.gang1.equals(var2)) {
                this.gang1Players.add(var1.getUniqueId());
                this.sendMessage(Lang.MSG_FIGHT_JOIN_JOINED.toString().replace("%player%", var1.getName()).replace("%gang%", this.gang1.getName()).replace("%membersamount%", String.valueOf(this.getGang1Players().size())));
                if (GangsPlugin.getInstance().getPlayerManager().isPlayerLoaded(var1)) {
                    var3 = GangsPlugin.getInstance().getPlayerManager().getPlayerData(var1);
                    var3.setLastLocation(var1.getLocation());
                }

                var1.teleport(this.spawn1, TeleportCause.PLUGIN);
                var1.setFireTicks(0);
                var1.setHealth(20.0D);
            } else if (this.gang2.equals(var2)) {
                this.gang2Players.add(var1.getUniqueId());
                this.sendMessage(Lang.MSG_FIGHT_JOIN_JOINED.toString().replace("%player%", var1.getName()).replace("%gang%", this.gang2.getName()).replace("%membersamount%", String.valueOf(this.getGang2Players().size())));
                if (GangsPlugin.getInstance().getPlayerManager().isPlayerLoaded(var1)) {
                    var3 = GangsPlugin.getInstance().getPlayerManager().getPlayerData(var1);
                    var3.setLastLocation(var1.getLocation());
                }

                var1.teleport(this.spawn2, TeleportCause.PLUGIN);
                var1.setFireTicks(0);
                var1.setHealth(20.0D);
            }

        }
    }

    public void removePlayer(Player var1, boolean var2, boolean var3, boolean var4, boolean var5) {
        if (GangsPlugin.getInstance().getGangManager().isInGang(var1)) {
            Gang var6 = GangsPlugin.getInstance().getGangManager().getPlayersGang(var1);
            boolean var7 = false;
            if (this.gang1.equals(var6)) {
                if (var5) {
                    this.gang1Players.remove(var1.getUniqueId());
                }

                var7 = true;
            } else if (this.gang2.equals(var6)) {
                if (var5) {
                    this.gang2Players.remove(var1.getUniqueId());
                }

                var7 = true;
            }

            if (var7) {
                if (var2 && GangsPlugin.getInstance().getPlayerManager().isPlayerLoaded(var1)) {
                    GangsPlugin.getInstance().getPlayerManager().getPlayerData(var1).teleportToLastLocation();
                    var1.setFireTicks(0);
                    var1.setHealth(20.0D);
                }

                if (var3 && GangsPlugin.getInstance().getCombatHandler().isTagged(var1)) {
                    GangsPlugin.getInstance().getCombatHandler().untagPlayer(var1);
                }

                if (!var4) {
                    this.sendMessage(Lang.MSG_FIGHT_LEAVE_LEFT.toString().replace("%player%", var1.getName()).replace("%gang%", var6.getName()));
                }

                this.endFightIfNecessary();
            }

        }
    }

    public void beginFight(FightChallenge var1) {
        this.state = FightArenaState.WAITING;
        this.gang1 = var1.getChallengedGang();
        this.gang2 = var1.getChallengerGang();
        this.members = var1.getMembers();
        this.money = var1.getMoney();
        int var2 = Settings.fightsDelayBeforeStart;
        this.gang1.sendMessage(Lang.MSG_FIGHTS_START_WILLSTART.toString().replace("%gang%", this.gang2.getName()).replace("%arena%", this.name).replace("%time%", TimeUtils.getTimeString(var2)).replace("%membersamount%", String.valueOf(this.members)).replace("%money%", StringUtils.formatDoubleString(this.money)));
        this.gang2.sendMessage(Lang.MSG_FIGHTS_START_WILLSTART.toString().replace("%gang%", this.gang1.getName()).replace("%arena%", this.name).replace("%time%", TimeUtils.getTimeString(var2)).replace("%membersamount%", String.valueOf(this.members)).replace("%money%", StringUtils.formatDoubleString(this.money)));
        Bukkit.getScheduler().runTaskLater(GangsPlugin.getInstance().getCore(), new FightStartTask(GangsPlugin.getInstance(), this), (long) var2 * 20L);
    }

    public void endFightIfNecessary() {
        if (this.state == FightArenaState.IN_PROGRESS && (this.gang1Players.size() <= 0 || this.gang2Players.size() <= 0)) {
            this.state = FightArenaState.ENDED;
            double var1 = 2.0D * this.money;
            Gang var3 = this.getWinner();
            Gang var4 = this.getLoser();
            if (Settings.enableModuleBank) {
                var3.deposit(var1);
            } else {
                double var5 = var1 / (double) this.getWinnerPlayers().size();
                Iterator var7 = this.getWinnerPlayers().iterator();

                while (var7.hasNext()) {
                    UUID var8 = (UUID) var7.next();
                    if (Bukkit.getPlayer(var8) != null) {
                        Player var9 = Bukkit.getPlayer(var8);
                        GangsPlugin.getInstance().getEconomy().depositPlayer(var9, var5);
                        var9.sendMessage(Lang.MSG_FIGHTS_END_MONEY.toMsg().replace("%amount%", StringUtils.formatDoubleString(var5)));
                    }
                }
            }

            var3.registerFightWon();
            var4.registerFightLost();
            if (Settings.getBroadcast("fightEnd")) {
                Bukkit.broadcastMessage(Lang.MSG_FIGHTS_END_WON_BROADCAST.toMsg().replace("%winner%", var3.getName()).replace("%loser%", var4.getName()));
            }

            var3.sendMessage(Lang.MSG_FIGHTS_END_WON_INGANG.toString().replace("%gang%", var4.getName()).replace("%amount%", StringUtils.formatDoubleString(var1)));
            var4.sendMessage(Lang.MSG_FIGHTS_END_LOST_INGANG.toString().replace("%gang%", var3.getName()).replace("%amount%", StringUtils.formatDoubleString(var1)));
            this.finish();
        }
    }

    public void endOnStart() {
        if (this.state == FightArenaState.IN_PROGRESS) {
            this.state = FightArenaState.ENDED;
            this.removeAllPlayers();
            this.money = 0.0D;
            this.members = 0;
            this.gang1 = null;
            this.gang2 = null;
            this.state = FightArenaState.EMPTY;
        }
    }

    public void finish() {
        this.state = FightArenaState.ENDED;
        if (Settings.fightsEnableFightTimeLimit && this.endFightTaskId != -1) {
            Bukkit.getScheduler().cancelTask(this.endFightTaskId);
            this.endFightTaskId = -1;
        }

        if (Settings.fightsDelayBeforeTeleport > 0) {
            this.sendMessage(Lang.MSG_FIGHTS_END_TELEPORT.toString().replace("%time%", TimeUtils.getTimeString(Settings.fightsDelayBeforeTeleport)));
        }

        Bukkit.getScheduler().runTaskLater(GangsPlugin.getInstance().getCore(), new FightEndTeleportTask(this), (long) Settings.fightsDelayBeforeTeleport * 20L);
    }

    public void removeAllPlayers() {
        Iterator var1 = this.getGang1Players().iterator();

        while (var1.hasNext()) {
            this.removePlayer(Bukkit.getPlayer((UUID) var1.next()), true, true, true, false);
            var1.remove();
        }

        Iterator var2 = this.getGang2Players().iterator();

        while (var2.hasNext()) {
            this.removePlayer(Bukkit.getPlayer((UUID) var2.next()), true, true, true, false);
            var2.remove();
        }

    }
}
