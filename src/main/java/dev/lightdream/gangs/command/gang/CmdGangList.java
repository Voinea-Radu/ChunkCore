package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.gang.Gang;

import java.util.List;

public class CmdGangList extends BSubCommand {
    public CmdGangList() {
        this.aliases.add("list");
        this.aliases.add("l");
        this.permission = "gangsplus.gang.list";
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        int var1 = 1;
        if (this.args.length > 0) {
            try {
                var1 = Integer.parseInt(this.args[0]);
            } catch (NumberFormatException var9) {
            }
        }

        if (var1 < 1) {
            var1 = 1;
        }

        if (this.main.getGangManager().getAllGangs().size() < 1) {
            this.msg(Lang.MSG_GANG_LIST_NOGANGS.toMsg());
        } else {
            int var2 = (int)Math.ceil((double)this.main.getGangManager().getAllGangs().size() / (double)Settings.gangsPerPage);
            var1 = var1 > var2 ? var2 : var1;
            int var3 = Math.max(0, (var1 - 1) * Settings.gangsPerPage);
            int var4 = Math.min(this.main.getGangManager().getAllGangs().size(), var1 * Settings.gangsPerPage);
            StringBuilder var5 = new StringBuilder();
            var5.append(Lang.MSG_GANG_LIST_HEADER.toMsg().replace("%page%", String.valueOf(var1)).replace("%totalpages%", String.valueOf(var2)) + "\n");
            List var6 = this.main.getGangManager().getAllGangs().subList(var3, var4);

            for(int var8 = 0; var8 < var6.size(); ++var8) {
                Gang var7 = (Gang)var6.get(var8);
                var5.append(var7.formatPlaceholders(Lang.MSG_GANG_LIST_GANG.toString())).append("\n");
            }

            this.msg(var5.toString());
        }
    }
}
