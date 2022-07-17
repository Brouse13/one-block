package es.noobcraft.oneblock.api.database;

import es.noobcraft.oneblock.api.profile.OneBlockProfile;


public interface PlayerLoader {
    /**
     * Create a new empty player with an
     * @param name player name
     * @param maxProfiles maxProfiles to player
     * @return the new player
     */
    OneBlockProfile createPlayer(String name, int maxProfiles);
}
