package es.noobcraft.oneblock.api.flags;

public interface IslandPermissionManager {
    /**
     * Get the permission from a player on an
     * island.
     * @param worldName world name
     * @param username player name
     * @return the island permissions
     */
    int getPermission(String worldName, String username);

    /**
     * Add to the cache the permission of the
     * player in the given island.
     * @param worldName world name
     * @param username player username
     * @param permission island permissions
     * @return value associates previously
     */
    int addCache(String worldName, String username, int permission);

    /**
     * Remove from the cache the permission of the
     * player on the given island.
     * @param world world name
     * @param username player name
     * @return the value associated previously
     */
    int removeCache(String world, String username);

    /**
     * Create the default permissions to of the player in
     * the given island.
     * @param worldName world name
     * @param username player username
     * @param owner player is owner
     * @return the id of the perms
     */
    int createPerms(String worldName, String username, boolean owner);

    /**
     * Update given permission into the database
     * @param worldName world name
     * @param username player name
     * @param permission permission value
     */
    void updatePermission(String worldName, String username, int permission);
}
