package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.NonNull;

import java.util.Set;

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
     * @param owner island owner
     * @param perms profile owner permissions
     * @param name profile name
     * @return the created profile
     */
    OneBlockProfile createProfile(@NonNull OneBlockPlayer player, @NonNull OneBlockPlayer owner, int perms, @NonNull String name);

    /**
     * Load all the players profiles from the database
     * @param player player to load profiles
     * @return all the player profiles
     */
    Set<OneBlockProfile> getProfiles(OneBlockPlayer player);

    /**
     * Delete a specific profile from the database
     * @param profile profile to remove
     * @return the operation status
     */
    boolean deleteProfile(@NonNull OneBlockProfile profile);

    /**
     * Update a profile into the database
     * @param profile profile to update
     * @return the operation status
     */
    boolean updateProfile(OneBlockProfile profile);
}
