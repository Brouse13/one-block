package es.noobcraft.oneblock.permission;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import com.google.common.collect.Maps;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.permission.PermissionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SetPermissionManager implements PermissionManager {
    //Map<WorldName, Permission>
    private final Map<String, Integer> permissions = Maps.newHashMap();

    private static final String GET_PERMISSION = "SELECT permissions FROM one_block_worlds WHERE name=?";
    private static final String UPDATE_PERMISSIONS = "UPDATE one_block_worlds SET permissions=? WHERE world=?";

    @Override
    public int getPermission(String worldName) {
        if (permissions.containsKey(worldName)) return permissions.get(worldName);

        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(GET_PERMISSION)) {
                statement.setString(1, worldName);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next())
                        return addCache(worldName, resultSet.getInt("permissions"));
                }
            }
        }catch (SQLException exception) {
            throw new NotFoundException("Not found perms for world: "+ worldName, exception);
        }
        return 0;
    }

    @Override
    public int addCache(String worldName, int permission) {
        permissions.put(worldName, permission);
        return permission;
    }

    @Override
    public void removeCache(String world) {
        permissions.remove(world);
    }

    @Override
    public int createPerms(String worldName, boolean owner) {
        final int perms = owner ? OneBlockAPI.getSettings().getDefaultIslandPermission() : 0;

        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_PERMISSIONS)) {
                statement.setInt(1, perms);
                statement.setString(2, worldName);
                statement.executeQuery();
                return perms;
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public void updatePermission(String worldName, int permission) {
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_PERMISSIONS)) {
                statement.setInt(1, permission);
                statement.setString(2, worldName);
                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
