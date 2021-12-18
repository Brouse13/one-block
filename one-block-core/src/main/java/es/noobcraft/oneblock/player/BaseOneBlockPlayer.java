package es.noobcraft.oneblock.player;

import com.google.common.collect.ImmutableSet;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
public class BaseOneBlockPlayer implements OneBlockPlayer {
    @Getter private final String name;
    @Setter private Set<OneBlockProfile> profiles = null;
    @Getter private final int maxProfiles;
    @Getter private final long lastPlayed;

    public BaseOneBlockPlayer(String name) {
        this.name = name;
        this.maxProfiles = OneBlockConstants.DEF_PROFILES;
        this.lastPlayed = System.currentTimeMillis();
    }

    @Override
    public Set<OneBlockProfile> getProfiles() {
        return ImmutableSet.copyOf(profiles);
    }

    @Override
    public boolean addProfile(OneBlockProfile profile) {
        if (profiles.size() == maxProfiles)
            return false;
        return profiles.add(profile);
    }

    @Override
    public boolean removeProfile(OneBlockProfile profile) {
       return profiles.remove(profile);
    }
}
