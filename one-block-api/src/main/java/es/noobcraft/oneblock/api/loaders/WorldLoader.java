package es.noobcraft.oneblock.api.loaders;

public interface WorldLoader {
    /**
     * Create a new world using the SlimeWorldManager plugin.
     * @param worldName name of the world to create
     * @return the operation status
     */
    boolean createWorld(String worldName);

    /**
     * Load the given world into spigot throw
     * SlimeWorldManager plugin.
     * @param worldName world to load
     * @param locked if the world should be locked
     * @apiNote If the world is loaded it will return false
     * @return if the world was loaded
     */
    boolean loadWorld(String worldName, boolean locked);

    /**
     * Unload the given world from spigot and saved it on
     * the database using .slime format.
     * @param worldName world to unload
     * @apiNote if the world contains players it will return false
     * @return if the world was saved
     */
    boolean unloadWorld(String worldName);
}
