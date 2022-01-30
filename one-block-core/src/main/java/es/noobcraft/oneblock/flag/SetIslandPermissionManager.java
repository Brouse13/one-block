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
    //Map<WorldName, Map<Username, Permsissions>>
    private final Map<String, Map<String, Integer>> permissions = Maps.newHashMap();

    private final String GET_PERMISSION = "SELECT world_permissions FROM one_block_worlds WHERE name=?";
    private final String UPDATE_PERMISSION = "UPDATE one_block_worlds SET world_permissions=? WHERE name=?";

    @Override
    public int getPermission(String worldName, String username) {
        return 0;
    }

    @Override
    public int addCache(String worldName, String username, int permission) {
        return 0;
    }

    @Override
    public int removeCache(String world, String username) {
        return 0;
    }

    @Override
    public int createPerms(String worldName, String username, boolean owner) {
        return 0;
    }

    @Override
    public void updatePermission(String worldName, String username, int permission) {

    }
}
