package dev.lightdream.gangs.util;

import dev.lightdream.gangs.exception.UnsupportedMinecraftVersionException;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NmsUtils {
    private static final Pattern MINECRAFT_SHORT_VERSION_PATTERN = Pattern.compile("(v\\d_\\d+)");
    private static final Pattern MINECRAFT_FULL_VERSION_PATTERN = Pattern.compile("(v\\d_\\d+_[a-zA-Z0-9]+)");

    public NmsUtils() {
    }

    public static boolean isNmsVersionAtLeast(NmsVersion var0) {
        NmsVersion var1 = readNmsVersion();
        return var1.extractVersionNumber() >= var0.extractVersionNumber();
    }

    public static Class<?> getNmsClass(String var0) {
        String var1 = "net.minecraft.server." + getNmsClasspath() + "." + var0;
        Class var2 = null;

        try {
            var2 = Class.forName(var1);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return var2;
    }

    public static Field getField(Class<?> var0, String var1) {
        try {
            Field var2 = var0.getDeclaredField(var1);
            var2.setAccessible(true);
            return var2;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> var0, String var1, Class<?>... var2) {
        Method[] var3 = var0.getMethods();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Method var6 = var3[var5];
            if (var6.getName().equals(var1) && (var2.length == 0 || Arrays.equals(var2, var6.getParameterTypes()))) {
                var6.setAccessible(true);
                return var6;
            }
        }

        return null;
    }

    public static Object getHandle(Object var0) {
        try {
            return getMethod(var0.getClass(), "getHandle").invoke(var0);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static String getNmsClasspath() {
        return extractNmsVersion(MINECRAFT_FULL_VERSION_PATTERN);
    }

    private static NmsVersion readNmsVersion() {
        return parseShortNmsVersion(extractNmsVersion(MINECRAFT_SHORT_VERSION_PATTERN));
    }

    private static String extractNmsVersion(Pattern var0) {
        String var1 = Bukkit.getServer().getClass().getPackage().getName();
        Matcher var2 = var0.matcher(var1);
        if (var2.find()) {
            return var2.group();
        } else {
            throw new UnsupportedMinecraftVersionException();
        }
    }

    private static NmsVersion parseShortNmsVersion(String var0) {
        try {
            NmsVersion var1 = NmsVersion.valueOf(var0);
            return var1;
        } catch (IllegalArgumentException var3) {
            throw new UnsupportedMinecraftVersionException();
        }
    }
}
