package dev.lightdream.pickaxelevels.api;

import dev.lightdream.pickaxelevels.model.PickaxeLevel;
import dev.lightdream.pickaxelevels.UltraPrisonPickaxeLevels;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UltraPrisonPickaxeLevelsAPIImpl implements UltraPrisonPickaxeLevelsAPI {

	private UltraPrisonPickaxeLevels plugin;

	public UltraPrisonPickaxeLevelsAPIImpl(UltraPrisonPickaxeLevels plugin) {
		this.plugin = plugin;
	}

	@Override
	public PickaxeLevel getPickaxeLevel(ItemStack item) {
		return this.plugin.getPickaxeLevel(item);
	}

	@Override
	public PickaxeLevel getPickaxeLevel(Player player) {
		ItemStack item = this.plugin.findPickaxe(player);
		return this.getPickaxeLevel(item);
	}
}
