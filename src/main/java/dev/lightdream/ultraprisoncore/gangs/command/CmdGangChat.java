package dev.lightdream.ultraprisoncore.gangs.command;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BCommand;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerGangChatEvent;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import dev.lightdream.ultraprisoncore.gangs.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdGangChat extends BCommand implements CommandExecutor {
    public CmdGangChat(GangsPlugin var1) {
        super(var1);
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (!Settings.enableModuleChat) {
            var1.sendMessage(Lang.MSG_MODULE_DISABLED.toMsg());
            return true;
        } else if (!var1.hasPermission("gangsplus.gangchat")) {
            var1.sendMessage(Lang.MSG_NOACCESS.toMsg());
            return true;
        } else if (!(var1 instanceof Player)) {
            var1.sendMessage(Lang.MSG_PLAYERONLY.toMsg());
            return true;
        } else {
            Player var5 = (Player)var1;
            if (Settings.disableGangsInWorlds.contains(var5.getWorld().getName())) {
                var1.sendMessage(Lang.MSG_GANGS_DISABLED.toMsg());
                return true;
            } else if (!this.main.getGangManager().isInGang(var5)) {
                var1.sendMessage(Lang.MSG_INGANGONLY.toMsg());
                return true;
            } else if (!this.main.getPlayerManager().isPlayerLoaded(var5)) {
                var1.sendMessage(Lang.MSG_ERROR.toMsg());
                return true;
            } else {
                Gang var6 = this.main.getGangManager().getPlayersGang(var5);
                PlayerData var7 = this.main.getPlayerManager().getPlayerData(var5);
                if (var4.length > 0) {
                    if (var4.length == 1) {
                        if (var4[0].equalsIgnoreCase("on")) {
                            var7.setUsingAllyChat(false);
                            var7.setUsingGangChat(true);
                            var1.sendMessage(Lang.MSG_GANGCHAT_TOGGLED.toMsg().replace("%state%", Lang.LANG_ENABLED.toString()));
                            return true;
                        }

                        if (var4[0].equalsIgnoreCase("off")) {
                            var7.setUsingGangChat(false);
                            var1.sendMessage(Lang.MSG_GANGCHAT_TOGGLED.toMsg().replace("%state%", Lang.LANG_DISABLED.toString()));
                            return true;
                        }
                    }

                    StringBuilder var8 = new StringBuilder(var4[0]);

                    for(int var9 = 1; var9 < var4.length; ++var9) {
                        var8.append(" " + var4[var9]);
                    }

                    String var11 = var8.toString();
                    PlayerGangChatEvent var10 = new PlayerGangChatEvent(var5, var11);
                    Bukkit.getServer().getPluginManager().callEvent(var10);
                    if (!var10.isCancelled()) {
                        var6.sendChatMessage(var10.getPlayer(), var10.getMessage());
                    }
                } else {
                    this.toggleGangChat(var1, var7);
                }

                return true;
            }
        }
    }

    private void toggleGangChat(CommandSender var1, PlayerData var2) {
        String var3;
        if (var2.isUsingGangChat()) {
            var2.setUsingGangChat(false);
            var3 = Lang.LANG_DISABLED.toString();
        } else {
            var2.setUsingAllyChat(false);
            var2.setUsingGangChat(true);
            var3 = Lang.LANG_ENABLED.toString();
        }

        var1.sendMessage(Lang.MSG_GANGCHAT_TOGGLED.toMsg().replace("%state%", var3));
    }
}
