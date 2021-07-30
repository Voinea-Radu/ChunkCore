package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.event.PlayerLeaveGangEvent;
import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.player.PlayerLastAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CmdGangConfirm extends BSubCommand {
    public CmdGangConfirm() {
        this.aliases.add("confirm");
        this.correctUsage = "/gang confirm";
        this.permission = "gangsplus.gang.confirm";
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        PlayerData var1 = this.main.getPlayerManager().getPlayerData(this.player);
        PlayerLastAction var2 = var1.getLastAction();
        if (var2 == PlayerLastAction.NONE) {
            this.msg(Lang.MSG_GANG_CONFIRM_NONE.toMsg());
        } else if (var2 == PlayerLastAction.GANG_DISBAND) {
            var1.setLastAction(PlayerLastAction.NONE);
            String var3 = this.gang.getName();
            PlayerLeaveGangEvent var4 = new PlayerLeaveGangEvent(this.player, (Player)null, this.gang, PlayerLeaveGangEvent.LeaveReason.DISBANDED_LEADER);
            Bukkit.getServer().getPluginManager().callEvent(var4);
            if (!var4.isCancelled()) {
                Iterator var5 = this.gang.getOnlineMembers().iterator();

                while(var5.hasNext()) {
                    Player var6 = (Player)var5.next();
                    if (!var6.equals(this.player)) {
                        PlayerLeaveGangEvent var7 = new PlayerLeaveGangEvent(var6, (Player)null, this.gang, PlayerLeaveGangEvent.LeaveReason.DISBANDED);
                        Bukkit.getServer().getPluginManager().callEvent(var7);
                    }
                }

                this.main.getGangManager().removeGang(this.gang);
                if (Settings.getBroadcast("gangDisband")) {
                    Settings.broadcast(Lang.MSG_GANG_DISBAND_BROADCAST.toString().replace("%gang%", var3).replace("%player%", this.player.getName()));
                }

                this.msg(Lang.MSG_GANG_DISBAND_DISBANDED.toMsg().replace("%gang%", var3));
            }
        }

    }
}
