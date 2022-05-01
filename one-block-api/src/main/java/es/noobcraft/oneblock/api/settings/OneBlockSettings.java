package es.noobcraft.oneblock.api.settings;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public interface OneBlockSettings {
    /**
     * DefaultProfiles constant
     * @return the OneBlock defaultProfiles
     */
    int getDefaultProfiles();

    /**
     * DefaultMaterial constant
     * @return the OneBlock defaultMaterial
     */
    Material getDefaultMaterial();

    /**
     * DefaultIslandPermission constant
     * @return the OneBlock defaultIslandPermission
     */
    int getDefaultIslandPermission();

    /**
     * AddProfileTexture constant
     * @return the OneBlock addProfileTexture
     */
    String getAddProfileTexture();

    /**
     * RemoveProfileTexture constant
     * @return the OneBlock removeProfileTexture
     */
    String getRemoveProfileTexture();

    /**
     * LobbySpawn constant
     * @return the OneBlock lobbySpawn
     */
    Location getLobbySpawn();

    /**
     * IslandSpawn constant
     * @return the OneBlock islandSpawn
     */
    Vector getIslandSpawn();
}
