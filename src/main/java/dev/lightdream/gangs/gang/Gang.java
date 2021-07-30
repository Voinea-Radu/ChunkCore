package dev.lightdream.gangs.gang;

import dev.lightdream.gangs.config.Settings;
import dev.lightdream.gangs.util.StringUtils;
import dev.lightdream.gangs.GangsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class Gang {
    private int id;
    private String name;
    private long createdAt;
    private boolean friendlyFire;
    private int level;
    private int kills;
    private int deaths;
    private int assists;
    private int fightsWon;
    private int fightsLost;
    private double bankMoney;
    private Map<UUID, GangMemberData> members;
    private Set<UUID> invitations;
    private Map<String, Location> homes;
    private List<Gang> allyGangs;
    private List<Gang> allyRequests;
    private boolean requiresUpdate;

    public Gang(int var1, String var2, long var3, boolean var5, int var6, int var7, int var8, int var9, int var10, int var11, double var12, Map<UUID, GangMemberData> var14, Set<UUID> var15, Map<String, Location> var16) {
        this.id = var1;
        this.name = var2;
        this.createdAt = var3;
        this.friendlyFire = var5;
        this.level = var6;
        this.kills = var7;
        this.deaths = var8;
        this.assists = var9;
        this.fightsWon = var10;
        this.fightsLost = var11;
        this.bankMoney = var12;
        this.members = var14;
        this.invitations = var15;
        this.homes = var16;
        this.allyGangs = new ArrayList();
        this.allyRequests = new ArrayList();
        this.requiresUpdate = false;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int var1) {
        this.id = var1;
    }

    public String getRawName() {
        return this.name;
    }

    public String getName() {
        return Settings.parseColorCodesInNames ? StringUtils.fixColors(this.name) : this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long var1) {
        this.createdAt = var1;
    }

    public boolean isFriendlyFire() {
        return this.friendlyFire;
    }

    public void setFriendlyFire(boolean var1) {
        this.friendlyFire = var1;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int var1) {
        this.level = var1;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int var1) {
        this.kills = var1;
    }

    public void registerKill() {
        ++this.kills;
        this.save();
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int var1) {
        this.deaths = var1;
    }

    public void registerDeath() {
        ++this.deaths;
        this.save();
    }

    public double getKdRatio() {
        return this.deaths != 0 ? (double) this.kills / (double) this.deaths : (double) this.kills;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int var1) {
        this.assists = var1;
    }

    public void registerAssist() {
        ++this.assists;
        this.save();
    }

    public int getFightsWon() {
        return this.fightsWon;
    }

    public void setFightsWon(int var1) {
        this.fightsWon = var1;
    }

    public void registerFightWon() {
        ++this.fightsWon;
        this.save();
    }

    public int getFightsLost() {
        return this.fightsLost;
    }

    public void setFightsLost(int var1) {
        this.fightsLost = var1;
    }

    public void registerFightLost() {
        ++this.fightsLost;
        this.save();
    }

    public double getWlRatio() {
        return this.fightsLost != 0 ? (double) this.fightsWon / (double) this.fightsLost : (double) this.fightsWon;
    }

    public double getBankMoney() {
        return this.bankMoney;
    }

    public void setBankMoney(double var1, boolean var3) {
        this.bankMoney = var1;
        if (var3) {
            this.save();
        }

    }

    public boolean has(double var1) {
        return this.bankMoney >= var1;
    }

    public void deposit(double var1) {
        this.bankMoney += var1;
        this.save();
    }

    public void withdraw(double var1) {
        this.bankMoney -= var1;
        this.save();
    }

    public Set<UUID> getInvitations() {
        return this.invitations;
    }

    public void setInvitations(Set<UUID> var1) {
        this.invitations = var1;
    }

    public boolean isInvited(OfflinePlayer var1) {
        return this.invitations.contains(var1.getUniqueId());
    }

    public void addInvitation(OfflinePlayer var1) {
        this.invitations.add(var1.getUniqueId());
    }

    public void removeInvitation(OfflinePlayer var1) {
        this.invitations.remove(var1.getUniqueId());
    }

    public Map<String, Location> getHomes() {
        return this.homes;
    }

    public void setHomes(Map<String, Location> var1) {
        this.homes = var1;
    }

    public void addHome(String var1, Location var2) {
        this.homes.put(var1, var2);
    }

    public boolean hasHome(String var1) {
        return this.homes.containsKey(var1);
    }

    public void removeHome(String var1) {
        this.homes.remove(var1);
    }

    public Location getHome(String var1) {
        return this.homes.get(var1);
    }

    public Map<UUID, GangMemberData> getMembers() {
        return this.members;
    }

    public void setMembers(Map<UUID, GangMemberData> var1) {
        this.members = var1;
    }

    public boolean isMember(OfflinePlayer var1) {
        return this.members.containsKey(var1.getUniqueId());
    }

    public GangMemberData getMemberData(OfflinePlayer var1) {
        return this.members.get(var1.getUniqueId());
    }

    public void addMember(Player var1, int var2) {
        this.members.put(var1.getUniqueId(), new GangMemberData(var1.getUniqueId(), var1.getName(), var2));
    }

    public void removeMember(OfflinePlayer var1) {
        this.members.remove(var1.getUniqueId());
    }

    public String getMembersList() {
        String var1 = "";
        int var2 = 0;
        Iterator var3 = this.members.keySet().iterator();

        while (var3.hasNext()) {
            UUID var4 = (UUID) var3.next();
            if (Bukkit.getOfflinePlayer(var4) != null && Bukkit.getOfflinePlayer(var4).hasPlayedBefore()) {
                if (var2 < this.members.size() - 1) {
                    var1 = var1 + Bukkit.getOfflinePlayer(var4).getName() + ", ";
                } else {
                    var1 = var1 + Bukkit.getOfflinePlayer(var4).getName();
                }

                ++var2;
            }
        }

        return var1;
    }

    public String getMembersListWithOnlineHighlighted() {
        String var1 = "";
        int var2 = 0;

        for (Iterator var3 = this.members.keySet().iterator(); var3.hasNext(); ++var2) {
            UUID var4 = (UUID) var3.next();
            if (Bukkit.getPlayer(var4) != null) {
                if (var2 < this.members.size() - 1) {
                    var1 = var1 + Settings.onlinePlayerNameFormat.replace("%player%", Bukkit.getOfflinePlayer(var4).getName()) + ", ";
                } else {
                    var1 = var1 + Settings.onlinePlayerNameFormat.replace("%player%", Bukkit.getOfflinePlayer(var4).getName());
                }
            } else if (Bukkit.getOfflinePlayer(var4) != null && Bukkit.getOfflinePlayer(var4).hasPlayedBefore()) {
                if (var2 < this.members.size() - 1) {
                    var1 = var1 + Bukkit.getOfflinePlayer(var4).getName() + ", ";
                } else {
                    var1 = var1 + Bukkit.getOfflinePlayer(var4).getName();
                }
            }
        }

        return var1;
    }

    public Set<OfflinePlayer> getAllMembers() {
        HashSet var1 = new HashSet();
        Iterator var2 = this.members.keySet().iterator();

        while (var2.hasNext()) {
            UUID var3 = (UUID) var2.next();
            if (Bukkit.getOfflinePlayer(var3) != null) {
                var1.add(Bukkit.getOfflinePlayer(var3));
            }
        }

        return var1;
    }

    public Set<Player> getOnlineMembers() {
        HashSet var1 = new HashSet();
        Iterator var2 = this.members.keySet().iterator();

        while (var2.hasNext()) {
            UUID var3 = (UUID) var2.next();
            if (Bukkit.getPlayer(var3) != null) {
                var1.add(Bukkit.getPlayer(var3));
            }
        }

        return var1;
    }

    public String getOnlineMembersList() {
        String var1 = "";
        int var2 = 0;

        for (Iterator var3 = this.getOnlineMembers().iterator(); var3.hasNext(); ++var2) {
            Player var4 = (Player) var3.next();
            if (var2 < this.getOnlineMembers().size() - 1) {
                var1 = var1 + var4.getName() + ", ";
            } else {
                var1 = var1 + var4.getName();
            }
        }

        return var1;
    }

    public GangMemberData getOwnerMemberData() {
        GangMemberData var1 = null;
        Iterator var2 = this.members.values().iterator();

        while (true) {
            GangMemberData var3;
            do {
                if (!var2.hasNext()) {
                    return var1;
                }

                var3 = (GangMemberData) var2.next();
            } while (var1 != null && var3.getRank() <= var1.getRank());

            var1 = var3;
        }
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(this.getOwnerMemberData().getId()) != null && Bukkit.getOfflinePlayer(this.getOwnerMemberData().getId()).hasPlayedBefore() ? Bukkit.getOfflinePlayer(this.getOwnerMemberData().getId()) : null;
    }

    public String getOwnerName() {
        return this.getOwner() != null ? this.getOwner().getName() : "";
    }

    public List<Gang> getAllyGangs() {
        return this.allyGangs;
    }

    public void setAllyGangs(List<Gang> var1) {
        this.allyGangs = var1;
    }

    public String getAllyGangsList() {
        String var1 = "";
        int var2 = 0;

        for (Iterator var3 = this.allyGangs.iterator(); var3.hasNext(); ++var2) {
            Gang var4 = (Gang) var3.next();
            if (var2 < this.allyGangs.size() - 1) {
                var1 = var1 + var4.getName() + ", ";
            } else {
                var1 = var1 + var4.getName();
            }
        }

        return var1;
    }

    public boolean isAlly(Gang var1) {
        return this.allyGangs.contains(var1);
    }

    public List<Gang> getAllyRequests() {
        return this.allyRequests;
    }

    public void setAllyRequests(List<Gang> var1) {
        this.allyRequests = var1;
    }

    public boolean isRequiresUpdate() {
        return this.requiresUpdate;
    }

    public void setRequiresUpdate(boolean var1) {
        this.requiresUpdate = var1;
    }

    public String getFormattedName() {
        return Settings.gangNameFormatUseLevelBased && Settings.gangNameFormatLevelBased.containsKey(this.level) ? StringUtils.fixColors(Settings.gangNameFormatLevelBased.get(this.level).replace("%gang%", this.getName())) : StringUtils.fixColors(Settings.gangNameFormatDefault.replace("%gang%", this.getName()));
    }

    public void sendMessage(String var1) {
        Iterator var2 = this.getOnlineMembers().iterator();

        while (var2.hasNext()) {
            Player var3 = (Player) var2.next();
            var3.sendMessage(StringUtils.fixColors(Settings.messagesInGang.replace("%gang%", this.name).replace("%message%", var1)));
        }

    }

    public void sendChatMessage(Player var1, String var2) {
        String var3 = StringUtils.fixColors(Settings.messagesGangChatFormat.replace("%gang%", this.name).replace("%rank%", Settings.getRankName(this.getMemberData(var1).getRank())).replace("%player%", var1.getName()).replace("%message%", var2));
        GangsPlugin.getInstance().getPlayerManager().sendMessageToSpying(var3);
        Iterator var4 = this.getOnlineMembers().iterator();

        while (var4.hasNext()) {
            Player var5 = (Player) var4.next();
            var5.sendMessage(var3);
        }

        if (Settings.gangChatLogEnable) {
            GangsPlugin.getInstance().logRaw(Settings.gangChatLogFormat.replace("%gang%", this.name).replace("%rank%", Settings.getRankName(this.getMemberData(var1).getRank())).replace("%player%", var1.getName()).replace("%message%", var2));
        }

    }

    public void sendAllyChatMessage(Player var1, String var2) {
        String var3 = StringUtils.fixColors(Settings.messagesAllyChatFormat.replace("%gang%", this.name).replace("%rank%", Settings.getRankName(this.getMemberData(var1).getRank())).replace("%player%", var1.getName()).replace("%message%", var2));
        GangsPlugin.getInstance().getPlayerManager().sendMessageToSpying(var3);
        Iterator var4 = this.getOnlineMembers().iterator();

        while (var4.hasNext()) {
            Player var5 = (Player) var4.next();
            var5.sendMessage(var3);
        }

        var4 = this.allyGangs.iterator();

        while (var4.hasNext()) {
            Gang var8 = (Gang) var4.next();
            Iterator var6 = var8.getOnlineMembers().iterator();

            while (var6.hasNext()) {
                Player var7 = (Player) var6.next();
                var7.sendMessage(var3);
            }
        }

        if (Settings.allyChatLogEnable) {
            GangsPlugin.getInstance().logRaw(Settings.allyChatLogFormat.replace("%gang%", this.name).replace("%rank%", Settings.getRankName(this.getMemberData(var1).getRank())).replace("%player%", var1.getName()).replace("%message%", var2));
        }

    }

    public String formatPlaceholders(String var1) {
        return var1.replace("%id%", String.valueOf(this.id)).replace("%name%", this.getName()).replace("%date%", Settings.formatDate(this.createdAt)).replace("%friendlyfire%", String.valueOf(this.friendlyFire)).replace("%level%", String.valueOf(this.level)).replace("%kills%", String.valueOf(this.kills)).replace("%deaths%", String.valueOf(this.deaths)).replace("%assists%", String.valueOf(this.assists)).replace("%kdr%", StringUtils.formatDoubleString(this.getKdRatio())).replace("%fightswon%", String.valueOf(this.fightsWon)).replace("%fightslost%", String.valueOf(this.fightsLost)).replace("%wlr%", StringUtils.formatDoubleString(this.getWlRatio())).replace("%bank%", StringUtils.formatDoubleString(this.bankMoney)).replace("%leader%", this.getOwnerName()).replace("%members%", this.getMembersList()).replace("%membershighlighted%", this.getMembersListWithOnlineHighlighted()).replace("%membersamount%", String.valueOf(this.getMembers().size())).replace("%onlinemembers%", this.getOnlineMembersList()).replace("%onlinemembersamount%", String.valueOf(this.getOnlineMembers().size())).replace("%allies%", this.getAllyGangsList());
    }

    public void save() {
        if (Settings.saveDataPeriodically) {
            this.requiresUpdate = true;
        } else {
            GangsPlugin.getInstance().getDataManager().updateGang(this);
        }

    }

    public double getMembersBalanceSum() {
        double var1 = 0.0D;

        OfflinePlayer var4;
        for (Iterator var3 = this.getAllMembers().iterator(); var3.hasNext(); var1 += GangsPlugin.getInstance().getEconomy().getBalance(var4)) {
            var4 = (OfflinePlayer) var3.next();
        }

        return var1;
    }
}
