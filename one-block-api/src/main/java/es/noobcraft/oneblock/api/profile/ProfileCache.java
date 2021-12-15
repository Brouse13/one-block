package es.noobcraft.oneblock.api.profile;

import java.util.Set;

public interface ProfileCache {
    /**
     * Get all the loaded profiles on the
     * cache. The data will be erased async
     * 2 minutes before the last player disconnect
     * @return set with all loaded cache profiles
     */
    Set<OneBlockProfile> getProfiles();

    /**
     * Add a profile to the cached profiles,
     * if all the island player disconnect
     * the Profile will expire in 2 minutes
     * @return the operation status
     */
    boolean addProfile(OneBlockProfile profile);

    /**
     * Force to remove a profile without waiting the cache
     * @param profile profile to remove
     * @return the operation status
     */
    boolean removeProfile(OneBlockProfile profile);
}
