package es.noobcraft.oneblock.api.phases;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface BlockType extends Probability {
    /**
     * Get the respective ItemStack if the Block
     * @return the block ItemStack
     */
    ItemStack getType();

    /**
     * Spawn a new Block on the given world
     * @param world world where the island is
     */
    void spawn(World world);
}
