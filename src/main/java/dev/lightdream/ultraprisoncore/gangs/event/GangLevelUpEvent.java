package dev.lightdream.ultraprisoncore.gangs.event;

import dev.lightdream.ultraprisoncore.gangs.gang.Gang;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GangLevelUpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Gang gang;
    private final int previousLevel;
    private final int currentLevel;
    private boolean cancelled;

    public GangLevelUpEvent(Gang var1, int var2, int var3) {
        this.gang = var1;
        this.previousLevel = var2;
        this.currentLevel = var3;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Gang getGang() {
        return this.gang;
    }

    public int getPreviousLevel() {
        return this.previousLevel;
    }

    public int getCurrentLevel() {
        return this.currentLevel;
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
