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

public class MultipliersHelpGui extends Gui {

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


	public MultipliersHelpGui(Player player) {
		super(player, 5, "Multipliers Help");
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
					"&7Multipliers allows your players",
					"&7to earn more money when they",
					"&7are mining."
			).buildItem().build());

			//Commands
			this.setItem(22, ItemStackBuilder.of(SkullUtils.COMMAND_BLOCK_SKULL.clone()).name("&eCommands").lore(
					"&f/gmulti [multiplier] [time in minutes]",
					"&7Example: /gmulti 1.0 10",
					"&7Set the global multiplier for all players on server.",
					" ",
					"&f/pmulti [player] [multiplier] [time in minutes]",
					"&7Example: /pmulti Drawethree 1.0 10",
					"&7Set the personal multiplier for player.",
					" ",
					"&f/multi",
					"&7Check your active multipliers."
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
