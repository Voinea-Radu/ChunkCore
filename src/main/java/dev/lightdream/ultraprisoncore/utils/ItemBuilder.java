package dev.lightdream.ultraprisoncore.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ItemBuilder {

    private ItemStack stack;

    public ItemBuilder(@NotNull Material mat) {
        stack = new ItemStack(mat);
    }

    public ItemBuilder(@NotNull Material mat, short sh) {
        stack = new ItemStack(mat, 1, sh);
    }

    public ItemBuilder(@NotNull ItemStack stack) {
        this.stack = stack;
    }

    public ItemMeta getItemMeta() {
        return stack.getItemMeta();
    }

    public ItemBuilder setItemMeta(@NotNull ItemMeta meta) {
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setColor(@NotNull Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            addEnchant(Enchantment.KNOCKBACK, 1);
            addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            ItemMeta meta = getItemMeta();
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                meta.removeEnchant(enchantment);
            }
        }
        return this;
    }

    public ItemBuilder setBannerColor(@NotNull DyeColor color) {
        BannerMeta meta = (BannerMeta) stack.getItemMeta();
        meta.setBaseColor(color);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setHead(@NotNull String owner) {
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDisplayName(@NotNull String displayname) {
        ItemMeta meta = getItemMeta();
        if (meta == null) {
            return this;
        }
        meta.setDisplayName(displayname);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setItemStack(@NotNull ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public ItemBuilder setLore(@NotNull ArrayList<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(@NotNull List<String> lore) {
        ItemMeta meta = getItemMeta();
        if (lore.size() == 0) {
            return this;
        }
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(@NotNull String lore) {
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(lore);
        ItemMeta meta = getItemMeta();
        meta.setLore(loreList);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(@NotNull String[] lore) {
        ArrayList<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, lore);
        ItemMeta meta = getItemMeta();
        meta.setLore(loreList);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level) {
        ItemMeta meta = getItemMeta();
        meta.addEnchant(enchantment, level, true);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlag(@NotNull ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addNBTString(@NotNull String attribute, @NotNull String value) {
        stack = Utils.setNBT(stack, attribute, value);
        return this;
    }

    public ItemBuilder addNBTInt(@NotNull String attribute, int value) {
        stack = Utils.setNBT(stack, attribute, value);
        return this;
    }

    public ItemBuilder addNBTStringList(@NotNull String attribute, @NotNull List<String> value) {
        stack = Utils.setNBT(stack, attribute, value);
        return this;
    }

    public ItemBuilder setHeadTexture(@NotNull String value) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        this.stack = head;
        return this;
    }

    public ItemStack build() {
        return stack;
    }

}
