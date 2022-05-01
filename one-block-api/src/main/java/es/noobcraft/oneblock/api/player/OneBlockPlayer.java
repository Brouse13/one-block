package es.noobcraft.oneblock.api.player;

import es.noobcraft.core.api.player.NoobPlayer;
import es.noobcraft.oneblock.api.profile.OneBlockProfile;
import org.bukkit.entity.Player;

public interface OneBlockPlayer extends OfflineOneBlockPlayer {

    /**
     * Get the current profile that the player is using.
     * This method can return null, if the player is visiting an
     * island or in the lobby.
     * @return the inUse player profile
     */
    OneBlockProfile getCurrentProfile();

    /**
     * Set the current profile to the player
     */
    void setCurrentProfile(OneBlockProfile profile);

    /**
     * Get the Bukkit player instance
     * @return the Bukkit player instance
     */
    Player getBukkitPlayer();

    /**
     * Get the player NoobPlayer instance
     * @return the NoobPlayer instance
     */
    NoobPlayer getNoobPlayer();

    /**
     * Teleport the player to the specific island
     * @param profile profile to teleport
     */
    void teleport(OneBlockProfile profile);
}
