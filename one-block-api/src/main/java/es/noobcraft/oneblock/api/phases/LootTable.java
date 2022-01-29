package es.noobcraft.oneblock.api.phases;

import org.bukkit.inventory.ItemStack;

public interface LootTable {
    /**
     * Get the ItemStack ofh this lootTable
     * @return lootTable item
     */
    ItemStack getItem();

    /**
     * Get the probability to get this item
     * @return the item probability
     */
    double getProbability();

    /**
     * Get the max amount of items to roll
     * @return max item amount
     */
    int getMaxAmount();
}
