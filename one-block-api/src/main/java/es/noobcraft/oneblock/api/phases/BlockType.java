package es.noobcraft.oneblock.api.phases;

import es.noobcraft.oneblock.api.utils.Weigh;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface BlockType extends Weigh {
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
