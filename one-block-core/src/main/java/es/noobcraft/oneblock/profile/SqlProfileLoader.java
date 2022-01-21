package es.noobcraft.oneblock.profile;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import lombok.NonNull;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class SqlProfileLoader implements ProfileLoader {
    private static final String EXIST_PROFILE = "SELECT * FROM one_block_profiles WHERE username=? AND name=?";
    private static final String CREATE_PROFILE = "INSERT INTO one_block_profiles VALUES(?, ?, ?, ?, null, ?)";
    private static final String GET_PROFILE_NAME = "SELECT * FROM one_block_profiles profile JOIN one_block_worlds world ON profile.world = world.name WHERE username=?";
    private static final String GET_PROFILE_WORLD = "SELECT * FROM one_block_profiles profile JOIN one_block_worlds world ON profile.world = world.name WHERE world.name=?";
    private static final String DELETE_PROFILE = "DELETE FROM one_block_profiles WHERE name=?";
    private static final String DELETE_COOP = "DELETE FROM one_block_profiles WHERE username=? AND name=?";
    private static final String UPDATE_PROFILE = "UPDATE one_block_profiles SET name=?, world=?, inventory=?, itemstack=? WHERE username=? AND name=?";
    private static final String UPDATE_PERMS = "UPDATE one_block_worlds SET world_permissions=? WHERE name=? ";

    private final SQLClient sqlClient = Core.getSQLClient();

    @Override
    public boolean existProfile(@NonNull OneBlockPlayer player, @NonNull String name) {
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
    public OneBlockProfile createProfile(@NonNull OneBlockPlayer owner, @NonNull OneBlockPlayer islandOwner, int perms, @NonNull String name) {
        if (existProfile(owner, name)) return OneBlockAPI.getProfileManager().getProfile(getProfiles(owner), name);

        OneBlockProfile profile = null;
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                if (OneBlockAPI.getWorldManager().createWorld(name, true)) {
                    profile = new BaseOneBlockProfile(owner, islandOwner.getName(), name, perms);
                    statement.setString(1, owner.getName());
                    statement.setString(2, name);
                    statement.setString(3, name);
                    statement.setString(4, islandOwner.getName());
                    statement.setString(5, OneBlockConstants.DEF_PROFILE_MATERIAL.toString());
                    statement.execute();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    @Override
    public Set<OneBlockProfile> getProfiles(@NonNull OneBlockPlayer player) {
        Set<OneBlockProfile> profiles = Sets.newHashSet();

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_PROFILE_NAME)) {
                statement.setString(1, player.getName());

                try(final ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) profiles.add(new BaseOneBlockProfile(resultSet));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return profiles;
    }

    @Override
    public Set<OneBlockProfile> getProfiles(@NonNull String world) {
        Set<OneBlockProfile> profiles = Sets.newHashSet();

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_PROFILE_WORLD)) {
                statement.setString(1, world);

                try(final ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) profiles.add(new BaseOneBlockProfile(resultSet));
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

                if (!coop)
                    OneBlockAPI.getWorldManager().unloadWorld(profile.getProfileName());
                return statement.execute();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateProfile(@NonNull OneBlockProfile profile) {
        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PROFILE)) {
                statement.setString(1, profile.getProfileName());//Profile name
                statement.setString(2, profile.getProfileName());//World name
                statement.setInt(3, profile.getIslandPermissions());//IslandPerms
                statement.setBlob(4, new SerialBlob(profile.getInventory()));//Inv
                statement.setString(5, profile.getProfileItem().name());//ItemStack
                statement.setString(6, profile.getOwner().getName());//Profile username
                statement.setString(7, profile.getProfileName());//Profile name
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePermissions(String world, int perms) {
        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PERMS)) {
                statement.setInt(1, perms);
                statement.setString(2, world);

                statement.executeUpdate();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
