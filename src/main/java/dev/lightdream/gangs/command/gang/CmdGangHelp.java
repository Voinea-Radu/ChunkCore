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
        if (args.length == 2) {
            switch (args[1]) {
                case "1":
                    this.msg(Lang.MSG_GANG_HELP_HELP_1.toString());
                    break;
                case "2":
                    this.msg(Lang.MSG_GANG_HELP_HELP_2.toString());
                    break;
                case "3":
                    this.msg(Lang.MSG_GANG_HELP_HELP_3.toString());
                    break;
                case "4":
                    this.msg(Lang.MSG_GANG_HELP_HELP_4.toString());
                    break;
                case "5":
                    this.msg(Lang.MSG_GANG_HELP_HELP_5.toString());
                    break;
                default:
                    this.msg(Lang.MSG_GANG_HELP_HELP_1.toString());
                    break;
            }
        } else {
            this.msg(Lang.MSG_GANG_HELP_HELP_1.toString());
        }
    }
}
