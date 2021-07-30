package dev.lightdream.gangs.event;

import dev.lightdream.gangs.gang.Gang;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveGangEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Player kickedBy;
    private final Gang gang;
    private final PlayerLeaveGangEvent.LeaveReason leaveReason;
    private boolean cancelled;

    public PlayerLeaveGangEvent(Player var1, Player var2, Gang var3, PlayerLeaveGangEvent.LeaveReason var4) {
        this.player = var1;
        this.kickedBy = var2;
        this.gang = var3;
        this.leaveReason = var4;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Player getKickedBy() {
        return this.kickedBy;
    }

    public Gang getGang() {
        return this.gang;
    }

    public PlayerLeaveGangEvent.LeaveReason getLeaveReason() {
        return this.leaveReason;
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

    public enum LeaveReason {
        DISBANDED,
        DISBANDED_LEADER,
        KICKED,
        LEFT;

        LeaveReason() {
        }
    }
}
