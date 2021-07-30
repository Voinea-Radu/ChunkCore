package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.database.Callback;
import dev.lightdream.gangs.gang.Gang;
import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Iterator;

public class CmdGangInfo extends BSubCommand {
    public CmdGangInfo() {
        this.aliases.add("info");
        this.aliases.add("gang");
        this.aliases.add("i");
        this.aliases.add("gang");
        this.correctUsage = "/gang info <gang>";
        this.permission = "gangsplus.gang.info";
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        Gang var1 = null;
        String var2;
        if (this.args.length < 1) {
            if (!this.isInGang()) {
                this.sendCorrectUsage();
                return;
            }

            var1 = this.gang;
        } else {
            var2 = this.buildStringFromArgs(0, this.args.length - 1);
            if (!this.main.getGangManager().isGang(var2)) {
                final OfflinePlayer var5 = Bukkit.getOfflinePlayer(this.args[0]);
                if (var5 != null) {
                    if (this.main.getPlayerManager().isPlayerLoaded(var5)) {
                        this.msg(StringUtils.formatPlayerInfo(var5, this.main.getPlayerManager().getPlayerData(var5)));
                    } else {
                        this.main.getDataManager().retrievePlayerData(var5.getName(), new Callback<PlayerData>() {
                            public void onSuccess(PlayerData var1) {
                                CmdGangInfo.this.msg(StringUtils.formatPlayerInfo(var5, var1));
                            }

                            public void onFailure(PlayerData var1) {
                                CmdGangInfo.this.msg(Lang.MSG_INVALIDPLAYER.toMsg());
                            }
                        });
                    }

                    return;
                }

                this.msg(Lang.MSG_INVALIDGANG.toMsg());
                return;
            }

            var1 = this.main.getGangManager().getGang(var2);
        }

        var2 = "";
        Iterator var3 = Settings.getCommand("info").iterator();

        while(true) {
            String var4;
            do {
                if (!var3.hasNext()) {
                    this.msg(var2);
                    return;
                }

                var4 = (String)var3.next();
            } while(Settings.enableModuleBank && var4.contains("%bank%") && !Settings.showBankBalanceToOthers && !var1.isMember(this.player) && !this.player.hasPermission("gangsplus.checkbalance"));

            var2 = var2 + StringUtils.fixColors(var1.formatPlaceholders(var4)) + "\n";
        }
    }
}
