package dev.lightdream.ultraprisoncore.utils;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class Utils {

    public static @NotNull ItemStack setNBT(@NotNull ItemStack item, @NotNull String attribute, @NotNull Object value) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.addCompound("settings");
        nbtCompound.setObject(attribute, value);
        return nbtItem.getItem();
    }

    public static @Nullable Object getNBT(@NotNull ItemStack item, @NotNull String attribute) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound nbtCompound = nbtItem.addCompound("settings");
        return nbtCompound.getObject(attribute, Object.class);
    }

    public static void fillInventory(@NotNull Inventory inventory, @NotNull ItemStack fillItem, @NotNull List<Integer> positions) {
        positions.forEach(pos -> inventory.setItem(pos, Utils.setNBT(fillItem, "gui_protect", true)));
    }

    public static void fillInventory(@NotNull Inventory inventory, @NotNull ItemStack fillItem) {
        for (int pos = 0; pos < inventory.getSize(); pos++) {
            inventory.setItem(pos, Utils.setNBT(fillItem, "gui_protect", true));
        }
    }

    public static @NotNull List<String> color(@NotNull List<String> list) {
        List<String> output = new ArrayList<>();
        list.forEach(line -> output.add(color(line)));
        return output;
    }

    public static @NotNull String color(@NotNull String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static @NotNull String[] playerInventoryToBase64(@NotNull PlayerInventory playerInventory) throws IllegalStateException {
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(Arrays.asList(playerInventory.getArmorContents()));

        return new String[]{content, armor};
    }

    public static @NotNull String itemStackArrayToBase64(@NotNull List<ItemStack> items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.size());

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static @NotNull String toBase64(@NotNull Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static @NotNull Inventory fromBase64(@NotNull String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static @NotNull List<ItemStack> itemStackArrayFromBase64(@NotNull String data) throws IOException {
        if (data.equals("")) {
            return new ArrayList<>();
        }
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            List<ItemStack> items = new ArrayList<>();
            int size = dataInput.readInt();

            for (int i = 0; i < size; i++) {
                items.add((ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static boolean checkExecute(double chance) {
        double result = Math.random() * 101 + 0;
        return result < chance;
    }

    public static int generateRandom(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static double generateRandom(double a, double b) {
        if (b < a) {
            return Math.random() * (a - b + 1) + b;
        }
        return Math.random() * (b - a + 1) + a;
    }

    public static void spawnFireworks(@NotNull Location location, int amount, @NotNull Color color, boolean flicker) {
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(flicker).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}
