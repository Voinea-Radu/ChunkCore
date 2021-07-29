package dev.lightdream.ultraprisoncore.gangs.database;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqliteDataManager extends DataManager {
    public SqliteDataManager(GangsPlugin var1) {
        super(var1);
    }

    public void setup() {
        PreparedStatement var1 = null;
        PreparedStatement var2 = null;
        PreparedStatement var3 = null;
        PreparedStatement var4 = null;

        try {
            var1 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.alliancesTableName + "` (id INTEGER PRIMARY KEY, gang1 INTEGER NOT NULL, gang2 INTEGER NOT NULL);");
            var1.executeUpdate();
            var2 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.arenasTableName + "` (id INTEGER PRIMARY KEY, textId TEXT NOT NULL, name TEXT NOT NULL, corner1 REAL NOT NULL, corner2 REAL NOT NULL, spawn1 REAL NOT NULL, spawn2 REAL NOT NULL)");
            var2.executeUpdate();
            var3 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.gangsTableName + "` (id INTEGER PRIMARY KEY, name TEXT NOT NULL, created INTEGER NOT NULL, friendlyfire INTEGER NOT NULL, level INTEGER NOT NULL, kills INTEGER NOT NULL, deaths INTEGER NOT NULL, assists INTEGER NOT NULL, fightswon INTEGER NOT NULL, fightslost INTEGER NOT NULL, bankmoney REAL NOT NULL, members TEXT NOT NULL, invitations TEXT NOT NULL, homes TEXT NOT NULL)");
            var3.executeUpdate();
            var4 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.playersTableName + " (id INTEGER PRIMARY KEY, uuid TEXT NOT NULL, name TEXT NOT NULL, registered INTEGER NOT NULL, kills INTEGER NOT NULL, deaths INTEGER NOT NULL, assists INTEGER NOT NULL);");
            var4.executeUpdate();
        } catch (SQLException var14) {
            var14.printStackTrace();
        } finally {
            try {
                var1.close();
                var2.close();
                var3.close();
                var4.close();
            } catch (SQLException var13) {
            }

            this.main.load();
        }

    }

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.main.getCore().getDataFolder().getPath() + File.separator + this.fileBackendName + ".db");
        } catch (SQLException var2) {
            this.main.getLogger().severe("Plugin disabled due to SQLite error!");
            Bukkit.getServer().getPluginManager().disablePlugin(this.main.getCore());
            var2.printStackTrace();
        } catch (ClassNotFoundException var3) {
            this.main.getLogger().severe("Plugin disabled due to no SQLite driver found!");
            Bukkit.getServer().getPluginManager().disablePlugin(this.main.getCore());
            var3.printStackTrace();
        }

    }
}
