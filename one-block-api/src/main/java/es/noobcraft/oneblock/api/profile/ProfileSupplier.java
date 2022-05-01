package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;

import java.sql.ResultSet;

public interface ProfileSupplier {
    /**
     * Creates a new empty OneBlockProfile
     * @param owner owner of the profile
     * @param islandOwner owner of the island
     * @param worldName world name
     * @param profileName profile name
     * @return the created profile
     */
    OneBlockProfile createProfile(OneBlockPlayer owner, String islandOwner, String worldName, String profileName);

    /**
     * Create a new OneBlockProfile from a ResultSet
     * @param resultSet one_block_profiles ResultSet
     * @return the created profile
     */
    OneBlockProfile createProfile(ResultSet resultSet);
}
