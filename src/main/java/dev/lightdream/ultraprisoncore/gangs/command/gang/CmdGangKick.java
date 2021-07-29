package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.event.PlayerLeaveGangEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangKick extends BSubCommand {
    public CmdGangKick() {
        this.aliases.add("kick");
        this.aliases.add("remove");
        this.aliases.add("k");
        this.correctUsage = "/gang kick <player>";
        this.permission = "gangsplus.gang.kick";
        this.requiredRank = Settings.getRequiredRank("kick");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            OfflinePlayer var1 = Bukkit.getOfflinePlayer(this.args[0]);
            if (var1 == null) {
                this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
            } else if (!this.gang.isMember(var1)) {
                this.msg(Lang.MSG_GANG_KICK_NOTINGANG.toMsg().replace("%player%", var1.getName()));
            } else {
                boolean var2 = var1.getPlayer() != null;
                if (var2 && var1.getPlayer().equals(this.player)) {
                    this.msg(Lang.MSG_GANG_KICK_OWNSELF.toMsg());
                } else if (this.gang.getOwner().equals(var1)) {
                    this.msg(Lang.MSG_GANG_KICK_LEADER.toMsg());
                } else if (var2 && this.main.getFightManager().isGangInArena(this.gang) && this.main.getFightManager().isPlayerInArena(var1.getPlayer())) {
                    this.msg(Lang.MSG_GANG_KICK_FIGHTING.toMsg().replace("%player%", var1.getName()));
                } else {
                    PlayerLeaveGangEvent var3 = new PlayerLeaveGangEvent(var1.getPlayer(), this.player, this.gang, PlayerLeaveGangEvent.LeaveReason.KICKED);
                    Bukkit.getServer().getPluginManager().callEvent(var3);
                    if (!var3.isCancelled()) {
                        this.gang.removeMember(var1);
                        this.gang.save();
                        if (Settings.getBroadcast("gangKick")) {
                            Settings.broadcast(Lang.MSG_GANG_KICK_KICKED_BROADCAST.toString().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                        }

                        this.gang.sendMessage(Lang.MSG_GANG_KICK_KICKED_INGANG.toString().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                        if (var2) {
                            var1.getPlayer().sendMessage(Lang.MSG_GANG_KICK_KICKED_BY.toMsg().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                        }

                        this.msg(Lang.MSG_GANG_KICK_KICKED_KICKED.toMsg().replace("%target%", var1.getName()).replace("%player%", this.player.getName()).replace("%gang%", this.gang.getName()));
                    }

                }
            }
        }
    }
}
