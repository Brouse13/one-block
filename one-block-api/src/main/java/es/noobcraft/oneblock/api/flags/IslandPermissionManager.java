package es.noobcraft.oneblock.api.flags;

public interface IslandPermissionManager {
    /**
     * Get the permissions of an island stored
     * on the cache
     * @param world world name
     * @return the island permissions
     */
    int getPermission(String world);

    /**
     * Add to the cache the permission of the
     * given island
     * @param world world name
     * @param permission island permissions
     * @return value associates previously
     */
    int addCache(String world, int permission);

    /**
     * Add to the cache the permission of the
     * given island
     * @param world world name
     * @return the value associated previously
     */
    int removeCache(String world);

    /**
     * Update given permission into the database
     * @param world world name
     * @param permission permission value
     * @return the operation status
     */
    void updatePermission(String world, int permission);
}
