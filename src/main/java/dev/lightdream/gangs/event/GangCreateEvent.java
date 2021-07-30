package dev.lightdream.gangs.event;

import dev.lightdream.gangs.gang.Gang;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangCreateEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Gang gang;
    private final Player player;
    private boolean cancelled;

    public GangCreateEvent(Gang var1, Player var2) {
        this.gang = var1;
        this.player = var2;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Gang getGang() {
        return this.gang;
    }

    public Player getPlayer() {
        return this.player;
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
