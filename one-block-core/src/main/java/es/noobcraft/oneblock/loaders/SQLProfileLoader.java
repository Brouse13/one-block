package es.noobcraft.oneblock.loaders;

import com.google.common.collect.Sets;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.loaders.ProfileLoader;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public class SQLProfileLoader implements ProfileLoader {
    private String CREATE_PROFILE = "INSERT INTO one_block_profiles (username, name, world, island_owner, inventory, itemstack) VALUES(?, ?, ?, ?, ? ,?)";
    private String LOAD_PROFILES = "SELECT * FROM one_block_profiles WHERE username=?";
    private String UPDATE_PROFILE = "UPDATE one_block_profiles SET inventory=?, itemstack=? WHERE username=? AND name=?";
    private String DELETE_PROFILE = "DELETE FROM one_block_profiles WHERE username=? AND name=?";

    @Override
    public boolean createProfile(OneBlockProfile profile) {
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                //VALUES: username, name, world, island_owner, inventory, itemstack
                statement.setString(1, profile.getOwner().getName());
                statement.setString(2, profile.getProfileName());
                statement.setString(3, profile.getWorldName());
                statement.setString(4, profile.getIslandOwner());
                statement.setBlob(5, new SerialBlob(profile.getInventory()));
                statement.setString(6, profile.getProfileItem().toString());

                return statement.executeUpdate() != 0;
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, exception.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Set<OneBlockProfile>> loadProfiles(OneBlockPlayer player) {
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(LOAD_PROFILES)) {
                //VALUES: username
                statement.setString(1, player.getName());

                try(ResultSet resultSet = statement.executeQuery()) {
                    Set<OneBlockProfile> profiles = Sets.newHashSet();
                    while (resultSet.next())
                        profiles.add(new BaseOneBlockProfile(resultSet));

                    return profiles.isEmpty() ? Optional.empty() : Optional.of(profiles);
                }
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, exception.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<OneBlockProfile> loadProfile(OneBlockPlayer player, String profileName) {
        return loadProfiles(player).flatMap(profiles -> profiles.stream()
                .filter(profile -> profile.getProfileName().equals(profileName)).findFirst());
    }

    @Override
    public boolean updateProfile(OneBlockProfile profile) {
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_PROFILE)) {
                //VALUES: inventory, itemstack, username, name
                statement.setBlob(1, new SerialBlob(profile.getInventory()));
                statement.setString(2, profile.getProfileItem().toString());
                statement.setString(3, profile.getOwner().getName());
                statement.setString(4, profile.getProfileName());

                return statement.executeUpdate() != 0;
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, exception.getMessage());
            return false;
        }
    }

    @Override
    public void deleteProfile(OneBlockProfile profile) {
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(DELETE_PROFILE)) {
                //VALUES: username, name
                statement.setString(1, profile.getOwner().getName());
                statement.setString(2, profile.getProfileName());

                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, exception.getMessage());
        }
    }
}
