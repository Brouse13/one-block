package es.noobcraft.oneblock.api.phases;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Phase {
    /**
     * Get the identifier of the phase, and must be unique.
     * @return the phase identifier
     */
    String getIdentifier();

    /**
     * Get the min score that the island has to have
     * to be in that phase
     * @return the phase min score
     */
    int getMinScore();

    /**
     * Get the max score that the island has to have
     * to upgrade to next phase
     * @return the phase max score
     */
    int getMaxScore();

    /**
     * Get all the available materials that can spawn
     * during that phase
     * @return the available materials
     */
    List<BlockType> getItems();

    /**
     * Get all the available entities that can spawn
     * during that phase
     * @return the available entities
     */
    List<MobType> getEntities();

    /**
     * Get all the available LooTables that can spawn
     * during that phase
     * @return the available LootTables
     */
    List<LootTable> getLootTables();

    /**
     * Get all the SpecialAction that will happen on the OneBlock
     * @return the available SpecialActions
     */
    Map<Integer, Set<SpecialActions>> getSpecialActions();
}
