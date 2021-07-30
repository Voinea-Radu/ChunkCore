package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.core.BSubCommand;

public class CmdGangHelp extends BSubCommand {
    public CmdGangHelp() {
        this.aliases.add("help");
        this.aliases.add("?");
        this.permission = "gangsplus.gang.help";
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        this.msg(Lang.MSG_GANG_HELP_HELP.toString());
    }
}
