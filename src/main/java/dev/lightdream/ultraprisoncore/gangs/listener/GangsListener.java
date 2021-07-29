package dev.lightdream.ultraprisoncore.gangs.listener;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerJoinGangEvent;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerLeaveGangEvent;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;

public class GangsListener implements Listener {
    private GangsPlugin main;

    public GangsListener(GangsPlugin var1) {
        this.main = var1;
    }

    @EventHandler
    public void onPlayerJoinGang(PlayerJoinGangEvent var1) {
        Player var2 = var1.getPlayer();
        Gang var3 = var1.getGang();
        Iterator var4 = Settings.getEventCommands("gangJoin").iterator();

        while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var5.replace("{GANG}", var3.getName()).replace("{PLAYER}", var2.getName()));
        }

    }

    @EventHandler
    public void onPlayerLeaveGang(PlayerLeaveGangEvent var1) {
        Player var2 = var1.getPlayer();
        Gang var3 = var1.getGang();
        if (var1.getLeaveReason() == PlayerLeaveGangEvent.LeaveReason.KICKED) {
            Player var4 = var1.getKickedBy();
            Iterator var5 = Settings.getEventCommands("gangKick").iterator();

            while(var5.hasNext()) {
                String var6 = (String)var5.next();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var6.replace("{GANG}", var3.getName()).replace("{KICKEDBY}", var4.getName()).replace("{PLAYER}", var2.getName()));
            }
        } else if (var1.getLeaveReason() == PlayerLeaveGangEvent.LeaveReason.LEFT) {
            Iterator var8 = Settings.getEventCommands("gangLeave").iterator();

            while(var8.hasNext()) {
                String var7 = (String)var8.next();
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), var7.replace("{GANG}", var3.getName()).replace("{PLAYER}", var2.getName()));
            }
        }

    }
}
