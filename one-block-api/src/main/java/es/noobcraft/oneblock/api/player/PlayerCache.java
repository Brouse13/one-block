package es.noobcraft.oneblock.api.player;

import java.util.Set;

public interface PlayerCache {
    /**
     * Return a set of all the online players in cache.
     * @return all the oline players
     */
    Set<OneBlockPlayer> getPlayers();

    /**
     * Get the OneBlockPlayer loaded on the cache.
     * @param name player name
     * @return the OneBlockPlayer
     */
    OneBlockPlayer getPlayer(String name);

    /**
     * Add a new OneBlockPlayer to the cache and
     * return the status of the operation.
     * @param name OneBlockPlayer username
     * @return the operation status
     */
    boolean addPlayer(String name);

    /**
     * Remove a OneBlockPlayer from the cache and
     * return the status of the operation.
     * This method is called on NoobPlayerQuitEvent.
     * @param name OneBlockPlayer username
     * @return the operation status
     */
    boolean removePlayer(String name);
}
