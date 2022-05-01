package es.noobcraft.oneblock.api.player;

public interface PlayerSupplier {
    /**
     * Create a new base OneBlockPlayer
     * @param username player username
     * @return the created OneBlockPlayer
     */
    OneBlockPlayer createPlayer(String username);

    /**
     * Create a new base OfflineOneBlockPlayer
     * @param username player username
     * @return the created OfflineOneBlockPlayer
     */
    OfflineOneBlockPlayer createOfflinePlayer(String username);
}
