package es.noobcraft.oneblock.api.profile;

import es.noobcraft.oneblock.api.player.OneBlockPlayer;
import org.bukkit.World;

import java.util.Set;

public interface ProfileCache {
    /**
     * Get a profile from a specific player and a given name
     * @param player player profile owner
     * @param name profile name
     * @return the found profile
     */
    OneBlockProfile getProfile(OneBlockPlayer player, String name);

    /**
     * Get all the profile loaded on cache in a given world
     * @param world world to search
     * @return the found profile
     */
    Set<OneBlockProfile> getProfiles(World world);

    /**
     * Add a new profile to the cache profiles
     * @param profile profile to add
     * @return the operation status
     */
    boolean addProfile(OneBlockProfile profile);

    /**
     * Remove a specific profile from the cache
     * @param profile profile to remove
     * @return the operation status
     */
    boolean removeProfile(OneBlockProfile profile);
}
