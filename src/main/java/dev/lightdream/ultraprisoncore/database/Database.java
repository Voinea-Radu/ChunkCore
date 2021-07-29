package dev.lightdream.ultraprisoncore.database;

import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.multipliers.multiplier.PlayerMultiplier;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Database {

    protected final UltraPrisonCore plugin;

    public Database(UltraPrisonCore plugin) {
        this.plugin = plugin;
    }

    public abstract PlayerMultiplier getPlayerPersonalMultiplier(OfflinePlayer player);

    public abstract long getPlayerTokens(OfflinePlayer player);

    public abstract long getPlayerGems(OfflinePlayer player);

    public abstract long getPlayerBrokenBlocks(OfflinePlayer player);

    public abstract int getPlayerRank(OfflinePlayer player);

    public abstract long getPlayerPrestige(OfflinePlayer player);

    public abstract int getPlayerAutoMinerTime(OfflinePlayer player);

    public abstract void updateTokens(OfflinePlayer player, long newAmount);

    public abstract void resetAllData(CommandSender sender);

    public abstract void resetBlocksWeekly(CommandSender sender);

    public abstract void updateGems(OfflinePlayer player, long newAmount);

    public abstract void updateRankAndPrestige(OfflinePlayer player, int rank, long prestige);

    public abstract void removeExpiredAutoMiners();

    public abstract void removeExpiredMultipliers();

    public abstract void updateBlocks(OfflinePlayer player, long newAmount);

    public abstract void updateBlocksWeekly(OfflinePlayer player, long newAmount);

    public abstract long getPlayerBrokenBlocksWeekly(OfflinePlayer player);

    public abstract void savePersonalMultiplier(Player player, PlayerMultiplier multiplier);

    public abstract void saveAutoMiner(Player p, int timeLeft);

    public abstract Map<UUID, Integer> getTop10Prestiges();

    public abstract Map<UUID, Long> getTop10Gems();

    public abstract Map<UUID, Long> getTop10BlocksWeekly();

    public abstract Map<UUID, Long> getTop10Tokens();

    public abstract Map<UUID, Long> getTop10Blocks();

    public abstract void addIntoTokens(OfflinePlayer player);

    public abstract void addIntoBlocks(OfflinePlayer player);

    public abstract void addIntoBlocksWeekly(OfflinePlayer player);

    public abstract void addIntoGems(OfflinePlayer player);

    public abstract void addIntoRanksAndPrestiges(OfflinePlayer player);

    public abstract void updatePlayerNickname(OfflinePlayer player);

    public abstract List<UUID> getFriends(UUID uuid);

    public abstract void setFriends(UUID uuid, List<UUID> friends);

    public abstract void addFriend(UUID uuid, UUID friend);

    public abstract void removeFriend(UUID uuid, UUID friend);

    public abstract boolean checkFriends(UUID uuid, UUID friend);
}
