package dev.lightdream.gangs.command.gangadmin;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.database.Callback;
import dev.lightdream.gangs.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CmdGangAdminReset extends BSubCommand {
    public CmdGangAdminReset() {
        this.aliases.add("reset");
        this.correctUsage = "/gadmin reset <kills|deaths|assists> <player>";
        this.permission = "gangsplus.gangadmin.reset";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 2) {
            this.sendCorrectUsage();
        } else {
            final String var1 = this.args[0];
            String var2 = this.args[1];
            OfflinePlayer var3 = Bukkit.getOfflinePlayer(var2);
            if (var3 == null) {
                this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
            } else {
                if (this.main.getPlayerManager().isPlayerLoaded(var3)) {
                    PlayerData var4 = this.main.getPlayerManager().getPlayerData(var3);
                    this.reset(var1, var4);
                } else {
                    this.main.getDataManager().retrievePlayerData(var3.getName(), new Callback<PlayerData>() {
                        public void onSuccess(PlayerData var1x) {
                            if (var1x == null) {
                                CmdGangAdminReset.this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
                            } else {
                                CmdGangAdminReset.this.reset(var1, var1x);
                            }
                        }

                        public void onFailure(PlayerData var1x) {
                            CmdGangAdminReset.this.msg(Lang.MSG_ERROR.toMsg());
                        }
                    });
                }

            }
        }
    }

    private void reset(String var1, PlayerData var2) {
        if (var1.equalsIgnoreCase("kills")) {
            var2.setKills(0);
        } else if (var1.equalsIgnoreCase("deaths")) {
            var2.setDeaths(0);
        } else {
            if (!var1.equalsIgnoreCase("assists")) {
                this.sendCorrectUsage();
                return;
            }

            var2.setAssists(0);
        }

        var2.save();
    }
}
