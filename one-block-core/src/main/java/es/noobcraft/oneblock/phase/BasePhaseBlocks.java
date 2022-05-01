package es.noobcraft.oneblock.phase;

import com.google.common.collect.Lists;
import es.noobcraft.core.api.Core;
import es.noobcraft.oneblock.api.OneBlockAPI;
import es.noobcraft.oneblock.api.logger.Logger;
import es.noobcraft.oneblock.api.logger.LoggerType;
import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseBlocks;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class BasePhaseBlocks implements PhaseBlocks {
    private static final String GET_BLOCKS = "SELECT blocks FROM one_block_worlds WHERE name=?";
    private static final String SYNC_BLOCKS = "UPDATE one_block_worlds SET blocks=? WHERE name=?";

    @Getter private int blocks = 0;
    private final String world;

    public BasePhaseBlocks(String world) {
        this.world = world;

        //Load the world blocks from the database
        try(Connection connection = Core.getSQLClient().getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(GET_BLOCKS)) {
                //VALUES: name
                statement.setString(1, world);
                try(ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    blocks = resultSet.getInt("blocks");
                }
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, "Unable to get "+ world+ " blocks "+ exception.getMessage());
        }
    }

    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public Phase getPhase() {
        final Set<Phase> phases = OneBlockAPI.getPhaseLoader().getPhases();
        return phases.stream()
                .filter(phase -> blocks >= phase.getMinScore() && blocks < phase.getMaxScore())
                .findFirst().orElse(Lists.newArrayList(phases).get(phases.size() - 1));
    }

    @Override
    public void addBlock(int amount) {
        blocks += amount;
    }

    @Override
    public void removeBlock(int amount) {
        blocks -= amount;
    }

    @Override
    public void setBlocks(int amount) {
        blocks = amount;
    }

    @Override
    public void syncBlocks() {
        try (Connection connection = Core.getSQLClient().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SYNC_BLOCKS)) {
                //VALUES: blocks, name
                statement.setInt(1, blocks);
                statement.setString(2, world);
                statement.executeUpdate();
            }
        }catch (SQLException exception) {
            Logger.log(LoggerType.ERROR, "Unable to get " + world + " blocks " + exception.getMessage());
        }
    }
}
