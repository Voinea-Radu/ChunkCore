package dev.lightdream.enchants.enchants.implementations;

import dev.lightdream.enchants.UltraPrisonEnchants;
import dev.lightdream.enchants.enchants.UltraPrisonEnchantment;
import dev.lightdream.utils.compat.CompMaterial;
import me.lucko.helper.cooldown.Cooldown;
import me.lucko.helper.cooldown.CooldownMap;
import me.lucko.helper.time.Time;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExplosiveEnchant extends UltraPrisonEnchantment {
    private double chance;
    private int cooldown;
    private final CooldownMap<Player> cooldownMap;


    public ExplosiveEnchant(UltraPrisonEnchants instance) {
        super(instance, 9);
        this.cooldownMap = CooldownMap.create(Cooldown.of(cooldown, TimeUnit.SECONDS));
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
        if (plugin.hasExplosiveDisabled(e.getPlayer())) {
            return;
        }
        if (!cooldownMap.test(e.getPlayer())) {
            return;
        }
        if (chance * enchantLevel >= ThreadLocalRandom.current().nextDouble(100)) {
			long timeStart = Time.nowMillis();
            Block b = e.getBlock();
            List<IWrappedRegion> regions = WorldGuardWrapper.getInstance().getRegions(b.getLocation()).stream().filter(reg -> reg.getId().toLowerCase().startsWith("mine")).collect(Collectors.toList());
            if (regions.size() > 0) {
                Player p = e.getPlayer();
                IWrappedRegion region = regions.get(0);
                int threshold = this.getMaxLevel() / 3;
                int radius = enchantLevel <= threshold ? 3 : enchantLevel <= threshold * 2 ? 4 : 5;

                b.getWorld().createExplosion(b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ(), 0F, false, false);

                List<Block> blocksAffected = new ArrayList<>();
                //int move = (radius / 2 - 1) + (radius % 2 == 0 ? 0 : 1);
                double totalDeposit = 0;
                int blockCount = 0;
                int fortuneLevel = plugin.getApi().getEnchantLevel(p.getItemInHand(), 3);
                int amplifier = fortuneLevel == 0 ? 1 : fortuneLevel + 1;

                final Location startLocation = e.getBlock().getLocation();

                for (int x = startLocation.getBlockX() - (radius == 4 ? 0 : (radius / 2)); x <= startLocation.getBlockX() + (radius == 4 ? radius - 1 : (radius / 2)); x++) {
                    for (int z = startLocation.getBlockZ() - (radius == 4 ? 0 : (radius / 2)); z <= startLocation.getBlockZ() + (radius == 4 ? radius - 1 : (radius / 2)); z++) {
                        for (int y = startLocation.getBlockY() - (radius == 4 ? 3 : (radius / 2)); y <= startLocation.getBlockY() + (radius == 4 ? 0 : (radius / 2)); y++) {
                            Block b1 = b.getWorld().getBlockAt(x, y, z);
                            if (region.contains(b1.getLocation()) && b1.getType() != Material.AIR) {
                                blockCount++;
                                blocksAffected.add(b1);
                                if (plugin.getCore().getAutoSell().isEnabled() && plugin.getCore().getAutoSell().hasAutoSellEnabled(p)) {
                                    totalDeposit += ((plugin.getCore().getAutoSell().getPriceForBrokenBlock(region.getId(), b1.getType()) + 0.0) * amplifier);
                                } else {
                                    p.getInventory().addItem(new ItemStack(b1.getType(), fortuneLevel + 1));
                                }
                                b1.setType(CompMaterial.AIR.toMaterial());
                            }
                        }
                    }
                }

                if (plugin.getCore().getJetsPrisonMinesAPI() != null) {
                    plugin.getCore().getJetsPrisonMinesAPI().blockBreak(blocksAffected);
                }

                boolean luckyBooster = LuckyBoosterEnchant.hasLuckyBoosterRunning(e.getPlayer());

                double total = luckyBooster ? plugin.getCore().getMultipliers().getApi().getTotalToDeposit(p, totalDeposit) * 2 : plugin.getCore().getMultipliers().getApi().getTotalToDeposit(p, totalDeposit);

                plugin.getCore().getEconomy().depositPlayer(p, total);

                if (plugin.getCore().getAutoSell().isEnabled()) {
                    plugin.getCore().getAutoSell().addToCurrentEarnings(p, total);
                }

                plugin.getEnchantsManager().addBlocksBrokenToItem(p, blockCount);
                plugin.getCore().getTokens().getTokensManager().addBlocksBroken(null, p, blockCount);
                plugin.getCore().getTokens().handleBlockBreak(p, blocksAffected);
            }
			long timeEnd = Time.nowMillis();
			this.plugin.getCore().debug("ExplosiveEnchant::onBlockBreak >> Took " + (timeEnd - timeStart) + " ms.");

        }
    }

    @Override
    public void reload() {
        this.chance = plugin.getConfig().get().getDouble("enchants." + id + ".Chance");
        this.cooldown = plugin.getConfig().get().getInt("enchants." + id + ".Cooldown");
    }
}
