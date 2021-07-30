package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.util.LocationUtils;
import org.bukkit.Location;

public class CmdGangSethome extends BSubCommand {
    public CmdGangSethome() {
        this.aliases.add("sethome");
        this.aliases.add("shome");
        this.aliases.add("sh");
        this.permission = "gangsplus.gang.sethome";
        this.requiredRank = Settings.getRequiredRank("sethome");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleHomes) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else {
            String var1 = this.args.length < 1 ? "home" : this.args[0];
            if (this.gang.hasHome(var1)) {
                this.msg(Lang.MSG_GANG_SETHOME_ALREADYEXISTS.toMsg());
            } else if (!var1.contains("'") && !var1.contains("\"")) {
                Location var2 = this.player.getLocation();
                if (Settings.safeCheckHomes && !LocationUtils.isSafe(var2)) {
                    this.msg(Lang.MSG_GANG_SETHOME_UNSAFELOCATION.toMsg());
                } else if (Settings.disableHomesInWorlds.contains(var2.getWorld().getName())) {
                    this.msg(Lang.MSG_GANG_SETHOME_BANNEDWORLD.toMsg());
                } else {
                    int var3 = Settings.maxHomesUseLevelBased && Settings.maxHomesLevelBased.containsKey(this.gang.getLevel()) ? (Integer)Settings.maxHomesLevelBased.get(this.gang.getLevel()) : Settings.maxHomesDefault;
                    if (this.gang.getHomes().size() >= var3) {
                        this.msg(Lang.MSG_GANG_SETHOME_LIMIT.toMsg().replace("%current%", this.str(this.gang.getHomes().size())).replace("%limit%", this.str(var3)));
                    } else {
                        this.gang.addHome(var1, var2);
                        this.gang.save();
                        this.msg(Lang.MSG_GANG_SETHOME_SET.toMsg().replace("%home%", var1));
                    }
                }
            } else {
                this.msg(Lang.MSG_GANG_SETHOME_INVALIDNAME.toMsg());
            }
        }
    }
}
