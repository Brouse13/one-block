package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.Getter;

public class OneBlockCoopRemoveEvent extends OneBlockEvent {
    @Getter private final OneBlockPlayer coop;
    @Getter private final String ownerName;

    public OneBlockCoopRemoveEvent(OneBlockPlayer player, OneBlockPlayer coop, String ownerName) {
        super(player);
        this.coop = coop;
        this.ownerName = ownerName;
    }
}
