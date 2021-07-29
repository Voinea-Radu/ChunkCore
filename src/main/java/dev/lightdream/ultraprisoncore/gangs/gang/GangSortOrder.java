package dev.lightdream.ultraprisoncore.gangs.gang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum GangSortOrder {
    LIST(new String[]{"all"}),
    KILLS(new String[]{"kill", "mostkills", "mostkill"}),
    DEATHS(new String[]{"death", "mostdeaths", "mostdeath"}),
    KDR(new String[]{"kdratio", "kd"}),
    WON(new String[]{"win", "wins"}),
    LOST(new String[]{"loss", "losses"}),
    WLR(new String[]{"wlratio", "wl"}),
    LEVEL(new String[]{"lvl"}),
    MEMBERS(new String[]{"players"}),
    MEMBERS_ONLINE(new String[]{"online"}),
    BANK(new String[]{"bankmoney"}),
    MONEY(new String[]{"balance", "bal"});

    private List<String> names = new ArrayList();

    private GangSortOrder(String... var3) {
        this.names.addAll(Arrays.asList(var3));
    }

    public static GangSortOrder fromString(String var0) {
        GangSortOrder[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            GangSortOrder var4 = var1[var3];
            if (var4.name().replace("_", "").equalsIgnoreCase(var0.replace("_", ""))) {
                return var4;
            }

            Iterator var5 = var4.getNames().iterator();

            while(var5.hasNext()) {
                String var6 = (String)var5.next();
                if (var0.replace("_", "").equalsIgnoreCase(var6)) {
                    return var4;
                }
            }
        }

        return null;
    }

    private List<String> getNames() {
        return this.names;
    }
}
