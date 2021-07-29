package dev.lightdream.ultraprisoncore.gangs.command;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.command.gang.*;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BCommand;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CmdGang extends BCommand implements CommandExecutor {
    public CmdGang(GangsPlugin var1) {
        super(var1);
        this.subcommands.add(new CmdGangAlly());
        this.subcommands.add(new CmdGangConfirm());
        this.subcommands.add(new CmdGangCreate());
        this.subcommands.add(new CmdGangDelhome());
        this.subcommands.add(new CmdGangDemote());
        this.subcommands.add(new CmdGangDeposit());
        this.subcommands.add(new CmdGangDisband());
        this.subcommands.add(new CmdGangFriendlyfire());
        this.subcommands.add(new CmdGangHelp());
        this.subcommands.add(new CmdGangHome());
        this.subcommands.add(new CmdGangInfo());
        this.subcommands.add(new CmdGangInvite());
        this.subcommands.add(new CmdGangJoin());
        this.subcommands.add(new CmdGangKick());
        this.subcommands.add(new CmdGangLeader());
        this.subcommands.add(new CmdGangLeave());
        this.subcommands.add(new CmdGangLevelup());
        this.subcommands.add(new CmdGangList());
        this.subcommands.add(new CmdGangListhomes());
        this.subcommands.add(new CmdGangNeutral());
        this.subcommands.add(new CmdGangPlayerinfo());
        this.subcommands.add(new CmdGangPromote());
        this.subcommands.add(new CmdGangRegroup());
        this.subcommands.add(new CmdGangSethome());
        this.subcommands.add(new CmdGangTop());
        this.subcommands.add(new CmdGangUninvite());
        this.subcommands.add(new CmdGangWithdraw());
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player && Settings.disableGangsInWorlds.contains(((Player)var1).getWorld().getName())) {
            var1.sendMessage(Lang.MSG_GANGS_DISABLED.toMsg());
            return true;
        } else if (var4.length < 1) {
            if (Settings.sendGangCmdUsage) {
                var1.sendMessage(Lang.MSG_GANG_HELP_HELP.toString());
            } else {
                var1.sendMessage(Lang.MSG_USAGE_GANG.toMsg());
            }

            return true;
        } else {
            Iterator var5 = this.subcommands.iterator();

            BSubCommand var6;
            do {
                if (!var5.hasNext()) {
                    if (Settings.sendGangCmdUsage) {
                        var1.sendMessage(Lang.MSG_GANG_HELP_HELP.toString());
                    } else {
                        var1.sendMessage(Lang.MSG_USAGE_GANG.toMsg());
                    }

                    return true;
                }

                var6 = (BSubCommand)var5.next();
            } while(!var6.aliases.contains(var4[0]));

            var6.execute(var1, var4);
            return true;
        }
    }
}
