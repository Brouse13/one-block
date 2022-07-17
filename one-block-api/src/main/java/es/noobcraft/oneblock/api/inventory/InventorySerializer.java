package es.noobcraft.oneblock.api.inventory;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;


public interface InventorySerializer {
    /**
     * Serialize the itemStack content into a byte array.
     * @param content array with all the player inventory items
     * @return a byte array with the data encoded
     */
    byte[] serialize(ItemStack[] content);

    /**
     * Deserialize the content from a byte array and
     * return an array with the ItemStacks
     * @param bytes bytes with ItemStack data
     * @return an array with all the player inventory data
     * @throws IOException if the content can't be deserialized
     */
    ItemStack[] deserialize(byte[] bytes) throws IOException;
}
