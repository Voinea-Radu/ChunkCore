package dev.lightdream.gangs.util;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class TitleUtils {
    private static Class<?> packetTitle = NmsUtils.getNmsClass("PacketPlayOutTitle");
    private static Class<?> packetActions = NmsUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
    private static Class<?> nmsChatSerializer = NmsUtils.getNmsClass("IChatBaseComponent$ChatSerializer");
    private static Class<?> chatBaseComponent = NmsUtils.getNmsClass("IChatBaseComponent");

    public TitleUtils() {
    }

    public static void sendTitle(Player var0, String var1, String var2, int var3, int var4, int var5) {
        try {
            Object var6 = NmsUtils.getHandle(var0);
            Object var7 = NmsUtils.getField(var6.getClass(), "playerConnection").get(var6);
            Method var8 = NmsUtils.getMethod(var7.getClass(), "sendPacket", new Class[0]);
            Object var9 = packetTitle.getConstructor(packetActions, chatBaseComponent, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(packetActions.getEnumConstants()[2], null, var3 * 20, var4 * 20, var5 * 20);
            var8.invoke(var7, var9);
            Object var10 = NmsUtils.getMethod(nmsChatSerializer, "a", new Class[]{String.class}).invoke((Object)null, "{\"text\": \" " + StringUtils.fixColors(var1) + " \"}");
            var9 = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(packetActions.getEnumConstants()[0], var10);
            var8.invoke(var7, var9);
            if (!var2.isEmpty()) {
                var10 = NmsUtils.getMethod(nmsChatSerializer, "a", new Class[]{String.class}).invoke((Object)null, "{\"text\": \" " + StringUtils.fixColors(var2) + " \"}");
                var9 = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(packetActions.getEnumConstants()[1], var10);
                var8.invoke(var7, var9);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

    }
}
