package es.noobcraft.oneblock.phase;

import com.google.common.collect.Maps;
import es.noobcraft.core.api.Core;
import es.noobcraft.core.api.database.SQLClient;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseLoader;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class JSONPhaseLoader implements PhaseLoader {
    private final SQLClient client = Core.getSQLClient();
    private final Map<String, Phase> phases = Maps.newHashMap();

    private final String GET_BLOCKS = "SELECT blocks FROM one_block_worlds WHERE name=?";
    private final String UPDATE_BLOCKS = "UPDATE one_block_worlds SET blocks=? WHERE name=?";
    @Override
    public void loadPhases() {
        final File directory = new File(Core.getServerConfigurationsDirectory() + "/phases");
        if (!directory.mkdirs()) {
            final File[] json = directory.listFiles(file -> file.getName().endsWith(".json") && !file.isDirectory());

            for (File file : json) {
                try (FileReader reader = new FileReader(file)) {
                    final BasePhase phase = OneBlockAPI.getGson().fromJson(reader, BasePhase.class);
                    phases.put(phase.getIdentifier(), phase);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Logger.log(LoggerType.CONSOLE, "Loaded "+ phases.size()+ " phases");
    }

    @Override
    public void unloadPhase(String identifier) {
        phases.remove(identifier);
    }

    @Override
    public void unsetPhases() {
        phases.clear();
    }

    @Override
    public int getPhaseBlocks(String world) {
        try(Connection connection = client.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(GET_BLOCKS)) {
                statement.setString(1, world);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) return resultSet.getInt("blocks");
                }
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updatePhaseBlocks(String world, int blocks) {
        try(Connection connection = client.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_BLOCKS)) {
                statement.setInt(1, blocks);
                statement.setString(2, world);
                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
