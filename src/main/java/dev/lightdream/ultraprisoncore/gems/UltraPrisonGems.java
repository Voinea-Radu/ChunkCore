package dev.lightdream.ultraprisoncore.gems;

import dev.lightdream.ultraprisoncore.gems.api.UltraPrisonGemsAPI;
import dev.lightdream.ultraprisoncore.gems.api.UltraPrisonGemsAPIImpl;
import dev.lightdream.ultraprisoncore.gems.commands.GemsCommand;
import dev.lightdream.ultraprisoncore.gems.managers.GemsManager;
import lombok.Getter;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.config.FileManager;
import me.lucko.helper.Commands;
import me.lucko.helper.text.Text;
import me.lucko.helper.utils.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public final class UltraPrisonGems implements UltraPrisonModule {

    public static final String GEMS_ADMIN_PERM = "ultraprison.gems.admin";

    @Getter
    private static UltraPrisonGems instance;

    @Getter
    private FileManager.Config config;

    @Getter
    private UltraPrisonGemsAPI api;

    @Getter
    private GemsManager gemsManager;
    @Getter
    private UltraPrisonCore core;

    private HashMap<String, String> messages;
    private boolean enabled;

    public UltraPrisonGems(UltraPrisonCore UltraPrisonCore) {
        instance = this;
        this.core = UltraPrisonCore;
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.config.reload();
        this.loadMessages();
        this.gemsManager.reload();
    }

    @Override
    public void enable() {
        this.enabled = true;
        this.config = this.core.getFileManager().getConfig("gems.yml").copyDefaults(true).save();
        this.loadMessages();
        this.gemsManager = new GemsManager(this);
        this.api = new UltraPrisonGemsAPIImpl(this.gemsManager);
        this.registerCommands();
        this.registerEvents();
    }



    @Override
    public void disable() {
        this.gemsManager.stopUpdating();
        this.gemsManager.savePlayerDataOnDisable();
        this.enabled = false;

    }

    @Override
    public String getName() {
        return "Gems";
    }

    private void registerEvents() {
        //TODO: add support ?
        /*Events.subscribe(PlayerInteractEvent.class)
                .filter(e -> e.getItem() != null && e.getItem().getType() == Material.DOUBLE_PLANT && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR))
                .handler(e -> {
                    if (e.getItem().hasItemMeta()) {
                        this.gemsManager.redeemGems(e.getPlayer(), e.getItem(), e.getPlayer().isSneaking());
                    }
                })
                .bindWith(core);*/
    }

    private void registerCommands() {
        Commands.create()
                .handler(c -> {
                    if (c.args().size() == 0 && c.sender() instanceof Player) {
                        this.gemsManager.sendInfoMessage(c.sender(), (OfflinePlayer) c.sender());
                        return;
                    }
                    GemsCommand subCommand = GemsCommand.getCommand(c.rawArg(0));
                    if (subCommand != null) {
                        subCommand.execute(c.sender(), c.args().subList(1, c.args().size()));
                    } else {
                        OfflinePlayer target = Players.getOfflineNullable(c.rawArg(0));
                        this.gemsManager.sendInfoMessage(c.sender(), target);
                    }
                })
                .registerAndBind(core, "gems");
        Commands.create()
                .handler(c -> {
                    if (c.args().size() == 0) {
                        this.gemsManager.sendGemsTop(c.sender());
                    }
                })
                .registerAndBind(core, "gemstop", "gemtop");
    }

    private void loadMessages() {
        messages = new HashMap<>();
        for (String key : this.getConfig().get().getConfigurationSection("messages").getKeys(false)) {
            messages.put(key, Text.colorize(this.getConfig().get().getString("messages." + key)));
        }
    }

    public String getMessage(String key) {
        return messages.get(key);
    }
}
