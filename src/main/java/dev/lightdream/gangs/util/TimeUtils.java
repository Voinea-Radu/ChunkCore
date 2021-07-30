package dev.lightdream.gangs.util;

import dev.lightdream.gangs.config.Lang;

public class TimeUtils {
    public static final long SEC_IN_MINUTE = 60L;
    public static final long SEC_IN_HOUR = 3600L;
    public static final long SEC_IN_DAY = 86400L;
    public static final long SEC_IN_WEEK = 604800L;
    public static final long SEC_IN_MONTH = 2592000L;
    public static final long SEC_IN_YEAR = 31536000L;

    public TimeUtils() {
    }

    public static String getTimeString(long var0) {
        String var2 = "";
        long var3 = var0 / 31536000L;
        if (var3 > 1L) {
            var2 = var2 + var3 + " " + Lang.TIME_YEARS_PLURAL + " ";
            var0 -= var3 * 31536000L;
        } else if (var3 == 1L) {
            var2 = var2 + var3 + " " + Lang.TIME_YEARS_SINGULAR + " ";
            var0 -= var3 * 31536000L;
        }

        long var5 = var0 / 2592000L;
        if (var5 > 1L) {
            var2 = var2 + var5 + " " + Lang.TIME_MONTHS_PLURAL + " ";
            var0 -= var5 * 2592000L;
        } else if (var5 == 1L) {
            var2 = var2 + var5 + " " + Lang.TIME_MONTHS_SINGULAR + " ";
            var0 -= var5 * 2592000L;
        }

        long var7 = var0 / 604800L;
        if (var7 > 1L) {
            var2 = var2 + var7 + " " + Lang.TIME_WEEKS_PLURAL + " ";
            var0 -= var7 * 604800L;
        } else if (var7 == 1L) {
            var2 = var2 + var7 + " " + Lang.TIME_WEEKS_SINGULAR + " ";
            var0 -= var7 * 604800L;
        }

        long var9 = var0 / 86400L;
        if (var9 > 1L) {
            var2 = var2 + var9 + " " + Lang.TIME_DAYS_PLURAL + " ";
            var0 -= var9 * 86400L;
        } else if (var9 == 1L) {
            var2 = var2 + var9 + " " + Lang.TIME_DAYS_SINGULAR + " ";
            var0 -= var9 * 86400L;
        }

        long var11 = var0 / 3600L;
        if (var11 > 1L) {
            var2 = var2 + var11 + " " + Lang.TIME_HOURS_PLURAL + " ";
            var0 -= var11 * 3600L;
        } else if (var11 == 1L) {
            var2 = var2 + var11 + " " + Lang.TIME_HOURS_SINGULAR + " ";
            var0 -= var11 * 3600L;
        }

        long var13 = var0 / 60L;
        if (var13 > 1L) {
            var2 = var2 + var13 + " " + Lang.TIME_MINUTES_PLURAL + " ";
            var0 -= var13 * 60L;
        } else if (var13 == 1L) {
            var2 = var2 + var13 + " " + Lang.TIME_MINUTES_SINGULAR + " ";
            var0 -= var13 * 60L;
        }

        if (var0 > 1L) {
            var2 = var2 + var0 + " " + Lang.TIME_SECONDS_PLURAL + " ";
        } else if (var0 == 1L) {
            var2 = var2 + var0 + " " + Lang.TIME_SECONDS_SINGULAR + " ";
        }

        var2 = var2.trim();
        if (var2.isEmpty()) {
            var2 = Lang.TIME_NOW.toString();
        }

        return var2;
    }
}
