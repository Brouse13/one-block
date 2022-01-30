package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.NonNull;

import java.util.Set;

public interface ProfileLoader {
    /**
     * Get if the database has a profile with the given name
     * @param player player to check from
     * @param worldName world name
     * @return if the profile exists
     */
    boolean existProfile(@NonNull OneBlockPlayer player, @NonNull String worldName);

    /**
     * Create a new profile to the given player with a specific name.
     * @param owner profile owner
     * @param islandOwner island owner
     * @param worldName name of the world
     * @return the created profile
     */
    OneBlockProfile createProfile(@NonNull OneBlockPlayer owner, @NonNull OneBlockPlayer islandOwner, @NonNull String worldName);

    /**
     * Get all the player profiles that are loaded on the database
     * @param player player to load profiles
     * @return all the player profiles
     */
    Set<OneBlockProfile> getProfiles(@NonNull OneBlockPlayer player);

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
    boolean updateProfile(@NonNull OneBlockProfile profile);
}
