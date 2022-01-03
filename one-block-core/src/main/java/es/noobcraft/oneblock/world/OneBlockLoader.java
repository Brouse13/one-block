package es.noobcraft.oneblock.world;

import com.google.common.collect.Maps;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import es.noobcraft.core.api.Core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class OneBlockLoader implements SlimeLoader {
    //Create tables queries
    private static final String CREATE_VERSIONING_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `database_version` (`id` INT NOT NULL AUTO_INCREMENT, " +
            "`version` INT(11), PRIMARY KEY(id));";
    private static final String CREATE_WORLDS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `one_block_worlds` (`id` INT NOT NULL AUTO_INCREMENT, " +
            "`name` VARCHAR(255) UNIQUE, `world` MEDIUMBLOB, `locked` BIGINT, PRIMARY KEY(id));";

    //World queries
    private static final String SELECT_WORLD_QUERY = "SELECT `world`, `locked` FROM `one_block_worlds` WHERE `name` = ?;";
    private static final String UPDATE_WORLD_QUERY = "INSERT INTO `one_block_worlds` (`name`, `world`, `locked`) VALUES (?, ?, 0) ON DUPLICATE KEY UPDATE `world` = ?;";
    private static final String UPDATE_LOCK_QUERY = "UPDATE `one_block_worlds` SET `locked` = ? WHERE `name` = ?;";
    private static final String DELETE_WORLD_QUERY = "DELETE FROM `one_block_worlds` WHERE `name` = ?;";
    private static final String LIST_WORLDS_QUERY = "SELECT 'name' FROM one_block_worlds";

    private final Map<String, ScheduledFuture> lockedWorlds = Maps.newHashMap();

    public OneBlockLoader() throws SQLException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            //Create worlds table
            try (PreparedStatement statement = connection.prepareStatement(CREATE_WORLDS_TABLE_QUERY)) {
                statement.execute();
            }
            //Create versioning table
            try (PreparedStatement statement = connection.prepareStatement(CREATE_VERSIONING_TABLE_QUERY)) {
                statement.execute();
            }
        }
    }

    @Override
    public byte[] loadWorld(String worldName, boolean readOnly) throws UnknownWorldException, IOException, WorldInUseException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD_QUERY)) {
                statement.setString(1, worldName);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) throw new UnknownWorldException(worldName);

                if (!readOnly && resultSet.getLong("locked") == 1) throw new WorldInUseException("World is in use");

                return resultSet.getBytes("world");
            }
        } catch (SQLException exception) {
            throw new IOException(exception);
        }
    }

    @Override
    public boolean worldExists(String worldName) throws IOException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD_QUERY)) {
                statement.setString(1, worldName);
                ResultSet set = statement.executeQuery();
                return set.next();
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public List<String> listWorlds() throws IOException {
        List<String> worldList = new ArrayList<>();

        try (Connection con = Core.getSQLClient().getConnection()) {
            try( PreparedStatement statement = con.prepareStatement(LIST_WORLDS_QUERY)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next())
                        worldList.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
        return worldList;
    }

    @Override
    public void saveWorld(String worldName, byte[] serializedWorld, boolean lock) throws IOException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_WORLD_QUERY)) {
                statement.setString(1, worldName);
                statement.setBytes(2, serializedWorld);
                statement.setBytes(3, serializedWorld);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void unlockWorld(String worldName) throws IOException, UnknownWorldException {
        ScheduledFuture future = lockedWorlds.remove(worldName);

        if (future != null) {
            future.cancel(false);
        }

        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_LOCK_QUERY)) {
                statement.setLong(1, 0L);
                statement.setString(2, worldName);

                if (statement.executeUpdate() == 0) throw new UnknownWorldException(worldName);
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public boolean isWorldLocked(String worldName) throws IOException, UnknownWorldException {
        if (lockedWorlds.containsKey(worldName)) return true;


        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD_QUERY)) {
                statement.setString(1, worldName);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) throw new UnknownWorldException(worldName);

                return resultSet.getLong("locked") == 1;
            }

        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void deleteWorld(String worldName) throws IOException, UnknownWorldException {
        ScheduledFuture future = lockedWorlds.remove(worldName);

        if (future != null) future.cancel(false);

        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(DELETE_WORLD_QUERY)) {
                statement.setString(1, worldName);

                if (statement.executeUpdate() == 0) throw new UnknownWorldException(worldName);
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}