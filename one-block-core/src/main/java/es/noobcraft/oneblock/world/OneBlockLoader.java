package es.noobcraft.oneblock.world;

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

public class OneBlockLoader implements SlimeLoader {
    private static final String SELECT_WORLD_QUERY = "SELECT world, locked FROM one_block_worlds WHERE name = ?";
    private static final String UPDATE_WORLD_QUERY = "INSERT INTO one_block_worlds (name, world, locked) VALUES (?, ?, 0) ON DUPLICATE KEY UPDATE world = ?";
    private static final String UPDATE_LOCK_QUERY = "UPDATE one_block_worlds SET locked = ? WHERE name = ?";
    private static final String DELETE_WORLD_QUERY = "DELETE FROM one_block_worlds WHERE name = ?";
    private static final String LIST_WORLDS_QUERY = "SELECT name FROM one_block_worlds";

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
                    while (resultSet.next()) worldList.add(resultSet.getString("name"));
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