package es.noobcraft.oneblock.api.player;

import java.util.Set;

public interface PlayerCache {
    /**
     * Return a set of all the online players on
     * cache.
     * @return set with all the players
     */
    Set<OneBlockPlayer> getPlayers();

    /**
     * Get the corresponding OneBlockPlayer with the
     * given name
     * @param name player name
     * @return OneBlock player
     */
    OneBlockPlayer getPlayer(String name);

    /**
     * Add a player to the player cache and
     * return the status of the operation.
     * @param name player username
     * @return the status of the operation
     */
    boolean addPlayer(String name);

    /**
     * Remove a player from the player cache and
     * return the status of the operation.
     * This method is called on NoobPlayerQuitEvent.
     * @param name player username
     * @return the status of the operation
     */
    boolean removePlayer(String name);
}
