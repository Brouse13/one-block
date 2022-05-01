package es.noobcraft.oneblock.api.player;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;

import java.util.Set;

public interface OfflineOneBlockPlayer {
    /**
     * Get the name of the player
     * @return the player name
     */
    String getName();

    /**
     * Get all the profiles that the player has
     * @return player profiles set
     */
    Set<OneBlockProfile> getProfiles();

    /**
     * Add a profile to the player
     * @param profile profile to add
     */
    void addProfile(OneBlockProfile profile);

    /**
     * Remove a profile from the player profiles
     * @param profile profile to remove
     */
    void removeProfile(OneBlockProfile profile);

    /**
     * Get the max amount of profiles that the player can have
     * @return the player max profiles amount
     */
    int getMaxProfiles();
}
