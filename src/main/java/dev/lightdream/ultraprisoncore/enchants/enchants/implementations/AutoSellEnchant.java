package dev.lightdream.ultraprisoncore.enchants.enchants.implementations;

import dev.lightdream.ultraprisoncore.enchants.enchants.UltraPrisonEnchantment;
import dev.lightdream.ultraprisoncore.enchants.UltraPrisonEnchants;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class AutoSellEnchant extends UltraPrisonEnchantment {

    private double chance;

    public AutoSellEnchant(UltraPrisonEnchants instance) {
        super(instance, 19);
    }

    @Override
    public String getAuthor() {
        return "Drawethree";
    }

    @Override
    public void onEquip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onUnequip(Player p, ItemStack pickAxe, int level) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int enchantLevel) {

        if (this.chance * enchantLevel >= ThreadLocalRandom.current().nextDouble(100)) {
            e.getPlayer().performCommand("sellall");
        }

    }

    @Override
    public void reload() {
        this.chance = plugin.getConfig().get().getDouble("enchants." + id + ".Chance");
    }
}
