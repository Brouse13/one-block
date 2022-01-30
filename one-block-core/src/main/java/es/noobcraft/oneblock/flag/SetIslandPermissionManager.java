package es.noobcraft.oneblock.flag;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import com.google.common.collect.Maps;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockConstants;
import es.noobcraft.oneblock.api.flags.IslandPermissionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SetIslandPermissionManager implements IslandPermissionManager {
    private final SQLClient sqlClient = Core.getSQLClient();
    //Map<WorldName, Map<Username, Permissions>>
    private final Map<String, Map<String, Integer>> permissions = Maps.newHashMap();

    private static final String GET_PERMISSION = "SELECT * FROM one_block_perms WHERE id=? AND username=?";
    private static final String CREATE_PERMISSIONS = "INSERT INTO one_block_perms VALUES(?, ?, ?)";
    private static final String UPDATE_PERMISSION = "UPDATE one_block_perms SET perms=? WHERE id=? AND username=?";

    @Override
    public int getPermission(String worldName, String username) {
        if (permissions.containsKey(worldName) && permissions.get(worldName).containsKey(username))
            return permissions.get(worldName).get(username);

        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(GET_PERMISSION)) {
                statement.setString(1, worldName);
                statement.setString(2, username);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        addCache(worldName, username, resultSet.getInt("perms"));
                        return resultSet.getInt("perms");
                    }
                }
            }
        }catch (SQLException exception) {
            throw new NotFoundException("Not found perms for username: "+ username+ " and world: "+ worldName, exception);
        }
        return 0;
    }

    @Override
    public int addCache(String worldName, String username, int permission) {
        Map<String, Integer> stored;
        if (permissions.containsKey(worldName)) stored = permissions.get(worldName);
        else stored = Maps.newHashMap();
        stored.put(username, permission);
        permissions.put(worldName, stored);
        return permission;
    }

    @Override
    public int removeCache(String world, String username) {
        final Integer perm = permissions.get(world).get(username);
        permissions.get(world).remove(username);
        return perm;
    }

    @Override
    public int createPerms(String worldName, String username, boolean owner) {
        final int pemrs = owner ? OneBlockConstants.DEF_ISLAND_PERMISSION : 0;
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(CREATE_PERMISSIONS)) {
                statement.setString(1, worldName);
                statement.setString(2, username);
                statement.setInt(3, pemrs);
                statement.executeQuery();
                return pemrs;
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public void updatePermission(String worldName, String username, int permission) {
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_PERMISSION)) {
                statement.setInt(1, permission);
                statement.setString(2, worldName);
                statement.setString(3, username);
                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
