package dev.lightdream.pickaxelevels.api;

import dev.lightdream.pickaxelevels.model.PickaxeLevel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface UltraPrisonPickaxeLevelsAPI {

	/**
	 * Method to get PickaxeLevel of itemstack
	 *
	 * @param item ItemStack
	 * @return PickaxeLevel.class
	 */
	PickaxeLevel getPickaxeLevel(ItemStack item);

	/**
	 * Method to get PickaxeLevel of Player
	 *
	 * @param player Player
	 * @return PickaxeLevel.class
	 */
	PickaxeLevel getPickaxeLevel(Player player);
}
