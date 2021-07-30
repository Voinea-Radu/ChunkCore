package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.player.PlayerData;
import dev.lightdream.gangs.player.PlayerLastAction;

public class CmdGangDisband extends BSubCommand {
    public CmdGangDisband() {
        this.aliases.add("disband");
        this.aliases.add("ds");
        this.permission = "gangsplus.gang.disband";
        this.requiredRank = Settings.getRequiredRank("disband");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.main.getFightManager().isGangInArena(this.gang)) {
            this.msg(Lang.MSG_GANG_DISBAND_FIGHTING.toMsg());
        } else {
            PlayerData var1 = this.main.getPlayerManager().getPlayerData(this.player);
            var1.setLastAction(PlayerLastAction.GANG_DISBAND);
            this.msg(Lang.MSG_GANG_DISBAND_CONFIRM.toMsg());
        }
    }
}
