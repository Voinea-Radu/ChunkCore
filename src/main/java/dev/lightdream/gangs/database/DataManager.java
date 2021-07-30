package dev.lightdream.gangs.database;

import com.google.gson.reflect.TypeToken;
import dev.lightdream.gangs.fight.FightArena;
import dev.lightdream.gangs.fight.FightArenaState;
import dev.lightdream.gangs.legacy.location.InvalidLocationException;
import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.util.GsonUtils;
import dev.lightdream.gangs.util.LocationUtils;
import lombok.SneakyThrows;
import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.gang.Gang;
import dev.lightdream.gangs.gang.GangMemberData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DataManager {
    protected GangsPlugin main;
    protected String alliancesTableName;
    protected String arenasTableName;
    protected String gangsTableName;
    protected String playersTableName;
    protected Connection connection = null;
    protected String fileBackendName = "database";
    protected ExecutorService executorService = null;
    protected long lastValidation;

    public DataManager(GangsPlugin var1) {
        this.main = var1;
        this.alliancesTableName = !Settings.databaseTableNameAlliances.isEmpty() ? Settings.databaseTableNameAlliances : "alliances";
        this.arenasTableName = !Settings.databaseTableNameArenas.isEmpty() ? Settings.databaseTableNameArenas : "arenas";
        this.gangsTableName = !Settings.databaseTableNameGangs.isEmpty() ? Settings.databaseTableNameGangs : "gangs";
        this.playersTableName = !Settings.databaseTableNamePlayers.isEmpty() ? Settings.databaseTableNamePlayers : "players";
        this.executorService = Executors.newFixedThreadPool(1);
        this.lastValidation = System.currentTimeMillis();
    }

    public abstract void setup();

    public abstract void open();

    public void close() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                return;
            }

            this.connection.close();
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.open();
            }

            if (System.currentTimeMillis() - this.lastValidation >= 5000L) {
                PreparedStatement var1 = this.connection.prepareStatement("SELECT 1");
                var1.executeQuery();
                this.lastValidation = System.currentTimeMillis();
            }
        } catch (SQLException var2) {
            this.open();
        }

        return this.connection;
    }

    public void executeQuery(String var1) {
        this.executorService.submit(() -> {
            PreparedStatement var2 = null;

            try {
                var2 = this.getConnection().prepareStatement(var1);
                var2.executeUpdate();
            } catch (SQLException var12) {
                var12.printStackTrace();
            } finally {
                try {
                    var2.close();
                } catch (SQLException var11) {
                }

            }

        });
    }

    @SneakyThrows
    protected PlayerData getPlayerDataFromResult(ResultSet var1) {
        return new PlayerData(UUID.fromString(var1.getString("uuid")), var1.getString("name"), var1.getLong("registered"), var1.getInt("kills"), var1.getInt("deaths"), var1.getInt("assists"));
    }

    public void loadPlayerData(Player var1, Callback<PlayerData> var2) {
        this.executorService.submit(() -> {
            PreparedStatement var3 = null;
            ResultSet var4 = null;

            try {
                String var5 = "SELECT * FROM " + this.playersTableName + " WHERE uuid='" + var1.getUniqueId().toString() + "'";
                var3 = this.getConnection().prepareStatement(var5);
                var4 = var3.executeQuery();
                if (!var4.next()) {
                    this.main.getPlayerManager().registerPlayer(var1);
                    var2.onSuccess(null);
                } else {
                    var2.onSuccess(this.getPlayerDataFromResult(var4));
                }
            } catch (SQLException var14) {
                var2.onFailure(null);
                var14.printStackTrace();
            } finally {
                try {
                    var4.close();
                    var3.close();
                } catch (SQLException var13) {
                }

            }

        });
    }

    public void retrievePlayerData(String var1, Callback<PlayerData> var2) {
        this.executorService.submit(() -> {
            PreparedStatement var3 = null;
            ResultSet var4 = null;

            try {
                var3 = this.getConnection().prepareStatement("SELECT * FROM " + this.playersTableName + " WHERE name='" + var1 + "'");
                var4 = var3.executeQuery();
                if (!var4.next()) {
                    var2.onSuccess(null);
                } else {
                    var2.onSuccess(this.getPlayerDataFromResult(var4));
                }
            } catch (SQLException var14) {
                var2.onFailure(null);
                var14.printStackTrace();
            } finally {
                try {
                    var4.close();
                    var3.close();
                } catch (SQLException var13) {
                }

            }

        });
    }

    public void registerPlayerData(PlayerData var1) {
        this.executeQuery("INSERT INTO " + this.playersTableName + "(uuid, name, registered, kills, deaths, assists) VALUES ('" + var1.getId().toString() + "', '" + var1.getName() + "', '" + var1.getRegistered() + "', '" + var1.getKills() + "', '" + var1.getDeaths() + "', '" + var1.getAssists() + "');");
    }

    public void updatePlayerData(PlayerData var1) {
        this.executeQuery("UPDATE " + this.playersTableName + " SET name='" + var1.getName() + "', kills='" + var1.getKills() + "', deaths='" + var1.getDeaths() + "', assists='" + var1.getAssists() + "' WHERE uuid='" + var1.getId().toString() + "';");
    }

    public void loadGangs() {
        this.executorService.submit(new Runnable() {
            final List<Gang> result = new ArrayList();

            public void run() {
                PreparedStatement var1 = null;
                ResultSet var2 = null;

                try {
                    var1 = DataManager.this.getConnection().prepareStatement("SELECT * FROM " + DataManager.this.gangsTableName + ";");
                    var2 = var1.executeQuery();
                    if (var2 != null) {
                        while (var2.next()) {
                            Map var3 = DataManager.this.main.getGson().fromJson(var2.getString("members"), (new TypeToken<Map<UUID, GangMemberData>>() {
                            }).getType());
                            Set var4 = DataManager.this.main.getGson().fromJson(var2.getString("invitations"), (new TypeToken<Set<UUID>>() {
                            }).getType());
                            Map var5 = GsonUtils.getGson().fromJson(var2.getString("homes"), (new TypeToken<Map<String, Location>>() {
                            }).getType());
                            this.result.add(new Gang(var2.getInt("id"), var2.getString("name"), var2.getLong("created"), var2.getBoolean("friendlyfire"), var2.getInt("level"), var2.getInt("kills"), var2.getInt("deaths"), var2.getInt("assists"), var2.getInt("fightswon"), var2.getInt("fightslost"), var2.getDouble("bankmoney"), var3, var4, var5));
                        }
                    }
                } catch (SQLException var14) {
                    var14.printStackTrace();
                } finally {
                    try {
                        var2.close();
                        var1.close();
                    } catch (SQLException var13) {
                    }

                    DataManager.this.main.getGangManager().setGangs(this.result);
                }

            }
        });
    }

    public void createGang(Gang var1, Callback<Integer> var2) {
        this.executorService.submit(() -> {
            PreparedStatement var3 = null;
            ResultSet var4 = null;

            try {
                var3 = this.getConnection().prepareStatement("INSERT INTO " + this.gangsTableName + " (name, created, friendlyfire, level, kills, deaths, assists, fightswon, fightslost, bankmoney, members, invitations, homes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 1);
                var3.setString(1, var1.getRawName());
                var3.setLong(2, var1.getCreatedAt());
                var3.setBoolean(3, var1.isFriendlyFire());
                var3.setInt(4, var1.getLevel());
                var3.setInt(5, var1.getKills());
                var3.setInt(6, var1.getDeaths());
                var3.setInt(7, var1.getAssists());
                var3.setInt(8, var1.getFightsWon());
                var3.setInt(9, var1.getFightsLost());
                var3.setDouble(10, var1.getBankMoney());
                var3.setString(11, this.main.getGson().toJson(var1.getMembers()));
                var3.setString(12, this.main.getGson().toJson(var1.getInvitations()));
                var3.setString(13, this.main.getGson().toJson(var1.getHomes()));
                var3.executeUpdate();
                var4 = var3.getGeneratedKeys();
                if (var4.next()) {
                    var2.onSuccess(var4.getInt(1));
                } else {
                    var2.onSuccess(-1);
                }
            } catch (SQLException var14) {
                var2.onFailure(-1);
                var14.printStackTrace();
            } finally {
                try {
                    var4.close();
                    var3.close();
                } catch (SQLException var13) {
                }

            }

        });
    }

    public void updateGang(Gang var1) {
        int var2 = var1.isFriendlyFire() ? 1 : 0;
        this.executeQuery("UPDATE " + this.gangsTableName + " SET name = '" + var1.getRawName() + "', created = '" + var1.getCreatedAt() + "', friendlyfire = '" + var2 + "', level = '" + var1.getLevel() + "', kills = '" + var1.getKills() + "', deaths = '" + var1.getDeaths() + "', assists = '" + var1.getAssists() + "', fightswon = '" + var1.getFightsWon() + "', fightslost = '" + var1.getFightsLost() + "', bankmoney = '" + var1.getBankMoney() + "', members = '" + this.main.getGson().toJson(var1.getMembers()) + "', invitations = '" + this.main.getGson().toJson(var1.getInvitations()) + "', homes = '" + GsonUtils.getGson().toJson(var1.getHomes()) + "' WHERE id = '" + var1.getId() + "'");
    }

    public void removeGang(Gang var1) {
        this.executeQuery("DELETE FROM " + this.gangsTableName + " WHERE id='" + var1.getId() + "';");
    }

    public void loadAlliances() {
        this.executorService.submit(() -> {
            PreparedStatement var1 = null;
            ResultSet var2 = null;

            try {
                Iterator var3 = this.main.getGangManager().getAllGangs().iterator();

                while (var3.hasNext()) {
                    Gang var4 = (Gang) var3.next();
                    var1 = this.getConnection().prepareStatement("SELECT * FROM " + this.alliancesTableName + " WHERE gang1=?");
                    var1.setInt(1, var4.getId());
                    var2 = var1.executeQuery();

                    while (var2.next()) {
                        if (this.main.getGangManager().getGang(var2.getInt("gang2")) != null) {
                            var4.getAllyGangs().add(this.main.getGangManager().getGang(var2.getInt("gang2")));
                        }
                    }
                }
            } catch (SQLException var13) {
                var13.printStackTrace();
            } finally {
                try {
                    var2.close();
                    var1.close();
                } catch (SQLException var12) {
                }

            }

        });
    }

    public void addAlliance(Gang var1, Gang var2) {
        this.executeQuery("INSERT INTO " + this.alliancesTableName + "(gang1, gang2) VALUES (" + var1.getId() + ", " + var2.getId() + ")");
        this.executeQuery("INSERT INTO " + this.alliancesTableName + "(gang1, gang2) VALUES (" + var2.getId() + ", " + var1.getId() + ")");
    }

    public void removeAlliance(Gang var1, Gang var2) {
        this.executeQuery("DELETE FROM " + this.alliancesTableName + " WHERE (gang1=" + var1.getId() + " AND gang2=" + var2.getId() + ") OR (gang1=" + var2.getId() + " AND gang2=" + var1.getId() + ")");
    }

    public void loadArenas() {
        this.executorService.submit(new Runnable() {
            final List<FightArena> fightArenas = new ArrayList();

            public void run() {
                PreparedStatement var1 = null;
                ResultSet var2 = null;

                try {
                    var1 = DataManager.this.getConnection().prepareStatement("SELECT * FROM " + DataManager.this.arenasTableName + ";");
                    var2 = var1.executeQuery();
                    if (var2 != null) {
                        while (var2.next()) {
                            FightArena var3;
                            try {
                                var3 = new FightArena(var2.getInt("id"), var2.getString("textId"), var2.getString("name"), LocationUtils.deserialize(var2.getString("corner1")), LocationUtils.deserialize(var2.getString("corner2")), LocationUtils.deserialize(var2.getString("spawn1")), LocationUtils.deserialize(var2.getString("spawn2")), FightArenaState.EMPTY);
                            } catch (InvalidLocationException var14) {
                                continue;
                            }

                            this.fightArenas.add(var3);
                        }
                    }
                } catch (SQLException var15) {
                    var15.printStackTrace();
                } finally {
                    try {
                        var2.close();
                        var1.close();
                    } catch (SQLException var13) {
                    }

                    DataManager.this.main.getFightManager().setFightArenas(this.fightArenas);
                }

            }
        });
    }

    public void createArena(FightArena var1, Callback<Integer> var2) {
        this.executorService.submit(() -> {
            PreparedStatement var3 = null;
            ResultSet var4 = null;

            try {
                var3 = this.getConnection().prepareStatement("INSERT INTO " + this.arenasTableName + " (textId, name, corner1, corner2, spawn1, spawn2) VALUES (?, ?, ?, ?, ?, ?)", 1);
                var3.setString(1, var1.getId());
                var3.setString(2, var1.getName());
                var3.setString(3, LocationUtils.serialize(var1.getCorner1()));
                var3.setString(4, LocationUtils.serialize(var1.getCorner2()));
                var3.setString(5, LocationUtils.serialize(var1.getSpawn1()));
                var3.setString(6, LocationUtils.serialize(var1.getSpawn2()));
                var3.executeUpdate();
                var4 = var3.getGeneratedKeys();
                if (var4.next()) {
                    var2.onSuccess(var4.getInt(1));
                } else {
                    var2.onSuccess(-1);
                }
            } catch (SQLException var14) {
                var2.onFailure(-1);
                var14.printStackTrace();
            } finally {
                try {
                    var4.close();
                    var3.close();
                } catch (SQLException var13) {
                }

            }

        });
    }

    public void updateArena(FightArena var1) {
        this.executeQuery("UPDATE " + this.arenasTableName + " SET name = '" + var1.getName() + "', corner1 = '" + LocationUtils.serialize(var1.getCorner1()) + "', corner2 = '" + LocationUtils.serialize(var1.getCorner2()) + "', spawn1 = '" + LocationUtils.serialize(var1.getSpawn1()) + "', spawn2 = '" + LocationUtils.serialize(var1.getSpawn2()) + "' WHERE id = '" + var1.getDatabaseId() + "';");
    }

    public void removeArena(FightArena var1) {
        this.executeQuery("DELETE FROM " + this.arenasTableName + " WHERE id='" + var1.getDatabaseId() + "';");
    }

    public enum DatabaseType {
        MYSQL,
        SQLITE;

        DatabaseType() {
        }
    }
}
