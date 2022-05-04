package es.noobcraft.oneblock.api.server;

import java.util.List;

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
}
