package dev.lightdream.database.implementations;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.UltraPrisonCore;
import dev.lightdream.database.SQLDatabase;
import dev.lightdream.multipliers.multiplier.PlayerMultiplier;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SQLiteDatabase extends SQLDatabase {

	private static final String FILE_NAME = "playerdata.db";
	private String filePath;

	public SQLiteDatabase(UltraPrisonCore plugin) {
		super(plugin);

		this.plugin.getLogger().info("Using SQLite (local) database.");

		this.filePath = this.plugin.getDataFolder().getPath() + File.separator + FILE_NAME;
		this.plugin.getLogger().info(String.format("Path to SQLite Database %s is %s", FILE_NAME, this.filePath));
		this.createDBFile();

		this.connect();
	}

	@Override
	public void connect() {

		final HikariConfig hikari = new HikariConfig();

		hikari.setPoolName("ultraprison-" + POOL_COUNTER.getAndIncrement());

		hikari.setDriverClassName("org.sqlite.JDBC");
		hikari.setJdbcUrl("jdbc:sqlite:" + this.filePath);
		hikari.setConnectionTestQuery("SELECT 1");

		hikari.setMinimumIdle(MINIMUM_IDLE);
		hikari.setMaxLifetime(MAX_LIFETIME);
		hikari.setConnectionTimeout(CONNECTION_TIMEOUT);
		hikari.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
		hikari.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD);

		this.hikari = new HikariDataSource(hikari);

		this.createTables();
	}

	private void createDBFile() {
		File yourFile = new File(this.filePath);
		try {
			yourFile.createNewFile();
		} catch (IOException e) {
			this.plugin.getLogger().warning(String.format("Unable to create %s", FILE_NAME));
			e.printStackTrace();
		}
	}

	@Override
	public void addIntoTokens(OfflinePlayer player) {
		this.execute("INSERT OR IGNORE INTO " + MySQLDatabase.TOKENS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
	}

	@Override
	public void addIntoRanksAndPrestiges(OfflinePlayer player) {
		this.execute("INSERT OR IGNORE INTO " + MySQLDatabase.RANKS_TABLE_NAME + " VALUES(?,?,?)", player.getUniqueId().toString(), 0, 0);
	}

	@Override
	public void addIntoBlocks(OfflinePlayer player) {
		this.execute("INSERT OR IGNORE INTO " + MySQLDatabase.BLOCKS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
	}


	@Override
	public void addIntoBlocksWeekly(OfflinePlayer player) {
		this.execute("INSERT OR IGNORE INTO " + MySQLDatabase.BLOCKS_WEEKLY_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
	}

	@Override
	public void addIntoGems(OfflinePlayer player) {
		this.execute("INSERT OR IGNORE INTO " + MySQLDatabase.GEMS_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), 0);
	}

	@Override
	public void saveAutoMiner(Player p, int timeLeft) {
		try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT OR REPLACE INTO " + MySQLDatabase.AUTOMINER_TABLE_NAME + " VALUES (?,?) ")) {
			statement.setString(1, p.getUniqueId().toString());
			statement.setInt(2, timeLeft);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void savePersonalMultiplier(Player player, PlayerMultiplier multiplier) {
		try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT OR REPLACE INTO " + MySQLDatabase.MULTIPLIERS_TABLE_NAME + " VALUES(?,?,?)")) {
			statement.setString(1, player.getUniqueId().toString());
			statement.setDouble(2, multiplier.getMultiplier());
			statement.setLong(3, multiplier.getEndTime());
			statement.execute();
		} catch (SQLException e) {
			this.plugin.getLogger().warning("Could not save multiplier for player " + player.getName() + "!");
			e.printStackTrace();
		}
	}

	@Override
	public void updatePlayerNickname(OfflinePlayer player) {
		this.executeAsync("INSERT OR REPLACE INTO " + MySQLDatabase.UUID_PLAYERNAME_TABLE_NAME + " VALUES(?,?)", player.getUniqueId().toString(), player.getName());
	}

	@Override
	public void setFriends(UUID uuid, List<UUID> friends) {
		StringBuilder friendsStr = new StringBuilder();
		for (UUID friend : friends) {
			friendsStr.append(friend.toString()).append("|");
		}

		try (Connection con = this.hikari.getConnection(); PreparedStatement statement = con.prepareStatement("INSERT OR REPLACE INTO " + FRIENDS_TABLE_NAME + " VALUES(?,?)")) {
			statement.setString(1, uuid.toString());
			statement.setString(2, friendsStr.toString());
			statement.execute();
		} catch (SQLException e) {
			this.plugin.getLogger().warning("Could not save multiplier for uuid " + uuid.toString() + "!");
			e.printStackTrace();
		}
	}
}
