package es.noobcraft.oneblock.profile;

import com.google.common.collect.Sets;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileCache;

import java.util.Set;
import java.util.stream.Collectors;

public class SetProfileCache implements ProfileCache {
    private final Set<OneBlockProfile> profiles = Sets.newHashSet();

    @Override
    public OneBlockProfile getProfile(OneBlockPlayer player, String worldName) {
        return profiles.stream()
                .filter(profile -> profile.getWorldName().equals(worldName))
                .filter(profile -> profile.getOwner().getName().equals(player.getName()))
                .findFirst().orElse(null);
    }

    @Override
    public Set<OneBlockProfile> getProfiles(String world) {
        return profiles.stream()
                .filter(profile -> profile.getWorldName().equals(world))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean addProfile(OneBlockProfile profile) {
        return profiles.add(profile);
    }

    @Override
    public boolean removeProfile(OneBlockProfile profile) {
        return profiles.remove(profile);
    }
}
