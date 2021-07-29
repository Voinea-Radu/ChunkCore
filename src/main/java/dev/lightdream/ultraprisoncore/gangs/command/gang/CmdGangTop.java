package dev.lightdream.ultraprisoncore.gangs.command.gang;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import dev.lightdream.ultraprisoncore.gangs.gang.GangSortOrder;

import java.util.Iterator;
import java.util.List;

public class CmdGangTop extends BSubCommand {
    public CmdGangTop() {
        this.aliases.add("top");
        this.aliases.add("t");
        this.permission = "gangsplus.gang.top";
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        GangSortOrder var1 = Settings.gangsTopDefaultOrder;
        if (this.args.length > 0 && GangSortOrder.fromString(this.args[0]) != null) {
            var1 = GangSortOrder.fromString(this.args[0]);
        }

        List var2 = this.main.getGangManager().getGangs(var1);
        byte var3 = 0;
        int var4 = Math.min(var2.size(), Settings.gangsTopLimit);
        StringBuilder var5 = new StringBuilder();
        var5.append(Lang.MSG_GANG_TOP_HEADER.toMsg().replace("%amount%", this.str(var4)).replace("%order%", var1.toString().toLowerCase()) + "\n");
        List var6 = var2.subList(var3, var4);
        String var7 = Lang.MSG_GANG_TOP_GANG.toMsg();
        Iterator var8 = var6.iterator();

        while(var8.hasNext()) {
            Gang var9 = (Gang)var8.next();
            var5.append(var9.formatPlaceholders(var7));
        }

        this.msg(var5.toString());
    }
}
