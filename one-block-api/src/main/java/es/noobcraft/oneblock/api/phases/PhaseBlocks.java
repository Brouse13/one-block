package es.noobcraft.oneblock.api.phases;

public interface PhaseBlocks {
    /**
     * Get the name of the world of the PhaseBlock.
     * @return the world name
     */
    String getWorld();

    /**
     * Get the phase on that block
     * @return the phase block
     */
    Phase getPhase();

    /**
     * Get the amount of blocks broken on the server
     * @return the amount of broken blocks
     */
    int getBlocks();

    /**
     * Add to current profile broken blocks a
     * specific amount of blocks.
     * @param amount block amount
     */
    void addBlock(int amount);

    /**
     * Remove to current profile broken blocks a
     * specific amount of blocks.
     * @param amount block amount
     */
    void removeBlock(int amount);

    /**
     * Set the current profile broken blocks to
     * a specific amount of blocks.
     * @param amount block amount
     */
    void setBlocks(int amount);

    /**
     * Method to sync island broken blocks
     */
    void syncBlocks();
}
