package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.exceptions.IslandFullException;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;

import java.util.Set;

public interface ProfileManager {
    /**
     * Get a specific profile from a set of profiles.
     * @param profiles set of profiles
     * @param name profile name to search
     * @return the profile found
     */
    OneBlockProfile getProfile(Set<OneBlockProfile> profiles, String name);

    /**
     * Get if a player can build on one of the profiles given
     * @param profiles set of profiles
     * @param player if player can build
     * @return if the player can build
     */
    boolean canBuild(Set<OneBlockProfile> profiles, OneBlockPlayer player);

    /**
     * Create a new coop to te given player, if the island is full it will
     * throw an IslandFullException.
     * @param owner profile owner
     * @param masterProfile main profile from where make the coop
     * @throws IslandFullException if the island has already max coops
     * @return the created profile
     */
    OneBlockProfile createCoop(OneBlockPlayer owner, OneBlockProfile masterProfile) throws IslandFullException;
}
