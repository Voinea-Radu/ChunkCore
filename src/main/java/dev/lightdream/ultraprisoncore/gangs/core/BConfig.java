package dev.lightdream.ultraprisoncore.gangs.core;

import dev.lightdream.ultraprisoncore.gangs.util.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BConfig {
    private final JavaPlugin main;
    private final String name;
    private FileConfiguration fileConfiguration;
    private File file;

    public BConfig(JavaPlugin var1, String var2) {
        this.main = var1;
        this.name = var2;
        this.load();
    }

    private void load() {
        if (!this.main.getDataFolder().exists()) {
            FileUtils.mkdir(this.main.getDataFolder());
        }

        File var1 = new File(this.main.getDataFolder(), this.name);
        if (!var1.exists()) {
            FileUtils.copy(this.main.getResource(this.name), var1);
        }

        this.file = var1;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(var1);
        this.fileConfiguration.options().copyDefaults(true);
    }

    public void save() {
        try {
            this.fileConfiguration.options().copyDefaults(true);
            this.fileConfiguration.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }
}
