package es.noobcraft.oneblock.profile;

import com.google.common.collect.Sets;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import lombok.NonNull;

import java.io.IOException;
import java.sql.*;
import java.util.Set;

public class SqlProfileLoader implements ProfileLoader {
    private static final String EXIST_PROFILE = "SELECT * FROM one_block_profiles WHERE username=? AND name=?";
    private static final String CREATE_PROFILE = "INSERT INTO one_block_profiles VALUES(?, ?, ?, ?, ?, null, ?)";
    private static final String GET_PROFILES = "SELECT * FROM one_block_profiles WHERE username=?";
    private static final String DELETE_PROFILE = "DELETE FROM one_block_profiles WHERE name=?";
    private static final String DELETE_COOP = "DELETE FROM one_block_profiles WHERE username=? AND name=?";
    private static final String UPDATE_PROFILE = "UPDATE one_block_profiles SET " +
            "username=?, name=?, world=?, island_owner=?, island_permissions=?, inventory=?, itemstack=? WHERE username=? AND name=?";

    private final SQLClient sqlClient = Core.getSQLClient();

    @Override
    public boolean existProfile(@NonNull OneBlockPlayer player, String name) {
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(EXIST_PROFILE)) {
                statement.setString(1, player.getName());
                statement.setString(2, name);

                try(ResultSet userResult = statement.executeQuery()) {
                    return userResult.next();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public OneBlockProfile createProfile(@NonNull OneBlockPlayer player, @NonNull OneBlockPlayer owner, int perms, @NonNull String name) {
        if (existProfile(player, name)) return OneBlockAPI.getProfileManager().getProfile(getProfiles(player), name);

        OneBlockProfile profile = null;
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                if (OneBlockAPI.getWorldManager().createWorld(player, name)) {
                    profile = new BaseOneBlockProfile(player, name);
                    statement.setString(1, player.getName());
                    statement.setString(2, name);
                    statement.setString(3, name);
                    statement.setString(4, owner.getName());
                    statement.setInt(5,  perms);
                    statement.setString(6, OneBlockConstants.DEF_PROFILE_MATERIAL.toString());
                    statement.execute();
                }
            } catch (IOException | WorldAlreadyExistsException e) {
                e.printStackTrace();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    @Override
    public Set<OneBlockProfile> getProfiles(OneBlockPlayer player) {
        Set<OneBlockProfile> profiles = Sets.newHashSet();

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_PROFILES)) {
                statement.setString(1, player.getName());

                try(final ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next())
                        profiles.add(new BaseOneBlockProfile(resultSet));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return profiles;
    }

    @Override
    public boolean deleteProfile(@NonNull OneBlockProfile profile) {
        if (!existProfile(profile.getOwner(), profile.getProfileName())) return false;

        final boolean coop = !profile.getOwner().getName().equals(profile.getIslandOwner());

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(coop ? DELETE_COOP : DELETE_PROFILE)) {
                if (coop) {
                    statement.setString(1, profile.getOwner().getName());
                    statement.setString(2, profile.getProfileName());
                } else
                    statement.setString(1, profile.getProfileName());

                if (!coop) {
                    OneBlockAPI.getWorldManager().unloadWorld(profile.getProfileName());
                    //Remove Profile
                }

                return statement.execute();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateProfile(OneBlockProfile profile) {
        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PROFILE)) {
                statement.setString(1, profile.getOwner().getName());
                statement.setString(2, profile.getProfileName());
                statement.setString(3, profile.getProfileName());
                statement.setString(4, profile.getIslandOwner());
                statement.setInt(5, profile.getIslandPermissions());
                statement.setString(6, profile.getProfileItem().toString());
                statement.setString(7, profile.getOwner().getName());
                statement.setString(7, profile.getProfileName());
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
