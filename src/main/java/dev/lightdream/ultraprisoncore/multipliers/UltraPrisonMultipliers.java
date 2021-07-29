package dev.lightdream.ultraprisoncore.multipliers;

import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.config.FileManager;
import dev.lightdream.ultraprisoncore.multipliers.api.UltraPrisonMultipliersAPI;
import dev.lightdream.ultraprisoncore.multipliers.api.UltraPrisonMultipliersAPIImpl;
import dev.lightdream.ultraprisoncore.multipliers.multiplier.Multiplier;
import lombok.Getter;
import dev.lightdream.ultraprisoncore.multipliers.multiplier.GlobalMultiplier;
import dev.lightdream.ultraprisoncore.multipliers.multiplier.PlayerMultiplier;
import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.scheduler.Task;
import me.lucko.helper.text.Text;
import me.lucko.helper.time.Time;
import me.lucko.helper.utils.Players;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class UltraPrisonMultipliers implements UltraPrisonModule {


    public static final String MODULE_NAME = "Multipliers";

    private static GlobalMultiplier GLOBAL_MULTIPLIER;

    @Getter
    private static UltraPrisonMultipliers instance;

    @Getter
    private FileManager.Config config;

    @Getter
    private UltraPrisonMultipliersAPI api;

    private HashMap<UUID, Multiplier> rankMultipliers;
    private HashMap<UUID, PlayerMultiplier> personalMultipliers;

    private HashMap<String, String> messages;
    private LinkedHashMap<String, Double> permissionToMultiplier;


    @Getter
    private UltraPrisonCore core;
    private boolean enabled;

    private Task rankUpdateTask;
    private int rankMultiplierUpdateTime;

    @Getter
    private double globalMultiMax;
    @Getter
    private double playerMultiMax;

    public UltraPrisonMultipliers(UltraPrisonCore UltraPrisonCore) {
        instance = this;
        this.core = UltraPrisonCore;
    }


    private void loadRankMultipliers() {
        this.permissionToMultiplier = new LinkedHashMap<>();

        ConfigurationSection section = getConfig().get().getConfigurationSection("ranks");

        if (section == null) {
            return;
        }

		boolean useLuckPerms = getConfig().get().getBoolean("use-luckperms-groups", false);

		String permPrefix = useLuckPerms ? "group." : "ultraprison.multiplier.";
        for (String rank : section.getKeys(false)) {
			String perm = permPrefix + rank;
            double multiplier = getConfig().get().getDouble("ranks." + rank);
            this.permissionToMultiplier.put(perm, multiplier);
            this.core.getLogger().info("Loaded rank multiplier." + rank + " with multiplier " + multiplier + " (" + perm + ")");
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
        this.loadRankMultipliers();

        this.rankMultiplierUpdateTime = this.getConfig().get().getInt("rank-multiplier-update-time");
    }

    @Override
    public void enable() {

        this.enabled = true;
        this.config = this.core.getFileManager().getConfig("multipliers.yml").copyDefaults(true).save();

        this.rankMultipliers = new HashMap<>();
        this.personalMultipliers = new HashMap<>();

        this.rankMultiplierUpdateTime = this.getConfig().get().getInt("rank-multiplier-update-time");
        this.globalMultiMax = this.getConfig().get().getDouble("global-multiplier.max");
        this.playerMultiMax = this.getConfig().get().getDouble("player-multiplier.max");

        this.loadMessages();
        this.loadRankMultipliers();
        this.registerCommands();
        this.registerEvents();
        this.removeExpiredMultipliers();
        this.loadGlobalMultiplier();
        this.loadOnlineMultipliers();
        api = new UltraPrisonMultipliersAPIImpl(this);

        this.rankUpdateTask = Schedulers.async().runRepeating(() -> {
            Players.all().forEach(p -> {
                this.rankMultipliers.put(p.getUniqueId(), this.calculateRankMultiplier(p));

            });
        }, this.rankMultiplierUpdateTime, TimeUnit.MINUTES, this.rankMultiplierUpdateTime, TimeUnit.MINUTES);
    }

    private void loadOnlineMultipliers() {
        Players.all().forEach(p -> {
            this.rankMultipliers.put(p.getUniqueId(), this.calculateRankMultiplier(p));
            this.loadPersonalMultiplier(p);
        });
    }

    private void registerEvents() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> {
                    this.rankMultipliers.put(e.getPlayer().getUniqueId(), this.calculateRankMultiplier(e.getPlayer()));
                    this.loadPersonalMultiplier(e.getPlayer());
                }).bindWith(core);
        Events.subscribe(PlayerQuitEvent.class)
                .handler(e -> {
                    this.rankMultipliers.remove(e.getPlayer().getUniqueId());
                    this.savePersonalMultiplier(e.getPlayer(), true);
                }).bindWith(core);
    }

    private void savePersonalMultiplier(Player player, boolean async) {

        if (!personalMultipliers.containsKey(player.getUniqueId())) {
            return;
        }

        PlayerMultiplier multiplier = personalMultipliers.get(player.getUniqueId());

        if (async) {
            Schedulers.async().run(() -> {
                this.core.getPluginDatabase().savePersonalMultiplier(player, multiplier);
                this.personalMultipliers.remove(player.getUniqueId());
                this.core.getLogger().info(String.format("Saved multiplier of player %s", player.getName()));
            });
        } else {
            this.core.getPluginDatabase().savePersonalMultiplier(player, multiplier);
            this.personalMultipliers.remove(player.getUniqueId());
            this.core.getLogger().info(String.format("Saved multiplier of player %s", player.getName()));
        }
    }

    private void loadGlobalMultiplier() {
        double multi = this.config.get().getDouble("global-multiplier.multiplier");
        long timeLeft = this.config.get().getLong("global-multiplier.timeLeft");

        GLOBAL_MULTIPLIER = new GlobalMultiplier(0.0, 0);
        GLOBAL_MULTIPLIER.setMultiplier(multi,this.globalMultiMax);

        if (timeLeft > Time.nowMillis()) {
            GLOBAL_MULTIPLIER.setDuration(timeLeft);
        } else {
            GLOBAL_MULTIPLIER.setDuration(0);
        }

        this.core.getLogger().info(String.format("Loaded Global Multiplier %.2fx", multi));
    }

    private void saveGlobalMultiplier() {
        this.config.set("global-multiplier.multiplier", GLOBAL_MULTIPLIER.getMultiplier());
        this.config.set("global-multiplier.timeLeft", GLOBAL_MULTIPLIER.getEndTime());
        this.config.save();
        this.core.getLogger().info("Saved Global Multiplier into multipliers.yml");
    }

    private void loadPersonalMultiplier(Player player) {
        Schedulers.async().run(() -> {
            PlayerMultiplier multiplier = this.core.getPluginDatabase().getPlayerPersonalMultiplier(player);

            if (multiplier == null) {
                multiplier = new PlayerMultiplier(player.getUniqueId(), 0, 0);
            }

            personalMultipliers.put(player.getUniqueId(), multiplier);

            this.core.getLogger().info(String.format("Loaded multiplier %.2fx for player %s", multiplier.getMultiplier(), player.getName()));
        });
    }

    private void removeExpiredMultipliers() {
        Schedulers.async().run(() -> {
            this.core.getPluginDatabase().removeExpiredMultipliers();
            this.core.getLogger().info("Removed expired multipliers from database");
        });
    }


    @Override
    public void disable() {
        this.saveAllMultipliers();
        this.rankUpdateTask.stop();
        this.enabled = false;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    private void saveAllMultipliers() {
        Players.all().forEach(p -> savePersonalMultiplier(p, false));
        this.saveGlobalMultiplier();
    }

    private void loadMessages() {
        messages = new HashMap<>();
        for (String key : getConfig().get().getConfigurationSection("messages").getKeys(false)) {
            messages.put(key.toLowerCase(), Text.colorize(getConfig().get().getString("messages." + key)));
        }
    }

    private void registerCommands() {
        Commands.create()
                .assertPermission("ultraprison.multipliers.admin")
                .handler(c -> {
                    if (c.args().size() == 2) {
                        double amount = c.arg(0).parseOrFail(Double.class).doubleValue();
                        int minutes = c.arg(1).parseOrFail(Integer.class).intValue();
                        setupGlobalMultiplier(c.sender(), minutes, amount);
                    }
                }).registerAndBind(core, "globalmultiplier", "gmulti");
        Commands.create()
                .assertPermission("ultraprison.multipliers.admin")
                .handler(c -> {
                    if (c.args().size() == 3) {
                        Player onlinePlayer = Players.getNullable(c.rawArg(0));
                        double amount = c.arg(1).parseOrFail(Double.class).doubleValue();
                        int minutes = c.arg(2).parseOrFail(Integer.class).intValue();
                        setupPersonalMultiplier(c.sender(), onlinePlayer, amount, minutes);
                    }
                }).registerAndBind(core, "personalmultiplier", "pmulti");
        Commands.create()
                .assertPlayer()
                .handler(c -> {
                    c.sender().sendMessage(messages.get("global_multi").replace("%multiplier%", String.valueOf(GLOBAL_MULTIPLIER.getMultiplier())).replace("%duration%", GLOBAL_MULTIPLIER.getTimeLeft()));
                    c.sender().sendMessage(messages.get("rank_multi").replace("%multiplier%", String.valueOf(rankMultipliers.getOrDefault(c.sender().getUniqueId(), Multiplier.getDefaultPlayerMultiplier()).getMultiplier())));
                    c.sender().sendMessage(messages.get("vote_multi").replace("%multiplier%", String.valueOf(personalMultipliers.getOrDefault(c.sender().getUniqueId(), Multiplier.getDefaultPlayerMultiplier(c.sender().getUniqueId())).getMultiplier())).replace("%duration%", personalMultipliers.getOrDefault(c.sender().getUniqueId(), Multiplier.getDefaultPlayerMultiplier(c.sender().getUniqueId())).getTimeLeft()));
                }).registerAndBind(core, "multiplier", "multi");
    }

    private void setupPersonalMultiplier(CommandSender sender, Player onlinePlayer, double amount, int minutes) {
        if (onlinePlayer == null || !onlinePlayer.isOnline()) {
            sender.sendMessage(Text.colorize("&cPlayer must be online!"));
            return;
        }

        if (personalMultipliers.containsKey(onlinePlayer.getUniqueId())) {
            PlayerMultiplier multiplier = personalMultipliers.get(onlinePlayer.getUniqueId());
            multiplier.addMultiplier(amount, this.playerMultiMax);
            multiplier.addDuration(minutes);
            personalMultipliers.put(onlinePlayer.getUniqueId(), multiplier);
        } else {
            personalMultipliers.put(onlinePlayer.getUniqueId(), new PlayerMultiplier(onlinePlayer.getUniqueId(), amount, minutes));
        }

        onlinePlayer.sendMessage(messages.get("personal_multi_apply").replace("%multiplier%", String.valueOf(amount)).replace("%minutes%", String.valueOf(minutes)));
        sender.sendMessage(Text.colorize(String.format("&aYou have set &e%s's &ePersonal Multiplier &ato &e%.2f &afor &e%d &aminutes.", onlinePlayer.getName(), amount, minutes)));
    }


    private void setupGlobalMultiplier(CommandSender sender, int time, double amount) {

        GLOBAL_MULTIPLIER.addMultiplier(amount, this.globalMultiMax);
        GLOBAL_MULTIPLIER.addDuration(time);
        sender.sendMessage(Text.colorize(String.format("&aYou have set the &eGlobal Multiplier &ato &e%.2f &afor &e%d &aminutes.", amount, time)));
    }


    public double getGlobalMultiplier() {
        return GLOBAL_MULTIPLIER.getMultiplier();
    }

    public double getPersonalMultiplier(Player p) {
        return personalMultipliers.getOrDefault(p.getUniqueId(), Multiplier.getDefaultPlayerMultiplier(p.getUniqueId())).getMultiplier();
    }

    public double getRankMultiplier(Player p) {
        return rankMultipliers.getOrDefault(p.getUniqueId(), Multiplier.getDefaultPlayerMultiplier()).getMultiplier();
    }

    public void removePersonalMultiplier(UUID uuid) {
        personalMultipliers.remove(uuid);
    }


    private Multiplier calculateRankMultiplier(Player p) {
        PlayerMultiplier toReturn = new PlayerMultiplier(p.getUniqueId(), 0.0, -1);

        for (String perm : permissionToMultiplier.keySet()) {
            if (p.hasPermission(perm)) {
                toReturn.addMultiplier(permissionToMultiplier.get(perm), this.playerMultiMax);
                break;
            }
        }

        return toReturn;
    }


}
