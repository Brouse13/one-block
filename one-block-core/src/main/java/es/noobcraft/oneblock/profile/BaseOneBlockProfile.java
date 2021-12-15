package es.noobcraft.oneblock.profile;

import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.OneBlockProfileSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Set;

@AllArgsConstructor
public class BaseOneBlockProfile implements OneBlockProfile {
    @Getter private final OneBlockPlayer owner;
    @Getter private final String profileName;
    @Getter private final byte[] inventory;
    @Getter private final Set<OneBlockProfileSettings> islandSettings;
    @Getter private final Material profileItem;

    public BaseOneBlockProfile(OneBlockPlayer owner, String profileName) {
        this.owner = owner;
        this.profileName = profileName;
        this.inventory = null;
        this.islandSettings = Sets.newHashSet();
        this.profileItem = OneBlockConstants.DEF_PROFILE_MATERIAL;
    }
}
