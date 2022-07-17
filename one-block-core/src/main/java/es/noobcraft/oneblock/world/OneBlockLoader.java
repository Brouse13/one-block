package es.noobcraft.oneblock.world;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.logger.Logger;
import es.noobcraft.oneblock.logger.LoggerType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OneBlockLoader implements SlimeLoader {
    public static final long MAX_LOCK_TIME = 300000L; //Max time difference between current time millis and world lock
    public static final long LOCK_INTERVAL = 60000L;

    //World locking executor service
    private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(2, new ThreadFactoryBuilder()
            .setNameFormat("SWM MySQL Lock Pool Thread #%1$d").build());


    //Create tables queries
    private static final String CREATE_VERSIONING_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `database_version` (`id` INT NOT NULL AUTO_INCREMENT, " +
            "`version` INT(11), PRIMARY KEY(id));";
    private static final String CREATE_WORLDS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `worlds` (`id` INT NOT NULL AUTO_INCREMENT, " +
            "`name` VARCHAR(255) UNIQUE, `world` MEDIUMBLOB, `locked` BIGINT, PRIMARY KEY(id));";

    //World queries
    private static final String SELECT_WORLD_QUERY = "SELECT `world`, `locked` FROM `one_block_worlds` WHERE `name` = ?;";
    private static final String UPDATE_WORLD_QUERY = "INSERT INTO `one_block_worlds` (`name`, `world`, `locked`) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE `world` = ?;";
    private static final String UPDATE_LOCK_QUERY = "UPDATE `one_block_worlds` SET `locked` = ? WHERE `name` = ?;";
    private static final String DELETE_WORLD_QUERY = "DELETE FROM `one_block_worlds` WHERE `name` = ?;";
    private static final String LIST_WORLDS_QUERY = "SELECT 'name' FROM 'one_block_worlds'";

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

                if (!readOnly) {
                    long lockedMillis = resultSet.getLong("locked");

                    if (System.currentTimeMillis() - lockedMillis <= MAX_LOCK_TIME) throw new WorldInUseException(worldName);

                    updateLock(worldName, true);
                }
                return resultSet.getBytes("world");
            }
        } catch (SQLException exception) {
            throw new IOException(exception);
        }
    }

    private void updateLock(String worldName, boolean forceSchedule) {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_LOCK_QUERY)) {
                statement.setLong(1, System.currentTimeMillis());
                statement.setString(2, worldName);

                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, "Failed to update the lock for world " + worldName + ":");
            exception.printStackTrace();
        }

        //Only schedule another update if the world is still on the map
        if (forceSchedule || lockedWorlds.containsKey(worldName))
            lockedWorlds.put(worldName, SERVICE.schedule(() -> updateLock(worldName, false), LOCK_INTERVAL, TimeUnit.MILLISECONDS));
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

                if (lock) updateLock(worldName, true);
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

                return System.currentTimeMillis() - resultSet.getLong("locked") <= MAX_LOCK_TIME;
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