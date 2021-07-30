package dev.lightdream.gangs.dependency;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import dev.lightdream.gangs.GangsPlugin;
import dev.lightdream.gangs.config.Settings;
import org.bukkit.OfflinePlayer;

public class MvdwPlaceholderHandler {
    private final GangsPlugin main;

    public MvdwPlaceholderHandler(GangsPlugin var1) {
        this.main = var1;
    }

    public void registerPlaceholders() {
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_in_gang", (var1) -> {
            return this.getIsInGang(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_name", (var1) -> {
            return this.getGangName(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_name_formatted", (var1) -> {
            return this.getGangFormattedName(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_rank", (var1) -> {
            return this.getGangRank(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_rank_number", (var1) -> {
            return this.getGangRankNumber(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_friendly_fire", (var1) -> {
            return this.getGangIsFriendlyFire(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_online_members_list", (var1) -> {
            return this.getGangMembersOnlineList(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_online_members_count", (var1) -> {
            return this.getGangMembersOnlineCount(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_members_list", (var1) -> {
            return this.getGangMembersList(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_members_count", (var1) -> {
            return this.getGangMembersCount(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_leader", (var1) -> {
            return this.getGangLeader(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_level", (var1) -> {
            return this.getGangLevel(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_wins", (var1) -> {
            return this.getGangWins(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_losses", (var1) -> {
            return this.getGangLosses(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_wlr", (var1) -> {
            return this.getGangWlRatio(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_kills", (var1) -> {
            return this.getGangKills(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_deaths", (var1) -> {
            return this.getGangDeaths(var1.getOfflinePlayer());
        });
        PlaceholderAPI.registerPlaceholder(this.main.getCore(), "gangsplus_2_gang_kdr", (var1) -> {
            return this.getGangKdRatio(var1.getOfflinePlayer());
        });
    }

    private String getIsInGang(OfflinePlayer var1) {
        return var1 != null ? String.valueOf(this.main.getGangManager().isInGang(var1)) : "";
    }

    private String getGangName(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? this.main.getGangManager().getPlayersGang(var1).getName() : "";
    }

    private String getGangFormattedName(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? this.main.getGangManager().getPlayersGang(var1).getFormattedName() : "";
    }

    private String getGangRank(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? Settings.getRankName(this.main.getGangManager().getPlayersGang(var1).getMemberData(var1).getRank()) : "";
    }

    private String getGangRankNumber(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getMemberData(var1).getRank()) : "";
    }

    private String getGangIsFriendlyFire(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).isFriendlyFire()) : "";
    }

    private String getGangMembersOnlineList(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? this.main.getGangManager().getPlayersGang(var1).getOnlineMembersList() : "";
    }

    private String getGangMembersOnlineCount(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getOnlineMembers().size()) : "";
    }

    private String getGangMembersList(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? this.main.getGangManager().getPlayersGang(var1).getMembersList() : "";
    }

    private String getGangMembersCount(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getMembers().size()) : "";
    }

    private String getGangLeader(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? this.main.getGangManager().getPlayersGang(var1).getOwnerName() : "";
    }

    private String getGangLevel(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getLevel()) : "";
    }

    private String getGangWins(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getFightsWon()) : "";
    }

    private String getGangLosses(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getFightsLost()) : "";
    }

    private String getGangWlRatio(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getWlRatio()) : "";
    }

    private String getGangKills(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getKills()) : "";
    }

    private String getGangDeaths(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getDeaths()) : "";
    }

    private String getGangKdRatio(OfflinePlayer var1) {
        return var1 != null && this.main.getGangManager().isInGang(var1) ? String.valueOf(this.main.getGangManager().getPlayersGang(var1).getKdRatio()) : "";
    }
}
