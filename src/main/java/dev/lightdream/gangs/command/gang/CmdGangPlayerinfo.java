package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.database.Callback;
import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangPlayerinfo extends BSubCommand {
    public CmdGangPlayerinfo() {
        this.aliases.add("playerinfo");
        this.aliases.add("infoplayer");
        this.aliases.add("pinfo");
        this.aliases.add("infop");
        this.aliases.add("player");
        this.aliases.add("player");
        this.correctUsage = "/gang playerinfo <player>";
        this.permission = "gangsplus.gang.playerinfo";
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            final OfflinePlayer var1 = Bukkit.getOfflinePlayer(this.args[0]);
            if (var1 == null) {
                this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
            } else {
                if (this.main.getPlayerManager().isPlayerLoaded(var1)) {
                    this.msg(StringUtils.formatPlayerInfo(var1, this.main.getPlayerManager().getPlayerData(var1)));
                } else {
                    this.main.getDataManager().retrievePlayerData(var1.getName(), new Callback<PlayerData>() {
                        public void onSuccess(PlayerData var1x) {
                            CmdGangPlayerinfo.this.msg(StringUtils.formatPlayerInfo(var1, var1x));
                        }

                        public void onFailure(PlayerData var1x) {
                            CmdGangPlayerinfo.this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
                        }
                    });
                }

            }
        }
    }
}
