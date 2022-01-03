package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.Getter;

public class OneBlockProfileDeleteEvent extends OneBlockEvent {
    @Getter private final OneBlockProfile profile;

    public OneBlockProfileDeleteEvent(OneBlockPlayer player, OneBlockProfile profile) {
        super(player);
        this.profile = profile;
    }
}
