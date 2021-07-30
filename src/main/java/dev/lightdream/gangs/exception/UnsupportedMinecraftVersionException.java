package dev.lightdream.gangs.exception;

public class UnsupportedMinecraftVersionException extends RuntimeException {
    public UnsupportedMinecraftVersionException() {
        super("This Minecraft version is unsupported by the plugin. Try downgrading Minecraft or upgrading the plugin.");
    }
}
