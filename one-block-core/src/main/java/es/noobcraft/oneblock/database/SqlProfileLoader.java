package es.noobcraft.oneblock.database;

import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.database.ProfileLoader;
import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import es.noobcraft.oneblock.profile.BaseOneBlockProfile;
import lombok.NonNull;
import org.bukkit.Material;

import java.io.IOException;
import java.sql.*;

public class SqlProfileLoader implements ProfileLoader {
    private final String EXIST_PROFILE = "SELECT * FROM one_block_profiles WHERE username=? AND name=?";
    private final String CREATE_PROFILE = "INSERT INTO one_block_profiles VALUES(?, ?, ?, ?, ?, null, ?)";
    private final String GET_PROFILE = "SELECT * FROM one_block_profiles WHERE username=? AND name=?";
    private final String DELETE_PROFILE = "DELETE FROM one_block_profiles WHERE name=?";
    private final String DELETE_COOP = "DELETE FROM one_block_profiles WHERE username=? AND name=?";
    private final String UPDATE_PROFILE = "UPDATE one_block_profiles SET " +
            "username=?, name=?, world=?, island_owner=?, island_permissions=?, inventory=?, itemstack=?";

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
    public OneBlockProfile createProfile(@NonNull OneBlockPlayer player, String name) {
        if (existProfile(player, name)) return getProfile(player, name);

        OneBlockProfile profile = null;
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                if (OneBlockAPI.getWorldManager().createWorld(player, name)) {
                    profile = new BaseOneBlockProfile(player, name);
                    statement.setString(1, player.getName());
                    statement.setString(2, name);
                    statement.setString(3, name);
                    statement.setString(4, player.getName());
                    statement.setInt(5,  -1);
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
    public OneBlockProfile createCoop(@NonNull OneBlockProfile profile, OneBlockPlayer target) {
        if (existProfile(target, profile.getProfileName())) return getProfile(target, profile.getProfileName());

        OneBlockProfile newProfile = null;
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PROFILE)) {
                newProfile = new BaseOneBlockProfile(target, profile.getIslandOwner(), profile.getProfileName());

                statement.setString(1, target.getName());
                statement.setString(2, profile.getProfileName());
                statement.setString(3, profile.getProfileName());
                statement.setString(4, profile.getIslandOwner());
                statement.setInt(5,  OneBlockConstants.DEF_ISLAND_PERMISSION);
                statement.setString(6, OneBlockConstants.DEF_PROFILE_MATERIAL.toString());

                statement.execute();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return newProfile;
    }

    @Override
    public OneBlockProfile getProfile(OneBlockPlayer player, String name) {
        if (!existProfile(player, name)) return null;

        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(GET_PROFILE)) {
                statement.setString(1, player.getName());
                statement.setString(2, name);

                try(ResultSet resultSet = statement.executeQuery()) {
                    final Blob inventory = resultSet.getBlob("inventory");
                    return new BaseOneBlockProfile(
                            OneBlockAPI.getPlayerCache().getPlayer(resultSet.getString("username")),
                            resultSet.getString("name"),
                            resultSet.getString("island_owner"),
                            inventory == null ? null : inventory.getBytes(0, (int) inventory.length()),
                            resultSet.getInt("island_permissions"),
                            Material.valueOf(resultSet.getString("itemstack"))
                    );
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteProfile(OneBlockPlayer player, String name) {
        if (!existProfile(player, name)) return false;

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_PROFILE)) {
                statement.setString(1, name);

                return statement.execute();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCoop(OneBlockProfile profile, OneBlockPlayer target) {
        if (!existProfile(target, profile.getProfileName())) return false;

        try(Connection connection = sqlClient.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_COOP)) {
                statement.setString(1, target.getName());
                statement.setString(2, profile.getProfileName());

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
                statement.setInt(5, profile.getIslandSettings());
                statement.setString(6, profile.getProfileItem().toString());

                statement.executeUpdate();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
