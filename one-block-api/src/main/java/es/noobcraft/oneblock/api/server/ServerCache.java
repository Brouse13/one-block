package es.noobcraft.oneblock.api.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServerCache {
    /**
     * Get a map with all the worlds with its
     * respective server on the cache
     * @return the map with all the loaded worlds
     */
    Map<String, List<String>> getWorlds();

    /**
     * Add a new world to the cache
     * @param server server where the world is
     * @param world world name
     */
    void addWorld(String server, String world);

    /**
     * Remove a world from the cache
     * @param server server where the world is
     * @param world world to remove
     */
    void removeWorld(String server, String world);

    /**
     * Get the server where the world is loaded.
     * If the world isn't loaded it will return an
     * empty Optional
     * @param world world to check
     * @return an Optional with the server
     */
    Optional<String> getServer(String world);

    /**
     * Sync the world from the database
     */
    void syncWorlds();
}
