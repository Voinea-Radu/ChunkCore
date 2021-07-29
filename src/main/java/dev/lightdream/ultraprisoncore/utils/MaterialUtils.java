package dev.lightdream.ultraprisoncore.utils;

import org.bukkit.Material;

public class MaterialUtils {

	public static Material getSmeltedForm(Material m) {
		switch (m) {
			case STONE:
				return Material.COBBLESTONE;
			case COAL_ORE:
				return Material.COAL;
			case DIAMOND_ORE:
				return Material.DIAMOND;
			case EMERALD_ORE:
				return Material.EMERALD;
			case GLOWING_REDSTONE_ORE:
			case REDSTONE_ORE:
				return Material.REDSTONE;
			case GOLD_ORE:
				return Material.GOLD_INGOT;
			case IRON_ORE:
				return Material.IRON_INGOT;
			case QUARTZ_ORE:
				return Material.QUARTZ;
			default:
				return m;
		}
	}
}
