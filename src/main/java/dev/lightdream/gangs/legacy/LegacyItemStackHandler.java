package dev.lightdream.gangs.legacy;

import dev.lightdream.gangs.util.NmsUtils;
import dev.lightdream.gangs.util.NmsVersion;
import org.bukkit.Material;

public class LegacyItemStackHandler {
    public LegacyItemStackHandler() {
    }

    public static boolean isLava(Material var0) {
        if (NmsUtils.isNmsVersionAtLeast(NmsVersion.v1_13)) {
            return var0.name().equals("LAVA");
        } else {
            return var0.name().equals("LAVA") || var0.name().equals("STATIONARY_LAVA");
        }
    }

    public static boolean isWater(Material var0) {
        if (NmsUtils.isNmsVersionAtLeast(NmsVersion.v1_13)) {
            return var0.name().equals("WATER");
        } else {
            return var0.name().equals("WATER") || var0.name().equals("STATIONARY_WATER");
        }
    }
}
