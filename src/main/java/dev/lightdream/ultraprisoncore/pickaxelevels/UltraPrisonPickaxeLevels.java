package dev.lightdream.ultraprisoncore.pickaxelevels;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.config.FileManager;
import dev.lightdream.ultraprisoncore.pickaxelevels.api.UltraPrisonPickaxeLevelsAPI;
import dev.lightdream.ultraprisoncore.pickaxelevels.api.UltraPrisonPickaxeLevelsAPIImpl;
import dev.lightdream.ultraprisoncore.pickaxelevels.model.PickaxeLevel;
import lombok.Getter;
import me.lucko.helper.Events;
import me.lucko.helper.event.filter.EventFilters;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.text.Text;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.WorldGuardWrapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UltraPrisonPickaxeLevels implements UltraPrisonModule {


    private static final String ADMIN_PERMISSION = "ultraprison.pickaxe.admin";
    private static final String NBT_TAG_INDETIFIER = "ultra-prison-pickaxe-level";

    @Getter
    private FileManager.Config config;

    private Map<Integer, PickaxeLevel> pickaxeLevels;
    private Map<String, String> messages;
    private PickaxeLevel defaultLevel;
    private PickaxeLevel maxLevel;
    @Getter
    private UltraPrisonPickaxeLevelsAPI api;
    @Getter
    private UltraPrisonCore core;
    private boolean enabled;

    public UltraPrisonPickaxeLevels(UltraPrisonCore UltraPrisonCore) {
        this.core = UltraPrisonCore;
    }

    private void loadMessages() {
        messages = new HashMap<>();
        for (String key : this.getConfig().get().getConfigurationSection("messages").getKeys(false)) {
            messages.put(key.toLowerCase(), Text.colorize(this.getConfig().get().getString("messages." + key)));
        }
    }

    private void loadPickaxeLevels() {
        pickaxeLevels = new LinkedHashMap<>();

        ConfigurationSection section = this.getConfig().get().getConfigurationSection("levels");
        if (section == null) {
            return;
        }

        for (String level : section.getKeys(false)) {

            int levelId = Integer.parseInt(level);

            String displayName = Text.colorize(this.getConfig().get().getString("levels." + level + ".display_name"));
            long blocksRequire = this.getConfig().get().getLong("levels." + level + ".blocks_required");
            List<String> rewards = this.getConfig().get().getStringList("levels." + level + ".rewards");


            PickaxeLevel pickaxeLevel = new PickaxeLevel(levelId, blocksRequire, displayName, rewards);

            if (levelId == 1) {
                this.defaultLevel = pickaxeLevel;
            }

            this.pickaxeLevels.put(levelId, pickaxeLevel);
            this.maxLevel = pickaxeLevel;

            this.core.getLogger().info("Loaded Pickaxe Level " + levelId);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.config.reload();
        this.loadMessages();
        this.loadPickaxeLevels();
    }

    @Override
    public void enable() {
        this.enabled = true;

        this.config = this.core.getFileManager().getConfig("pickaxe-levels.yml").copyDefaults(true).save();


        this.loadPickaxeLevels();
        this.loadMessages();
        this.registerCommands();
        this.registerListeners();

        this.api = new UltraPrisonPickaxeLevelsAPIImpl(this);
    }

    private void registerListeners() {

        Events.subscribe(BlockBreakEvent.class, EventPriority.HIGHEST)
                .filter(EventFilters.ignoreCancelled())
                .filter(e -> WorldGuardWrapper.getInstance().getRegions(e.getBlock().getLocation()).stream().anyMatch(region -> region.getId().toLowerCase().startsWith("mine")))
                .filter(e -> e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getPlayer().getItemInHand() != null && this.getCore().isPickaxeSupported(e.getPlayer().getItemInHand().getType()))
                .handler(e -> {
                    //Check for next level progression

                    PickaxeLevel currentLevel = this.getPickaxeLevel(e.getPlayer().getItemInHand());
                    PickaxeLevel nextLevel = this.getNextPickaxeLevel(currentLevel);

                    if (nextLevel != null && this.core.getEnchants().getEnchantsManager().getBlocksBroken(e.getPlayer().getItemInHand()) >= nextLevel.getBlocksRequired()) {
                        nextLevel.giveRewards(e.getPlayer());
                        e.getPlayer().setItemInHand(this.setPickaxeLevel(e.getPlayer().getItemInHand(), nextLevel, e.getPlayer()));
                        e.getPlayer().sendMessage(this.getMessage("pickaxe-level-up").replace("%level%", String.valueOf(nextLevel.getLevel())));
                    }
                }).bindWith(core);
        Events.subscribe(PlayerItemHeldEvent.class)
                .handler(e -> {
                    ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
                    if (item != null && this.getCore().isPickaxeSupported(item.getType())) {
                        e.getPlayer().getInventory().setItem(e.getNewSlot(), this.addDefaultPickaxeLevel(item, e.getPlayer()));
                    }
                }).bindWith(core);
    }

    public PickaxeLevel getNextPickaxeLevel(PickaxeLevel currentLevel) {
        if (currentLevel == null || currentLevel == maxLevel) {
            return null;
        }
        return this.pickaxeLevels.get(currentLevel.getLevel() + 1);
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public String getName() {
        return "Pickaxe Levels";
    }

    private void registerCommands() {

    }


    public String getMessage(String key) {
        return messages.get(key.toLowerCase());
    }

    public PickaxeLevel getPickaxeLevel(ItemStack itemStack) {
        if (itemStack == null || !this.getCore().isPickaxeSupported(itemStack.getType())) {
            return null;
        }

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey(NBT_TAG_INDETIFIER)) {
            return defaultLevel;
        }

        return this.pickaxeLevels.get(nbtItem.getInteger(NBT_TAG_INDETIFIER));
    }

    public ItemStack setPickaxeLevel(ItemStack item, PickaxeLevel level, Player p) {

        if (level.getLevel() <= 0 || level.getLevel() > this.maxLevel.getLevel()) {
            return item;
        }

        NBTItem nbtItem = new NBTItem(item);

        if (!nbtItem.hasKey(NBT_TAG_INDETIFIER)) {
            nbtItem.setInteger(NBT_TAG_INDETIFIER, 0);
        }

        nbtItem.setInteger(NBT_TAG_INDETIFIER, level.getLevel());

        ItemStackBuilder builder = ItemStackBuilder.of(nbtItem.getItem());
        if (level.getDisplayName() != null && !level.getDisplayName().isEmpty()) {
            builder = builder.name(level.getDisplayName(p));
        }

        item = builder.build();
        this.core.getEnchants().getEnchantsManager().updatePickaxe(item);
        return item;
    }

    private ItemStack addDefaultPickaxeLevel(ItemStack item, Player p) {

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasKey(NBT_TAG_INDETIFIER)) {
            return item;
        }

        nbtItem.setInteger(NBT_TAG_INDETIFIER, 1);

        ItemStackBuilder builder = ItemStackBuilder.of(nbtItem.getItem());
        if (defaultLevel.getDisplayName() != null && !defaultLevel.getDisplayName().isEmpty()) {
            builder = builder.name(defaultLevel.getDisplayName(p));
        }

        item = builder.build();
        this.core.getEnchants().getEnchantsManager().updatePickaxe(item);
        return item;
    }


    public ItemStack findPickaxe(Player p) {
        for (ItemStack i : p.getInventory()) {
            if (i == null) {
                continue;
            }
            if (this.getCore().isPickaxeSupported(i.getType())) {
                return i;
            }
        }
        return null;
    }

    public String getProgressBar(Player player) {
        ItemStack pickaxe = findPickaxe(player);
        return this.getProgressBar(pickaxe);
    }

    public String getProgressBar(ItemStack item) {

        PickaxeLevel level = this.getPickaxeLevel(item);
        PickaxeLevel nextLevel = this.getNextPickaxeLevel(level);

        if (nextLevel != null) {
            long required = nextLevel.getBlocksRequired() - level.getBlocksRequired();
            double treshold = required / 20.0;
            long collected = this.core.getEnchants().getEnchantsManager().getBlocksBroken(item) - level.getBlocksRequired();

            String result = "";
            for (int i = 0; i < 20; i++) {
                if (collected >= treshold * (i + 1)) {
                    result += "&a:";
                } else {
                    result += "&c:";
                }
            }
            return Text.colorize(result);

        } else {
            return Text.colorize("&a::::::::::::::::::::");
        }

    }
}
