package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import org.bukkit.Material;

import java.util.Set;

public interface OneBlockProfile {
    /**
     * Return the owner of this profile. If he
     * removes the profile all the coop players will
     * lose the access to the profile
     * @return profile region
     */
    OneBlockPlayer getOwner();

    /**
     * Get the profile name that is stored on the database
     * it corresponds to the database world name
     * @return the profile name
     */
    String getProfileName();

    /**
     * Get the name of the player who owns the island
     * @return island owner name
     */
    String getIslandOwner();

    /**
     * Get the player inventory encoded in a base64 string.
     * First element is player inventory, second its inventory
     * @return serialized inventory
     */
    byte[] getInventory();

    /**
     * Return all the settings to this profile
     * all of these are global settings.
     * The value -1 is all perms
     * @return profile settings
     */
    int getIslandPermissions();

    /**
     * Get the Material that appears on the menu
     * to represent the profile.
     * @return profile Material
     */
    Material getProfileItem();
}
