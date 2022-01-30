package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import org.bukkit.Material;

public interface OneBlockProfile {
    /**
     * Get the owner of this profile, it might be
     * different from the island owner.
     * @return profile owner
     */
    OneBlockPlayer getOwner();

    /**
     * Get the profile and world name.
     * @return the profile and world name
     */
    String getProfileName();

    /**
     *
     * @return
     */
    String getWorldName();

    /**
     * Get the name of the island owner.
     * @return island owner name
     */
    String getIslandOwner();

    /**
     * Get the player inventory and armour content
     * serialized in a byte array.
     * @return the serialized inventory
     */
    byte[] getInventory();

    /**
     * Set a new InventoryContent
     * @param content new content
     */
    void setInventory(byte[] content);

    /**
     * Get the profile Material that will display on the menu.
     * @return profile Material
     */
    Material getProfileItem();
}
