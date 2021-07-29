package dev.lightdream.ultraprisoncore.gangs.command.gangadmin;

import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.core.BSubCommand;
import dev.lightdream.ultraprisoncore.gangs.database.Callback;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArena;
import dev.lightdream.ultraprisoncore.gangs.fight.FightArenaState;
import org.bukkit.Location;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class CmdGangAdminArena extends BSubCommand {
    private Pattern regex = Pattern.compile("[^a-zA-Z0-9]");

    public CmdGangAdminArena() {
        this.aliases.add("arena");
        this.aliases.add("a");
        this.correctUsage = "/gadmin arena <list|create|delete|setlocation|setname|save> [arena] [location]";
        this.permission = "gangsplus.gangadmin.arena";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            if (!this.args[0].equalsIgnoreCase("list") && !this.args[0].equalsIgnoreCase("l")) {
                final String var5;
                final FightArena var6;
                if (!this.args[0].equalsIgnoreCase("create") && !this.args[0].equalsIgnoreCase("c")) {
                    if (!this.args[0].equalsIgnoreCase("delete") && !this.args[0].equalsIgnoreCase("del") && !this.args[0].equalsIgnoreCase("remove")) {
                        String var7;
                        FightArena var8;
                        if (!this.args[0].equalsIgnoreCase("setlocation") && !this.args[0].equalsIgnoreCase("sl")) {
                            if (!this.args[0].equalsIgnoreCase("setname") && !this.args[0].equalsIgnoreCase("sn")) {
                                if (this.args[0].equalsIgnoreCase("save") || this.args[0].equalsIgnoreCase("s")) {
                                    if (this.args.length < 2) {
                                        this.sendCorrectUsage();
                                        return;
                                    }

                                    var5 = this.args[1];
                                    if (!this.main.getFightManager().isArena(var5)) {
                                        this.msg(Lang.MSG_GANGADMIN_ARENA_INVALIDARENA.toMsg());
                                        return;
                                    }

                                    var6 = this.main.getFightManager().getArena(var5);
                                    boolean var9 = true;
                                    String var10 = "";
                                    if (var6.getCorner1() == null) {
                                        var9 = false;
                                        var10 = var10 + "corner1, ";
                                    }

                                    if (var6.getCorner2() == null) {
                                        var9 = false;
                                        var10 = var10 + "corner2, ";
                                    }

                                    if (var6.getSpawn1() == null) {
                                        var9 = false;
                                        var10 = var10 + "spawn1, ";
                                    }

                                    if (var6.getSpawn2() == null) {
                                        var9 = false;
                                        var10 = var10 + "spawn2, ";
                                    }

                                    if (var10.endsWith(", ")) {
                                        var10 = var10.substring(0, var10.length() - 2);
                                    }

                                    if (var9) {
                                        if (var6.getState() == FightArenaState.NOT_CONFIGURED) {
                                            var6.setState(FightArenaState.EMPTY);
                                        }

                                        this.main.getDataManager().updateArena(var6);
                                        this.msg(Lang.MSG_GANGADMIN_ARENA_SAVE_SAVED.toMsg().replace("%id%", var6.getId()));
                                    } else {
                                        this.msg(Lang.MSG_GANGADMIN_ARENA_SAVE_CANTSAVE.toMsg().replace("%id%", var6.getId()).replace("%locations%", var10));
                                    }
                                }
                            } else {
                                if (this.args.length < 3) {
                                    this.sendCorrectUsage();
                                    return;
                                }

                                var5 = this.args[1];
                                var7 = this.args[2];
                                if (!this.main.getFightManager().isArena(var5)) {
                                    this.msg(Lang.MSG_GANGADMIN_ARENA_INVALIDARENA.toMsg());
                                    return;
                                }

                                if (this.regex.matcher(var7).find()) {
                                    this.msg(Lang.MSG_GANGADMIN_ARENA_SETNAME_SPECIALCHARS.toMsg().replace("%id%", var5));
                                    return;
                                }

                                var8 = this.main.getFightManager().getArena(var5);
                                if (var8.getState() != FightArenaState.NOT_CONFIGURED && var8.getState() != FightArenaState.EMPTY) {
                                    this.msg(Lang.MSG_GANGADMIN_ARENA_INUSE.toMsg().replace("%id%", var8.getId()));
                                    return;
                                }

                                var8.setName(var7);
                                this.msg(Lang.MSG_GANGADMIN_ARENA_SETNAME_SET.toMsg().replace("%id%", var5).replace("%name%", var7));
                            }
                        } else {
                            if (this.args.length < 3) {
                                this.sendCorrectUsage();
                                return;
                            }

                            var5 = this.args[1];
                            var7 = this.args[2];
                            if (!this.main.getFightManager().isArena(var5)) {
                                this.msg(Lang.MSG_GANGADMIN_ARENA_INVALIDARENA.toMsg());
                                return;
                            }

                            var8 = this.main.getFightManager().getArena(var5);
                            if (var8.getState() != FightArenaState.NOT_CONFIGURED && var8.getState() != FightArenaState.EMPTY) {
                                this.msg(Lang.MSG_GANGADMIN_ARENA_INUSE.toMsg().replace("%id%", var8.getId()));
                                return;
                            }

                            if (var8.getLocationsWorld() != null && !this.player.getWorld().equals(var8.getLocationsWorld())) {
                                this.msg(Lang.MSG_GANGADMIN_ARENA_SETLOCATION_INVALIDWORLD.toMsg().replace("%world%", var8.getLocationsWorld().getName()));
                                return;
                            }

                            if (var7.equalsIgnoreCase("corner1")) {
                                var8.setCorner1(this.player.getLocation());
                            } else if (var7.equalsIgnoreCase("corner2")) {
                                var8.setCorner2(this.player.getLocation());
                            } else if (var7.equalsIgnoreCase("spawn1")) {
                                var8.setSpawn1(this.player.getLocation());
                            } else {
                                if (!var7.equalsIgnoreCase("spawn2")) {
                                    this.msg(Lang.MSG_GANGADMIN_ARENA_SETLOCATION_INVALIDLOCATION.toMsg());
                                    return;
                                }

                                var8.setSpawn2(this.player.getLocation());
                            }

                            this.msg(Lang.MSG_GANGADMIN_ARENA_SETLOCATION_SET.toMsg().replace("%id%", var5).replace("%location%", var7));
                            if (var8.getState() == FightArenaState.NOT_CONFIGURED && var8.getCorner1() != null && var8.getCorner2() != null && var8.getSpawn1() != null && var8.getSpawn2() != null) {
                                this.msg(Lang.MSG_GANGADMIN_ARENA_SETLOCATION_READYTOUSE.toMsg().replace("%id%", var5));
                            }
                        }
                    } else {
                        if (this.args.length < 2) {
                            this.sendCorrectUsage();
                            return;
                        }

                        var5 = this.args[1];
                        if (!this.main.getFightManager().isArena(var5)) {
                            this.msg(Lang.MSG_GANGADMIN_ARENA_INVALIDARENA.toMsg());
                            return;
                        }

                        var6 = this.main.getFightManager().getArena(var5);
                        if (var6.getState() != FightArenaState.NOT_CONFIGURED && var6.getState() != FightArenaState.EMPTY) {
                            this.msg(Lang.MSG_GANGADMIN_ARENA_INUSE.toMsg().replace("%id%", var6.getId()));
                            return;
                        }

                        this.main.getFightManager().removeArena(var6);
                        this.msg(Lang.MSG_GANGADMIN_ARENA_REMOVE_REMOVED.toMsg().replace("%id%", var5));
                    }
                } else {
                    if (this.args.length < 2) {
                        this.sendCorrectUsage();
                        return;
                    }

                    var5 = this.args[1];
                    if (this.regex.matcher(var5).find()) {
                        this.msg(Lang.MSG_GANGADMIN_ARENA_CREATE_SPECIALCHARS.toMsg().replace("%id%", var5));
                        return;
                    }

                    if (this.main.getFightManager().isArena(var5)) {
                        this.msg(Lang.MSG_GANGADMIN_ARENA_CREATE_ALREADYEXISTS.toMsg().replace("%id%", var5));
                        return;
                    }

                    var6 = new FightArena(-1, var5, var5, (Location)null, (Location)null, (Location)null, (Location)null, FightArenaState.NOT_CONFIGURED);
                    this.main.getDataManager().createArena(var6, new Callback<Integer>() {
                        public void onSuccess(Integer var1) {
                            var6.setDatabaseId(var1);
                            CmdGangAdminArena.this.main.getFightManager().getAllArenas().add(var6);
                            CmdGangAdminArena.this.msg(Lang.MSG_GANGADMIN_ARENA_CREATE_CREATED.toMsg().replace("%id%", var5));
                        }

                        public void onFailure(Integer var1) {
                            CmdGangAdminArena.this.msg(Lang.MSG_ERROR.toMsg());
                        }
                    });
                }
            } else {
                List var1 = this.main.getFightManager().getAllArenas();
                if (var1.size() == 0) {
                    this.msg(Lang.MSG_GANGADMIN_ARENA_LIST_NOARENAS.toMsg());
                    return;
                }

                StringBuilder var2 = new StringBuilder();
                var2.append(Lang.MSG_GANGADMIN_ARENA_LIST_HEADER.toMsg() + "\n");
                Iterator var3 = var1.iterator();

                while(var3.hasNext()) {
                    FightArena var4 = (FightArena)var3.next();
                    var2.append(Lang.MSG_GANGADMIN_ARENA_LIST_ARENA.toString().replace("%id%", var4.getId()).replace("%name%", var4.getName()).replace("%state%", var4.getState().getTranslation().toString()) + "\n");
                }

                this.msg(var2.toString());
            }

        }
    }
}
