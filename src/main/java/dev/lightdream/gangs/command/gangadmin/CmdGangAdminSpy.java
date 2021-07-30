package dev.lightdream.gangs.command.gangadmin;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdGangAdminSpy extends BSubCommand {
    public CmdGangAdminSpy() {
        this.aliases.add("spy");
        this.aliases.add("s");
        this.aliases.add("socialspy");
        this.correctUsage = "/gadmin spy [player]";
        this.permission = "gangsplus.gangadmin.socialspy";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length >= 1 && Bukkit.getPlayer(this.args[0]) != null) {
            Player var3 = Bukkit.getPlayer(this.args[0]);
            if (!this.main.getPlayerManager().isPlayerLoaded(var3)) {
                this.msg(Lang.MSG_ERROR.toMsg());
            } else {
                String var2 = this.toggleAdminSpy(var3);
                var3.sendMessage(Lang.MSG_GANGADMIN_SPY_TOGGLED.toMsg().replace("%state%", var2).replace("%player%", this.getSenderName()));
                this.msg(Lang.MSG_GANGADMIN_SPY_TOGGLED.toMsg().replace("%state%", var2).replace("%player%", var3.getName()));
            }
        } else if (this.isPlayer()) {
            if (!this.main.getPlayerManager().isPlayerLoaded(this.player)) {
                this.msg(Lang.MSG_ERROR.toMsg());
            } else {
                String var1 = this.toggleAdminSpy(this.player);
                this.msg(Lang.MSG_GANGADMIN_SPY_TOGGLEDBY.toMsg().replace("%state%", var1).replace("%player%", this.getSenderName()));
            }
        } else {
            this.msg(Lang.MSG_PLAYERONLY.toMsg());
        }

    }

    private String toggleAdminSpy(Player var1) {
        PlayerData var2 = this.main.getPlayerManager().getPlayerData(var1);
        String var3;
        if (var2.isEnableChatSpy()) {
            var2.setEnableChatSpy(false);
            var3 = Lang.LANG_DISABLED.toString();
        } else {
            var2.setEnableChatSpy(true);
            var3 = Lang.LANG_ENABLED.toString();
        }

        return var3;
    }
}
