package dev.lightdream.gangs.util;

import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.gang.Gang;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.Iterator;

public class StringUtils {
    public StringUtils() {
    }

    public static String fixColors(String var0) {
        return var0 == null ? "" : ChatColor.translateAlternateColorCodes('&', var0);
    }

    public static String stripColors(String var0) {
        return ChatColor.stripColor(fixColors(var0));
    }

    public static String formatDoubleString(double var0) {
        DecimalFormat var2 = new DecimalFormat("#.##");
        var2.setMaximumIntegerDigits(32);
        return var0 % (double) ((int) var0) == 0.0D ? var2.format(var0).split("\\.")[0] : var2.format(var0).replace(",", ".");
    }

    public static String formatPlayerInfo(OfflinePlayer var0, PlayerData var1) {
        StringBuilder var2 = new StringBuilder();
        Iterator var3 = Settings.getCommand("playerinfo").iterator();

        while (var3.hasNext()) {
            String var4 = (String) var3.next();
            if (var4.contains("{INGANG}")) {
                if (GangsPlugin.getInstance().getGangManager().isInGang(var0)) {
                    Gang var5 = GangsPlugin.getInstance().getGangManager().getPlayersGang(var0);
                    var2.append(fixColors(var1.formatPlaceholders(var4)).replace("{INGANG}", "").replace("%gang%", var5.getName()).replace("%gangrank%", Settings.getRankName(var5.getMemberData(var0).getRank()))).append("\n");
                }
            } else {
                var2.append(fixColors(var1.formatPlaceholders(var4))).append("\n");
            }
        }

        return var2.toString();
    }
}
