package dev.lightdream.help;

import dev.lightdream.utils.SkullUtils;
import dev.lightdream.utils.compat.CompMaterial;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.scheme.MenuPopulator;
import me.lucko.helper.menu.scheme.MenuScheme;
import me.lucko.helper.text3.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AutoSellHelpGui extends Gui {

	protected static final MenuScheme LAYOUT_WHITE = new MenuScheme()
			.mask("011111110")
			.mask("110000011")
			.mask("100000001")
			.mask("110000011")
			.mask("011111110");

	protected static final MenuScheme LAYOUT_RED = new MenuScheme()
			.mask("100000001")
			.mask("000000000")
			.mask("000000000")
			.mask("000000000")
			.mask("100000001");


	public AutoSellHelpGui(Player player) {
		super(player, 5, "AutoSell Help");
	}

	@Override
	public void redraw() {

		if (isFirstDraw()) {

			MenuPopulator populator = LAYOUT_WHITE.newPopulator(this);

			while (populator.hasSpace()) {
				populator.accept(ItemStackBuilder.of(CompMaterial.WHITE_STAINED_GLASS_PANE.toItem()).name(" ").buildItem().build());
			}

			populator = LAYOUT_RED.newPopulator(this);

			while (populator.hasSpace()) {
				populator.accept(ItemStackBuilder.of(CompMaterial.RED_STAINED_GLASS_PANE.toItem()).name(" ").buildItem().build());
			}

			//Info
			this.setItem(13, ItemStackBuilder.of(SkullUtils.INFO_SKULL.clone()).name("&eWhat it is ?").lore(
					"&7AutoSell lets your players",
					"&7automatically sell any block",
					"&7they mine in your set",
					"&7mine regions."
			).buildItem().build());

			//Commands
			this.setItem(22, ItemStackBuilder.of(SkullUtils.COMMAND_BLOCK_SKULL.clone()).name("&eCommands").lore(
					"&f/sellprice [price]",
					"&7Set the price for any item",
					"&7you are holding. Make sure to",
					"&7stand in the correct region that",
					"&7you want autosell and sellall to work in.",
					" ",
					"&f/autosell",
					"&7Toggle your autosell mode.",
					"&7If enabled, blocks mined will",
					"&7be sold automatically.",
					"&7If disabled, blocks mined will",
					"&7go to your inventory.",
					" ",
					"&f/sellall",
					"&7Instantly sell every item",
					"&7that is in your inventory.",
					"&7You need to be standing in",
					"&7mine region."
			).buildItem().build());

			//Back
			this.setItem(36, ItemStackBuilder.of(Material.BARRIER).name("&c&lBack").lore("&7Back to main gui.").build(() -> {
				this.close();
				new HelpGui(this.getPlayer()).open();
			}));

			this.setItem(44, ItemStackBuilder.of(SkullUtils.HELP_SKULL.clone()).name("&e&lNeed more help?").lore("&7Right-Click to see plugin's Wiki", "&7Left-Click to join Discord Support.")
					.build(() -> {
						this.close();
						this.getPlayer().sendMessage(" ");
						this.getPlayer().sendMessage(Text.colorize("&eUltraPrisonCore - Wiki"));
						this.getPlayer().sendMessage(Text.colorize("&7https://github.com/Drawethree/UltraPrisonCore/wiki"));
						this.getPlayer().sendMessage(" ");
					}, () -> {
						this.close();
						this.getPlayer().sendMessage(" ");
						this.getPlayer().sendMessage(Text.colorize("&eUltraPrisonCore - Discord"));
						this.getPlayer().sendMessage(Text.colorize("&7https://discord.com/invite/cssWTNK"));
						this.getPlayer().sendMessage(" ");
					}));

		}
	}
}
