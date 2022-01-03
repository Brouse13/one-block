package es.noobcraft.oneblock.api.events;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.Getter;

public class OneBlockProfileCreateEvent extends OneBlockEvent {
    @Getter private final OneBlockProfile profile;

    public OneBlockProfileCreateEvent(OneBlockPlayer player, OneBlockProfile profile) {
        super(player);
        this.profile = profile;
    }
}
