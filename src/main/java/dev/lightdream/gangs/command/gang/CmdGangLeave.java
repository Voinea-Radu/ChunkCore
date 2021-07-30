package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.event.PlayerLeaveGangEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdGangLeave extends BSubCommand {
    public CmdGangLeave() {
        this.aliases.add("leave");
        this.aliases.add("quit");
        this.permission = "gangsplus.gang.leave";
        this.requiredRank = Settings.getRequiredRank("leave");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.gang.getMembers().size() < 2) {
            this.msg(Lang.MSG_GANG_LEAVE_NOMEMBERS.toMsg());
        } else if (this.gang.getOwner().getPlayer() != null && this.gang.getOwner().getPlayer().equals(this.player)) {
            this.msg(Lang.MSG_GANG_LEAVE_LEADER.toMsg());
        } else if (this.main.getFightManager().isGangInArena(this.gang) && this.main.getFightManager().isPlayerInArena(this.player)) {
            this.msg(Lang.MSG_GANG_LEAVE_FIGHTING.toMsg());
        } else {
            PlayerLeaveGangEvent var1 = new PlayerLeaveGangEvent(this.player, (Player)null, this.gang, PlayerLeaveGangEvent.LeaveReason.LEFT);
            Bukkit.getServer().getPluginManager().callEvent(var1);
            if (!var1.isCancelled()) {
                this.gang.removeMember(this.player);
                this.gang.save();
                if (Settings.getBroadcast("gangLeave")) {
                    Settings.broadcast(Lang.MSG_GANG_LEAVE_LEFT_BROADCAST.toString().replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                }

                this.gang.sendMessage(Lang.MSG_GANG_LEAVE_LEFT_INGANG.toString().replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                this.msg(Lang.MSG_GANG_LEAVE_LEFT_LEFT.toMsg().replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
            }

        }
    }
}
