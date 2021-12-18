package es.noobcraft.oneblock.api.player;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;

import java.util.Set;

public interface OneBlockPlayer {
    /**
     * Get the OneBlockPlayer name
     * @return the player name
     */
    String getName();

    /**
     * Initialize the player profiles.
     */
    void setProfiles(Set<OneBlockProfile> profiles);

    /**
     * Get a Set that contains all the available
     * profiles for this player
     * @return all available profiles
     */
    Set<OneBlockProfile> getProfiles();

    /**
     * Add a profile to the player, if the player has
     * all the profiles completed it won't add it
     * @param profile profile to remove
     * @return operation status
     */
    boolean addProfile(OneBlockProfile profile);

    /**
     * Remove a profile from the player
     * @param profile profile to remove
     * @return operation status
     */
    boolean removeProfile(OneBlockProfile profile);

    /**
     * Get the max amount of profiles that this player
     * can have at once, this also counts the coop profiles
     * @return profiles available
     */
    int getMaxProfiles();

    /**
     * Get the last time that this player joined to the
     * OneBlock game
     * @return player last played
     */
    long getLastPlayed();
}
