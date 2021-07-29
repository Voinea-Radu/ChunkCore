package dev.lightdream.ultraprisoncore.gangs;

import com.google.gson.Gson;
import dev.lightdream.ultraprisoncore.gangs.command.*;
import dev.lightdream.ultraprisoncore.gangs.config.Lang;
import dev.lightdream.ultraprisoncore.gangs.config.Settings;
import dev.lightdream.ultraprisoncore.gangs.core.BConfig;
import dev.lightdream.ultraprisoncore.gangs.database.DataManager;
import dev.lightdream.ultraprisoncore.gangs.database.MySqlDataManager;
import dev.lightdream.ultraprisoncore.gangs.database.SqliteDataManager;
import dev.lightdream.ultraprisoncore.gangs.dependency.CombatHandler;
import dev.lightdream.ultraprisoncore.gangs.dependency.MvdwPlaceholderHandler;
import dev.lightdream.ultraprisoncore.gangs.dependency.PlaceholderApiHandler;
import dev.lightdream.ultraprisoncore.gangs.fight.FightManager;
import dev.lightdream.ultraprisoncore.gangs.gang.GangManager;
import dev.lightdream.ultraprisoncore.gangs.player.PlayerManager;
import dev.lightdream.ultraprisoncore.gangs.task.SaveDataTask;
import lombok.Getter;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.gangs.listener.CrackShotListener;
import dev.lightdream.ultraprisoncore.gangs.listener.EntityListener;
import dev.lightdream.ultraprisoncore.gangs.listener.GangsListener;
import dev.lightdream.ultraprisoncore.gangs.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

@Getter
public class GangsPlugin implements UltraPrisonModule {
    @Getter
    private static GangsPlugin instance;
    private final UltraPrisonCore core;
    private BConfig configLang;
    private BConfig configMain;
    private DataManager dataManager;
    private FightManager fightManager;
    private GangManager gangManager;
    private PlayerManager playerManager;
    private CombatHandler combatHandler;
    @Getter
    private Economy economy;
    @Getter
    private Gson gson;
    @Getter
    private Logger logger;
    private int saveDataTaskId = -1;

    public GangsPlugin(UltraPrisonCore core) {
        this.core = core;
    }


    private void registerListeners() {
        core.getServer().getPluginManager().registerEvents(new EntityListener(this), core);
        core.getServer().getPluginManager().registerEvents(new GangsListener(this), core);
        core.getServer().getPluginManager().registerEvents(new PlayerListener(this), core);
    }

    private void registerCommands() {
        core.getCommand("allychat").setExecutor(new CmdAllyChat(this));
        core.getCommand("gang").setExecutor(new CmdGang(this));
        core.getCommand("fight").setExecutor(new CmdFight(this));
        core.getCommand("gangadmin").setExecutor(new CmdGangAdmin(this));
        core.getCommand("gangchat").setExecutor(new CmdGangChat(this));
    }


    public void load() {
        this.gangManager.loadGangs();
        this.fightManager.loadArenas();
        if (Settings.saveDataPeriodically) {
            this.saveDataTaskId = Bukkit.getScheduler().runTaskTimer(core, new SaveDataTask(this), (long) (Settings.dataSaveInterval * 60) * 20L, (long) (Settings.dataSaveInterval * 60) * 20L).getTaskId();
        }

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void reload() {
        this.configLang = new BConfig(core, "lang-gangs.yml");
        this.configMain = new BConfig(core, "config-gangs.yml");
        Lang.setConfig(this.configLang);
        Settings.setConfig(this.configMain);
    }

    @Override
    public void enable() {
        instance = this;
        this.logger = core.getLogger();
        this.gson = new Gson();
        if (this.setupCrackShot()) {
            this.logger.info("CrackShot support enabled!");
        }

        if (this.setupClipPlaceholders()) {
            this.logger.info("Clip's PlaceholderAPI support enabled!");
        }

        if (this.setupMVDWPlaceholders()) {
            this.logger.info("MVdWPlaceholderAPI support enabled!");
        }

        if (!this.setupEconomy()) {
            this.logger.severe("Plugin disabled due to no Vault/economy dependency found!");
            disable();
        } else {
            this.configLang = new BConfig(core, "lang-gangs.yml");
            this.configMain = new BConfig(core, "config-gangs.yml");
            Lang.setConfig(this.configLang);
            Settings.setConfig(this.configMain);
            this.fightManager = new FightManager(this);
            this.gangManager = new GangManager(this);
            this.playerManager = new PlayerManager(this);
            this.combatHandler = new CombatHandler(this);
            switch (Settings.databaseType) {
                case MYSQL:
                    this.dataManager = new MySqlDataManager(this);
                    break;
                case SQLITE:
                    this.dataManager = new SqliteDataManager(this);
            }

            this.dataManager.open();
            this.dataManager.setup();
            this.registerListeners();
            this.registerCommands();
        }

    }

    @Override
    public void disable() {
        if (this.dataManager != null) {
            this.dataManager.close();
        }

        if (Bukkit.getScheduler().isCurrentlyRunning(this.saveDataTaskId)) {
            Bukkit.getScheduler().cancelTask(this.saveDataTaskId);
        }
    }

    @Override
    public String getName() {
        return "Gangs";
    }

    public void logRaw(String var1) {
        System.out.println(var1);
    }

    private boolean setupCrackShot() {
        if (core.getServer().getPluginManager().getPlugin("CrackShot") == null) {
            return false;
        } else {
            core.getServer().getPluginManager().registerEvents(new CrackShotListener(this), core);
            return true;
        }
    }

    private boolean setupEconomy() {
        if (core.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider var1 = core.getServer().getServicesManager().getRegistration(Economy.class);
            if (var1 == null) {
                return false;
            } else {
                this.economy = (Economy) var1.getProvider();
                return this.economy != null;
            }
        }
    }

    private boolean setupClipPlaceholders() {
        return core.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null ? false : (new PlaceholderApiHandler(this)).register();
    }

    private boolean setupMVDWPlaceholders() {
        if (core.getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI") == null) {
            return false;
        } else {
            (new MvdwPlaceholderHandler(this)).registerPlaceholders();
            return true;
        }
    }
}
