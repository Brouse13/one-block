package es.noobcraft.oneblock.api.profile;

public interface OneBlockProfileSettings {
    /**
     * Ger the max coop players that can be on
     * the island
     * @return max coop players
     */
    int getMaxCoops();

    /**
     * Get the max size of the island this
     * is used to represent the square region
     * @return island size
     */
    int getSize();
}
