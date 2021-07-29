package dev.lightdream.ultraprisoncore.friends.dto;

import java.util.HashMap;

public class FriendsConfig {

    public HashMap<String, Integer> permissionMap = new HashMap<String, Integer>(){{
        put("friends.200", 200);
        put("friends.100", 100);
    }};

}
