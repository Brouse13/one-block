package es.noobcraft.oneblock.loaders;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SQLSlimeLoader implements SlimeLoader {
    //World look pool
    private static final ScheduledExecutorService LOCK_POOL = Executors.newScheduledThreadPool(2,
            new ThreadFactoryBuilder().setNameFormat("SWM MySQL Lock Pool Thread #%1$d").build());

    //World handling queries
    private static final String SELECT_WORLD = "SELECT world, locked FROM one_block_worlds WHERE name = ?";
    private static final String UPDATE_WORLD = "INSERT INTO one_block_worlds (name, world, locked) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE world = ?";
    private static final String UPDATE_LOCK = "UPDATE one_block_worlds SET locked = ? WHERE name = ?";
    private static final String DELETE_WORLD = "DELETE FROM one_block_worlds WHERE name = ?";
    private static final String LIST_WORLDS = "SELECT name FROM one_block_worlds";

    private final Map<String, ScheduledFuture> lockedWorlds = new HashMap<>();

    public static final long MAX_LOCK_TIME = 300000L;
    public static final long LOCK_INTERVAL = 60000L;

    @Override
    public byte[] loadWorld(String worldName, boolean readOnly) throws UnknownWorldException, IOException, WorldInUseException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD)) {
                statement.setString(1, worldName);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) throw new UnknownWorldException(worldName);

                    if (!readOnly) {
                        if (System.currentTimeMillis() - resultSet.getLong("locked") <= MAX_LOCK_TIME)
                            throw new WorldInUseException(worldName);

                        updateLock(worldName, true);
                    }
                    return resultSet.getBytes("world");
                }
            }
        } catch (SQLException exception) {
            throw new IOException(exception);
        }
    }

    private void updateLock(String worldName, boolean forceSchedule) {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_LOCK)) {
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
            lockedWorlds.put(worldName, LOCK_POOL.schedule(() -> updateLock(worldName, false), LOCK_INTERVAL, TimeUnit.MILLISECONDS));
    }

    @Override
    public boolean worldExists(String worldName) throws IOException {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD)) {
                statement.setString(1, worldName);
                try(ResultSet set = statement.executeQuery()) {
                    return set.next();
                }
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public List<String> listWorlds() throws IOException {
        List<String> worldList = new ArrayList<>();

        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(LIST_WORLDS)) {
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
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_WORLD)) {
                statement.setString(1, worldName);
                statement.setBytes(2, serializedWorld);
                statement.setBytes(3, serializedWorld);
                statement.executeUpdate();
            }
            if (lock) updateLock(worldName, true);
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void unlockWorld(String worldName) throws IOException, UnknownWorldException {
        ScheduledFuture future = lockedWorlds.remove(worldName);

        if (future != null) future.cancel(false);

        try (Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_LOCK)) {
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
            try(PreparedStatement statement = connection.prepareStatement(SELECT_WORLD)) {
                statement.setString(1, worldName);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) throw new UnknownWorldException(worldName);

                    return System.currentTimeMillis() - resultSet.getLong("locked") <= MAX_LOCK_TIME;
                }
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
            try(PreparedStatement statement = connection.prepareStatement(DELETE_WORLD)) {
                statement.setString(1, worldName);

                if (statement.executeUpdate() == 0) throw new UnknownWorldException(worldName);
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}