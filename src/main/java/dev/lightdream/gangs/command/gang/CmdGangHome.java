package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.util.LocationUtils;
import dev.lightdream.gangs.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CmdGangHome extends BSubCommand {
    public CmdGangHome() {
        this.aliases.add("home");
        this.aliases.add("h");
        this.correctUsage = "/gang home <home>";
        this.permission = "gangsplus.gang.home";
        this.requiredRank = Settings.getRequiredRank("home");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleHomes) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.args[0];
            if (!this.gang.hasHome(var1)) {
                this.msg(Lang.MSG_GANG_HOME_INVALIDHOME.toMsg());
            } else if (Settings.safeCheckHomes && !LocationUtils.isSafe(this.gang.getHome(var1))) {
                this.msg(Lang.MSG_GANG_HOME_UNSAFE.toMsg());
            } else {
                Location var2 = this.gang.getHome(var1).clone();
                int var3 = Settings.getCooldown("home");
                if (this.main.getPlayerManager().isPlayerLoaded(this.player) && var3 > 0) {
                    if (!this.main.getPlayerManager().getPlayerData(this.player).isTeleportCooldownRunning()) {
                        this.msg(Lang.MSG_TELEPORT_TELEPORT.toMsg().replace("%time%", TimeUtils.getTimeString((long)var3)));
                        int var4 = Bukkit.getScheduler().runTaskLater(this.main.getCore(), () -> {
                            this.player.teleport(var2);
                            this.main.getPlayerManager().getPlayerData(this.player).setTeleportCooldownId(-1);
                        }, (long)var3 * 20L).getTaskId();
                        this.main.getPlayerManager().getPlayerData(this.player).setTeleportCooldownId(var4);
                    } else {
                        this.msg(Lang.MSG_TELEPORT_AWAITING.toMsg());
                    }
                } else {
                    this.player.teleport(var2);
                }

            }
        }
    }
}
