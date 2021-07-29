package dev.lightdream.ultraprisoncore.tokens;


import dev.lightdream.ultraprisoncore.enchants.enchants.implementations.LuckyBoosterEnchant;
import dev.lightdream.ultraprisoncore.tokens.commands.TokensCommand;
import dev.lightdream.ultraprisoncore.tokens.managers.TokensManager;
import lombok.Getter;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.api.enums.ReceiveCause;
import dev.lightdream.ultraprisoncore.config.FileManager;
import dev.lightdream.ultraprisoncore.tokens.api.UltraPrisonTokensAPI;
import dev.lightdream.ultraprisoncore.tokens.api.UltraPrisonTokensAPIImpl;
import dev.lightdream.ultraprisoncore.utils.compat.CompMaterial;
import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.event.filter.EventFilters;
import me.lucko.helper.text.Text;
import me.lucko.helper.utils.Players;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.codemc.worldguardwrapper.WorldGuardWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class UltraPrisonTokens implements UltraPrisonModule {

	public static final String TOKENS_ADMIN_PERM = "ultraprison.tokens.admin";

	@Getter
	private static UltraPrisonTokens instance;

	@Getter
	private FileManager.Config config;

	@Getter
	private FileManager.Config blockRewardsConfig;

	@Getter
	private UltraPrisonTokensAPI api;

	@Getter
	private TokensManager tokensManager;
	@Getter
	private UltraPrisonCore core;

	private HashMap<String, String> messages;
	private Map<Material, Long> luckyBlockRewards;

	private double chance;
	private long minAmount;
	private long maxAmount;
	private boolean enabled;


	public UltraPrisonTokens(UltraPrisonCore prisonCore) {
		instance = this;
		this.core = prisonCore;
	}


	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void reload() {

		this.config.reload();
		this.blockRewardsConfig.reload();

		this.loadMessages();
		this.loadVariables();
		this.tokensManager.reloadConfig();
	}

	private void loadVariables() {
		this.chance = getConfig().get().getDouble("tokens.breaking.chance");
		this.minAmount = getConfig().get().getLong("tokens.breaking.min");
		this.maxAmount = getConfig().get().getLong("tokens.breaking.max");
		this.luckyBlockRewards = new HashMap<>();

		for (String key : this.getConfig().get().getConfigurationSection("lucky-blocks").getKeys(false)) {
			CompMaterial material = CompMaterial.fromString(key);
			long reward = this.getConfig().get().getLong("lucky-blocks." + key);
			if (reward <= 0) {
				continue;
			}
			this.luckyBlockRewards.put(material.toMaterial(), reward);
			this.getCore().debug("Loaded LuckyBlock: " + material.toMaterial().name() + ": " + reward);
		}
	}


	@Override
	public void enable() {

		this.enabled = true;
		this.config = this.core.getFileManager().getConfig("tokens.yml").copyDefaults(true).save();
		this.blockRewardsConfig = this.core.getFileManager().getConfig("block-rewards.yml").copyDefaults(true).save();

		this.loadMessages();
		this.loadVariables();

		this.tokensManager = new TokensManager(this);
		this.api = new UltraPrisonTokensAPIImpl(this.tokensManager);

		this.registerCommands();
		this.registerEvents();
	}


	@Override
	public void disable() {
		this.tokensManager.stopUpdating();
		this.tokensManager.saveWeeklyReset();
		this.tokensManager.savePlayerDataOnDisable();
		this.enabled = false;

	}

	@Override
	public String getName() {
		return "Tokens";
	}

	private void registerEvents() {

		Events.subscribe(PlayerInteractEvent.class, EventPriority.LOWEST)
				.filter(e -> e.getItem() != null && e.getItem().getType() == this.tokensManager.getTokenItemMaterial() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR))
				.handler(e -> {
					if (e.getItem().hasItemMeta()) {
						e.setCancelled(true);
						e.setUseInteractedBlock(Event.Result.DENY);
						this.tokensManager.redeemTokens(e.getPlayer(), e.getItem(), e.getPlayer().isSneaking());
					}
				})
				.bindWith(core);

		Events.subscribe(BlockBreakEvent.class)
				.filter(EventFilters.ignoreCancelled())
				.filter(e -> WorldGuardWrapper.getInstance().getRegions(e.getBlock().getLocation()).stream().anyMatch(region -> region.getId().toLowerCase().startsWith("mine")))
				.filter(e -> e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getPlayer().getItemInHand() != null && this.getCore().isPickaxeSupported(e.getPlayer().getItemInHand().getType()))
				.handler(e -> {
					this.handleBlockBreak(e.getPlayer(), Collections.singletonList(e.getBlock()));
				}).bindWith(core);
	}

	public void handleBlockBreak(Player p, List<Block> blocks) {
		tokensManager.addBlocksBroken(null, p, blocks.size());

		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);

			boolean luckyBooster = LuckyBoosterEnchant.hasLuckyBoosterRunning(p.getPlayer());

			//Lucky block check
			if (this.luckyBlockRewards.containsKey(block.getType())) {
				long reward = this.luckyBlockRewards.get(block.getType());
				reward = luckyBooster ? reward * 2 : reward;
				tokensManager.giveTokens(p, reward, null, ReceiveCause.LUCKY_BLOCK);
			}

			double random = ThreadLocalRandom.current().nextDouble(100);

			if (this.chance >= random) {

				long randAmount = ThreadLocalRandom.current().nextLong(minAmount, maxAmount);
				randAmount = luckyBooster ? randAmount * 2 : randAmount;

				tokensManager.giveTokens(p, randAmount, null, ReceiveCause.MINING);
			}
		}
	}

	private void registerCommands() {
		Commands.create()
				.handler(c -> {
					if (c.args().size() == 0 && c.sender() instanceof Player) {
						this.tokensManager.sendInfoMessage(c.sender(), (OfflinePlayer) c.sender(), true);
						return;
					}
					TokensCommand subCommand = TokensCommand.getCommand(c.rawArg(0));
					if (subCommand != null) {
						subCommand.execute(c.sender(), c.args().subList(1, c.args().size()));
					} else {
						OfflinePlayer target = Players.getOfflineNullable(c.rawArg(0));
						this.tokensManager.sendInfoMessage(c.sender(), target, true);
					}
				}).registerAndBind(core, "tokens", "token");

		Commands.create()
				.assertPlayer()
				.handler(c -> {
					this.tokensManager.toggleTokenMessage(c.sender());
				}).registerAndBind(core, "tokenmessage");

		Commands.create()
				.handler(c -> {
					if (c.args().size() == 0) {
						this.tokensManager.sendBlocksTop(c.sender());
					}
				})
				.registerAndBind(core, "blockstop", "blocktop");
		Commands.create()
				.handler(c -> {
					if (c.args().size() == 0) {
						this.tokensManager.sendBlocksTopWeekly(c.sender());
					}
				})
				.registerAndBind(core, "blockstopweekly", "blockstopw", "btw");
		Commands.create()
				.assertPermission("ultraprison.tokens.admin")
				.handler(c -> {
					if (c.args().size() == 0) {
						this.tokensManager.resetBlocksTopWeekly(c.sender());
					}
				})
				.registerAndBind(core, "blockstopweeklyreset");
		Commands.create()
				.handler(c -> {
					if (c.args().size() == 0) {
						this.tokensManager.sendTokensTop(c.sender());
					}
				})
				.registerAndBind(core, "tokenstop", "tokentop");
		Commands.create()
				.handler(c -> {
					if (c.args().size() == 0) {
						this.tokensManager.sendInfoMessage(c.sender(), (OfflinePlayer) c.sender(), false);
					} else if (c.args().size() == 1) {
						OfflinePlayer target = Players.getOfflineNullable(c.rawArg(0));
						this.tokensManager.sendInfoMessage(c.sender(), target, false);
					}
				})
				.registerAndBind(core, "blocks", "block");
		Commands.create()
				.assertPermission("ultraprison.tokens.admin")
				.handler(c -> {
					if (c.args().size() == 3) {

						OfflinePlayer target = c.arg(1).parseOrFail(OfflinePlayer.class);
						long amount = c.arg(2).parseOrFail(Long.class);

						switch (c.rawArg(0).toLowerCase()) {
							case "add":
								this.tokensManager.addBlocksBroken(c.sender(), target, amount);
								break;
							case "remove":
								this.tokensManager.removeBlocksBroken(c.sender(), target, amount);
								break;
							case "set":
								this.tokensManager.setBlocksBroken(c.sender(), target, amount);
								break;
							default:
								c.sender().sendMessage(Text.colorize("&c/blocksadmin <add/set/remove> <player> <amount>"));
								break;
						}
					} else {
						c.sender().sendMessage(Text.colorize("&c/blocksadmin <add/set/remove> <player> <amount>"));
					}
				})
				.registerAndBind(core, "blocksadmin", "blocksa");
	}

	private void loadMessages() {
		messages = new HashMap<>();
		for (String key : this.getConfig().get().getConfigurationSection("messages").getKeys(false)) {
			messages.put(key, Text.colorize(this.getConfig().get().getString("messages." + key)));
		}
	}

	public String getMessage(String key) {
		return messages.get(key);
	}
}
