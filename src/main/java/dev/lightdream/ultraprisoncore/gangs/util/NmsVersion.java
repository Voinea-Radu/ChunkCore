package dev.lightdream.ultraprisoncore.gangs.util;

import dev.lightdream.ultraprisoncore.gangs.exception.UnsupportedMinecraftVersionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NmsVersion {
    v1_7,
    v1_8,
    v1_9,
    v1_10,
    v1_11,
    v1_12,
    v1_13,
    v1_14,
    v1_15;

    private static final Pattern ONE_POINT_VERSION_NUMBER_PATTERN = Pattern.compile("v\\d_(\\d+)");

    NmsVersion() {
    }

    public int extractVersionNumber() {
        Matcher var1 = ONE_POINT_VERSION_NUMBER_PATTERN.matcher(this.name());
        var1.find();
        if (var1.groupCount() < 1) {
            throw new UnsupportedMinecraftVersionException();
        } else {
            return Integer.valueOf(var1.group(1));
        }
    }
}
