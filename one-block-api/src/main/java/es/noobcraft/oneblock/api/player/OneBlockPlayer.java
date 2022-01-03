package es.noobcraft.oneblock.api.player;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;

import java.util.Set;

public interface OneBlockPlayer {
    /**
     * Get the OneBlockPlayer name.
     * @return the player name
     */
    String getName();

    /**
     * Set the player profiles, this is a destructive
     * action, it will overwrite all te loaded profiles.
     */
    void setProfiles(Set<OneBlockProfile> profiles);

    /**
     * Get a Set that contains all the loaded
     * player profiles.
     * @return all the player profiles
     */
    Set<OneBlockProfile> getProfiles();

    /**
     * Get the current profile that the player is using.
     * This method can return null, if the player is visiting an
     * island or in the lobby.
     * @return the inUse player profile
     */
    OneBlockProfile getCurrentProfile();

    /**
     * Set the current profile to the player
     */
    void setCurrentProfile(OneBlockProfile profile);

    /**
     * Add a profile to the player, if the player has
     * all the profiles completed it won't add it.
     * @param profile profile to add
     * @return operation status
     */
    boolean addProfile(OneBlockProfile profile);

    /**
     * Remove a profile from the player loaded profiles.
     * @param profile profile to remove
     * @return operation status
     */
    boolean removeProfile(OneBlockProfile profile);

    /**
     * Get the max amount of profiles that this player
     * can have at once, this also counts the coop profiles.
     * @return profiles available
     */
    int getMaxProfiles();
}
