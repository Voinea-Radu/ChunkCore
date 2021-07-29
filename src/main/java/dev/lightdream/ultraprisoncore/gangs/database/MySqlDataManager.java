package dev.lightdream.ultraprisoncore.gangs.database;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlDataManager extends DataManager {
    public MySqlDataManager(GangsPlugin var1) {
        super(var1);
    }

    public void setup() {
        PreparedStatement var1 = null;
        PreparedStatement var2 = null;
        PreparedStatement var3 = null;
        PreparedStatement var4 = null;

        try {
            var1 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.alliancesTableName + "` (`id` int(11) NOT NULL AUTO_INCREMENT, `gang1` int(32) NOT NULL, `gang2` int(32) NOT NULL, PRIMARY KEY (`id`));");
            var1.executeUpdate();
            var2 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.arenasTableName + "` (`id` int(11) NOT NULL AUTO_INCREMENT, `textId` varchar(32) CHARACTER SET utf8 NOT NULL, `name` varchar(32) CHARACTER SET utf8 NOT NULL, `corner1` varchar(128) CHARACTER SET utf8 NOT NULL, `corner2` varchar(128) CHARACTER SET utf8 NOT NULL, `spawn1` varchar(128) CHARACTER SET utf8 NOT NULL, `spawn2` varchar(128) CHARACTER SET utf8 NOT NULL, PRIMARY KEY (`id`))");
            var2.executeUpdate();
            var3 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + this.gangsTableName + "` (`id` int(11) NOT NULL AUTO_INCREMENT, `name` varchar(32) CHARACTER SET utf8 NOT NULL, `created` bigint(64) NOT NULL, `friendlyfire` tinyint(1) NOT NULL, `level` int(32) NOT NULL, `kills` int(32) NOT NULL, `deaths` int(32) NOT NULL, `assists` int(32) NOT NULL, `fightswon` int(32) NOT NULL, `fightslost` int(32) NOT NULL, `bankmoney` double NOT NULL, `members` TEXT NOT NULL, `invitations` TEXT NOT NULL, `homes` TEXT NOT NULL, PRIMARY KEY (`id`))");
            var3.executeUpdate();
            var4 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.playersTableName + " (`id` int(11) NOT NULL AUTO_INCREMENT, `uuid` char(36) CHARACTER SET utf8 NOT NULL, `name` varchar(16) CHARACTER SET utf8 NOT NULL, `registered` bigint(64) NOT NULL, `kills` int(32) NOT NULL, `deaths` int(32) NOT NULL, `assists` int(32) NOT NULL, PRIMARY KEY (`id`));");
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
            Class.forName("com.mysql.jdbc.Driver");
            String var1 = "jdbc:mysql://" + Settings.databaseMySQLHost + ":" + Settings.databaseMySQLPort + "/" + Settings.databaseMySQLDatabase + "?characterEncoding=utf8&autoReconnect=true&useSSL=false";
            this.connection = DriverManager.getConnection(var1, Settings.databaseMySQLUser, Settings.databaseMySQLPassword);
        } catch (SQLException var2) {
            this.main.getLogger().severe("Plugin disabled due to MySQL error!");
            Bukkit.getServer().getPluginManager().disablePlugin(this.main.getCore());
            var2.printStackTrace();
        } catch (ClassNotFoundException var3) {
            this.main.getLogger().severe("Plugin disabled due to no MySQL driver found!");
            Bukkit.getServer().getPluginManager().disablePlugin(this.main.getCore());
            var3.printStackTrace();
        }

    }
}
