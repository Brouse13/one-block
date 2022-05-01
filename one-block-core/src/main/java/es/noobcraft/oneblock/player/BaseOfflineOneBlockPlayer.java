package es.noobcraft.oneblock.player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.player.OfflineOneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.Getter;

import java.util.Set;

public class BaseOfflineOneBlockPlayer implements OfflineOneBlockPlayer {
    @Getter private final String name;
    @Getter private final int maxProfiles;
    private final Set<OneBlockProfile> profiles = Sets.newHashSet();

    public BaseOfflineOneBlockPlayer(String name, int maxProfiles) {
        this.name = name;
        this.maxProfiles = maxProfiles;
    }

    @Override
    public Set<OneBlockProfile> getProfiles() {
        return ImmutableSet.copyOf(profiles);
    }

    @Override
    public void addProfile(OneBlockProfile profile) {
        if (profiles.size() < maxProfiles)
            profiles.add(profile);
    }

    @Override
    public void removeProfile(OneBlockProfile profile) {
        profiles.remove(profile);
    }
}
