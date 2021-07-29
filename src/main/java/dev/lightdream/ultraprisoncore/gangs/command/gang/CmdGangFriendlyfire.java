package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;

public class CmdGangFriendlyfire extends BSubCommand {
    public CmdGangFriendlyfire() {
        this.aliases.add("friendlyfire");
        this.aliases.add("ffire");
        this.aliases.add("ff");
        this.permission = "gangsplus.gang.friendlyfire";
        this.requiredRank = Settings.getRequiredRank("friendlyfire");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!Settings.friendlyFireTogglableByLeader) {
            this.msg(Lang.MSG_GANG_FRIENDLYFIRE_DISABLED.toMsg());
        } else {
            String var1 = "";
            if (this.gang.isFriendlyFire()) {
                var1 = Lang.LANG_DISABLED.toString();
                this.gang.setFriendlyFire(false);
            } else {
                var1 = Lang.LANG_ENABLED.toString();
                this.gang.setFriendlyFire(true);
            }

            this.gang.save();
            this.gang.sendMessage(Lang.MSG_GANG_FRIENDLYFIRE_TOGGLED_INGANG.toString().replace("%player%", this.player.getName()).replace("%state%", var1));
            this.msg(Lang.MSG_GANG_FRIENDLYFIRE_TOGGLED_TOGGLED.toMsg().replace("%state%", var1));
        }
    }
}
