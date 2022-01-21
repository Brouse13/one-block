package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import lombok.NonNull;

import java.util.Set;

public interface ProfileLoader {
    /**
     * Get if the database has a profile with the given name
     * @param player player to check from
     * @param name profile name
     * @return if the profile exists
     */
    boolean existProfile(@NonNull OneBlockPlayer player, @NonNull String name);

    /**
     * Create a new profile to the given player with a specific name.
     * @param owner profile owner
     * @param islandOwner island owner
     * @param perms profile owner permissions
     * @param name profile name
     * @return the created profile
     */
    OneBlockProfile createProfile(@NonNull OneBlockPlayer owner, @NonNull OneBlockPlayer islandOwner, int perms, @NonNull String name);

    /**
     * Get all the player profiles that are loaded on the database
     * @param player player to load profiles
     * @return all the player profiles
     */
    Set<OneBlockProfile> getProfiles(@NonNull OneBlockPlayer player);

    /**
     * Get all the profiles that are stored for a specific world.
     * Those profiles should not be stored on the ProfileCache due to
     * they won't be removed on player leave. If they're added they must
     * be removed manually.
     * @param world world to query
     * @return stored profiles
     */
    Set<OneBlockProfile> getProfiles(@NonNull String world);

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

    /**
     * Update the world permissions into the database
     * @param world world naem
     * @param perms new perms
     * @return the operation status
     */
    boolean updatePermissions(String world, int perms);
}
