package dev.lightdream.gangs.gang;

import dev.lightdream.gangs.config.Lang;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.database.Callback;
import dev.lightdream.gangs.event.GangCreateEvent;
import dev.lightdream.gangs.event.PlayerJoinGangEvent;
import dev.lightdream.gangs.util.StringUtils;
import dev.lightdream.gangs.GangsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class GangManager {
    private final GangsPlugin main;
    private List<Gang> gangs = new ArrayList();

    public GangManager(GangsPlugin var1) {
        this.main = var1;
    }

    public static Comparator<Gang> getComparator(GangSortOrder var0) {
        if (var0 == GangSortOrder.LIST) {
            return (var0x, var1) -> {
                if ((var0x.getOnlineMembers().size() <= 0 || var1.getOnlineMembers().size() <= 0) && (var0x.getOnlineMembers().size() != 0 || var1.getOnlineMembers().size() != 0)) {
                    if (var0x.getOnlineMembers().size() > 0 && var1.getOnlineMembers().size() == 0) {
                        return -1;
                    } else {
                        return var0x.getOnlineMembers().size() == 0 && var1.getOnlineMembers().size() > 0 ? 1 : 0;
                    }
                } else {
                    return var0x.getRawName().compareTo(var1.getRawName());
                }
            };
        } else if (var0 == GangSortOrder.KILLS) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getKills(), var0x.getKills());
            };
        } else if (var0 == GangSortOrder.DEATHS) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getDeaths(), var0x.getDeaths());
            };
        } else if (var0 == GangSortOrder.KDR) {
            return (var0x, var1) -> {
                return Double.compare(var1.getKdRatio(), var0x.getKdRatio());
            };
        } else if (var0 == GangSortOrder.WON) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getFightsWon(), var0x.getFightsWon());
            };
        } else if (var0 == GangSortOrder.LOST) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getFightsLost(), var0x.getFightsLost());
            };
        } else if (var0 == GangSortOrder.WLR) {
            return (var0x, var1) -> {
                return Double.compare(var1.getWlRatio(), var0x.getWlRatio());
            };
        } else if (var0 == GangSortOrder.LEVEL) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getLevel(), var0x.getLevel());
            };
        } else if (var0 == GangSortOrder.MEMBERS) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getAllMembers().size(), var0x.getAllMembers().size());
            };
        } else if (var0 == GangSortOrder.MEMBERS_ONLINE) {
            return (var0x, var1) -> {
                return Integer.compare(var1.getOnlineMembers().size(), var0x.getOnlineMembers().size());
            };
        } else if (var0 == GangSortOrder.BANK) {
            return (var0x, var1) -> {
                return Double.compare(var1.getBankMoney(), var0x.getBankMoney());
            };
        } else if (var0 == GangSortOrder.MONEY) {
            return (var0x, var1) -> {
                return Double.compare(var1.getMembersBalanceSum(), var0x.getMembersBalanceSum());
            };
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGangs(List<Gang> var1) {
        this.gangs = var1;
    }

    public List<Gang> getAllGangs() {
        return this.gangs;
    }

    public List<Gang> getGangs(GangSortOrder var1) {
        ArrayList var2 = new ArrayList(this.gangs);

        Comparator var3;
        try {
            var3 = getComparator(var1);
        } catch (IllegalArgumentException var5) {
            return var2;
        }

        var2.sort(var3);
        return var2;
    }

    public void loadGangs() {
        this.main.getDataManager().loadGangs();
        if (Settings.enableModuleAlliances) {
            this.main.getDataManager().loadAlliances();
        }

    }

    public Gang getGang(int var1) {
        Iterator var2 = this.gangs.iterator();

        Gang var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (Gang) var2.next();
        } while (var3.getId() != var1);

        return var3;
    }

    public Gang getGang(String var1) {
        return this.getGang(var1, Settings.ignoreColorCodesInNames);
    }

    public Gang getGang(String var1, boolean var2) {
        Iterator var3 = this.gangs.iterator();

        Gang var4;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            var4 = (Gang) var3.next();
        } while ((!var2 || !StringUtils.stripColors(var4.getRawName()).equalsIgnoreCase(StringUtils.stripColors(var1))) && (var2 || !var4.getRawName().equalsIgnoreCase(var1)));

        return var4;
    }

    public boolean isGang(String var1) {
        return this.getGang(var1) != null;
    }

    public Gang getPlayersGang(OfflinePlayer var1) {
        Iterator var2 = this.gangs.iterator();

        Gang var3;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            var3 = (Gang) var2.next();
        } while (!var3.isMember(var1));

        return var3;
    }

    public boolean isInGang(OfflinePlayer var1) {
        if (var1 == null) {
            return false;
        } else {
            return this.getPlayersGang(var1) != null;
        }
    }

    public void createGang(String var1, final Player var2) {
        final Gang var3 = new Gang(0, var1, System.currentTimeMillis(), false, 1, 0, 0, 0, 0, 0, 0.0D, new HashMap(), new HashSet(), new HashMap());
        GangCreateEvent var4 = new GangCreateEvent(var3, var2);
        Bukkit.getServer().getPluginManager().callEvent(var4);
        if (!var4.isCancelled()) {
            final int var5 = Settings.getHighestRank();
            this.main.getDataManager().createGang(var3, new Callback<Integer>() {
                public void onSuccess(Integer var1) {
                    var3.setId(var1);
                    Bukkit.getScheduler().runTask(GangManager.this.main.getCore(), () -> {
                        PlayerJoinGangEvent var2x = new PlayerJoinGangEvent(var2, var3);
                        Bukkit.getServer().getPluginManager().callEvent(var2x);
                    });
                    var3.addMember(var2, var5);
                    GangManager.this.gangs.add(var3);
                    var3.save();
                    if (Settings.getBroadcast("gangCreate")) {
                        Settings.broadcast(Lang.MSG_GANG_CREATE_BROADCAST.toString().replace("%player%", var2.getName()).replace("%gang%", var3.getName()));
                    }

                    var2.sendMessage(Lang.MSG_GANG_CREATE_CREATED.toMsg().replace("%gang%", var3.getName()));
                }

                public void onFailure(Integer var1) {
                    var2.sendMessage(Lang.MSG_ERROR.toMsg());
                }
            });
        }

    }

    public void removeGang(Gang var1) {
        this.main.getDataManager().removeGang(var1);
        this.gangs.remove(var1);
    }
}
