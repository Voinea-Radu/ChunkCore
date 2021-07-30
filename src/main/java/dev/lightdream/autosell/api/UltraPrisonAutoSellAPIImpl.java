package dev.lightdream.autosell.api;

import dev.lightdream.autosell.SellRegion;
import dev.lightdream.autosell.UltraPrisonAutoSell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class UltraPrisonAutoSellAPIImpl implements UltraPrisonAutoSellAPI {

    private UltraPrisonAutoSell plugin;

    public UltraPrisonAutoSellAPIImpl(UltraPrisonAutoSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public double getCurrentEarnings(Player player) {
        return plugin.getCurrentEarnings(player);
    }

    @Override
    public double getPriceForBlock(String regionName, Material material) {
        return plugin.getPriceForBrokenBlock(regionName, material);
    }

    @Override
    public boolean hasAutoSellEnabled(Player p) {
        return plugin.hasAutoSellEnabled(p);
    }

    @Override
    public Collection<SellRegion> getSellRegions() {
        return plugin.getAutoSellRegions();
    }

    @Override
    public SellRegion getSellRegionAtLocation(Location location) {
        return plugin.getAutoSellRegion(location);
    }
}
