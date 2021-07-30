package dev.lightdream.enchants.enchants.implementations;

import dev.lightdream.enchants.UltraPrisonEnchants;
import dev.lightdream.enchants.enchants.UltraPrisonEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class PrestigeFinderEnchant extends UltraPrisonEnchantment {

    private double chance;
    private int minLevels;
    private int maxLevels;

    public PrestigeFinderEnchant(UltraPrisonEnchants instance) {
        super(instance, 16);
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {
        if (chance * enchantLevel >= ThreadLocalRandom.current().nextDouble(100)) {
            int levels = ThreadLocalRandom.current().nextInt(this.minLevels, this.maxLevels);
            this.plugin.getCore().getRanks().getRankManager().givePrestige(e.getPlayer(), levels);
        }
    }

    @Override
    public void reload() {
        this.chance = plugin.getConfig().get().getDouble("enchants." + id + ".Chance");
        this.minLevels = plugin.getConfig().get().getInt("enchants." + id + ".Min-Levels");
        this.maxLevels = plugin.getConfig().get().getInt("enchants." + id + ".Max-Levels");
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }
}
