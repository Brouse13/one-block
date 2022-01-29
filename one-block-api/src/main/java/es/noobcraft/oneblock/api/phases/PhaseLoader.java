package es.noobcraft.oneblock.api.phases;

public interface PhaseLoader {
    /**
     * Load all the phases from the database with the
     * respective Mobs, blocks and looTables
     */
    void loadPhases();

    /**
     * Unload a specific phase from the loaded phases
     * @param identifier phase identifier
     */
    void unloadPhase(String identifier);

    /**
     * Unset all the phases from the cache
     */
    void unsetPhases();

    /**
     * Get phase blocks from an island
     * @param world world name
     */
    int getPhaseBlocks(String world);

    /**
     * Update phase blocks from the databse
     */
    void updatePhaseBlocks(String world, int blocks);
}
