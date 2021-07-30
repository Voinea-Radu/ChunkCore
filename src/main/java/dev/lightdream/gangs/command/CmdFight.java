package dev.lightdream.gangs.command;

import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.command.fight.*;
import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BCommand;
import dev.lightdream.gangs.core.BSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class CmdFight extends BCommand implements CommandExecutor {
    public CmdFight(GangsPlugin var1) {
        super(var1);
        this.subcommands.add(new CmdFightAccept());
        this.subcommands.add(new CmdFightChallenge());
        this.subcommands.add(new CmdFightDecline());
        this.subcommands.add(new CmdFightJoin());
        this.subcommands.add(new CmdFightLeave());
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (!Settings.enableModuleFights) {
            var1.sendMessage(Lang.MSG_MODULE_DISABLED.toMsg());
            return true;
        } else if (var1 instanceof Player && Settings.disableGangsInWorlds.contains(((Player)var1).getWorld().getName())) {
            var1.sendMessage(Lang.MSG_GANGS_DISABLED.toMsg());
            return true;
        } else if (var4.length < 1) {
            var1.sendMessage(Lang.MSG_USAGE_FIGHT.toMsg());
            return true;
        } else {
            Iterator var5 = this.subcommands.iterator();

            BSubCommand var6;
            do {
                if (!var5.hasNext()) {
                    var1.sendMessage(Lang.MSG_USAGE_FIGHT.toMsg());
                    return true;
                }

                var6 = (BSubCommand)var5.next();
            } while(!var6.aliases.contains(var4[0]));

            var6.execute(var1, var4);
            return true;
        }
    }
}
