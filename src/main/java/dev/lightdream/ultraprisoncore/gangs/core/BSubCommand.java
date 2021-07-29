package dev.lightdream.ultraprisoncore.gangs.core;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import dev.lightdream.ultraprisoncore.gangs.GangsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BSubCommand {
    public GangsPlugin main = GangsPlugin.getInstance();
    public List<String> aliases = new ArrayList();
    public CommandSender sender;
    public String[] args;
    public Player player;
    public Gang gang;
    public String correctUsage = "";
    public String permission = "";
    public int requiredRank = 0;
    public boolean senderMustBePlayer;
    public boolean senderMustBeInGang;
    public boolean senderMustBeWithoutGang;

    public BSubCommand() {
    }

    public void execute(CommandSender var1, String[] var2) {
        if (var1 instanceof Player) {
            this.player = (Player) var1;
            this.gang = this.main.getGangManager().getPlayersGang(this.player);
        } else {
            this.player = null;
            this.gang = null;
        }

        this.sender = var1;
        LinkedList var3 = new LinkedList(Arrays.asList(var2));
        var3.remove(0);
        this.args = (String[]) var3.toArray(new String[var3.size()]);
        if (!var1.hasPermission(this.permission)) {
            this.msg(Lang.MSG_NOACCESS.toMsg());
        } else if (this.senderMustBePlayer && !this.isPlayer()) {
            this.msg(Lang.MSG_PLAYERONLY.toMsg());
        } else if (this.senderMustBeInGang && (!this.isPlayer() || !this.isInGang())) {
            this.msg(Lang.MSG_INGANGONLY.toMsg());
        } else if (this.senderMustBeWithoutGang && this.isPlayer() && this.isInGang()) {
            this.msg(Lang.MSG_NOGANGONLY.toMsg());
        } else if (this.requiredRank >= 0 && this.isInGang() && this.gang.getMemberData(this.player).getRank() < this.requiredRank) {
            this.msg(Lang.MSG_TOOLOWRANK.toMsg().replace("%rank%", Settings.getRankName(this.requiredRank)));
        } else {
            this.execute();
        }
    }

    public abstract void execute();

    public void msg(String var1) {
        this.sender.sendMessage(var1);
    }

    public void sendCorrectUsage() {
        this.msg(Lang.MSG_USAGE_SUBCOMMAND.toMsg().replace("%usage%", this.correctUsage));
    }

    public boolean isPlayer() {
        return this.player != null;
    }

    public boolean isInGang() {
        return this.gang != null;
    }

    public String buildStringFromArgs(int var1, int var2) {
        if (var2 < 0) {
            var2 = 0;
        }

        String var3;
        if (this.args.length > var1 + 1) {
            StringBuilder var4 = new StringBuilder(this.args[var1]);

            for (int var5 = var1 + 1; var5 < var2 + 1; ++var5) {
                var4.append(" " + this.args[var5]);
            }

            var3 = var4.toString();
        } else {
            var3 = this.args[var1];
        }

        return var3;
    }

    public String str(Object var1) {
        return String.valueOf(var1);
    }

    public String getSenderName() {
        return this.isPlayer() ? this.player.getName() : Lang.LANG_CONSOLE.toString();
    }
}
