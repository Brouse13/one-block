package es.noobcraft.oneblock.flag;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import com.google.common.collect.Maps;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.flags.IslandPermissionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SetIslandPermissionManager implements IslandPermissionManager {
    private final SQLClient sqlClient = Core.getSQLClient();
    private final Map<String, Integer> permissions = Maps.newHashMap();

    private final String GET_PERMISSION = "SELECT world_permissions FROM one_block_worlds WHERE name=?";
    private final String UPDATE_PERMISSION = "UPDATE one_block_worlds SET world_permissions=? WHERE name=?";

    @Override
    public int getPermission(String name) {
       if(permissions.containsKey(name)) return permissions.get(name);

       try(Connection connection = sqlClient.getConnection()) {
           try(final PreparedStatement statement = connection.prepareStatement(GET_PERMISSION)) {
                statement.setString(1, name);

                try(final ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int permission = resultSet.getInt("world_permissions");
                        System.out.println(name+ " "+ permission);
                        return addCache(name, permission);
                    }
                }
           }
       }catch (SQLException exception) {
           exception.printStackTrace();
       }
        throw new NotFoundException("Not found permissions for world "+ name);
    }

    @Override
    public int addCache(String world, int permission) {
        permissions.put(world, permission);
        return permission;
    }

    @Override
    public int removeCache(String world) {
        return permissions.remove(world);
    }

    @Override
    public void updatePermission(String world, int permission) {
        permissions.put(world, permission);
        try(Connection connection = sqlClient.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_PERMISSION)) {
                statement.setInt(1, permission);
                statement.setString(2, world);

                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
