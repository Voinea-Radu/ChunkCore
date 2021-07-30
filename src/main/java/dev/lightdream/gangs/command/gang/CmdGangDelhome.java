package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;

public class CmdGangDelhome extends BSubCommand {
    public CmdGangDelhome() {
        this.aliases.add("delhome");
        this.aliases.add("dhome");
        this.aliases.add("dh");
        this.correctUsage = "/gang delhome <home>";
        this.permission = "gangsplus.gang.delhome";
        this.requiredRank = Settings.getRequiredRank("delhome");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.enableModuleHomes) {
            this.msg(Lang.MSG_MODULE_DISABLED.toMsg());
        } else {
            String var1 = this.args.length < 1 ? "home" : this.args[0];
            if (!this.gang.hasHome(var1)) {
                this.msg(Lang.MSG_GANG_DELHOME_INVALIDHOME.toMsg());
            } else {
                this.gang.removeHome(var1);
                this.gang.save();
                this.msg(Lang.MSG_GANG_DELHOME_DELETED.toMsg());
            }
        }
    }
}
