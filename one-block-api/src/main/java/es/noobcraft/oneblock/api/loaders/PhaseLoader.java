package es.noobcraft.oneblock.api.loaders;

import es.noobcraft.oneblock.api.phases.Phase;
import es.noobcraft.oneblock.api.phases.PhaseBlocks;

import java.util.Set;

public interface PhaseLoader {
    /**
     * Load all the phases from the database with the
     * respective Mobs, blocks and looTables
     */
    void loadPhases();

    /**
     * Get all the loaded phases
     * @return loaded phases
     */
    Set<Phase> getPhases();

    /**
     * Get the island blocks manager
     * @param world world to get the manager
     * @return the PhaseBlock manager
     */
    PhaseBlocks getPhaseBlocks(String world);
}
