package dev.lightdream.ultraprisoncore.gangs.util;

public class NumberUtils {
    public NumberUtils() {
    }

    public static boolean isPositiveInteger(String var0) {
        int var1;
        try {
            var1 = Integer.valueOf(var0);
        } catch (NumberFormatException var3) {
            return false;
        }

        return var1 > 0;
    }

    public static boolean isPositiveDouble(String var0) {
        double var1;
        try {
            var1 = Double.valueOf(var0);
        } catch (NumberFormatException var4) {
            return false;
        }

        return var1 > 0.0D;
    }
}
