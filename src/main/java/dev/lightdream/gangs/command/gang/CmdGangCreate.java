package dev.lightdream.gangs.command.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.core.BSubCommand;
import dev.lightdream.gangs.util.StringUtils;

import java.util.Iterator;

public class CmdGangCreate extends BSubCommand {
    public CmdGangCreate() {
        this.aliases.add("create");
        this.aliases.add("c");
        this.correctUsage = "/gang create <name>";
        this.permission = "gangsplus.gang.create";
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = true;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.buildStringFromArgs(0, this.args.length - 1);
            if (!this.main.getEconomy().has(this.player, Settings.priceCreate)) {
                this.msg(Lang.MSG_GANG_CREATE_CANTAFFORD.toMsg().replace("%amount%", StringUtils.formatDoubleString(Settings.priceCreate)));
            } else if (var1.length() >= Settings.minGangNameLength && var1.length() <= Settings.maxGangNameLength) {
                if (!Settings.allowSpaces && var1.contains(" ")) {
                    this.msg(Lang.MSG_GANG_CREATE_SPACES.toMsg());
                } else if (Settings.gangNamePattern.matcher(var1).find()) {
                    this.msg(Lang.MSG_GANG_CREATE_SPECIALCHARS.toMsg());
                } else if (Settings.allowedNameCharacters.contains("&") && Settings.parseColorCodesInNames && !StringUtils.fixColors(var1).equals(var1) && !this.player.hasPermission("gangsplus.gang.create.colors")) {
                    this.msg(Lang.MSG_GANG_CREATE_COLORS.toMsg());
                } else {
                    Iterator var2 = Settings.bannedNames.iterator();

                    String var3;
                    do {
                        if (!var2.hasNext()) {
                            if (this.main.getGangManager().isGang(var1)) {
                                this.msg(Lang.MSG_GANG_CREATE_ALREADYEXISTS.toMsg());
                                return;
                            }

                            this.main.getEconomy().withdrawPlayer(this.player, Settings.priceCreate);
                            this.main.getGangManager().createGang(var1, this.player);
                            return;
                        }

                        var3 = (String)var2.next();
                    } while(!var1.toLowerCase().contains(var3.toLowerCase()));

                    this.msg(Lang.MSG_GANG_CREATE_BANNEDNAME.toMsg());
                }
            } else {
                this.msg(Lang.MSG_GANG_CREATE_LENGTH.toMsg().replace("%min%", this.str(Settings.minGangNameLength)).replace("%max%", this.str(Settings.maxGangNameLength)));
            }
        }
    }
}

