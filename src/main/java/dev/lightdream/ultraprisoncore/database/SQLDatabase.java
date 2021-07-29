package dev.lightdream.ultraprisoncore.database;

import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.ultraprisoncore.database.implementations.MySQLDatabase;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.multipliers.multiplier.PlayerMultiplier;
import me.lucko.helper.Schedulers;
import me.lucko.helper.time.Time;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SQLDatabase extends Database {

    public static final String RANKS_TABLE_NAME = "UltraPrison_Ranks";
    public static final String TOKENS_TABLE_NAME = "UltraPrison_Tokens";
    public static final String GEMS_TABLE_NAME = "UltraPrison_Gems";
    public static final String BLOCKS_TABLE_NAME = "UltraPrison_BlocksBroken";
    public static final String BLOCKS_WEEKLY_TABLE_NAME = "UltraPrison_BlocksBrokenWeekly";
    public static final String MULTIPLIERS_TABLE_NAME = "UltraPrison_Multipliers";
    public static final String AUTOMINER_TABLE_NAME = "UltraPrison_AutoMiner";
    public static final String GANGS_TABLE_NAME = "UltraPrison_Gangs";
    public static final String FRIENDS_TABLE_NAME = "UltraPrison_Friends";
    public static final String UUID_PLAYERNAME_TABLE_NAME = "UltraPrison_Nicknames";
    public static final String RANKS_UUID_COLNAME = "UUID";
    public static final String RANKS_RANK_COLNAME = "id_rank";
    public static final String RANKS_PRESTIGE_COLNAME = "id_prestige";
    public static final String TOKENS_UUID_COLNAME = "UUID";
    public static final String TOKENS_TOKENS_COLNAME = "Tokens";
    public static final String GEMS_UUID_COLNAME = "UUID";
    public static final String GEMS_GEMS_COLNAME = "Gems";
    public static final String BLOCKS_UUID_COLNAME = "UUID";
    public static final String BLOCKS_BLOCKS_COLNAME = "Blocks";
    public static final String MULTIPLIERS_UUID_COLNAME = "UUID";
    public static final String MULTIPLIERS_MULTIPLIER_COLNAME = "vote_multiplier";
    public static final String MULTIPLIERS_TIMELEFT_COLNAME = "vote_multiplier_timeleft";
    public static final String UUID_PLAYERNAME_UUID_COLNAME = "UUID";
    public static final String UUID_PLAYERNAME_NICK_COLNAME = "nickname";
    public static final String AUTOMINER_UUID_COLNAME = "UUID";
    public static final String AUTOMINER_TIME_COLNAME = "time";
    public static final String GANGS_NAME_COLNAME = "name";
    public static final String GANGS_OWNER_COLNAME = "owner";
    public static final String GANGS_MEMBERS_COLNAME = "members";
    public static final String GANGS_VALUE_COLNAME = "value";
    public static final String FRIENDS_UUID_COLNAME = "UUID";
    public static final String FRIENDS_FRIENDS_COLNAME = "friends";
    public static final String[] ALL_TABLES = new String[]{
            RANKS_TABLE_NAME,
            TOKENS_TABLE_NAME,
            GEMS_TABLE_NAME,
            BLOCKS_TABLE_NAME,
            BLOCKS_WEEKLY_TABLE_NAME,
            MULTIPLIERS_TABLE_NAME,
            AUTOMINER_TABLE_NAME,
            GANGS_TABLE_NAME,
            UUID_PLAYERNAME_TABLE_NAME,
            FRIENDS_TABLE_NAME
    };
    protected static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);
    protected static final int MAXIMUM_POOL_SIZE = (Runtime.getRuntime().availableProcessors() * 2) + 1;
    protected static final int MINIMUM_IDLE = Math.min(MAXIMUM_POOL_SIZE, 10);
    protected static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30); // 30 Minutes
    protected static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(10); // 10 seconds
    protected static final long LEAK_DETECTION_THRESHOLD = TimeUnit.SECONDS.toMillis(10); // 10 seconds
    protected UltraPrisonCore plugin;
    protected HikariDataSource hikari;

    public SQLDatabase(UltraPrisonCore plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public abstract void connect();

    public void close() {
        if (this.hikari != null) {
            this.hikari.close();
            this.plugin.getLogger().info("Closing SQL Connection");
        }
    }

    //Always execute async!
    public synchronized void execute(String sql, Object... replacements) {
        try (Connection c = this.hikari.getConnection(); PreparedStatement statement = c.prepareStatement(sql)) {
            if (replacements != null) {
                for (int i = 0; i < replacements.length; i++) {
                    statement.setObject(i + 1, replacements[i]);
                }
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void executeAsync(String sql, Object... replacements) {
        Schedulers.async().run(() -> {
            this.execute(sql, replacements);
        });
    }

    public void createTables() {
        Schedulers.async().run(() -> {
            execute("CREATE TABLE IF NOT EXISTS " + RANKS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, id_rank int, id_prestige bigint, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + TOKENS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, Tokens bigint, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + GEMS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, Gems bigint, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + BLOCKS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, Blocks bigint, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + BLOCKS_WEEKLY_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, Blocks bigint, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + MULTIPLIERS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, vote_multiplier double, vote_multiplier_timeleft long, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + AUTOMINER_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, time int, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + GANGS_TABLE_NAME + "(name varchar(36) NOT NULL UNIQUE, owner varchar(36) NOT NULL, value int default 0, members text, primary key (name))");
            execute("CREATE TABLE IF NOT EXISTS " + UUID_PLAYERNAME_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, nickname varchar(16) NOT NULL, primary key (UUID))");
            execute("CREATE TABLE IF NOT EXISTS " + FRIENDS_TABLE_NAME + "(UUID varchar(36) NOT NULL UNIQUE, friends text, primary key (UUID))");
        });
    }

    @Override
    public void resetAllData(CommandSender sender) {
        Schedulers.async().run(() -> {
            for (String table : ALL_TABLES) {
                if (table == null || table.isEmpty() || table.equals(UUID_PLAYERNAME_TABLE_NAME)) {
                    continue;
                }
                execute("TRUNCATE TABLE " + table);
            }
        });
    }

    @Override
    public void updatePlayerNickname(OfflinePlayer player) {
        this.executeAsync("INSERT INTO " + MySQLDatabase.UUID_PLAYERNAME_TABLE_NAME + " VALUES(?,?) ON DUPLICATE KEY UPDATE " + MySQLDatabase.UUID_PLAYERNAME_NICK_COLNAME + "=?", player.getUniqueId().toString(), player.getName(), player.getName());
    }

    @Override
    public long getPlayerTokens(OfflinePlayer p) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.TOKENS_TABLE_NAME + " WHERE " + MySQLDatabase.TOKENS_UUID_COLNAME + "=?")) {
            statement.setString(1, p.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong(MySQLDatabase.TOKENS_TOKENS_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateTokens(OfflinePlayer p, long amount) {
        this.executeAsync("UPDATE " + MySQLDatabase.TOKENS_TABLE_NAME + " SET " + MySQLDatabase.TOKENS_TOKENS_COLNAME + "=? WHERE " + MySQLDatabase.TOKENS_UUID_COLNAME + "=?", amount, p.getUniqueId().toString());
    }

    @Override
    public void resetBlocksWeekly(CommandSender sender) {
        this.execute("DELETE FROM " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME);
    }

    @Override
    public long getPlayerGems(OfflinePlayer p) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.GEMS_TABLE_NAME + " WHERE " + MySQLDatabase.GEMS_UUID_COLNAME + "=?")) {
            statement.setString(1, p.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong(MySQLDatabase.GEMS_GEMS_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateGems(OfflinePlayer p, long newAmount) {
        this.execute("UPDATE " + MySQLDatabase.GEMS_TABLE_NAME + " SET " + MySQLDatabase.GEMS_GEMS_COLNAME + "=? WHERE " + MySQLDatabase.GEMS_UUID_COLNAME + "=?", newAmount, p.getUniqueId().toString());
    }

    @Override
    public void updateRankAndPrestige(OfflinePlayer player, int newRank, long newPrestige) {
        this.execute("UPDATE " + MySQLDatabase.RANKS_TABLE_NAME + " SET " + MySQLDatabase.RANKS_RANK_COLNAME + "=?," + MySQLDatabase.RANKS_PRESTIGE_COLNAME + "=? WHERE " + MySQLDatabase.RANKS_UUID_COLNAME + "=?", newRank, newPrestige, player.getUniqueId().toString());
    }

    @Override
    public int getPlayerRank(OfflinePlayer player) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.RANKS_TABLE_NAME + " WHERE " + MySQLDatabase.RANKS_UUID_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getInt(SQLDatabase.RANKS_RANK_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getPlayerPrestige(OfflinePlayer player) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.RANKS_TABLE_NAME + " WHERE " + MySQLDatabase.RANKS_UUID_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong(SQLDatabase.RANKS_PRESTIGE_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void removeExpiredAutoMiners() {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("DELETE FROM " + MySQLDatabase.AUTOMINER_TABLE_NAME + " WHERE " + MySQLDatabase.AUTOMINER_TIME_COLNAME + " <= 0")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPlayerAutoMinerTime(OfflinePlayer p) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.AUTOMINER_TABLE_NAME + " WHERE " + MySQLDatabase.AUTOMINER_UUID_COLNAME + "=?")) {
            statement.setString(1, p.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getInt(MySQLDatabase.AUTOMINER_TIME_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public PlayerMultiplier getPlayerPersonalMultiplier(OfflinePlayer player) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.MULTIPLIERS_TABLE_NAME + " WHERE " + MySQLDatabase.MULTIPLIERS_UUID_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    double multiplier = set.getDouble(MySQLDatabase.MULTIPLIERS_MULTIPLIER_COLNAME);
                    long endTime = set.getLong(MySQLDatabase.MULTIPLIERS_TIMELEFT_COLNAME);
                    if (endTime > Time.nowMillis()) {
                        return new PlayerMultiplier(player.getUniqueId(), multiplier, endTime);
                    }
                }
            }
        } catch (SQLException e) {
            this.plugin.getLogger().warning("Could not load multiplier for player " + player.getName() + "!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeExpiredMultipliers() {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("DELETE FROM " + MySQLDatabase.MULTIPLIERS_TABLE_NAME + " WHERE " + MySQLDatabase.MULTIPLIERS_TIMELEFT_COLNAME + " < " + Time.nowMillis())) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getPlayerBrokenBlocks(OfflinePlayer player) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.BLOCKS_TABLE_NAME + " WHERE " + MySQLDatabase.BLOCKS_UUID_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong(MySQLDatabase.BLOCKS_BLOCKS_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getPlayerBrokenBlocksWeekly(OfflinePlayer player) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME + " WHERE " + MySQLDatabase.BLOCKS_UUID_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getLong(MySQLDatabase.BLOCKS_BLOCKS_COLNAME);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateBlocks(OfflinePlayer player, long newAmount) {
        this.execute("UPDATE " + MySQLDatabase.BLOCKS_TABLE_NAME + " SET " + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + "=? WHERE " + MySQLDatabase.BLOCKS_UUID_COLNAME + "=?", newAmount, player.getUniqueId().toString());
    }

    @Override
    public void updateBlocksWeekly(OfflinePlayer player, long newAmount) {
        this.execute("UPDATE " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME + " SET " + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + "=? WHERE " + MySQLDatabase.BLOCKS_UUID_COLNAME + "=?", newAmount, player.getUniqueId().toString());
    }

    @Override
    public void savePersonalMultiplier(Player player, PlayerMultiplier multiplier) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT INTO " + MySQLDatabase.MULTIPLIERS_TABLE_NAME + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE " + MySQLDatabase.MULTIPLIERS_MULTIPLIER_COLNAME + "=?, " + MySQLDatabase.MULTIPLIERS_TIMELEFT_COLNAME + "=?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, multiplier.getMultiplier());
            statement.setLong(3, multiplier.getEndTime());
            statement.setDouble(4, multiplier.getMultiplier());
            statement.setLong(5, multiplier.getEndTime());
            statement.execute();
        } catch (SQLException e) {
            this.plugin.getLogger().warning("Could not save multiplier for player " + player.getName() + "!");
            e.printStackTrace();
        }
    }

    @Override
    public void saveAutoMiner(Player p, int timeLeft) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT INTO " + MySQLDatabase.AUTOMINER_TABLE_NAME + " VALUES (?,?) ON DUPLICATE KEY UPDATE " + MySQLDatabase.AUTOMINER_TIME_COLNAME + "=?")) {
            statement.setString(1, p.getUniqueId().toString());
            statement.setInt(2, timeLeft);
            statement.setInt(3, timeLeft);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<UUID, Integer> getTop10Prestiges() {
        Map<UUID, Integer> top10Prestige = new LinkedHashMap<>();
        try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT " + MySQLDatabase.RANKS_UUID_COLNAME + "," + MySQLDatabase.RANKS_PRESTIGE_COLNAME + " FROM " + MySQLDatabase.RANKS_TABLE_NAME + " ORDER BY " + MySQLDatabase.RANKS_PRESTIGE_COLNAME + " DESC LIMIT 10").executeQuery()) {
            while (set.next()) {
                top10Prestige.put(UUID.fromString(set.getString(MySQLDatabase.RANKS_UUID_COLNAME)), set.getInt(MySQLDatabase.RANKS_PRESTIGE_COLNAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10Prestige;
    }

    @Override
    public Map<UUID, Long> getTop10Gems() {
        Map<UUID, Long> top10Gems = new LinkedHashMap<>();
        try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT " + MySQLDatabase.GEMS_UUID_COLNAME + "," + MySQLDatabase.GEMS_GEMS_COLNAME + " FROM " + MySQLDatabase.GEMS_TABLE_NAME + " ORDER BY " + MySQLDatabase.GEMS_GEMS_COLNAME + " DESC LIMIT 10").executeQuery()) {
            while (set.next()) {
                top10Gems.put(UUID.fromString(set.getString(MySQLDatabase.GEMS_UUID_COLNAME)), set.getLong(MySQLDatabase.GEMS_GEMS_COLNAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10Gems;
    }

    @Override
    public Map<UUID, Long> getTop10BlocksWeekly() {
        Map<UUID, Long> top10BlocksWeekly = new LinkedHashMap<>();
        try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT " + MySQLDatabase.BLOCKS_UUID_COLNAME + "," + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + " FROM " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME + " ORDER BY " + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + " DESC").executeQuery()) {
            while (set.next()) {
                top10BlocksWeekly.put(UUID.fromString(set.getString(MySQLDatabase.BLOCKS_UUID_COLNAME)), set.getLong(MySQLDatabase.BLOCKS_BLOCKS_COLNAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10BlocksWeekly;
    }

    @Override
    public Map<UUID, Long> getTop10Tokens() {
        Map<UUID, Long> top10Tokens = new LinkedHashMap<>();
        try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT " + MySQLDatabase.TOKENS_UUID_COLNAME + "," + MySQLDatabase.TOKENS_TOKENS_COLNAME + " FROM " + MySQLDatabase.TOKENS_TABLE_NAME + " ORDER BY " + MySQLDatabase.TOKENS_TOKENS_COLNAME + " DESC LIMIT 10").executeQuery()) {
            while (set.next()) {
                top10Tokens.put(UUID.fromString(set.getString(MySQLDatabase.TOKENS_UUID_COLNAME)), set.getLong(MySQLDatabase.TOKENS_TOKENS_COLNAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10Tokens;
    }

    @Override
    public Map<UUID, Long> getTop10Blocks() {
        Map<UUID, Long> top10Blocks = new LinkedHashMap<>();
        try (Connection con = this.hikari.getConnection(); ResultSet set = con.prepareStatement("SELECT " + MySQLDatabase.BLOCKS_UUID_COLNAME + "," + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + " FROM " + MySQLDatabase.BLOCKS_TABLE_NAME + " ORDER BY " + MySQLDatabase.BLOCKS_BLOCKS_COLNAME + " DESC LIMIT 10").executeQuery()) {
            while (set.next()) {
                top10Blocks.put(UUID.fromString(set.getString(MySQLDatabase.BLOCKS_UUID_COLNAME)), set.getLong(MySQLDatabase.BLOCKS_BLOCKS_COLNAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top10Blocks;
    }

    @Override
    public void addIntoTokens(OfflinePlayer player) {
        this.execute("INSERT IGNORE INTO " + MySQLDatabase.TOKENS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
    }

    @Override
    public void addIntoRanksAndPrestiges(OfflinePlayer player) {
        this.execute("INSERT IGNORE INTO " + MySQLDatabase.RANKS_TABLE_NAME + " VALUES(?,?,?)", player.getUniqueId().toString(), 0, 0);
    }

    @Override
    public void addIntoBlocks(OfflinePlayer player) {
        this.execute("INSERT IGNORE INTO " + MySQLDatabase.BLOCKS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
    }

    @Override
    public void addIntoBlocksWeekly(OfflinePlayer player) {
        this.execute("INSERT IGNORE INTO " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
    }

    @Override
    public void addIntoGems(OfflinePlayer player) {
        this.execute("INSERT IGNORE INTO " + MySQLDatabase.GEMS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
    }


    @Override
    public List<UUID> getFriends(UUID uuid) {
        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT * FROM " + FRIENDS_TABLE_NAME + " WHERE " + FRIENDS_UUID_COLNAME + "=?")) {
            statement.setString(1, uuid.toString());
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    String friends = set.getString(FRIENDS_FRIENDS_COLNAME);
                    List<UUID> output = new ArrayList<>();
                    for (String uuidStr : friends.split("\\|")) {
                        if (!uuidStr.equals("")) {
                            output.add(UUID.fromString(uuidStr));
                        }
                    }
                    return output;
                }
            }
        } catch (SQLException e) {
            this.plugin.getLogger().warning("Could not load friends for uuid " + uuid.toString() + "!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public void setFriends(UUID uuid, List<UUID> friends) {
        StringBuilder friendsStr = new StringBuilder();
        for (UUID friend : friends) {
            friendsStr.append(friend.toString()).append("|");
        }

        try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT INTO " + FRIENDS_TABLE_NAME + " VALUES(?,?) ON DUPLICATE KEY UPDATE " + MySQLDatabase.MULTIPLIERS_MULTIPLIER_COLNAME + "=?")) {
            statement.setString(1, uuid.toString());
            statement.setString(2, friendsStr.toString());
            statement.setString(3, friendsStr.toString());
            statement.execute();
        } catch (SQLException e) {
            this.plugin.getLogger().warning("Could not save multiplier for uuid " + uuid.toString() + "!");
            e.printStackTrace();
        }
    }

    @Override
    public void addFriend(UUID uuid, UUID friend) {
        if (!checkFriends(uuid, friend)) {
            List<UUID> friends = getFriends(uuid);
            friends.add(friend);
            setFriends(uuid, friends);
        }
    }

    @Override
    public void removeFriend(UUID uuid, UUID friend) {
        if (checkFriends(uuid, friend)) {
            List<UUID> friends = getFriends(uuid);
            friends.remove(friend);
            setFriends(uuid, friends);
        }
    }

    @Override
    public boolean checkFriends(UUID uuid, UUID friend) {
        List<UUID> friends = getFriends(uuid);
        return friends.contains(friend);
    }
}
