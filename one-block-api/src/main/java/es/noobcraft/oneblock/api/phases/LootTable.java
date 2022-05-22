package es.noobcraft.oneblock.api.phases;

import es.noobcraft.oneblock.api.utils.WeighList;
import org.bukkit.World;


public interface LootTable {
    /**
     * Get the name of the lootTable that will appear
     * on the chest
     * @return lootTable name
     */
    String getName();

    /**
     * Get all the items from the lootTable
     * @return lootTable items
     */
    WeighList<LootTableItem> getItems();

    /**
     * Get the amount of items will be rolled on this lootTable
     * @return LootTable rolls
     */
    int getRolls();

    /**
     * Summon the given lootTable in the given world
     * @param world world to spawn
     */
    void summon(World world);
}
