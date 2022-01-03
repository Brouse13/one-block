package es.noobcraft.oneblock.profile;

import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.exceptions.IslandFullException;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileManager;

import java.util.Set;

public class BaseProfileManager implements ProfileManager {
    @Override
    public OneBlockProfile getProfile(Set<OneBlockProfile> profiles, String name) {
        return profiles.stream().filter(profile -> profile.getProfileName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public boolean canBuild(Set<OneBlockProfile> profiles, OneBlockPlayer player) {
        //TODO perms I don't know how to manage them
        return profiles.stream()
                .filter(profile -> profile.getOwner().getName().equals(player.getName()))
                .anyMatch(profile -> profile.getIslandPermissions() == -1);
    }

    @Override
    public OneBlockProfile createCoop(OneBlockPlayer owner, OneBlockProfile masterProfile) throws IslandFullException {
        if (owner.getMaxProfiles() >= owner.getProfiles().size()) throw new IslandFullException();

        OneBlockPlayer islandOwner = OneBlockAPI.getPlayerCache().getPlayer(masterProfile.getIslandOwner());
        return OneBlockAPI.getProfileLoader().createProfile(owner, islandOwner,
                OneBlockConstants.DEF_ISLAND_PERMISSION, masterProfile.getProfileName());
    }
}
