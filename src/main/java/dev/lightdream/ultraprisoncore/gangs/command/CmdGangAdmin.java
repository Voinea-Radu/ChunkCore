package dev.lightdream.ultraprisoncore.gangs.command;

import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import dev.lightdream.ultraprisoncore.gangs.command.gangadmin.*;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BCommand;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CmdGangAdmin extends BCommand implements CommandExecutor {
    public CmdGangAdmin(GangsPlugin var1) {
        super(var1);
        this.subcommands.add(new CmdGangAdminArena());
        this.subcommands.add(new CmdGangAdminBank());
        this.subcommands.add(new CmdGangAdminDisband());
        this.subcommands.add(new CmdGangAdminReload());
        this.subcommands.add(new CmdGangAdminReset());
        this.subcommands.add(new CmdGangAdminResetGang());
        this.subcommands.add(new CmdGangAdminSpy());
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player && Settings.disableGangsInWorlds.contains(((Player)var1).getWorld().getName())) {
            var1.sendMessage(Lang.MSG_GANGS_DISABLED.toMsg());
            return true;
        } else if (var4.length < 1) {
            var1.sendMessage(Lang.MSG_USAGE_GANGADMIN.toMsg());
            return true;
        } else {
            Iterator var5 = this.subcommands.iterator();

            BSubCommand var6;
            do {
                if (!var5.hasNext()) {
                    var1.sendMessage(Lang.MSG_USAGE_GANGADMIN.toMsg());
                    return true;
                }

                var6 = (BSubCommand)var5.next();
            } while(!var6.aliases.contains(var4[0]));

            var6.execute(var1, var4);
            return true;
        }
    }
}
