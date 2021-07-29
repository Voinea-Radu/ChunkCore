package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.event.GangLevelUpEvent;
import dev.lightdream.ultraprisoncore.gangs.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Iterator;

public class CmdGangLevelup extends BSubCommand {
    public CmdGangLevelup() {
        this.aliases.add("levelup");
        this.aliases.add("level");
        this.aliases.add("lvlup");
        this.aliases.add("lvl");
        this.permission = "gangsplus.gang.levelup";
        this.requiredRank = Settings.getRequiredRank("levelup");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.gang.getLevel() >= Settings.maxGangLevel) {
            this.msg(Lang.MSG_GANG_LEVELUP_MAX.toMsg());
        } else {
            double var1 = Settings.getPriceLevelup(this.gang.getLevel());
            if (Settings.enableModuleBank && !this.gang.has(var1)) {
                this.msg(Lang.MSG_GANG_LEVELUP_GANGCANTAFFORD.toMsg().replace("%amount%", StringUtils.formatDoubleString(var1)).replace("%amountremaining%", StringUtils.formatDoubleString(var1 - this.gang.getBankMoney())));
            } else if (!Settings.enableModuleBank && !this.main.getEconomy().has(this.player, var1)) {
                this.msg(Lang.MSG_GANG_LEVELUP_CANTAFFORD.toMsg().replace("%amount%", StringUtils.formatDoubleString(var1)).replace("%amountremaining%", StringUtils.formatDoubleString(var1 - this.main.getEconomy().getBalance(this.player))));
            } else {
                GangLevelUpEvent var3 = new GangLevelUpEvent(this.gang, this.gang.getLevel(), this.gang.getLevel() + 1);
                Bukkit.getServer().getPluginManager().callEvent(var3);
                if (!var3.isCancelled()) {
                    if (Settings.enableModuleBank) {
                        this.gang.withdraw(var1);
                    } else {
                        this.main.getEconomy().withdrawPlayer(this.player, var1);
                    }

                    this.gang.setLevel(this.gang.getLevel() + 1);
                    this.gang.save();
                    if (Settings.getLevelupRewards(this.gang.getLevel() - 1).size() > 0) {
                        ArrayList var4 = new ArrayList();
                        if (Settings.levelUpRewardOnlinePlayersOnly) {
                            var4.addAll(this.gang.getOnlineMembers());
                        } else {
                            var4.addAll(this.gang.getAllMembers());
                        }

                        Iterator var5 = var4.iterator();

                        while(var5.hasNext()) {
                            OfflinePlayer var6 = (OfflinePlayer)var5.next();
                            Iterator var7 = Settings.getLevelupRewards(this.gang.getLevel() - 1).iterator();

                            while(var7.hasNext()) {
                                String var8 = (String)var7.next();
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), var8.replace("{PLAYER}", var6.getName()).replace("{GANG}", this.gang.getName()).replace("{LEVEL}", String.valueOf(this.gang.getLevel())));
                            }
                        }
                    }

                    if (Settings.getBroadcast("gangLevelUp")) {
                        Settings.broadcast(Lang.MSG_GANG_LEVELUP_LEVELEDUP_BROADCAST.toString().replace("%gang%", this.gang.getName()).replace("%player%", this.player.getName()).replace("%level%", this.str(this.gang.getLevel())));
                    }

                    this.gang.sendMessage(Lang.MSG_GANG_LEVELUP_LEVELEDUP_INGANG.toString().replace("%gang%", this.gang.getName()).replace("%player%", this.player.getName()).replace("%level%", this.str(this.gang.getLevel())));
                    this.msg(Lang.MSG_GANG_LEVELUP_LEVELEDUP_LEVELEDUP.toMsg().replace("%gang%", this.gang.getName()).replace("%player%", this.player.getName()).replace("%level%", this.str(this.gang.getLevel())));
                }

            }
        }
    }
}
