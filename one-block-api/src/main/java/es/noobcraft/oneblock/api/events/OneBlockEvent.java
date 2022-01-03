package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OneBlockEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter private final OneBlockPlayer player;

    public OneBlockEvent(OneBlockPlayer player) {
        this.player = player;
    }

    @NonNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NonNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
