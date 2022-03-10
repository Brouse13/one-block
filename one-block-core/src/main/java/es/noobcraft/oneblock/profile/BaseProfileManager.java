package es.noobcraft.oneblock.profile;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.exceptions.IslandFullException;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileManager;

import java.util.Set;

public class BaseProfileManager implements ProfileManager {
    @Override
    public OneBlockProfile getProfile(Set<OneBlockProfile> profiles, String worldName) {
        if (profiles == null) return null;
        return profiles.stream().filter(profile -> profile.getWorldName().equalsIgnoreCase(worldName)).findFirst().orElse(null);
    }

    @Override
    public OneBlockProfile getProfile(Set<OneBlockProfile> profiles, OneBlockPlayer player) {
        if (profiles == null) return null;
        return profiles.stream().filter(profile -> profile.getOwner().getName().equals(player.getName())).findFirst().orElse(null);
    }

    @Override
    public OneBlockProfile createCoop(OneBlockPlayer owner, OneBlockProfile masterProfile) throws IslandFullException {
        if (owner.getMaxProfiles() >= owner.getProfiles().size()) throw new IslandFullException();

        OneBlockPlayer islandOwner = OneBlockAPI.getPlayerCache().getPlayer(masterProfile.getIslandOwner());
        return OneBlockAPI.getProfileLoader().createProfile(owner, islandOwner, masterProfile.getWorldName());
    }
}
