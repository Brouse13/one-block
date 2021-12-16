package es.noobcraft.oneblock.api.database;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;

public interface ProfileLoader {
    /**
     * Return if the database has any profile with the given name
     * @param player player to check profiles
     * @param name profile name
     * @return if the profile exists
     */
    boolean existProfile(OneBlockPlayer player, String name);

    /**
     * Create a new profile to the given player with a specific name.
     * @param player profile owner
     * @param name profile name
     * @return the created profile
     */
    OneBlockProfile createProfile(OneBlockPlayer player, String name);

    /**
     * Add a player to a specific island as a coop
     * @param profile profile to add player
     * @param target  target to add
     * @return the created profile
     */
    OneBlockProfile createCoop(OneBlockProfile profile, OneBlockPlayer target);

    /**
     * Get an existing OneBlockProfile from the database
     * @param player player to lock for profiles
     * @param name profile name
     * @return the player profile
     */
    OneBlockProfile getProfile(OneBlockPlayer player, String name);

    /**
     * Delete a specific profile from the database
     * @param player player to
     * @param name profile name
     * @return the operation status
     */
    boolean deleteProfile(OneBlockPlayer player, String name);

    /**
     * Delete a specific profile from the database
     * @param profile profile from to remove
     * @param target player to remove
     * @return the operation status
     */
    boolean deleteCoop(OneBlockProfile profile, OneBlockPlayer target);

    /**
     * Update a profile into the database
     * @param profile profile to update
     * @return the operation status
     */
    boolean updateProfile(OneBlockProfile profile);
}
