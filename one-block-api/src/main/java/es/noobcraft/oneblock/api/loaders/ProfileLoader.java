package es.noobcraft.oneblock.api.loaders;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;

import java.util.Optional;
import java.util.Set;

public interface ProfileLoader {
    /**
     * Create a profile into the database.
     * @param profile profile t create
     * @return the operation status
     */
    boolean createProfile(OneBlockProfile profile);

    /**
     * Load all the profiles from the player stored on the database
     * @param player owner of the profiles
     * @return an Optional with all the loaded profiles
     */
    Optional<Set<OneBlockProfile>> loadProfiles(OneBlockPlayer player);

    /**
     * Load a profile from the database, if it's not
     * present it will return an empty Optional
     * @param player owner of the profile
     * @param profileName name of the profile
     * @return an Optional with the loaded profile
     */
    Optional<OneBlockProfile> loadProfile(OneBlockPlayer player, String profileName);

    /**
     * Update a specific profile into the database
     * @param profile profile to update
     * @return the operation status
     */
    boolean updateProfile(OneBlockProfile profile);

    /**
     * Delete a profile from the database
     * @param profile profile to remove
     * @apiNote if the profile is a main profile
     * it will remove all its childs
     */
    void deleteProfile(OneBlockProfile profile);
}
