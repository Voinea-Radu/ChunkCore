package dev.lightdream.ultraprisoncore.friends;

import dev.lightdream.ultraprisoncore.friends.dto.FriendsConfig;
import lombok.Getter;
import dev.lightdream.ultraprisoncore.UltraPrisonCore;
import dev.lightdream.ultraprisoncore.UltraPrisonModule;
import dev.lightdream.ultraprisoncore.friends.dto.FriendsGUIConfig;
import dev.lightdream.ultraprisoncore.friends.gui.FriendsGUI;
import me.lucko.helper.Commands;
import me.lucko.helper.text3.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class UltraPrisonFriends implements UltraPrisonModule {

    private static UltraPrisonFriends instance;

    private final UltraPrisonCore core;
    private boolean enabled;

    private FriendsGUIConfig friendsGUIConfig;
    private final FriendsConfig friendsConfig;

    private final FriendsManager friendsManager;

    public UltraPrisonFriends(UltraPrisonCore core) {
        this.core = core;
        instance = this;

        this.friendsGUIConfig = core.getPersist().load(FriendsGUIConfig.class);
        this.friendsConfig = core.getPersist().load(FriendsConfig.class);

        this.friendsManager = new FriendsManager(core, this);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void reload() {
        this.friendsGUIConfig = core.getPersist().load(FriendsGUIConfig.class);
    }

    @Override
    public void enable() {
        this.enabled = true;
        this.friendsGUIConfig = core.getPersist().load(FriendsGUIConfig.class);
        registerCommands();
    }

    @Override
    public void disable() {
        this.enabled = true;
    }

    @Override
    public String getName() {
        return "Friends";
    }

    private void registerCommands() {
        Commands.create()
                .assertPlayer()
                .handler(c -> {
                    Player player;
                    switch (c.args().size()) {
                        case 0:
                            c.sender().openInventory(new FriendsGUI(core, this, c.sender().getUniqueId(), 0).getInventory());
                            break;
                        case 2:
                            switch (c.rawArg(0)) {
                                case "add":
                                    player = Bukkit.getPlayer(c.rawArg(1));
                                    if (player == null) {
                                        c.sender().sendMessage(Text.colorize(core.getMessages().playerNotOnline));
                                        return;
                                    }
                                    friendsManager.addFriend(c.sender(), player.getUniqueId());
                                    break;
                                case "remove":
                                    player = Bukkit.getPlayer(c.rawArg(1));
                                    if (player == null) {
                                        c.sender().sendMessage(Text.colorize(core.getMessages().playerNotOnline));
                                        return;
                                    }
                                    friendsManager.removeFriend(c.sender(), player.getUniqueId());
                                    break;
                            }
                    }
                }).registerAndBind(core, "friends");

    }
}



/*
        GUI_TITLE = Text.colorize(UltraPrisonFriends.getInstance().getConfig().get().getString("friends_gui.title"));
        EMPTY_SLOT_ITEM = ItemStackBuilder.of(CompMaterial.fromString(UltraPrisonFriends.getInstance().getConfig().get().getString("friends_gui.empty_slots")).toItem()).buildItem().build();
        GUI_LINES = UltraPrisonEnchants.getInstance().getConfig().get().getInt("friends_gui.lines");
 */