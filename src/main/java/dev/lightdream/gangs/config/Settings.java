package dev.lightdream.gangs.config;

import dev.lightdream.gangs.database.DataManager;
import dev.lightdream.gangs.gang.GangSortOrder;
import dev.lightdream.gangs.util.StringUtils;
import dev.lightdream.gangs.util.TitleUtils;
import dev.lightdream.gangs.core.BConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Settings {
    public static boolean enableModuleAlliances;
    public static boolean enableModuleBank;
    public static boolean enableModuleChat;
    public static boolean enableModuleFights;
    public static boolean enableModuleHomes;
    public static boolean allowSpaces;
    public static boolean friendlyFire;
    public static boolean friendlyFireTogglableByLeader;
    public static boolean showBankBalanceToOthers;
    public static boolean parseColorCodesInNames;
    public static boolean ignoreColorCodesInNames;
    public static boolean gangNameFormatUseLevelBased;
    public static boolean maxMembersUseLevelBased;
    public static boolean maxHomesUseLevelBased;
    public static boolean alliancesAllowPvp;
    public static boolean alliancesAllowFights;
    public static boolean alliancesEnableAllyChat;
    public static boolean fightsFreezeBeforeStart;
    public static boolean fightsEnableFightTimeLimit;
    public static boolean saveDataPeriodically;
    public static boolean gangChatLogEnable;
    public static boolean allyChatLogEnable;
    public static boolean safeCheckHomes;
    public static boolean levelUpRewardOnlinePlayersOnly;
    public static boolean sendGangCmdUsage;
    public static String databaseMySQLHost;
    public static String databaseMySQLDatabase;
    public static String databaseMySQLUser;
    public static String databaseMySQLPassword;
    public static String databaseTableNameAlliances;
    public static String databaseTableNameArenas;
    public static String databaseTableNameGangs;
    public static String databaseTableNamePlayers;
    public static String allowedNameCharacters;
    public static String onlinePlayerNameFormat;
    public static String gangNameFormatDefault;
    public static String gangNameFormatNoGang;
    public static String messagesInGang;
    public static String messagesInArena;
    public static String messagesGangChatFormat;
    public static String messagesAllyChatFormat;
    public static String gangChatLogFormat;
    public static String allyChatLogFormat;
    public static int databaseMySQLPort;
    public static int minGangNameLength;
    public static int maxGangNameLength;
    public static int maxGangLevel;
    public static int maxMembersDefault;
    public static int maxHomesDefault;
    public static int gangsPerPage;
    public static int gangsTopLimit;
    public static int assistDamageDelay;
    public static int fightsDelayBeforeStart;
    public static int fightsMinMembersAmount;
    public static int fightsMaxMembersAmount;
    public static int fightsFightTimeLimit;
    public static int fightsDelayBeforeTeleport;
    public static int dataSaveInterval;
    public static double priceCreate;
    public static double fightsMinMoneyAmount;
    public static double fightsMaxMoneyAmount;
    public static List<String> bannedNames = new ArrayList();
    public static List<String> disableGangsInWorlds = new ArrayList();
    public static List<String> disableHomesInWorlds = new ArrayList();
    public static List<String> disableFriendlyFireInWorlds = new ArrayList();
    public static List<String> fightsDisableCommandsDuringFight = new ArrayList();
    public static Map<Integer, String> gangNameFormatLevelBased = new HashMap();
    public static Map<Integer, Integer> maxMembersLevelBased = new HashMap();
    public static Map<Integer, Integer> maxHomesLevelBased = new HashMap();
    public static Map<Integer, List<String>> levelUpRewards = new HashMap();
    public static Map<String, List<String>> commands = new HashMap();
    public static Map<String, Integer> cooldowns = new HashMap();
    public static Map<Integer, Double> priceLevelup = new HashMap();
    public static Map<String, Boolean> broadcasts = new HashMap();
    public static Map<Integer, String> rankNames = new HashMap();
    public static Map<String, Integer> requiredRanks = new HashMap();
    public static Map<String, List<String>> eventCommands = new HashMap();
    public static DataManager.DatabaseType databaseType;
    public static Settings.BroadcastType broadcastType;
    public static GangSortOrder gangsTopDefaultOrder;
    public static SimpleDateFormat dateFormat;
    public static Pattern gangNamePattern;
    private static FileConfiguration config;

    public Settings() {
    }

    public static void setConfig(BConfig var0) {
        config = var0.getConfig();
        load();
    }

    private static void load() {
        enableModuleAlliances = config.getBoolean("modules.alliances");
        enableModuleBank = config.getBoolean("modules.bank");
        enableModuleChat = config.getBoolean("modules.chat");
        enableModuleFights = config.getBoolean("modules.fights");
        enableModuleHomes = config.getBoolean("modules.homes");
        allowSpaces = config.getBoolean("gangs.allowSpaces");
        friendlyFire = config.getBoolean("gangs.friendlyFire");
        friendlyFireTogglableByLeader = config.getBoolean("gangs.friendlyFireTogglableByLeader");
        showBankBalanceToOthers = config.getBoolean("gangs.showBankBalanceToOthers");
        parseColorCodesInNames = config.getBoolean("gangs.parseColorCodesInNames");
        ignoreColorCodesInNames = config.getBoolean("gangs.ignoreColorCodesInNames");
        gangNameFormatUseLevelBased = config.getBoolean("gangs.gangNameFormat.useLevelBased");
        maxMembersUseLevelBased = config.getBoolean("gangs.maxMembers.useLevelBased");
        maxHomesUseLevelBased = config.getBoolean("gangs.maxHomes.useLevelBased");
        alliancesAllowPvp = config.getBoolean("alliances.allowPvp");
        alliancesAllowFights = config.getBoolean("alliances.allowFights");
        alliancesEnableAllyChat = config.getBoolean("alliances.enableAllyChat");
        fightsFreezeBeforeStart = config.getBoolean("fights.freezeBeforeStart");
        fightsEnableFightTimeLimit = config.getBoolean("fights.enableFightTimeLimit");
        saveDataPeriodically = config.getBoolean("settings.saveDataPeriodically");
        gangChatLogEnable = config.getBoolean("log.gangChatLogEnable");
        allyChatLogEnable = config.getBoolean("log.allyChatLogEnable");
        safeCheckHomes = config.getBoolean("settings.safeCheckHomes");
        levelUpRewardOnlinePlayersOnly = config.getBoolean("settings.levelUpRewardOnlinePlayersOnly");
        sendGangCmdUsage = config.getBoolean("commands.sendGangCmdUsage", true);
        databaseMySQLHost = config.getString("database.mySQLHost", "localhost");
        databaseMySQLDatabase = config.getString("database.mySQLDatabase", "db");
        databaseMySQLUser = config.getString("database.mySQLUser", "root");
        databaseMySQLPassword = config.getString("database.mySQLPassword", "");
        databaseTableNameAlliances = config.getString("database.tableNames.alliances", "alliances");
        databaseTableNameArenas = config.getString("database.tableNames.arenas", "arenas");
        databaseTableNameGangs = config.getString("database.tableNames.gangs", "gangs");
        databaseTableNamePlayers = config.getString("database.tableNames.players", "players");
        allowedNameCharacters = config.getString("gangs.allowedNameCharacters", "[^a-zA-Z0-9\\ ]");
        onlinePlayerNameFormat = config.getString("settings.onlinePlayerNameFormat", "&a%player%&b");
        gangNameFormatDefault = config.getString("gangs.gangNameFormat.default", "&f[%gang%]");
        gangNameFormatNoGang = config.getString("gangs.gangNameFormat.noGang", "");
        messagesInGang = config.getString("messages.inGang", "&b%gang% > &7%message%");
        messagesInArena = config.getString("messages.inArena", "&3Fight > &7%message%");
        messagesGangChatFormat = config.getString("messages.gangChatFormat", "&a[%gang%]&7 %rank% &b%player%&7: %message%");
        messagesAllyChatFormat = config.getString("messages.allyChatFormat", "&b[%gang%]&7 %rank% %player%: %message%");
        gangChatLogFormat = config.getString("log.gangChatLogFormat", "%gang% %player%: %message%");
        allyChatLogFormat = config.getString("log.allyChatLogFormat", "%gang% %player%: %message%");
        databaseMySQLPort = config.getInt("database.mySQLPort", 3306);
        minGangNameLength = config.getInt("gangs.minGangNameLength", 1);
        maxGangNameLength = config.getInt("gangs.maxGangNameLength", 32);
        maxGangLevel = config.getInt("gangs.maxGangLevel", 5);
        maxMembersDefault = config.getInt("gangs.maxMembers.default", 5);
        maxHomesDefault = config.getInt("gangs.maxHomes.default", 5);
        gangsPerPage = config.getInt("settings.gangsPerPage", 10);
        gangsTopLimit = config.getInt("settings.gangsTopLimit", 10);
        assistDamageDelay = config.getInt("settings.assistDamageDelay", 60);
        dataSaveInterval = config.getInt("settings.dataSaveInterval", 10);
        fightsDelayBeforeStart = config.getInt("fights.delayBeforeStart", 30);
        fightsMinMembersAmount = config.getInt("fights.minMembersAmount", 2);
        fightsMaxMembersAmount = config.getInt("fights.maxMembersAmount", 5);
        fightsFightTimeLimit = config.getInt("fights.fightTimeLimit", 300);
        fightsDelayBeforeTeleport = config.getInt("fights.delayBeforeTeleport", 15);
        priceCreate = config.getDouble("prices.create", 0.0D);
        fightsMinMoneyAmount = config.getDouble("fights.minMoneyAmount", 100.0D);
        fightsMaxMoneyAmount = config.getDouble("fights.maxMoneyAmount", 1000000.0D);
        if (config.getStringList("gangs.bannedNames") != null) {
            bannedNames = config.getStringList("gangs.bannedNames");
        }

        if (config.getStringList("gangs.disableGangsInWorlds") != null) {
            disableGangsInWorlds = config.getStringList("gangs.disableGangsInWorlds");
        }

        if (config.getStringList("gangs.disableHomesInWorlds") != null) {
            disableHomesInWorlds = config.getStringList("gangs.disableHomesInWorlds");
        }

        if (config.getStringList("gangs.disableFriendlyFireInWorlds") != null) {
            disableFriendlyFireInWorlds = config.getStringList("gangs.disableFriendlyFireInWorlds");
        }

        if (config.getStringList("fights.disableCommandsDuringFight") != null) {
            fightsDisableCommandsDuringFight = config.getStringList("fights.disableCommandsDuringFight");
        }

        Iterator var0;
        String var1;
        if (config.getConfigurationSection("gangs.gangNameFormat.levelBased") != null) {
            var0 = config.getConfigurationSection("gangs.gangNameFormat.levelBased").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                gangNameFormatLevelBased.put(Integer.valueOf(var1), config.getConfigurationSection("gangs.gangNameFormat.levelBased").getString(var1));
            }
        }

        if (config.getConfigurationSection("gangs.maxMembers.levelBased") != null) {
            var0 = config.getConfigurationSection("gangs.maxMembers.levelBased").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                maxMembersLevelBased.put(Integer.valueOf(var1), config.getConfigurationSection("gangs.maxMembers.levelBased").getInt(var1));
            }
        }

        if (config.getConfigurationSection("gangs.maxHomes.levelBased") != null) {
            var0 = config.getConfigurationSection("gangs.maxHomes.levelBased").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                maxHomesLevelBased.put(Integer.valueOf(var1), config.getConfigurationSection("gangs.maxHomes.levelBased").getInt(var1));
            }
        }

        if (config.getConfigurationSection("gangs.levelUpRewards") != null) {
            var0 = config.getConfigurationSection("gangs.levelUpRewards").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                levelUpRewards.put(Integer.valueOf(var1), config.getConfigurationSection("gangs.levelUpRewards").getStringList(var1 + ".commands"));
            }
        }

        if (config.getConfigurationSection("commands") != null) {
            var0 = config.getConfigurationSection("commands").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                commands.put(var1, config.getConfigurationSection("commands").getStringList(var1));
            }
        }

        if (config.getConfigurationSection("cooldowns") != null) {
            var0 = config.getConfigurationSection("cooldowns").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                cooldowns.put(var1, config.getConfigurationSection("cooldowns").getInt(var1));
            }
        }

        if (config.getConfigurationSection("prices.levelup") != null) {
            var0 = config.getConfigurationSection("prices.levelup").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                priceLevelup.put(Integer.valueOf(var1), config.getConfigurationSection("prices.levelup").getDouble(var1));
            }
        }

        if (config.getConfigurationSection("broadcasts.broadcast") != null) {
            var0 = config.getConfigurationSection("broadcasts.broadcast").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                broadcasts.put(var1, config.getConfigurationSection("broadcasts.broadcast").getBoolean(var1));
            }
        }

        if (config.getConfigurationSection("ranks.ranks") != null) {
            var0 = config.getConfigurationSection("ranks.ranks").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                rankNames.put(Integer.valueOf(var1), config.getConfigurationSection("ranks.ranks").getString(var1));
            }
        }

        if (config.getConfigurationSection("ranks.requiredRanks") != null) {
            var0 = config.getConfigurationSection("ranks.requiredRanks").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                requiredRanks.put(var1, config.getConfigurationSection("ranks.requiredRanks").getInt(var1));
            }
        }

        if (config.getConfigurationSection("eventCommands") != null) {
            var0 = config.getConfigurationSection("eventCommands").getKeys(false).iterator();

            while (var0.hasNext()) {
                var1 = (String) var0.next();
                eventCommands.put(var1, config.getConfigurationSection("eventCommands").getStringList(var1));
            }
        }

        try {
            databaseType = DataManager.DatabaseType.valueOf(config.getString("database.type").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException var4) {
            databaseType = DataManager.DatabaseType.SQLITE;
        }

        try {
            broadcastType = Settings.BroadcastType.valueOf(config.getString("broadcasts.type").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException var3) {
            broadcastType = Settings.BroadcastType.MESSAGE;
        }

        try {
            gangsTopDefaultOrder = GangSortOrder.valueOf(config.getString("settings.gangsTopDefaultOrder").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException var2) {
            gangsTopDefaultOrder = GangSortOrder.LEVEL;
        }
        dateFormat = new SimpleDateFormat(config.getString("settings.dateFormat"));
        gangNamePattern = Pattern.compile(allowedNameCharacters);
    }

    public static void broadcast(String var0) {
        switch (broadcastType) {
            case MESSAGE:
                Bukkit.broadcastMessage(StringUtils.fixColors(Lang.PREFIX + var0));
                return;
            case TITLE:
                Bukkit.getOnlinePlayers().forEach((var1) -> {
                    TitleUtils.sendTitle(var1, var0, "", 1, 5, 1);
                });
                return;
            case SUBTITLE:
                Bukkit.getOnlinePlayers().forEach((var1) -> {
                    TitleUtils.sendTitle(var1, "", var0, 1, 5, 1);
                });
            default:
        }
    }

    public static String getRankName(int var0) {
        return rankNames.getOrDefault(var0, "");
    }

    public static int getRequiredRank(String var0) {
        return requiredRanks.getOrDefault(var0, 0);
    }

    public static int getLowestRank() {
        return Collections.min(rankNames.keySet());
    }

    public static int getHighestRank() {
        return Collections.max(rankNames.keySet());
    }

    public static boolean getBroadcast(String var0) {
        return broadcasts.getOrDefault(var0, false);
    }

    public static int getCooldown(String var0) {
        return cooldowns.getOrDefault(var0, 0);
    }

    public static List<String> getCommand(String var0) {
        return commands.containsKey(var0) ? (List) commands.get(var0) : new ArrayList();
    }

    public static double getPriceLevelup(int var0) {
        return priceLevelup.getOrDefault(var0, 0.0D);
    }

    public static List<String> getLevelupRewards(int var0) {
        return levelUpRewards.containsKey(var0) ? (List) levelUpRewards.get(var0) : new ArrayList();
    }

    public static List<String> getEventCommands(String var0) {
        return eventCommands.containsKey(var0) ? (List) eventCommands.get(var0) : new ArrayList();
    }

    public static String formatDate(long var0) {
        return dateFormat.format(new Date(var0));
    }

    public enum BroadcastType {
        MESSAGE,
        TITLE,
        SUBTITLE;

        BroadcastType() {
        }
    }
}
