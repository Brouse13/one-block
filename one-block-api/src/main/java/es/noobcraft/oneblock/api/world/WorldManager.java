package es.noobcraft.oneblock.api.world;

public interface WorldManager {
    /**
     * Create a new SlimeWorld into the database.
     * If the world loading is set, it will be load
     * in non read mode.
     * @param name world name
     * @param load if the world should be loaded
     * @return the operation status
     */
    boolean createWorld(String name, boolean load);

    /**
     * Load a world from the database into Bukkit worlds
     * @param name world name
     * @param readOnly open in readOnly mode
     * @return the operation status
     */
    boolean loadWorld(String name, boolean readOnly);

    /**
     * Unload a SlimeWorld from Bukkit worlds
     * @param name SlimeWorld name
     * @return the operation status
     */
    boolean unloadWorld(String name);
}
