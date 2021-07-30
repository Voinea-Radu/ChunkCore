package dev.lightdream.ranks.api;

import dev.lightdream.ranks.UltraPrisonRankup;
import dev.lightdream.ranks.rank.Prestige;
import dev.lightdream.ranks.rank.Rank;
import org.bukkit.entity.Player;

public class UltraPrisonRankupAPIImpl implements UltraPrisonRankupAPI{

    private UltraPrisonRankup plugin;

    public UltraPrisonRankupAPIImpl(UltraPrisonRankup plugin) {
        this.plugin = plugin;
    }

    @Override
    public Rank getPlayerRank(Player p) {
        return plugin.getRankManager().getPlayerRank(p);
    }

    @Override
    public Prestige getPlayerPrestige(Player p) {
        return plugin.getRankManager().getPlayerPrestige(p);
    }

    @Override
    public Rank getNextPlayerRank(Player player) {
        return plugin.getRankManager().getNextRank(this.getPlayerRank(player).getId());
    }

    @Override
    public int getRankupProgress(Player player) {
        return plugin.getRankManager().getRankupProgress(player);
    }
}
