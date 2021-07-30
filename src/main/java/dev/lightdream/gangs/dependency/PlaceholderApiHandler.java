package dev.lightdream.gangs.dependency;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.gang.Gang;
import dev.lightdream.gangs.util.StringUtils;
import org.bukkit.entity.Player;

public class PlaceholderApiHandler extends PlaceholderExpansion {
    private final GangsPlugin main;

    public PlaceholderApiHandler(GangsPlugin var1) {
        this.main = var1;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getIdentifier() {
        return "gangsplus";
    }

    public String getAuthor() {
        return this.main.getCore().getDescription().getAuthors().toString();
    }

    public String getVersion() {
        return this.main.getCore().getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player var1, String var2) {
        if (var1 == null) {
            return "";
        } else {
            var2 = var2.toLowerCase();
            if (var2.equals("in_gang")) {
                return this.getIsInGang(var1);
            } else if (!this.main.getGangManager().isInGang(var1)) {
                return "";
            } else {
                byte var4 = -1;
                switch (var2.hashCode()) {
                    case -2097558092:
                        if (var2.equals("gang_name_formatted")) {
                            var4 = 1;
                        }
                        break;
                    case -1738352627:
                        if (var2.equals("gang_kdr")) {
                            var4 = 16;
                        }
                        break;
                    case -1738340847:
                        if (var2.equals("gang_wlr")) {
                            var4 = 13;
                        }
                        break;
                    case -548582332:
                        if (var2.equals("gang_online_members_list")) {
                            var4 = 5;
                        }
                        break;
                    case -489915874:
                        if (var2.equals("gang_friendly_fire")) {
                            var4 = 4;
                        }
                        break;
                    case 165685801:
                        if (var2.equals("gang_online_members_count")) {
                            var4 = 6;
                        }
                        break;
                    case 169737392:
                        if (var2.equals("gang_members_list")) {
                            var4 = 7;
                        }
                        break;
                    case 185550249:
                        if (var2.equals("gang_kills")) {
                            var4 = 14;
                        }
                        break;
                    case 186363992:
                        if (var2.equals("gang_level")) {
                            var4 = 10;
                        }
                        break;
                    case 247326864:
                        if (var2.equals("gang_rank_number")) {
                            var4 = 3;
                        }
                        break;
                    case 958760765:
                        if (var2.equals("gang_members_count")) {
                            var4 = 8;
                        }
                        break;
                    case 1252672043:
                        if (var2.equals("gang_deaths")) {
                            var4 = 15;
                        }
                        break;
                    case 1481689781:
                        if (var2.equals("gang_leader")) {
                            var4 = 9;
                        }
                        break;
                    case 1491475645:
                        if (var2.equals("gang_losses")) {
                            var4 = 12;
                        }
                        break;
                    case 1945729847:
                        if (var2.equals("gang_name")) {
                            var4 = 0;
                        }
                        break;
                    case 1945849048:
                        if (var2.equals("gang_rank")) {
                            var4 = 2;
                        }
                        break;
                    case 1946005699:
                        if (var2.equals("gang_wins")) {
                            var4 = 11;
                        }
                }

                switch (var4) {
                    case 0:
                        return this.getGangName(var1);
                    case 1:
                        return this.getGangFormattedName(var1);
                    case 2:
                        return this.getGangRank(var1);
                    case 3:
                        return this.getGangRankNumber(var1);
                    case 4:
                        return this.getGangIsFriendlyFire(var1);
                    case 5:
                        return this.getGangMembersOnlineList(var1);
                    case 6:
                        return this.getGangMembersOnlineCount(var1);
                    case 7:
                        return this.getGangMembersList(var1);
                    case 8:
                        return this.getGangMembersCount(var1);
                    case 9:
                        return this.getGangLeader(var1);
                    case 10:
                        return this.getGangLevel(var1);
                    case 11:
                        return this.getGangWins(var1);
                    case 12:
                        return this.getGangLosses(var1);
                    case 13:
                        return this.getGangWlRatio(var1);
                    case 14:
                        return this.getGangKills(var1);
                    case 15:
                        return this.getGangDeaths(var1);
                    case 16:
                        return this.getGangKdRatio(var1);
                    default:
                        return null;
                }
            }
        }
    }

    private Gang getPlayersGang(Player var1) {
        return this.main.getGangManager().getPlayersGang(var1);
    }

    private String getIsInGang(Player var1) {
        return this.main.getGangManager().isInGang(var1) ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    private String getGangName(Player var1) {
        return this.getPlayersGang(var1).getName();
    }

    private String getGangFormattedName(Player var1) {
        return this.getPlayersGang(var1).getFormattedName();
    }

    private String getGangRank(Player var1) {
        return Settings.getRankName(this.getPlayersGang(var1).getMemberData(var1).getRank());
    }

    private String getGangRankNumber(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getMemberData(var1).getRank());
    }

    private String getGangIsFriendlyFire(Player var1) {
        return this.getPlayersGang(var1).isFriendlyFire() ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    private String getGangMembersOnlineList(Player var1) {
        return this.getPlayersGang(var1).getOnlineMembersList();
    }

    private String getGangMembersOnlineCount(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getOnlineMembers().size());
    }

    private String getGangMembersList(Player var1) {
        return this.getPlayersGang(var1).getMembersList();
    }

    private String getGangMembersCount(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getAllMembers().size());
    }

    private String getGangLeader(Player var1) {
        return this.getPlayersGang(var1).getOwnerName();
    }

    private String getGangLevel(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getLevel());
    }

    private String getGangWins(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getFightsWon());
    }

    private String getGangLosses(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getFightsLost());
    }

    private String getGangWlRatio(Player var1) {
        return StringUtils.formatDoubleString(this.getPlayersGang(var1).getWlRatio());
    }

    private String getGangKills(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getKills());
    }

    private String getGangDeaths(Player var1) {
        return String.valueOf(this.getPlayersGang(var1).getDeaths());
    }

    private String getGangKdRatio(Player var1) {
        return StringUtils.formatDoubleString(this.getPlayersGang(var1).getKdRatio());
    }
}
