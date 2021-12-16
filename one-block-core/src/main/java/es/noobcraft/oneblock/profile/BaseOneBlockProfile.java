package es.noobcraft.oneblock.profile;

import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;


@AllArgsConstructor
public class BaseOneBlockProfile implements OneBlockProfile {
    @Getter private final OneBlockPlayer owner;
    @Getter private final String profileName;
    @Getter private final String islandOwner;
    @Getter private final byte[] inventory;
    @Getter private final int islandSettings;
    @Getter private final Material profileItem;

    public BaseOneBlockProfile(OneBlockPlayer owner, String islandOwner, String profileName) {
        this.owner = owner;
        this.profileName = profileName;
        this.islandOwner = islandOwner;
        this.inventory = null;
        this.islandSettings = 0;
        this.profileItem = OneBlockConstants.DEF_PROFILE_MATERIAL;
    }

    public BaseOneBlockProfile(OneBlockPlayer owner, String profileName) {
        this(owner, owner.getName(), profileName);
    }
}
