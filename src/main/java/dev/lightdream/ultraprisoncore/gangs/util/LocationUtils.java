package dev.lightdream.ultraprisoncore.gangs.util;

import dev.lightdream.ultraprisoncore.gangs.legacy.LegacyItemStackHandler;
import dev.lightdream.ultraprisoncore.gangs.legacy.location.InvalidLocationException;
import dev.lightdream.ultraprisoncore.gangs.legacy.location.InvalidLocationWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class LocationUtils {
    public LocationUtils() {
    }

    public static boolean isPassable(Material var0) {
        LocationUtils.Passable[] var1 = LocationUtils.Passable.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            LocationUtils.Passable var4 = var1[var3];
            if (var4.name().equals(var0.name())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSafe(Location var0) {
        int var1 = var0.getBlockX();
        int var2 = var0.getBlockY();
        int var3 = var0.getBlockZ();
        if (isPassable(var0.getWorld().getBlockAt(var1, var2, var3).getType()) && isPassable(var0.getWorld().getBlockAt(var1, var2 + 1, var3).getType())) {
            while (true) {
                if (var2 > 0) {
                    --var2;
                    Material var4 = var0.getWorld().getBlockAt(var1, var2, var3).getType();
                    if (!var4.isSolid()) {
                        if (!LegacyItemStackHandler.isLava(var4) && !LegacyItemStackHandler.isWater(var4) && !var4.equals(Material.FIRE)) {
                            continue;
                        }

                        return false;
                    }
                }

                return var0.getBlockY() - var2 <= 3 && var2 != 0;
            }
        } else {
            return false;
        }
    }

    private static boolean isBetween(int var0, int var1, int var2) {
        return var0 <= var1 && var1 <= var2 || var0 >= var1 && var1 >= var2;
    }

    public static boolean isLocationInside(Location var0, Location var1, Location var2) {
        return isBetween(var0.getBlockX(), var2.getBlockX(), var1.getBlockX()) && isBetween(var0.getBlockY(), var2.getBlockY(), var1.getBlockY()) && isBetween(var0.getBlockZ(), var2.getBlockZ(), var1.getBlockZ());
    }

    public static Location deserialize(String var0) {
        if (var0 == null) {
            throw new InvalidLocationException();
        } else {
            String[] var1 = var0.split(":");
            if (var1.length < 6) {
                throw new InvalidLocationException();
            } else {
                World var2 = Bukkit.getWorld(var1[0]);
                if (var2 == null) {
                    throw new InvalidLocationWorldException();
                } else {
                    return new Location(var2, Double.valueOf(var1[1]), Double.valueOf(var1[2]), Double.valueOf(var1[3]), Float.valueOf(var1[4]), Float.valueOf(var1[5]));
                }
            }
        }
    }

    public static String serialize(Location var0) {
        return var0 == null ? "" : var0.getWorld().getName() + ":" + var0.getX() + ":" + var0.getY() + ":" + var0.getZ() + ":" + var0.getYaw() + ":" + var0.getPitch();
    }

    private enum Passable {
        AIR,
        BROWN_MUSHROOM,
        CARPET,
        LONG_GRASS,
        LADDER,
        RED_MUSHROOM,
        YELLOW_FLOWER,
        RED_ROSE,
        DOUBLE_PLANT,
        ROSE,
        POWERED_RAIL,
        DETECTOR_RAIL,
        RAILS,
        ACTIVATOR_RAIL,
        WOOD_BUTTON,
        STONE_BUTTON,
        VINE,
        LEVER,
        BANNER,
        ARMOR_STAND,
        SNOW,
        REDSTONE_TORCH_OFF,
        REDSTONE_TORCH_ON,
        TORCH,
        WALL_SIGN,
        SIGN,
        BUTTON;

        Passable() {
        }
    }
}
