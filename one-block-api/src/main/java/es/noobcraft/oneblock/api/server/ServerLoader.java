package es.noobcraft.oneblock.api.server;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;

import java.util.List;
import java.util.Optional;

public interface ServerLoader {
    /**
     * Get a map with all the worlds with its
     * respective server on the Database
     * @param server server where to get the worlds
     * @return the map with all the loaded worlds
     */
    List<String> getWorlds(String server);

    /**
     * Add a new world to the Database
     * @param server server where the world is
     * @param world world name
     */
    void addWorld(String server, String world);

    /**
     * Remove a world from the DataBase
     * @param server server where the world is
     * @param world world to remove
     */
    void removeWorld(String server, String world);

    /**
     * Send a request to the server to teleport the player to
     * the given profile on player connect
     * @param profile profile to teleport
     * @param server server name
     */
    void teleportRequest(OneBlockProfile profile, String server);

    /**
     * Return if the player has any pending teleport requests
     * on the given server.
     * It will return the world where player has to be
     * teleported if is present on the server
     * @param player player name
     * @param server server name
     * @return an Optional with the world to teleport, if is present
     */
    Optional<String> hasTeleportRequest(String player, String server);

    /**
     * Remove the teleportRequests from player if they exist
     * @param player player name
     */
    void removeTeleportRequest(String player);
}
