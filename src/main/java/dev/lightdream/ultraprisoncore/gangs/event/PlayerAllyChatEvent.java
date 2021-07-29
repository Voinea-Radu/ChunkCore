package dev.lightdream.ultraprisoncore.gangs.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.awt.*;

public class PlayerAllyChatEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String message;
    private boolean cancelled;

    public PlayerAllyChatEvent(Player var1, String var2) {
        this.player = var1;
        this.message = var2;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String var1) {
        this.message = var1;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean var1) {
        this.cancelled = var1;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
