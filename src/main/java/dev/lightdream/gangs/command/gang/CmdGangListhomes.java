package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;

import java.util.Iterator;

public class CmdGangListhomes extends BSubCommand {
    public CmdGangListhomes() {
        this.aliases.add("listhomes");
        this.aliases.add("lhomes");
        this.aliases.add("homes");
        this.aliases.add("lh");
        this.permission = "gangsplus.gang.listhomes";
        this.requiredRank = Settings.getRequiredRank("listhomes");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleHomes) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else if (this.gang.getHomes().size() < 1) {
            this.msg(Lang.MSG_GANG_LISTHOMES_NOHOMES.toMsg().replace("%gang%", this.gang.getName()));
        } else {
            StringBuilder var1 = new StringBuilder(this.gang.formatPlaceholders(Lang.MSG_GANG_LISTHOMES_HEADER.toMsg().replace("%gang%", this.gang.getName()) + "\n"));
            Iterator var2 = this.gang.getHomes().keySet().iterator();

            while (var2.hasNext()) {
                String var3 = (String) var2.next();
                var1.append(var3 + ", ");
            }

            var1.append(",");

            this.msg(var1.toString().replace(", ,", ""));
        }
    }
}
