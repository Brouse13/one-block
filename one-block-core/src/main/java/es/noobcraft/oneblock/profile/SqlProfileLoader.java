package es.noobcraft.oneblock.profile;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.api.profile.ProfileLoader;
import es.noobcraft.oneblock.api.profile.ProfileName;
import lombok.NonNull;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class SqlProfileLoader implements ProfileLoader {
    private static final String EXIST_PROFILE = "SELECT * FROM one_block_profiles WHERE username=? AND world=?";
    private static final String CREATE_PROFILE = "INSERT INTO one_block_profiles VALUES(?, ?, ?, ?, null, ?)";
    private static final String GET_PROFILE_NAME = "SELECT * FROM one_block_profiles profile WHERE username=?";
    private static final String DELETE_PROFILE = "DELETE FROM one_block_profiles WHERE world=?";
    private static final String DELETE_COOP = "DELETE FROM one_block_profiles WHERE username=? AND world=?";
    private static final String UPDATE_PROFILE = "UPDATE one_block_profiles SET name=?, world=?, inventory=?, itemstack=? WHERE username=? AND world=?";

    private final SQLClient sqlClient = Core.getSQLClient();

    @Override
    public boolean existProfile(@NonNull OneBlockPlayer player, @NonNull String worldName) {
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(EXIST_PROFILE)) {
                statement.setString(1, player.getName());
                statement.setString(2, worldName);

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
    public OneBlockProfile createProfile(@NonNull OneBlockPlayer owner, @NonNull OneBlockPlayer islandOwner, @NonNull String worldName) {
        if (existProfile(owner, worldName)) return OneBlockAPI.getProfileManager().getProfile(getProfiles(owner), worldName);
        OneBlockProfile profile = null;

        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                if (OneBlockAPI.getWorldManager().createWorld(worldName, true)) {
                    final String name = ProfileName.randomName(owner.getProfiles()).name();
                    //private static final String CREATE_PROFILE = "INSERT INTO one_block_profiles VALUES(?, ?, ?, ?, ?, null, ?)";
                    profile = new BaseOneBlockProfile(owner, islandOwner.getName(), worldName, name);
                    statement.setString(1, owner.getName());
                    statement.setString(2, name);//TODO method to generate worldNames
                    statement.setString(3, worldName);
                    statement.setString(5, islandOwner.getName());
                    statement.setString(6, OneBlockConstants.DEF_PROFILE_MATERIAL.toString());
                    statement.execute();
                }
            }
            OneBlockAPI.getIslandPermissionLoader().createPerms(worldName, true);
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
    public boolean deleteProfile(@NonNull OneBlockProfile profile) {
        if (!existProfile(profile.getOwner(), profile.getWorldName())) return false;
        final boolean coop = !profile.getOwner().getName().equals(profile.getIslandOwner());

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(coop ? DELETE_COOP : DELETE_PROFILE)) {
                if (coop) {
                    statement.setString(1, profile.getOwner().getName());
                    statement.setString(2, profile.getWorldName());
                } else {
                    statement.setString(1, profile.getWorldName());
                    OneBlockAPI.getWorldManager().unloadWorld(profile.getWorldName());
                }

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
                statement.setString(2, profile.getWorldName());//World name
                statement.setBlob(3, new SerialBlob(profile.getInventory()));//Inv
                statement.setString(4, profile.getProfileItem().name());//ItemStack
                statement.setString(5, profile.getOwner().getName());//Profile username
                statement.setString(6, profile.getWorldName());//World name
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
