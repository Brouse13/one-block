package es.noobcraft.oneblock.api.flags;

public interface IslandPermissionManager {
    /**
     * Get the permission of the island.
     * @param worldName world name
     * @return the island permission
     */
    int getPermission(String worldName);

    /**
     * Add the island perms to the cache.
     * @param worldName world name
     * @param permission island permissions
     * @return value associates previously
     */
    int addCache(String worldName, int permission);

    /**
     * Remove an island perms from the cache.
     * @param world world name
     */
    void removeCache(String world);

    /**
     * Create the default permissions for the given island.
     * @param worldName world name
     * @param owner player is owner
     * @return the id of the perms
     */
    int createPerms(String worldName, boolean owner);

    /**
     * Update permission into the database
     * @param worldName world name
     * @param permission permission value
     */
    void updatePermission(String worldName, int permission);
}
