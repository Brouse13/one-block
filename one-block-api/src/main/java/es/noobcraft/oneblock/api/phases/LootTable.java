package es.noobcraft.oneblock.api.phases;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * LootTables interface V2
 */
public interface LootTable {
    /**
     * Get the name of the lootTable tat will apear
     * on the chest
     * @return lootTable name
     */
    String getName();

    /**
     * Get all the items from the lootTable
     * @return lootTable items
     */
    List<LootTableItem> getItems();

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

    interface LootTableItem {
        /**
         * Get the ItemStack ofh this lootTable
         * @return lootTable item
         */
        ItemStack getItem();

        /**
         * Get weigh of the item
         * @return items weigh
         */
        int getWeigh();

        /**
         * Get the chance of getting this LootTableItem
         * @return LootTableItem chance
         */
        double getChance();

        /**
         * Set the chance to get this LootTableItem
         * @param chance new chance
         */
        void setChance(double chance);
    }
}
