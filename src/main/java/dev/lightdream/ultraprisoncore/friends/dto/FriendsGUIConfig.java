package dev.lightdream.ultraprisoncore.friends.dto;

import dev.lightdream.ultraprisoncore.utils.Item;
import dev.lightdream.ultraprisoncore.utils.XMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsGUIConfig {

    public String title = "Friends";
    public Item fillItem = new Item(XMaterial.BLUE_STAINED_GLASS_PANE, 1, "", new ArrayList<>());
    public Item playerItem = new Item(XMaterial.PLAYER_HEAD, 1, "%player%", "%player%", Arrays.asList(
            "Left Click to add this player as a friend",
            "Right Click to remove this player as a friend",
            "Middle click to mute/unmute this player"
    ));

    public String muteCommand = "ignore %player%";

    public Item backItem = new Item(XMaterial.ARROW, 45, 1, "Back", new ArrayList<>());
    public Item nextItem = new Item(XMaterial.ARROW, 53, 1, "Next", new ArrayList<>());
    public List<Integer> fillItemPositions = Arrays.asList(46, 47, 48, 50, 51, 52);

    public Item openOnlinePlayerGUI = new Item(XMaterial.BARRIER, 49, 1, "Go to Online Player GUI", new ArrayList<>());
    public Item openFriendsGUI = new Item(XMaterial.BARRIER, 49, 1, "Go to Friends GUI", new ArrayList<>());


}
